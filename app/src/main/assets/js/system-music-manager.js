/**
 * @fileoverview 系统音乐管理器模块
 * 负责系统音乐播放控制、音乐信息显示和权限检查
 * @module system-music-manager
 * @version 1.0.0
 */

/**
 * 系统音乐管理器对象
 * 管理音乐播放控制、状态更新和UI显示
 */
const SystemMusicManager = {
    /** @type {number|null} 定时更新间隔ID */
    updateInterval: null,
    
    /** @type {number} 更新间隔毫秒数 */
    updateIntervalMs: 2000,

    /**
     * 初始化系统音乐管理器
     * 绑定控制按钮、启动定时更新、检查权限
     */
    init: function() {
        this.bindControls();
        this.startUpdate();
        this.checkPermission();
    },

    /**
     * 绑定音乐控制按钮事件
     * 绑定播放/暂停、上一首、下一首按钮的点击事件
     */
    bindControls: function() {
        const playPauseBtn = document.getElementById('play-pause-btn');
        const nextBtn = document.getElementById('next-btn');
        const prevBtn = document.getElementById('prev-btn');

        if (playPauseBtn) {
            playPauseBtn.addEventListener('click', function() {
                if (typeof Android !== 'undefined' && Android.playPauseMusic) {
                    Android.playPauseMusic();
                    // 点击后立即更新状态
                    setTimeout(() => SystemMusicManager.updateMusicInfo(), 500);
                }
            });
        }

        if (nextBtn) {
            nextBtn.addEventListener('click', function() {
                if (typeof Android !== 'undefined' && Android.nextMusic) {
                    Android.nextMusic();
                    setTimeout(() => SystemMusicManager.updateMusicInfo(), 500);
                }
            });
        }

        if (prevBtn) {
            prevBtn.addEventListener('click', function() {
                if (typeof Android !== 'undefined' && Android.prevMusic) {
                    Android.prevMusic();
                    setTimeout(() => SystemMusicManager.updateMusicInfo(), 500);
                }
            });
        }
    },

    /**
     * 启动音乐信息定时更新
     * 立即更新一次并设置定时更新
     */
    startUpdate: function() {
        this.updateMusicInfo();
        this.updateInterval = setInterval(() => {
            this.updateMusicInfo();
        }, this.updateIntervalMs);
    },

    /**
     * 停止音乐信息定时更新
     * 清除定时器并重置间隔ID
     */
    stopUpdate: function() {
        if (this.updateInterval) {
            clearInterval(this.updateInterval);
            this.updateInterval = null;
        }
    },

    /**
     * 更新音乐信息显示
     * 从Android接口获取音乐信息并更新UI（歌名、播放状态、黑胶动画）
     */
    updateMusicInfo: function() {
        try {
            if (typeof Android !== 'undefined' && Android.getSystemMusicInfo) {
                const musicInfoJson = Android.getSystemMusicInfo();
                const musicInfo = JSON.parse(musicInfoJson);

                // 更新歌名
                const songNameEl = document.getElementById('current-song-name');
                if (songNameEl) {
                    if (musicInfo.hasData && musicInfo.title) {
                        // 显示歌名 - 歌手
                        let displayText = musicInfo.title;
                        if (musicInfo.artist) {
                            displayText = musicInfo.title + ' - ' + musicInfo.artist;
                        }
                        songNameEl.textContent = displayText;
                        songNameEl.title = displayText; // 完整内容显示在tooltip
                    } else {
                        songNameEl.textContent = '此刻无声，佳音已备候君启...';
                        songNameEl.title = '';
                    }
                }

                // 更新播放/暂停按钮图标
                const playPauseBtn = document.getElementById('play-pause-btn');
                if (playPauseBtn) {
                    const img = playPauseBtn.querySelector('img');
                    if (img) {
                        if (musicInfo.isPlaying) {
                            img.src = 'images/nav_music_pause.png';
                        } else {
                            img.src = 'images/nav_music_play.png';
                        }
                    }
                }

                // 更新黑胶唱片旋转动画
                const vinylRecord = document.getElementById('vinylRecord');
                if (vinylRecord) {
                    if (musicInfo.isPlaying) {
                        vinylRecord.style.animationPlayState = 'running';
                    } else {
                        vinylRecord.style.animationPlayState = 'paused';
                    }
                }
            }
        } catch (e) {
            console.error('更新音乐信息失败:', e);
        }
    },

    /**
     * 检查通知监听权限
     * 如果没有权限则在UI上显示引导提示
     */
    checkPermission: function() {
        try {
            if (typeof Android !== 'undefined' && Android.isNotificationListenerEnabled) {
                const hasPermission = Android.isNotificationListenerEnabled();
                if (!hasPermission) {
                    // 没有权限时，在歌名位置显示提示
                    const songNameEl = document.getElementById('current-song-name');
                    if (songNameEl) {
                        songNameEl.textContent = '点击开启音乐通知权限';
                        songNameEl.style.cursor = 'pointer';
                        songNameEl.style.color = '#4fc3f7';
                        songNameEl.addEventListener('click', function() {
                            if (typeof Android !== 'undefined' && Android.openNotificationListenerSettings) {
                                Android.openNotificationListenerSettings();
                            }
                        });
                    }
                }
            }
        } catch (e) {
            console.error('检查通知权限失败:', e);
        }
    }
};

/**
 * 初始化音乐播放控制
 * 在Android环境下启动SystemMusicManager
 */
function initMusicControls() {
    // 初始化系统音乐管理器
    if (typeof Android !== 'undefined') {
        SystemMusicManager.init();
    }
}
