/**
 * index.js - 主入口模块
 * @description 统一初始化入口 + 设置面板逻辑 + 面板显示/隐藏控制
 */

/**
 * 初始化队列注册函数
 * @description 将初始化函数注册到 AppBootstrap 队列，若队列不可用则降级到 load 事件
 */
const safeInit = function(name, fn) {
    if (window.AppBootstrap && window.AppBootstrap.registerInit) {
        window.AppBootstrap.registerInit(name, fn);
    } else {
        window.addEventListener('load', function() {
            try { fn(); } catch(e) { console.error('[Init] ' + name + ' 失败:', e); }
        });
    }
};

/**
 * 面板显示/隐藏控制（原 panel-controller.js，合并于此）
 * 规范：active 类 + display 属性配合控制
 *   打开：先 display:flex，再添加 active 类触发动画
 *   关闭：先移除 active 类，等动画结束再 display:none
 *   可见性判断：classList.contains('active')
 */
function showSettingsModal() {
    const modal = document.getElementById('settingsModal');
    if (!modal) return;
    modal.style.display = 'flex';
    requestAnimationFrame(function() {
        modal.classList.add('active');
    });
}

function hideSettingsModal() {
    const modal = document.getElementById('settingsModal');
    if (!modal) return;
    modal.classList.remove('active');
    setTimeout(function() {
        modal.style.display = 'none';
    }, 350);
}

function showAppsModal() {
    const modal = document.getElementById('appsModal');
    if (!modal) return;
    modal.style.display = 'flex';
    requestAnimationFrame(function() {
        modal.classList.add('active');
    });

    // 触发应用列表加载
    const appsLoading = document.getElementById('appsLoading');
    const appsList = document.getElementById('appsList');
    if (appsLoading && appsList) {
        appsList.style.display = 'none';
        appsLoading.style.display = 'block';
    }
    if (window.AppListManager && typeof window.AppListManager.loadAppList === 'function') {
        setTimeout(function() {
            window.AppListManager.loadAppList();
        }, 1);
    }
}

function hideAppsModal() {
    const modal = document.getElementById('appsModal');
    if (!modal) return;
    modal.classList.remove('active');
    setTimeout(function() {
        modal.style.display = 'none';
    }, 350);
}

function initWallpaperDoubleClick() {
    const bgContainer = document.querySelector('.background-container');
    if (!bgContainer) return;

    let lastClickTime = 0;
    bgContainer.addEventListener('click', function(e) {
        const now = Date.now();
        if (now - lastClickTime < 300) {
            toggleWallpaperCarousel();
        }
        lastClickTime = now;
    });
}

function handleWallpaperLongPress() {
    const bgContainer = document.querySelector('.background-container');
    if (!bgContainer) return;

    let longPressTimer = null;
    let isLongPress = false;

    bgContainer.addEventListener('touchstart', function(e) {
        isLongPress = false;
        longPressTimer = setTimeout(function() {
            isLongPress = true;
            showSettingsModal();
        }, 500);
    });

    bgContainer.addEventListener('touchend', function() {
        if (longPressTimer) {
            clearTimeout(longPressTimer);
            longPressTimer = null;
        }
    });

    bgContainer.addEventListener('touchmove', function() {
        if (longPressTimer) {
            clearTimeout(longPressTimer);
            longPressTimer = null;
        }
    });
}

function loadWallpaperSettings() {
    if (window.SettingsSync) { SettingsSync.loadWallpaperSettings(); }
}

function initEffectLevel() {
    if (window.SettingsSync) { SettingsSync.initEffectLevel(); }
}

function applyEffectLevel(level) {
    if (window.SettingsSync) { SettingsSync.applyEffectLevel(level); }
}

function initThemeMode() {
    if (window.SettingsSync) { SettingsSync.initThemeMode(); }
}

function applyThemeMode(mode) {
    if (window.SettingsSync) { SettingsSync.applyThemeMode(mode); }
}

function initThemeToggleIcon() {
    if (window.SettingsSync) { SettingsSync.initThemeToggleIcon(); }
}

function toggleWallpaperCarousel() {
    if (window.SettingsSync) { SettingsSync.toggleWallpaperCarousel(); }
}

function restoreDefaultWallpaper() {
    if (window.SettingsSync) { SettingsSync.restoreDefaultWallpaper(); }
}

/**
 * UI 初始化器适配层
 */
function loadQuickApps() {
    if (window.UiInitializer) { UiInitializer.loadQuickApps(); }
}

function loadQuickSwitches() {
    if (window.UiInitializer) { UiInitializer.loadQuickSwitches(); }
}

function updateACControlStatus() {
    if (window.UiInitializer) { UiInitializer.updateACControlStatus(); }
}

function checkWifiStatus() {
    if (window.UiInitializer) { UiInitializer.checkWifiStatus(); }
}

function checkBluetoothStatus() {
    if (window.UiInitializer) { UiInitializer.checkBluetoothStatus(); }
}

function checkLocationStatus() {
    if (window.UiInitializer) { UiInitializer.checkLocationStatus(); }
}

function updateNetworkAndBluetoothStatus() {
    if (window.UiInitializer) { UiInitializer.updateNetworkAndBluetoothStatus(); }
}

function initHorizontalScroll() {
    if (window.UiInitializer) { UiInitializer.initHorizontalScroll(); }
}

function updateMusicProgress() {
    if (window.UiInitializer) { UiInitializer.updateMusicProgress(); }
}

function initAcTemperature() {
    if (window.UiInitializer) { UiInitializer.initAcTemperature(); }
}

function updateAcTemperature(temp) {
    if (window.UiInitializer) { UiInitializer.updateAcTemperature(temp); }
}

function updateAcState(acOn) {
    if (window.UiInitializer) { UiInitializer.updateAcState(acOn); }
}

function updateWindLevel(level) {
    if (window.UiInitializer) { UiInitializer.updateWindLevel(level); }
}

function initMusicControls() {
    if (window.UiInitializer) { UiInitializer.initMusicControls(); }
}

function addTimeDisplayClickEvent() {
    if (window.UiInitializer) { UiInitializer.addTimeDisplayClickEvent(); }
}

function initNavigationButtons() {
    if (window.UiInitializer) { UiInitializer.initNavigationButtons(); }
}

/**
 * 应用列表管理器适配层
 */
function initAppsModal() {
    if (window.AppListManager) { AppListManager.initAppsModal(); }
}

function renderAppsList(appsData) {
    if (window.AppListManager) { AppListManager.renderAppsList(appsData); }
}

function escapeHtml(text) {
    if (window.AppListManager) { return AppListManager.escapeHtml(text); }
    return text;
}

function normalizeAppIcon(icon) {
    if (window.AppListManager) { return AppListManager.normalizeAppIcon(icon); }
    return icon || 'images/ic_launcher.png';
}

function initAlphabetNav() {
    if (window.AppListManager) { AppListManager.initAlphabetNav(); }
}

function showAddToQuickAppsDialog(app) {
    if (window.AppListManager) { AppListManager.showAddToQuickAppsDialog(app); }
}

function showRemoveFromQuickAppsDialog(app) {
    if (window.AppListManager) { AppListManager.showRemoveFromQuickAppsDialog(app); }
}

/**
 * 模块内部状态对象
 */
const indexState = {
    isLoadingSettings: false,
    categoryCheckboxEventsInitialized: false,
    componentConfigListenersAdded: false,
    currentDialog: null,
    isConfigurableButtonsInitialized: false
};

/**
 * 防重复绑定辅助函数
 */
function ensureListener(element, event, handler) {
    if (!element || element.dataset.listenerAdded) return;
    element.addEventListener(event, handler);
    element.dataset.listenerAdded = 'true';
}

/**
 * 设置面板初始化（未迁移的部分：TAB切换、复选框事件）
 */
function initSettingsModal() {
    const settingsBtn = document.getElementById('settingsBtn');
    const closeSettings = document.getElementById('closeSettings');
    const settingsModal = document.getElementById('settingsModal');
    const tabButtons = document.querySelectorAll('.tab-button');
    const tabContents = document.querySelectorAll('.tab-content');

    if (!settingsModal) return;

    if (settingsBtn) {
        settingsBtn.addEventListener('click', function () {
            if (settingsModal.classList.contains('active')) {
                hideSettingsModal();
            } else {
                showSettingsModal();
            }
        });
    }

    if (closeSettings) {
        closeSettings.addEventListener('click', hideSettingsModal);
    }

    tabButtons.forEach(button => {
        button.addEventListener('click', function () {
            const tabId = this.getAttribute('data-tab');
            tabButtons.forEach(btn => btn.classList.remove('active'));
            this.classList.add('active');
            tabContents.forEach(content => content.classList.remove('active'));
            document.getElementById(`${tabId}-tab`).classList.add('active');
            if (tabId === 'components') {
                loadComponentConfigs();
            }
            if (tabId === 'apps') {
                initRestartAppButton();
            }
        });
    });

    const wallpaperCarouselCheckbox = document.getElementById('wallpaperCarouselCheckbox');
    ensureListener(wallpaperCarouselCheckbox, 'change', function () {
        if (typeof Android !== 'undefined' && Android.saveWallpaperCarouselSetting) {
            const result = Android.saveWallpaperCarouselSetting(this.checked);
            if (result) {
                showToast('壁纸轮播设置保存成功');
                ensureWallpaperCarouselSettings();
            } else {
                showToast('壁纸轮播设置保存失败');
            }
        }
    });

    const randomModeCheckbox = document.getElementById('randomModeCheckbox');
    ensureListener(randomModeCheckbox, 'change', function () {
        if (indexState.isLoadingSettings) return;
        if (typeof Android !== 'undefined' && Android.saveRandomModeSetting) {
            const result = Android.saveRandomModeSetting(this.checked);
            if (result) {
                showToast('随机模式设置保存成功');
                if (typeof Android !== 'undefined' && Android.sendWallpaperSettingsChangedBroadcast) {
                    try { Android.sendWallpaperSettingsChangedBroadcast(); } catch(e) {}
                }
            } else {
                showToast('随机模式设置保存失败');
            }
        }
    });

    const specifiedModeCheckbox = document.getElementById('specifiedModeCheckbox');
    ensureListener(specifiedModeCheckbox, 'change', function () {
        if (indexState.isLoadingSettings) return;
        if (typeof Android !== 'undefined' && Android.saveSpecifiedModeSetting) {
            const result = Android.saveSpecifiedModeSetting(this.checked);
            if (result) {
                showToast('指定模式设置保存成功');
                if (typeof Android !== 'undefined' && Android.sendWallpaperSettingsChangedBroadcast) {
                    try { Android.sendWallpaperSettingsChangedBroadcast(); } catch(e) {}
                }
            } else {
                showToast('指定模式设置保存失败');
            }
        }
    });

    const switchIntervalInput = document.getElementById('switchIntervalInput');
    ensureListener(switchIntervalInput, 'change', function () {
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

    settingsModal.addEventListener('click', function (event) {
        if (event.target === settingsModal) {
            hideSettingsModal();
        }
    });
}

/**
 * 加载系统设置
 */
function loadSystemSettings(settings) {
    const systemLauncherCheckbox = document.getElementById('systemLauncherCheckbox');
    if (systemLauncherCheckbox) {
        systemLauncherCheckbox.checked = settings.system_launcher !== undefined ? settings.system_launcher : false;
    }
    const bootGreetingCheckbox = document.getElementById('bootGreetingCheckbox');
    if (bootGreetingCheckbox) {
        bootGreetingCheckbox.checked = settings.boot_greeting !== undefined ? settings.boot_greeting : false;
    }
}

/**
 * 加载已启用的壁纸分类状态
 */
function loadEnabledCategories() {
    if (typeof Android !== 'undefined' && Android.getEnabledCategoriesAsync) {
        const callbackId = AsyncCallbackManager.register(function (enabledCategoriesJson) {
            try {
                const enabledCategories = JSON.parse(enabledCategoriesJson);
                const checkboxes = document.querySelectorAll('.category-checkbox');
                checkboxes.forEach(checkbox => {
                    const categoryId = checkbox.getAttribute('data-category-id');
                    checkbox.checked = enabledCategories.includes(categoryId);
                });
            } catch (e) {
                console.error('加载已启用分类时出错:', e);
            }
        });
        Android.getEnabledCategoriesAsync(callbackId);
    } else if (typeof Android !== 'undefined' && Android.getEnabledCategories) {
        try {
            const enabledCategoriesJson = Android.getEnabledCategories();
            const enabledCategories = JSON.parse(enabledCategoriesJson);
            const checkboxes = document.querySelectorAll('.category-checkbox');
            checkboxes.forEach(checkbox => {
                const categoryId = checkbox.getAttribute('data-category-id');
                checkbox.checked = enabledCategories.includes(categoryId);
            });
        } catch (e) {
            console.error('加载已启用分类时出错:', e);
        }
    }
}

/**
 * 初始化壁纸分类复选框事件监听器
 */
function initCategoryCheckboxEvents() {
    const checkboxes = document.querySelectorAll('.category-checkbox');
    checkboxes.forEach(checkbox => {
        if (!checkbox.dataset.listenerAdded) {
            checkbox.addEventListener('click', function () {
                const categoryId = this.getAttribute('data-category-id');
                const enabled = this.checked;
                if (typeof Android !== 'undefined' && Android.updateCategoryEnabledAsync) {
                    const callbackId = AsyncCallbackManager.register(function (result) {
                        showToast(result === "true" ? '分类状态保存成功' : '分类状态保存失败');
                    });
                    Android.updateCategoryEnabledAsync(categoryId, enabled, callbackId);
                } else if (typeof Android !== 'undefined' && Android.updateCategoryEnabled) {
                    Android.updateCategoryEnabled(categoryId, enabled);
                    showToast('分类状态保存成功');
                }
            });
            checkbox.dataset.listenerAdded = 'true';
        }
    });
}

/**
 * 初始化重启应用按钮
 */
function initRestartAppButton() {
    const restartBtn = document.getElementById('restartAppBtn');
    if (!restartBtn || restartBtn.dataset.listenerAdded) return;
    restartBtn.addEventListener('click', function () {
        if (typeof Android !== 'undefined' && Android.restartApp) {
            Android.restartApp();
        }
    });
    restartBtn.dataset.listenerAdded = 'true';
}

/**
 * 初始化ADB按钮
 */
function initADBButton() {
    const adbBtn = document.getElementById('adbBtn');
    if (!adbBtn || adbBtn.dataset.listenerAdded) return;
    adbBtn.addEventListener('click', function () {
        if (typeof Android !== 'undefined' && Android.openAdbSettings) {
            Android.openAdbSettings();
        }
    });
    adbBtn.dataset.listenerAdded = 'true';
}

/**
 * 组件可见性管理
 */
function initComponentVisibility() {
    loadComponentConfigs();
    addComponentConfigEventListeners();
}

function loadComponentConfigs() {
    if (typeof Android !== 'undefined' && Android.getComponentConfigs) {
        try {
            const configsJson = Android.getComponentConfigs();
            const configs = JSON.parse(configsJson);
            for (const [name, visible] of Object.entries(configs)) {
                updateComponentVisibility(name, visible);
            }
        } catch (e) {
            console.error('加载组件配置失败:', e);
        }
    }
}

function addComponentConfigEventListeners() {
    if (indexState.componentConfigListenersAdded) return;
    indexState.componentConfigListenersAdded = true;
    
    const configItems = document.querySelectorAll('.widget-config-item');
    configItems.forEach(item => {
        const checkbox = item.querySelector('input[type="checkbox"]');
        if (checkbox) {
            checkbox.addEventListener('change', function () {
                const componentName = this.getAttribute('data-component');
                if (typeof Android !== 'undefined' && Android.setComponentVisible) {
                    Android.setComponentVisible(componentName, this.checked);
                }
                updateComponentVisibility(componentName, this.checked);
            });
        }
    });
}

function updateComponentVisibility(componentName, isVisible) {
    const element = document.getElementById(componentName);
    if (element) {
        element.style.display = isVisible ? '' : 'none';
    }
}

/**
 * 空调控制辅助函数
 */
function decreaseWindLevel() {
    if (typeof Android !== 'undefined' && Android.decreaseWindLevel) {
        Android.decreaseWindLevel();
    }
}

function increaseWindLevel() {
    if (typeof Android !== 'undefined' && Android.increaseWindLevel) {
        Android.increaseWindLevel();
    }
}

function getCurrentWindLevel() {
    if (typeof Android !== 'undefined' && Android.getWindLevel) {
        return Android.getWindLevel();
    }
    return 0;
}

function decreaseTemperature() {
    if (typeof Android !== 'undefined' && Android.decreaseTemperature) {
        Android.decreaseTemperature();
    }
}

function increaseTemperature() {
    if (typeof Android !== 'undefined' && Android.increaseTemperature) {
        Android.increaseTemperature();
    }
}

function setWindLevel(level) {
    if (typeof Android !== 'undefined' && Android.setWindLevel) {
        Android.setWindLevel(level);
    }
}

function toggleAirConditioning() {
    if (typeof Android !== 'undefined' && Android.toggleAirConditioning) {
        Android.toggleAirConditioning();
    }
}

function toggleDefrost() {
    if (typeof Android !== 'undefined' && Android.toggleDefrost) {
        Android.toggleDefrost();
    }
}

/**
 * 壁纸轮播设置辅助
 */
function ensureWallpaperCarouselSettings() {
    const switchIntervalInput = document.getElementById('switchIntervalInput');
    const wallpaperCarouselCheckbox = document.getElementById('wallpaperCarouselCheckbox');
    if (switchIntervalInput && wallpaperCarouselCheckbox) {
        switchIntervalInput.parentElement.style.display = wallpaperCarouselCheckbox.checked ? 'flex' : 'none';
    }
}

/**
 * 触摸滑动监听
 */
function addTouchSwipeListener() {
    const container = document.querySelector('.main-container');
    if (!container) return;

    let startX = 0;
    let startY = 0;
    let isSwiping = false;

    container.addEventListener('touchstart', function(e) {
        startX = e.touches[0].clientX;
        startY = e.touches[0].clientY;
        isSwiping = true;
    });

    container.addEventListener('touchend', function(e) {
        if (!isSwiping) return;
        isSwiping = false;

        const endX = e.changedTouches[0].clientX;
        const endY = e.changedTouches[0].clientY;
        const deltaX = endX - startX;
        const deltaY = endY - startY;

        if (Math.abs(deltaX) > Math.abs(deltaY) && Math.abs(deltaX) > 50) {
            if (deltaX > 0) {
                console.log('向右滑动');
            } else {
                console.log('向左滑动');
            }
        }
    });
}

/**
 * 点击效果
 */
function addClickEffect() {
    const clickableElements = document.querySelectorAll('.qsp-item, .app-item, .tab-button, .setting-item');
    clickableElements.forEach(element => {
        element.addEventListener('touchstart', function() {
            this.style.opacity = '0.7';
            this.style.backgroundColor = 'rgba(255, 255, 255, 0.1)';
        });
        element.addEventListener('touchend', function() {
            this.style.opacity = '';
            this.style.backgroundColor = '';
        });
    });
}

/**
 * 统一初始化入口
 */
safeInit('addClickEffect', addClickEffect);
safeInit('initSettingsModal', initSettingsModal);
safeInit('initAppsModal', initAppsModal);
safeInit('loadQuickApps', loadQuickApps);
safeInit('loadQuickSwitches', loadQuickSwitches);
safeInit('initConfigurableButtons', function() {
    if (!indexState.isConfigurableButtonsInitialized) {
        indexState.isConfigurableButtonsInitialized = true;
        const buttons = document.querySelectorAll('.configurable-button');
        buttons.forEach(btn => {
            if (!btn.dataset.listenerAdded) {
                btn.addEventListener('click', function() {
                    const action = this.getAttribute('data-action');
                    if (action === 'launchApp' && typeof Android !== 'undefined' && Android.launchApp) {
                        Android.launchApp(this.getAttribute('data-package'));
                    }
                });
                btn.dataset.listenerAdded = 'true';
            }
        });
    }
});
safeInit('addTouchSwipeListener', addTouchSwipeListener);
safeInit('initHorizontalScroll', initHorizontalScroll);
safeInit('updateNetworkAndBluetoothStatus', updateNetworkAndBluetoothStatus);
safeInit('initWallpaperDoubleClick', initWallpaperDoubleClick);
safeInit('initComponentVisibility', initComponentVisibility);
safeInit('initMusicControls', initMusicControls);
safeInit('initAcTemperature', initAcTemperature);
safeInit('updateMusicProgress', updateMusicProgress);
safeInit('addTimeDisplayClickEvent', addTimeDisplayClickEvent);
safeInit('initNavigationButtons', initNavigationButtons);
safeInit('initEffectLevel', initEffectLevel);
safeInit('initThemeMode', initThemeMode);
safeInit('initThemeToggleIcon', initThemeToggleIcon);
