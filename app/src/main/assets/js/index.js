/**
 * 全局回调管理器
 * 
 * 功能说明：
 * 1. 管理所有异步回调函数
 * 2. 为每个回调生成唯一ID
 * 3. 执行回调后自动清理
 * 
 * 使用场景：
 * - 与Android原生代码的异步通信
 * - 处理需要等待返回结果的操作
 */
const AsyncCallbackManager = {
    callbacks: {},
    counter: 0,

    /**
     * 生成唯一的回调ID
     * 
     * @return 唯一的回调ID字符串
     */
    generateId: function () {
        return 'callback_' + (++this.counter);
    },

    /**
     * 注册回调函数
     * 
     * @param callback 要注册的回调函数
     * @return 回调ID
     */
    register: function (callback) {
        const id = this.generateId();
        this.callbacks[id] = callback;
        return id;
    },

    /**
     * 执行回调函数
     * 执行后自动删除回调，避免内存泄漏
     * 
     * @param id 回调ID
     * @param result 回调结果
     */
    execute: function (id, result) {
        if (this.callbacks[id]) {
            this.callbacks[id](result);
            delete this.callbacks[id];
        }
    }
};

// 标志：是否正在加载设置
let isLoadingSettings = false;

// 标志：是否已经初始化了分类复选框事件
let categoryCheckboxEventsInitialized = false;

/**
 * 添加按钮点击效果
 * 
 * 功能说明：
 * 1. 为所有按钮添加按下和释放的视觉效果
 * 2. 支持鼠标和触摸事件
 * 3. 为音乐控制按钮添加特殊播放效果
 * 
 * 视觉效果：
 * - 按下时添加button-pressed类
 * - 释放时移除button-pressed类
 * - 音乐播放时添加playing类，3秒后移除
 */
function addClickEffect() {
    // 获取所有需要添加点击效果的按钮元素
    const buttons = document.querySelectorAll('.control-button, .control-icon, .ac-control, .ac-hcs, .nav-button');

    // 为每个按钮添加点击效果
    buttons.forEach(button => {
        // 添加按下效果
        button.addEventListener('mousedown', function () {
            this.classList.add('button-pressed');
        });

        // 添加释放效果
        button.addEventListener('mouseup', function () {
            this.classList.remove('button-pressed');
        });

        // 添加鼠标离开时释放效果
        button.addEventListener('mouseleave', function () {
            this.classList.remove('button-pressed');
        });

        // 添加触摸开始效果（移动端）
        button.addEventListener('touchstart', function () {
            this.classList.add('button-pressed');
        });

        // 添加触摸结束效果（移动端）
        button.addEventListener('touchend', function () {
            this.classList.remove('button-pressed');
        });

        // 添加触摸取消效果（移动端）
        button.addEventListener('touchcancel', function () {
            this.classList.remove('button-pressed');
        });
    });

    // 为音乐控制按钮添加特殊效果
    const musicPauseBtn = document.getElementById('musicPauseBtn');
    if (musicPauseBtn) {
        musicPauseBtn.addEventListener('click', function () {
            const musicControl = document.querySelector('.music-control');
            if (musicControl) {
                // 切换播放状态效果
                if (musicControl.classList.contains('playing')) {
                    musicControl.classList.remove('playing');
                } else {
                    musicControl.classList.add('playing');
                    // 3秒后移除效果
                    setTimeout(() => {
                        musicControl.classList.remove('playing');
                    }, 3000);
                }
            }
        });
    }
}

/**
 * 初始化设置弹窗事件
 * 
 * 功能说明：
 * 1. 设置弹窗的打开和关闭事件
 * 2. 实现标签页切换功能
 * 3. 加载设置数据
 */
function initSettingsModal() {
    // 获取相关元素
    const settingsBtn = document.getElementById('settingsBtn');
    const closeSettings = document.getElementById('closeSettings');
    const settingsModal = document.getElementById('settingsModal');
    const tabButtons = document.querySelectorAll('.tab-button');
    const tabContents = document.querySelectorAll('.tab-content');

    // 点击设置按钮切换弹窗显示/隐藏
    settingsBtn.addEventListener('click', function () {
        if (settingsModal.style.display === 'block') {
            hideSettingsModal();
        } else {
            showSettingsModal();
        }
    });

    // 点击关闭按钮隐藏弹窗
    closeSettings.addEventListener('click', function () {
        hideSettingsModal();
    });

    // TAB切换功能
    tabButtons.forEach(button => {
        button.addEventListener('click', function () {
            const tabId = this.getAttribute('data-tab');

            // 移除所有按钮的active类
            tabButtons.forEach(btn => btn.classList.remove('active'));
            // 为当前按钮添加active类
            this.classList.add('active');

            // 隐藏所有TAB内容
            tabContents.forEach(content => content.classList.remove('active'));
            // 显示当前TAB内容
            document.getElementById(`${tabId}-tab`).classList.add('active');

            // 如果是组件配置TAB，加载组件配置
            if (tabId === 'components') {
                loadComponentConfigs();
            }

            // 如果是应用管理TAB，初始化重启应用按钮
            if (tabId === 'apps') {
                initRestartAppButton();
            }
        });
    });


    // 壁纸轮播复选框事件
    const wallpaperCarouselCheckbox = document.getElementById('wallpaperCarouselCheckbox');
    if (wallpaperCarouselCheckbox && !wallpaperCarouselCheckbox.dataset.listenerAdded) {
        wallpaperCarouselCheckbox.addEventListener('change', function () {
            if (typeof Android !== 'undefined' && Android.saveWallpaperCarouselSetting) {
                // 使用同步方法
                const result = Android.saveWallpaperCarouselSetting(this.checked);
                if (result) {
                    showToast('壁纸轮播设置保存成功');
                    // 更新壁纸轮播状态
                    ensureWallpaperCarouselSettings();
                } else {
                    showToast('壁纸轮播设置保存失败');
                }
            }
        });
        wallpaperCarouselCheckbox.dataset.listenerAdded = 'true';
    }

    // 随机模式复选框事件
    const randomModeCheckbox = document.getElementById('randomModeCheckbox');
    if (randomModeCheckbox && !randomModeCheckbox.dataset.listenerAdded) {
        randomModeCheckbox.addEventListener('change', function () {
            if (isLoadingSettings) return;

            if (typeof Android !== 'undefined' && Android.saveRandomModeSetting) {
                const result = Android.saveRandomModeSetting(this.checked);
                if (result) {
                    showToast('随机模式设置保存成功');

                    // 发送设置更改广播以确保设置立即生效
                    if (typeof Android !== 'undefined' && Android.sendWallpaperSettingsChangedBroadcast) {
                        try {
                            Android.sendWallpaperSettingsChangedBroadcast();
                        } catch (e) {
                            console.log('无法发送壁纸设置更改广播');
                        }
                    }
                } else {
                    showToast('随机模式设置保存失败');
                }
            }
        });
        randomModeCheckbox.dataset.listenerAdded = 'true';
    }

    // 指定模式复选框事件
    const specifiedModeCheckbox = document.getElementById('specifiedModeCheckbox');
    if (specifiedModeCheckbox && !specifiedModeCheckbox.dataset.listenerAdded) {
        specifiedModeCheckbox.addEventListener('change', function () {
            if (isLoadingSettings) return;

            if (typeof Android !== 'undefined' && Android.saveSpecifiedModeSetting) {
                const result = Android.saveSpecifiedModeSetting(this.checked);
                if (result) {
                    showToast('指定模式设置保存成功');

                    // 发送设置更改广播以确保设置立即生效
                    if (typeof Android !== 'undefined' && Android.sendWallpaperSettingsChangedBroadcast) {
                        try {
                            Android.sendWallpaperSettingsChangedBroadcast();
                        } catch (e) {
                            console.log('无法发送壁纸设置更改广播');
                        }
                    }
                } else {
                    showToast('指定模式设置保存失败');
                }
            }
        });
        specifiedModeCheckbox.dataset.listenerAdded = 'true';
    }

    // 轮播间隔输入框事件
    const switchIntervalInput = document.getElementById('switchIntervalInput');
    if (switchIntervalInput && !switchIntervalInput.dataset.listenerAdded) {
        switchIntervalInput.addEventListener('change', function () {
            if (typeof Android !== 'undefined' && Android.saveWallpaperSwitchInterval) {
                const interval = parseInt(this.value) * 1000;
                const result = Android.saveWallpaperSwitchInterval(interval);
                if (result) {
                    showToast('轮播间隔设置保存成功');
                } else {
                    showToast('轮播间隔设置保存失败');
                }
            }
        });
        switchIntervalInput.dataset.listenerAdded = 'true';
    }

    // 点击弹窗外部区域隐藏弹窗
    window.addEventListener('click', function (event) {
        if (event.target === settingsModal) {
            hideSettingsModal();
        }
    });
}

// 加载壁纸设置
function loadWallpaperSettings() {
    isLoadingSettings = true;
    if (typeof Android !== 'undefined' && Android.getWallpaperSettingsAsync) {
        const callbackId = AsyncCallbackManager.register(function (settingsJson) {
            try {
                const settings = JSON.parse(settingsJson);
                console.log('加载壁纸设置:', settings);

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

                const switchIntervalInput = document.getElementById('switchIntervalInput');
                if (switchIntervalInput) {
                    switchIntervalInput.value = settings.switch_interval / 1000;
                }

                // 加载已启用的分类状态
                loadEnabledCategories();

                // 设置壁纸轮播间隔输入框的显示
                if (switchIntervalInput) {
                    if (settings.wallpaper_carousel) {
                        switchIntervalInput.parentElement.style.display = 'flex';
                    } else {
                        switchIntervalInput.parentElement.style.display = 'none';
                    }
                }

                // 加载原桌面自启和开机问候语设置
                loadSystemSettings(settings);
            } catch (e) {
                console.error('加载壁纸设置时出错:', e);
            } finally {
                isLoadingSettings = false;
            }
        });
        Android.getWallpaperSettingsAsync(callbackId);
    } else if (typeof Android !== 'undefined' && Android.getWallpaperSettings) {
        // 回退到同步方法
        isLoadingSettings = true;
        try {
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

            const switchIntervalInput = document.getElementById('switchIntervalInput');
            if (switchIntervalInput) {
                switchIntervalInput.value = settings.switch_interval / 1000;
            }

            // 加载已启用的分类状态
            loadEnabledCategories();

            // 设置壁纸轮播间隔输入框的显示
            if (switchIntervalInput) {
                if (settings.wallpaper_carousel) {
                    switchIntervalInput.parentElement.style.display = 'flex';
                } else {
                    switchIntervalInput.parentElement.style.display = 'none';
                }
            }

            // 加载原桌面自启和开机问候语设置
            loadSystemSettings(settings);
        } catch (e) {
            console.error('加载壁纸设置时出错:', e);
        } finally {
            isLoadingSettings = false;
        }
    }
}

// 加载系统设置（原桌面自启和开机问候语）
function loadSystemSettings(settings) {
    // 设置原桌面自启复选框状态
    const systemLauncherCheckbox = document.getElementById('systemLauncherCheckbox');
    if (systemLauncherCheckbox) {
        systemLauncherCheckbox.checked = settings.system_launcher !== undefined ? settings.system_launcher : false; // 默认不启用
    }

    // 设置开机问候语复选框状态
    const bootGreetingCheckbox = document.getElementById('bootGreetingCheckbox');
    if (bootGreetingCheckbox) {
        bootGreetingCheckbox.checked = settings.boot_greeting !== undefined ? settings.boot_greeting : false; // 默认不启用
    }
}

// 加载已启用的分类状态
function loadEnabledCategories() {
    if (typeof Android !== 'undefined' && Android.getEnabledCategoriesAsync) {
        const callbackId = AsyncCallbackManager.register(function (enabledCategoriesJson) {
            try {
                const enabledCategories = JSON.parse(enabledCategoriesJson);
                const checkboxes = document.querySelectorAll('.category-checkbox');
                checkboxes.forEach(checkbox => {
                    const categoryId = checkbox.getAttribute('data-category-id');
                    if (enabledCategories.includes(categoryId)) {
                        checkbox.checked = true;
                    } else {
                        checkbox.checked = false;
                    }
                });
                console.log('已加载在线壁纸分类状态');
            } catch (e) {
                console.error('加载已启用分类时出错:', e);
            }
        });
        Android.getEnabledCategoriesAsync(callbackId);
    } else if (typeof Android !== 'undefined' && Android.getEnabledCategories) {
        // 回退到同步方法
        try {
            const enabledCategoriesJson = Android.getEnabledCategories();
            const enabledCategories = JSON.parse(enabledCategoriesJson);
            const checkboxes = document.querySelectorAll('.category-checkbox');
            checkboxes.forEach(checkbox => {
                const categoryId = checkbox.getAttribute('data-category-id');
                if (enabledCategories.includes(categoryId)) {
                    checkbox.checked = true;
                } else {
                    checkbox.checked = false;
                }
            });
            console.log('已加载在线壁纸分类状态');
        } catch (e) {
            console.error('加载已启用分类时出错:', e);
        }
    }
}

// 初始化分类复选框事件
function initCategoryCheckboxEvents() {
    console.log('initCategoryCheckboxEvents 被调用');
    const checkboxes = document.querySelectorAll('.category-checkbox');
    console.log('找到的分类复选框数量:', checkboxes.length);
    checkboxes.forEach(checkbox => {
        if (!checkbox.dataset.listenerAdded) {
            console.log('为复选框添加事件监听器:', checkbox.getAttribute('data-category-id'));
            checkbox.addEventListener('click', function () {
                console.log('分类复选框click事件被触发');
                const categoryId = this.getAttribute('data-category-id');
                const enabled = this.checked;

                if (typeof Android !== 'undefined' && Android.updateCategoryEnabledAsync) {
                    const callbackId = AsyncCallbackManager.register(function (result) {
                        if (result === "true") {
                            console.log('分类状态已更新:', categoryId, enabled);
                            showToast('分类状态保存成功');
                        } else {
                            console.error('分类状态更新失败');
                            showToast('分类状态保存失败');
                        }
                    });
                    Android.updateCategoryEnabledAsync(categoryId, enabled, callbackId);
                } else if (typeof Android !== 'undefined' && Android.updateCategoryEnabled) {
                    // 回退到同步方法
                    Android.updateCategoryEnabled(categoryId, enabled);
                    console.log('分类状态已更新:', categoryId, enabled);
                    showToast('分类状态保存成功');
                }
            });
            checkbox.dataset.listenerAdded = 'true';
        }
    });
}


// 初始化重启应用按钮事件监听器
function initRestartAppButton() {
    const restartAppBtn = document.getElementById('restartAppBtn');

    // 检查按钮是否已绑定事件
    if (restartAppBtn && !restartAppBtn.dataset.listenerAdded) {
        restartAppBtn.addEventListener('click', function () {
            // 重启应用按钮被点击
            // 调用原生代码重启应用
            if (typeof Android !== 'undefined' && Android.restartApp) {
                Android.restartApp();
            } else {
                // 重启应用功能将在原生代码中实现
                alert('重启应用功能将在原生代码中实现');
            }
        });

        // 标记已添加事件监听器
        restartAppBtn.dataset.listenerAdded = 'true';
        // 重启应用按钮事件监听器已添加
    } else if (restartAppBtn) {
        // 重启应用按钮事件监听器已存在
    } else {
        // 未找到restartAppBtn元素
    }

    // 初始化设置为默认桌面按钮事件监听器
    const setDefaultDesktopBtn = document.getElementById('setDefaultDesktopBtn');

    // 检查按钮是否已绑定事件
    if (setDefaultDesktopBtn && !setDefaultDesktopBtn.dataset.listenerAdded) {
        setDefaultDesktopBtn.addEventListener('click', function () {
            console.log('默认桌面按钮被点击');
            // 设置为默认桌面按钮被点击
            // 调用原生代码设置为默认桌面
            if (typeof Android !== 'undefined' && Android.setDefaultDesktopViaAdb) {
                try {
                    Android.setDefaultDesktopViaAdb();
                } catch (e) {
                    console.error('设置默认桌面时出错:', e);
                    alert('设置默认桌面时出错: ' + e.message);
                }
            } else if (typeof Android !== 'undefined' && Android.setDefaultDesktop) {
                // 如果新方法不可用，使用旧方法
                Android.setDefaultDesktop();
            } else {
                // 设置为默认桌面功能将在原生代码中实现
                alert('设置为默认桌面功能将在原生代码中实现');
            }
        });

        // 标记已添加事件监听器
        setDefaultDesktopBtn.dataset.listenerAdded = 'true';
        // 设置为默认桌面按钮事件监听器已添加
    } else if (setDefaultDesktopBtn) {
        // 设置为默认桌面按钮事件监听器已存在
    } else {
        // 未找到setDefaultDesktopBtn元素
    }

    // 初始化原桌面自启复选框事件
    const systemLauncherCheckbox = document.getElementById('systemLauncherCheckbox');
    if (systemLauncherCheckbox && !systemLauncherCheckbox.dataset.listenerAdded) {
        systemLauncherCheckbox.addEventListener('change', function () {
            if (typeof Android !== 'undefined' && Android.saveSystemLauncherSetting) {
                // 使用同步方法
                const result = Android.saveSystemLauncherSetting(this.checked);
                if (result) {
                    showToast('原桌面自启设置保存成功');
                } else {
                    showToast('原桌面自启设置保存失败');
                }
            }
        });
        systemLauncherCheckbox.dataset.listenerAdded = 'true';
    }

    // 初始化开机问候语复选框事件
    const bootGreetingCheckbox = document.getElementById('bootGreetingCheckbox');
    if (bootGreetingCheckbox && !bootGreetingCheckbox.dataset.listenerAdded) {
        bootGreetingCheckbox.addEventListener('change', function () {
            if (typeof Android !== 'undefined' && Android.saveBootGreetingSetting) {
                // 使用同步方法
                const result = Android.saveBootGreetingSetting(this.checked);
                if (result) {
                    showToast('开机问候语设置保存成功');
                } else {
                    showToast('开机问候语设置保存失败');
                }
            }
        });
        bootGreetingCheckbox.dataset.listenerAdded = 'true';
    }
}
// 初始化ADB调试授权按钮事件监听器
function initADBButton() {
    console.log('初始化ADB按钮');
    const adbAuthorizationBtn = document.getElementById('adbAuthorizationBtn');
    if (adbAuthorizationBtn && !adbAuthorizationBtn.dataset.listenerAdded) {
        adbAuthorizationBtn.addEventListener('click', function () {
            console.log('ADB授权按钮被点击');
            // 显示提示信息
            showToast('正在尝试ADB授权...');

            // 首先尝试新的无线WiFi ADB授权方法
            if (typeof Android !== 'undefined' && Android.triggerWirelessAdbAuthorization) {
                console.log('调用 triggerWirelessAdbAuthorization');
                Android.triggerWirelessAdbAuthorization();
            } else if (typeof Android !== 'undefined' && Android.triggerUsbDebugAuthorization) {
                // 如果无线方法不可用，直接使用传统USB方法
                console.log('调用 triggerUsbDebugAuthorization');
                Android.triggerUsbDebugAuthorization();
            } else {
                // 如果新接口不可用，尝试旧接口
                if (typeof Android !== 'undefined' && Android.enableAdbDebugging) {
                    Android.enableAdbDebugging();
                } else {
                    alert('ADB调试授权功能将在原生代码中实现');
                }
            }
        });

        // 标记已添加事件监听器
        adbAuthorizationBtn.dataset.listenerAdded = 'true';
        console.log('ADB授权按钮事件监听器已添加');
    } else if (adbAuthorizationBtn) {
        // ADB调试授权按钮事件监听器已存在
        console.log('ADB授权按钮事件监听器已存在');
    } else {
        // 未找到adbAuthorizationBtn元素
        console.log('未找到adbAuthorizationBtn元素');
    }

    // 初始化ADB执行按钮事件
    const adbAuthorizationBtn2 = document.getElementById('adbAuthorizationBtn2');
    if (adbAuthorizationBtn2 && !adbAuthorizationBtn2.dataset.listenerAdded) {
        adbAuthorizationBtn2.addEventListener('click', function () {
            console.log('ADB执行按钮被点击');
            // 显示提示信息
            showToast('正在执行ADB权限授权...');

            // 调用执行ADB权限授权的方法
            if (typeof Android !== 'undefined' && Android.executeAdbPermissionGrant) {
                console.log('调用 executeAdbPermissionGrant');
                Android.executeAdbPermissionGrant();
            } else {
                alert('ADB权限授权功能不可用');
            }
        });

        // 标记已添加事件监听器
        adbAuthorizationBtn2.dataset.listenerAdded = 'true';
        console.log('ADB执行按钮事件监听器已添加');
    } else if (adbAuthorizationBtn2) {
        // ADB执行按钮事件监听器已存在
        console.log('ADB执行按钮事件监听器已存在');
    } else {
        // 未找到adbAuthorizationBtn2元素
        console.log('未找到adbAuthorizationBtn2元素');
    }

    // 初始化ADB测试按钮事件
    const adbTestBtn = document.getElementById('adbTestBtn');
    if (adbTestBtn && !adbTestBtn.dataset.listenerAdded) {
        adbTestBtn.addEventListener('click', function () {
            // 跳转到无线ADB测试页面
            window.location.href = 'wireless_adb_test.html';
        });

        // 标记已添加事件监听器
        adbTestBtn.dataset.listenerAdded = 'true';
    } else if (adbTestBtn) {
        // ADB测试按钮事件监听器已存在
    } else {
        // 未找到adbTestBtn元素
    }
}

// 显示设置弹窗
function showSettingsModal() {
    // 先关闭应用列表弹窗（如果已打开）
    hideAppsModal();

    document.getElementById('settingsModal').style.display = 'block';
    // 默认激活壁纸设置TAB
    const tabButtons = document.querySelectorAll('.tab-button');
    const tabContents = document.querySelectorAll('.tab-content');

    // 移除所有按钮的active类
    tabButtons.forEach(btn => btn.classList.remove('active'));
    // 为壁纸设置按钮添加active类
    document.querySelector('.tab-button[data-tab="wallpaper"]').classList.add('active');

    // 隐藏所有TAB内容
    tabContents.forEach(content => content.classList.remove('active'));
    // 显示壁纸设置TAB内容
    document.getElementById('wallpaper-tab').classList.add('active');

    // 加载壁纸设置（包含系统管理设置）
    loadWallpaperSettings();

    // 加载组件配置
    loadComponentConfigs();

    // 初始化系统管理TAB的事件监听器
    initRestartAppButton();
    initADBButton();

    // 初始化分类复选框事件
    initCategoryCheckboxEvents();
    
    // 启动自动关闭定时器
    resetAutoCloseTimer();
    
    // 为弹窗内的元素添加交互事件，重置定时器
    const settingsModal = document.getElementById('settingsModal');
    const interactiveElements = settingsModal.querySelectorAll('button, .tab-button, input, select, #closeSettings');
    interactiveElements.forEach(element => {
        element.addEventListener('click', resetAutoCloseTimer);
    });
    
    // 为滚动事件添加监听器，重置定时器
    settingsModal.addEventListener('scroll', resetAutoCloseTimer);
}

// 隐藏设置弹窗
function hideSettingsModal() {
    document.getElementById('settingsModal').style.display = 'none';
    // 清除自动关闭定时器
    clearAutoCloseTimer();
}

// 处理保存指定模式设置的回调
window.handleSaveSpecifiedModeSettingCallback = function (callbackId, result) {
    AsyncCallbackManager.execute(callbackId, result === 'true');
};

// 处理保存随机模式设置的回调
window.handleSaveRandomModeSettingCallback = function (callbackId, result) {
    AsyncCallbackManager.execute(callbackId, result === 'true');
};

// 处理保存壁纸轮播设置的回调
window.handleSaveWallpaperCarouselSettingCallback = function (callbackId, result) {
    AsyncCallbackManager.execute(callbackId, result === 'true');
};

// 处理保存壁纸轮播时间间隔设置的回调
window.handleSaveWallpaperSwitchIntervalCallback = function (callbackId, result) {
    AsyncCallbackManager.execute(callbackId, result === 'true');
};

// 处理保存壁纸轮播时间间隔设置的回调
window.handleSaveSwitchIntervalSettingCallback = function (callbackId, result) {
    AsyncCallbackManager.execute(callbackId, result === 'true');
};

// 处理获取所有壁纸设置的回调
window.handleGetWallpaperSettingsCallback = function (callbackId, result) {
    AsyncCallbackManager.execute(callbackId, result);
};

// 处理更新分类启用状态的回调
window.handleUpdateCategoryEnabledCallback = function (callbackId, result) {
    AsyncCallbackManager.execute(callbackId, result);
};

// 处理获取壁纸分类列表的回调
window.handleGetWallpaperCategoriesCallback = function (callbackId, result) {
    AsyncCallbackManager.execute(callbackId, result);
};

// 处理保存或更新组件配置的回调
window.handleSaveComponentConfigCallback = function (callbackId, result) {
    AsyncCallbackManager.execute(callbackId, result === 'true');
};

// 处理获取所有组件配置的回调
window.handleGetAllComponentConfigsCallback = function (callbackId, result) {
    AsyncCallbackManager.execute(callbackId, result);
};

// 处理刷新应用列表的回调
window.handleRefreshAppListCallback = function (callbackId, result) {
    AsyncCallbackManager.execute(callbackId, result === 'true');
};

// 处理获取音乐律动设置的回调
window.handleGetMusicRhythmSettingCallback = function (callbackId, result) {
    AsyncCallbackManager.execute(callbackId, result === 'true');
};

// 处理保存音乐律动设置的回调
window.handleSaveMusicRhythmSettingCallback = function (callbackId, result) {
    AsyncCallbackManager.execute(callbackId, result === 'true');
};

// 处理获取随机壁纸Base64数据的回调
window.handleGetRandomWallpaperBase64Callback = function (callbackId, result) {
    AsyncCallbackManager.execute(callbackId, result);
};

// 处理获取随机壁纸URL的回调
window.handleGetRandomWallpaperCallback = function (callbackId, result) {
    AsyncCallbackManager.execute(callbackId, result);
};

// 处理获取应用列表的回调
window.handleGetAppListCallback = function (callbackId, result) {
    AsyncCallbackManager.execute(callbackId, result);
};

// 处理壁纸更新通知
window.handleWallpaperUpdateNotification = function () {
    setWallpaperBackground();
};

// 处理获取已启用分类的回调
window.handleGetEnabledCategoriesCallback = function (callbackId, enabledCategoriesJson) {
    try {
        const enabledCategories = JSON.parse(enabledCategoriesJson);
        const checkboxes = document.querySelectorAll('.category-checkbox');
        checkboxes.forEach(checkbox => {
            const categoryId = checkbox.getAttribute('data-category-id');
            if (enabledCategories.includes(categoryId)) {
                checkbox.checked = true;
            } else {
                checkbox.checked = false;
            }
        });
        console.log('已加载在线壁纸分类状态');
    } catch (e) {
        console.error('加载已启用分类时出错:', e);
    }
};

// 隐藏设置弹窗
function hideSettingsModal() {
    document.getElementById('settingsModal').style.display = 'none';
    // 清除自动关闭定时器
    clearAutoCloseTimer();
}

// 初始化组件可见性
function initComponentVisibility() {
    if (typeof Android !== 'undefined' && Android.getAllComponentConfigs) {
        // 使用同步方法
        try {
            const configsJson = Android.getAllComponentConfigs();
            const configs = JSON.parse(configsJson);

            // 更新各个组件的可见性
            updateComponentVisibility('music_component', configs.music_component === true);
            updateComponentVisibility('map_component', configs.map_component === true);
            updateComponentVisibility('app_component', configs.app_component === true);
            updateComponentVisibility('tire_pressure_component', configs.tire_pressure_component === true);
            updateComponentVisibility('weather_component', configs.weather_component === true);
        } catch (e) {
            console.error('初始化组件可见性时出错:', e);
        }
    }
}

// 加载组件配置
let componentConfigListenersAdded = false;

function loadComponentConfigs() {
    if (typeof Android !== 'undefined' && Android.getAllComponentConfigs) {
        // 使用同步方法
        try {
            const configsJson = Android.getAllComponentConfigs();
            const configs = JSON.parse(configsJson);

            // 设置复选框状态
            document.getElementById('musicComponentCheckbox').checked = configs.music_component === true;
            document.getElementById('mapComponentCheckbox').checked = configs.map_component === true;
            document.getElementById('appComponentCheckbox').checked = configs.app_component === true;
            document.getElementById('tirePressureComponentCheckbox').checked = configs.tire_pressure_component === true;
            document.getElementById('weatherComponentCheckbox').checked = configs.weather_component === true;

            // 只在第一次加载时添加事件监听器
            if (!componentConfigListenersAdded) {
                addComponentConfigEventListeners();
                componentConfigListenersAdded = true;
            }
        } catch (e) {
            console.error('加载组件配置时出错:', e);
        }
    }
}

// 添加组件配置事件监听器
function addComponentConfigEventListeners() {
    // 音乐组件复选框事件
    document.getElementById('musicComponentCheckbox').addEventListener('change', function () {
        if (typeof Android !== 'undefined' && Android.saveComponentConfig) {
            // 使用同步方法
            const result = Android.saveComponentConfig('music_component', this.checked);
            // 显示保存结果的Toast提示
            if (result) {
                showToast('音乐组件设置保存成功');
                // 更新前端显示
                updateComponentVisibility('music_component', this.checked);
            } else {
                showToast('音乐组件设置保存失败');
            }
        }
    });

    // 地图组件复选框事件
    document.getElementById('mapComponentCheckbox').addEventListener('change', function () {
        if (typeof Android !== 'undefined' && Android.saveComponentConfig) {
            // 使用同步方法
            const result = Android.saveComponentConfig('map_component', this.checked);
            // 显示保存结果的Toast提示
            if (result) {
                showToast('地图组件设置保存成功');
                // 更新前端显示
                updateComponentVisibility('map_component', this.checked);
            } else {
                showToast('地图组件设置保存失败');
            }
        }
    });

    // 应用组件复选框事件
    document.getElementById('appComponentCheckbox').addEventListener('change', function () {
        if (typeof Android !== 'undefined' && Android.saveComponentConfig) {
            // 使用同步方法
            const result = Android.saveComponentConfig('app_component', this.checked);
            // 显示保存结果的Toast提示
            if (result) {
                showToast('应用组件设置保存成功');
                // 更新前端显示
                updateComponentVisibility('app_component', this.checked);
            } else {
                showToast('应用组件设置保存失败');
            }
        }
    });

    // 轮胎气压组件复选框事件
    document.getElementById('tirePressureComponentCheckbox').addEventListener('change', function () {
        if (typeof Android !== 'undefined' && Android.saveComponentConfig) {
            // 使用同步方法
            const result = Android.saveComponentConfig('tire_pressure_component', this.checked);
            // 显示保存结果的Toast提示
            if (result) {
                showToast('轮胎气压组件设置保存成功');
                // 更新前端显示
                updateComponentVisibility('tire_pressure_component', this.checked);
            } else {
                showToast('轮胎气压组件设置保存失败');
            }
        }
    });

    // 天气组件复选框事件
    document.getElementById('weatherComponentCheckbox').addEventListener('change', function () {
        if (typeof Android !== 'undefined' && Android.saveComponentConfig) {
            // 使用同步方法
            const result = Android.saveComponentConfig('weather_component', this.checked);
            // 显示保存结果的Toast提示
            if (result) {
                showToast('天气组件设置保存成功');
                // 更新前端显示
                updateComponentVisibility('weather_component', this.checked);
            } else {
                showToast('天气组件设置保存失败');
            }
        }
    });
}

// 更新组件可见性
function updateComponentVisibility(componentName, isVisible) {
    let selector = '';
    let element = null;

    switch (componentName) {
        case 'music_component':
            // 音乐组件在.widget-group中的第二个
            selector = '.widget-group:nth-child(2)';
            break;
        case 'map_component':
            // 地图组件在.widget-group中的第一个
            selector = '.widget-group:nth-child(1)';
            break;
        case 'app_component':
            // APP组件在.widget-group中的第三个
            selector = '.widget-group:nth-child(3)';
            break;
        case 'tire_pressure_component':
            // 胎压组件在.widget-group中的第四个
            selector = '.widget-group:nth-child(4)';
            break;
        case 'weather_component':
            // 天气组件在.widget-group中的第五个
            selector = '.widget-group:nth-child(5)';
            break;
    }

    // 所有组件都使用selector
    if (selector) {
        element = document.querySelector(selector);
    }
    if (element) {
        // 对于.widget-group元素，使用flex显示以保持布局
        if (selector.includes('.widget-group')) {
            element.style.display = isVisible ? 'inline-flex' : 'none';
        } else {
            element.style.display = isVisible ? 'block' : 'none';
        }
    }
}

// 生成模拟应用数据
function generateAppData() {
    // 模拟应用数据，按字母分类
    const apps = {
        'A': [
            { name: '阿里云', packageName: 'com.alibaba.cloud', icon: 'images/ic_launcher.png' },
            { name: '爱奇艺', packageName: 'com.qiyi.video', icon: 'images/ic_launcher.png' }
        ],
        'B': [
            { name: '百度地图', packageName: 'com.autonavi.minimap', icon: 'images/ic_launcher.png' },
            { name: '哔哩哔哩', packageName: 'tv.danmaku.bili', icon: 'images/ic_launcher.png' }
        ],
        'C': [
            { name: 'Chrome浏览器', packageName: 'com.android.chrome', icon: 'images/ic_launcher.png' }
        ],
        'D': [
            { name: '钉钉', packageName: 'com.alibaba.android.rimet', icon: 'images/ic_launcher.png' },
            { name: '抖音', packageName: 'com.ss.android.ugc.aweme', icon: 'images/ic_launcher.png' }
        ],
        'F': [
            { name: 'Firefox', packageName: 'org.mozilla.firefox', icon: 'images/ic_launcher.png' }
        ],
        'G': [
            { name: '高德地图', packageName: 'com.autonavi.minimap', icon: 'images/ic_launcher.png' }
        ],
        'J': [
            { name: '今日头条', packageName: 'com.ss.android.article.news', icon: 'images/ic_launcher.png' }
        ],
        'K': [
            { name: '快手', packageName: 'com.smile.gifmaker', icon: 'images/ic_launcher.png' }
        ],
        'M': [
            { name: '美团', packageName: 'com.sankuai.meituan', icon: 'images/ic_launcher.png' },
            { name: '墨迹天气', packageName: 'com.moji.mjweather', icon: 'images/ic_launcher.png' }
        ],
        'Q': [
            { name: 'QQ', packageName: 'com.tencent.mobileqq', icon: 'images/ic_launcher.png' },
            { name: '企业微信', packageName: 'com.tencent.wework', icon: 'images/ic_launcher.png' }
        ],
        'S': [
            { name: '搜狐视频', packageName: 'com.sohu.sohuvideo', icon: 'images/ic_launcher.png' }
        ],
        'T': [
            { name: '腾讯视频', packageName: 'com.tencent.qqlive', icon: 'images/ic_launcher.png' },
            { name: '头条新闻', packageName: 'com.ss.android.article.news', icon: 'images/ic_launcher.png' }
        ],
        'W': [
            { name: '微信', packageName: 'com.tencent.mm', icon: 'images/ic_launcher.png' },
            { name: '网易云音乐', packageName: 'com.netease.cloudmusic', icon: 'images/ic_launcher.png' }
        ],
        'X': [
            { name: '小米商城', packageName: 'com.xiaomi.shop', icon: 'images/ic_launcher.png' },
            { name: '携程旅行', packageName: 'ctrip.android.view', icon: 'images/ic_launcher.png' }
        ],
        'Y': [
            { name: '优酷视频', packageName: 'com.youku.phone', icon: 'images/ic_launcher.png' },
            { name: '音乐播放器', packageName: 'com.android.music', icon: 'images/ic_launcher.png' }
        ],
        'Z': [
            { name: '支付宝', packageName: 'com.eg.android.AlipayGphone', icon: 'images/ic_launcher.png' },
            { name: '知乎', packageName: 'com.zhihu.android', icon: 'images/ic_launcher.png' }
        ]
    };

    return apps;
}

// 初始化字母导航栏
function initAlphabetNav() {
    const alphabetList = document.getElementById('alphabetList');
    const alphabet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';

    // 清空现有内容
    alphabetList.innerHTML = '';

    // 为每个字母创建列表项
    for (let i = 0; i < alphabet.length; i++) {
        const letter = alphabet[i];
        const li = document.createElement('li');
        li.textContent = letter;
        li.setAttribute('data-letter', letter);
        li.addEventListener('click', function () {
            // 滚动到对应字母的应用列表
            const section = document.getElementById(`section-${letter}`);
            if (section) {
                section.scrollIntoView({ behavior: 'smooth' });
            }
        });
        alphabetList.appendChild(li);
    }
}

// 根据数据初始化字母导航栏
function initAlphabetNavFromData(appsData) {
    const alphabetList = document.getElementById('alphabetList');

    // 清空现有内容
    alphabetList.innerHTML = '';

    // 为每个字母创建列表项
    for (const letter in appsData) {
        if (appsData.hasOwnProperty(letter) && appsData[letter].length > 0) {
            const li = document.createElement('li');
            li.textContent = letter;
            li.setAttribute('data-letter', letter);
            li.addEventListener('click', function () {
                // 滚动到对应字母的应用列表
                const section = document.getElementById(`section-${letter}`);
                if (section) {
                    section.scrollIntoView({ behavior: 'smooth' });
                }
            });
            alphabetList.appendChild(li);
        }
    }
}

// 渲染应用列表
function renderAppsList(appsData) {
    const appsList = document.getElementById('appsList');
    appsList.innerHTML = '';

    // 遍历每个字母分类
    for (const letter in appsData) {
        if (appsData.hasOwnProperty(letter) && appsData[letter].length > 0) {
            // 创建字母分组容器
            const section = document.createElement('div');
            section.className = 'app-section';
            section.id = `section-${letter}`;

            // 创建字母标题
            const title = document.createElement('h3');
            title.className = 'app-section-title';
            title.textContent = letter;
            section.appendChild(title);

            // 创建应用网格
            const grid = document.createElement('div');
            grid.className = 'app-grid';

            // 添加应用项
            appsData[letter].forEach(app => {
                const appItem = document.createElement('div');
                appItem.className = 'app-item';
                appItem.setAttribute('data-package', app.packageName);
                appItem.innerHTML = `
                    <div class="app-icon" style="background-image: url('${app.icon}');"></div>
                    <div class="app-name">${app.name}</div>
                `;

                // 添加点击事件
                appItem.addEventListener('click', function () {
                    // 调用原生代码启动应用
                    if (typeof Android !== 'undefined' && Android.launchApp) {
                        Android.launchApp(app.packageName);
                    } else {
                        // 用于测试的模拟数据
                        alert(`启动应用: ${app.name}\n包名: ${app.packageName}`);
                    }
                });

                // 添加长按事件（用于添加到快速启动）
                let pressTimer;
                appItem.addEventListener('touchstart', function (e) {
                    pressTimer = setTimeout(() => {
                        showAddToQuickAppsDialog(app);
                    }, 1000); // 长按1秒触发
                });

                appItem.addEventListener('touchend', function () {
                    clearTimeout(pressTimer);
                });

                appItem.addEventListener('touchmove', function () {
                    clearTimeout(pressTimer);
                });

                // 鼠标右键事件（用于测试环境）
                appItem.addEventListener('contextmenu', function (e) {
                    e.preventDefault();
                    showAddToQuickAppsDialog(app);
                });

                grid.appendChild(appItem);
            });

            section.appendChild(grid);
            appsList.appendChild(section);
        }
    }
}

function initAppsModal() {
    // 获取相关元素
    const appsBtn = document.getElementById('appsBtn');
    const closeApps = document.getElementById('closeApps');
    const appsModal = document.getElementById('appsModal');
    const appSearch = document.getElementById('appSearch');
    const appsLoading = document.getElementById('appsLoading');
    const appsList = document.getElementById('appsList');

    // 前端应用列表缓存
    let cachedAppsData = null;
    let lastAppListLoadTime = 0;
    const APP_LIST_CACHE_DURATION = 5 * 60 * 1000; // 5分钟缓存
    
    // 接收预加载的应用列表
    function setAppListCache(appListJson) {
        try {
            if (appListJson && appListJson !== "") {
                cachedAppsData = JSON.parse(appListJson);
                lastAppListLoadTime = Date.now();
                console.log('已接收并缓存预加载的应用列表');
                console.log('预加载的应用列表包含 ' + Object.keys(cachedAppsData).length + ' 个字母分组');
            }
        } catch (e) {
            console.error('解析预加载应用列表时出错:', e);
        }
    }
    
    // 暴露给原生代码的全局函数
    window.setAppListCache = setAppListCache;

    // 点击应用按钮切换弹窗显示/隐藏
    appsBtn.addEventListener('click', function () {
        if (appsModal.style.display === 'block') {
            hideAppsModal();
        } else {
            showAppsModal(); // 添加这行代码来显示应用列表弹窗

            // 显示加载提示
            appsList.style.display = 'none';
            appsLoading.style.display = 'block';

            // 使用setTimeout来确保UI更新后再执行耗时操作
            setTimeout(() => {
                loadAppList();
            }, 1); // 短暂延迟确保UI更新
        }
    });

    // 点击关闭按钮隐藏弹窗
    closeApps.addEventListener('click', function () {
        hideAppsModal();
    });

    // 点击弹窗外部区域隐藏弹窗
    window.addEventListener('click', function (event) {
        if (event.target === appsModal) {
            hideAppsModal();
        }
    });

    // 搜索功能
    appSearch.addEventListener('input', function () {
        const searchTerm = this.value.toLowerCase();
        const appItems = document.querySelectorAll('.app-item');

        appItems.forEach(item => {
            const appName = item.querySelector('.app-name').textContent.toLowerCase();
            if (appName.includes(searchTerm)) {
                item.style.display = 'flex';
            } else {
                item.style.display = 'none';
            }
        });
    });

    // 加载应用列表的函数
    function loadAppList() {
        const now = Date.now();
        
        // 检查前端缓存
        if (cachedAppsData && (now - lastAppListLoadTime) < APP_LIST_CACHE_DURATION) {
            console.log('使用前端缓存的应用列表');
            initAlphabetNavFromData(cachedAppsData);
            renderAppsList(cachedAppsData);
            
            // 刷新快速启动应用列表
            loadQuickApps();
            
            // 隐藏加载提示，显示应用列表
            appsLoading.style.display = 'none';
            appsList.style.display = 'block';
            return;
        }
        
        // 检查是否在Android环境中
        if (typeof Android !== 'undefined' && Android.getAppListAsync) {
            const callbackId = AsyncCallbackManager.register(function (appListJson) {
                try {
                    const appsData = JSON.parse(appListJson);

                    // 缓存应用列表
                    cachedAppsData = appsData;
                    lastAppListLoadTime = now;

                    initAlphabetNavFromData(appsData);
                    renderAppsList(appsData);
                } catch (e) {
                    console.error('获取应用列表时出错:', e);
                    // 出错时使用模拟数据
                    const appsData = generateAppData();
                    cachedAppsData = appsData;
                    lastAppListLoadTime = now;

                    initAlphabetNav();
                    renderAppsList(appsData);
                }

                // 刷新快速启动应用列表
                loadQuickApps();

                // 隐藏加载提示，显示应用列表
                appsLoading.style.display = 'none';
                appsList.style.display = 'block';
            });
            Android.getAppListAsync(callbackId);
        } else {
            // 非Android环境，使用模拟数据
            const appsData = generateAppData();
            // cachedAppsData = appsData;
            // lastAppListLoadTime = now;

            initAlphabetNav();
            renderAppsList(appsData);

            // 刷新快速启动应用列表
            loadQuickApps();

            // 隐藏加载提示，显示应用列表
            appsLoading.style.display = 'none';
            appsList.style.display = 'block';
        }
    }
}

// 优化应用列表渲染函数，使用文档片段来提高性能
function renderAppsList(appsData) {
    const appsList = document.getElementById('appsList');
    appsList.innerHTML = '';

    // 使用文档片段来减少DOM操作
    const fragment = document.createDocumentFragment();

    // 遍历每个字母分类
    for (const letter in appsData) {
        if (appsData.hasOwnProperty(letter) && appsData[letter].length > 0) {
            // 创建字母分组容器
            const section = document.createElement('div');
            section.className = 'app-section';
            section.id = `section-${letter}`;

            // 创建字母标题
            const title = document.createElement('h3');
            title.className = 'app-section-title';
            title.textContent = letter;
            section.appendChild(title);

            // 创建应用网格
            const grid = document.createElement('div');
            grid.className = 'app-grid';

            // 添加应用项
            appsData[letter].forEach(app => {
                const appItem = document.createElement('div');
                appItem.className = 'app-item';
                appItem.setAttribute('data-package', app.packageName);
                appItem.innerHTML = `
                    <div class="app-icon" style="background-image: url('${app.icon}');"></div>
                    <div class="app-name">${app.name}</div>
                `;

                // 添加点击事件
                appItem.addEventListener('click', function () {
                    // 调用原生代码启动应用
                    if (typeof Android !== 'undefined' && Android.launchApp) {
                        Android.launchApp(app.packageName);
                    } else {
                        // 用于测试的模拟数据
                        alert(`启动应用: ${app.name}\n包名: ${app.packageName}`);
                    }
                });

                // 添加长按事件（用于添加到快速启动）
                let pressTimer;
                appItem.addEventListener('touchstart', function (e) {
                    pressTimer = setTimeout(() => {
                        showAddToQuickAppsDialog(app);
                    }, 1000); // 长按1秒触发
                });

                appItem.addEventListener('touchend', function () {
                    clearTimeout(pressTimer);
                });

                appItem.addEventListener('touchmove', function () {
                    clearTimeout(pressTimer);
                });

                // 鼠标右键事件（用于测试环境）
                appItem.addEventListener('contextmenu', function (e) {
                    e.preventDefault();
                    showAddToQuickAppsDialog(app);
                });

                grid.appendChild(appItem);
            });

            section.appendChild(grid);
            fragment.appendChild(section);
        }
    }

    // 一次性添加到DOM中
    appsList.appendChild(fragment);
}

// 优化字母导航栏初始化函数
function initAlphabetNavFromData(appsData) {
    const alphabetList = document.getElementById('alphabetList');

    // 清空现有内容
    alphabetList.innerHTML = '';

    // 使用文档片段来减少DOM操作
    const fragment = document.createDocumentFragment();

    // 为每个字母创建列表项
    for (const letter in appsData) {
        if (appsData.hasOwnProperty(letter) && appsData[letter].length > 0) {
            const li = document.createElement('li');
            li.textContent = letter;
            li.setAttribute('data-letter', letter);
            li.addEventListener('click', function () {
                // 滚动到对应字母的应用列表
                const section = document.getElementById(`section-${letter}`);
                if (section) {
                    section.scrollIntoView({ behavior: 'smooth' });
                }
            });
            fragment.appendChild(li);
        }
    }

    // 一次性添加到DOM中
    alphabetList.appendChild(fragment);
}

/**
 * 显示应用列表弹窗
 * 
 * 功能说明：
 * 1. 关闭设置弹窗（如果已打开）
 * 2. 显示应用列表弹窗
 * 3. 启动30秒自动关闭定时器
 * 4. 为弹窗内的元素添加交互事件，重置定时器
 */
function showAppsModal() {
    // 先关闭设置弹窗（如果已打开）
    hideSettingsModal();

    document.getElementById('appsModal').style.display = 'block';
    
    // 启动自动关闭定时器
    resetAutoCloseTimer();
    
    // 为弹窗内的元素添加交互事件，重置定时器
    const appsModal = document.getElementById('appsModal');
    const interactiveElements = appsModal.querySelectorAll('button, .app-item, #alphabetList li, #closeApps');
    interactiveElements.forEach(element => {
        element.addEventListener('click', resetAutoCloseTimer);
    });
    
    // 为滚动事件添加监听器，重置定时器
    appsModal.addEventListener('scroll', resetAutoCloseTimer);
}

/**
 * 隐藏应用列表弹窗
 * 
 * 功能说明：
 * 1. 隐藏应用列表弹窗
 * 2. 清除自动关闭定时器
 */
function hideAppsModal() {
    document.getElementById('appsModal').style.display = 'none';
    // 清除自动关闭定时器
    clearAutoCloseTimer();
}

// 全局变量来跟踪当前打开的对话框
let currentDialog = null;

// 全局变量来存储自动关闭定时器
let autoCloseTimer = null;

/**
 * 重置自动关闭定时器
 * 
 * 功能说明：
 * 1. 清除现有的定时器
 * 2. 设置新的30秒定时器
 * 3. 超时后自动关闭当前打开的弹窗
 * 
 * 使用场景：
 * - 应用列表弹窗
 * - 设置页面弹窗
 */
function resetAutoCloseTimer() {
    // 清除现有的定时器
    if (autoCloseTimer) {
        clearTimeout(autoCloseTimer);
    }
    
    // 设置新的30秒定时器
    autoCloseTimer = setTimeout(() => {
        // 检查是否有打开的弹窗
        const appsModal = document.getElementById('appsModal');
        const settingsModal = document.getElementById('settingsModal');
        
        if (appsModal && appsModal.style.display === 'block') {
            hideAppsModal();
        } else if (settingsModal && settingsModal.style.display === 'block') {
            hideSettingsModal();
        }
        showToast('超时未操作，自动关闭！');
    }, 30000); // 30秒后自动关闭
     
}

/**
 * 清除自动关闭定时器
 * 
 * 功能说明：
 * 1. 清除当前的自动关闭定时器
 * 2. 将定时器变量设置为null
 * 
 * 使用场景：
 * - 弹窗被手动关闭时
 * - 切换弹窗时
 */
function clearAutoCloseTimer() {
    if (autoCloseTimer) {
        clearTimeout(autoCloseTimer);
        autoCloseTimer = null;
    }
}

/**
 * 显示添加到快速启动对话框
 * 
 * 功能说明：
 * 1. 检查是否已有对话框打开
 * 2. 检查应用是否已在快速启动中
 * 3. 如果已存在，显示移除对话框
 * 4. 如果不存在，显示添加对话框
 * 
 * @param app 应用对象，包含应用信息
 */
function showAddToQuickAppsDialog(app) {
    // 检查是否已有对话框打开
    if (currentDialog) {
        return;
    }

    // 检查是否已在快速启动中
    let isQuickApp = false;
    if (typeof Android !== 'undefined' && Android.isQuickApp) {
        isQuickApp = Android.isQuickApp(app.packageName);
    }

    // 如果已在快速启动中，显示移除对话框
    if (isQuickApp) {
        showRemoveFromQuickAppsDialog(app);
        return;
    }

    // 创建对话框元素
    const dialog = document.createElement('div');
    dialog.className = 'modal confirm-dialog';
    dialog.innerHTML = `
        <div class="modal-content confirm-dialog-content">
            <div class="confirm-dialog-header">
                <h2>添加到快速启动</h2>
            </div>
            <div class="confirm-dialog-body">
                <div class="app-icon" style="background-image: url('${app.icon}');"></div>
                <p class="app-name">${app.name}</p>
                <p class="confirm-message">确定要添加到快速启动吗？</p>
            </div>
            <div class="confirm-dialog-footer">
                <button id="cancelBtn" class="dialog-button cancel-button">取消</button>
                <button id="confirmBtn" class="dialog-button confirm-button">确定</button>
            </div>
        </div>
    `;

    // 添加到页面
    document.body.appendChild(dialog);
    currentDialog = dialog; // 保存当前对话框引用

    // 获取按钮元素
    const confirmBtn = dialog.querySelector('#confirmBtn');
    const cancelBtn = dialog.querySelector('#cancelBtn');

    // 关闭对话框函数
    function closeDialogFunc() {
        if (!currentDialog) return; // 防止重复关闭

        dialog.classList.add('closing');
        setTimeout(() => {
            if (dialog.parentNode) {
                dialog.parentNode.removeChild(dialog);
            }
            currentDialog = null; // 清除引用
        }, 300);
    }

    // 绑定事件
    cancelBtn.addEventListener('click', closeDialogFunc);

    // 确认按钮事件
    confirmBtn.addEventListener('click', function () {
        if (typeof Android !== 'undefined' && Android.addQuickApp) {
            Android.addQuickApp(app.name, app.packageName, app.icon);
            // 刷新快速启动应用列表
            loadQuickApps();
        }
        closeDialogFunc();
    });

    // 点击对话框外部区域关闭
    dialog.addEventListener('click', function (event) {
        if (event.target === dialog) {
            closeDialogFunc();
        }
    });

    // 防止事件冒泡
    dialog.querySelector('.modal-content').addEventListener('click', function (event) {
        event.stopPropagation();
    });
}

// 显示从快速启动移除对话框
function showRemoveFromQuickAppsDialog(app) {
    // 检查是否已有对话框打开
    if (currentDialog) {
        return;
    }

    // 创建对话框元素
    const dialog = document.createElement('div');
    dialog.className = 'modal confirm-dialog';
    dialog.innerHTML = `
        <div class="modal-content confirm-dialog-content">
            <div class="confirm-dialog-header">
                <h2>移除快速启动</h2>
            </div>
            <div class="confirm-dialog-body">
                <div class="app-icon" style="background-image: url('${app.icon}');"></div>
                <p class="app-name">${app.name}</p>
                <p class="confirm-message">确定要从快速启动中移除吗？</p>
            </div>
            <div class="confirm-dialog-footer">
                <button id="cancelBtn" class="dialog-button cancel-button">取消</button>
                <button id="confirmBtn" class="dialog-button confirm-button">确定</button>
            </div>
        </div>
    `;

    // 添加到页面
    document.body.appendChild(dialog);
    currentDialog = dialog; // 保存当前对话框引用

    // 获取按钮元素
    const confirmBtn = dialog.querySelector('#confirmBtn');
    const cancelBtn = dialog.querySelector('#cancelBtn');

    // 关闭对话框函数
    function closeDialogFunc() {
        if (!currentDialog) return; // 防止重复关闭

        dialog.classList.add('closing');
        setTimeout(() => {
            if (dialog.parentNode) {
                dialog.parentNode.removeChild(dialog);
            }
            currentDialog = null; // 清除引用
        }, 300);
    }

    // 绑定事件
    cancelBtn.addEventListener('click', closeDialogFunc);

    // 确认按钮事件
    confirmBtn.addEventListener('click', function () {
        if (typeof Android !== 'undefined' && Android.removeQuickApp) {
            Android.removeQuickApp(app.packageName);
            // 刷新快速启动应用列表
            loadQuickApps();
        }
        closeDialogFunc();
    });

    // 点击对话框外部区域关闭
    dialog.addEventListener('click', function (event) {
        if (event.target === dialog) {
            closeDialogFunc();
        }
    });

    // 防止事件冒泡
    dialog.querySelector('.modal-content').addEventListener('click', function (event) {
        event.stopPropagation();
    });
}

// 加载快速启动应用列表
function loadQuickApps() {
    const quickAppsContainer = document.getElementById('quickAppsContainer');
    const quickAppsWidget = document.querySelector('.quick-apps-widget');

    // 清空现有内容
    quickAppsContainer.innerHTML = '';

    if (typeof Android !== 'undefined' && Android.getQuickAppList) {
        try {
            const quickAppsJson = Android.getQuickAppList();
            const quickApps = JSON.parse(quickAppsJson);

            // 显示组件
            if (quickAppsWidget) {
                quickAppsWidget.style.display = 'flex';
            }

            // 如果没有快速启动应用，显示占位提示
            if (quickApps.length === 0) {
                const emptyTip = document.createElement('div');
                emptyTip.style.cssText = 'color: rgba(255,255,255,0.6); font-size: 14px; padding: 0 20px; text-align: center;';
                emptyTip.innerHTML = '长按应用列表<br>中的应用添加';
                quickAppsContainer.appendChild(emptyTip);
                return;
            }

            // 生成快速启动应用列表
            quickApps.forEach(app => {
                const appItem = document.createElement('div');
                appItem.className = 'quick-app-item';
                appItem.setAttribute('data-package', app.packageName);
                appItem.innerHTML = `
                    <div class="quick-app-icon" style="background-image: url('${app.icon}'); width: 60px; height: 60px;"></div>
                    <div class="quick-app-name" style="font-size: 14px;">${app.name}</div>
                `;

                // 添加点击事件
                appItem.addEventListener('click', function () {
                    if (typeof Android !== 'undefined' && Android.launchApp) {
                        Android.launchApp(app.packageName);
                    }
                });

                // 添加长按事件（用于从快速启动中移除）
                let pressTimer;
                appItem.addEventListener('touchstart', function (e) {
                    pressTimer = setTimeout(() => {
                        showRemoveFromQuickAppsDialog(app);
                    }, 1000); // 长按1秒触发
                });

                appItem.addEventListener('touchend', function () {
                    clearTimeout(pressTimer);
                });

                appItem.addEventListener('touchmove', function () {
                    clearTimeout(pressTimer);
                });

                // 鼠标右键事件（用于测试环境）
                appItem.addEventListener('contextmenu', function (e) {
                    e.preventDefault();
                    showRemoveFromQuickAppsDialog(app);
                });

                quickAppsContainer.appendChild(appItem);
            });
        } catch (e) {
            console.error('加载快速启动应用列表时出错:', e);
            // 出错时隐藏组件
            if (quickAppsWidget) {
                quickAppsWidget.style.display = 'none';
            }
        }
    } else {
        // 在非Android环境中隐藏组件
        if (quickAppsWidget) {
            quickAppsWidget.style.display = 'none';
        }
    }
}

// 加载桌面快捷开关
function loadQuickSwitches() {
    const container = document.getElementById('quickSwitchesContainer');
    if (!container) return;

    container.innerHTML = '';

    // 桌面显示的4个常用开关 + 1个更多按钮
    const desktopSwitches = [
        { id: 'lowBeamLight', name: '近光灯', icon: '💡' },
        { id: 'maxCooling', name: '极速制冷', icon: '❄️' },
        { id: 'cameraOverspeed', name: '360限速', icon: '📸' },
        { id: 'ambientLight', name: '氛围灯', icon: '🌈' },
    ];

    // 添加4个快捷开关
    desktopSwitches.forEach(sw => {
        const item = document.createElement('div');
        item.className = 'quick-switch-item';
        item.setAttribute('data-id', sw.id);
        item.innerHTML = `
            <div class="quick-switch-icon">${sw.icon}</div>
            <div class="quick-switch-name">${sw.name}</div>
        `;

        // 点击切换开关
        item.addEventListener('click', function() {
            if (window.QuickSwitchManager) {
                window.QuickSwitchManager.toggleSwitch(sw.id);
                // 更新显示状态
                updateQuickSwitchStatus(sw.id);
            }
        });

        container.appendChild(item);
    });

    // 添加"全部"按钮
    const moreItem = document.createElement('div');
    moreItem.className = 'quick-switch-item more';
    moreItem.innerHTML = `
        <div class="quick-switch-icon">⋯</div>
        <div class="quick-switch-name">全部</div>
    `;

    moreItem.addEventListener('click', function() {
        if (window.QuickSwitchManager) {
            window.QuickSwitchManager.togglePanel();
        }
    });

    container.appendChild(moreItem);

    // 初始化状态
    refreshQuickSwitchesStatus();
}

// 更新单个快捷开关状态
function updateQuickSwitchStatus(switchId) {
    const item = document.querySelector('.quick-switch-item[data-id="' + switchId + '"]');
    if (!item || !window.QuickSwitchManager) return;

    // 从QuickSwitchManager获取状态
    const sw = window.QuickSwitchManager.switches.find(s => s.id === switchId);
    if (sw && sw.currentState) {
        item.classList.add('active');
    } else {
        item.classList.remove('active');
    }
}

// 刷新所有快捷开关状态
function refreshQuickSwitchesStatus() {
    if (!window.QuickSwitchManager) return;

    const items = document.querySelectorAll('.quick-switch-item[data-id]');
    items.forEach(item => {
        const id = item.getAttribute('data-id');
        updateQuickSwitchStatus(id);
    });
}


// 检查是否有其他元素覆盖在背景上:
function checkForOverlappingElements() {
    const backgroundElement = document.querySelector('.background-image');
    if (!backgroundElement) return;

    // 背景元素位置和尺寸:
    const bgRect = backgroundElement.getBoundingClientRect();

    // 检查页面上所有元素
    const allElements = document.querySelectorAll('*');
    const overlappingElements = [];

    allElements.forEach(element => {
        // 跳过背景元素本身和它的子元素
        if (element === backgroundElement || backgroundElement.contains(element)) {
            return;
        }

        // 跳过隐藏元素
        const computedStyle = window.getComputedStyle(element);
        if (computedStyle.display === 'none' || computedStyle.visibility === 'hidden') {
            return;
        }

        // 获取元素的位置和尺寸
        const rect = element.getBoundingClientRect();

        // 检查是否与背景元素重叠
        if (rect.left < bgRect.right &&
            rect.right > bgRect.left &&
            rect.top < bgRect.bottom &&
            rect.bottom > bgRect.top) {
            // 检查z-index
            const zIndex = computedStyle.zIndex;
            if (zIndex !== 'auto' && parseInt(zIndex) >= 0) {
                overlappingElements.push({
                    element: element,
                    tagName: element.tagName,
                    className: element.className,
                    id: element.id,
                    zIndex: zIndex,
                    rect: rect
                });
            }
        }
    });

    // 按z-index排序
    overlappingElements.sort((a, b) => parseInt(b.zIndex) - parseInt(a.zIndex));

    // 可能覆盖在背景上的元素:
}

// 获取元素事件监听器的函数（用于调试）
function getEventListeners(element) {
    // 这是一个简化版本，实际浏览器可能有专门的API
    const listeners = {};

    // 获取标准事件
    ['touchstart', 'touchmove', 'touchend', 'mousedown'].forEach(eventType => {
        const hasListener = element.hasAttribute(`on${eventType}`) ||
            element[`on${eventType}`] ||
            (element._listeners && element._listeners[eventType]);
        if (hasListener) {
            listeners[eventType] = true;
        }
    });

    return listeners;
}

// 添加触摸滑动事件监听器 - 已由WallpaperSwipeManager接管
function addTouchSwipeListener() {
    // 壁纸滑动功能已由WallpaperSwipeManager接管，此处留空
    console.log('壁纸滑动监听器已由WallpaperSwipeManager管理');
}

// 初始化可配置按钮
// 添加一个标志来防止重复初始化
let isConfigurableButtonsInitialized = false;

function initConfigurableButtons() {
    // 防止重复初始化
    if (isConfigurableButtonsInitialized) {
        console.log('可配置按钮已初始化，跳过重复初始化');
        return;
    }

    // 标记已初始化
    isConfigurableButtonsInitialized = true;

    // 获取所有可配置的按钮
    const configurableButtons = document.querySelectorAll('[data-configurable="true"]');

    configurableButtons.forEach(button => {
        const buttonId = button.id;

        // 添加长按事件监听器
        let pressTimer;
        button.addEventListener('touchstart', function (e) {
            pressTimer = setTimeout(() => {
                showMapAppSelectionDialog(buttonId);
            }, 1000); // 长按1秒触发
        });

        button.addEventListener('touchend', function () {
            clearTimeout(pressTimer);
        });

        button.addEventListener('touchmove', function () {
            clearTimeout(pressTimer);
        });

        // 鼠标右键事件（用于测试环境）
        button.addEventListener('contextmenu', function (e) {
            e.preventDefault();
            showMapAppSelectionDialog(buttonId);
        });

        // 添加点击事件监听器
        button.addEventListener('click', function () {
            // 检查是否为回家或公司按钮
            if (buttonId === 'goHomeBtn') {
                // 调用Android原生方法导航到家
                if (typeof Android !== 'undefined' && Android.navigateToHome) {
                    Android.navigateToHome();
                }
                return;
            }

            if (buttonId === 'goCompanyBtn') {
                // 调用Android原生方法导航到公司
                if (typeof Android !== 'undefined' && Android.navigateToCompany) {
                    Android.navigateToCompany();
                }
                return;
            }

            // 获取配置的应用信息
            if (typeof Android !== 'undefined' && Android.getConfigApp) {
                try {
                    const appInfoJson = Android.getConfigApp(buttonId);
                    if (appInfoJson && appInfoJson !== "{}") {
                        const appInfo = JSON.parse(appInfoJson);
                        if (appInfo.package_name) {
                            // 启动配置的应用
                            Android.launchApp(appInfo.package_name);
                        } else {
                            // 如果没有配置应用，显示地图应用选择对话框
                            showMapAppSelectionDialog(buttonId);
                        }
                    } else {
                        // 如果没有配置应用，显示地图应用选择对话框
                        showMapAppSelectionDialog(buttonId);
                    }
                } catch (e) {
                    // 出错时显示地图应用选择对话框
                    showMapAppSelectionDialog(buttonId);
                }
            } else {
                // 在非Android环境中显示地图应用选择对话框
                showMapAppSelectionDialog(buttonId);
            }
        });
    });


    // 为地图图标添加点击事件，启动高德地图
    const mapIcon = document.querySelector('.map-icon');
    if (mapIcon) {
        mapIcon.addEventListener('click', function () {
            // 检查是否在Android环境中
            if (typeof Android !== 'undefined' && Android.launchApp) {
                try {
                    // 启动高德地图应用
                    Android.launchApp("com.autonavi.minimap");
                } catch (e) {
                    console.error('启动高德地图时出错:', e);
                    // 处理异常情况
                    if (typeof Android !== 'undefined' && Android.showToast) {
                        Android.showToast("启动高德地图时出现错误");
                    }
                }
            } else {
                // 在非Android环境中显示提示
                alert("此功能仅在Android设备上可用");
            }
        });
    }

    // 为比亚迪桌面按钮添加点击事件
    const camera360Btn = document.getElementById('camera360Btn');
    if (camera360Btn) {
        camera360Btn.addEventListener('click', function () {
            if (typeof Android !== 'undefined' && Android.startCamera360) {
                try {
                    Android.startCamera360();
                } catch (e) {
                }
            }
        });
    }

    // 为多任务按钮添加点击事件
    const tasksBtn = document.getElementById('tasksBtn');
    if (tasksBtn) {
        tasksBtn.addEventListener('click', function () {
            console.log('多任务按钮被点击');
            // 先尝试通过ADB连接打开多任务
            if (typeof Android !== 'undefined' && Android.openRecentTasks) {
                try {
                    Android.openRecentTasks();
                } catch (e) {
                    console.error('打开多任务页面时出错:', e);
                }
            } else if (typeof Android !== 'undefined' && Android.openRecents) {
                // 如果新方法不可用，使用旧方法
                try {
                    Android.openRecents();
                } catch (e) {
                    console.error('打开多任务页面时出错:', e);
                }
            } else {
                alert('多任务功能不可用');
            }
        });
    }

    // 为空调组件添加点击事件
    const acControls = document.querySelector('.center-controls');
    if (acControls) {
        acControls.addEventListener('click', function (e) {
            // 检查点击的是否是空调相关控件
            const target = e.target;

            // 空调开关
            if (target.id === 'acControl' || target.closest('#acControl')) {
                toggleAirConditioning();
                return;
            }

            // 空调温度减
            if (target.id === 'acMinus' || target.closest('#acMinus')) {
                decreaseTemperature();
                return;
            }

            // 空调温度加
            if (target.id === 'acPlus' || target.closest('#acPlus')) {
                increaseTemperature();
                return;
            }

            // 空调风量减
            if (target.id === 'acArrow1' || target.closest('#acArrow1')) {
                decreaseWindLevel();
                return;
            }

            // 空调风量加
            if (target.id === 'acArrow2' || target.closest('#acArrow2')) {
                increaseWindLevel();
                return;
            }

            // 空调温度减（第二个温度控制）
            if (target.id === 'acWindMinus' || target.closest('#acWindMinus')) {
                decreaseTemperature();
                return;
            }

            // 空调温度加（第二个温度控制）
            if (target.id === 'acWindPlus' || target.closest('#acWindPlus')) {
                increaseTemperature();
                return;
            }

            // 前除霜开关
            if (target.id === 'acHcs' || target.closest('#acHcs')) {
                toggleDefrost();
                return;
            }

            // 其他空调相关控件
            if (target.classList.contains('ac-control') ||
                target.classList.contains('ac-hcs') ||
                target.classList.contains('wind-display') ||
                target.closest('.ac-control') ||
                target.closest('.ac-hcs') ||
                target.closest('.wind-display')) {
                if (typeof Android !== 'undefined' && Android.toggleAirConditioning) {
                    try {
                        Android.toggleAirConditioning();
                    } catch (e) {
                    }
                }
            }
        });
    }
}

// 获取按钮显示名称
function getButtonDisplayName(buttonId) {
    switch (buttonId) {
        case 'goHomeBtn':
            return '回家按钮';
        case 'goCompanyBtn':
            return '公司按钮';
        default:
            return buttonId;
    }
}


// 减少风量
function decreaseWindLevel() {
    if (typeof Android !== 'undefined' && Android.adjustWindLevel) {
        try {
            Android.adjustWindLevel(-1);
        } catch (e) {
            console.error('减少空调风量失败:', e);
        }
    }

    const currentLevel = getCurrentWindLevel();
    const newLevel = Math.max(0, currentLevel - 1);
    setWindLevel(newLevel);
}

// 增加风量
function increaseWindLevel() {
    if (typeof Android !== 'undefined' && Android.adjustWindLevel) {
        try {
            Android.adjustWindLevel(1);
        } catch (e) {
            console.error('增加空调风量失败:', e);
        }
    }

    const currentLevel = getCurrentWindLevel();
    const newLevel = Math.min(7, currentLevel + 1);
    setWindLevel(newLevel);
}
// 设置风量级别
function setWindLevel(level) {
    const windLevelElement = document.querySelector('.wind-level');
    if (windLevelElement && level >= 0 && level <= 7) {
        windLevelElement.style.backgroundImage = `url('images/wind_level_0${level}.png')`;
        console.log('风量设置为:', level);
    }
}


// 获取当前风量级别
function getCurrentWindLevel() {
    const windLevelElement = document.querySelector('.wind-level');
    if (windLevelElement) {
        const backgroundImage = windLevelElement.style.backgroundImage;
        const match = backgroundImage.match(/wind_level_(\d+)\.png/);
        if (match) {
            return parseInt(match[1]);
        }
    }
    return 6; // 默认为6级风量
}

// 空调温度控制 - 减少温度
function decreaseTemperature() {
    if (typeof Android !== 'undefined' && Android.adjustTemperature) {
        try {
            Android.adjustTemperature(-1);
        } catch (e) {
            console.error('减少空调温度失败:', e);
        }
    }

    const airTextElements = document.querySelectorAll('.air-text');
    airTextElements.forEach(element => {
        const currentTempStr = element.textContent.replace("°", "");
        try {
            const currentTemp = parseInt(currentTempStr);
            if (!isNaN(currentTemp)) {
                const newTemp = currentTemp - 1;
                element.textContent = newTemp + "°";
            }
        } catch (e) {
        }
    });
}

// 空调温度控制 - 增加温度
function increaseTemperature() {
    if (typeof Android !== 'undefined' && Android.adjustTemperature) {
        try {
            Android.adjustTemperature(1);
        } catch (e) {
            console.error('增加空调温度失败:', e);
        }
    }

    const airTextElements = document.querySelectorAll('.air-text');
    airTextElements.forEach(element => {
        const currentTempStr = element.textContent.replace("°", "");
        try {
            const currentTemp = parseInt(currentTempStr);
            if (!isNaN(currentTemp)) {
                const newTemp = currentTemp + 1;
                element.textContent = newTemp + "°";
            }
        } catch (e) {
        }
    });
}

// 设置风量级别
function setWindLevel(level) {
    const windLevelElement = document.querySelector('.wind-level');
    if (windLevelElement && level >= 0 && level <= 7) {
        windLevelElement.style.backgroundImage = `url('images/wind_level_0${level}.png')`;
    }
}

// 空调开关切换
function toggleAirConditioning() {
    if (typeof Android !== 'undefined' && Android.toggleAirConditioning) {
        try {
            Android.toggleAirConditioning();
        } catch (e) {
            console.error('切换空调开关状态失败:', e);
        }
    }

    const acControl = document.getElementById('acControl');
    if (acControl) {
        const currentImage = acControl.style.backgroundImage;

        // 检查当前是否是开启状态
        if (currentImage.includes('nav_air.png')) {
            // 切换到关闭状态
            acControl.style.backgroundImage = "url('images/nav_air_close.png')";
            // 停止旋转动画
            acControl.classList.remove('rotate-animation');
        } else {
            // 切换到开启状态
            acControl.style.backgroundImage = "url('images/nav_air.png')";
            // 启动旋转动画
            acControl.classList.add('rotate-animation');
        }
    }
}

// 切换前除霜状态
function toggleDefrost() {
    if (typeof Android !== 'undefined' && Android.toggleDefrost) {
        try {
            Android.toggleDefrost();
        } catch (e) {
            console.error('切换前除霜状态失败:', e);
        }
    }
}

/**
 * 初始化空调状态
 * 接收从Android传递过来的空调状态数据并更新UI显示
 * @param acData 空调状态数据对象
 */
window.initializeAcStatus = function (acData) {
    try {
        console.log('收到空调状态数据:', acData);

        // 更新空调开关状态
        const acControl = document.getElementById('acControl');
        if (acControl) {
            if (acData.acOn) {
                acControl.style.backgroundImage = "url('images/nav_air.png')";
                acControl.classList.add('rotate-animation');
            } else {
                acControl.style.backgroundImage = "url('images/nav_air_close.png')";
                acControl.classList.remove('rotate-animation');
            }
        }

        // 更新温度显示
        const airTextElements = document.querySelectorAll('.air-text');
        airTextElements.forEach(element => {
            element.textContent = acData.temperature + "°";
        });

        // 更新风量显示
        const windLevelElement = document.querySelector('.wind-level');
        if (windLevelElement && acData.windLevel >= 0 && acData.windLevel <= 7) {
            windLevelElement.style.backgroundImage = `url('images/wind_level_0${acData.windLevel}.png')`;
        }

        // 更新前除霜状态
        const acHcs = document.getElementById('acHcs');
        if (acHcs) {
            if (acData.defrostOn) {
                acHcs.style.opacity = '1';
            } else {
                acHcs.style.opacity = '0.5';
            }
        }
    } catch (e) {
        console.error('初始化空调状态时出错:', e);
    }
};

// 检查WiFi连接状态
function checkWifiStatus() {
    const wifiIcon = document.getElementById('wifiIcon');

    // 检查是否在Android环境中
    if (typeof Android !== 'undefined' && Android.isWifiConnected) {
        try {
            const isConnected = Android.isWifiConnected();
            if (isConnected) {
                wifiIcon.style.display = 'block';
                wifiIcon.classList.add('connected');
            } else {
                wifiIcon.style.display = 'none';
                wifiIcon.classList.remove('connected');
            }
        } catch (error) {
            // 出错时默认隐藏图标
            wifiIcon.style.display = 'none';
            wifiIcon.classList.remove('connected');
        }
    } else {
        // 在非Android环境中，默认隐藏WiFi图标
        wifiIcon.style.display = 'none';
        wifiIcon.classList.remove('connected');
    }
}

// 检查蓝牙连接状态
function checkBluetoothStatus() {
    const bluetoothIcon = document.getElementById('bluetoothIcon');

    // 检查是否在Android环境中
    if (typeof Android !== 'undefined' && Android.isBluetoothConnected) {
        try {
            const isConnected = Android.isBluetoothConnected();
            if (isConnected) {
                bluetoothIcon.style.display = 'block';
                bluetoothIcon.classList.add('connected');
            } else {
                bluetoothIcon.style.display = 'none';
                bluetoothIcon.classList.remove('connected');
            }
        } catch (error) {
            // 出错时默认隐藏图标
            bluetoothIcon.style.display = 'none';
            bluetoothIcon.classList.remove('connected');
        }
    } else {
        // 在非Android环境中，默认隐藏蓝牙图标
        bluetoothIcon.style.display = 'none';
        bluetoothIcon.classList.remove('connected');
    }
}

// 更新网络和蓝牙状态
function updateNetworkAndBluetoothStatus() {
    checkWifiStatus();
    checkBluetoothStatus();
}


// 检查元素是否在背景元素上方
function isElementOverBackground(element, backgroundElement) {
    // 获取元素的边界矩形
    const elementRect = element.getBoundingClientRect();
    const backgroundRect = backgroundElement.getBoundingClientRect();

    // 检查元素是否与背景元素重叠
    return !(elementRect.right < backgroundRect.left ||
        elementRect.left > backgroundRect.right ||
        elementRect.bottom < backgroundRect.top ||
        elementRect.top > backgroundRect.bottom);
}

// 获取触摸点上的所有元素
function getElementsAtTouchPoint(touchX, touchY, backgroundElement) {
    const elementsAtPoint = document.elementsFromPoint(touchX, touchY);

    // 检查是否有元素覆盖在背景上
    let topMostElement = null;
    let isBackgroundTopMost = false;

    for (let i = 0; i < elementsAtPoint.length; i++) {
        const element = elementsAtPoint[i];

        // 跳过隐藏元素
        const computedStyle = window.getComputedStyle(element);
        if (computedStyle.display === 'none' || computedStyle.visibility === 'hidden') {
            continue;
        }

        // 如果找到背景元素，标记它为顶层元素
        if (element === backgroundElement || backgroundElement.contains(element)) {
            isBackgroundTopMost = true;
            topMostElement = element;
            break;
        }

        // 检查z-index
        const zIndex = parseInt(computedStyle.zIndex) || 0;
        if (zIndex > 0) {
            topMostElement = element;
            break;
        }
    }

    return {
        elements: elementsAtPoint,
        topMostElement: topMostElement,
        isBackgroundTopMost: isBackgroundTopMost
    };
}

// 初始化横向滚动功能
function initHorizontalScroll() {
    const horizontalScroll = document.querySelector('.horizontal-scroll');

    if (horizontalScroll) {
        // 确保元素可以滚动
        horizontalScroll.style.overflowX = 'auto';

        // 添加触摸事件支持
        let startX = 0;
        let scrollLeft = 0;

        // 触摸开始事件
        horizontalScroll.addEventListener('touchstart', (e) => {
            startX = e.touches[0].pageX - horizontalScroll.offsetLeft;
            scrollLeft = horizontalScroll.scrollLeft;
        });

        // 触摸移动事件
        horizontalScroll.addEventListener('touchmove', (e) => {
            e.preventDefault();
            const x = e.touches[0].pageX - horizontalScroll.offsetLeft;
            const walk = (x - startX) * 2; // 滚动速度
            horizontalScroll.scrollLeft = scrollLeft - walk;
        });

        // 鼠标拖拽支持
        let isDown = false;

        horizontalScroll.addEventListener('mousedown', (e) => {
            isDown = true;
            startX = e.pageX - horizontalScroll.offsetLeft;
            scrollLeft = horizontalScroll.scrollLeft;
        });

        horizontalScroll.addEventListener('mouseleave', () => {
            isDown = false;
        });

        horizontalScroll.addEventListener('mouseup', () => {
            isDown = false;
        });

        horizontalScroll.addEventListener('mousemove', (e) => {
            if (!isDown) return;
            e.preventDefault();
            const x = e.pageX - horizontalScroll.offsetLeft;
            const walk = (x - startX) * 2; // 滚动速度
            horizontalScroll.scrollLeft = scrollLeft - walk;
        });
    }
}


// 页面加载完成后初始化所有功能
window.addEventListener('DOMContentLoaded', () => {
    // 使用默认壁纸
    const backgroundElement = document.querySelector('.background-image');
    if (backgroundElement) {
        backgroundElement.style.backgroundImage = "url('images/default_bg_1.jpg')";
    }

    // 注册时间更新监听事件
    registerTimeUpdateListener();


    // 添加按钮点击效果
    addClickEffect();

    // 初始化设置弹窗
    initSettingsModal();

    // 初始化应用列表弹窗
    initAppsModal();


    // 启动壁纸轮播
    //ensureWallpaperCarouselSettings();

    // 加载快速启动应用列表
    loadQuickApps();

    // 加载桌面快捷开关
    loadQuickSwitches();

    // 初始化可配置按钮
    initConfigurableButtons();

    // 添加触摸滑动事件监听器
    addTouchSwipeListener();
    // 初始化横向滚动功能
    initHorizontalScroll();

    // 初始化时隐藏WiFi和蓝牙图标，只在连接时显示
    const wifiIcon = document.getElementById('wifiIcon');
    const bluetoothIcon = document.getElementById('bluetoothIcon');
    if (wifiIcon) wifiIcon.style.display = 'none';
    if (bluetoothIcon) bluetoothIcon.style.display = 'none';

    // 立即检查并更新网络和蓝牙状态
    updateNetworkAndBluetoothStatus();
    // 每5秒检查一次网络和蓝牙状态
    setInterval(updateNetworkAndBluetoothStatus, 5000);

    // 初始化壁纸双击事件
    initWallpaperDoubleClick();

    // 初始化组件可见性
    initComponentVisibility();

    // 初始化自定义音频播放器
    initCustomAudioPlayer();

    // 初始化音乐播放控制按钮
    initMusicControls();

    // 启动音乐名称更新
    //updateMusicName();
    //setInterval(updateMusicName, 2000); // 每2秒更新一次音乐名称

    // 启动音乐播放状态图标更新
    updateMusicPlayPauseIcon();
    setInterval(updateMusicPlayPauseIcon, 1000); // 每1秒更新一次音乐播放状态图标

    // 启动音乐进度更新
    updateMusicProgress();
    setInterval(updateMusicProgress, 1000); // 每1秒更新一次音乐进度

    // 注意：getMusicProgressInfo方法已在WebViewBridge中实现，用于获取音乐播放进度

    // 添加时间显示点击事件，用于切换壁纸轮播状态
    addTimeDisplayClickEvent();

});

// 初始化壁纸双击和长按事件
function initWallpaperDoubleClick() {
    const backgroundContainer = document.querySelector('.background-container');
    let lastClickTime = 0;
    let isWallpaperLocked = false;
    let longPressTimer = null;
    
    if (backgroundContainer) {
        // 点击事件（用于双击检测）
        backgroundContainer.addEventListener('click', function() {
            const currentTime = new Date().getTime();
            const timeSinceLastClick = currentTime - lastClickTime;
            
            // 检测双击（300毫秒内的两次点击）
            if (timeSinceLastClick < 300 && timeSinceLastClick > 0) {
                // 双击事件
                isWallpaperLocked = !isWallpaperLocked;
                
                if (isWallpaperLocked) {
                    // 锁定壁纸，暂停轮播
                    if (typeof Android !== 'undefined' && Android.pauseWallpaperCarousel) {
                        Android.pauseWallpaperCarousel();
                        showToast('壁纸已锁定');
                    }
                } else {
                    // 解锁壁纸，恢复轮播
                    if (typeof Android !== 'undefined' && Android.resumeWallpaperCarousel) {
                        Android.resumeWallpaperCarousel();
                        showToast('壁纸已解锁，恢复轮播');
                    }
                }
                
                // 重置点击时间
                lastClickTime = 0;
            } else {
                // 单击事件，更新最后点击时间
                lastClickTime = currentTime;
            }
        });
        
        // 长按事件
        backgroundContainer.addEventListener('mousedown', function() {
            longPressTimer = setTimeout(function() {
                // 长按事件处理
                handleWallpaperLongPress();
            }, 800); // 800毫秒视为长按
        });
        
        backgroundContainer.addEventListener('mouseup', function() {
            clearTimeout(longPressTimer);
        });
        
        backgroundContainer.addEventListener('mouseleave', function() {
            clearTimeout(longPressTimer);
        });
        
        // 触摸事件（适用于移动设备）
        backgroundContainer.addEventListener('touchstart', function() {
            longPressTimer = setTimeout(function() {
                // 长按事件处理
                handleWallpaperLongPress();
            }, 800); // 800毫秒视为长按
        });
        
        backgroundContainer.addEventListener('touchend', function() {
            clearTimeout(longPressTimer);
        });
        
        backgroundContainer.addEventListener('touchcancel', function() {
            clearTimeout(longPressTimer);
        });
        
        console.log('壁纸双击和长按事件已初始化');
    }
}

// 处理壁纸长按事件
function handleWallpaperLongPress() {
    try {
        // 检查是否在Android环境中
        if (typeof Android !== 'undefined' && Android.deleteCurrentWallpaper) {
            // 调用后端方法删除当前壁纸
            const result = Android.deleteCurrentWallpaper();
            if (result) {
                showToast('壁纸已删除');
                // 切换到下一张壁纸
                if (typeof Android !== 'undefined' && Android.getRandomWallpaperBase64Async) {
                    const callbackId = AsyncCallbackManager.register(function (wallpaperBase64) {
                        if (wallpaperBase64 && wallpaperBase64 !== "") {
                            const backgroundElement = document.querySelector('.background-image');
                            if (backgroundElement) {
                                backgroundElement.style.transition = 'opacity 0.3s ease-in-out';
                                backgroundElement.style.opacity = '0';
                                setTimeout(() => {
                                    backgroundElement.style.backgroundImage = `url('data:image/jpeg;base64,${wallpaperBase64}')`;
                                    backgroundElement.style.opacity = '1';
                                }, 150);
                            }
                        }
                    });
                    Android.getRandomWallpaperBase64Async(callbackId);
                }
            } else {
                showToast('删除壁纸失败或当前壁纸不可删除');
            }
        }
    } catch (error) {
        console.error('处理壁纸长按事件时出错:', error);
        showToast('操作失败');
    }
}

/**
 * 添加时间显示点击事件处理
 */
function addTimeDisplayClickEvent() {
    const timeDisplay = document.getElementById('timeDisplay');
    if (timeDisplay) {
        timeDisplay.addEventListener('click', function () {
            toggleWallpaperCarousel();
        });
    }
    
    // 为顶部时间显示区域也添加点击事件
    const topLeftTime = document.querySelector('.top-left-time');
    if (topLeftTime) {
        topLeftTime.addEventListener('click', function () {
            toggleWallpaperCarousel();
        });
    }
}

/**
 * 切换壁纸轮播状态
 */
function toggleWallpaperCarousel() {
    try {
        // 获取当前壁纸设置
        if (typeof Android !== 'undefined' && Android.getWallpaperSettings) {
            const settingsJson = Android.getWallpaperSettings();
            const settings = JSON.parse(settingsJson);

            // 切换壁纸轮播状态
            const newCarouselState = !settings.wallpaper_carousel;

            // 保存新状态
            if (Android.saveWallpaperCarouselSetting) {
                const result = Android.saveWallpaperCarouselSetting(newCarouselState);
                if (result) {
                    // 显示提示信息
                    if (newCarouselState) {
                        showToast('壁纸轮播已开启');
                    } else {
                        // 恢复默认壁纸
                        restoreDefaultWallpaper();
                        showToast('已恢复默认壁纸，壁纸轮播已关闭');
                    }
                } else {
                    showToast('设置保存失败');
                }
            }
        }
    } catch (error) {
        console.error('切换壁纸轮播状态时出错:', error);
        showToast('操作失败');
    }
}

/**
 * 恢复默认壁纸
 */
function restoreDefaultWallpaper() {
    const backgroundElement = document.querySelector('.background-image');
    if (backgroundElement) {
        // 添加淡出效果
        backgroundElement.style.transition = 'opacity 0.3s ease-in-out';
        backgroundElement.style.opacity = '0';

        // 在淡出完成后设置默认壁纸并淡入
        setTimeout(() => {
            backgroundElement.style.backgroundImage = "url('images/default_bg_1.jpg')";
            backgroundElement.style.opacity = '1';
        }, 150);
    }
}

/**
 * 更新音乐播放进度
 */
function updateMusicProgress() {
    try {
        // 检查是否在Android环境中
        if (typeof Android !== 'undefined' && Android.getMusicProgressInfo) {
            const progressInfoJson = Android.getMusicProgressInfo();
            const progressInfo = JSON.parse(progressInfoJson);
            
            const progressBar = document.querySelector('.progress-bar');
            const progressBarFill = document.querySelector('.progress-fill');
            const currentTimeElement = document.querySelector('.current-time');
            
            // 添加调试信息
            //console.log('更新音乐播放进度:', progressInfo);
            
            // 计算进度百分比
            const progressPercent = progressInfo.duration > 0 
                ? (progressInfo.currentPosition / progressInfo.duration) * 100 
                : 0;
            
            if (progressBarFill) {
                // 更新进度条填充宽度
                progressBarFill.style.width = progressPercent + '%';
            }
            
            if (currentTimeElement) {
                // 格式化时间显示
                const formatTime = (ms) => {
                    if (!ms || ms <= 0) return '00:00';
                    const totalSeconds = Math.floor(ms / 1000);
                    const minutes = Math.floor(totalSeconds / 60);
                    const seconds = totalSeconds % 60;
                    return String(minutes).padStart(2, '0') + ':' + String(seconds).padStart(2, '0');
                };
                
                currentTimeElement.textContent = formatTime(progressInfo.currentPosition);
            }
            
        } else {
            //console.log('Android接口不可用');
        }
    } catch (error) {
        console.error('更新音乐播放进度时出错:', error);
    }
}

/**
 * 更新音乐播放状态图标
 */
function updateMusicPlayPauseIcon() {
    try {
        // 检查是否在Android环境中
        if (typeof Android !== 'undefined' && Android.isMusicPlaying) {
            const isPlaying = Android.isMusicPlaying();
            const playPauseBtn = document.getElementById('play-pause-btn');

            // 添加调试信息
            //console.log('检查音乐播放状态图标:', isPlaying);

            if (playPauseBtn) {
                if (isPlaying) {
                    playPauseBtn.src = 'images/nav_music_pause.png';
                    //console.log('设置为暂停图标');
                } else {
                    playPauseBtn.src = 'images/nav_music_play.png';
                    //console.log('设置为播放图标');
                }
            } else {
                //console.log('未找到播放/暂停按钮');
            }
        } else {
            //console.log('Android接口不可用');
        }
    } catch (error) {
        //console.error('更新音乐播放状态图标时出错:', error);
    }
}

// 添加控制进度条循环动画的函数
function toggleProgressLoop(enableLoop) {
    const progressBar = document.querySelector('.progress-bar');
    if (!progressBar) return;

    if (enableLoop) {
        progressBar.classList.add('looping');
    } else {
        progressBar.classList.remove('looping');
    }
}

// 初始化音乐播放控制按钮
function initMusicControls() {
    // 获取播放/暂停按钮和下一首按钮
    const playPauseBtn = document.getElementById('play-pause-btn');
    const nextBtn = document.getElementById('next-btn');

    // 为播放/暂停按钮添加点击事件监听器
    /*if (playPauseBtn) {
        playPauseBtn.addEventListener('click', function() {
            console.log('播放/暂停按钮被点击');
            // 检查是否在Android环境中
            if (typeof Android !== 'undefined' && Android.playPause) {
                console.log('调用Android.playPause方法');
                // 调用Android原生方法
                Android.playPause();
            } else {
                console.log('Android.playPause 方法未定义');
            }
        });
    }*/

    // 为下一首按钮添加点击事件监听器
    /*if (nextBtn) {
        nextBtn.addEventListener('click', function() {
            console.log('下一首按钮被点击');
            // 检查是否在Android环境中
            if (typeof Android !== 'undefined' && Android.playNext) {
                console.log('调用Android.playNext方法');
                // 调用Android原生方法
                Android.playNext();
            } else {
                console.log('Android.playNext 方法未定义');
            }
        });
    }*/
}

/**
 * 注册时间更新监听事件
 */
function registerTimeUpdateListener() {
    console.log('注册时间更新监听器');

    // 将时间更新函数挂载到window对象上，供后端调用
    window.updateTimeDisplay = function (timeData) {
        //console.log('收到时间更新数据:', timeData);

        // 如果传入的是对象格式
        if (typeof timeData === 'object' && timeData !== null) {
            if (timeData.time) {
                const timeElements = document.querySelectorAll('.top-left-time .time-text, .layout-left .time-text');
                timeElements.forEach(element => {
                    if (element) element.textContent = timeData.time;
                });
            }

            if (timeData.date) {
                const dateElement = document.getElementById('dateDisplay');
                if (dateElement) dateElement.textContent = timeData.date;
            }

            if (timeData.lunarDate) {
                const lunarElement = document.getElementById('lunarDisplay');
                if (lunarElement) lunarElement.textContent = timeData.lunarDate;
            }
        }
        // 如果传入的是三个独立参数的旧格式
        else if (arguments.length === 3) {
            const time = arguments[0];
            const date = arguments[1];
            const lunarDate = arguments[2];

            if (time) {
                const timeElements = document.querySelectorAll('.top-left-time .time-text, .layout-left .time-text');
                timeElements.forEach(element => {
                    if (element) element.textContent = time;
                });
            }

            if (date) {
                const dateElement = document.getElementById('dateDisplay');
                if (dateElement) dateElement.textContent = date;
            }

            if (lunarDate) {
                const lunarElement = document.getElementById('lunarDisplay');
                if (lunarElement) lunarElement.textContent = lunarDate;
            }
        }
    };

    // 将音乐播放状态更新函数挂载到window对象上，供后端调用
    window.updateMusicPlaybackState = function (isPlaying) {
        console.log('收到音乐播放状态更新:', isPlaying);
        // 可以在这里添加音乐播放状态的UI更新逻辑
        // 例如：显示/隐藏音乐播放器，更新播放按钮状态等
    };
    
    // ==================== 零跑C11车辆状态管理 ====================
    
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
            this.updateTurnIndicators(state.leftTurnLight, state.rightTurnLight);
            this.updateLockIndicator(state.isLocked);
            this.updateSpeedDisplay(state.speed);
            this.updateTirePressure(state);
        },
        
        /**
         * 更新档位指示器
         */
        updateGearIndicator: function(gear, gearText) {
            const element = document.getElementById('gearIndicator');
            if (element) {
                element.textContent = gearText || 'P';
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
         */
        updateDoorIndicator: function(isAnyDoorOpen, fl, fr, rl, rr, trunk, hood) {
            const element = document.getElementById('doorIndicator');
            if (element) {
                if (isAnyDoorOpen) {
                    element.style.display = 'inline';
                    element.style.color = '#ff4444';
                    // 可以根据具体哪个门开了显示不同的提示
                    let doorCount = 0;
                    if (fl === 1) doorCount++;
                    if (fr === 1) doorCount++;
                    if (rl === 1) doorCount++;
                    if (rr === 1) doorCount++;
                    if (trunk === 1) doorCount++;
                    if (hood === 1) doorCount++;
                    element.textContent = '🚪×' + doorCount;
                } else {
                    element.style.display = 'none';
                }
            }
        },
        
        /**
         * 更新转向灯指示器
         */
        updateTurnIndicators: function(left, right) {
            const leftElement = document.getElementById('leftTurnIndicator');
            const rightElement = document.getElementById('rightTurnIndicator');
            
            if (leftElement) {
                if (left === 1) {
                    leftElement.style.opacity = '1';
                    leftElement.style.animation = 'blink 0.5s infinite';
                    leftElement.style.color = '#4CAF50';
                } else {
                    leftElement.style.opacity = '0.3';
                    leftElement.style.animation = 'none';
                    leftElement.style.color = '#ffffff';
                }
            }
            
            if (rightElement) {
                if (right === 1) {
                    rightElement.style.opacity = '1';
                    rightElement.style.animation = 'blink 0.5s infinite';
                    rightElement.style.color = '#4CAF50';
                } else {
                    rightElement.style.opacity = '0.3';
                    rightElement.style.animation = 'none';
                    rightElement.style.color = '#ffffff';
                }
            }
        },
        
        /**
         * 更新锁车指示器
         */
        updateLockIndicator: function(isLocked) {
            const element = document.getElementById('lockIndicator');
            if (element) {
                if (isLocked) {
                    element.textContent = '🔒';
                    element.classList.remove('unlocked');
                } else {
                    element.textContent = '🔓';
                    element.classList.add('unlocked');
                }
            }
        },
        
        /**
         * 更新车速显示
         */
        updateSpeedDisplay: function(speed) {
            // 可以在状态栏显示车速，或者在其他位置显示
            console.log('当前车速:', speed, 'km/h');
        },

        /**
         * 更新胎压显示
         */
        updateTirePressure: function(state) {
            const pressures = [
                { selector: '.tire-pressure.front-left', pressure: state.frontLeftTirePressure, temp: state.frontLeftTireTemp },
                { selector: '.tire-pressure.front-right', pressure: state.frontRightTirePressure, temp: state.frontRightTireTemp },
                { selector: '.tire-pressure.rear-left', pressure: state.rearLeftTirePressure, temp: state.rearLeftTireTemp },
                { selector: '.tire-pressure.rear-right', pressure: state.rearRightTirePressure, temp: state.rearRightTireTemp }
            ];

            pressures.forEach(item => {
                const el = document.querySelector(item.selector);
                if (el) {
                    if (item.pressure && item.pressure > 0) {
                        // 有真实数据，显示胎压和胎温
                        const pressureKpa = Math.round(item.pressure);
                        const temp = item.temp || 0;
                        el.innerHTML = pressureKpa + 'kPa<br><span style="font-size:10px;opacity:0.7;">' + temp + '°C</span>';
                        // 异常胎压标红
                        if (pressureKpa < 200 || pressureKpa > 300) {
                            el.style.color = '#ff4444';
                        } else {
                            el.style.color = '#ffffff';
                        }
                    }
                    // 没有数据时保持默认显示
                }
            });
        },
        
        /**
         * 更新360全景指示器
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
         * 初始化车辆状态
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
         * 添加闪烁动画样式
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
    
    // ==================== 快捷开关面板 ====================
    
    /**
     * 快捷开关管理器
     * 管理快捷开关面板的显示和控制
     */
    const QuickSwitchManager = {
        isOpen: false,
        
        // 开关列表
        switches: [
            { id: 'lowBeamLight', name: '近光灯', icon: '💡', type: 'toggle' },
            { id: 'rearFogLight', name: '后雾灯', icon: '🌫️', type: 'toggle' },
            { id: 'positionLight', name: '示廓灯', icon: '🚨', type: 'toggle' },
            { id: 'pedestrianAlert', name: '行人警示', icon: '🔔', type: 'toggle' },
            { id: 'maxCooling', name: '极速制冷', icon: '❄️', type: 'toggle' },
            { id: 'nightMode', name: '夜间模式', icon: '🌙', type: 'toggle' },
            { id: 'wifi', name: 'WiFi', icon: '📶', type: 'toggle' },
            { id: 'bluetooth', name: '蓝牙', icon: '📱', type: 'toggle' },
            { id: 'videoWhileDriving', name: '行驶视频', icon: '🎬', type: 'toggle' },
            { id: 'cameraOverspeed', name: '360限速', icon: '📸', type: 'toggle' },
            { id: 'ambientLight', name: '氛围灯', icon: '🌈', type: 'toggle' },
            { id: 'speech', name: '语音播报', icon: '🔊', type: 'toggle' },
            { id: 'secondaryScreen', name: '副屏', icon: '📺', type: 'toggle' },
            { id: 'screenPresentation', name: '副屏投屏', icon: '🖥️', type: 'toggle' },
        ],
        
        // 驾驶模式列表
        driveModes: [
            { id: 0, name: '舒适', icon: '🛋️' },
            { id: 1, name: '运动', icon: '🏃' },
            { id: 2, name: '自定义', icon: '⚙️' },
            { id: 3, name: '极致', icon: '⚡' },
            { id: 4, name: '经济', icon: '🌱' },
            { id: 5, name: '零跑', icon: '🏎️' },
        ],
        
        // 场景模式列表
        sceneModes: [
            { id: 'guard', name: '守护模式', icon: '🛡️' },
            { id: 'rest', name: '小憩模式', icon: '😴' },
            { id: 'camping', name: '露营模式', icon: '⛺' },
            { id: 'powerSave', name: '省电模式', icon: '🔋' },
            { id: 'sentinel', name: '哨兵模式', icon: '👁️' },
        ],
        
        /**
         * 切换快捷开关面板
         */
        togglePanel: function() {
            this.isOpen = !this.isOpen;
            if (this.isOpen) {
                this.showPanel();
            } else {
                this.hidePanel();
            }
        },
        
        /**
         * 显示快捷开关面板
         */
        showPanel: function() {
            let panel = document.getElementById('quickSwitchPanel');
            if (!panel) {
                panel = this.createPanel();
                document.body.appendChild(panel);
            }
            panel.style.display = 'block';
            this.refreshSwitchStates();
        },
        
        /**
         * 隐藏快捷开关面板
         */
        hidePanel: function() {
            const panel = document.getElementById('quickSwitchPanel');
            if (panel) {
                panel.style.display = 'none';
            }
            this.isOpen = false;
        },
        
        /**
         * 创建快捷开关面板
         */
        createPanel: function() {
            const panel = document.createElement('div');
            panel.id = 'quickSwitchPanel';
            panel.className = 'quick-switch-panel';
            panel.style.cssText = `
                position: fixed;
                top: 50px;
                right: 20px;
                width: 360px;
                max-height: 80vh;
                background: rgba(0, 0, 0, 0.9);
                border-radius: 16px;
                padding: 20px;
                z-index: 9999;
                overflow-y: auto;
                backdrop-filter: blur(10px);
                border: 1px solid rgba(255, 255, 255, 0.1);
            `;
            
            let html = '<h3 style="color: white; margin: 0 0 16px 0; font-size: 18px;">快捷控制</h3>';
            
            // 快捷开关网格
            html += '<div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 12px; margin-bottom: 20px;">';
            this.switches.forEach(sw => {
                html += `
                    <div class="switch-item" data-id="${sw.id}" 
                         style="text-align: center; padding: 12px 8px; background: rgba(255,255,255,0.1); border-radius: 12px; cursor: pointer; transition: all 0.2s;">
                        <div style="font-size: 24px; margin-bottom: 4px;">${sw.icon}</div>
                        <div style="color: white; font-size: 12px;">${sw.name}</div>
                        <div class="switch-status" style="margin-top: 4px; font-size: 10px; color: #888;">--</div>
                    </div>
                `;
            });
            html += '</div>';
            
            // 驾驶模式
            html += '<h4 style="color: white; margin: 16px 0 12px 0; font-size: 14px;">驾驶模式</h4>';
            html += '<div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 8px; margin-bottom: 20px;">';
            this.driveModes.forEach(mode => {
                html += `
                    <div class="drive-mode-item" data-id="${mode.id}"
                         style="text-align: center; padding: 10px 6px; background: rgba(255,255,255,0.1); border-radius: 10px; cursor: pointer; font-size: 12px; color: white;">
                        <div style="font-size: 20px;">${mode.icon}</div>
                        <div>${mode.name}</div>
                    </div>
                `;
            });
            html += '</div>';
            
            // 场景模式
            html += '<h4 style="color: white; margin: 16px 0 12px 0; font-size: 14px;">场景模式</h4>';
            html += '<div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 8px;">';
            this.sceneModes.forEach(mode => {
                html += `
                    <div class="scene-mode-item" data-id="${mode.id}"
                         style="text-align: center; padding: 10px 6px; background: rgba(255,255,255,0.1); border-radius: 10px; cursor: pointer; font-size: 12px; color: white;">
                        <div style="font-size: 20px;">${mode.icon}</div>
                        <div>${mode.name}</div>
                    </div>
                `;
            });
            html += '</div>';
            
            panel.innerHTML = html;
            
            // 绑定开关点击事件
            panel.querySelectorAll('.switch-item').forEach(item => {
                item.addEventListener('click', () => {
                    this.toggleSwitch(item.dataset.id);
                });
            });
            
            // 绑定驾驶模式点击事件
            panel.querySelectorAll('.drive-mode-item').forEach(item => {
                item.addEventListener('click', () => {
                    this.setDriveMode(parseInt(item.dataset.id));
                });
            });
            
            // 绑定场景模式点击事件
            panel.querySelectorAll('.scene-mode-item').forEach(item => {
                item.addEventListener('click', () => {
                    this.toggleSceneMode(item.dataset.id);
                });
            });
            
            return panel;
        },
        
        /**
         * 切换开关
         */
        toggleSwitch: function(switchId) {
            if (!window.AndroidInterface) {
                console.warn('AndroidInterface不可用');
                return;
            }
            
            try {
                let result = false;
                switch (switchId) {
                    case 'lowBeamLight':
                        // 先获取当前状态再切换
                        result = window.AndroidInterface.setLowBeamLight(true); // 简化处理，实际需要读取状态
                        break;
                    case 'rearFogLight':
                        result = window.AndroidInterface.setRearFogLight(true);
                        break;
                    case 'positionLight':
                        result = window.AndroidInterface.setPositionLight(true);
                        break;
                    case 'pedestrianAlert':
                        result = window.AndroidInterface.setPedestrianAlert(true);
                        break;
                    case 'maxCooling':
                        result = window.AndroidInterface.setMaxCooling(true);
                        break;
                    case 'nightMode':
                        result = window.AndroidInterface.setNightMode(true);
                        break;
                    case 'wifi':
                        result = window.AndroidInterface.setWifiEnabled(true);
                        break;
                    case 'bluetooth':
                        result = window.AndroidInterface.setBluetoothEnabled(true);
                        break;
                    case 'videoWhileDriving':
                        const videoEnabled = window.AndroidInterface.isVideoWhileDrivingEnabled();
                        result = window.AndroidInterface.setVideoWhileDriving(!videoEnabled);
                        break;
                    case 'cameraOverspeed':
                        const overspeedEnabled = window.AndroidInterface.isCameraOverspeedLimitEnabled();
                        result = window.AndroidInterface.setCameraOverspeedLimit(!overspeedEnabled);
                        break;
                    case 'ambientLight':
                        const ambientEnabled = window.AndroidInterface.isAmbientLightEnabled();
                        result = window.AndroidInterface.setAmbientLightEnabled(!ambientEnabled);
                        break;
                    case 'speech':
                        const speechEnabled = window.AndroidInterface.isSpeechEnabled();
                        result = window.AndroidInterface.setSpeechEnabled(!speechEnabled);
                        break;
                    case 'secondaryScreen':
                        const screenEnabled = window.AndroidInterface.isSecondaryScreenEnabled();
                        result = window.AndroidInterface.setSecondaryScreenEnabled(!screenEnabled);
                        break;
                    case 'screenPresentation':
                        const showing = window.AndroidInterface.isPresentationShowing();
                        if (showing) {
                            window.AndroidInterface.hidePresentation();
                            result = true;
                        } else {
                            result = window.AndroidInterface.showCarStatusPresentation();
                        }
                        break;
                }
                
                console.log('切换开关:', switchId, '结果:', result);
                this.refreshSwitchStates();
                
            } catch (e) {
                console.error('切换开关失败:', switchId, e);
            }
        },
        
        /**
         * 设置驾驶模式
         */
        setDriveMode: function(mode) {
            if (window.AndroidInterface && window.AndroidInterface.setDriveMode) {
                const result = window.AndroidInterface.setDriveMode(mode);
                console.log('设置驾驶模式:', mode, '结果:', result);
            }
        },
        
        /**
         * 切换场景模式
         */
        toggleSceneMode: function(modeId) {
            if (!window.AndroidInterface) return;
            
            try {
                let result = false;
                switch (modeId) {
                    case 'guard':
                        result = window.AndroidInterface.setGuardMode(true);
                        break;
                    case 'rest':
                        result = window.AndroidInterface.setRestMode(true);
                        break;
                    case 'camping':
                        result = window.AndroidInterface.setCampingMode(true);
                        break;
                    case 'powerSave':
                        result = window.AndroidInterface.setPowerSaveMode(true);
                        break;
                    case 'sentinel':
                        result = window.AndroidInterface.setSentinelMode(true);
                        break;
                }
                console.log('切换场景模式:', modeId, '结果:', result);
            } catch (e) {
                console.error('切换场景模式失败:', modeId, e);
            }
        },
        
        /**
         * 刷新开关状态显示
         */
        refreshSwitchStates: function() {
            if (!window.AndroidInterface) return;
            
            const panel = document.getElementById('quickSwitchPanel');
            if (!panel) return;
            
            // 更新可读取状态的开关
            const statusMap = {
                'videoWhileDriving': window.AndroidInterface.isVideoWhileDrivingEnabled ? window.AndroidInterface.isVideoWhileDrivingEnabled() : null,
                'cameraOverspeed': window.AndroidInterface.isCameraOverspeedLimitEnabled ? window.AndroidInterface.isCameraOverspeedLimitEnabled() : null,
                'ambientLight': window.AndroidInterface.isAmbientLightEnabled ? window.AndroidInterface.isAmbientLightEnabled() : null,
                'speech': window.AndroidInterface.isSpeechEnabled ? window.AndroidInterface.isSpeechEnabled() : null,
                'secondaryScreen': window.AndroidInterface.isSecondaryScreenEnabled ? window.AndroidInterface.isSecondaryScreenEnabled() : null,
                'screenPresentation': window.AndroidInterface.isPresentationShowing ? window.AndroidInterface.isPresentationShowing() : null,
            };
            
            panel.querySelectorAll('.switch-item').forEach(item => {
                const id = item.dataset.id;
                const statusEl = item.querySelector('.switch-status');
                if (statusEl && statusMap[id] !== null && statusMap[id] !== undefined) {
                    statusEl.textContent = statusMap[id] ? '开' : '关';
                    statusEl.style.color = statusMap[id] ? '#4CAF50' : '#888';
                    item.style.background = statusMap[id] ? 'rgba(76, 175, 80, 0.2)' : 'rgba(255,255,255,0.1)';
                }
            });
            
            // 更新桌面快捷开关状态
            const desktopContainer = document.getElementById('quickSwitchesContainer');
            if (desktopContainer) {
                desktopContainer.querySelectorAll('.quick-switch-item[data-id]').forEach(item => {
                    const id = item.getAttribute('data-id');
                    if (statusMap[id] !== null && statusMap[id] !== undefined) {
                        if (statusMap[id]) {
                            item.classList.add('active');
                        } else {
                            item.classList.remove('active');
                        }
                    }
                });
            }
        }
    };
    
    // 将快捷开关管理器挂载到window对象上
    window.QuickSwitchManager = QuickSwitchManager;
    
    // 为360全景按钮添加快捷开关面板切换功能
    document.addEventListener('DOMContentLoaded', function() {
        const cameraBtn = document.getElementById('camera360Btn');
        if (cameraBtn) {
            let pressTimer = null;
            let isLongPress = false;
            
            // 长按显示快捷开关面板
            cameraBtn.addEventListener('mousedown', function(e) {
                isLongPress = false;
                pressTimer = setTimeout(function() {
                    isLongPress = true;
                    if (window.QuickSwitchManager) {
                        window.QuickSwitchManager.togglePanel();
                    }
                }, 500);
            });
            
            cameraBtn.addEventListener('mouseup', function(e) {
                if (pressTimer) {
                    clearTimeout(pressTimer);
                    pressTimer = null;
                }
            });
            
            cameraBtn.addEventListener('mouseleave', function(e) {
                if (pressTimer) {
                    clearTimeout(pressTimer);
                    pressTimer = null;
                }
            });
            
            // 单击还是触发360全景
            cameraBtn.addEventListener('click', function(e) {
                if (isLongPress) {
                    e.preventDefault();
                    e.stopPropagation();
                    isLongPress = false;
                }
            });
            
            // 触摸事件支持
            cameraBtn.addEventListener('touchstart', function(e) {
                isLongPress = false;
                pressTimer = setTimeout(function() {
                    isLongPress = true;
                    if (window.QuickSwitchManager) {
                        window.QuickSwitchManager.togglePanel();
                    }
                }, 500);
            });
            
            cameraBtn.addEventListener('touchend', function(e) {
                if (pressTimer) {
                    clearTimeout(pressTimer);
                    pressTimer = null;
                }
            });
        }
    });

    console.log('时间更新监听器注册完成');
}

// ==================== 自动化场景配置 ====================

/**
 * 自动化场景管理器
 * 管理各种自动化场景的配置和触发
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
     */
    init: function() {
        console.log('自动化场景管理器初始化');
        this.loadSettings();
    },
    
    /**
     * 加载配置
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
     */
    isEnabled: function(scenarioId) {
        return this.enabledScenarios[scenarioId] === true;
    },
    
    /**
     * 切换场景启用状态
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
     * 切换自动化配置面板
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
        
        return panel;
    },
    
    /**
     * 刷新面板状态
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

