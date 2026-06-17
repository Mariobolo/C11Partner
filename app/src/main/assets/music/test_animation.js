// 测试动画脚本
console.log("测试动画脚本已加载");

// 简单的动画测试
function testMusicAnimation() {
    console.log("开始测试音乐动画");
    
    const canvas = document.getElementById('canvas');
    if (!canvas) {
        console.error("未找到canvas元素");
        return;
    }
    
    const ctx = canvas.getContext('2d');
    if (!ctx) {
        console.error("无法获取canvas上下文");
        return;
    }
    
    // 设置canvas尺寸
    canvas.width = 280;
    canvas.height = 200;
    
    console.log("Canvas尺寸:", canvas.width, canvas.height);
    
    let animationId = null;
    let time = 0;
    
    // 生成测试数据（更慢更随机）
    function generateTestData() {
        const data = new Uint8Array(50);
        const slowTime = time * 0.2; // 进一步减慢时间因子
        
        for (let i = 0; i < data.length; i++) {
            // 生成更加随机和自然的数据来模拟音频，变化更慢
            data[i] = Math.floor(
                Math.abs(Math.sin(slowTime / 500 + i / 10) * 100) + 
                Math.abs(Math.sin(slowTime / 250 + i / 20) * 70) +
                Math.abs(Math.cos(slowTime / 300 + i / 15) * 50) +
                Math.random() * 40
            );
        }
        return data;
    }
    
    // 控制帧率的绘制函数
    let lastTime = 0;
    const frameInterval = 300; // 增加到300毫秒，进一步减慢动画更新频率
    
    function controlledDraw(timestamp) {
        // 控制帧率
        if (timestamp - lastTime < frameInterval) {
            animationId = requestAnimationFrame(controlledDraw);
            return;
        }
        
        lastTime = timestamp;
        
        // 清除画布
        ctx.fillStyle = 'rgb(29,19,62)';
        ctx.fillRect(0, 0, canvas.width, canvas.height);
        
        // 生成测试数据
        const data = generateTestData();
        
        // 绘制柱状图
        const barWidth = canvas.width / data.length;
        for (let i = 0; i < data.length; i++) {
            const barHeight = data[i];
            const x = i * barWidth;
            const y = canvas.height - barHeight;
            
            // 创建渐变色
            const gradient = ctx.createLinearGradient(0, y, 0, canvas.height);
            gradient.addColorStop(0, '#68b3ec');
            gradient.addColorStop(0.5, '#4b5fc9');
            gradient.addColorStop(1, '#68b3ec');
            
            ctx.fillStyle = gradient;
            ctx.fillRect(x, y, barWidth - 2, barHeight);
        }
        
        // 绘制跳动的方块
        for (let i = 0; i < data.length; i++) {
            const barHeight = data[i];
            const x = i * barWidth;
            const y = canvas.height - barHeight - 10;
            
            ctx.fillStyle = '#3e47a0';
            ctx.fillRect(x, y, barWidth - 2, 4);
        }
        
        time++;
        animationId = requestAnimationFrame(controlledDraw);
    }
    
    // 启动动画
    console.log("启动动画");
    controlledDraw();
    
    // 提供停止动画的方法
    window.stopTestAnimation = function() {
        if (animationId) {
            cancelAnimationFrame(animationId);
            console.log("动画已停止");
        }
    };
    
    return animationId;
}

// 页面加载完成后启动测试
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', function() {
        // 延迟1秒启动测试动画
        setTimeout(testMusicAnimation, 1000);
    });
} else {
    // 延迟1秒启动测试动画
    setTimeout(testMusicAnimation, 1000);
}

// 也提供一个可以手动调用的函数
window.runTestAnimation = function() {
    console.log("手动启动测试动画");
    return testMusicAnimation();
};

// 添加按钮事件监听器
document.addEventListener('DOMContentLoaded', function() {
    const runTestBtn = document.getElementById('runTestAnimationBtn');
    if (runTestBtn) {
        runTestBtn.addEventListener('click', function() {
            console.log("运行测试动画按钮被点击");
            window.runTestAnimation();
        });
    }
});