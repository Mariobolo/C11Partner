/**
 * 壁纸管理器
 * 统一管理各种类型的壁纸：默认、必应、本地图片、本地视频、本地文件夹、iframe（预留）
 */

const WallpaperManager = (function() {
    // 壁纸类型常量
    const TYPE_DEFAULT = 0;      // 默认壁纸
    const TYPE_BING = 1;         // 必应壁纸
    const TYPE_LOCAL_IMAGE = 2;  // 本地图片
    const TYPE_LOCAL_VIDEO = 3;  // 本地视频
    const TYPE_LOCAL_FOLDER = 4; // 本地文件夹轮播
    const TYPE_IFRAME = 5;       // iframe壁纸（预留）

    // 填充模式
    const FILL_MODE_COVER = 0;   // 填充（裁剪）
    const FILL_MODE_CONTAIN = 1; // 包含（留白）
    const FILL_MODE_STRETCH = 2; // 拉伸

    // 状态
    let currentSettings = {
        wallpaper_type: TYPE_DEFAULT,
        wallpaper_path: '',
        carousel_enabled: false,
        carousel_interval: 15000,
        fill_mode: FILL_MODE_COVER
    };

    let carouselTimer = null;
    let isInitialized = false;

    /**
     * 初始化
     */
    function init() {
        if (isInitialized) return;
        isInitialized = true;

        // 加载设置
        loadSettings();

        // 设置初始壁纸
        applyWallpaper();

        // 启动轮播（如果启用）
        if (currentSettings.carousel_enabled) {
            startCarousel();
        }
    }

    /**
     * 加载设置
     */
    function loadSettings() {
        if (typeof Android !== 'undefined' && Android.getWallpaperSettingsV2) {
            try {
                const settingsJson = Android.getWallpaperSettingsV2();
                if (settingsJson) {
                    currentSettings = JSON.parse(settingsJson);
                }
            } catch (e) {
                console.error('加载壁纸设置失败:', e);
            }
        }
    }

    /**
     * 保存设置
     */
    function saveSettings() {
        if (typeof Android !== 'undefined') {
            if (Android.setWallpaperType) {
                Android.setWallpaperType(currentSettings.wallpaper_type);
            }
            if (Android.setWallpaperPath) {
                Android.setWallpaperPath(currentSettings.wallpaper_path);
            }
            if (Android.setWallpaperCarouselEnabled) {
                Android.setWallpaperCarouselEnabled(currentSettings.carousel_enabled);
            }
            if (Android.setWallpaperCarouselInterval) {
                Android.setWallpaperCarouselInterval(currentSettings.carousel_interval);
            }
            if (Android.setWallpaperFillMode) {
                Android.setWallpaperFillMode(currentSettings.fill_mode);
            }
        }
    }

    /**
     * 应用壁纸
     */
    function applyWallpaper() {
        const bgElement = document.querySelector('.background-image');
        if (!bgElement) return;

        let wallpaperUrl = getCurrentWallpaperUrl();

        // 根据壁纸类型处理
        switch (currentSettings.wallpaper_type) {
            case TYPE_LOCAL_VIDEO:
                // 视频壁纸
                applyVideoWallpaper(wallpaperUrl);
                break;
            case TYPE_IFRAME:
                // iframe壁纸
                applyIframeWallpaper(wallpaperUrl || '3d/index.html');
                break;
            case TYPE_DEFAULT:
            case TYPE_BING:
            case TYPE_LOCAL_IMAGE:
            case TYPE_LOCAL_FOLDER:
            default:
                // 图片壁纸
                applyImageWallpaper(wallpaperUrl);
                break;
        }

        // 应用填充模式
        applyFillMode();
    }

    /**
     * 应用图片壁纸
     */
    function applyImageWallpaper(url) {
        const bgElement = document.querySelector('.background-image');
        if (!bgElement) return;

        // 确保是图片模式
        bgElement.style.display = 'block';

        // 预加载图片
        const img = new Image();
        img.onload = function() {
            // 淡入效果
            bgElement.style.transition = 'opacity 0.5s ease-in-out';
            bgElement.style.opacity = '0';
            
            setTimeout(() => {
                bgElement.style.backgroundImage = `url('${url}')`;
                bgElement.style.opacity = '1';
            }, 300);
        };
        img.onerror = function() {
            console.error('壁纸加载失败:', url);
            // 加载失败，使用默认壁纸
            bgElement.style.backgroundImage = "url('images/default_bg_1.jpg')";
        };
        img.src = url;
    }

    /**
     * 应用视频壁纸
     */
    function applyVideoWallpaper(url) {
        // 检查是否已有视频元素
        let videoBg = document.getElementById('video-wallpaper');
        
        if (!videoBg) {
            videoBg = document.createElement('video');
            videoBg.id = 'video-wallpaper';
            videoBg.autoplay = true;
            videoBg.loop = true;
            videoBg.muted = true;
            videoBg.playsInline = true;
            videoBg.style.cssText = `
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                object-fit: cover;
                z-index: -1;
            `;
            
            const bgContainer = document.querySelector('.background-container');
            if (bgContainer) {
                bgContainer.appendChild(videoBg);
            }
        }

        videoBg.src = url;
        videoBg.play().catch(e => console.error('视频播放失败:', e));

        // 隐藏图片背景
        const bgElement = document.querySelector('.background-image');
        if (bgElement) {
            bgElement.style.display = 'none';
        }
    }


    /**
     * 应用iframe壁纸（预留功能，暂时禁用）
     * 注意：当前版本暂不启用iframe壁纸，会自动降级为默认图片壁纸
     */
    function applyIframeWallpaper(url) {
        console.log('应用iframe壁纸:', url);
        
        let iframeBg = document.getElementById('iframe-wallpaper');
        
        if (!iframeBg) {
            iframeBg = document.createElement('iframe');
            iframeBg.id = 'iframe-wallpaper';
            iframeBg.style.cssText = `
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                border: none;
                z-index: -1;
            `;
            iframeBg.setAttribute('allow', 'autoplay; fullscreen');
            iframeBg.setAttribute('scrolling', 'no');
            
            const bgContainer = document.querySelector('.background-container');
            if (bgContainer) {
                bgContainer.appendChild(iframeBg);
            }
        }

        iframeBg.src = url;

        // 隐藏图片背景
        const bgElement = document.querySelector('.background-image');
        if (bgElement) {
            bgElement.style.display = 'none';
        }
        
        // 隐藏壁纸预览层
        const previewElement = document.getElementById('wallpaperPreview');
        if (previewElement) {
            previewElement.style.display = 'none';
        }
    }

    /**
     * 应用填充模式
     */
    function applyFillMode() {
        const bgElement = document.querySelector('.background-image');
        if (!bgElement) return;

        switch (currentSettings.fill_mode) {
            case FILL_MODE_COVER:
                bgElement.style.backgroundSize = 'cover';
                bgElement.style.backgroundPosition = 'center center';
                break;
            case FILL_MODE_CONTAIN:
                bgElement.style.backgroundSize = 'contain';
                bgElement.style.backgroundPosition = 'center center';
                bgElement.style.backgroundRepeat = 'no-repeat';
                break;
            case FILL_MODE_STRETCH:
                bgElement.style.backgroundSize = '100% 100%';
                bgElement.style.backgroundPosition = 'center center';
                break;
        }
    }

    /**
     * 获取当前壁纸URL
     */
    function getCurrentWallpaperUrl() {
        if (typeof Android !== 'undefined' && Android.getCurrentWallpaperUrl) {
            try {
                return Android.getCurrentWallpaperUrl();
            } catch (e) {
                console.error('获取当前壁纸URL失败:', e);
            }
        }
        return 'images/default_bg_1.jpg';
    }

    /**
     * 获取下一张壁纸URL
     */
    function getNextWallpaperUrl() {
        if (typeof Android !== 'undefined' && Android.getNextWallpaperUrl) {
            try {
                return Android.getNextWallpaperUrl();
            } catch (e) {
                console.error('获取下一张壁纸URL失败:', e);
            }
        }
        return 'images/default_bg_1.jpg';
    }

    /**
     * 切换到下一张壁纸
     */
    function nextWallpaper() {
        const url = getNextWallpaperUrl();
        
        // 视频壁纸特殊处理
        if (currentSettings.wallpaper_type === TYPE_LOCAL_VIDEO) {
            applyVideoWallpaper(url);
        } else {
            applyImageWallpaper(url);
        }
    }

    /**
     * 启动轮播
     */
    function startCarousel() {
        stopCarousel();

        if (currentSettings.carousel_interval > 0) {
            carouselTimer = setInterval(() => {
                nextWallpaper();
            }, currentSettings.carousel_interval);
        }
    }

    /**
     * 停止轮播
     */
    function stopCarousel() {
        if (carouselTimer) {
            clearInterval(carouselTimer);
            carouselTimer = null;
        }
    }

    /**
     * 重启轮播
     */
    function restartCarousel() {
        if (currentSettings.carousel_enabled) {
            startCarousel();
        } else {
            stopCarousel();
        }
    }

    // ==================== 设置方法 ====================

    /**
     * 设置壁纸类型
     */
    function setWallpaperType(type) {
        currentSettings.wallpaper_type = type;
        saveSettings();
        applyWallpaper();
        restartCarousel();
    }

    /**
     * 设置壁纸路径
     */
    function setWallpaperPath(path) {
        currentSettings.wallpaper_path = path;
        saveSettings();
        applyWallpaper();
    }

    /**
     * 设置是否启用轮播
     */
    function setCarouselEnabled(enabled) {
        currentSettings.carousel_enabled = enabled;
        saveSettings();
        restartCarousel();
    }

    /**
     * 设置轮播间隔
     */
    function setCarouselInterval(intervalMs) {
        currentSettings.carousel_interval = intervalMs;
        saveSettings();
        restartCarousel();
    }

    /**
     * 设置填充模式
     */
    function setFillMode(mode) {
        currentSettings.fill_mode = mode;
        saveSettings();
        applyFillMode();
    }

    /**
     * 刷新必应壁纸
     */
    function refreshBingWallpaper() {
        if (typeof Android !== 'undefined' && Android.refreshBingWallpaper) {
            Android.refreshBingWallpaper();
        }
        // 延迟一下再应用壁纸
        setTimeout(() => {
            applyWallpaper();
        }, 2000);
    }

    /**
     * 获取当前设置
     */
    function getSettings() {
        return { ...currentSettings };
    }

    /**
     * 获取壁纸类型名称
     */
    function getTypeName(type) {
        const names = {
            [TYPE_DEFAULT]: '默认壁纸',
            [TYPE_BING]: '必应壁纸',
            [TYPE_LOCAL_IMAGE]: '本地图片',
            [TYPE_LOCAL_VIDEO]: '本地视频',
            [TYPE_LOCAL_FOLDER]: '本地文件夹',
            [TYPE_IFRAME]: 'iframe壁纸'
        };
        return names[type] || '未知';
    }

    /**
     * 获取填充模式名称
     */
    function getFillModeName(mode) {
        const names = {
            [FILL_MODE_COVER]: '填充',
            [FILL_MODE_CONTAIN]: '包含',
            [FILL_MODE_STRETCH]: '拉伸'
        };
        return names[mode] || '填充';
    }

    // 暴露公共方法
    return {
        // 常量
        TYPE_DEFAULT,
        TYPE_BING,
        TYPE_LOCAL_IMAGE,
        TYPE_LOCAL_VIDEO,
        TYPE_LOCAL_FOLDER,
        TYPE_IFRAME,
        FILL_MODE_COVER,
        FILL_MODE_CONTAIN,
        FILL_MODE_STRETCH,
        
        // 方法
        init,
        loadSettings,
        applyWallpaper,
        nextWallpaper,
        startCarousel,
        stopCarousel,
        restartCarousel,
        setWallpaperType,
        setWallpaperPath,
        setCarouselEnabled,
        setCarouselInterval,
        setFillMode,
        refreshBingWallpaper,
        getSettings,
        getTypeName,
        getFillModeName
    };
})();

// 页面加载完成后初始化
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', function() {
        WallpaperManager.init();
    });
} else {
    WallpaperManager.init();
}


// ==================== 设置界面初始化 ====================

/**
 * 初始化壁纸设置界面
 */
function initWallpaperSettingsUI() {
    // 加载当前设置到UI
    loadSettingsToUI();
    
    // 绑定事件
    bindSettingsEvents();
}

/**
 * 加载设置到UI
 */
function loadSettingsToUI() {
    const settings = WallpaperManager.getSettings();
    
    // 壁纸类型
    const typeRadios = document.querySelectorAll('input[name="wallpaperType"]');
    typeRadios.forEach(radio => {
        radio.checked = parseInt(radio.value) === settings.wallpaper_type;
    });
    
    // 更新路径区域显示
    updatePathSectionVisibility(settings.wallpaper_type);
    
    // 更新必应区域显示
    updateBingSectionVisibility(settings.wallpaper_type);
    
    // 壁纸路径
    const pathInput = document.getElementById('wallpaperPathInput');
    if (pathInput) {
        pathInput.value = settings.wallpaper_path;
    }
    
    // 轮播
    const carouselCheckbox = document.getElementById('wallpaperCarouselV2Checkbox');
    if (carouselCheckbox) {
        carouselCheckbox.checked = settings.carousel_enabled;
    }
    
    // 轮播间隔
    const intervalInput = document.getElementById('wallpaperIntervalV2Input');
    if (intervalInput) {
        intervalInput.value = Math.round(settings.carousel_interval / 1000);
    }
    
    // 填充模式
    const fillModeSelect = document.getElementById('wallpaperFillModeSelect');
    if (fillModeSelect) {
        fillModeSelect.value = settings.fill_mode;
    }
}

/**
 * 绑定设置界面事件
 */
function bindSettingsEvents() {
    // 壁纸类型切换
    const typeRadios = document.querySelectorAll('input[name="wallpaperType"]');
    typeRadios.forEach(radio => {
        radio.addEventListener('change', function() {
            const type = parseInt(this.value);
            updatePathSectionVisibility(type);
            updateBingSectionVisibility(type);
        });
    });
    
    // 应用设置按钮
    const applyBtn = document.getElementById('applyWallpaperBtn');
    if (applyBtn) {
        applyBtn.addEventListener('click', function() {
            applyWallpaperSettings();
        });
    }
    
    // 刷新必应壁纸按钮
    const refreshBingBtn = document.getElementById('refreshBingWallpaperBtn');
    if (refreshBingBtn) {
        refreshBingBtn.addEventListener('click', function() {
            WallpaperManager.refreshBingWallpaper();
            alert('正在刷新必应壁纸，请稍候...');
        });
    }
    
    // 选择文件按钮
    const pickFileBtn = document.getElementById('pickWallpaperFileBtn');
    if (pickFileBtn) {
        pickFileBtn.addEventListener('click', function() {
            if (typeof Android !== 'undefined' && Android.pickLocalWallpaperFile) {
                Android.pickLocalWallpaperFile();
            } else {
                alert('请在输入框中手动输入文件路径');
            }
        });
    }
    
    // 选择文件夹按钮
    const pickFolderBtn = document.getElementById('pickWallpaperFolderBtn');
    if (pickFolderBtn) {
        pickFolderBtn.addEventListener('click', function() {
            if (typeof Android !== 'undefined' && Android.pickLocalWallpaperFolder) {
                Android.pickLocalWallpaperFolder();
            } else {
                alert('请在输入框中手动输入文件夹路径');
            }
        });
    }
}

/**
 * 更新路径区域显示
 */
function updatePathSectionVisibility(type) {
    const pathSection = document.getElementById('wallpaperPathSection');
    if (!pathSection) return;
    
    // 本地图片、本地视频、本地文件夹显示路径输入
    if (type === WallpaperManager.TYPE_LOCAL_IMAGE || 
        type === WallpaperManager.TYPE_LOCAL_VIDEO || 
        type === WallpaperManager.TYPE_LOCAL_FOLDER) {
        pathSection.style.display = 'block';
    } else {
        pathSection.style.display = 'none';
    }
}

/**
 * 更新必应区域显示
 */
function updateBingSectionVisibility(type) {
    const bingSection = document.getElementById('bingWallpaperSection');
    if (!bingSection) return;
    
    if (type === WallpaperManager.TYPE_BING) {
        bingSection.style.display = 'block';
    } else {
        bingSection.style.display = 'none';
    }
}

/**
 * 应用壁纸设置
 */
function applyWallpaperSettings() {
    // 获取壁纸类型
    const typeRadio = document.querySelector('input[name="wallpaperType"]:checked');
    if (typeRadio) {
        const type = parseInt(typeRadio.value);
        WallpaperManager.setWallpaperType(type);
    }
    
    // 获取壁纸路径
    const pathInput = document.getElementById('wallpaperPathInput');
    if (pathInput) {
        WallpaperManager.setWallpaperPath(pathInput.value);
    }
    
    // 获取轮播设置
    const carouselCheckbox = document.getElementById('wallpaperCarouselV2Checkbox');
    if (carouselCheckbox) {
        WallpaperManager.setCarouselEnabled(carouselCheckbox.checked);
    }
    
    // 获取轮播间隔
    const intervalInput = document.getElementById('wallpaperIntervalV2Input');
    if (intervalInput) {
        const intervalSec = parseInt(intervalInput.value) || 15;
        WallpaperManager.setCarouselInterval(intervalSec * 1000);
    }
    
    // 获取填充模式
    const fillModeSelect = document.getElementById('wallpaperFillModeSelect');
    if (fillModeSelect) {
        WallpaperManager.setFillMode(parseInt(fillModeSelect.value));
    }
    
    // 提示成功
    alert('壁纸设置已应用');
}

// 页面加载完成后初始化设置界面
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', function() {
        // 延迟初始化，确保设置面板已加载
        setTimeout(() => {
            initWallpaperSettingsUI();
        }, 500);
    });
} else {
    setTimeout(() => {
        initWallpaperSettingsUI();
    }, 500);
}

