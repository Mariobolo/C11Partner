/**
 * 3D壁纸档位桥接模块
 * 监听档位变化，控制3D场景中的长按交互
 * 
 * D档：每3秒触发一次长按，车辆持续运动
 * P档：停止长按，车辆静止
 */

(function() {
    'use strict';

    const TAG = '[GearBridge]';
    
    // 长按间隔（毫秒）
    const LONG_PRESS_INTERVAL = 3000;
    // 长按持续时间（毫秒）- 比间隔略长，确保无缝衔接
    const LONG_PRESS_DURATION = 3500;
    
    // 状态
    let currentGear = 4; // 默认P档
    let longPressTimer = null;
    let isRunning = false;
    
    /**
     * 获取iframe中的InteractionController
     */
    function getInteractionController() {
        const iframe = document.getElementById('iframe-wallpaper');
        if (!iframe || !iframe.contentWindow) return null;
        
        // InteractionController 挂载在 iframe 的 window 上
        return iframe.contentWindow.interactionController || null;
    }
    
    /**
     * 触发一次长按
     */
    function triggerLongPress() {
        const controller = getInteractionController();
        if (!controller) {
            console.warn(TAG, 'InteractionController 不可用');
            return;
        }
        
        console.log(TAG, '触发长按');
        controller.simulateLongPress(LONG_PRESS_DURATION);
    }
    
    /**
     * 开始持续长按（D档）
     */
    function startContinuousLongPress() {
        if (isRunning) return;
        isRunning = true;
        
        console.log(TAG, '开始持续长按模式');
        
        // 立即触发第一次
        triggerLongPress();
        
        // 设置定时器，每3秒触发一次
        longPressTimer = setInterval(function() {
            triggerLongPress();
        }, LONG_PRESS_INTERVAL);
    }
    
    /**
     * 停止持续长按（P档）
     */
    function stopContinuousLongPress() {
        if (!isRunning) return;
        isRunning = false;
        
        console.log(TAG, '停止持续长按模式');
        
        if (longPressTimer) {
            clearInterval(longPressTimer);
            longPressTimer = null;
        }
        
        // 确保停止当前长按
        const controller = getInteractionController();
        if (controller && controller.longPressTimer !== null) {
            clearTimeout(controller.longPressTimer);
            if (window.i && window.i.v) {
                window.i.v.presssed = false;
            }
            controller.longPressTimer = null;
        }
    }
    
    /**
     * 处理档位变化
     * @param {number} newGear - 新档位 (1=R, 2=N, 3=D, 4=P)
     */
    function onGearChanged(newGear) {
        const oldGear = currentGear;
        currentGear = newGear;
        
        console.log(TAG, `档位变化: ${oldGear} -> ${newGear}`);
        
        // D档(3)：开始持续长按
        if (newGear === 3) {
            startContinuousLongPress();
        }
        // P档(4)或其他档位：停止持续长按
        else {
            stopContinuousLongPress();
        }
    }
    
    /**
     * 监听车辆状态更新
     * 拦截 updateCarState 来捕获档位变化
     */
    function setupGearListener() {
        // 保存原始的 updateCarState
        const originalUpdateCarState = window.updateCarState;
        
        // 重写 updateCarState
        window.updateCarState = function(stateJson) {
            // 调用原始函数
            if (originalUpdateCarState) {
                originalUpdateCarState(stateJson);
            }
            
            // 解析档位
            try {
                const state = typeof stateJson === 'string' ? JSON.parse(stateJson) : stateJson;
                if (state && state.gear !== undefined) {
                    onGearChanged(state.gear);
                }
            } catch (e) {
                console.error(TAG, '解析车辆状态失败:', e);
            }
        };
        
        console.log(TAG, '档位监听器已设置');
    }
    
    /**
     * 检查当前是否使用iframe壁纸
     */
    function isIframeWallpaperActive() {
        const iframe = document.getElementById('iframe-wallpaper');
        return iframe && iframe.src && iframe.src !== '';
    }
    
    /**
     * 初始化
     */
    function init() {
        // 等待 iframe 加载完成
        const checkIframe = setInterval(function() {
            const iframe = document.getElementById('iframe-wallpaper');
            if (iframe && iframe.contentWindow) {
                clearInterval(checkIframe);
                console.log(TAG, 'iframe壁纸已检测到，初始化档位桥接');
                setupGearListener();
            }
        }, 1000);
        
        // 30秒后停止检查
        setTimeout(function() {
            clearInterval(checkIframe);
        }, 30000);
    }
    
    // 暴露到全局，供外部调用
    window.GearBridge = {
        onGearChanged: onGearChanged,
        startContinuousLongPress: startContinuousLongPress,
        stopContinuousLongPress: stopContinuousLongPress,
        triggerLongPress: triggerLongPress,
        init: init
    };
    
    // 页面加载后初始化
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }
    
    console.log(TAG, '模块已加载');
})();
