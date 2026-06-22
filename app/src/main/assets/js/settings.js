/**
 * Settings - 设置页面逻辑模块
 * 
 * 功能说明：
 * 1. 设置项的读取、保存、应用
 * 2. 设置页面的显示/隐藏控制
 * 3. 各项设置的管理（壁纸、组件、系统等）
 * 4. 设置变更事件通知
 * 
 * 使用方式：
 * - Settings.init() - 初始化设置
 * - Settings.get(key, defaultValue) - 获取设置值
 * - Settings.set(key, value) - 设置值
 * - Settings.show() - 显示设置页面
 * - Settings.hide() - 隐藏设置页面
 * - Settings.apply() - 应用设置
 * - Settings.onChange(callback) - 监听设置变化
 * 
 * 设置分类：
 * - wallpaper: 壁纸设置
 * - components: 组件配置
 * - system: 系统设置
 * - ui: UI设置
 */

(function() {
    'use strict';

    // 存储键名
    const STORAGE_KEY = 'app_settings';

    // 默认设置
    const DEFAULT_SETTINGS = {
        // 壁纸设置
        wallpaper: {
            type: 0,           // 0: 默认壁纸, 1: 必应壁纸, 2: 本地图片, 3: 本地视频, 4: 本地文件夹
            path: '',          // 壁纸路径
            carousel: false,   // 是否启用轮播
            interval: 15,      // 轮播间隔（秒）
            fillMode: 0        // 填充模式：0填充, 1包含, 2拉伸
        },
        // 组件配置
        components: {
            music: true,       // 音乐组件
            map: true,         // 地图组件
            app: true,         // APP组件
            tirePressure: true, // 胎压组件
            weather: true      // 天气组件
        },
        // 系统设置
        system: {
            launcher: false,   // 桌面自启
            bootGreeting: false, // 开机问候
            nightMode: false   // 夜间模式
        },
        // UI设置
        ui: {
            theme: 'dark',     // 主题：dark/light/auto
            animations: true,  // 是否启用动画
            fontScale: 1.0     // 字体缩放
        }
    };

    /**
     * 设置模块
     */
    const Settings = {
        // 当前设置
        settings: {},
        
        // 设置变更监听器
        listeners: [],
        
        // 设置弹窗元素
        modalElement: null,

        /**
         * 初始化设置
         * 从存储中加载设置，或使用默认值
         */
        init: function() {
            // 加载设置
            this.load();
            
            // 获取设置弹窗元素
            this.modalElement = document.getElementById('settingsModal');
            
            // 绑定关闭按钮事件
            this.bindEvents();
            
            console.log('Settings: 初始化完成');
        },

        /**
         * 加载设置
         * 从存储中读取，与默认值合并
         */
        load: function() {
            try {
                let savedSettings = null;
                
                // 尝试从存储中读取
                if (window.Storage) {
                    savedSettings = Storage.get(STORAGE_KEY);
                } else {
                    const saved = localStorage.getItem(STORAGE_KEY);
                    if (saved) {
                        savedSettings = JSON.parse(saved);
                    }
                }
                
                // 与默认设置合并
                this.settings = this.deepMerge(DEFAULT_SETTINGS, savedSettings || {});
                
            } catch (e) {
                console.warn('Settings.load: 加载设置失败，使用默认值', e);
                this.settings = this.deepClone(DEFAULT_SETTINGS);
            }
        },

        /**
         * 保存设置
         * 将当前设置保存到存储
         */
        save: function() {
            try {
                if (window.Storage) {
                    Storage.set(STORAGE_KEY, this.settings);
                } else {
                    localStorage.setItem(STORAGE_KEY, JSON.stringify(this.settings));
                }
                
                // 通知监听器
                this.notifyListeners();
                
                return true;
            } catch (e) {
                console.error('Settings.save: 保存设置失败', e);
                return false;
            }
        },

        /**
         * 获取设置值
         * 
         * @param {string} key - 设置键名，支持点号分隔的路径（如 'wallpaper.type'）
         * @param {*} defaultValue - 默认值
         * @returns {*} 设置值
         */
        get: function(key, defaultValue = null) {
            if (!key) {
                return this.deepClone(this.settings);
            }

            // 支持点号路径
            const keys = key.split('.');
            let value = this.settings;
            
            for (const k of keys) {
                if (value && typeof value === 'object' && k in value) {
                    value = value[k];
                } else {
                    return defaultValue;
                }
            }
            
            return value !== undefined ? value : defaultValue;
        },

        /**
         * 设置值
         * 
         * @param {string} key - 设置键名，支持点号分隔的路径
         * @param {*} value - 设置值
         * @param {boolean} autoSave - 是否自动保存，默认true
         * @returns {boolean} 是否成功
         */
        set: function(key, value, autoSave = true) {
            if (!key) {
                return false;
            }

            // 支持点号路径
            const keys = key.split('.');
            let target = this.settings;
            
            for (let i = 0; i < keys.length - 1; i++) {
                const k = keys[i];
                if (!target[k] || typeof target[k] !== 'object') {
                    target[k] = {};
                }
                target = target[k];
            }
            
            target[keys[keys.length - 1]] = value;

            // 自动保存
            if (autoSave) {
                this.save();
            }

            return true;
        },

        /**
         * 重置为默认设置
         * 
         * @param {string} category - 可选，指定重置的分类
         * @returns {boolean} 是否成功
         */
        reset: function(category = null) {
            if (category) {
                if (DEFAULT_SETTINGS[category]) {
                    this.settings[category] = this.deepClone(DEFAULT_SETTINGS[category]);
                    this.save();
                    return true;
                }
                return false;
            }
            
            this.settings = this.deepClone(DEFAULT_SETTINGS);
            this.save();
            return true;
        },

        /**
         * 显示设置页面
         */
        show: function() {
            if (this.modalElement) {
                this.modalElement.style.display = 'block';
            }
        },

        /**
         * 隐藏设置页面
         */
        hide: function() {
            if (this.modalElement) {
                this.modalElement.style.display = 'none';
            }
        },

        /**
         * 切换设置页面显示/隐藏
         * 
         * @returns {boolean} 切换后的显示状态
         */
        toggle: function() {
            if (this.modalElement) {
                const isVisible = this.modalElement.style.display !== 'none';
                if (isVisible) {
                    this.hide();
                    return false;
                } else {
                    this.show();
                    return true;
                }
            }
            return false;
        },

        /**
         * 应用设置
         * 将设置应用到实际的UI和功能
         */
        apply: function() {
            // 应用主题设置
            if (window.Theme && this.settings.ui.theme) {
                Theme.setTheme(this.settings.ui.theme);
            }

            // 应用组件显示设置
            this.applyComponentVisibility();

            // 应用壁纸设置
            this.applyWallpaperSettings();

            console.log('Settings: 设置已应用');
        },

        /**
         * 应用组件显示设置
         */
        applyComponentVisibility: function() {
            const components = this.settings.components;
            
            // 音乐组件
            const musicWidget = document.querySelector('.music-widget');
            if (musicWidget) {
                musicWidget.style.display = components.music ? '' : 'none';
            }
            
            // 地图组件
            const mapWidget = document.querySelector('.map-widget');
            if (mapWidget) {
                mapWidget.style.display = components.map ? '' : 'none';
            }
            
            // 可以继续添加其他组件的显示控制...
        },

        /**
         * 应用壁纸设置
         */
        applyWallpaperSettings: function() {
            // 壁纸设置的具体应用逻辑
            // 这部分可以后续与wallpaper模块对接
        },

        /**
         * 绑定事件
         */
        bindEvents: function() {
            // 关闭按钮
            const closeBtn = document.getElementById('closeSettings');
            if (closeBtn) {
                closeBtn.addEventListener('click', () => {
                    this.hide();
                });
            }

            // 点击遮罩层关闭
            if (this.modalElement) {
                this.modalElement.addEventListener('click', (e) => {
                    if (e.target === this.modalElement) {
                        this.hide();
                    }
                });
            }
        },

        /**
         * 添加设置变更监听器
         * 
         * @param {Function} callback - 回调函数 (settings) => {}
         * @returns {Function} 移除监听器的函数
         */
        onChange: function(callback) {
            if (typeof callback !== 'function') {
                return () => {};
            }

            this.listeners.push(callback);

            // 返回移除函数
            return () => {
                const index = this.listeners.indexOf(callback);
                if (index > -1) {
                    this.listeners.splice(index, 1);
                }
            };
        },

        /**
         * 通知所有监听器
         */
        notifyListeners: function() {
            const settings = this.deepClone(this.settings);
            this.listeners.forEach(callback => {
                try {
                    callback(settings);
                } catch (e) {
                    console.error('Settings.notifyListeners: 监听器执行失败', e);
                }
            });
        },

        /**
         * 获取所有设置分类
         * 
         * @returns {Array} 分类列表
         */
        getCategories: function() {
            return [
                { id: 'wallpaper', name: '壁纸设置', icon: '🖼️' },
                { id: 'components', name: '组件配置', icon: '🧩' },
                { id: 'system', name: '系统管理', icon: '⚙️' },
                { id: 'ui', name: '界面设置', icon: '🎨' }
            ];
        },

        // ==================== 工具函数 ====================

        /**
         * 深拷贝对象
         * 
         * @param {*} obj - 要拷贝的对象
         * @returns {*} 拷贝后的对象
         */
        deepClone: function(obj) {
            if (obj === null || typeof obj !== 'object') {
                return obj;
            }
            
            if (Array.isArray(obj)) {
                return obj.map(item => this.deepClone(item));
            }
            
            const cloned = {};
            for (const key in obj) {
                if (obj.hasOwnProperty(key)) {
                    cloned[key] = this.deepClone(obj[key]);
                }
            }
            return cloned;
        },

        /**
         * 深度合并对象
         * 
         * @param {object} target - 目标对象
         * @param {object} source - 源对象
         * @returns {object} 合并后的对象
         */
        deepMerge: function(target, source) {
            const result = this.deepClone(target);
            
            for (const key in source) {
                if (source.hasOwnProperty(key)) {
                    if (source[key] && typeof source[key] === 'object' && !Array.isArray(source[key])) {
                        result[key] = this.deepMerge(result[key] || {}, source[key]);
                    } else {
                        result[key] = this.deepClone(source[key]);
                    }
                }
            }
            
            return result;
        }
    };

    // 暴露到全局
    window.Settings = Settings;

})();
