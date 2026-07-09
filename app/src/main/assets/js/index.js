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
            if (window.SettingsSync) { SettingsSync.toggleWallpaperCarousel(); }
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

/**
 * UI 初始化函数（原 ui-initializer.js，合并于此）
 */
function loadQuickApps() {
    const quickAppsContainer = document.getElementById('quickAppsContainer');
    const quickAppsWidget = document.querySelector('.quick-apps-widget');

    quickAppsContainer.innerHTML = '';

    if (typeof Android !== 'undefined' && Android.getQuickAppList) {
        try {
            const quickAppsJson = Android.getQuickAppList();
            const quickApps = JSON.parse(quickAppsJson);

            if (quickAppsWidget) {
                quickAppsWidget.style.display = 'flex';
            }

            if (quickApps.length === 0) {
                const emptyTip = document.createElement('div');
                emptyTip.style.cssText = 'color: rgba(255,255,255,0.6); font-size: 14px; padding: 0 20px; text-align: center;';
                emptyTip.innerHTML = '长按应用列表<br>中的应用添加';
                quickAppsContainer.appendChild(emptyTip);
                quickAppsContainer.appendChild(createShowAllAppsButton());
                return;
            }

            quickApps.forEach(app => {
                const appItem = document.createElement('div');
                appItem.className = 'quick-app-item';
                appItem.setAttribute('data-package', app.packageName);
                const appSafeIcon = window.AppListManager ? AppListManager.normalizeAppIcon(app.icon) : (app.icon || 'images/ic_launcher.png');
                const appSafeName = window.AppListManager ? AppListManager.escapeHtml(app.name) : app.name;
                appItem.innerHTML = `
                    <div style="background-image: url('${appSafeIcon}');"></div>
                    <div>${appSafeName}</div>
                `;
                appItem.addEventListener('click', function() {
                    if (typeof Android !== 'undefined' && Android.launchApp) {
                        Android.launchApp(app.packageName);
                    }
                });
                appItem.addEventListener('contextmenu', function(e) {
                    e.preventDefault();
                    if (window.AppListManager && typeof AppListManager.showRemoveFromQuickAppsDialog === 'function') {
                        AppListManager.showRemoveFromQuickAppsDialog(app);
                    }
                });
                quickAppsContainer.appendChild(appItem);
            });

            quickAppsContainer.appendChild(createShowAllAppsButton());
        } catch (e) {
            console.error('加载快速启动应用列表失败:', e);
            if (quickAppsWidget) {
                quickAppsWidget.style.display = 'flex';
            }
            const mockQuickApps = [
                { name: '高德地图', packageName: 'com.autonavi.minimap', icon: 'images/ic_launcher.png' },
                { name: '音乐', packageName: 'com.android.music', icon: 'images/ic_launcher.png' },
                { name: '微信', packageName: 'com.tencent.mm', icon: 'images/ic_launcher.png' },
            ];
            mockQuickApps.forEach(app => {
                const appItem = document.createElement('div');
                appItem.className = 'quick-app-item';
                const appSafeName = window.AppListManager ? AppListManager.escapeHtml(app.name) : app.name;
                appItem.innerHTML = `
                    <div style="background-image: url('${app.icon}');"></div>
                    <div>${appSafeName}</div>
                `;
                quickAppsContainer.appendChild(appItem);
            });
            quickAppsContainer.appendChild(createShowAllAppsButton());
        }
    } else {
        if (quickAppsWidget) {
            quickAppsWidget.style.display = 'flex';
        }
        const mockQuickApps = [
            { name: '高德地图', packageName: 'com.autonavi.minimap', icon: 'images/ic_launcher.png' },
            { name: '音乐', packageName: 'com.android.music', icon: 'images/ic_launcher.png' },
            { name: '微信', packageName: 'com.tencent.mm', icon: 'images/ic_launcher.png' },
        ];
        mockQuickApps.forEach(app => {
            const appItem = document.createElement('div');
            appItem.className = 'quick-app-item';
            const appSafeName = window.AppListManager ? AppListManager.escapeHtml(app.name) : app.name;
            appItem.innerHTML = `
                <div style="background-image: url('${app.icon}');"></div>
                <div>${appSafeName}</div>
            `;
            quickAppsContainer.appendChild(appItem);
        });
        quickAppsContainer.appendChild(createShowAllAppsButton());
    }
}

function createShowAllAppsButton() {
    const allBtn = document.createElement('div');
    allBtn.className = 'quick-app-item';
    allBtn.innerHTML = `
        <div>
            <svg viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="rgba(255,255,255,0.8)" stroke-width="2" stroke-linecap="round">
                <circle cx="5" cy="5" r="1.5"/><circle cx="12" cy="5" r="1.5"/><circle cx="19" cy="5" r="1.5"/>
                <circle cx="5" cy="12" r="1.5"/><circle cx="12" cy="12" r="1.5"/><circle cx="19" cy="12" r="1.5"/>
                <circle cx="5" cy="19" r="1.5"/><circle cx="12" cy="19" r="1.5"/><circle cx="19" cy="19" r="1.5"/>
            </svg>
        </div>
        <div>全部</div>
    `;
    allBtn.addEventListener('click', function (e) {
        e.stopPropagation();
        e.preventDefault();
        if (typeof showAppsModal === 'function') {
            showAppsModal();
        }
    });
    return allBtn;
}

function loadQuickSwitches() {
    const container = document.getElementById('quickSwitchesContainer');
    if (!container) return;

    container.innerHTML = '';

    try {
        const acControls = [
            { id: 'acSwitch', name: '空调', icon: '❄️', type: 'toggle' },
            { id: 'windMinus', name: '风量-', icon: '🌬️', type: 'action' },
            { id: 'windPlus', name: '风量+', icon: '💨', type: 'action' },
            { id: 'tempMinus', name: '温度-', icon: '🌡️', type: 'action' },
            { id: 'tempPlus', name: '温度+', icon: '🔥', type: 'action' },
        ];

        const debounceFn = typeof debounce !== 'undefined' ? debounce : function(fn) { return fn; };

        acControls.forEach(ctrl => {
            const item = document.createElement('div');
            item.className = 'qsp-item qsp-switch';
            item.setAttribute('data-id', ctrl.id);
            item.setAttribute('data-type', ctrl.type);
            item.innerHTML = `
                <div>${ctrl.icon}</div>
                <div>${ctrl.name}</div>
            `;

            item.addEventListener('click', debounceFn(function() {
                if (window.ACManager) {
                    if (ctrl.type === 'toggle') {
                        window.ACManager.toggleAC();
                    } else if (ctrl.id === 'windMinus') {
                        window.ACManager.decreaseWind();
                    } else if (ctrl.id === 'windPlus') {
                        window.ACManager.increaseWind();
                    } else if (ctrl.id === 'tempMinus') {
                        window.ACManager.decreaseTemp();
                    } else if (ctrl.id === 'tempPlus') {
                        window.ACManager.increaseTemp();
                    }
                    updateACControlStatus();
                } else if (window.Android) {
                    if (ctrl.type === 'toggle') {
                        Android.toggleAirConditioning();
                    } else if (ctrl.id === 'windMinus') {
                        decreaseWindLevel();
                    } else if (ctrl.id === 'windPlus') {
                        increaseWindLevel();
                    } else if (ctrl.id === 'tempMinus') {
                        decreaseTemperature();
                    } else if (ctrl.id === 'tempPlus') {
                        increaseTemperature();
                    }
                    updateACControlStatus();
                }
            }));

            container.appendChild(item);
        });

        const allBtn = document.createElement('div');
        allBtn.className = 'qsp-item qsp-switch';
        allBtn.innerHTML = `
            <div>
                <svg viewBox="0 0 24 24" width="24" height="24" fill="none" stroke="rgba(255,255,255,0.8)" stroke-width="2" stroke-linecap="round">
                    <circle cx="5" cy="5" r="1.5"/><circle cx="12" cy="5" r="1.5"/><circle cx="19" cy="5" r="1.5"/>
                    <circle cx="5" cy="12" r="1.5"/><circle cx="12" cy="12" r="1.5"/><circle cx="19" cy="12" r="1.5"/>
                    <circle cx="5" cy="19" r="1.5"/><circle cx="12" cy="19" r="1.5"/><circle cx="19" cy="19" r="1.5"/>
                </svg>
            </div>
            <div>全部</div>
        `;
        allBtn.addEventListener('click', function(e) {
            e.stopPropagation();
            e.preventDefault();
            if (window.QuickSwitchManager && typeof window.QuickSwitchManager.showPanel === 'function') {
                window.QuickSwitchManager.showPanel();
            }
        });
        container.appendChild(allBtn);
    } catch (e) {
        console.error('加载快捷开关失败:', e);
    }
}

function updateACControlStatus() {
    if (window.ACManager) {
        const acStatus = window.ACManager.getStatus();
        const acSwitch = document.querySelector('[data-id="acSwitch"]');
        if (acSwitch) {
            acSwitch.classList.toggle('qsp-active', acStatus.acOn);
        }
    }
}

function checkWifiStatus() {
    const wifiIcon = document.getElementById('wifiIcon');
    if (!wifiIcon) return;
    wifiIcon.style.display = 'block';

    if (typeof Android !== 'undefined' && Android.isWifiConnected) {
        try {
            const isConnected = Android.isWifiConnected();
            wifiIcon.classList.toggle('connected', isConnected);
            wifiIcon.style.opacity = isConnected ? '0.95' : '0.35';
        } catch (error) {
            wifiIcon.classList.remove('connected');
            wifiIcon.style.opacity = '0.35';
        }
    } else {
        wifiIcon.classList.remove('connected');
        wifiIcon.style.opacity = '0.35';
    }
}

function checkBluetoothStatus() {
    const bluetoothIcon = document.getElementById('bluetoothIcon');
    if (!bluetoothIcon) return;
    bluetoothIcon.style.display = 'block';

    if (typeof Android !== 'undefined' && Android.isBluetoothConnected) {
        try {
            const isConnected = Android.isBluetoothConnected();
            bluetoothIcon.classList.toggle('connected', isConnected);
            bluetoothIcon.style.opacity = isConnected ? '0.95' : '0.35';
        } catch (error) {
            bluetoothIcon.classList.remove('connected');
            bluetoothIcon.style.opacity = '0.35';
        }
    } else {
        bluetoothIcon.classList.remove('connected');
        bluetoothIcon.style.opacity = '0.35';
    }
}

function checkLocationStatus() {
    const locationIcon = document.getElementById('locationIcon');
    if (!locationIcon) return;
    locationIcon.style.display = 'block';

    if (typeof Android !== 'undefined' && Android.isLocationEnabled) {
        try {
            const isEnabled = Android.isLocationEnabled();
            locationIcon.classList.toggle('connected', isEnabled);
            locationIcon.style.opacity = isEnabled ? '0.95' : '0.35';
        } catch (error) {
            locationIcon.classList.remove('connected');
            locationIcon.style.opacity = '0.35';
        }
    } else {
        locationIcon.classList.remove('connected');
        locationIcon.style.opacity = '0.35';
    }
}

function updateNetworkAndBluetoothStatus() {
    checkWifiStatus();
    checkBluetoothStatus();
    checkLocationStatus();
}

function initHorizontalScroll() {
    const container = document.querySelector('.horizontal-scroll-container');
    if (!container) return;

    let isDragging = false;
    let startX = 0;
    let scrollLeft = 0;

    const debounceFn = typeof debounce !== 'undefined' ? debounce : function(fn) { return fn; };

    container.addEventListener('mousedown', debounceFn(function(e) {
        isDragging = true;
        startX = e.pageX - container.offsetLeft;
        scrollLeft = container.scrollLeft;
    }));

    container.addEventListener('mouseleave', debounceFn(function() {
        isDragging = false;
    }));

    container.addEventListener('mouseup', debounceFn(function() {
        isDragging = false;
    }));

    container.addEventListener('mousemove', debounceFn(function(e) {
        if (!isDragging) return;
        e.preventDefault();
        const x = e.pageX - container.offsetLeft;
        const walk = (x - startX) * 1.5;
        container.scrollLeft = scrollLeft - walk;
    }));
}

function updateMusicProgress() {
    if (typeof Android !== 'undefined' && Android.getMusicProgress) {
        try {
            const progress = Android.getMusicProgress();
            const progressElement = document.querySelector('.music-progress');
            if (progressElement && progress !== null) {
                progressElement.style.width = progress + '%';
            }
        } catch (e) {
            console.error('更新音乐进度失败:', e);
        }
    }
}

function initAcTemperature() {
    if (typeof Android !== 'undefined' && Android.getDriverTemp) {
        try {
            const temp = Android.getDriverTemp();
            updateAcTemperature(temp);
        } catch (e) {
            console.log('获取空调温度失败，使用默认值');
        }
    }
}

function updateAcTemperature(temp) {
    const airTextElements = document.querySelectorAll('.air-text');
    airTextElements.forEach(element => {
        element.textContent = temp + "°";
    });
}

function updateAcState(acOn) {
    const acControl = document.getElementById('acControl');
    if (!acControl) return;

    if (acOn) {
        acControl.style.backgroundImage = "url('images/nav_air.png')";
        acControl.classList.add('rotate-animation');
    } else {
        acControl.style.backgroundImage = "url('images/nav_air_close.png')";
        acControl.classList.remove('rotate-animation');
    }
}

function updateWindLevel(level) {
    const windLevelElement = document.querySelector('.wind-level');
    if (windLevelElement && level >= 0 && level <= 7) {
        windLevelElement.style.backgroundImage = `url('images/wind_level_0${level}.png')`;
    }
}

function initMusicControls() {
    const playBtn = document.getElementById('musicPlayBtn');
    const prevBtn = document.getElementById('musicPrevBtn');
    const nextBtn = document.getElementById('musicNextBtn');

    if (playBtn) {
        playBtn.addEventListener('click', function() {
            if (typeof Android !== 'undefined' && Android.toggleMusicPlayback) {
                Android.toggleMusicPlayback();
            }
        });
    }

    if (prevBtn) {
        prevBtn.addEventListener('click', function() {
            if (typeof Android !== 'undefined' && Android.sendPrevTrack) {
                Android.sendPrevTrack();
            }
        });
    }

    if (nextBtn) {
        nextBtn.addEventListener('click', function() {
            if (typeof Android !== 'undefined' && Android.sendNextTrack) {
                Android.sendNextTrack();
            }
        });
    }
}

function addTimeDisplayClickEvent() {
    const timeDisplay = document.getElementById('timeDisplay');
    const layoutLeft = document.querySelector('.layout-left');
    
    if (timeDisplay) {
        timeDisplay.addEventListener('click', function() {
            if (window.SettingsSync) { SettingsSync.toggleWallpaperCarousel(); }

            if (layoutLeft) {
                const statusBarHeight = 52;
                const bottomDockHeight = 80;
                const cardAreaHeight = 150;
                const timeWidth = layoutLeft.offsetWidth;
                const timeHeight = layoutLeft.offsetHeight;
                
                const maxX = window.innerWidth - timeWidth - 20;
                const maxY = window.innerHeight - statusBarHeight - bottomDockHeight - cardAreaHeight - timeHeight - 20;
                
                const randomX = Math.max(10, Math.random() * maxX);
                const randomY = Math.max(statusBarHeight + 10, Math.random() * maxY);
                
                layoutLeft.style.transition = 'all 0.5s cubic-bezier(0.34, 1.56, 0.64, 1)';
                layoutLeft.style.left = randomX + 'px';
                layoutLeft.style.top = randomY + 'px';
                layoutLeft.style.position = 'absolute';
                layoutLeft.style.right = 'auto';
            }
        });
    }
}

function initNavigationButtons() {
    const goHomeBtn = document.getElementById('goHomeBtn');
    const goCompanyBtn = document.getElementById('goCompanyBtn');

    if (goHomeBtn) {
        goHomeBtn.addEventListener('click', function() {
            if (typeof Android !== 'undefined' && Android.navigateToHome) {
                Android.navigateToHome();
            } else {
                window.location.href = 'amapuri://route/plan?dname=家&dev=0&t=0';
            }
        });
    }

    if (goCompanyBtn) {
        goCompanyBtn.addEventListener('click', function() {
            if (typeof Android !== 'undefined' && Android.navigateToCompany) {
                Android.navigateToCompany();
            } else {
                window.location.href = 'amapuri://route/plan?dname=公司&dev=0&t=0';
            }
        });
    }
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
safeInit('initAppsModal', function() { if (window.AppListManager) AppListManager.initAppsModal(); });
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
safeInit('initEffectLevel', function() { if (window.SettingsSync) SettingsSync.initEffectLevel(); });
safeInit('initThemeMode', function() { if (window.SettingsSync) SettingsSync.initThemeMode(); });
safeInit('initThemeToggleIcon', function() { if (window.SettingsSync) SettingsSync.initThemeToggleIcon(); });

/**
 * 后端回调：注册时间更新监听
 * 时间更新由 datetime.js 独立处理（每秒 HH:mm:ss），此处保留空实现避免后端调用报错
 */
function registerTimeUpdateListener() {
    console.log('[index.js] registerTimeUpdateListener 已调用，时间更新由 datetime.js 处理');
}

/**
 * 后端回调：更新时间显示
 * datetime.js 已每秒自动更新，此处接收后端推送的时间（可选）
 */
window.updateTimeDisplay = function(time, date, lunarDate) {
    // datetime.js 已独立处理时间显示，此处保留避免后端调用报错
};

/**
 * 后端回调：初始化空调状态
 * 接收后端推送的空调信息JSON，目前无前端处理逻辑，保留避免后端调用报错
 */
window.initializeAcStatus = function(acInfo) {
    console.log('[index.js] initializeAcStatus 已调用，空调信息：' + JSON.stringify(acInfo));
};
