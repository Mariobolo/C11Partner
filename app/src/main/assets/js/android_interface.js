// Android接口模拟
// 在非Android环境中提供模拟接口，方便开发和测试

// 检查是否在Android环境中
if (typeof Android === 'undefined') {
    // 创建模拟的Android对象
    window.Android = {
        // 音乐可视化相关接口
        startMusicVisualization: function() {
            // 模拟启动音乐可视化
        },
        
        stopMusicVisualization: function() {
            // 模拟停止音乐可视化
        },
        
        // 音乐控制相关接口
        playPauseMusic: function() {
            // 模拟播放音乐
        },
        
        nextMusic: function() {
            // 模拟暂停音乐
        },
        
        // 其他接口...
        isMusicPlaying: function() {
            return isMusicPlayingState;
        }
    };
    
    // 模拟音乐播放状态
    let isMusicPlayingState = false;
    
    // 模拟音乐播放/暂停切换
    setInterval(function() {
        isMusicPlayingState = !isMusicPlayingState;
    }, 5000);
} else {
    // 生产环境，Android接口模拟已禁用
}

// Android接口模拟文件，用于开发和测试

// 只在开发环境中启用模拟代码
const isDevelopment = location.hostname === 'localhost' || location.hostname === '127.0.0.1';

// 模拟音乐播放状态
let isMusicPlayingState = false;
let lastFrequencyData = null; // 存储上一次的频率数据
let simulationInterval = null; // 模拟数据生成间隔

// 生成模拟频率数据的函数
function generateFrequencyData() {
    // 生成一些随机但有规律的数据来模拟音频频率
    const frequencyData = [];
    const time = Date.now();
    
    for (let i = 0; i < 64; i++) { // 与实际数据长度保持一致
        // 创建多个波形叠加效果，模拟真实音乐
        const wave1 = Math.abs(Math.sin(time / 300 + i / 10) * 60);
        const wave2 = Math.abs(Math.sin(time / 150 + i / 20) * 30);
        const wave3 = Math.abs(Math.cos(time / 200 + i / 15) * 20);
        
        // 组合波形并添加基础值
        let value = wave1 + wave2 + wave3 + 20;
        
        // 添加随机变化，模拟音乐的不规则性
        value += Math.random() * 15;
        
        // 确保值在合理范围内
        value = Math.max(0, Math.min(100, value));
        
        frequencyData.push(Math.floor(value));
    }
    
    return frequencyData;
}

// 开始模拟音乐播放
function startMusicSimulation() {
    // 只在开发环境中运行
    if (!isDevelopment) return;
    
    if (simulationInterval) {
        clearInterval(simulationInterval);
    }
    
    // 每150ms生成一次模拟数据
    simulationInterval = setInterval(() => {
        if (isMusicPlayingState && typeof window.updateMusicVisualization === 'function') {
            const frequencyData = generateFrequencyData();
            window.updateMusicVisualization(frequencyData);
        }
    }, 150);
}

// 停止模拟音乐播放
function stopMusicSimulation() {
    // 只在开发环境中运行
    if (!isDevelopment) return;
    
    if (simulationInterval) {
        clearInterval(simulationInterval);
        simulationInterval = null;
    }
    lastFrequencyData = null; // 清空上一次的数据
}

// 模拟Android对象 - 只在开发环境中创建
if (isDevelopment) {
    // 模拟Android对象
    window.Android = {
        // 模拟音乐播放状态
        isMusicPlaying: function() {
            // 在实际应用中，这将由Android原生代码提供
            return isMusicPlayingState;
        },
        
        // 模拟音乐可视化是否可用
        isMusicVisualizerAvailable: function() {
            // 在实际应用中，这将由Android原生代码提供
            return true;
        },
        
        // 模拟启动音乐可视化
        startMusicVisualizer: function() {
            // 在实际应用中，这将由Android原生代码提供
            isMusicPlayingState = true;
            startMusicSimulation(); // 开始模拟数据生成
        },
        
        // 模拟停止音乐可视化
        stopMusicVisualizer: function() {
            // 在实际应用中，这将由Android原生代码提供
            isMusicPlayingState = false;
            stopMusicSimulation(); // 停止模拟数据生成
        },
        
        // 模拟获取音乐频率数据
        getMusicFrequencyData: function() {
            // 只有在音乐播放时才返回频率数据
            if (!isMusicPlayingState) {
                return [];
            }
            
            // 生成模拟频率数据
            const frequencyData = generateFrequencyData();
            
            // 检查数据是否与上一次相同，避免重复返回相同数据
            if (lastFrequencyData && JSON.stringify(lastFrequencyData) === JSON.stringify(frequencyData)) {
                return []; // 返回空数组，避免重复渲染
            }
            
            // 保存当前数据作为上一次数据
            lastFrequencyData = frequencyData;
            
            // 在实际应用中，这将由Android原生代码提供真实的频率数据
            return frequencyData;
        },
        
        // 模拟播放音乐
        playMusic: function() {
            isMusicPlayingState = true;
            startMusicSimulation(); // 开始模拟数据生成
        },
        
        // 模拟暂停音乐
        pauseMusic: function() {
            isMusicPlayingState = false;
            stopMusicSimulation(); // 停止模拟数据生成
        }
    };

    // 添加键盘事件监听器，用于测试
    document.addEventListener('keydown', function(event) {
        // 按空格键切换播放/暂停状态
        if (event.code === 'Space') {
            event.preventDefault(); // 阻止默认行为
            if (isMusicPlayingState) {
                Android.pauseMusic();
            } else {
                Android.playMusic();
            }
        }
    });
} else {
}