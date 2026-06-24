/**
 * @module VoiceTestManager
 * @description 语音测试管理器 - 提供语音指令测试功能
 * @version 1.0.0
 */

/**
 * 语音测试管理器
 * @type {Object}
 * @description 管理语音指令测试界面的交互和功能
 */
const VoiceTestManager = {
    /**
     * 初始化语音测试管理器
     * @description 绑定测试按钮、输入框、快捷指令的事件监听器
     */
    init: function() {
        const testBtn = document.getElementById('voiceTestBtn');
        const input = document.getElementById('voiceCommandInput');
        const resultDiv = document.getElementById('voiceTestResult');
        const quickCmds = document.querySelectorAll('.quick-cmd');
        
        if (testBtn && input) {
            // 测试按钮点击
            testBtn.addEventListener('click', () => {
                const command = input.value.trim();
                if (command) {
                    this.sendVoiceCommand(command);
                } else {
                    this.showResult('请输入语音指令', 'error');
                }
            });
            
            // 回车发送
            input.addEventListener('keypress', (e) => {
                if (e.key === 'Enter') {
                    const command = input.value.trim();
                    if (command) {
                        this.sendVoiceCommand(command);
                    }
                }
            });
        }
        
        // 快捷指令点击
        if (quickCmds) {
            quickCmds.forEach(cmd => {
                cmd.addEventListener('click', () => {
                    const command = cmd.getAttribute('data-cmd');
                    if (input) {
                        input.value = command;
                    }
                    this.sendVoiceCommand(command);
                });
            });
        }
    },
    
    /**
     * 发送语音指令到Android接口
     * @param {string} command - 要发送的语音指令文本
     * @description 通过AndroidInterface发送语音指令并显示结果
     */
    sendVoiceCommand: function(command) {
        this.showResult('正在发送指令: ' + command + '...', '');
        
        try {
            if (window.AndroidInterface && typeof window.AndroidInterface.sendVoiceCommand === 'function') {
                const result = window.AndroidInterface.sendVoiceCommand(command);
                if (result) {
                    this.showResult('✅ 指令发送成功: ' + command, 'success');
                } else {
                    this.showResult('❌ 指令发送失败', 'error');
                }
            } else {
                this.showResult('⚠️  Android 接口不可用', 'error');
            }
        } catch (e) {
            console.error('发送语音指令失败:', e);
            this.showResult('❌ 发送异常: ' + e.message, 'error');
        }
    },
    
    /**
     * 显示测试结果
     * @param {string} message - 结果消息文本
     * @param {string} type - 结果类型 ('success' | 'error' | '')
     * @description 在结果区域显示测试消息，并应用对应的样式类
     */
    showResult: function(message, type) {
        const resultDiv = document.getElementById('voiceTestResult');
        if (resultDiv) {
            resultDiv.textContent = message;
            resultDiv.className = 'voice-test-result show ' + type;
        }
    }
};

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', function() {
    // 延迟初始化，确保设置面板已加载
    setTimeout(() => {
        VoiceTestManager.init();
    }, 500);
});
