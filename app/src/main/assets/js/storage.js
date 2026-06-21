/**
 * Storage - 本地存储封装模块
 * 
 * 功能说明：
 * 1. 封装 localStorage 操作
 * 2. 提供 JSON 序列化/反序列化
 * 3. 支持过期时间
 * 4. 统一错误处理
 * 
 * 使用方式：
 * - Storage.get(key)
 * - Storage.set(key, value, expires)
 * - Storage.remove(key)
 * - Storage.clear()
 */

(function() {
    'use strict';

    /**
     * 存储模块
     */
    const Storage = {
        /**
         * 获取存储的值
         * 
         * @param {string} key - 存储键名
         * @param {*} defaultValue - 默认值（当键不存在时返回）
         * @returns {*} 存储的值
         */
        get: function(key, defaultValue = null) {
            try {
                const item = localStorage.getItem(key);
                
                if (item === null) {
                    return defaultValue;
                }
                
                // 尝试解析JSON
                try {
                    const parsed = JSON.parse(item);
                    
                    // 检查是否有过期时间
                    if (parsed && typeof parsed === 'object' && parsed.__expires__) {
                        if (Date.now() > parsed.__expires__) {
                            // 已过期，删除并返回默认值
                            localStorage.removeItem(key);
                            return defaultValue;
                        }
                        return parsed.__value__;
                    }
                    
                    return parsed;
                } catch (e) {
                    // 不是JSON，直接返回字符串
                    return item;
                }
            } catch (e) {
                console.error('Storage.get error:', e);
                return defaultValue;
            }
        },

        /**
         * 设置存储的值
         * 
         * @param {string} key - 存储键名
         * @param {*} value - 要存储的值
         * @param {number} expires - 过期时间（毫秒），可选
         * @returns {boolean} 是否成功
         */
        set: function(key, value, expires = null) {
            try {
                let storedValue;
                
                if (expires !== null) {
                    // 带过期时间的存储
                    storedValue = JSON.stringify({
                        __value__: value,
                        __expires__: Date.now() + expires
                    });
                } else {
                    // 普通存储
                    storedValue = typeof value === 'object' 
                        ? JSON.stringify(value) 
                        : String(value);
                }
                
                localStorage.setItem(key, storedValue);
                return true;
            } catch (e) {
                console.error('Storage.set error:', e);
                return false;
            }
        },

        /**
         * 删除存储的值
         * 
         * @param {string} key - 存储键名
         * @returns {boolean} 是否成功
         */
        remove: function(key) {
            try {
                localStorage.removeItem(key);
                return true;
            } catch (e) {
                console.error('Storage.remove error:', e);
                return false;
            }
        },

        /**
         * 清空所有存储
         * 
         * @returns {boolean} 是否成功
         */
        clear: function() {
            try {
                localStorage.clear();
                return true;
            } catch (e) {
                console.error('Storage.clear error:', e);
                return false;
            }
        },

        /**
         * 检查键是否存在
         * 
         * @param {string} key - 存储键名
         * @returns {boolean} 是否存在
         */
        has: function(key) {
            try {
                return localStorage.getItem(key) !== null;
            } catch (e) {
                console.error('Storage.has error:', e);
                return false;
            }
        },

        /**
         * 获取所有键名
         * 
         * @returns {Array} 键名数组
         */
        keys: function() {
            try {
                const keys = [];
                for (let i = 0; i < localStorage.length; i++) {
                    keys.push(localStorage.key(i));
                }
                return keys;
            } catch (e) {
                console.error('Storage.keys error:', e);
                return [];
            }
        },

        /**
         * 获取存储大小（字节）
         * 
         * @returns {number} 存储大小
         */
        size: function() {
            try {
                let total = 0;
                for (let i = 0; i < localStorage.length; i++) {
                    const key = localStorage.key(i);
                    total += key.length + localStorage.getItem(key).length;
                }
                return total;
            } catch (e) {
                console.error('Storage.size error:', e);
                return 0;
            }
        }
    };

    // 暴露到全局
    window.Storage = Storage;

})();
