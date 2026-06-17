
// 加载壁纸设置
function loadWallpaperSettings() {
    if (typeof Android !== 'undefined' && Android.getWallpaperSettingsAsync) {
        const callbackId = AsyncCallbackManager.register(function (settingsJson) {
            try {
                const settings = JSON.parse(settingsJson);

                // 设置复选框状态
                const wallpaperCarouselCheckbox = document.getElementById('wallpaperCarouselCheckbox');
                if (wallpaperCarouselCheckbox) {
                    wallpaperCarouselCheckbox.checked = settings.wallpaper_carousel;
                }

                const randomModeCheckbox = document.getElementById('randomModeCheckbox');
                if (randomModeCheckbox) {
                    randomModeCheckbox.checked = settings.random_mode !== undefined ? settings.random_mode : false;
                }

                const specifiedModeCheckbox = document.getElementById('specifiedModeCheckbox');
                if (specifiedModeCheckbox) {
                    specifiedModeCheckbox.checked = settings.specified_mode !== undefined ? settings.specified_mode : false;
                }

                // 设置轮播间隔（转换为秒）
                const switchIntervalInput = document.getElementById('switchIntervalInput');
                if (switchIntervalInput) {
                    switchIntervalInput.value = settings.switch_interval / 1000;
                }
            } catch (e) {
                console.error('加载壁纸设置时出错:', e);
            }
        });
        Android.getWallpaperSettingsAsync(callbackId);
    } else if (typeof Android !== 'undefined' && Android.getWallpaperSettings) {
        // 回退到同步方法
        const settingsJson = Android.getWallpaperSettings();
        const settings = JSON.parse(settingsJson);

        // 设置复选框状态
        const wallpaperCarouselCheckbox = document.getElementById('wallpaperCarouselCheckbox');
        if (wallpaperCarouselCheckbox) {
            wallpaperCarouselCheckbox.checked = settings.wallpaper_carousel;
        }

        const randomModeCheckbox = document.getElementById('randomModeCheckbox');
        if (randomModeCheckbox) {
            randomModeCheckbox.checked = settings.random_mode !== undefined ? settings.random_mode : false;
        }

        const specifiedModeCheckbox = document.getElementById('specifiedModeCheckbox');
        if (specifiedModeCheckbox) {
            specifiedModeCheckbox.checked = settings.specified_mode !== undefined ? settings.specified_mode : false;
        }

        // 设置轮播间隔（转换为秒）
        const switchIntervalInput = document.getElementById('switchIntervalInput');
        if (switchIntervalInput) {
            switchIntervalInput.value = settings.switch_interval / 1000;
        }
    } else {
        // 模拟数据用于测试
        const wallpaperCarouselCheckbox = document.getElementById('wallpaperCarouselCheckbox');
        if (wallpaperCarouselCheckbox) {
            wallpaperCarouselCheckbox.checked = false;
        }

        const randomModeCheckbox = document.getElementById('randomModeCheckbox');
        if (randomModeCheckbox) {
            randomModeCheckbox.checked = false;
        }

        const specifiedModeCheckbox = document.getElementById('specifiedModeCheckbox');
        if (specifiedModeCheckbox) {
            specifiedModeCheckbox.checked = false;
        }

        const switchIntervalInput = document.getElementById('switchIntervalInput');
        if (switchIntervalInput) {
            switchIntervalInput.value = 15;
        }
    }
}

// 设置壁纸背景
function setWallpaperBackground() {
    // 检查是否在Android环境中
    // 优先使用URL接口而不是Base64接口，以确保在线壁纸能正确保存到本地
    if (typeof Android !== 'undefined' && Android.getRandomWallpaperAsync) {
        // 使用异步方式获取URL
        const callbackId = AsyncCallbackManager.register(function (wallpaperUrl) {
            if (wallpaperUrl && wallpaperUrl !== "") {
                // 预加载新壁纸
                const img = new Image();
                img.onload = function () {
                    // 创建新的背景元素实现淡入效果
                    const backgroundElement = document.querySelector('.background-image');
                    if (backgroundElement) {
                        // 添加淡出效果
                        backgroundElement.style.transition = 'opacity 0.3s ease-in-out';
                        backgroundElement.style.opacity = '0';

                        // 在淡出完成后设置新壁纸并淡入
                        setTimeout(() => {
                            backgroundElement.style.backgroundImage = `url('${wallpaperUrl}')`;
                            backgroundElement.style.opacity = '1';
                        }, 150);
                    }
                };

                img.onerror = function () {
                    // 加载失败时使用默认壁纸
                    const backgroundElement = document.querySelector('.background-image');
                    if (backgroundElement) {
                        backgroundElement.style.backgroundImage = "url('images/default_bg_1.jpg')";
                    }
                };

                img.src = wallpaperUrl;
            } else {
                // 没有获取到壁纸URL时使用默认壁纸
                const backgroundElement = document.querySelector('.background-image');
                if (backgroundElement) {
                    backgroundElement.style.backgroundImage = "url('images/default_bg_1.jpg')";
                }
            }
        });
        Android.getRandomWallpaperAsync(callbackId);
    } else if (typeof Android !== 'undefined' && Android.getRandomWallpaper) {
        // 回退到同步方式
        try {
            // 从原生代码获取随机壁纸URL
            const wallpaperUrl = Android.getRandomWallpaper();
            if (wallpaperUrl && wallpaperUrl !== "") {
                // 预加载新壁纸
                const img = new Image();
                img.onload = function () {
                    // 创建新的背景元素实现淡入效果
                    const backgroundElement = document.querySelector('.background-image');
                    if (backgroundElement) {
                        // 添加淡出效果
                        backgroundElement.style.transition = 'opacity 0.3s ease-in-out';
                        backgroundElement.style.opacity = '0';

                        // 在淡出完成后设置新壁纸并淡入
                        setTimeout(() => {
                            backgroundElement.style.backgroundImage = `url('${wallpaperUrl}')`;
                            backgroundElement.style.opacity = '1';
                        }, 150);
                    }
                };

                img.onerror = function () {
                    // 加载失败时使用默认壁纸
                    const backgroundElement = document.querySelector('.background-image');
                    if (backgroundElement) {
                        backgroundElement.style.backgroundImage = "url('images/default_bg_1.jpg')";
                    }
                };

                img.src = wallpaperUrl;
            } else {
                // 没有获取到壁纸URL时使用默认壁纸
                const backgroundElement = document.querySelector('.background-image');
                if (backgroundElement) {
                    backgroundElement.style.backgroundImage = "url('images/default_bg_1.jpg')";
                }
            }
        } catch (error) {
            // 发生错误时使用默认壁纸
            const backgroundElement = document.querySelector('.background-image');
            if (backgroundElement) {
                backgroundElement.style.backgroundImage = "url('images/default_bg_1.jpg')";
            }
        }
    } else if (typeof Android !== 'undefined' && Android.getRandomWallpaperBase64Async) {
        // 使用异步方式获取Base64数据接口
        const callbackId = AsyncCallbackManager.register(function (wallpaperBase64) {
            if (wallpaperBase64 && wallpaperBase64 !== "") {
                // 直接使用Base64数据设置壁纸
                const backgroundElement = document.querySelector('.background-image');
                if (backgroundElement) {
                    // 添加淡出效果
                    backgroundElement.style.transition = 'opacity 0.3s ease-in-out';
                    backgroundElement.style.opacity = '0';

                    // 在淡出完成后设置新壁纸并淡入
                    setTimeout(() => {
                        backgroundElement.style.backgroundImage = `url('data:image/jpeg;base64,${wallpaperBase64}')`;
                        backgroundElement.style.opacity = '1';
                    }, 150);
                }
            }
        });
        Android.getRandomWallpaperBase64Async(callbackId);
    } else if (typeof Android !== 'undefined' && Android.getRandomWallpaperBase64) {
        // 回退到同步方式
        try {
            // 优先使用Base64数据接口
            const wallpaperBase64 = Android.getRandomWallpaperBase64();
            if (wallpaperBase64 && wallpaperBase64 !== "") {
                // 直接使用Base64数据设置壁纸
                const backgroundElement = document.querySelector('.background-image');
                if (backgroundElement) {
                    // 添加淡出效果
                    backgroundElement.style.transition = 'opacity 0.3s ease-in-out';
                    backgroundElement.style.opacity = '0';

                    // 在淡出完成后设置新壁纸并淡入
                    setTimeout(() => {
                        backgroundElement.style.backgroundImage = `url('data:image/jpeg;base64,${wallpaperBase64}')`;
                        backgroundElement.style.opacity = '1';
                    }, 150);
                }
                return;
            }
        } catch (error) {
        }
    } else {
        // 非Android环境，使用默认背景
        const backgroundElement = document.querySelector('.background-image');
        if (backgroundElement) {
            backgroundElement.style.backgroundImage = "url('images/default_bg_1.jpg')";
        }
    }
}

// 壁纸轮播现在由后端控制，前端只负责显示
// startWallpaperCarousel 和 stopWallpaperCarousel 已移除

// 确保壁纸轮播设置正确应用
function ensureWallpaperCarouselSettings() {
    // 壁纸轮播现在由后端控制，前端不再管理定时器
    // 后端会根据应用前后台状态自动暂停/恢复轮播
    console.log('壁纸轮播由后端控制');
}

// 切换到上一张壁纸（异步版本，避免阻塞UI线程）
function nextWallpaper() {
    // 检查是否在Android环境中
    if (typeof Android !== 'undefined' && Android.getRandomWallpaperBase64Async) {
        // 使用异步方式获取Base64数据接口
        const callbackId = AsyncCallbackManager.register(function (wallpaperBase64) {
            if (wallpaperBase64 && wallpaperBase64 !== "") {
                // 直接使用Base64数据设置壁纸
                const backgroundElement = document.querySelector('.background-image');
                if (backgroundElement) {
                    // 添加淡出效果
                    backgroundElement.style.transition = 'opacity 0.3s ease-in-out';
                    backgroundElement.style.opacity = '0';

                    // 在淡出完成后设置新壁纸并淡入
                    setTimeout(() => {
                        backgroundElement.style.backgroundImage = `url('data:image/jpeg;base64,${wallpaperBase64}')`;
                        backgroundElement.style.opacity = '1';
                        showToast('上一张壁纸');
                    }, 150);
                }
            }
        });
        Android.getRandomWallpaperBase64Async(callbackId);

    }
}

// 恢复到下一张壁纸（异步版本，避免阻塞UI线程）
function previousWallpaper() {
    // 检查是否在Android环境中
    if (typeof Android !== 'undefined' && Android.getRandomWallpaperBase64Async) {
        // 使用异步方式获取Base64数据接口
        const callbackId = AsyncCallbackManager.register(function (wallpaperBase64) {
            if (wallpaperBase64 && wallpaperBase64 !== "") {
                // 直接使用Base64数据设置壁纸
                const backgroundElement = document.querySelector('.background-image');
                if (backgroundElement) {
                    // 添加淡出效果
                    backgroundElement.style.transition = 'opacity 0.3s ease-in-out';
                    backgroundElement.style.opacity = '0';

                    // 在淡出完成后设置新壁纸并淡入
                    setTimeout(() => {
                        backgroundElement.style.backgroundImage = `url('data:image/jpeg;base64,${wallpaperBase64}')`;
                        backgroundElement.style.opacity = '1';
                        showToast('下一张壁纸');
                    }, 150);
                }
            }
        });
        Android.getRandomWallpaperBase64Async(callbackId);
    }
}
