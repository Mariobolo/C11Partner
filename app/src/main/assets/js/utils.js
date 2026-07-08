/**
 * Utils - 通用工具函数模块
 * 
 * 功能说明：
 * 1. Toast提示、消息提示等UI工具
 * 2. DOM操作工具函数
 * 3. 通用工具方法
 * 
 * 使用方式：
 * - 全局函数：showToast(), showSwipeHint(), showSuccessMessage(), showErrorMessage()
 * - 也可以通过 window.Utils 访问
 */

// 生产环境日志抑制：保留warn/error，抑制debug/log
(function() {
    var originalLog = console.log;
    var logCount = 0;
    var MAX_LOGS_PER_SECOND = 5;
    var lastLogReset = Date.now();
    console.log = function() {
        logCount++;
        if (logCount > MAX_LOGS_PER_SECOND) {
            return; // 超过频率限制，静默丢弃
        }
        originalLog.apply(console, arguments);
    };
    // 每秒重置计数
    window._logResetInterval = setInterval(function() { logCount = 0; lastLogReset = Date.now(); }, 1000);
})();

(function() {
    'use strict';

    /**
     * 显示滑动提示
     * 
     * @param {string} message - 提示消息
     */
    function showSwipeHint(message) {
        // 检查是否已存在提示元素，如果存在则移除
        const existingHint = document.getElementById('swipeHint');
        if (existingHint) {
            existingHint.remove();
        }

        // 创建提示元素
        const hintElement = document.createElement('div');
        hintElement.id = 'swipeHint';
        hintElement.textContent = message;
        hintElement.style.cssText = `
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background-color: rgba(0, 0, 0, 0.7);
            color: white;
            padding: 15px 25px;
            border-radius: 8px;
            font-size: 16px;
            z-index: 1000;
            pointer-events: none;
            opacity: 0;
            transition: opacity 0.3s;
            text-align: center;
            min-width: 120px;
        `;

        // 添加到页面
        document.body.appendChild(hintElement);

        // 显示提示
        setTimeout(() => {
            hintElement.style.opacity = '1';
        }, 10);

        // 1.5秒后隐藏并移除提示
        setTimeout(() => {
            hintElement.style.opacity = '0';
            setTimeout(() => {
                if (hintElement.parentNode) {
                    hintElement.parentNode.removeChild(hintElement);
                }
            }, 300);
        }, 1500);
    }

    /**
     * 显示Toast提示
     * 
     * @param {string} message - 提示消息
     * @param {number} duration - 显示时长（毫秒），默认2000ms
     */
    function showToast(message, duration = 2000) {
        // 检查是否已存在Toast元素，如果存在则移除
        const existingToast = document.getElementById('toastMessage');
        if (existingToast) {
            existingToast.remove();
        }

        // 创建Toast元素
        const toastElement = document.createElement('div');
        toastElement.id = 'toastMessage';
        toastElement.textContent = message;
        toastElement.style.cssText = `
            position: fixed;
            bottom: 100px;
            left: 50%;
            transform: translateX(-50%);
            background-color: rgba(0, 0, 0, 0.7);
            color: white;
            padding: 12px 20px;
            border-radius: 20px;
            font-size: 14px;
            z-index: 1000;
            pointer-events: none;
            opacity: 0;
            transition: opacity 0.3s;
            text-align: center;
            min-width: 100px;
            max-width: 80%;
            word-wrap: break-word;
        `;

        // 添加到页面
        document.body.appendChild(toastElement);

        // 显示Toast
        setTimeout(() => {
            toastElement.style.opacity = '1';
        }, 10);

        // 指定时间后隐藏并移除Toast
        setTimeout(() => {
            toastElement.style.opacity = '0';
            setTimeout(() => {
                if (toastElement.parentNode) {
                    toastElement.parentNode.removeChild(toastElement);
                }
            }, 300);
        }, duration);
    }

    /**
     * 显示成功消息
     * 
     * @param {string} message - 消息内容
     */
    function showSuccessMessage(message) {
        showMessage(message, '#4CAF50');
    }

    /**
     * 显示错误消息
     * 
     * @param {string} message - 消息内容
     */
    function showErrorMessage(message) {
        showMessage(message, '#F44336');
    }

    /**
     * 显示消息（内部方法）
     * 
     * @param {string} message - 消息内容
     * @param {string} backgroundColor - 背景颜色
     */
    function showMessage(message, backgroundColor) {
        // 创建消息元素
        const messageElement = document.createElement('div');
        messageElement.textContent = message;
        messageElement.style.cssText = `
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background-color: ${backgroundColor};
            color: white;
            padding: 15px 25px;
            border-radius: 5px;
            font-size: 16px;
            z-index: 10000;
            pointer-events: none;
            opacity: 0;
            transition: opacity 0.3s;
        `;

        // 添加到页面
        document.body.appendChild(messageElement);

        // 显示消息
        setTimeout(() => {
            messageElement.style.opacity = '1';
        }, 10);

        // 2秒后隐藏并移除消息
        setTimeout(() => {
            messageElement.style.opacity = '0';
            setTimeout(() => {
                if (messageElement.parentNode) {
                    messageElement.parentNode.removeChild(messageElement);
                }
            }, 300);
        }, 2000);
    }

    /**
     * 触屏元素点击后恢复失焦
     * @description 在触屏设备上，元素点击后会保持焦点状态导致样式异常，此函数确保点击后立即失焦
     * @param {HTMLElement} element - 需要失焦的元素
     */
    function blurAfterClick(element) {
        if (!element) return;
        const focusable = element.closest('button, [role="button"], .widget, .qsp-item, .quick-app-item, .nav-button, .dock-btn, .control-icon, .ac-control, .ac-hcs, .music-control-btn, .btn, .setting-item, .app-item, .switch-item, .automation-item');
        if (focusable && typeof focusable.blur === 'function') {
            try {
                focusable.blur();
            } catch (e) {}
        }
        if (document.activeElement && document.activeElement !== document.body) {
            try {
                document.activeElement.blur();
            } catch (e) {}
        }
    }

    /**
     * 清除所有元素的 :active 状态（解决触摸粘滞问题）
     */
    function clearActiveState() {
        const activeElements = document.querySelectorAll(':active');
        activeElements.forEach(el => {
            try { el.blur(); } catch (e) {}
        });
        
        if (document.activeElement && document.activeElement !== document.body) {
            try { document.activeElement.blur(); } catch (e) {}
        }
    }

    /**
     * 添加点击效果
     * 为元素添加点击时的缩放反馈，并在触摸后恢复失焦
     */
    function addClickEffect() {
        const clickableElements = document.querySelectorAll('.widget, .btn, .nav-button, .quick-app-item, .qsp-item');
        
        clickableElements.forEach(element => {
            element.addEventListener('mousedown', function() {
                this.style.transform = 'scale(0.98)';
                this.style.transition = 'transform 0.1s';
            });
            
            element.addEventListener('mouseup', function() {
                this.style.transform = '';
                this.style.transition = '';
                blurAfterClick(this);
            });
            
            element.addEventListener('mouseleave', function() {
                this.style.transform = '';
                this.style.transition = '';
                blurAfterClick(this);
            });
            
            element.addEventListener('touchstart', function() {
                this.style.transform = 'scale(0.98)';
                this.style.transition = 'transform 0.1s';
            }, { passive: true });
            
            element.addEventListener('touchend', function() {
                const el = this;
                el.style.transform = '';
                el.style.transition = '';
                blurAfterClick(el);
                clearActiveState();
            });
            
            element.addEventListener('touchcancel', function() {
                const el = this;
                el.style.transform = '';
                el.style.transition = '';
                blurAfterClick(el);
                clearActiveState();
            });
        });
    }

    /**
     * 格式化时间（MM:SS格式）
     * 
     * @param {number} seconds - 秒数
     * @returns {string} 格式化后的时间字符串
     */
    function formatTime(seconds) {
        if (isNaN(seconds) || seconds < 0) {
            return '00:00';
        }
        const mins = Math.floor(seconds / 60);
        const secs = Math.floor(seconds % 60);
        return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
    }

    /**
     * 防抖函数
     * 
     * @param {Function} func - 要防抖的函数
     * @param {number} wait - 等待时间（毫秒）
     * @returns {Function} 防抖后的函数
     */
    function debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }

    /**
     * 节流函数
     * 
     * @param {Function} func - 要节流的函数
     * @param {number} limit - 限制时间（毫秒）
     * @returns {Function} 节流后的函数
     */
    function throttle(func, limit) {
        let inThrottle;
        return function(...args) {
            if (!inThrottle) {
                func.apply(this, args);
                inThrottle = true;
                setTimeout(() => inThrottle = false, limit);
            }
        };
    }

    /**
     * 深拷贝对象
     * 
     * @param {*} obj - 要拷贝的对象
     * @returns {*} 深拷贝后的对象
     */
    function deepClone(obj) {
        if (obj === null || typeof obj !== 'object') {
            return obj;
        }
        if (obj instanceof Date) {
            return new Date(obj.getTime());
        }
        if (obj instanceof Array) {
            return obj.map(item => deepClone(item));
        }
        if (typeof obj === 'object') {
            const cloned = {};
            for (const key in obj) {
                if (obj.hasOwnProperty(key)) {
                    cloned[key] = deepClone(obj[key]);
                }
            }
            return cloned;
        }
    }

    /**
     * 全局触屏失焦处理
     * @description 在DOMContentLoaded后，为所有可点击元素添加点击后失焦处理
     */
    function initTouchBlur() {
        const isTouchDevice = ('ontouchstart' in window) || 
                              (navigator.maxTouchPoints > 0) || 
                              (navigator.msMaxTouchPoints > 0);
        
        if (!isTouchDevice) return;

        document.documentElement.classList.add('touch-device');
        
        const handleTouchEnd = function(e) {
            setTimeout(() => {
                blurAfterClick(e.target);
                clearActiveState();
            }, 30);

            const target = e.target;
            if (target && target.style) {
                target.style.pointerEvents = 'none';
                requestAnimationFrame(() => {
                    requestAnimationFrame(() => {
                        target.style.pointerEvents = '';
                    });
                });
            }
        };
        
        const handleClick = function(e) {
            setTimeout(() => {
                blurAfterClick(e.target);
                clearActiveState();
            }, 30);
        };
        
        document.addEventListener('touchend', handleTouchEnd, { passive: true });
        document.addEventListener('touchcancel', handleTouchEnd, { passive: true });
        document.addEventListener('click', handleClick);
    }

    // 暴露到全局（保持向后兼容）
    window.showSwipeHint = showSwipeHint;
    window.showToast = showToast;
    window.showSuccessMessage = showSuccessMessage;
    window.showErrorMessage = showErrorMessage;
    window.addClickEffect = addClickEffect;
    window.blurAfterClick = blurAfterClick;
    window.clearActiveState = clearActiveState;
    window.initTouchBlur = initTouchBlur;
    window.formatTime = formatTime;
    window.debounce = debounce;
    window.throttle = throttle;
    window.deepClone = deepClone;

    // 也暴露为模块对象
    window.Utils = {
        showSwipeHint,
        showToast,
        showSuccessMessage,
        showErrorMessage,
        addClickEffect,
        blurAfterClick,
        clearActiveState,
        initTouchBlur,
        formatTime,
        debounce,
        throttle,
        deepClone
    };

    // 页面加载完成后自动初始化触屏失焦处理
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initTouchBlur);
    } else {
        initTouchBlur();
    }

})();
