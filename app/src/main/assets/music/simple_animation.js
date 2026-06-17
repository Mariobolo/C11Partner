// 简单的动画测试脚本
function simpleAnimation() {
    const canvas = document.getElementById('canvas');
    if (!canvas) {
        console.error('Canvas元素未找到');
        return;
    }
    
    const ctx = canvas.getContext('2d');
    if (!ctx) {
        console.error('无法获取Canvas上下文');
        return;
    }
    
    // 设置canvas尺寸
    canvas.width = 280;
    canvas.height = 200;
    
    let x = 0;
    let time = 0;
    
    // 生成更随机的Y位置
    function getRandomY() {
        return 80 + Math.sin(time / 30) * 20 + Math.random() * 15;
    }
    
    // 控制帧率的绘制函数
    let lastTime = 0;
    const frameInterval = 300; // 增加到300毫秒，进一步减慢动画更新频率
    
    function controlledDraw(timestamp) {
        // 控制帧率
        if (timestamp - lastTime < frameInterval) {
            requestAnimationFrame(controlledDraw);
            return;
        }
        
        lastTime = timestamp;
        
        // 清除画布
        ctx.fillStyle = 'rgb(29,19,62)';
        ctx.fillRect(0, 0, canvas.width, canvas.height);
        
        // 绘制多个移动的方块
        for (let i = 0; i < 5; i++) {
            const offsetX = (x + i * 60) % canvas.width;
            const offsetY = getRandomY() + i * 10;
            
            ctx.fillStyle = '#3e47a0';
            ctx.fillRect(offsetX, offsetY, 20, 20);
        }
        
        // 更新位置（更慢的速度）
        x = (x + 0.2) % canvas.width;
        time++;
        
        // 继续动画
        requestAnimationFrame(controlledDraw);
    }
    
    // 开始动画
    controlledDraw();
    
    // 添加测试按钮事件监听器
    const testBtn = document.getElementById('testAnimationBtn');
    if (testBtn) {
        testBtn.addEventListener('click', function() {
            console.log('测试按钮被点击');
            // 重新开始简单动画
            x = 0;
            time = 0;
            controlledDraw();
        });
    }
}

// 页面加载完成后启动简单动画
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', simpleAnimation);
} else {
    simpleAnimation();
}