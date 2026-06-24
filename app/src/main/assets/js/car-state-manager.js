/**
 * @module CarStateManager
 * @description 车辆状态管理器模块
 * 负责接收后端推送的车辆状态并更新UI，包括档位、车门、车窗、转向灯、锁车状态、车速、胎压等
 * @version 1.0.0
 */

/**
 * 车辆状态管理器
 * 负责接收后端推送的车辆状态并更新UI
 */
const CarStateManager = {
    currentState: {},
    
    /**
     * 更新车辆状态
     * @param {Object} state - 车辆状态对象
     */
    updateState: function(state) {
        if (!state) return;
        this.currentState = state;
        console.log('收到车辆状态更新:', state);
        
        // 更新各个状态指示器
        this.updateGearIndicator(state.gear, state.gearText);
        this.updateDoorIndicator(state.isAnyDoorOpen, state.frontLeftDoor, state.frontRightDoor, state.rearLeftDoor, state.rearRightDoor, state.trunkDoor, state.hoodDoor);
        // 更新车窗状态（如果有数据的话）
        if (state.frontLeftWindow !== undefined || state.frontRightWindow !== undefined) {
            this.updateWindowStatus(
                state.frontLeftWindow,
                state.frontRightWindow,
                state.rearLeftWindow,
                state.rearRightWindow
            );
        }
        this.updateTurnIndicators(state.leftTurnLight, state.rightTurnLight);
        this.updateLockIndicator(state.isLocked);
        this.updateSpeedDisplay(state.speed);
        this.updateTirePressure(state);
    },
    
    /**
     * 更新档位指示器
     * @param {number} gear - 档位值 (1=R, 2=N, 3=D, 其他=P)
     * @param {string} gearText - 档位显示文本
     */
    updateGearIndicator: function(gear, gearText) {
        const element = document.getElementById('gearIndicator');
        if (element) {
            element.textContent = gearText || 'P档';
            // 移除所有档位类
            element.classList.remove('gear-r', 'gear-n', 'gear-d', 'gear-p');
            // 根据档位添加对应类
            if (gear === 1) { // R挡
                element.classList.add('gear-r');
            } else if (gear === 2) { // N挡
                element.classList.add('gear-n');
            } else if (gear === 3) { // D挡
                element.classList.add('gear-d');
            } else { // P挡
                element.classList.add('gear-p');
            }
        }
    },
    
    /**
     * 更新车门指示器
     * @param {boolean} isAnyDoorOpen - 是否有任意车门打开
     * @param {number} fl - 左前门状态 (1=打开, 0=关闭)
     * @param {number} fr - 右前门状态 (1=打开, 0=关闭)
     * @param {number} rl - 左后门状态 (1=打开, 0=关闭)
     * @param {number} rr - 右后门状态 (1=打开, 0=关闭)
     * @param {number} trunk - 后备箱状态
     * @param {number} hood - 引擎盖状态
     */
    updateDoorIndicator: function(isAnyDoorOpen, fl, fr, rl, rr, trunk, hood) {
        // 更新各个车门状态
        const doorElements = {
            frontLeft: document.getElementById('frontLeftDoorStatus'),
            frontRight: document.getElementById('frontRightDoorStatus'),
            rearLeft: document.getElementById('rearLeftDoorStatus'),
            rearRight: document.getElementById('rearRightDoorStatus')
        };
        
        const doorStates = {
            frontLeft: fl,
            frontRight: fr,
            rearLeft: rl,
            rearRight: rr
        };
        
        let hasAnyActive = false;
        
        for (const [key, element] of Object.entries(doorElements)) {
            if (element) {
                if (doorStates[key] === 1) {
                    element.classList.add('active');
                    hasAnyActive = true;
                } else {
                    element.classList.remove('active');
                }
            }
        }
        
        // 更新门窗状态容器的显示
        const dwContainer = document.getElementById('doorWindowStatus');
        if (dwContainer) {
            // 检查是否有任何门窗处于激活状态
            const allDwElements = dwContainer.querySelectorAll('.dw-status');
            let anyActive = false;
            allDwElements.forEach(el => {
                if (el.classList.contains('active')) {
                    anyActive = true;
                }
            });
            
            if (anyActive) {
                dwContainer.style.display = 'flex';
            } else {
                dwContainer.style.display = 'none';
            }
        }
    },
    
    /**
     * 更新车窗状态
     * @param {number} flw - 左前窗状态 (1=打开, 0=关闭)
     * @param {number} frw - 右前窗状态 (1=打开, 0=关闭)
     * @param {number} rlw - 左后窗状态 (1=打开, 0=关闭)
     * @param {number} rrw - 右后窗状态 (1=打开, 0=关闭)
     */
    updateWindowStatus: function(flw, frw, rlw, rrw) {
        const windowElements = {
            frontLeft: document.getElementById('frontLeftWindowStatus'),
            frontRight: document.getElementById('frontRightWindowStatus'),
            rearLeft: document.getElementById('rearLeftWindowStatus'),
            rearRight: document.getElementById('rearRightWindowStatus')
        };
        
        const windowStates = {
            frontLeft: flw,
            frontRight: frw,
            rearLeft: rlw,
            rearRight: rrw
        };
        
        for (const [key, element] of Object.entries(windowElements)) {
            if (element) {
                if (windowStates[key] === 1) {
                    element.classList.add('active');
                } else {
                    element.classList.remove('active');
                }
            }
        }
        
        // 更新门窗状态容器的显示
        const dwContainer = document.getElementById('doorWindowStatus');
        if (dwContainer) {
            const allDwElements = dwContainer.querySelectorAll('.dw-status');
            let anyActive = false;
            allDwElements.forEach(el => {
                if (el.classList.contains('active')) {
                    anyActive = true;
                }
            });
            
            if (anyActive) {
                dwContainer.style.display = 'flex';
            } else {
                dwContainer.style.display = 'none';
            }
        }
    },
    
    /**
     * 更新转向灯指示器
     * @param {number} left - 左转向灯状态 (1=开启, 0=关闭)
     * @param {number} right - 右转向灯状态 (1=开启, 0=关闭)
     */
    updateTurnIndicators: function(left, right) {
        const leftElement = document.getElementById('leftTurnIndicator');
        const rightElement = document.getElementById('rightTurnIndicator');
        
        if (leftElement) {
            if (left === 1) {
                leftElement.classList.add('active');
            } else {
                leftElement.classList.remove('active');
            }
        }
        
        if (rightElement) {
            if (right === 1) {
                rightElement.classList.add('active');
            } else {
                rightElement.classList.remove('active');
            }
        }
    },
    
    /**
     * 更新锁车指示器
     * @param {boolean} isLocked - 是否已锁车
     */
    updateLockIndicator: function(isLocked) {
        const element = document.getElementById('lockIndicator');
        if (element) {
            if (isLocked) {
                element.textContent = '已锁';
                element.classList.remove('unlocked');
            } else {
                element.textContent = '未锁';
                element.classList.add('unlocked');
            }
        }
    },
    
    /**
     * 更新车速显示
     * @param {number} speed - 车速 (km/h)
     */
    updateSpeedDisplay: function(speed) {
        // 可以在状态栏显示车速，或者在其他位置显示
        console.log('当前车速:', speed, 'km/h');
    },

    /**
     * 更新胎压显示
     * @param {Object} state - 包含胎压和胎温的状态对象
     */
    updateTirePressure: function(state) {
        const pressures = [
            { selector: '.tire-pressure.front-left', pressure: state.frontLeftTirePressure, temp: state.frontLeftTireTemp },
            { selector: '.tire-pressure.front-right', pressure: state.frontRightTirePressure, temp: state.frontRightTireTemp },
            { selector: '.tire-pressure.rear-left', pressure: state.rearLeftTirePressure, temp: state.rearLeftTireTemp },
            { selector: '.tire-pressure.rear-right', pressure: state.rearRightTirePressure, temp: state.rearRightTireTemp }
        ];

        let hasAnyData = false;

        pressures.forEach(item => {
            const el = document.querySelector(item.selector);
            if (el) {
                const pressureEl = el.querySelector('.tire-pressure-value');
                const tempEl = el.querySelector('.tire-temp-value');
                
                if (item.pressure && item.pressure > 0) {
                    // 有真实数据，显示胎压和胎温
                    hasAnyData = true;
                    const pressureKpa = Math.round(item.pressure);
                    const temp = item.temp || '--';
                    
                    if (pressureEl) {
                        pressureEl.textContent = pressureKpa + 'kPa';
                    }
                    if (tempEl) {
                        tempEl.textContent = temp + '°';
                    }
                    
                    // 异常胎压标红
                    if (pressureKpa < 200 || pressureKpa > 300) {
                        el.classList.add('warning');
                    } else {
                        el.classList.remove('warning');
                    }
                }
                // 没有数据时保持默认显示（-- 和 --°）
            }
        });

        // 有数据时隐藏"正在获取"提示
        const loadingTip = document.querySelector('.tire-loading-tip');
        if (loadingTip) {
            if (hasAnyData) {
                loadingTip.style.display = 'none';
            } else {
                loadingTip.style.display = 'block';
            }
        }
    },
    
    /**
     * 更新360全景指示器
     * @param {boolean} isVisible - 是否显示360全景
     */
    updateCamera360Indicator: function(isVisible) {
        const element = document.getElementById('camera360Indicator');
        if (element) {
            if (isVisible) {
                element.style.display = 'inline';
                element.style.color = '#2196F3';
            } else {
                element.style.display = 'none';
            }
        }
    },
    
    /**
     * 初始化车辆状态管理器
     */
    init: function() {
        console.log('车辆状态管理器初始化');
        // 添加转向灯闪烁动画样式
        this.addBlinkAnimation();
        
        // 尝试从后端获取初始状态
        if (window.AndroidInterface && window.AndroidInterface.getCarState) {
            try {
                const stateJson = window.AndroidInterface.getCarState();
                if (stateJson) {
                    const state = JSON.parse(stateJson);
                    this.updateState(state);
                }
            } catch (e) {
                console.error('获取初始车辆状态失败:', e);
            }
        }
    },
    
    /**
     * 添加转向灯闪烁动画样式
     */
    addBlinkAnimation: function() {
        const style = document.createElement('style');
        style.textContent = `
            @keyframes blink {
                0%, 100% { opacity: 1; }
                50% { opacity: 0.3; }
            }
        `;
        document.head.appendChild(style);
    }
};

// 将车辆状态更新函数挂载到window对象上，供后端调用
window.updateCarState = function(stateJson) {
    try {
        const state = typeof stateJson === 'string' ? JSON.parse(stateJson) : stateJson;
        CarStateManager.updateState(state);
    } catch (e) {
        console.error('解析车辆状态失败:', e);
    }
};

// 页面加载完成后初始化车辆状态管理器
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', function() {
        CarStateManager.init();
    });
} else {
    CarStateManager.init();
}
