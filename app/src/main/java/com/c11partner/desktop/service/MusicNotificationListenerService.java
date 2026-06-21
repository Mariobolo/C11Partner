package com.c11partner.desktop.service;

import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.c11partner.desktop.utils.MusicInfoExtractor;

import java.util.HashMap;
import java.util.Map;

/**
 * 音乐通知监听服务
 * 用于监听系统音乐播放器的通知，获取当前播放的音乐信息
 */
public class MusicNotificationListenerService extends NotificationListenerService {

    private static final String TAG = "MusicNotification";
    
    // 音乐播放器包名列表（常见的）
    private static final String[] MUSIC_PACKAGES = {
        "com.netease.cloudmusic",     // 网易云音乐
        "com.tencent.qqmusic",        // QQ音乐
        "com.kugou.android",          // 酷狗音乐
        "com.kuwo.player",            // 酷我音乐
        "com.spotify.music",          // Spotify
        "com.google.android.music",   // Google Play Music
        "com.apple.android.music",    // Apple Music
        "com.miui.player",            // 小米音乐
        "com.huawei.music",           // 华为音乐
        "com.samsung.android.music",  // 三星音乐
        "com.meizu.media.music",      // 魅族音乐
        "com.coolmeesong",            // 其他音乐播放器
    };
    
    // 当前播放的音乐信息
    private static String currentTitle = "";
    private static String currentArtist = "";
    private static String currentAlbum = "";
    private static boolean isPlaying = false;
    private static String currentPackage = "";
    
    // 音乐信息更新监听器
    public interface MusicInfoListener {
        void onMusicInfoChanged(String title, String artist, String album, boolean isPlaying, String packageName);
    }
    
    private static MusicInfoListener musicInfoListener;
    
    public static void setMusicInfoListener(MusicInfoListener listener) {
        musicInfoListener = listener;
    }
    
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        
        try {
            String packageName = sbn.getPackageName();
            
            // 检查是否是音乐播放器的通知
            if (!isMusicPackage(packageName)) {
                return;
            }
            
            Notification notification = sbn.getNotification();
            if (notification == null) {
                return;
            }
            
            // 提取音乐信息
            extractMusicInfo(notification, packageName);
            
        } catch (Exception e) {
            Log.e(TAG, "处理通知失败", e);
        }
    }
    
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        
        try {
            String packageName = sbn.getPackageName();
            
            // 如果是当前播放的音乐应用的通知被移除了
            if (packageName.equals(currentPackage)) {
                // 检查是否还有其他音乐通知
                boolean hasOtherMusicNotification = false;
                StatusBarNotification[] activeNotifications = getActiveNotifications();
                if (activeNotifications != null) {
                    for (StatusBarNotification n : activeNotifications) {
                        if (isMusicPackage(n.getPackageName())) {
                            hasOtherMusicNotification = true;
                            break;
                        }
                    }
                }
                
                if (!hasOtherMusicNotification) {
                    // 没有音乐通知了，清空状态
                    currentTitle = "";
                    currentArtist = "";
                    currentAlbum = "";
                    isPlaying = false;
                    currentPackage = "";
                    notifyListener();
                }
            }
            
        } catch (Exception e) {
            Log.e(TAG, "处理通知移除失败", e);
        }
    }
    
    /**
     * 检查是否是音乐播放器包名
     */
    private boolean isMusicPackage(String packageName) {
        if (packageName == null) {
            return false;
        }
        
        for (String pkg : MUSIC_PACKAGES) {
            if (packageName.equals(pkg)) {
                return true;
            }
        }
        
        // 也可以通过通知的 category 来判断
        return false;
    }
    
    /**
     * 从通知中提取音乐信息
     */
    private void extractMusicInfo(Notification notification, String packageName) {
        try {
            // 从通知的 extras 中提取信息
            if (notification.extras != null) {
                // 标题（歌名）
                CharSequence title = notification.extras.getCharSequence(Notification.EXTRA_TITLE);
                if (title != null) {
                    currentTitle = title.toString();
                }
                
                // 内容（歌手/专辑）
                CharSequence text = notification.extras.getCharSequence(Notification.EXTRA_TEXT);
                if (text != null) {
                    String textStr = text.toString();
                    // 通常格式是 "歌手 - 专辑" 或者直接是歌手
                    if (textStr.contains(" - ")) {
                        String[] parts = textStr.split(" - ", 2);
                        currentArtist = parts[0].trim();
                        currentAlbum = parts.length > 1 ? parts[1].trim() : "";
                    } else {
                        currentArtist = textStr;
                    }
                }
                
                // 子内容（通常是专辑名）
                CharSequence subText = notification.extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
                if (subText != null && currentAlbum.isEmpty()) {
                    currentAlbum = subText.toString();
                }
                
                // 判断是否正在播放
                // 可以通过通知的 actions 或者其他方式判断
                isPlaying = true; // 暂时默认有通知就是在播放
                
                currentPackage = packageName;
                
                // 通知监听器
                notifyListener();
                
                Log.d(TAG, "音乐信息更新: " + currentTitle + " - " + currentArtist + " (播放中: " + isPlaying + ")");
            }
        } catch (Exception e) {
            Log.e(TAG, "提取音乐信息失败", e);
        }
    }
    
    /**
     * 通知监听器
     */
    private void notifyListener() {
        if (musicInfoListener != null) {
            musicInfoListener.onMusicInfoChanged(
                currentTitle, currentArtist, currentAlbum, isPlaying, currentPackage
            );
        }
    }
    
    /**
     * 获取当前播放的音乐信息
     */
    public static Map<String, Object> getCurrentMusicInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("title", currentTitle);
        info.put("artist", currentArtist);
        info.put("album", currentAlbum);
        info.put("isPlaying", isPlaying);
        info.put("packageName", currentPackage);
        return info;
    }
    
    /**
     * 获取当前歌名
     */
    public static String getCurrentTitle() {
        return currentTitle;
    }
    
    /**
     * 获取当前歌手
     */
    public static String getCurrentArtist() {
        return currentArtist;
    }
    
    /**
     * 是否正在播放
     */
    public static boolean isPlaying() {
        return isPlaying;
    }
}
