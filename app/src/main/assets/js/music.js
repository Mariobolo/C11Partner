/**
 * 音乐播放器模块
 * 
 * 功能说明：
 * 1. 实现音乐播放控制（播放、暂停、上一首、下一首）
 * 2. 实现音频可视化效果（柱状图、浮动块）
 * 3. 实现黑胶唱片旋转动画
 * 4. 实现进度条显示和拖动控制
 * 
 * 主要特性：
 * - 黑胶唱片样式：左侧显示黑胶唱片，播放时旋转
 * - 音频可视化：支持柱状图和浮动块两种效果
 * - 进度控制：显示当前播放进度，支持拖动调整
 * - 歌曲信息：显示当前播放歌曲名称
 */

// ==================== 音频相关变量 ====================
let audioContext = null;      // 音频上下文
let analyser = null;          // 音频分析器
let animationFrameId = null;  // 动画帧ID
let floats = [];              // 浮动块数组
let source = null;            // 音频源

// ==================== 音频可视化相关变量 ====================
let canvas = null;            // 画布元素
let canvasCtx = null;         // 画布上下文

// ==================== 音频可视化相关常量 ====================
const FLOAT_HEIGHT = 4;       // 浮动块高度
const DROP_DISTANCE = 1;       // 下降距离
const BAR_GAP = 2;            // 柱状图间距
const FFT_SIZE = 256;          // FFT大小

// ==================== 全局函数 ====================

/**
 * 对接Android后台获取音乐播放的声音帧率来展示动画
 * 
 * 功能说明：
 * 1. 接收Android传递的频率数据
 * 2. 解析JSON格式的频率数据
 * 3. 根据频率数据更新可视化效果
 * 
 * @param frequencyDataString 频率数据的JSON字符串
 */
function updateMusicVisualization(frequencyDataString) {
    // 解析频率数据
    let frequencyData = [];
    try {
        if (frequencyDataString) {
            frequencyData = JSON.parse(frequencyDataString);
        }
    } catch (e) {
        return;
    }
    
    // 如果没有频率数据，清空画布
    if (!frequencyData || frequencyData.length === 0) {
        clearCanvas();
        return;
    }
    
    // 确保画布上下文存在
    if (!canvas || !canvasCtx) {
        return;
    }
    
    // 更新长度（只取前50个）
    const bars = frequencyData.slice(0, 50);
    
    // 画图
    clearCanvas();
    drawFloats(bars);
    drawBars(bars);
}

// 供Android原生代码调用，更新音乐可视化数据
window.updateMusicVisualization = updateMusicVisualization;

// ==================== 音乐状态管理 ====================
let musicStatus = {
    musicName: '此刻无声，佳音已备候君启...',
    isPlaying: false,
    currentPosition: 0,
    duration: 0
};

/**
 * 更新音乐状态（供Android原生代码调用）
 * 
 * 功能说明：
 * 1. 接收Android传递的音乐状态信息
 * 2. 更新音乐组件的显示
 * 3. 更新播放按钮、唱片动画、唱针位置
 * 
 * @param status 音乐状态对象，包含musicName, isPlaying, currentPosition, duration
 */
function updateMusicStatus(status) {
    try {
        console.log('收到音乐状态更新:', status);
        
        // 更新状态
        musicStatus = {
            musicName: status.musicName || '此刻无声，佳音已备候君启...',
            isPlaying: status.isPlaying || false,
            currentPosition: status.currentPosition || 0,
            duration: status.duration || 0
        };
        
        // 更新UI
        updateMusicUI();
        // 确保跑马灯效果被正确应用
        setTimeout(() => {
            updateMusicUI();
        }, 100);
    } catch (error) {
        console.error('更新音乐状态时出错:', error);
    }
}

/**
 * 更新音乐组件UI
 */
function updateMusicUI() {
    // 更新歌曲名称
    const currentSongNameElement = document.getElementById('current-song-name');
    const musicHeader = document.querySelector('.music-header');
    
    if (currentSongNameElement && musicHeader) {
        currentSongNameElement.textContent = musicStatus.musicName;
        
        // 检测标题是否超出容器宽度
        // 使用offsetWidth和scrollWidth比较，判断是否需要跑马灯效果
        const isOverflow = currentSongNameElement.scrollWidth > currentSongNameElement.offsetWidth;
        
        if (isOverflow) {
            // 标题过长，启用跑马灯效果
            currentSongNameElement.classList.add('scrolling');
        } else {
            // 标题不长，不启用跑马灯
            currentSongNameElement.classList.remove('scrolling');
        }
    }
    
    // 更新播放进度条
    updateMusicProgressBar();
    
    // 更新播放按钮和动画状态
    updateMusicPlayState();
}

/**
 * 更新音乐播放进度条
 */
function updateMusicProgressBar() {
    const progressBar = document.querySelector('.music-progress-bar');
    const progressFilled = document.querySelector('.music-progress-filled');
    const currentTimeElement = document.querySelector('.music-current-time');
    const totalTimeElement = document.querySelector('.music-total-time');
    
    if (!progressBar || !progressFilled) return;
    
    // 计算进度百分比
    const progressPercent = musicStatus.duration > 0 
        ? (musicStatus.currentPosition / musicStatus.duration) * 100 
        : 0;
    
    // 更新进度条宽度
    progressFilled.style.width = progressPercent + '%';
    
    // 更新时间显示
    if (currentTimeElement) {
        // 如果没有获取到播放时间权限，显示提示信息
        if (musicStatus.currentPosition === 0 && musicStatus.duration === 0 && musicStatus.isPlaying) {
            currentTimeElement.textContent = '--:--';
        } else {
            currentTimeElement.textContent = formatMusicTime(musicStatus.currentPosition);
        }
    }
    if (totalTimeElement) {
        // 如果没有获取到播放时间权限，显示提示信息
        if (musicStatus.duration === 0 && musicStatus.isPlaying) {
            totalTimeElement.textContent = '--:--';
        } else {
            totalTimeElement.textContent = formatMusicTime(musicStatus.duration);
        }
    }
}

/**
 * 更新音乐播放状态（唱片、唱针动画）
 * 注意：播放按钮图标由 SystemMusicManager 统一管理，此处不更新
 */
function updateMusicPlayState() {
    const vinylRecord = document.querySelector('.vinyl-record');
    const tonearm = document.getElementById('tonearm');
    
    if (musicStatus.isPlaying) {
        if (vinylRecord) vinylRecord.classList.add('spinning');
        if (tonearm) tonearm.classList.add('playing');
    } else {
        if (vinylRecord) vinylRecord.classList.remove('spinning');
        if (tonearm) tonearm.classList.remove('playing');
    }
}

/**
 * 格式化音乐时间（毫秒转换为 mm:ss）
 * 
 * @param ms 毫秒数
 * @return 格式化后的时间字符串
 */
function formatMusicTime(ms) {
    if (!ms || ms <= 0) return '00:00';
    
    const totalSeconds = Math.floor(ms / 1000);
    const minutes = Math.floor(totalSeconds / 60);
    const seconds = totalSeconds % 60;
    
    return String(minutes).padStart(2, '0') + ':' + String(seconds).padStart(2, '0');
}

// 检查是否有通知监听权限
function hasNotificationAccess() {
    if (typeof Android !== 'undefined' && Android.hasNotificationAccess) {
        return Android.hasNotificationAccess();
    }
    return false;
}


// 供Android原生代码调用，更新音乐状态
window.updateMusicStatus = updateMusicStatus;

/**
 * 清除浮动块
 * 清空浮动块数组
 */
function clearFloats() {
    floats = [];
}

/**
 * 清除画布
 * 用背景色填充整个画布
 */
function clearCanvas() {
    if (!canvas || !canvasCtx) return;
    
    const canvasWidth = canvas.width;
    const canvasHeight = canvas.height;
    
    canvasCtx.fillStyle = 'rgb(29,19,62)';
    canvasCtx.fillRect(0, 0, canvasWidth, canvasHeight);
}

/**
 * 绘制上下跳动的方块
 * 
 * 功能说明：
 * 1. 根据音频数据计算方块高度
 * 2. 实现方块上下跳动效果
 * 3. 使用渐变色绘制方块
 * 
 * @param dataArray 音频数据数组
 */
function drawFloats(dataArray) {
    if (!canvas || !canvasCtx) return;
    
    const canvasWidth = canvas.width;
    const canvasHeight = canvas.height;
    
    // 计算每个方块的位置
    const barWidth = canvasWidth / dataArray.length;
    let x = 0;
    
    // 找到最大值，以及初始化高度
    dataArray.forEach((item, index) => {
        // 默认值
        floats[index] = floats[index] || {height: FLOAT_HEIGHT, y: canvasHeight - FLOAT_HEIGHT};
        // 处理当前值
        const pushHeight = item + FLOAT_HEIGHT;
        const dropHeight = floats[index].height - DROP_DISTANCE;
        // 取最大值作为新的高度
        floats[index].height = Math.max(dropHeight, pushHeight);
        // 根据高度计算y坐标（上下跳动）
        floats[index].y = canvasHeight - floats[index].height;
    });
    
    floats.forEach((floatItem) => {
        const floatHeight = floatItem.height;
        const yPosition = floatItem.y;
        
        canvasCtx.fillStyle = '#3e47a0';
        canvasCtx.fillRect(x, yPosition, barWidth, FLOAT_HEIGHT);
        
        x += barWidth + BAR_GAP;
    });
}

/**
 * 绘制柱状图
 * 
 * 功能说明：
 * 1. 根据音频数据绘制柱状图
 * 2. 使用渐变色美化效果
 * 3. 从底部向上绘制
 * 
 * @param dataArray 音频数据数组
 */
function drawBars(dataArray) {
    if (!canvas || !canvasCtx) return;
    
    const canvasWidth = canvas.width;
    const canvasHeight = canvas.height;
    
    const barWidth = canvasWidth / dataArray.length;
    let x = 0;
    
    dataArray.forEach((dataItem) => {
        const barHeight = dataItem;
        
        // 添加渐变色
        const gradient = canvasCtx.createLinearGradient(canvasWidth / 2, canvasHeight / 2, canvasWidth / 2, canvasHeight);
        gradient.addColorStop(0, '#68b3ec');
        gradient.addColorStop(0.5, '#4b5fc9');
        gradient.addColorStop(1, '#68b3ec');
        
        // 画 bar
        canvasCtx.fillStyle = gradient;
        canvasCtx.fillRect(x, canvasHeight - barHeight, barWidth, barHeight);
        
        x += barWidth + BAR_GAP;
    });
}

// 每帧绘制
function drawEachFrame() {
    if (!analyser) return;
    
    animationFrameId = requestAnimationFrame(drawEachFrame);
    
    // 准备数据数组
    const bufferLength = analyser.frequencyBinCount;
    const dataArray = new Uint8Array(bufferLength);
    
    // 读取数据
    analyser.getByteFrequencyData(dataArray);
    
    // 更新长度（只取前50个）
    const bars = dataArray.slice(0, 50);
    
    // 画图
    clearCanvas();
    drawFloats(bars);
    drawBars(bars);
}


