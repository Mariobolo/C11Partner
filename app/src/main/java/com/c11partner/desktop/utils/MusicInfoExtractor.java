package com.c11partner.desktop.utils;

import android.content.Context;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 音乐信息提取工具类
 * 通过多种方式获取音乐播放信息
 */
public class MusicInfoExtractor {
    private static final String TAG = "MusicInfoExtractor";
    private Context context;

    public MusicInfoExtractor(Context context) {
        this.context = context;
    }

    /**
     * 通过系统API获取音乐信息
     */
    public MusicInfo getMusicInfoViaAPI() {
        MusicInfo info = new MusicInfo();
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                MediaSessionManager mediaSessionManager = (MediaSessionManager) context.getSystemService(Context.MEDIA_SESSION_SERVICE);
                if (mediaSessionManager != null) {
                    // 使用NotificationListenerService的组件名称来获取媒体会话
                    android.content.ComponentName notificationListener = new android.content.ComponentName(
                            context, 
                            com.c11partner.desktop.service.MusicNotificationListenerService.class
                    );
                    List<MediaController> controllers = mediaSessionManager.getActiveSessions(notificationListener);
                    if (controllers != null && !controllers.isEmpty()) {
                        for (MediaController controller : controllers) {
                            if (controller.getPackageName() != null) {
                                // 获取播放状态
                                PlaybackState playbackState = controller.getPlaybackState();
                                if (playbackState != null) {
                                    info.isPlaying = (playbackState.getState() == PlaybackState.STATE_PLAYING);
                                    info.currentPosition = playbackState.getPosition();
                                    // 获取更新时间，用于计算更准确的位置
                                    // 注意：getLastPositionUpdateTime()返回的是elapsedRealtime时间基准
                                    long updateTime = playbackState.getLastPositionUpdateTime();
                                    if (updateTime > 0 && info.isPlaying) {
                                        // 使用elapsedRealtime来计算时间差，而不是currentTimeMillis
                                        long elapsedTime = android.os.SystemClock.elapsedRealtime() - updateTime;
                                        info.currentPosition += (long)(elapsedTime * playbackState.getPlaybackSpeed());
                                    }
                                    Log.d(TAG, "API获取播放位置: " + info.currentPosition + "ms, updateTime: " + updateTime);
                                }
                                
                                // 获取媒体元数据
                                MediaMetadata metadata = controller.getMetadata();
                                if (metadata != null) {
                                    // 获取音乐名称
                                    CharSequence title = metadata.getText(MediaMetadata.METADATA_KEY_TITLE);
                                    if (title != null && !title.toString().isEmpty()) {
                                        info.musicName = title.toString();
                                    }
                                    
                                    // 获取音乐时长
                                    info.duration = metadata.getLong(MediaMetadata.METADATA_KEY_DURATION);
                                }
                                
                                Log.d(TAG, "通过API获取到音乐信息: " + info);
                                break; // 只处理第一个活动的会话
                            }
                        }
                    }
                } else {
                    // 如果获取到空的控制器列表，这可能表示设备不支持媒体会话查询
                    // 或者用户没有授予通知监听权限
                    Log.d(TAG, "未找到活动的媒体会话，可能是设备不支持或权限未授予");
                }
            } catch (SecurityException e) {
                Log.e(TAG, "权限不足，无法访问媒体会话信息: " + e.getMessage());
                // 设备可能不支持此功能
            } catch (Exception e) {
                Log.e(TAG, "通过系统API获取音乐信息失败: " + e.getMessage());
                // 可能是设备不支持媒体会话查询
            }
        }
        
        return info;
    }

    /**
     * 通过dumpsys命令获取音乐信息
     */
    public MusicInfo getMusicInfoViaDumpsys() {
        MusicInfo info = new MusicInfo();
        
        BufferedReader reader = null;
        Process process = null;
        try {
            process = new ProcessBuilder()
                    .command("sh", "-c", "dumpsys media_session")
                    .redirectErrorStream(true)
                    .start();

            reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 解析输出
            info = parseDumpsysOutput(output.toString());

        } catch (IOException e) {
            Log.e(TAG, "dumpsys执行失败: " + e.getMessage());
            // 可能是设备不支持dumpsys命令或权限不足
        } catch (Exception e) {
            Log.e(TAG, "dumpsys执行发生其他错误: " + e.getMessage());
            // 可能是设备不支持此功能
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "关闭reader失败", e);
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
        
        return info;
    }

    /**
     * 解析dumpsys输出
     */
    private MusicInfo parseDumpsysOutput(String output) {
        MusicInfo info = new MusicInfo();
        
        // 解析音乐名称
        Pattern titlePattern = Pattern.compile("metadata: size=\\d+, description=([^,]+)");
        Matcher titleMatcher = titlePattern.matcher(output);
        
        while (titleMatcher.find()) {
            if (output.contains("active=true") && titleMatcher.group(1) != null) {
                info.musicName = titleMatcher.group(1).trim();
                break;
            }
        }

        // 解析播放状态和位置信息
        // 查找包含state=PlaybackState的行
        String[] lines = output.split("\n");
        for (String line : lines) {
            if (line.contains("state=PlaybackState")) {
                // 解析播放状态 - 格式: state=PlaybackState {state=3, ...}
                // 匹配 {state=数字 的格式
                Pattern statePattern = Pattern.compile("\\{state=(\\d+)");
                Matcher stateMatcher = statePattern.matcher(line);
                if (stateMatcher.find()) {
                    int state = Integer.parseInt(stateMatcher.group(1));
                    info.isPlaying = (state == 3); // state=3表示正在播放
                    Log.d(TAG, "解析到播放状态: " + state + ", isPlaying: " + info.isPlaying);
                }

                // 解析播放位置 - 格式: position=27068
                // 在PlaybackState行中查找position=
                Pattern positionPattern = Pattern.compile("position=(\\d+)");
                Matcher positionMatcher = positionPattern.matcher(line);
                if (positionMatcher.find()) {
                    info.currentPosition = Long.parseLong(positionMatcher.group(1));
                    Log.d(TAG, "解析到播放位置: " + info.currentPosition + "ms");
                }

                // 解析音乐时长
                Pattern durationPattern = Pattern.compile("duration=(\\d+)");
                Matcher durationMatcher = durationPattern.matcher(output);
                if (durationMatcher.find()) {
                    info.duration = Long.parseLong(durationMatcher.group(1));
                    Log.d(TAG, "解析到音乐时长: " + info.duration + "ms");
                }

                break;
            }
        }
        
        //Log.d(TAG, "通过dumpsys解析到音乐信息: " + info);
        return info;
    }

    /**
     * 音乐信息数据类
     */
    public static class MusicInfo {
        public String musicName = "此刻无声，佳音已备候君启...";
        public long currentPosition = 0; // 当前播放位置（毫秒）
        public long duration = 0; // 音乐总时长（毫秒）
        public boolean isPlaying = false; // 播放状态
        
        @Override
        public String toString() {
            return "MusicInfo{" +
                    "musicName='" + musicName + '\'' +
                    ", currentPosition=" + currentPosition +
                    ", duration=" + duration +
                    ", isPlaying=" + isPlaying +
                    '}';
        }
    }
}