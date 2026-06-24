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
 * 
 * @module AsyncCallbackManager
 * @version 1.0.0
 */
const AsyncCallbackManager = {
    /**
     * 存储所有已注册的回调函数
     * @type {Object.<string, Function>}
     */
    callbacks: {},

    /**
     * 回调ID计数器，用于生成唯一ID
     * @type {number}
     */
    counter: 0,

    /**
     * 生成唯一的回调ID
     * 
     * @return {string} 唯一的回调ID字符串
     */
    generateId: function () {
        return 'callback_' + (++this.counter);
    },

    /**
     * 注册回调函数
     * 
     * @param {Function} callback - 要注册的回调函数
     * @return {string} 回调ID
     */
    register: function (callback) {
        const id = this.generateId();
        this.callbacks[id] = callback;
        return id;
    },

    /**
     * 执行回调函数
     * 执行后自动删除回调，避免内存泄漏
     * 
     * @param {string} id - 回调ID
     * @param {*} result - 回调结果
     */
    execute: function (id, result) {
        if (this.callbacks[id]) {
            this.callbacks[id](result);
            delete this.callbacks[id];
        }
    }
};
