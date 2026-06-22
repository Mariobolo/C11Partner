/**
 * App - 应用入口、初始化逻辑模块
 * 
 * 功能说明：
 * 1. 统一的应用初始化流程
 * 2. 各模块的启动和协调
 * 3. 全局事件绑定
 * 4. 应用生命周期管理
 * 5. 模块间通信协调
 * 
 * 使用方式：
 * - App.init() - 初始化应用
 * - App.ready(callback) - 应用就绪回调
 * - App.getModule(name) - 获取模块实例
 * - App.on(event, callback) - 监听应用事件
 * - App.emit(event, data) - 触发应用事件
 * 
 * 初始化顺序：
 * 1. 基础模块：Utils, Storage, Bridge
 * 2. 核心模块：Theme, Widgets
 * 3. 业务模块：Music, Settings
 * 4. 应用入口：App
 */

(function() {
    'use strict';

    /**
     * 应用模块
     */
    const App = {
        // 应用状态
        state: {
            initialized: false,
            ready: false,
            version: '1.2.0',
            startTime: null
        },

        // 已注册的模块
        modules: {},

        // 事件监听器
        eventListeners: {},

        // 就绪回调队列
        readyCallbacks: [],

        /**
         * 初始化应用
         * 按顺序初始化所有模块
         * 
         * @returns {Promise} 初始化Promise
         */
        init: function() {
            if (this.state.initialized) {
                console.warn('App.init: 应用已经初始化过了');
                return Promise.resolve();
            }

            this.state.startTime = Date.now();
            console.log('App: 开始初始化...');

            return new Promise((resolve, reject) => {
                try {
                    // 1. 初始化基础模块
                    this.initBaseModules();

                    // 2. 初始化核心模块
                    this.initCoreModules();

                    // 3. 初始化业务模块
                    this.initBusinessModules();

                    // 4. 绑定全局事件
                    this.bindGlobalEvents();

                    // 5. 标记初始化完成
                    this.state.initialized = true;
                    this.state.ready = true;

                    // 执行就绪回调
                    this.executeReadyCallbacks();

                    const initTime = Date.now() - this.state.startTime;
                    console.log(`App: 初始化完成，耗时 ${initTime}ms`);

                    // 触发ready事件
                    this.emit('ready', { initTime: initTime });

                    resolve();

                } catch (e) {
                    console.error('App.init: 初始化失败', e);
                    this.emit('error', { error: e });
                    reject(e);
                }
            });
        },

        /**
         * 初始化基础模块
         */
        initBaseModules: function() {
            console.log('App: 初始化基础模块...');

            // Utils - 通用工具函数
            if (window.Utils) {
                this.registerModule('utils', window.Utils);
                console.log('App: Utils 模块已加载');
            }

            // Storage - 本地存储
            if (window.Storage) {
                this.registerModule('storage', window.Storage);
                console.log('App: Storage 模块已加载');
            }

            // Bridge - Android交互
            if (window.Bridge) {
                this.registerModule('bridge', window.Bridge);
                console.log('App: Bridge 模块已加载');
            }
        },

        /**
         * 初始化核心模块
         */
        initCoreModules: function() {
            console.log('App: 初始化核心模块...');

            // Theme - 主题管理
            if (window.Theme) {
                this.registerModule('theme', window.Theme);
                if (typeof Theme.init === 'function') {
                    Theme.init();
                }
                console.log('App: Theme 模块已初始化');
            }

            // Widgets - 小组件管理
            if (window.Widgets) {
                this.registerModule('widgets', window.Widgets);
                console.log('App: Widgets 模块已加载');
            }
        },

        /**
         * 初始化业务模块
         */
        initBusinessModules: function() {
            console.log('App: 初始化业务模块...');

            // Settings - 设置管理
            if (window.Settings) {
                this.registerModule('settings', window.Settings);
                if (typeof Settings.init === 'function') {
                    Settings.init();
                }
                console.log('App: Settings 模块已初始化');
            }

            // Music - 音乐播放器
            // 注意：music.js已有独立实现，这里只注册引用
            if (typeof updateMusicVisualization === 'function') {
                this.registerModule('music', {
                    updateVisualization: updateMusicVisualization,
                    // 后续可以逐步整合更多音乐功能
                });
                console.log('App: Music 模块已注册');
            }
        },

        /**
         * 注册模块
         * 
         * @param {string} name - 模块名称
         * @param {object} module - 模块实例
         */
        registerModule: function(name, module) {
            if (!name || !module) {
                return false;
            }

            this.modules[name] = module;
            return true;
        },

        /**
         * 获取模块实例
         * 
         * @param {string} name - 模块名称
         * @returns {object|null} 模块实例
         */
        getModule: function(name) {
            return this.modules[name] || null;
        },

        /**
         * 检查模块是否存在
         * 
         * @param {string} name - 模块名称
         * @returns {boolean} 是否存在
         */
        hasModule: function(name) {
            return !!this.modules[name];
        },

        /**
         * 获取所有已注册的模块名称
         * 
         * @returns {Array} 模块名称列表
         */
        getModuleNames: function() {
            return Object.keys(this.modules);
        },

        /**
         * 绑定全局事件
         */
        bindGlobalEvents: function() {
            // 页面加载完成事件
            if (document.readyState === 'complete' || document.readyState === 'interactive') {
                // 页面已经就绪
            } else {
                document.addEventListener('DOMContentLoaded', () => {
                    this.emit('domReady');
                });
            }

            // 窗口大小变化事件
            window.addEventListener('resize', () => {
                this.emit('resize', {
                    width: window.innerWidth,
                    height: window.innerHeight
                });
            });

            // 页面可见性变化事件
            document.addEventListener('visibilitychange', () => {
                this.emit('visibilityChange', {
                    visible: !document.hidden
                });
            });

            // 错误事件
            window.addEventListener('error', (e) => {
                this.emit('error', {
                    message: e.message,
                    filename: e.filename,
                    lineno: e.lineno,
                    colno: e.colno,
                    error: e.error
                });
            });

            console.log('App: 全局事件已绑定');
        },

        /**
         * 添加应用就绪回调
         * 如果应用已经就绪，立即执行
         * 
         * @param {Function} callback - 回调函数
         */
        ready: function(callback) {
            if (typeof callback !== 'function') {
                return;
            }

            if (this.state.ready) {
                // 已经就绪，立即执行
                try {
                    callback(this);
                } catch (e) {
                    console.error('App.ready: 回调执行失败', e);
                }
            } else {
                // 加入队列
                this.readyCallbacks.push(callback);
            }
        },

        /**
         * 执行所有就绪回调
         */
        executeReadyCallbacks: function() {
            const callbacks = this.readyCallbacks.slice();
            this.readyCallbacks = [];

            callbacks.forEach(callback => {
                try {
                    callback(this);
                } catch (e) {
                    console.error('App.executeReadyCallbacks: 回调执行失败', e);
                }
            });
        },

        /**
         * 监听应用事件
         * 
         * @param {string} event - 事件名称
         * @param {Function} callback - 回调函数
         * @returns {Function} 移除监听器的函数
         */
        on: function(event, callback) {
            if (!event || typeof callback !== 'function') {
                return () => {};
            }

            if (!this.eventListeners[event]) {
                this.eventListeners[event] = [];
            }

            this.eventListeners[event].push(callback);

            // 返回移除函数
            return () => {
                this.off(event, callback);
            };
        },

        /**
         * 移除事件监听器
         * 
         * @param {string} event - 事件名称
         * @param {Function} callback - 回调函数
         */
        off: function(event, callback) {
            if (!event || !this.eventListeners[event]) {
                return;
            }

            const index = this.eventListeners[event].indexOf(callback);
            if (index > -1) {
                this.eventListeners[event].splice(index, 1);
            }
        },

        /**
         * 触发应用事件
         * 
         * @param {string} event - 事件名称
         * @param {*} data - 事件数据
         */
        emit: function(event, data) {
            if (!event || !this.eventListeners[event]) {
                return;
            }

            const listeners = this.eventListeners[event].slice();
            listeners.forEach(callback => {
                try {
                    callback(data);
                } catch (e) {
                    console.error(`App.emit: 事件 ${event} 的监听器执行失败`, e);
                }
            });
        },

        /**
         * 获取应用状态
         * 
         * @returns {object} 应用状态
         */
        getState: function() {
            return {
                initialized: this.state.initialized,
                ready: this.state.ready,
                version: this.state.version,
                startTime: this.state.startTime,
                moduleCount: Object.keys(this.modules).length,
                modules: Object.keys(this.modules)
            };
        },

        /**
         * 获取应用版本
         * 
         * @returns {string} 版本号
         */
        getVersion: function() {
            return this.state.version;
        },

        /**
         * 检查应用是否就绪
         * 
         * @returns {boolean} 是否就绪
         */
        isReady: function() {
            return this.state.ready;
        },

        /**
         * 销毁应用
         * 清理资源和事件监听器
         */
        destroy: function() {
            console.log('App: 正在销毁...');

            // 清空事件监听器
            this.eventListeners = {};
            this.readyCallbacks = [];

            // 清空模块
            this.modules = {};

            // 重置状态
            this.state.initialized = false;
            this.state.ready = false;

            this.emit('destroy');

            console.log('App: 已销毁');
        }
    };

    // 暴露到全局
    window.App = App;

    // 自动初始化（可选，根据需要开启）
    // 如果需要手动控制初始化时机，可以注释掉这行
    // App.init();

})();
