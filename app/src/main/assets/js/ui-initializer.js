const UiInitializer = (function() {
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
                    const appSafeIcon = typeof normalizeAppIcon === 'function' ? normalizeAppIcon(app.icon) : app.icon;
                    const appSafeName = typeof escapeHtml === 'function' ? escapeHtml(app.name) : app.name;
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
                        if (typeof showRemoveFromQuickAppsDialog === 'function') {
                            showRemoveFromQuickAppsDialog(app);
                        }
                    });
                    quickAppsContainer.appendChild(appItem);
                });

                quickAppsContainer.appendChild(createShowAllAppsButton());
            } catch (e) {
                console.error('加载快速启动应用列表失败:', e);
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
                const appSafeName = typeof escapeHtml === 'function' ? escapeHtml(app.name) : app.name;
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

            const debounceFn = typeof AppBootstrap !== 'undefined' ? AppBootstrap.debounce : function(fn) { return fn; };

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
                if (typeof showSettingsModal === 'function') {
                    showSettingsModal();
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

        if (typeof Android !== 'undefined' && Android.isWifiConnected) {
            try {
                const isConnected = Android.isWifiConnected();
                wifiIcon.style.display = isConnected ? 'block' : 'none';
                wifiIcon.classList.toggle('connected', isConnected);
            } catch (error) {
                wifiIcon.style.display = 'none';
                wifiIcon.classList.remove('connected');
            }
        } else {
            wifiIcon.style.display = 'none';
            wifiIcon.classList.remove('connected');
        }
    }

    function checkBluetoothStatus() {
        const bluetoothIcon = document.getElementById('bluetoothIcon');
        if (!bluetoothIcon) return;

        if (typeof Android !== 'undefined' && Android.isBluetoothConnected) {
            try {
                const isConnected = Android.isBluetoothConnected();
                bluetoothIcon.style.display = isConnected ? 'block' : 'none';
                bluetoothIcon.classList.toggle('connected', isConnected);
            } catch (error) {
                bluetoothIcon.style.display = 'none';
                bluetoothIcon.classList.remove('connected');
            }
        } else {
            bluetoothIcon.style.display = 'none';
            bluetoothIcon.classList.remove('connected');
        }
    }

    function checkLocationStatus() {
        const locationIcon = document.getElementById('locationIcon');
        if (!locationIcon) return;

        if (typeof Android !== 'undefined' && Android.isLocationEnabled) {
            try {
                const isEnabled = Android.isLocationEnabled();
                locationIcon.style.display = isEnabled ? 'block' : 'none';
                locationIcon.classList.toggle('connected', isEnabled);
            } catch (error) {
                locationIcon.style.display = 'none';
                locationIcon.classList.remove('connected');
            }
        } else {
            locationIcon.style.display = 'none';
            locationIcon.classList.remove('connected');
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

        const debounceFn = typeof AppBootstrap !== 'undefined' ? AppBootstrap.debounce : function(fn) { return fn; };

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

    function registerTimeUpdateListener() {
        function updateTime() {
            const now = new Date();
            const hours = String(now.getHours()).padStart(2, '0');
            const minutes = String(now.getMinutes()).padStart(2, '0');
            const timeElement = document.getElementById('timeDisplay');
            if (timeElement) {
                timeElement.textContent = `${hours}:${minutes}`;
            }
        }

        updateTime();
        setInterval(updateTime, 1000);
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
        const dateDisplay = document.getElementById('dateDisplay');
        const chineseDate = document.getElementById('chineseDate');
        const timeContainer = document.querySelector('.time-container');
        
        if (timeDisplay) {
            timeDisplay.addEventListener('click', function() {
                if (typeof toggleWallpaperCarousel === 'function') {
                    toggleWallpaperCarousel();
                }

                if (timeContainer) {
                    const statusBarHeight = 48;
                    const widgetAreaHeight = 160;
                    const containerRect = timeContainer.getBoundingClientRect();
                    const timeWidth = timeDisplay.offsetWidth;
                    const timeHeight = timeDisplay.offsetHeight;
                    
                    const maxX = window.innerWidth - timeWidth - 40;
                    const maxY = window.innerHeight - statusBarHeight - widgetAreaHeight - timeHeight - 40;
                    
                    const randomX = Math.max(20, Math.random() * maxX);
                    const randomY = Math.max(statusBarHeight + 20, Math.random() * maxY);
                    
                    timeContainer.style.transition = 'all 0.5s cubic-bezier(0.34, 1.56, 0.64, 1)';
                    timeContainer.style.left = randomX + 'px';
                    timeContainer.style.top = randomY + 'px';
                    timeContainer.style.position = 'absolute';
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

    return {
        loadQuickApps,
        createShowAllAppsButton,
        loadQuickSwitches,
        updateACControlStatus,
        checkWifiStatus,
        checkBluetoothStatus,
        checkLocationStatus,
        updateNetworkAndBluetoothStatus,
        initHorizontalScroll,
        registerTimeUpdateListener,
        updateMusicProgress,
        initAcTemperature,
        updateAcTemperature,
        updateAcState,
        updateWindLevel,
        initMusicControls,
        addTimeDisplayClickEvent,
        initNavigationButtons
    };
})();
window.UiInitializer = UiInitializer;
