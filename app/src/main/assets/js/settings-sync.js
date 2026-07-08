const SettingsSync = (function() {
    let isLoadingSettings = false;

    function loadWallpaperSettings() {
        isLoadingSettings = true;
        if (typeof Android !== 'undefined' && Android.getWallpaperSettingsAsync) {
            const callbackId = AsyncCallbackManager.register(function (settingsJson) {
                try {
                    const settings = JSON.parse(settingsJson);
                    applyWallpaperSettings(settings);
                } catch (e) {
                    console.error('解析壁纸设置失败:', e);
                } finally {
                    isLoadingSettings = false;
                }
            });
            Android.getWallpaperSettingsAsync(callbackId);
        } else if (typeof Android !== 'undefined' && Android.getWallpaperSettings) {
            try {
                const settingsJson = Android.getWallpaperSettings();
                const settings = JSON.parse(settingsJson);
                applyWallpaperSettings(settings);
            } catch (e) {
                console.error('加载壁纸设置失败:', e);
            } finally {
                isLoadingSettings = false;
            }
        } else {
            isLoadingSettings = false;
        }
    }

    function applyWallpaperSettings(settings) {
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

        if (typeof loadEnabledCategories === 'function') {
            loadEnabledCategories();
        }

        if (switchIntervalInput) {
            switchIntervalInput.parentElement.style.display = settings.wallpaper_carousel ? 'flex' : 'none';
        }
    }

    function initEffectLevel() {
        let savedLevel = 'none';
        if (typeof Android !== 'undefined' && Android.getEffectLevel) {
            try {
                savedLevel = Android.getEffectLevel();
            } catch (e) {
                console.log('获取特效等级失败，使用默认值');
            }
        } else {
            savedLevel = localStorage.getItem('effectLevel') || 'none';
        }
        applyEffectLevel(savedLevel);

        const radios = document.querySelectorAll('input[name="effectLevel"]');
        radios.forEach(function(radio) {
            if (radio.value === savedLevel) radio.checked = true;
            radio.addEventListener('change', function() {
                if (this.checked) {
                    const level = this.value;
                    if (typeof Android !== 'undefined' && Android.setEffectLevel) {
                        Android.setEffectLevel(level);
                    } else {
                        localStorage.setItem('effectLevel', level);
                    }
                    applyEffectLevel(level);
                }
            });
        });
    }

    function applyEffectLevel(level) {
        const html = document.documentElement;
        html.classList.remove('effect-none', 'effect-low', 'effect-high');
        html.classList.add('effect-' + level);
    }

    function initThemeMode() {
        let savedMode = 'dark';
        if (typeof Android !== 'undefined' && Android.getThemeMode) {
            try {
                savedMode = Android.getThemeMode();
            } catch (e) {
                console.log('获取主题模式失败，使用默认值');
            }
        } else {
            savedMode = localStorage.getItem('themeMode') || 'dark';
        }
        applyThemeMode(savedMode);

        const radios = document.querySelectorAll('input[name="themeMode"]');
        radios.forEach(function(radio) {
            if (radio.value === savedMode) radio.checked = true;
            radio.addEventListener('change', function() {
                if (this.checked) {
                    const mode = this.value;
                    if (typeof Android !== 'undefined' && Android.setThemeMode) {
                        Android.setThemeMode(mode);
                    } else {
                        localStorage.setItem('themeMode', mode);
                    }
                    applyThemeMode(mode);
                }
            });
        });
    }

    function applyThemeMode(mode) {
        const body = document.body;
        if (mode === 'light') body.classList.add('theme-light');
        else body.classList.remove('theme-light');
        updateThemeToggleIcon(mode);
    }

    function updateThemeToggleIcon(mode) {
        const icon = document.getElementById('themeToggleIcon');
        if (!icon) return;
        if (mode === 'light') {
            icon.innerText = '☀️';
            icon.title = '切换到夜间模式';
        } else {
            icon.innerText = '🌙';
            icon.title = '切换到白天模式';
        }
    }

    function initThemeToggleIcon() {
        const icon = document.getElementById('themeToggleIcon');
        if (!icon) return;

        const savedMode = (typeof Android !== 'undefined' && Android.getThemeMode) ? Android.getThemeMode() : (localStorage.getItem('themeMode') || 'dark');
        updateThemeToggleIcon(savedMode);

        icon.addEventListener('click', function() {
            const currentMode = document.body.classList.contains('theme-light') ? 'light' : 'dark';
            const newMode = currentMode === 'dark' ? 'light' : 'dark';

            if (typeof Android !== 'undefined' && Android.setThemeMode) {
                Android.setThemeMode(newMode);
            } else {
                localStorage.setItem('themeMode', newMode);
            }
            applyThemeMode(newMode);

            const radios = document.querySelectorAll('input[name="themeMode"]');
            radios.forEach(function(radio) {
                if (radio.value === newMode) radio.checked = true;
            });
        });
    }

    function toggleWallpaperCarousel() {
        if (typeof Android !== 'undefined' && Android.toggleWallpaperCarousel) {
            Android.toggleWallpaperCarousel();
        }
    }

    function restoreDefaultWallpaper() {
        if (typeof Android !== 'undefined' && Android.restoreDefaultWallpaper) {
            Android.restoreDefaultWallpaper();
        }
    }

    return {
        isLoadingSettings: function() { return isLoadingSettings; },
        loadWallpaperSettings,
        initEffectLevel,
        applyEffectLevel,
        initThemeMode,
        applyThemeMode,
        initThemeToggleIcon,
        toggleWallpaperCarousel,
        restoreDefaultWallpaper
    };
})();
