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

        let html = '<h3 class="qsp-title">功能控制面板</h3>';

        // 快捷开关网格
        html += '<div class="qsp-grid qsp-switch-grid">';
        this.switches.forEach(sw => {
            html += `<div class="qsp-item qsp-switch" data-id="${sw.id}"><div>${sw.icon}</div><div>${sw.name}</div></div>`;
        });
        html += '</div>';

        // 驾驶模式
        html += '<h4 class="qsp-section-title">驾驶模式</h4>';
        html += '<div class="qsp-grid qsp-drive-mode-grid">';
        this.driveModes.forEach(mode => {
            html += `<div class="qsp-item" data-id="${mode.id}"><div>${mode.icon}</div><div>${mode.name}</div></div>`;
        });
        html += '</div>';

        // 场景模式
        html += '<h4 class="qsp-section-title">场景模式</h4>';
        html += '<div class="qsp-grid qsp-scene-mode-grid">';
        this.sceneModes.forEach(mode => {
            html += `<div class="qsp-item" data-id="${mode.id}"><div>${mode.icon}</div><div>${mode.name}</div></div>`;
        });
        html += '</div>';

        // 设置入口
        html += '<h4 class="qsp-section-title">系统</h4>';
        html += '<div class="qsp-grid qsp-mode-grid">';
        html += `<div class="qsp-item" id="qspSettingsEntry"><div>⚙️</div><div>设置</div></div>`;
        html += '</div>';

        panel.innerHTML = html;

        // 事件委托：开关网格
        panel.querySelector('.qsp-switch-grid').addEventListener('click', (e) => {
            const item = e.target.closest('.qsp-item');
            if (item) this.toggleSwitch(item.dataset.id);
        });

        // 事件委托：驾驶模式网格
        panel.querySelector('.qsp-drive-mode-grid').addEventListener('click', (e) => {
            const item = e.target.closest('.qsp-item');
            if (item) this.setDriveMode(parseInt(item.dataset.id));
        });

        // 事件委托：场景模式网格
        panel.querySelector('.qsp-scene-mode-grid').addEventListener('click', (e) => {
            const item = e.target.closest('.qsp-item');
            if (item) this.toggleSceneMode(item.dataset.id);
        });

        // 事件委托：设置入口
        const self = this;
        const settingsEntry = panel.querySelector('#qspSettingsEntry');
        if (settingsEntry) {
            settingsEntry.addEventListener('click', function(e) {
                e.stopPropagation();
                self.hidePanel();
                setTimeout(() => {
                    if (typeof showSettingsModal === 'function') {
                        showSettingsModal();
                    } else {
                        const settingsModal = document.getElementById('settingsModal');
                        if (settingsModal) {
                            settingsModal.style.display = 'block';
                            settingsModal.classList.add('active');
                        }
                    }
                }, 100);
            });
        }

        // 点击面板内部空白区域关闭
        panel.addEventListener('click', function(e) {
            const interactive = e.target.closest('.qsp-item, #qspSettingsEntry');
            if (!interactive) {
                self.hidePanel();
            }
        });

        return panel;
    },
    
    /**
     * 切换开关
     * @description 切换指定开关的状态
     * @param {string} switchId - 开关ID
     */
    toggleSwitch: function(switchId) {
        if (!window.Android) {
            console.warn('Android环境不可用');
            return;
        }
        
        try {
            let result = false;
            // 先读取当前状态，再取反
            let currentState = false;
            switch (switchId) {
                case 'lowBeamLight':
                    currentState = window.Android.isLowBeamLightOn ? window.Android.isLowBeamLightOn() : false;
                    result = window.Android.setLowBeamLight(!currentState);
                    break;
                case 'rearFogLight':
                    currentState = window.Android.isRearFogLightOn ? window.Android.isRearFogLightOn() : false;
                    result = window.Android.setRearFogLight(!currentState);
                    break;
                case 'positionLight':
                    currentState = window.Android.isPositionLightOn ? window.Android.isPositionLightOn() : false;
                    result = window.Android.setPositionLight(!currentState);
                    break;
                case 'pedestrianAlert':
                    currentState = window.Android.isPedestrianAlertOn ? window.Android.isPedestrianAlertOn() : false;
                    result = window.Android.setPedestrianAlert(!currentState);
                    break;
                case 'maxCooling':
                    currentState = window.Android.isMaxCoolingOn ? window.Android.isMaxCoolingOn() : false;
                    result = window.Android.setMaxCooling(!currentState);
                    break;
                case 'nightMode':
                    currentState = window.Android.isNightModeOn ? window.Android.isNightModeOn() : false;
                    result = window.Android.setNightMode(!currentState);
                    break;
                case 'wifi':
                    currentState = window.Android.isWifiEnabled ? window.Android.isWifiEnabled() : false;
                    result = window.Android.setWifiEnabled(!currentState);
                    break;
                case 'bluetooth':
                    currentState = window.Android.isBluetoothEnabled ? window.Android.isBluetoothEnabled() : false;
                    result = window.Android.setBluetoothEnabled(!currentState);
                    break;
                case 'videoWhileDriving':
                    currentState = window.Android.isVideoWhileDrivingEnabled ? window.Android.isVideoWhileDrivingEnabled() : false;
                    result = window.Android.setVideoWhileDriving(!currentState);
                    break;
                case 'cameraOverspeed':
                    currentState = window.Android.isCameraOverspeedLimitEnabled ? window.Android.isCameraOverspeedLimitEnabled() : false;
                    result = window.Android.setCameraOverspeedLimit(!currentState);
                    break;
                case 'ambientLight':
                    currentState = window.Android.isAmbientLightEnabled ? window.Android.isAmbientLightEnabled() : false;
                    result = window.Android.setAmbientLightEnabled(!currentState);
                    break;
                case 'speech':
                    currentState = window.Android.isSpeechEnabled ? window.Android.isSpeechEnabled() : false;
                    result = window.Android.setSpeechEnabled(!currentState);
                    break;
                case 'secondaryScreen':
                    currentState = window.Android.isSecondaryScreenEnabled ? window.Android.isSecondaryScreenEnabled() : false;
                    result = window.Android.setSecondaryScreenEnabled(!currentState);
                    break;
                case 'screenPresentation':
                    currentState = window.Android.isPresentationShowing ? window.Android.isPresentationShowing() : false;
                    if (currentState) {
                        window.Android.hidePresentation();
                        result = true;
                    } else {
                        result = window.Android.showCarStatusPresentation();
                    }
                    break;
            }
            
            console.log('切换开关:', switchId, currentState, '→', !currentState, '结果:', result);
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
        if (window.Android && window.Android.setDriveMode) {
            const result = window.Android.setDriveMode(mode);
            console.log('设置驾驶模式:', mode, '结果:', result);
        }
    },
    
    /**
     * 切换场景模式
     * @description 切换指定场景模式
     * @param {string} modeId - 场景模式ID
     */
    toggleSceneMode: function(modeId) {
        if (!window.Android) return;
        
        try {
            let result = false;
            switch (modeId) {
                case 'guard':
                    result = window.Android.setGuardMode(true);
                    break;
                case 'rest':
                    result = window.Android.setRestMode(true);
                    break;
                case 'camping':
                    result = window.Android.setCampingMode(true);
                    break;
                case 'powerSave':
                    result = window.Android.setPowerSaveMode(true);
                    break;
                case 'sentinel':
                    result = window.Android.setSentinelMode(true);
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
        if (!window.Android) return;
        
        const panel = document.getElementById('quickSwitchPanel');
        if (!panel) return;
        
        // 更新所有可读取状态的开关
        const statusMap = {
            'lowBeamLight':    window.Android.isLowBeamLightOn ? window.Android.isLowBeamLightOn() : null,
            'rearFogLight':    window.Android.isRearFogLightOn ? window.Android.isRearFogLightOn() : null,
            'positionLight':   window.Android.isPositionLightOn ? window.Android.isPositionLightOn() : null,
            'pedestrianAlert': window.Android.isPedestrianAlertOn ? window.Android.isPedestrianAlertOn() : null,
            'maxCooling':      window.Android.isMaxCoolingOn ? window.Android.isMaxCoolingOn() : null,
            'nightMode':       window.Android.isNightModeOn ? window.Android.isNightModeOn() : null,
            'wifi':            window.Android.isWifiEnabled ? window.Android.isWifiEnabled() : null,
            'bluetooth':       window.Android.isBluetoothEnabled ? window.Android.isBluetoothEnabled() : null,
            'videoWhileDriving': window.Android.isVideoWhileDrivingEnabled ? window.Android.isVideoWhileDrivingEnabled() : null,
            'cameraOverspeed': window.Android.isCameraOverspeedLimitEnabled ? window.Android.isCameraOverspeedLimitEnabled() : null,
            'ambientLight':    window.Android.isAmbientLightEnabled ? window.Android.isAmbientLightEnabled() : null,
            'speech':          window.Android.isSpeechEnabled ? window.Android.isSpeechEnabled() : null,
            'secondaryScreen': window.Android.isSecondaryScreenEnabled ? window.Android.isSecondaryScreenEnabled() : null,
            'screenPresentation': window.Android.isPresentationShowing ? window.Android.isPresentationShowing() : null,
        };
        
        panel.querySelectorAll('.qsp-item.qsp-switch').forEach(item => {
            const id = item.dataset.id;
            if (statusMap[id] !== null && statusMap[id] !== undefined) {
                item.classList.toggle('qsp-active', statusMap[id]);
            }
        });

        // 更新桌面快捷开关状态
        const desktopContainer = document.getElementById('quickSwitchesContainer');
        if (desktopContainer) {
            desktopContainer.querySelectorAll('.qsp-item[data-id]').forEach(item => {
                const id = item.getAttribute('data-id');
                if (statusMap[id] !== null && statusMap[id] !== undefined) {
                    if (statusMap[id]) {
                        item.classList.add('qsp-active');
                    } else {
                        item.classList.remove('qsp-active');
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
