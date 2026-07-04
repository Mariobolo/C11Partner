/**
 * Bridge - Android WebViewBridge 交互封装模块
 * 
 * 功能说明：
 * 1. 管理与Android原生代码的异步通信
 * 2. 统一的回调管理机制
 * 3. 环境检测和兼容性处理
 * 4. 封装常用的Android接口调用
 * 
 * 使用方式：
 * - Bridge.isAvailable() - 检测Android环境是否可用
 * - Bridge.call(method, ...args) - 调用Android方法
 * - Bridge.callAsync(method, ...args) - 异步调用Android方法
 * - AsyncCallbackManager - 全局回调管理器
 */

(function() {
    'use strict';

    /**
     * 全局回调管理器
     * 
     * 功能说明：
     * 1. 管理所有异步回调函数
     * 2. 为每个回调生成唯一ID
     * 3. 执行回调后自动清理
     * 
     * 使用场景：
     * - 与Android原生代码的异步通信
     * - 处理需要等待返回结果的操作
     */
    const AsyncCallbackManager = {
        callbacks: {},
        timers: {},
        counter: 0,

        /**
         * 生成唯一的回调ID
         * 
         * @return 唯一的回调ID字符串
         */
        generateId: function () {
            return 'callback_' + (++this.counter);
        },

        /**
         * 注册回调函数
         * 
         * @param callback 要注册的回调函数
         * @param timeoutMs 可选，超时毫秒数，超时后自动清理回调
         * @return 回调ID
         */
        register: function (callback, timeoutMs) {
            const id = this.generateId();
            this.callbacks[id] = callback;
            if (timeoutMs && timeoutMs > 0) {
                this.timers[id] = setTimeout(function() {
                    delete AsyncCallbackManager.callbacks[id];
                    delete AsyncCallbackManager.timers[id];
                }, timeoutMs);
            }
            return id;
        },

        /**
         * 执行回调函数
         * 执行后自动删除回调，避免内存泄漏
         * 同时清理关联的定时器
         * 
         * @param id 回调ID
         * @param result 回调结果
         */
        execute: function (id, result) {
            if (this.callbacks[id]) {
                // 清理关联的定时器
                if (this.timers[id]) {
                    clearTimeout(this.timers[id]);
                    delete this.timers[id];
                }
                this.callbacks[id](result);
                delete this.callbacks[id];
            }
        }
    };

    /**
     * Bridge 模块
     */
    const Bridge = {
        /**
         * 检测Android环境是否可用
         * 
         * @returns {boolean} 是否在Android环境中
         */
        isAvailable: function() {
            return typeof Android !== 'undefined';
        },

        /**
         * 检测Android方法是否存在
         * 
         * @param {string} methodName - 方法名
         * @returns {boolean} 方法是否存在
         */
        hasMethod: function(methodName) {
            return this.isAvailable() && typeof Android[methodName] === 'function';
        },

        /**
         * 同步调用Android方法
         * 
         * @param {string} methodName - 方法名
         * @param {...*} args - 参数
         * @returns {*} 方法返回值
         */
        call: function(methodName, ...args) {
            if (!this.hasMethod(methodName)) {
                console.warn(`Bridge.call: 方法 ${methodName} 不存在`);
                return null;
            }
            try {
                return Android[methodName](...args);
            } catch (e) {
                console.error(`Bridge.call: 调用 ${methodName} 失败`, e);
                return null;
            }
        },

        /**
         * 异步调用Android方法（带回调）
         * 
         * @param {string} methodName - 方法名（Async后缀的方法）
         * @param {Function} callback - 回调函数
         * @param {...*} args - 参数
         */
        callAsync: function(methodName, callback, ...args) {
            if (!this.hasMethod(methodName)) {
                console.warn(`Bridge.callAsync: 方法 ${methodName} 不存在`);
                if (callback) callback(null);
                return;
            }
            try {
                const callbackId = AsyncCallbackManager.register(callback);
                Android[methodName](...args, callbackId);
            } catch (e) {
                console.error(`Bridge.callAsync: 调用 ${methodName} 失败`, e);
                if (callback) callback(null);
            }
        },

        /**
         * 调用Android方法（自动检测同步/异步）
         * 优先尝试Async版本，不存在则用同步版本
         * 
         * @param {string} baseName - 基础方法名（不带Async后缀）
         * @param {Function} callback - 回调函数（异步时使用）
         * @param {...*} args - 参数
         * @returns {*} 同步调用时返回结果，异步时返回undefined
         */
        invoke: function(baseName, callback, ...args) {
            const asyncName = baseName + 'Async';
            
            if (this.hasMethod(asyncName)) {
                // 异步方法
                this.callAsync(asyncName, callback, ...args);
                return undefined;
            } else if (this.hasMethod(baseName)) {
                // 同步方法
                const result = this.call(baseName, ...args);
                if (callback) callback(result);
                return result;
            } else {
                console.warn(`Bridge.invoke: 方法 ${baseName} 不存在`);
                if (callback) callback(null);
                return null;
            }
        },

        /**
         * 安全调用Android方法（带错误处理和默认值）
         * 
         * @param {string} methodName - 方法名
         * @param {*} defaultValue - 默认值
         * @param {...*} args - 参数
         * @returns {*} 方法返回值或默认值
         */
        safeCall: function(methodName, defaultValue = null, ...args) {
            if (!this.hasMethod(methodName)) {
                return defaultValue;
            }
            try {
                const result = Android[methodName](...args);
                return result !== undefined ? result : defaultValue;
            } catch (e) {
                console.error(`Bridge.safeCall: 调用 ${methodName} 失败`, e);
                return defaultValue;
            }
        }
    };

    // 暴露到全局（保持向后兼容）
    window.AsyncCallbackManager = AsyncCallbackManager;
    window.Bridge = Bridge;

    // 为了保持向后兼容，也把Bridge的一些方法暴露为全局函数
    // （如果需要的话，可以在这里添加）

})();
