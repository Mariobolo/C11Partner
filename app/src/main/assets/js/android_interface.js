/**
 * Android 接口模拟层
 * 在非Android环境（浏览器）中提供完整mock，支持前端开发和调试
 * 
 * 覆盖所有JS调用的Android.*方法，与WebViewBridge.java保持同步
 */

(function() {
    'use strict';

    // 已在Android环境中，跳过mock
    if (typeof Android !== 'undefined') return;

    // ==================== 模拟状态 ====================
    var mockState = {
        // 音乐状态
        musicPlaying: false,
        musicName: '测试歌曲 - 模拟歌手',
        musicPosition: 0,
        musicDuration: 240000,
        musicInterval: null,

        // 空调状态
        acEnabled: false,
        acTemp: 22,
        acWindLevel: 3,

        // 车辆状态
        gear: 4, // P档
        locked: true,
        speed: 0,

        // 壁纸
        wallpaperType: 0,

        // 快捷应用
        quickApps: [
            { name: '高德地图', packageName: 'com.autonavi.minimap', icon: 'images/ic_launcher.png' },
            { name: '音乐', packageName: 'com.android.music', icon: 'images/ic_launcher.png' },
            { name: '微信', packageName: 'com.tencent.mm', icon: 'images/ic_launcher.png' }
        ],

        // 应用列表缓存
        appListCache: null
    };

    // ==================== 生成模拟应用列表 ====================
    function generateMockAppList() {
        if (mockState.appListCache) return mockState.appListCache;
        mockState.appListCache = {
            'A': [
                { name: '高德地图', packageName: 'com.autonavi.minimap', icon: 'images/ic_launcher.png', isSystemApp: false },
                { name: '爱奇艺', packageName: 'com.qiyi.video', icon: 'images/ic_launcher.png', isSystemApp: false }
            ],
            'B': [
                { name: '百度地图', packageName: 'com.baidu.BaiduMap', icon: 'images/ic_launcher.png', isSystemApp: false },
                { name: '哔哩哔哩', packageName: 'tv.danmaku.bili', icon: 'images/ic_launcher.png', isSystemApp: false }
            ],
            'D': [
                { name: '抖音', packageName: 'com.ss.android.ugc.aweme', icon: 'images/ic_launcher.png', isSystemApp: false }
            ],
            'W': [
                { name: '微信', packageName: 'com.tencent.mm', icon: 'images/ic_launcher.png', isSystemApp: false }
            ],
            'Z': [
                { name: '支付宝', packageName: 'com.eg.android.AlipayGphone', icon: 'images/ic_launcher.png', isSystemApp: false }
            ]
        };
        return mockState.appListCache;
    }

    // ==================== 生成模拟车辆状态 ====================
    function generateMockCarState() {
        return {
            gear: mockState.gear,
            gearText: mockState.gear === 1 ? 'R档' : mockState.gear === 2 ? 'N档' : mockState.gear === 3 ? 'D档' : 'P档',
            speed: mockState.speed,
            isLocked: mockState.locked,
            isAnyDoorOpen: false,
            frontLeftDoor: 0, frontRightDoor: 0,
            rearLeftDoor: 0, rearRightDoor: 0,
            trunkDoor: 0, hoodDoor: 0,
            leftTurnLight: 0, rightTurnLight: 0,
            frontLeftTirePressure: 250, frontRightTirePressure: 248,
            rearLeftTirePressure: 252, rearRightTirePressure: 249,
            frontLeftTireTemp: 32, frontRightTireTemp: 31,
            rearLeftTireTemp: 30, rearRightTireTemp: 31
        };
    }

    // ==================== 模拟Android对象 ====================
    window.Android = {

        // ---------- 音乐控制 ----------
        playPauseMusic: function() { mockState.musicPlaying = !mockState.musicPlaying; },
        nextMusic: function() { mockState.musicName = '下一首歌曲 - 模拟歌手'; },
        prevMusic: function() { mockState.musicName = '上一首歌曲 - 模拟歌手'; },
        playPause: function() { mockState.musicPlaying = !mockState.musicPlaying; },
        playNext: function() { mockState.musicName = '下一首歌曲 - 模拟歌手'; },
        playPrevious: function() { mockState.musicName = '上一首歌曲 - 模拟歌手'; },
        playMusic: function() { mockState.musicPlaying = true; },
        pauseMusic: function() { mockState.musicPlaying = false; },
        seekTo: function(ms) { mockState.musicPosition = ms; },
        isMusicPlaying: function() { return mockState.musicPlaying; },
        getCurrentMusicName: function() { return mockState.musicName; },
        getCurrentMusicArtist: function() { return '模拟歌手'; },
        getMusicProgressInfo: function() {
            return JSON.stringify({
                isPlaying: mockState.musicPlaying,
                currentPosition: mockState.musicPosition,
                duration: mockState.musicDuration
            });
        },
        getSystemMusicInfo: function() {
            return JSON.stringify({
                hasData: mockState.musicPlaying,
                title: mockState.musicName,
                artist: '模拟歌手',
                isPlaying: mockState.musicPlaying
            });
        },
        hasNotificationAccess: function() { return true; },
        isNotificationListenerEnabled: function() { return true; },
        openNotificationListenerSettings: function() { console.log('[Mock] openNotificationListenerSettings'); },
        getMusicVolume: function() { return 50; },
        setMusicVolume: function(v) { console.log('[Mock] setMusicVolume:', v); },
        volumeUp: function() { console.log('[Mock] volumeUp'); },
        volumeDown: function() { console.log('[Mock] volumeDown'); },
        startMusicVisualizer: function() { console.log('[Mock] startMusicVisualizer'); },
        stopMusicVisualizer: function() { console.log('[Mock] stopMusicVisualizer'); },

        // ---------- 空调控制 ----------
        toggleAC: function() { mockState.acEnabled = !mockState.acEnabled; return true; },
        toggleAirConditioning: function() { mockState.acEnabled = !mockState.acEnabled; return true; },
        toggleDefrost: function() { console.log('[Mock] toggleDefrost'); return true; },
        isAcEnabled: function() { return mockState.acEnabled; },
        setAcEnabled: function(on) { mockState.acEnabled = on; return true; },
        getAcInfo: function() {
            return JSON.stringify({ enabled: mockState.acEnabled, temp: mockState.acTemp, windLevel: mockState.acWindLevel });
        },
        increaseTemperature: function() { mockState.acTemp++; return true; },
        decreaseTemperature: function() { mockState.acTemp--; return true; },
        increaseWindSpeed: function() { mockState.acWindLevel++; return true; },
        decreaseWindSpeed: function() { mockState.acWindLevel--; return true; },
        adjustTemperature: function() { return true; },
        adjustWindLevel: function() { return true; },
        getDriverTemp: function() { return mockState.acTemp; },
        setDriverTemp: function(t) { mockState.acTemp = t; return true; },
        getPassengerTemp: function() { return mockState.acTemp; },
        setPassengerTemp: function(t) { return true; },
        getWindLevel: function() { return mockState.acWindLevel; },
        setWindLevel: function(l) { mockState.acWindLevel = l; return true; },
        setMaxCooling: function(on) { console.log('[Mock] setMaxCooling:', on); mockState.maxCooling = on; return true; },
        isMaxCoolingOn: function() { return !!mockState.maxCooling; },

        // ---------- 车控功能 ----------
        getCarState: function() { return JSON.stringify(generateMockCarState()); },
        setLowBeamLight: function(on) { console.log('[Mock] setLowBeamLight:', on); mockState.lowBeamLight = on; return true; },
        isLowBeamLightOn: function() { return !!mockState.lowBeamLight; },
        setRearFogLight: function(on) { console.log('[Mock] setRearFogLight:', on); mockState.rearFogLight = on; return true; },
        isRearFogLightOn: function() { return !!mockState.rearFogLight; },
        setPositionLight: function(on) { console.log('[Mock] setPositionLight:', on); mockState.positionLight = on; return true; },
        isPositionLightOn: function() { return !!mockState.positionLight; },
        setPedestrianAlert: function(on) { console.log('[Mock] setPedestrianAlert:', on); mockState.pedestrianAlert = on; return true; },
        isPedestrianAlertOn: function() { return !!mockState.pedestrianAlert; },
        setNightMode: function(on) { console.log('[Mock] setNightMode:', on); mockState.nightMode = on; return true; },
        isNightModeOn: function() { return !!mockState.nightMode; },
        setDriveMode: function(m) { console.log('[Mock] setDriveMode:', m); return true; },
        setGuardMode: function(on) { console.log('[Mock] setGuardMode:', on); return true; },
        setRestMode: function(on) { console.log('[Mock] setRestMode:', on); return true; },
        setCampingMode: function(on) { console.log('[Mock] setCampingMode:', on); return true; },
        setPowerSaveMode: function(on) { console.log('[Mock] setPowerSaveMode:', on); return true; },
        setSentinelMode: function(on) { console.log('[Mock] setSentinelMode:', on); return true; },
        startCamera360: function() { console.log('[Mock] startCamera360'); return true; },
        navigateToHome: function() { console.log('[Mock] navigateToHome'); },
        navigateToCompany: function() { console.log('[Mock] navigateToCompany'); },

        // ---------- 网络/蓝牙 ----------
        isWifiConnected: function() { return true; },
        isBluetoothConnected: function() { return true; },
        setWifiEnabled: function(on) { console.log('[Mock] setWifiEnabled:', on); mockState.wifiEnabled = on; return true; },
        isWifiEnabled: function() { return mockState.wifiEnabled !== false; },
        setBluetoothEnabled: function(on) { console.log('[Mock] setBluetoothEnabled:', on); mockState.bluetoothEnabled = on; return true; },
        isBluetoothEnabled: function() { return mockState.bluetoothEnabled !== false; },

        // ---------- 快捷开关 ----------
        isVideoWhileDrivingEnabled: function() { return false; },
        setVideoWhileDriving: function(on) { console.log('[Mock] setVideoWhileDriving:', on); return true; },
        isCameraOverspeedLimitEnabled: function() { return false; },
        setCameraOverspeedLimit: function(on) { console.log('[Mock] setCameraOverspeedLimit:', on); return true; },
        isAmbientLightEnabled: function() { return false; },
        setAmbientLightEnabled: function(on) { console.log('[Mock] setAmbientLightEnabled:', on); return true; },
        isSpeechEnabled: function() { return true; },
        setSpeechEnabled: function(on) { console.log('[Mock] setSpeechEnabled:', on); return true; },
        isSecondaryScreenEnabled: function() { return false; },
        setSecondaryScreenEnabled: function(on) { console.log('[Mock] setSecondaryScreenEnabled:', on); return true; },
        isPresentationShowing: function() { return false; },
        hidePresentation: function() { console.log('[Mock] hidePresentation'); },
        showCarStatusPresentation: function() { console.log('[Mock] showCarStatusPresentation'); return false; },

        // ---------- 应用管理 ----------
        launchApp: function(pkg) { console.log('[Mock] launchApp:', pkg); },
        getAppList: function() { return JSON.stringify(generateMockAppList()); },
        getAppListAsync: function(cbId) {
            setTimeout(function() {
                if (window.handleGetAppListCallback) {
                    window.handleGetAppListCallback(cbId, JSON.stringify(generateMockAppList()));
                }
            }, 100);
        },
        getQuickAppList: function() { return JSON.stringify(mockState.quickApps); },
        addQuickApp: function(name, pkg, icon) {
            mockState.quickApps.push({ name: name, packageName: pkg, icon: icon });
        },
        removeQuickApp: function(pkg) {
            mockState.quickApps = mockState.quickApps.filter(function(a) { return a.packageName !== pkg; });
        },
        isQuickApp: function(pkg) {
            return mockState.quickApps.some(function(a) { return a.packageName === pkg; });
        },
        getConfigApp: function(btnId) { return ''; },
        saveConfigApp: function(btnId, name, pkg, icon) { console.log('[Mock] saveConfigApp:', btnId, name); },
        restartApp: function() { console.log('[Mock] restartApp'); location.reload(); },
        setDefaultDesktop: function() { console.log('[Mock] setDefaultDesktop'); },
        setDefaultDesktopViaAdb: function() { console.log('[Mock] setDefaultDesktopViaAdb'); },

        // ---------- 壁纸 ----------
        getWallpaperSettings: function() {
            return JSON.stringify({ wallpaper_carousel: false, random_mode: false, specified_mode: false, switch_interval: 15000 });
        },
        getWallpaperSettingsAsync: function(cbId) {
            var result = JSON.stringify({ wallpaper_carousel: false, random_mode: false, specified_mode: false, switch_interval: 15000 });
            setTimeout(function() { if (window.handleGetWallpaperSettingsCallback) window.handleGetWallpaperSettingsCallback(cbId, result); }, 50);
        },
        saveWallpaperCarouselSetting: function(on) { return true; },
        saveRandomModeSetting: function(on) { return true; },
        saveSpecifiedModeSetting: function(on) { return true; },
        saveWallpaperSwitchInterval: function(ms) { return true; },
        getRandomWallpaper: function() { return ''; },
        getRandomWallpaperAsync: function(cbId) { if (window.handleGetRandomWallpaperCallback) window.handleGetRandomWallpaperCallback(cbId, ''); },
        getRandomWallpaperBase64: function() { return ''; },
        getRandomWallpaperBase64Async: function(cbId) { if (window.handleGetRandomWallpaperBase64Callback) window.handleGetRandomWallpaperBase64Callback(cbId, ''); },
        deleteCurrentWallpaper: function() { return true; },
        pauseWallpaperCarousel: function() { console.log('[Mock] pauseWallpaperCarousel'); },
        resumeWallpaperCarousel: function() { console.log('[Mock] resumeWallpaperCarousel'); },
        sendWallpaperSettingsChangedBroadcast: function() { console.log('[Mock] sendWallpaperSettingsChangedBroadcast'); },
        getEnabledCategories: function() { return '[]'; },
        getEnabledCategoriesAsync: function(cbId) {
            setTimeout(function() { if (window.handleGetEnabledCategoriesCallback) window.handleGetEnabledCategoriesCallback(cbId, '[]'); }, 50);
        },
        updateCategoryEnabled: function(catId, enabled) { return true; },
        updateCategoryEnabledAsync: function(catId, enabled, cbId) {
            setTimeout(function() { if (window.handleUpdateCategoryEnabledCallback) window.handleUpdateCategoryEnabledCallback(cbId, 'true'); }, 50);
        },

        // ---------- 组件配置 ----------
        getAllComponentConfigs: function() {
            return JSON.stringify({ music_component: true, map_component: true, app_component: true, tire_pressure_component: true, weather_component: true });
        },
        saveComponentConfig: function(name, enabled) { return true; },
        isComponentEnabled: function(name) { return true; },

        // ---------- 系统设置 ----------
        saveSystemLauncherSetting: function(on) { return true; },
        saveBootGreetingSetting: function(on) { return true; },
        showToast: function(msg) { console.log('[Toast]', msg); },
        getLunarCalendar: function() { return '农历六月初七'; },

        // ---------- ADB ----------
        triggerUsbDebugAuthorization: function() { console.log('[Mock] triggerUsbDebugAuthorization'); },
        triggerWirelessAdbAuthorization: function() { console.log('[Mock] triggerWirelessAdbAuthorization'); },
        executeAdbPermissionGrant: function() { console.log('[Mock] executeAdbPermissionGrant'); },
        enableAdbDebugging: function() { console.log('[Mock] enableAdbDebugging'); },
        executeAdbCommand: function(cmd) { console.log('[Mock] executeAdbCommand:', cmd); },
        openRecentTasks: function() { console.log('[Mock] openRecentTasks'); },
        openRecents: function() { console.log('[Mock] openRecents'); },
        openRecentsViaAdb: function() { console.log('[Mock] openRecentsViaAdb'); },

        // ---------- 自动化 ----------
        getAutomationSettings: function() { return '{}'; },
        setAutomationSettings: function(json) { return true; },
        setAutomationScenarioEnabled: function(id, on) { return true; },

        // ---------- 座椅/舒适 ----------
        setDriverSeatHeating: function(level) { return true; },
        setDriverSeatVentilation: function(level) { return true; },
        setPassengerSeatHeating: function(level) { return true; },
        setPassengerSeatVentilation: function(level) { return true; },
        setSteeringWheelHeating: function(on) { return true; },
        setMirrorHeating: function(on) { return true; },
        foldMirrors: function() { return true; },
        unfoldMirrors: function() { return true; },

        // ---------- 语音 ----------
        sendVoiceCommand: function(cmd) { console.log('[Mock] sendVoiceCommand:', cmd); return true; },

        // ---------- 屏幕 ----------
        setScreenBrightness: function(b) { return true; },
        getScreenBrightness: function() { return 80; },
        setAutoBrightness: function(on) { return true; },
        isAutoBrightnessEnabled: function() { return true; },
        setScreenTimeout: function(s) { return true; },
        getScreenTimeout: function() { return 60; },
        isScreenOn: function() { return true; }
    };

    // ==================== 模拟音乐播放进度 ====================
    mockState.musicInterval = setInterval(function() {
        if (mockState.musicPlaying) {
            mockState.musicPosition += 1000;
            if (mockState.musicPosition >= mockState.musicDuration) {
                mockState.musicPosition = 0;
            }
        }
    }, 1000);

    console.log('[android_interface.js] Android mock 已加载，覆盖 ' + Object.keys(window.Android).length + ' 个方法');
})();
