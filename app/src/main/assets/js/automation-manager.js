/**
 * 自动化场景管理器模块
 * 管理各种自动化场景的配置和触发，提供可视化配置面板
 * 
 * @module AutomationManager
 * @version 1.0.0
 * @description 车辆自动化场景配置与管理模块
 */

/**
 * 自动化场景管理器
 * 管理12种自动化场景的启用/禁用状态，支持本地存储和后端同步
 */
const AutomationManager = {
    // 自动化场景列表
    scenarios: [
        {
            id: 'turnLight360',
            name: '转向灯自动开360',
            description: '打转向灯时自动启动360全景影像',
            icon: '📸',
            category: '360全景',
            defaultEnabled: false
        },
        {
            id: 'reverse360',
            name: 'R档自动开360',
            description: '挂倒挡时自动启动360全景影像',
            icon: '🚗',
            category: '360全景',
            defaultEnabled: false
        },
        {
            id: 'lowSpeed360',
            name: '降速自动开360',
            description: '车速低于10km/h时自动启动360全景',
            icon: '🐢',
            category: '360全景',
            defaultEnabled: false
        },
        {
            id: 'dGearVoice',
            name: 'D档语音提示',
            description: '挂D档时语音提示"前进档"',
            icon: '🔊',
            category: '语音提示',
            defaultEnabled: false
        },
        {
            id: 'rGearVoice',
            name: 'R档语音提示',
            description: '挂R档时语音提示"倒车请注意"',
            icon: '🔊',
            category: '语音提示',
            defaultEnabled: false
        },
        {
            id: 'doorOpenLight',
            name: '开门自动亮灯',
            description: '开车门时自动点亮近光灯',
            icon: '💡',
            category: '灯光控制',
            defaultEnabled: false
        },
        {
            id: 'reverseLowerVolume',
            name: '倒车自动降音量',
            description: '挂R档时自动降低媒体音量',
            icon: '🔉',
            category: '音量控制',
            defaultEnabled: false
        },
        {
            id: 'nightAutoLight',
            name: '夜间自动开大灯',
            description: '环境光线暗时自动开启近光灯',
            icon: '🌙',
            category: '灯光控制',
            defaultEnabled: false
        },
        {
            id: 'lockAutoCloseWindow',
            name: '锁车自动关窗',
            description: '锁车时自动关闭所有车窗',
            icon: '🔒',
            category: '车辆安全',
            defaultEnabled: false
        },
        {
            id: 'speedLimitWarning',
            name: '超速提醒',
            description: '车速超过120km/h时语音提醒',
            icon: '⚠️',
            category: '驾驶安全',
            defaultEnabled: false
        },
        {
            id: 'doorOpenWarning',
            name: '开门预警',
            description: '车门未关好时语音提醒',
            icon: '🚪',
            category: '驾驶安全',
            defaultEnabled: false
        },
        {
            id: 'seatbeltReminder',
            name: '安全带提醒',
            description: 'D档且未系安全带时语音提醒',
            icon: '🪑',
            category: '驾驶安全',
            defaultEnabled: false
        }
    ],
    
    // 当前启用的场景
    enabledScenarios: {},
    
    /**
     * 初始化自动化场景管理器
     * 加载配置并初始化
     */
    init: function() {
        console.log('自动化场景管理器初始化');
        this.loadSettings();
    },
    
    /**
     * 加载配置
     * 优先从后端加载，其次从本地存储加载，最后使用默认值
     */
    loadSettings: function() {
        try {
            // 优先从后端加载配置
            if (typeof Android !== 'undefined' && Android.getAutomationSettings) {
                try {
                    const backendSettings = Android.getAutomationSettings();
                    if (backendSettings) {
                        const parsed = JSON.parse(backendSettings);
                        if (parsed && Object.keys(parsed).length > 0) {
                            this.enabledScenarios = parsed;
                            console.log('从后端加载自动化配置成功');
                            return;
                        }
                    }
                } catch (e) {
                    console.warn('从后端加载配置失败，使用本地配置:', e);
                }
            }
            
            // 从本地存储加载
            const saved = localStorage.getItem('automationSettings');
            if (saved) {
                this.enabledScenarios = JSON.parse(saved);
            } else {
                // 使用默认值
                this.scenarios.forEach(s => {
                    this.enabledScenarios[s.id] = s.defaultEnabled;
                });
                this.saveSettings();
            }
        } catch (e) {
            console.error('加载自动化配置失败:', e);
        }
    },
    
    /**
     * 保存配置
     * 保存到本地存储并同步到后端
     */
    saveSettings: function() {
        try {
            localStorage.setItem('automationSettings', JSON.stringify(this.enabledScenarios));
            
            // 同步到后端
            if (typeof Android !== 'undefined' && Android.setAutomationSettings) {
                try {
                    Android.setAutomationSettings(JSON.stringify(this.enabledScenarios));
                } catch (e) {
                    console.error('同步配置到后端失败:', e);
                }
            }
        } catch (e) {
            console.error('保存自动化配置失败:', e);
        }
    },
    
    /**
     * 检查场景是否启用
     * @param {string} scenarioId - 场景ID
     * @returns {boolean} 是否启用
     */
    isEnabled: function(scenarioId) {
        return this.enabledScenarios[scenarioId] === true;
    },
    
    /**
     * 切换场景启用状态
     * @param {string} scenarioId - 场景ID
     * @returns {boolean} 切换后的状态
     */
    toggleScenario: function(scenarioId) {
        this.enabledScenarios[scenarioId] = !this.enabledScenarios[scenarioId];
        this.saveSettings();
        
        // 单独通知后端（可选，saveSettings已包含全量同步）
        if (typeof Android !== 'undefined' && Android.setAutomationScenarioEnabled) {
            try {
                Android.setAutomationScenarioEnabled(scenarioId, this.enabledScenarios[scenarioId]);
            } catch (e) {
                console.error('通知后端场景状态失败:', e);
            }
        }
        
        return this.enabledScenarios[scenarioId];
    },
    
    /**
     * 获取按分类分组的场景列表
     * @returns {Object} 按分类分组的场景对象
     */
    getScenariosByCategory: function() {
        const categories = {};
        this.scenarios.forEach(s => {
            if (!categories[s.category]) {
                categories[s.category] = [];
            }
            categories[s.category].push({
                ...s,
                enabled: this.isEnabled(s.id)
            });
        });
        return categories;
    },
    
    /**
     * 显示自动化配置面板
     * 创建并显示可视化配置面板
     */
    showPanel: function() {
        let panel = document.getElementById('automationPanel');
        if (!panel) {
            panel = this.createPanel();
            document.body.appendChild(panel);
        }
        panel.style.display = 'block';
        this.refreshPanel();
    },
    
    /**
     * 隐藏自动化配置面板
     */
    hidePanel: function() {
        const panel = document.getElementById('automationPanel');
        if (panel) {
            panel.style.display = 'none';
        }
    },
    
    /**
     * 切换自动化配置面板显示状态
     */
    togglePanel: function() {
        const panel = document.getElementById('automationPanel');
        if (panel && panel.style.display === 'block') {
            this.hidePanel();
        } else {
            this.showPanel();
        }
    },
    
    /**
     * 创建自动化配置面板
     * 创建可视化配置面板DOM元素
     * @returns {HTMLElement} 面板元素
     */
    createPanel: function() {
        const panel = document.createElement('div');
        panel.id = 'automationPanel';
        panel.className = 'automation-panel';
        panel.style.cssText = `
            position: fixed;
            top: 50px;
            left: 50%;
            transform: translateX(-50%);
            width: 500px;
            max-height: 80vh;
            background: rgba(0, 0, 0, 0.95);
            border-radius: 16px;
            padding: 20px;
            z-index: 10000;
            overflow-y: auto;
            backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.1);
        `;
        
        let html = `
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
                <h3 style="color: white; margin: 0; font-size: 18px;">⚙️ 自动化场景配置</h3>
                <button id="closeAutomationPanel" style="background: none; border: none; color: white; font-size: 20px; cursor: pointer;">✕</button>
            </div>
        `;
        
        const categories = this.getScenariosByCategory();
        
        Object.keys(categories).forEach(category => {
            html += `<h4 style="color: #4CAF50; margin: 16px 0 8px 0; font-size: 14px;">${category}</h4>`;
            html += '<div style="display: flex; flex-direction: column; gap: 8px;">';
            
            categories[category].forEach(scenario => {
                html += `
                    <div class="automation-item" data-id="${scenario.id}"
                         style="display: flex; align-items: center; padding: 12px; background: rgba(255,255,255,0.05); border-radius: 10px; cursor: pointer;">
                        <div style="font-size: 24px; margin-right: 12px;">${scenario.icon}</div>
                        <div style="flex: 1;">
                            <div style="color: white; font-size: 14px;">${scenario.name}</div>
                            <div style="color: #888; font-size: 12px;">${scenario.description}</div>
                        </div>
                        <div class="automation-toggle" style="width: 40px; height: 20px; border-radius: 10px; background: ${scenario.enabled ? '#4CAF50' : '#555'}; position: relative;">
                            <div style="width: 16px; height: 16px; border-radius: 50%; background: white; position: absolute; top: 2px; left: ${scenario.enabled ? '22px' : '2px'}; transition: all 0.2s;"></div>
                        </div>
                    </div>
                `;
            });
            
            html += '</div>';
        });
        
        panel.innerHTML = html;
        
        // 绑定关闭按钮
        panel.querySelector('#closeAutomationPanel').addEventListener('click', () => {
            this.hidePanel();
        });
        
        // 绑定场景切换事件
        panel.querySelectorAll('.automation-item').forEach(item => {
            item.addEventListener('click', () => {
                const id = item.dataset.id;
                const enabled = this.toggleScenario(id);
                this.refreshPanel();
            });
        });
        
        // 点击面板边缘或空白处关闭
        panel.addEventListener('click', (e) => {
            const interactive = e.target.closest('.automation-item, #closeAutomationPanel, h3, h4');
            if (!interactive) {
                this.hidePanel();
            }
        });
        
        return panel;
    },
    
    /**
     * 刷新面板状态
     * 更新面板中所有场景的开关状态显示
     */
    refreshPanel: function() {
        const panel = document.getElementById('automationPanel');
        if (!panel) return;
        
        panel.querySelectorAll('.automation-item').forEach(item => {
            const id = item.dataset.id;
            const enabled = this.isEnabled(id);
            const toggle = item.querySelector('.automation-toggle');
            if (toggle) {
                toggle.style.background = enabled ? '#4CAF50' : '#555';
                const knob = toggle.querySelector('div');
                if (knob) {
                    knob.style.left = enabled ? '22px' : '2px';
                }
            }
        });
    },
    
    /**
     * 触发自动化场景（供后端调用）
     * @param {string} trigger - 触发条件（如 'gear_change'、'door_open'等）
     * @param {Object} data - 触发数据
     */
    trigger: function(trigger, data) {
        console.log('自动化触发:', trigger, data);
        // 根据触发条件执行对应的自动化动作
        // 实际逻辑在后端实现，这里只是前端配置管理
    }
};

// 将自动化管理器挂载到window对象上
window.AutomationManager = AutomationManager;

// 页面加载完成后初始化
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', function() {
        AutomationManager.init();
    });
} else {
    AutomationManager.init();
}
