/**
 * debug-tool.js - 前端调试工具
 *
 * 功能：
 *   1. 车辆状态实时显示（浮动面板）
 *   2. console.log 输出状态变化（配合后端 onConsoleMessage 转发到 logcat）
 *   3. ADB 模拟命令参考（面板内显示）
 *
 * 触发：5次点击档位图标 → 切换面板显示/隐藏
 * 开关：localStorage.debug_mode === 'true' 时启用，默认关闭
 *       发行版无需改代码，关闭开关即可
 *
 * ADB 模拟车辆日志（DEBUG级别，匹配 LogcatMonitorService 过滤）：
 *   档位P: adb shell log -p D -t C11CarSomeIp "onMessage eventId: 1110 value: 0"
 *   档位R: adb shell log -p D -t C11CarSomeIp "onMessage eventId: 1110 value: 1"
 *   档位N: adb shell log -p D -t C11CarSomeIp "onMessage eventId: 1110 value: 2"
 *   档位D: adb shell log -p D -t C11CarSomeIp "onMessage eventId: 1110 value: 3"
 *   左前门开: adb shell log -p D -t C11CarSomeIp "onMessage eventId: 9123 value: 1"
 *   左前门关: adb shell log -p D -t C11CarSomeIp "onMessage eventId: 9123 value: 0"
 *   右前门: 9124, 左后门: 9125, 右后门: 9126, 后备箱: 9127, 前机盖: 9128
 *   天窗: 21201, 遮阳帘: 21207, 锁车: 1200 (value: 1=锁, 0=解锁)
 *   左转向开: adb shell log -p D -t AroundService "dealTurnLeftLight mLeftLightSts 1"
 *   车速60: adb shell log -p D -t C11CarXml "node_name : speed  setTextContent: 60"
 *   近光灯: adb shell log -p D -t C11CarXml "node_name : nearLight  setTextContent: 1"
 *   蓝牙连接: adb shell log -p I -t BtMusicManager "bluetooth connected"
 */

const DebugTool = (function() {
    let panel = null;
    let cmdList = null;
    let isVisible = false;
    let clickCount = 0;
    let clickTimer = null;
    let lastStateStr = '';

    /**
     * 是否启用调试模式（默认启用，仅控制面板是否响应点击）
     * 发行版可通过 localStorage.debug_mode = 'false' 关闭
     */
    function isDebugMode() {
        try {
            const v = localStorage.getItem('debug_mode');
            return v !== 'false'; // 默认启用
        } catch (e) {
            return true;
        }
    }

    /**
     * 初始化：绑定档位图标点击
     */
    function init() {
        if (!isDebugMode()) return;

        const gearIndicator = document.getElementById('gearIndicator');
        if (gearIndicator) {
            gearIndicator.addEventListener('click', handleGearClick);
        }

        // hook window.updateCarState 捕获状态变化
        hookUpdateCarState();

        console.log('[DebugTool] 调试工具已加载，连点5次档位图标显示面板');
    }

    /**
     * 档位图标点击：1.5秒内5次切换面板
     */
    function handleGearClick() {
        clickCount++;
        if (clickTimer) clearTimeout(clickTimer);
        clickTimer = setTimeout(function() { clickCount = 0; }, 1500);

        if (clickCount >= 5) {
            clickCount = 0;
            togglePanel();
        }
    }

    /**
     * 切换面板显示
     */
    function togglePanel() {
        if (isVisible) {
            hidePanel();
        } else {
            showPanel();
        }
    }

    /**
     * 显示面板
     */
    function showPanel() {
        if (!panel) createPanel();
        panel.style.display = 'block';
        isVisible = true;
        updatePanelContent();
        console.log('[DebugTool] 调试面板已显示');
    }

    /**
     * 隐藏面板
     */
    function hidePanel() {
        if (panel) panel.style.display = 'none';
        isVisible = false;
    }

    /**
     * 创建浮动面板
     */
    function createPanel() {
        panel = document.createElement('div');
        panel.id = 'debugPanel';
        panel.style.cssText = [
            'position: fixed',
            'top: 60px',
            'right: 10px',
            'width: 360px',
            'max-height: 70vh',
            'overflow-y: auto',
            'background: rgba(0, 0, 0, 0.85)',
            'color: #0f0',
            'font-family: monospace',
            'font-size: 12px',
            'padding: 0',
            'border: 1px solid #0f0',
            'border-radius: 4px',
            'z-index: 99999',
            'user-select: text',
            'pointer-events: auto'
        ].join(';');

        // 标题栏
        const header = document.createElement('div');
        header.style.cssText = [
            'background: #0f0',
            'color: #000',
            'padding: 4px 8px',
            'font-weight: bold',
            'display: flex',
            'justify-content: space-between',
            'align-items: center'
        ].join(';');
        header.innerHTML = '<span>调试面板</span>';

        const closeBtn = document.createElement('span');
        closeBtn.textContent = '×';
        closeBtn.style.cssText = 'cursor: pointer; padding: 0 6px; font-size: 16px;';
        closeBtn.addEventListener('click', hidePanel);
        header.appendChild(closeBtn);
        panel.appendChild(header);

        // 状态内容区
        const stateBox = document.createElement('div');
        stateBox.id = 'debugStateBox';
        stateBox.style.cssText = 'padding: 8px; white-space: pre-wrap; word-break: break-all;';
        panel.appendChild(stateBox);

        // ADB命令切换按钮
        const cmdBtn = document.createElement('div');
        cmdBtn.textContent = '▸ ADB 模拟命令';
        cmdBtn.style.cssText = 'padding: 4px 8px; background: #222; color: #0f0; cursor: pointer; border-top: 1px solid #333;';
        cmdBtn.addEventListener('click', function() {
            if (cmdList.style.display === 'none') {
                cmdList.style.display = 'block';
                cmdBtn.textContent = '▾ ADB 模拟命令';
            } else {
                cmdList.style.display = 'none';
                cmdBtn.textContent = '▸ ADB 模拟命令';
            }
        });
        panel.appendChild(cmdBtn);

        // ADB命令列表
        cmdList = document.createElement('div');
        cmdList.style.cssText = 'display: none; padding: 8px; border-top: 1px solid #333; color: #ccc;';
        cmdList.innerHTML = getAdbCommandsHtml();
        panel.appendChild(cmdList);

        document.body.appendChild(panel);
    }

    /**
     * ADB 模拟命令列表（HTML）
     */
    function getAdbCommandsHtml() {
        const cmds = [
            ['档位 P', 'adb shell log -p D -t C11CarSomeIp "onMessage eventId: 1110 value: 0"'],
            ['档位 R', 'adb shell log -p D -t C11CarSomeIp "onMessage eventId: 1110 value: 1"'],
            ['档位 N', 'adb shell log -p D -t C11CarSomeIp "onMessage eventId: 1110 value: 2"'],
            ['档位 D', 'adb shell log -p D -t C11CarSomeIp "onMessage eventId: 1110 value: 3"'],
            ['左前门 开/关', 'adb shell log -p D -t C11CarSomeIp "onMessage eventId: 9123 value: 1/0"'],
            ['右前门 开/关', 'adb shell log -p D -t C11CarSomeIp "onMessage eventId: 9124 value: 1/0"'],
            ['左后门 开/关', 'adb shell log -p D -t C11CarSomeIp "onMessage eventId: 9125 value: 1/0"'],
            ['右后门 开/关', 'adb shell log -p D -t C11CarSomeIp "onMessage eventId: 9126 value: 1/0"'],
            ['后备箱 开/关', 'adb shell log -p D -t C11CarSomeIp "onMessage eventId: 9127 value: 1/0"'],
            ['前机盖 开/关', 'adb shell log -p D -t C11CarSomeIp "onMessage eventId: 9128 value: 1/0"'],
            ['天窗 开/关', 'adb shell log -p D -t C11CarSomeIp "onMessage eventId: 21201 value: 1/0"'],
            ['遮阳帘 开/关', 'adb shell log -p D -t C11CarSomeIp "onMessage eventId: 21207 value: 1/0"'],
            ['锁车/解锁', 'adb shell log -p D -t C11CarSomeIp "onMessage eventId: 1200 value: 1/0"'],
            ['左转向 开/关', 'adb shell log -p D -t AroundService "dealTurnLeftLight mLeftLightSts 1/0"'],
            ['车速 60km/h', 'adb shell log -p D -t C11CarXml "node_name : speed  setTextContent: 60"'],
            ['近光灯 开/关', 'adb shell log -p D -t C11CarXml "node_name : nearLight  setTextContent: 1/0"'],
            ['蓝牙连接', 'adb shell log -p I -t BtMusicManager "bluetooth connected"'],
            ['胎压 左前 245/32°C', "adb shell log -p D -t zza 'TPMSBean{pos=0, singleSigSts=0, singleLeakSts=0, singlePressSts=0, singleTempSts=0, singleSensorSts=0, singleTirePress=245, singleTireTemp=32}'"],
            ['胎压 右前 250/35°C', "adb shell log -p D -t zza 'TPMSBean{pos=1, singleSigSts=0, singleLeakSts=0, singlePressSts=0, singleTempSts=0, singleSensorSts=0, singleTirePress=250, singleTireTemp=35}'"],
            ['胎压 左后 248/33°C', "adb shell log -p D -t zza 'TPMSBean{pos=2, singleSigSts=0, singleLeakSts=0, singlePressSts=0, singleTempSts=0, singleSensorSts=0, singleTirePress=248, singleTireTemp=33}'"],
            ['胎压 右后 252/34°C', "adb shell log -p D -t zza 'TPMSBean{pos=3, singleSigSts=0, singleLeakSts=0, singlePressSts=0, singleTempSts=0, singleSensorSts=0, singleTirePress=252, singleTireTemp=34}'"]
        ];

        let html = '<div style="margin-bottom:4px;color:#0f0;font-weight:bold;">点击复制命令到剪贴板：</div>';
        cmds.forEach(function(item) {
            html += '<div style="margin-bottom:6px;">';
            html += '<div style="color:#ff0;">' + item[0] + '</div>';
            html += '<div style="background:#111;padding:2px 4px;border-radius:2px;cursor:pointer;color:#0f0;" onclick="DebugTool.copyCmd(this)" data-cmd=\'' + item[1] + '\'>' + item[1] + '</div>';
            html += '</div>';
        });
        return html;
    }

    /**
     * 复制命令到剪贴板
     */
    function copyCmd(elem) {
        const cmd = elem.getAttribute('data-cmd');
        if (cmd) {
            try {
                navigator.clipboard.writeText(cmd);
                console.log('[DebugTool] 已复制: ' + cmd);
                const original = elem.style.background;
                elem.style.background = '#0f0';
                elem.style.color = '#000';
                setTimeout(function() {
                    elem.style.background = original;
                    elem.style.color = '#0f0';
                }, 300);
            } catch (e) {
                console.log('[DebugTool] 复制失败: ' + cmd);
            }
        }
    }

    /**
     * hook window.updateCarState
     * 在原始调用后触发调试输出
     */
    function hookUpdateCarState() {
        if (typeof window.updateCarState !== 'function') {
            // updateCarState 可能还没定义，延迟 hook
            setTimeout(hookUpdateCarState, 500);
            return;
        }

        // 避免重复 hook
        if (window.updateCarState._debugHooked) return;
        window.updateCarState._debugHooked = true;

        const original = window.updateCarState;
        window.updateCarState = function(state) {
            original(state);
            onCarStateUpdated(state);
        };
        console.log('[DebugTool] 已 hook window.updateCarState');
    }

    /**
     * 车辆状态更新回调
     */
    function onCarStateUpdated(state) {
        if (!state) return;

        const stateStr = JSON.stringify(state);
        if (stateStr === lastStateStr) return;
        lastStateStr = stateStr;

        // console 输出（配合后端 onConsoleMessage 转发到 logcat）
        console.log('[CarState] ' + stateStr);

        // 更新面板
        if (isVisible) updatePanelContent(state);
    }

    /**
     * 更新面板内容
     */
    function updatePanelContent(state) {
        const stateBox = document.getElementById('debugStateBox');
        if (!stateBox) return;

        const s = state || (window.CarStateManager && window.CarStateManager.currentState) || {};
        const lines = [];

        lines.push('=== 车辆状态 ===');
        lines.push('档位: ' + (s.gearText || s.gear || '未知'));
        lines.push('车速: ' + (s.speed !== undefined ? s.speed + ' km/h' : '未知'));
        lines.push('锁车: ' + (s.isLocked ? '已锁' : '未锁'));
        lines.push('');
        lines.push('-- 车门 --');
        lines.push('左前: ' + (s.frontLeftDoor ? '开' : '关'));
        lines.push('右前: ' + (s.frontRightDoor ? '开' : '关'));
        lines.push('左后: ' + (s.rearLeftDoor ? '开' : '关'));
        lines.push('右后: ' + (s.rearRightDoor ? '开' : '关'));
        lines.push('后备箱: ' + (s.trunkDoor ? '开' : '关'));
        lines.push('前机盖: ' + (s.hoodDoor ? '开' : '关'));
        lines.push('');
        lines.push('-- 灯光 --');
        lines.push('左转向: ' + (s.leftTurnLight ? '开' : '关'));
        lines.push('右转向: ' + (s.rightTurnLight ? '开' : '关'));
        lines.push('近光灯: ' + (s.isLowBeamLightOn ? '开' : '关'));
        lines.push('');
        lines.push('-- 其他 --');
        lines.push('蓝牙: ' + (s.bluetoothConnected ? '已连接' : '未连接'));
        lines.push('屏幕: ' + (s.screenOn ? '亮' : '灭'));
        lines.push('空调页: ' + (s.acPageOpen ? '开' : '关'));
        lines.push('360: ' + (s.camera360Visible ? '显示' : '隐藏'));
        lines.push('天窗: ' + (s.sunroof !== undefined ? s.sunroof : '未知'));
        lines.push('遮阳帘: ' + (s.sunshade !== undefined ? s.sunshade : '未知'));
        lines.push('');
        lines.push('-- 胎压/胎温 --');
        lines.push('左前: ' + (s.frontLeftTirePressure || '?') + 'kPa / ' + (s.frontLeftTireTemp || '?') + '°C');
        lines.push('右前: ' + (s.frontRightTirePressure || '?') + 'kPa / ' + (s.frontRightTireTemp || '?') + '°C');
        lines.push('左后: ' + (s.rearLeftTirePressure || '?') + 'kPa / ' + (s.rearLeftTireTemp || '?') + '°C');
        lines.push('右后: ' + (s.rearRightTirePressure || '?') + 'kPa / ' + (s.rearRightTireTemp || '?') + '°C');

        stateBox.textContent = lines.join('\n');
    }

    return {
        init: init,
        copyCmd: copyCmd,
        showPanel: showPanel,
        hidePanel: hidePanel
    };
})();

window.DebugTool = DebugTool;

// 自动初始化（在 DOM 和其他模块就绪后）
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', function() {
        setTimeout(function() { DebugTool.init(); }, 1000);
    });
} else {
    setTimeout(function() { DebugTool.init(); }, 1000);
}
