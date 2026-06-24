/**
 * @fileoverview 快捷开关管理器模块
 * @module QuickSwitchManager
 * @description 管理快捷开关面板的显示和控制，包含14个快捷开关、6个驾驶模式、5个场景模式
 * @version 1.0.0
 */

/**
 * 快捷开关管理器
 * 管理快捷开关面板的显示和控制
 */
const QuickSwitchManager = {
    /** @type {boolean} 面板是否打开 */
    isOpen: false,
    
    /**
     * 开关列表
     * @type {Array<{id: string, name: string, icon: string, type: string}>}
     */
    switches: [
        { id: 'lowBeamLight', name: '近光灯', icon: '💡', type: 'toggle' },
        { id: 'rearFogLight', name: '后雾灯', icon: '🌫️', type: 'toggle' },
        { id: 'positionLight', name: '示廓灯', icon: '🚨', type: 'toggle' },
        { id: 'pedestrianAlert', name: '行人警示', icon: '🔔', type: 'toggle' },
        { id: 'maxCooling', name: '极速制冷', icon: '❄️', type: 'toggle' },
        { id: 'nightMode', name: '夜间模式', icon: '🌙', type: 'toggle' },
        { id: 'wifi', name: 'WiFi', icon: '📶', type: 'toggle' },
        { id: 'bluetooth', name: '蓝牙', icon: '📱', type: 'toggle' },
        { id: 'videoWhileDriving', name: '行驶视频', icon: '🎬', type: 'toggle' },
        { id: 'cameraOverspeed', name: '360限速', icon: '📸', type: 'toggle' },
        { id: 'ambientLight', name: '氛围灯', icon: '🌈', type: 'toggle' },
        { id: 'speech', name: '语音播报', icon: '🔊', type: 'toggle' },
        { id: 'secondaryScreen', name: '副屏', icon: '📺', type: 'toggle' },
        { id: 'screenPresentation', name: '副屏投屏', icon: '🖥️', type: 'toggle' },
    ],
    
    /**
     * 驾驶模式列表
     * @type {Array<{id: number, name: string, icon: string}>}
     */
    driveModes: [
        { id: 0, name: '舒适', icon: '🛋️' },
        { id: 1, name: '运动', icon: '🏃' },
        { id: 2, name: '自定义', icon: '⚙️' },
        { id: 3, name: '极致', icon: '⚡' },
        { id: 4, name: '经济', icon: '🌱' },
        { id: 5, name: '零跑', icon: '🏎️' },
    ],
    
    /**
     * 场景模式列表
     * @type {Array<{id: string, name: string, icon: string}>}
     */
    sceneModes: [
        { id: 'guard', name: '守护模式', icon: '🛡️' },
        { id: 'rest', name: '小憩模式', icon: '😴' },
        { id: 'camping', name: '露营模式', icon: '⛺' },
        { id: 'powerSave', name: '省电模式', icon: '🔋' },
        { id: 'sentinel', name: '哨兵模式', icon: '👁️' },
    ],
    
    /**
     * 切换快捷开关面板
     * @description 切换快捷开关面板的显示/隐藏状态
     */
    togglePanel: function() {
        this.isOpen = !this.isOpen;
        if (this.isOpen) {
            this.showPanel();
        } else {
            this.hidePanel();
        }
    },
    
    /**
     * 显示快捷开关面板
     * @description 显示快捷开关面板，如不存在则创建
     */
    showPanel: function() {
        let panel = document.getElementById('quickSwitchPanel');
        if (!panel) {
            panel = this.createPanel();
            document.body.appendChild(panel);
        }
        panel.style.display = 'block';
        this.refreshSwitchStates();
    },
    
    /**
     * 隐藏快捷开关面板
     * @description 隐藏快捷开关面板
     */
    hidePanel: function() {
        const panel = document.getElementById('quickSwitchPanel');
        if (panel) {
            panel.style.display = 'none';
        }
        this.isOpen = false;
    },
    
    /**
     * 创建快捷开关面板
     * @description 创建快捷开关面板DOM元素并绑定事件
     * @returns {HTMLElement} 创建的面板元素
     */
    createPanel: function() {
        const panel = document.createElement('div');
        panel.id = 'quickSwitchPanel';
        panel.className = 'quick-switch-panel';
        panel.style.cssText = `
            position: fixed;
            top: 50px;
            right: 20px;
            width: 360px;
            max-height: 80vh;
            background: rgba(0, 0, 0, 0.9);
            border-radius: 16px;
            padding: 20px;
            z-index: 9999;
            overflow-y: auto;
            backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.1);
        `;
        
        let html = '<h3 style="color: white; margin: 0 0 16px 0; font-size: 18px;">快捷控制</h3>';
        
        // 快捷开关网格
        html += '<div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 12px; margin-bottom: 20px;">';
        this.switches.forEach(sw => {
            html += `
                <div class="switch-item" data-id="${sw.id}" 
                     style="text-align: center; padding: 12px 8px; background: rgba(255,255,255,0.1); border-radius: 12px; cursor: pointer; transition: all 0.2s;">
                    <div style="font-size: 24px; margin-bottom: 4px;">${sw.icon}</div>
                    <div style="color: white; font-size: 12px;">${sw.name}</div>
                    <div class="switch-status" style="margin-top: 4px; font-size: 10px; color: #888;">--</div>
                </div>
            `;
        });
        html += '</div>';
        
        // 驾驶模式
        html += '<h4 style="color: white; margin: 16px 0 12px 0; font-size: 14px;">驾驶模式</h4>';
        html += '<div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 8px; margin-bottom: 20px;">';
        this.driveModes.forEach(mode => {
            html += `
                <div class="drive-mode-item" data-id="${mode.id}"
                     style="text-align: center; padding: 10px 6px; background: rgba(255,255,255,0.1); border-radius: 10px; cursor: pointer; font-size: 12px; color: white;">
                    <div style="font-size: 20px;">${mode.icon}</div>
                    <div>${mode.name}</div>
                </div>
            `;
        });
        html += '</div>';
        
        // 场景模式
        html += '<h4 style="color: white; margin: 16px 0 12px 0; font-size: 14px;">场景模式</h4>';
        html += '<div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 8px;">';
        this.sceneModes.forEach(mode => {
            html += `
                <div class="scene-mode-item" data-id="${mode.id}"
                     style="text-align: center; padding: 10px 6px; background: rgba(255,255,255,0.1); border-radius: 10px; cursor: pointer; font-size: 12px; color: white;">
                    <div style="font-size: 20px;">${mode.icon}</div>
                    <div>${mode.name}</div>
                </div>
            `;
        });
        html += '</div>';
        
        // 设置入口
        html += '<h4 style="color: white; margin: 16px 0 12px 0; font-size: 14px;">系统</h4>';
        html += '<div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 8px;">';
        html += `
            <div class="settings-entry-item" 
                 style="text-align: center; padding: 10px 6px; background: rgba(255,255,255,0.1); border-radius: 10px; cursor: pointer; font-size: 12px; color: white; transition: all 0.2s;"
                 onmouseover="this.style.background='rgba(255,255,255,0.2)'"
                 onmouseout="this.style.background='rgba(255,255,255,0.1)'">
                <div style="font-size: 20px;">⚙️</div>
                <div>设置</div>
            </div>
        `;
        html += '</div>';
        
        panel.innerHTML = html;
        
        // 绑定开关点击事件
        panel.querySelectorAll('.switch-item').forEach(item => {
            item.addEventListener('click', () => {
                this.toggleSwitch(item.dataset.id);
            });
        });
        
        // 绑定驾驶模式点击事件
        panel.querySelectorAll('.drive-mode-item').forEach(item => {
            item.addEventListener('click', () => {
                this.setDriveMode(parseInt(item.dataset.id));
            });
        });
        
        // 绑定场景模式点击事件
        panel.querySelectorAll('.scene-mode-item').forEach(item => {
            item.addEventListener('click', () => {
                this.toggleSceneMode(item.dataset.id);
            });
        });
        
        // 绑定设置入口点击事件
        const settingsEntry = panel.querySelector('.settings-entry-item');
        if (settingsEntry) {
            const self = this;
            settingsEntry.addEventListener('click', function(e) {
                e.stopPropagation();
                self.hidePanel();
                setTimeout(() => {
                    const settingsModal = document.getElementById('settingsModal');
                    if (settingsModal) {
                        settingsModal.classList.add('active');
                        // 确保设置面板在最上层
                        settingsModal.style.zIndex = '99999';
                    }
                }, 100);
            });
        }
        
        return panel;
    },
    
    /**
     * 切换开关
     * @description 切换指定开关的状态
     * @param {string} switchId - 开关ID
     */
    toggleSwitch: function(switchId) {
        if (!window.AndroidInterface) {
            console.warn('AndroidInterface不可用');
            return;
        }
        
        try {
            let result = false;
            switch (switchId) {
                case 'lowBeamLight':
                    // 先获取当前状态再切换
                    result = window.AndroidInterface.setLowBeamLight(true); // 简化处理，实际需要读取状态
                    break;
                case 'rearFogLight':
                    result = window.AndroidInterface.setRearFogLight(true);
                    break;
                case 'positionLight':
                    result = window.AndroidInterface.setPositionLight(true);
                    break;
                case 'pedestrianAlert':
                    result = window.AndroidInterface.setPedestrianAlert(true);
                    break;
                case 'maxCooling':
                    result = window.AndroidInterface.setMaxCooling(true);
                    break;
                case 'nightMode':
                    result = window.AndroidInterface.setNightMode(true);
                    break;
                case 'wifi':
                    result = window.AndroidInterface.setWifiEnabled(true);
                    break;
                case 'bluetooth':
                    result = window.AndroidInterface.setBluetoothEnabled(true);
                    break;
                case 'videoWhileDriving':
                    const videoEnabled = window.AndroidInterface.isVideoWhileDrivingEnabled();
                    result = window.AndroidInterface.setVideoWhileDriving(!videoEnabled);
                    break;
                case 'cameraOverspeed':
                    const overspeedEnabled = window.AndroidInterface.isCameraOverspeedLimitEnabled();
                    result = window.AndroidInterface.setCameraOverspeedLimit(!overspeedEnabled);
                    break;
                case 'ambientLight':
                    const ambientEnabled = window.AndroidInterface.isAmbientLightEnabled();
                    result = window.AndroidInterface.setAmbientLightEnabled(!ambientEnabled);
                    break;
                case 'speech':
                    const speechEnabled = window.AndroidInterface.isSpeechEnabled();
                    result = window.AndroidInterface.setSpeechEnabled(!speechEnabled);
                    break;
                case 'secondaryScreen':
                    const screenEnabled = window.AndroidInterface.isSecondaryScreenEnabled();
                    result = window.AndroidInterface.setSecondaryScreenEnabled(!screenEnabled);
                    break;
                case 'screenPresentation':
                    const showing = window.AndroidInterface.isPresentationShowing();
                    if (showing) {
                        window.AndroidInterface.hidePresentation();
                        result = true;
                    } else {
                        result = window.AndroidInterface.showCarStatusPresentation();
                    }
                    break;
            }
            
            console.log('切换开关:', switchId, '结果:', result);
            this.refreshSwitchStates();
            
        } catch (e) {
            console.error('切换开关失败:', switchId, e);
        }
    },
    
    /**
     * 设置驾驶模式
     * @description 设置车辆驾驶模式
     * @param {number} mode - 驾驶模式ID (0-5)
     */
    setDriveMode: function(mode) {
        if (window.AndroidInterface && window.AndroidInterface.setDriveMode) {
            const result = window.AndroidInterface.setDriveMode(mode);
            console.log('设置驾驶模式:', mode, '结果:', result);
        }
    },
    
    /**
     * 切换场景模式
     * @description 切换指定场景模式
     * @param {string} modeId - 场景模式ID
     */
    toggleSceneMode: function(modeId) {
        if (!window.AndroidInterface) return;
        
        try {
            let result = false;
            switch (modeId) {
                case 'guard':
                    result = window.AndroidInterface.setGuardMode(true);
                    break;
                case 'rest':
                    result = window.AndroidInterface.setRestMode(true);
                    break;
                case 'camping':
                    result = window.AndroidInterface.setCampingMode(true);
                    break;
                case 'powerSave':
                    result = window.AndroidInterface.setPowerSaveMode(true);
                    break;
                case 'sentinel':
                    result = window.AndroidInterface.setSentinelMode(true);
                    break;
            }
            console.log('切换场景模式:', modeId, '结果:', result);
        } catch (e) {
            console.error('切换场景模式失败:', modeId, e);
        }
    },
    
    /**
     * 刷新开关状态显示
     * @description 刷新所有开关的状态显示
     */
    refreshSwitchStates: function() {
        if (!window.AndroidInterface) return;
        
        const panel = document.getElementById('quickSwitchPanel');
        if (!panel) return;
        
        // 更新可读取状态的开关
        const statusMap = {
            'videoWhileDriving': window.AndroidInterface.isVideoWhileDrivingEnabled ? window.AndroidInterface.isVideoWhileDrivingEnabled() : null,
            'cameraOverspeed': window.AndroidInterface.isCameraOverspeedLimitEnabled ? window.AndroidInterface.isCameraOverspeedLimitEnabled() : null,
            'ambientLight': window.AndroidInterface.isAmbientLightEnabled ? window.AndroidInterface.isAmbientLightEnabled() : null,
            'speech': window.AndroidInterface.isSpeechEnabled ? window.AndroidInterface.isSpeechEnabled() : null,
            'secondaryScreen': window.AndroidInterface.isSecondaryScreenEnabled ? window.AndroidInterface.isSecondaryScreenEnabled() : null,
            'screenPresentation': window.AndroidInterface.isPresentationShowing ? window.AndroidInterface.isPresentationShowing() : null,
        };
        
        panel.querySelectorAll('.switch-item').forEach(item => {
            const id = item.dataset.id;
            const statusEl = item.querySelector('.switch-status');
            if (statusEl && statusMap[id] !== null && statusMap[id] !== undefined) {
                statusEl.textContent = statusMap[id] ? '开' : '关';
                statusEl.style.color = statusMap[id] ? '#4CAF50' : '#888';
                item.style.background = statusMap[id] ? 'rgba(76, 175, 80, 0.2)' : 'rgba(255,255,255,0.1)';
            }
        });
        
        // 更新桌面快捷开关状态
        const desktopContainer = document.getElementById('quickSwitchesContainer');
        if (desktopContainer) {
            desktopContainer.querySelectorAll('.quick-switch-item[data-id]').forEach(item => {
                const id = item.getAttribute('data-id');
                if (statusMap[id] !== null && statusMap[id] !== undefined) {
                    if (statusMap[id]) {
                        item.classList.add('active');
                    } else {
                        item.classList.remove('active');
                    }
                }
            });
        }
    }
};

// 将快捷开关管理器挂载到window对象上
window.QuickSwitchManager = QuickSwitchManager;

// 为360全景按钮添加快捷开关面板切换功能
document.addEventListener('DOMContentLoaded', function() {
    const cameraBtn = document.getElementById('camera360Btn');
    if (cameraBtn) {
        let pressTimer = null;
        let isLongPress = false;
        
        // 长按显示快捷开关面板
        cameraBtn.addEventListener('mousedown', function(e) {
            isLongPress = false;
            pressTimer = setTimeout(function() {
                isLongPress = true;
                if (window.QuickSwitchManager) {
                    window.QuickSwitchManager.togglePanel();
                }
            }, 500);
        });
        
        cameraBtn.addEventListener('mouseup', function(e) {
            if (pressTimer) {
                clearTimeout(pressTimer);
                pressTimer = null;
            }
        });
        
        cameraBtn.addEventListener('mouseleave', function(e) {
            if (pressTimer) {
                clearTimeout(pressTimer);
                pressTimer = null;
            }
        });
        
        // 单击还是触发360全景
        cameraBtn.addEventListener('click', function(e) {
            if (isLongPress) {
                e.preventDefault();
                e.stopPropagation();
                isLongPress = false;
            }
        });
        
        // 触摸事件支持
        cameraBtn.addEventListener('touchstart', function(e) {
            isLongPress = false;
            pressTimer = setTimeout(function() {
                isLongPress = true;
                if (window.QuickSwitchManager) {
                    window.QuickSwitchManager.togglePanel();
                }
            }, 500);
        });
        
        cameraBtn.addEventListener('touchend', function(e) {
            if (pressTimer) {
                clearTimeout(pressTimer);
                pressTimer = null;
            }
        });
    }
});
