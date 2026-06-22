/**
 * Theme - 主题切换、样式管理模块
 * 
 * 功能说明：
 * 1. 主题切换（深色/浅色模式）
 * 2. CSS变量管理
 * 3. 主题持久化存储
 * 4. 跟随系统主题
 * 5. 主题变更事件通知
 * 
 * 使用方式：
 * - Theme.init() - 初始化主题
 * - Theme.setTheme(themeName) - 设置主题
 * - Theme.getTheme() - 获取当前主题
 * - Theme.toggleTheme() - 切换主题
 * - Theme.onChange(callback) - 监听主题变化
 * 
 * 主题类型：
 * - 'dark' - 深色主题（默认，车机常用）
 * - 'light' - 浅色主题
 * - 'auto' - 跟随系统
 */

(function() {
    'use strict';

    // 主题常量
    const THEME_DARK = 'dark';
    const THEME_LIGHT = 'light';
    const THEME_AUTO = 'auto';

    // 存储键名
    const STORAGE_KEY = 'app_theme';

    // 主题变量定义（深色主题为默认，在CSS中定义）
    // 浅色主题变量会通过JS动态设置

    /**
     * 主题模块
     */
    const Theme = {
        // 当前主题
        currentTheme: THEME_DARK,
        
        // 主题变更监听器
        listeners: [],

        /**
         * 初始化主题
         * 从存储中读取主题设置，或使用系统主题
         */
        init: function() {
            // 从存储中读取主题设置
            const savedTheme = this.loadTheme();
            
            if (savedTheme) {
                this.setTheme(savedTheme, false);
            } else {
                // 默认使用深色主题（车机常用）
                this.setTheme(THEME_DARK, false);
            }

            // 监听系统主题变化（如果支持）
            if (window.matchMedia) {
                const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
                if (mediaQuery.addEventListener) {
                    mediaQuery.addEventListener('change', (e) => {
                        if (this.currentTheme === THEME_AUTO) {
                            this.applyTheme(e.matches ? THEME_DARK : THEME_LIGHT);
                        }
                    });
                }
            }
        },

        /**
         * 设置主题
         * 
         * @param {string} themeName - 主题名称（dark/light/auto）
         * @param {boolean} save - 是否保存到存储，默认true
         */
        setTheme: function(themeName, save = true) {
            const validThemes = [THEME_DARK, THEME_LIGHT, THEME_AUTO];
            
            if (!validThemes.includes(themeName)) {
                console.warn(`Theme.setTheme: 无效的主题名称 ${themeName}`);
                return false;
            }

            this.currentTheme = themeName;

            // 应用主题
            if (themeName === THEME_AUTO) {
                const isDark = this.isSystemDarkMode();
                this.applyTheme(isDark ? THEME_DARK : THEME_LIGHT);
            } else {
                this.applyTheme(themeName);
            }

            // 保存到存储
            if (save) {
                this.saveTheme(themeName);
            }

            // 通知监听器
            this.notifyListeners(themeName);

            return true;
        },

        /**
         * 获取当前主题
         * 
         * @returns {string} 当前主题名称
         */
        getTheme: function() {
            return this.currentTheme;
        },

        /**
         * 切换主题（深色/浅色）
         * 
         * @returns {string} 切换后的主题名称
         */
        toggleTheme: function() {
            const newTheme = this.currentTheme === THEME_DARK ? THEME_LIGHT : THEME_DARK;
            this.setTheme(newTheme);
            return newTheme;
        },

        /**
         * 应用主题到DOM
         * 
         * @param {string} themeName - 主题名称（dark/light）
         */
        applyTheme: function(themeName) {
            const root = document.documentElement;
            
            // 设置主题属性
            root.setAttribute('data-theme', themeName);
            
            // 添加/移除主题类名
            if (themeName === THEME_DARK) {
                root.classList.add('theme-dark');
                root.classList.remove('theme-light');
            } else {
                root.classList.add('theme-light');
                root.classList.remove('theme-dark');
            }

            // 可以在这里动态设置CSS变量来实现主题切换
            // 目前主要依赖CSS中的变量定义
        },

        /**
         * 检测系统是否为深色模式
         * 
         * @returns {boolean} 是否为深色模式
         */
        isSystemDarkMode: function() {
            if (window.matchMedia) {
                return window.matchMedia('(prefers-color-scheme: dark)').matches;
            }
            return false; // 默认返回false
        },

        /**
         * 从存储中加载主题
         * 
         * @returns {string|null} 保存的主题名称
         */
        loadTheme: function() {
            try {
                if (window.Storage) {
                    return Storage.get(STORAGE_KEY);
                } else {
                    return localStorage.getItem(STORAGE_KEY);
                }
            } catch (e) {
                console.warn('Theme.loadTheme: 读取主题设置失败', e);
                return null;
            }
        },

        /**
         * 保存主题到存储
         * 
         * @param {string} themeName - 主题名称
         */
        saveTheme: function(themeName) {
            try {
                if (window.Storage) {
                    Storage.set(STORAGE_KEY, themeName);
                } else {
                    localStorage.setItem(STORAGE_KEY, themeName);
                }
            } catch (e) {
                console.warn('Theme.saveTheme: 保存主题设置失败', e);
            }
        },

        /**
         * 添加主题变更监听器
         * 
         * @param {Function} callback - 回调函数 (themeName) => {}
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
         * 
         * @param {string} themeName - 当前主题名称
         */
        notifyListeners: function(themeName) {
            this.listeners.forEach(callback => {
                try {
                    callback(themeName);
                } catch (e) {
                    console.error('Theme.notifyListeners: 监听器执行失败', e);
                }
            });
        },

        /**
         * 获取可用的主题列表
         * 
         * @returns {Array} 主题列表
         */
        getAvailableThemes: function() {
            return [
                { id: THEME_DARK, name: '深色主题', icon: '🌙' },
                { id: THEME_LIGHT, name: '浅色主题', icon: '☀️' },
                { id: THEME_AUTO, name: '跟随系统', icon: '🔄' }
            ];
        }
    };

    // 暴露到全局
    window.Theme = Theme;

    // 暴露主题常量
    window.THEME_DARK = THEME_DARK;
    window.THEME_LIGHT = THEME_LIGHT;
    window.THEME_AUTO = THEME_AUTO;

})();
