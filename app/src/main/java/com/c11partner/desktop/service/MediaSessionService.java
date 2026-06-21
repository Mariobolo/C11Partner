package com.c11partner.desktop.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.c11partner.desktop.R;
import com.c11partner.desktop.utils.MusicInfoExtractor;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MediaSessionService extends Service {
    private static final String TAG = "MediaSessionMonitor";
    private static final String CHANNEL_ID = "MusicMonitorChannel";
    private static final int NOTIFICATION_ID = 1001;
    private static final int MONITOR_INTERVAL = 2000; // 2秒间隔

    private HandlerThread monitorThread;
    private Handler monitorHandler;
    private Process dumpsysProcess;
    private boolean isRunning = false;

    private final IBinder binder = new LocalBinder();
    public static String currentMusicName = "此刻无声，佳音已备候君启...";
    public static String currentMusicArtist = "未知艺术家";
    public static long currentPosition = 0; // 当前播放位置（毫秒）
    public static long duration = 0; // 音乐总时长（毫秒）
    public static boolean isPlaying = false; // 播放状态
    
    private long lastUpdateTime = 0; // 上次更新时间
    private long lastPosition = 0; // 上次播放位置
    
    // 为了平滑播放位置更新，提供一个实时计算播放位置的方法
    public long getCurrentSmoothPosition() {
        if (isPlaying && lastUpdateTime > 0 && lastPosition > 0) {
            long currentTime = System.currentTimeMillis();
            long timePassed = currentTime - lastUpdateTime;
            return lastPosition + timePassed;
        }
        return currentPosition;
    }

    public class LocalBinder extends Binder {
        public MediaSessionService getService() {
            return MediaSessionService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 创建通知渠道
        createNotificationChannel();
        monitorThread = new HandlerThread("MediaSession-Monitor-Thread");
        monitorThread.start();
        monitorHandler = new Handler(monitorThread.getLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 启动前台服务
        startForeground(NOTIFICATION_ID, buildForegroundNotification());
        startMonitoring();
        
        // 添加额外的保活机制
        schedulePeriodicRestart();
        
        return START_STICKY;
    }

    private void startMonitoring() {
        if (isRunning) return;
        isRunning = true;
        monitorHandler.post(this::runMediaSessionDump);
    }
    
    /**
     * 定期重启服务以确保稳定性
     */
    private void schedulePeriodicRestart() {
        monitorHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    Log.d(TAG, "定期重启服务以确保稳定性");
                    stopMonitoring();
                    startMonitoring();
                    
                    // 继续安排下一次重启
                    schedulePeriodicRestart();
                }
            }
        }, 300000); // 每5分钟重启一次
    }

    private void runMediaSessionDump() {
        try {
            //直使用使用dumpsys命令获取完整的音乐信息
            updateMediaSessionInfoViaDumpsys();
            
            // 如果仍然无法获取播放时间信息，但音乐正在播放，
            // 表示设备可能不支持获取详细播放信息
            if (isPlaying && currentPosition == 0 && duration == 0) {
                Log.d(TAG, "检测到设备可能不支持获取音乐播放时间信息");
            }
        } catch (Exception e) {
            Log.e(TAG, "获取音乐信息时出错", e);
            // 如果出现异常，也要确保继续监控
            // 避免因单次错误导致监控停止
        } finally {
            // 确保继续监控
            if (isRunning) {
                monitorHandler.postDelayed(this::runMediaSessionDump, MONITOR_INTERVAL);
            }
        }
    }
    
    /**
     * 更新音乐信息
     */
    private void updateMusicInfo(MusicInfoExtractor.MusicInfo info) {
        boolean musicNameChanged = !info.musicName.equals(currentMusicName);
        boolean playingStateChanged = info.isPlaying != isPlaying;
        
        if (musicNameChanged) {
            currentMusicName = info.musicName;
            Log.d(TAG, "正在播放: " + currentMusicName);
            sendMusicNotification(); // 歌曲变化时发送通知
        }
        
        currentPosition = info.currentPosition;
        
        // 如果获取的时长无效，使用默认值4分30秒（270000毫秒）
       
        duration = 270000; // 4分30秒
           
       
        
        isPlaying = info.isPlaying;
        
        // 记录解析到的信息
        Log.d(TAG, "播放状态: " + (isPlaying ? "播放中" : "暂停/停止") + 
                 ", 当前位置: " + currentPosition + "ms, 总时长: " + duration + "ms");
        
        // 通知前端更新音乐状态
        notifyFrontendMusicUpdate();
    }
    
    /**
     * 通知前端更新音乐状态
     */
    private void notifyFrontendMusicUpdate() {
        try {
            // 使用平滑计算的播放位置更新前端
            long smoothPosition = getCurrentSmoothPosition();
            // 调用MainActivity的静态方法更新前端
            com.c11partner.desktop.MainActivity.updateMusicStatus(
                currentMusicName,
                isPlaying,
                smoothPosition,  // 使用平滑位置
                duration
            );
        } catch (Exception e) {
            Log.e(TAG, "通知前端更新音乐状态时出错: " + e.getMessage());
        }
    }

    /**
     * 通过系统API获取媒体会话信息（更可靠的方法）
     */
    private void updateMediaSessionInfoViaAPI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                MediaSessionManager mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
                if (mediaSessionManager != null) {
                    // 使用NotificationListenerService的组件名称来获取媒体会话
                    android.content.ComponentName notificationListener = new android.content.ComponentName(
                            this, 
                            MusicNotificationListenerService.class
                    );
                    List<MediaController> controllers = mediaSessionManager.getActiveSessions(notificationListener);
                    if (controllers != null && !controllers.isEmpty()) {
                        for (MediaController controller : controllers) {
                            if (controller.getPackageName() != null) {
                                // 获取播放状态
                                PlaybackState playbackState = controller.getPlaybackState();
                                if (playbackState != null) {
                                    isPlaying = (playbackState.getState() == PlaybackState.STATE_PLAYING);
                                    currentPosition = playbackState.getPosition();
                                    // 获取更新时间，用于计算更准确的位置
                                    long updateTime = playbackState.getLastPositionUpdateTime();
                                    if (updateTime > 0 && isPlaying) {
                                        // 根据更新时间和当前时间计算更准确的位置
                                        long elapsedTime = System.currentTimeMillis() - updateTime;
                                        currentPosition += (long)(elapsedTime * playbackState.getPlaybackSpeed());
                                    }
                                }
                                
                                // 获取媒体元数据
                                MediaMetadata metadata = controller.getMetadata();
                                if (metadata != null) {
                                    // 获取音乐名称
                                    CharSequence title = metadata.getText(MediaMetadata.METADATA_KEY_TITLE);
                                    if (title != null && !title.toString().isEmpty()) {
                                        String newMusicName = title.toString();
                                        if (!newMusicName.equals(currentMusicName)) {
                                            currentMusicName = newMusicName;
                                            Log.d(TAG, "正在播放: " + currentMusicName);
                                            sendMusicNotification(); // 歌曲变化时发送通知
                                        }
                                    }
                                    
                                    // 获取音乐时长
                                    duration = metadata.getLong(MediaMetadata.METADATA_KEY_DURATION);
                                }
                                
                                // 记录解析到的信息
                                Log.d(TAG, "播放状态: " + (isPlaying ? "播放中" : "暂停/停止") + 
                                         ", 当前位置: " + currentPosition + "ms, 总时长: " + duration + "ms");
                                break; // 只处理第一个活动的会话
                            }
                        }
                    } else {
                        Log.d(TAG, "未找到活动的媒体会话，可能需要授权通知监听权限");
                        // 重置状态
                        currentMusicName = "此刻无声，佳音已备候君启...";
                        currentPosition = 0;
                        duration = 0;
                        isPlaying = false;
                    }
                } else {
                    Log.w(TAG, "无法获取MediaSessionManager服务");
                }
            } catch (SecurityException e) {
                Log.e(TAG, "权限不足，无法访问媒体会话信息: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "通过系统API获取媒体会话信息失败: " + e.getMessage());
            }
        }
    }

    /**
     * 通过dumpsys命令获取媒体会话信息
     */
    private void updateMediaSessionInfoViaDumpsys() {
        BufferedReader reader = null;
        Process process = null;
        try {
            process = new ProcessBuilder()
                    .command("sh", "-c", "dumpsys media_session | cat")
                    .redirectErrorStream(true)
                    .start();

            reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "UTF-8"));

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            parseMediaSessionOutput(output.toString());

        } catch (IOException e) {
            Log.e(TAG, "dumpsys执行失败: " + e.getMessage());
        } finally {
            closeQuietly(reader);
            if (process != null) {
                process.destroy();
            }
        }
    }

    // 注：updateMediaSessionInfoViaADB()方法已移除，其功能已整合到dumpsys方法中

    private void parseMediaSessionOutput(String output) {
        // 重置音乐状态
        boolean musicNameChanged = false;
        String newMusicName = "此刻无声，佳音已备候君启...";
        boolean newIsPlaying = false;
        long newPosition = 0;
        long newDuration = 0;
            
        // 根据实际dumpsys输出格式进行解析
        // 示例输出：
        // Tag com.luna.music.car/Tag (userId=0)
        //   active=true
        //   metadata: size=8, description=是非题, 赵乃吉, 是非题
        //   state=PlaybackState {state=3, position=82579, ...}
            
        String[] lines = output.split("\n");
        String currentSessionPackage = null;
        boolean currentSessionActive = false;
        String currentSessionMetadata = null;
        String currentSessionState = null;
            
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
                
            // 检测会话开始
            if (line.startsWith("Tag ") || line.startsWith("MediaCenterService ") || line.startsWith("BluetoothMediaBrowserService ") || line.contains("MediaSessionService") || line.contains("KwMediaSessionService")) {
                // 处理上一个会话
                if (currentSessionPackage != null && currentSessionActive && currentSessionMetadata != null && currentSessionState != null) {
                    // 解析当前会话信息
                    String metadataDesc = extractDescriptionFromMetadata(currentSessionMetadata);
                    if (metadataDesc != null && !metadataDesc.isEmpty()) {
                        int playbackState = extractPlaybackState(currentSessionState);
                        long position = extractPlaybackPosition(currentSessionState);
                        long duration = extractDurationFromMetadata(currentSessionMetadata);
                                                
                        // 如果从元数据中没有获取到有效时长，尝试从外部源获取
                        if (duration <= 0 && currentSessionPackage != null) {
                            duration = getDurationFromExternalSource(currentSessionPackage);
                        }
                                                
                        // 如果是播放状态，使用这个会话的信息
                        if (playbackState == 3) { // state=3 表示正在播放
                            newMusicName = metadataDesc;
                            musicNameChanged = !newMusicName.equals(currentMusicName);
                            newIsPlaying = true;
                            newPosition = position;
                            newDuration = duration;
                            break; // 找到正在播放的会话就退出
                        } else if (playbackState == 2 && newMusicName.equals("此刻无声，佳音已备候君启...")) { // state=2 表示暂停，如果没有正在播放的音乐，则使用暂停的音乐
                            newMusicName = metadataDesc;
                            musicNameChanged = !newMusicName.equals(currentMusicName);
                            newIsPlaying = false;
                            newPosition = position;
                            newDuration = duration;
                        }
                    }
                }
                    
                // 开始新的会话 - 提取包名
                if (line.contains("/")) {
                    String[] parts = line.split("/");
                    if (parts.length > 0) {
                        String fullSession = parts[0]; // e.g. "Tag com.luna.music.car"
                        String[] nameParts = fullSession.split(" ");
                        if (nameParts.length >= 2) {
                            currentSessionPackage = nameParts[1]; // "com.luna.music.car"
                        }
                    }
                }
                currentSessionActive = false;
                currentSessionMetadata = null;
                currentSessionState = null;
            }
                
            // 检测活跃状态
            if (line.contains("active=true")) {
                currentSessionActive = true;
            }
                
            // 检测元数据
            if (line.contains("metadata: size=")) {
                currentSessionMetadata = line;
            }
                
            // 检测播放状态
            if (line.contains("state=PlaybackState")) {
                currentSessionState = line;
            }
        }
            
        // 检查最后一个会话
        if (currentSessionPackage != null && currentSessionActive && currentSessionMetadata != null && currentSessionState != null) {
            String metadataDesc = extractDescriptionFromMetadata(currentSessionMetadata);
            if (metadataDesc != null && !metadataDesc.isEmpty()) {
                int playbackState = extractPlaybackState(currentSessionState);
                long position = extractPlaybackPosition(currentSessionState);
                long duration = extractDurationFromMetadata(currentSessionMetadata);
                
                // 如果从元数据中没有获取到有效时长，尝试从外部源获取
                if (duration <= 0 && currentSessionPackage != null) {
                    duration = getDurationFromExternalSource(currentSessionPackage);
                    
                    // 如果获取到的时长接近播放位置，说明可能获取错了，使用一个默认值
                    if (duration > 0 && Math.abs(duration - position) < 5000) { // 如果时长和位置差值小于5秒
                        Log.d(TAG, "检测到时长与播放位置过于接近，可能解析错误: 时长=" + duration + ", 位置=" + position);
                        duration = 200000; // 默认3分20秒（200秒 = 200000毫秒）
                    }
                    // 如果时长异常大（超过30分钟），也认为是解析错误
                    else if (duration > 1800000) { // 30分钟 = 30*60*1000ms
                        Log.d(TAG, "检测到异常大的时长值，可能解析错误: 时长=" + duration);
                        duration = 200000; // 默认3分20秒（200秒 = 200000毫秒）
                    }
                    // 如果仍然没有获取到有效时长，使用默认值
                    else if (duration <= 0) {
                        duration = 200000; // 默认3分20秒（200秒 = 200000毫秒）
                    }
                }
                
                if (playbackState == 3) { // state=3 表示正在播放
                    newMusicName = metadataDesc;
                    musicNameChanged = !newMusicName.equals(currentMusicName);
                    newIsPlaying = true;
                    newPosition = position;
                    newDuration = duration;
                } else if (playbackState == 2 && newMusicName.equals("此刻无声，佳音已备候君启...")) { // state=2 表示暂停，如果没有正在播放的音乐，则使用暂停的音乐
                    newMusicName = metadataDesc;
                    musicNameChanged = !newMusicName.equals(currentMusicName);
                    newIsPlaying = false;
                    newPosition = position;
                    newDuration = duration;
                }
            }
        }
            
        // 如果正在播放且获取到了有效的播放位置，根据时间差计算更准确的位置
        if (newIsPlaying && newPosition > 0) {
            long currentTime = System.currentTimeMillis();
            if (lastUpdateTime > 0 && lastPosition > 0) {
                // 计算自上次更新以来经过的时间
                long timePassed = currentTime - lastUpdateTime;
                // 根据时间差更新播放位置
                newPosition = lastPosition + timePassed;
            }
            // 更新最后更新时间
            lastUpdateTime = currentTime;
            lastPosition = newPosition;
        } else {
            // 如果不在播放状态，重置计时器
            lastUpdateTime = 0;
            lastPosition = 0;
        }
        
        // 更新全局状态
        currentMusicName = newMusicName;
        isPlaying = newIsPlaying;
        currentPosition = newPosition;
        duration = newDuration;
    
        // 发送通知（仅在歌曲变化时）
        if (musicNameChanged && isPlaying) {
            sendMusicNotification();
        }
    
        //记录解析到的完整信息
        Log.d(TAG, "解析结果 - 歌名: " + currentMusicName +
                ", 状态: " + (isPlaying ? "播放中" : "暂停/停止") +
                ", 位置: " + currentPosition + "ms" +
                ", 时长: " + duration + "ms");
            
        // 通知前端更新音乐状态
        notifyFrontendMusicUpdate();
    }
        
    private String extractDescriptionFromMetadata(String metadataLine) {
        if (metadataLine == null) return null;
            
        // 从 metadata: size=8, description=是非题, 赵乃吉, 是非题 中提取描述
        int descIndex = metadataLine.indexOf("description=");
        if (descIndex != -1) {
            String descPart = metadataLine.substring(descIndex + "description=".length());
            // 找到下一个逗号或行尾
            int commaIndex = descPart.indexOf(",");
            if (commaIndex != -1) {
                descPart = descPart.substring(0, commaIndex);
            }
            descPart = descPart.trim();
            // 如果描述以引号开头结尾，移除它们
            if (descPart.startsWith("\"") && descPart.endsWith("\"")) {
                descPart = descPart.substring(1, descPart.length() - 1);
            }
            return descPart.isEmpty() ? null : descPart;
        }
            
        return null;
    }
        
    private int extractPlaybackState(String stateLine) {
        if (stateLine == null) return 0;
            
        // 查找 state= 后面的数字
        int stateIndex = stateLine.indexOf("state=");
        if (stateIndex != -1) {
            String statePart = stateLine.substring(stateIndex + "state=".length());
            // 跳过可能的括号
            if (statePart.startsWith("(") || Character.isLetter(statePart.charAt(0))) {
                int parenEnd = statePart.indexOf(")");
                if (parenEnd != -1) {
                    statePart = statePart.substring(parenEnd + 1);
                }
            }
                
            // 提取数字
            StringBuilder numBuilder = new StringBuilder();
            for (char c : statePart.toCharArray()) {
                if (Character.isDigit(c)) {
                    numBuilder.append(c);
                } else if (numBuilder.length() > 0) {
                    break; // 数字结束后停止
                }
                // 继续直到找到数字
                else if (Character.isWhitespace(c) && numBuilder.length() == 0) {
                    continue; // 跳过前导空白
                } else if (numBuilder.length() == 0) {
                    continue; // 继续查找数字
                }
            }
                
            if (numBuilder.length() > 0) {
                try {
                    return Integer.parseInt(numBuilder.toString());
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }
        return 0;
    }
        
    private long extractPlaybackPosition(String stateLine) {
        if (stateLine == null) return 0;
            
        // 查找 position= 后面的数字
        int posIndex = stateLine.indexOf("position=");
        if (posIndex != -1) {
            String posPart = stateLine.substring(posIndex + "position=".length());
            StringBuilder numBuilder = new StringBuilder();
            boolean isNegative = false;
                
            // 检查是否有负号
            if (posPart.startsWith("-")) {
                isNegative = true;
                posPart = posPart.substring(1);
            }
                
            for (char c : posPart.toCharArray()) {
                if (Character.isDigit(c)) {
                    numBuilder.append(c);
                } else if (numBuilder.length() > 0) {
                    break; // 数字结束后停止
                }
                // 继续直到找到数字
                else if (Character.isWhitespace(c) && numBuilder.length() == 0) {
                    continue; // 跳过前导空白
                } else if (numBuilder.length() == 0) {
                    continue; // 继续查找数字
                }
            }
                
            if (numBuilder.length() > 0) {
                try {
                    long value = Long.parseLong(numBuilder.toString());
                    return isNegative ? -value : value;
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }
        return 0;
    }
        
    private long extractDurationFromMetadata(String metadataLine) {
        if (metadataLine == null) return 0;
            
        // 查找 duration= 后面的数字
        int durIndex = metadataLine.indexOf("duration=");
        if (durIndex != -1) {
            String durPart = metadataLine.substring(durIndex + "duration=".length());
            StringBuilder numBuilder = new StringBuilder();
                
            for (char c : durPart.toCharArray()) {
                if (Character.isDigit(c)) {
                    numBuilder.append(c);
                } else if (numBuilder.length() > 0) {
                    break; // 数字结束后停止
                }
                // 继续直到找到数字
                else if (Character.isWhitespace(c) && numBuilder.length() == 0) {
                    continue; // 跳过前导空白
                } else if (numBuilder.length() == 0) {
                    continue; // 继续查找数字
                }
            }
                
            if (numBuilder.length() > 0) {
                try {
                    return Long.parseLong(numBuilder.toString());
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }
        return 0;
    }

    // 注：parseADBOutput()方法已移除，其功能已整合到parseMediaSessionOutput()方法中



    /**
     * 发送音乐通知
     * 显示当前播放的音乐信息
     */
    private void sendMusicNotification() {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("正在播放")
                .setContentText(currentMusicName)
                .setSmallIcon(R.mipmap.ic_launcher) // 使用mipmap目录中的图标
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(false) // 非持续通知，用户可清除
                .build();

        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.notify(NOTIFICATION_ID, notification);
        }
    }

    /**
     * 创建通知渠道
     * 为Android 8.0及以上版本创建通知渠道
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "音乐播放通知", NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("显示系统当前播放的歌曲");
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
    }

    /**
     * 构建前台服务通知
     * 创建用于保活的前台服务通知
     *
     * @return 前台服务通知
     */
    private Notification buildForegroundNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("音乐播放监测")
                .setContentText("服务运行中")
                .setSmallIcon(R.mipmap.ic_launcher) // 使用mipmap目录中的图标
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setOngoing(true) // 不可清除
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMonitoring();
        if (monitorThread != null) {
            monitorThread.quitSafely();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void stopMonitoring() {
        isRunning = false;
        if (dumpsysProcess != null) {
            dumpsysProcess.destroy();
        }
    }

    public String getCurrentMusicName() {
        return currentMusicName;
    }

    public String getCurrentMusicArtist() {
        return currentMusicArtist;
    }

    public long getCurrentPosition() {
        return currentPosition;
    }

    public long getDuration() {
        return duration;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
    
    /**
     * 从外部源获取音乐时长
     * 对于车机设备，尝试通过其他方式获取当前播放音乐的总时长
     */
    private long getDurationFromExternalSource(String packageName) {
        if (packageName == null || packageName.isEmpty()) {
            return 0;
        }
        
        // 首先尝试通过系统API获取音乐时长，这是最准确的方法
        long duration = getDurationFromSystemAPI();
        if (duration > 0) {
            Log.d(TAG, "通过系统API获取到音乐时长: " + duration + "ms");
            return duration;
        }
        
        // 如果API方法失败，再尝试使用dumpsys方法
        try {
            // 尝试使用adb命令获取当前音乐应用的详细信息
            Process process = new ProcessBuilder()
                    .command("sh", "-c", "dumpsys media_session | grep -A 30 \"" + packageName + "\"")
                    .redirectErrorStream(true)
                    .start();
            
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "UTF-8"));
            
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            // 关闭资源
            reader.close();
            process.destroy();
            
            String outputStr = output.toString();
            
            // 首先尝试查找特定关键词附近的数字，提高准确性
            String[] keywords = {"duration", "trackDuration", "totalTime", "length", "durationMillis"};
            for (String keyword : keywords) {
                int index = outputStr.toLowerCase().indexOf(keyword);
                if (index != -1) {
                    // 在关键词附近查找数字
                    int start = Math.max(0, index - 30);
                    int end = Math.min(outputStr.length(), index + 100);
                    String context = outputStr.substring(start, end);
                    
                    // 查找关键词后的数字
                    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(?:" + keyword + "\\s*[=:>]?\\s*)(\\d{2,6})");
                    java.util.regex.Matcher matcher = pattern.matcher(context.toLowerCase());
                    if (matcher.find()) {
                        try {
                            long number = Long.parseLong(matcher.group(1));
                            // 验证数字是否在合理范围内
                            if (number >= 30 && number <= 900) { // 秒级，转换为毫秒
                                return number * 1000;
                            } else if (number >= 30000 && number <= 600000) { // 毫秒级
                                return number;
                            }
                        } catch (NumberFormatException e) {
                            // 继续尝试其他匹配
                        }
                    }
                }
            }
            
            // 如果特定关键词没有找到，尝试通用方法
            StringBuilder numBuilder = new StringBuilder();
            boolean foundNumbers = false;
            
            for (int i = 0; i < outputStr.length(); i++) {
                char c = outputStr.charAt(i);
                if (Character.isDigit(c)) {
                    numBuilder.append(c);
                    foundNumbers = true;
                } else if (foundNumbers && !Character.isDigit(c) && c != '.') {
                    if (numBuilder.length() > 0) {
                        try {
                            long number = Long.parseLong(numBuilder.toString());
                            // 检查数字是否在合理范围（30秒到10分钟）
                            if (number >= 30 && number <= 600) { // 30秒到10分钟
                                // 检查是否紧邻时间相关关键词
                                String numberStr = String.valueOf(number);
                                int startIdx = Math.max(0, i - numberStr.length() - 20);
                                int endIdx = Math.min(outputStr.length(), i + 5);
                                String nearbyText = outputStr.substring(startIdx, endIdx).toLowerCase();
                                
                                if (nearbyText.contains("duration") || nearbyText.contains("time") || 
                                    nearbyText.contains("total") || nearbyText.contains("length")) {
                                    return number * 1000; // 作为秒转为毫秒
                                }
                            } else if (number >= 30000 && number <= 600000) { // 毫秒范围
                                return number;
                            }
                        } catch (NumberFormatException e) {
                            // 忽略并继续
                        }
                        numBuilder.setLength(0);
                        foundNumbers = false;
                    }
                }
            }
            
            // 检查最后的数字
            if (numBuilder.length() > 0) {
                try {
                    long number = Long.parseLong(numBuilder.toString());
                    if (number >= 30000 && number <= 600000) { // 毫秒范围
                        return number;
                    } else if (number >= 30 && number <= 900) { // 秒范围
                        return number * 1000; // 转为毫秒
                    }
                } catch (NumberFormatException e) {
                    // 忽略
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "获取外部时长信息失败: " + e.getMessage());
        }
        
        // 如果方法都失败，返回0
        return 0;
    }
    
    /**
     * 通过系统API获取音乐时长
     * 使用MediaSessionManager和MediaController获取准确的音乐信息
     */
    private long getDurationFromSystemAPI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                MediaSessionManager mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
                if (mediaSessionManager != null) {
                    // 获取所有活动的媒体会话
                    List<MediaController> controllers = mediaSessionManager.getActiveSessions(null);
                    if (controllers != null && !controllers.isEmpty()) {
                        for (MediaController controller : controllers) {
                            // 获取播放状态
                            PlaybackState playbackState = controller.getPlaybackState();
                            if (playbackState != null) {
                                // 检查是否正在播放
                                if (playbackState.getState() == PlaybackState.STATE_PLAYING) {
                                    // 获取媒体元数据
                                    MediaMetadata metadata = controller.getMetadata();
                                    if (metadata != null) {
                                        // 获取音乐时长
                                        long duration = metadata.getLong(MediaMetadata.METADATA_KEY_DURATION);
                                        if (duration > 0) {
                                            Log.d(TAG, "通过系统API获取到音乐时长: " + duration + "ms");
                                            return duration;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (SecurityException e) {
                Log.w(TAG, "权限不足，无法通过系统API获取音乐时长: " + e.getMessage());
            } catch (Exception e) {
                Log.w(TAG, "通过系统API获取音乐时长失败: " + e.getMessage());
            }
        }
        
        return 0; // API方法无法获取时长
    }
    
    /**
     * 跳转到指定播放位置
     *
     * @param position 播放位置（毫秒）
     */
    public void seekTo(long position) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                MediaSessionManager mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
                if (mediaSessionManager != null) {
                    List<MediaController> controllers = mediaSessionManager.getActiveSessions(null);
                    if (controllers != null && !controllers.isEmpty()) {
                        for (MediaController controller : controllers) {
                            try {
                                controller.getTransportControls().seekTo(position);
                                Log.d(TAG, "已发送seekTo命令到: " + controller.getPackageName());
                            } catch (Exception e) {
                                Log.w(TAG, "发送seekTo到 " + controller.getPackageName() + " 失败: " + e.getMessage());
                            }
                        }
                    }
                }
            }
            // 更新本地状态
            currentPosition = position;
            Log.d(TAG, "跳转到播放位置: " + position + "ms");
        } catch (Exception e) {
            Log.e(TAG, "跳转播放位置失败", e);
        }
    }
    
    /**
     * 设置播放速度
     *
     * @param speed 播放速度（0.5-2.0）
     */
    public void setPlaybackSpeed(float speed) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                MediaSessionManager mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
                if (mediaSessionManager != null) {
                    List<MediaController> controllers = mediaSessionManager.getActiveSessions(null);
                    if (controllers != null && !controllers.isEmpty()) {
                        for (MediaController controller : controllers) {
                            try {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    controller.getTransportControls().setPlaybackSpeed(speed);
                                    Log.d(TAG, "已发送setPlaybackSpeed命令到: " + controller.getPackageName());
                                }
                            } catch (Exception e) {
                                Log.w(TAG, "发送setPlaybackSpeed到 " + controller.getPackageName() + " 失败: " + e.getMessage());
                            }
                        }
                    }
                }
            }
            Log.d(TAG, "设置播放速度: " + speed);
        } catch (Exception e) {
            Log.e(TAG, "设置播放速度失败", e);
        }
    }
    
    private void closeQuietly(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (IOException e) {
                Log.e(TAG, "关闭资源失败", e);
            }
        }
    }
    
    // ==================== 播放列表与播放模式控制方法 ====================
    
    /**
     * 获取当前播放列表
     *
     * @return 播放列表JSON字符串
     */
    public String getPlaylist() {
        // 当前系统API不支持直接获取播放列表
        // 返回空列表作为默认实现
        Log.d(TAG, "getPlaylist() - 当前系统不支持获取播放列表");
        return "[]";
    }
    
    public void playSongAtIndex(int index) {
        // 当前系统API不支持直接跳转到指定歌曲
        // 通过skipToQueueItem尝试实现（如果支持）
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                MediaSessionManager mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
                if (mediaSessionManager != null) {
                    List<MediaController> controllers = mediaSessionManager.getActiveSessions(null);
                    if (controllers != null && !controllers.isEmpty()) {
                        for (MediaController controller : controllers) {
                            try {
                                controller.getTransportControls().skipToQueueItem(index);
                                Log.d(TAG, "已发送skipToQueueItem命令到: " + controller.getPackageName());
                            } catch (Exception e) {
                                Log.w(TAG, "发送skipToQueueItem到 " + controller.getPackageName() + " 失败: " + e.getMessage());
                            }
                        }
                    }
                }
            }
            Log.d(TAG, "播放第 " + index + " 首歌曲");
        } catch (Exception e) {
            Log.e(TAG, "播放指定歌曲失败", e);
        }
    }
    
    /**
     * 循环模式常量（兼容低版本）
     */
    private static final int REPEAT_MODE_INVALID = -1;
    private static final int REPEAT_MODE_NONE = 0;
    private static final int REPEAT_MODE_ONE = 1;
    private static final int REPEAT_MODE_ALL = 2;
    private static final int REPEAT_MODE_GROUP = 3;
    
    /**
     * 随机播放模式常量（兼容低版本）
     */
    private static final int SHUFFLE_MODE_INVALID = -1;
    private static final int SHUFFLE_MODE_NONE = 0;
    private static final int SHUFFLE_MODE_ALL = 1;
    private static final int SHUFFLE_MODE_GROUP = 2;
    
    /**
     * 设置循环模式
     *
     * @param mode 循环模式：0-不循环，1-单曲循环，2-列表循环
     */
    public void setRepeatMode(int mode) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                MediaSessionManager mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
                if (mediaSessionManager != null) {
                    List<MediaController> controllers = mediaSessionManager.getActiveSessions(null);
                    if (controllers != null && !controllers.isEmpty()) {
                        for (MediaController controller : controllers) {
                            try {
                                controller.getTransportControls().setRepeatMode(mode);
                                Log.d(TAG, "已发送setRepeatMode命令到: " + controller.getPackageName());
                            } catch (Exception e) {
                                Log.w(TAG, "发送setRepeatMode到 " + controller.getPackageName() + " 失败: " + e.getMessage());
                            }
                        }
                    }
                }
            }
            Log.d(TAG, "设置循环模式: " + mode);
        } catch (Exception e) {
            Log.e(TAG, "设置循环模式失败", e);
        }
    }
    
    /**
     * 获取当前循环模式
     *
     * @return 循环模式
     */
    public int getRepeatMode() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                MediaSessionManager mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
                if (mediaSessionManager != null) {
                    List<MediaController> controllers = mediaSessionManager.getActiveSessions(null);
                    if (controllers != null && !controllers.isEmpty()) {
                        for (MediaController controller : controllers) {
                            try {
                                return controller.getRepeatMode();
                            } catch (Exception e) {
                                Log.w(TAG, "获取循环模式失败: " + e.getMessage());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "获取循环模式失败", e);
        }
        return REPEAT_MODE_NONE; // 默认不循环
    }
    
    /**
     * 设置随机播放模式
     *
     * @param enabled 是否启用随机播放
     */
    public void setShuffleMode(boolean enabled) {
        try {
            int shuffleMode = enabled ? SHUFFLE_MODE_ALL : SHUFFLE_MODE_NONE;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                MediaSessionManager mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
                if (mediaSessionManager != null) {
                    List<MediaController> controllers = mediaSessionManager.getActiveSessions(null);
                    if (controllers != null && !controllers.isEmpty()) {
                        for (MediaController controller : controllers) {
                            try {
                                controller.getTransportControls().setShuffleMode(shuffleMode);
                                Log.d(TAG, "已发送setShuffleMode命令到: " + controller.getPackageName());
                            } catch (Exception e) {
                                Log.w(TAG, "发送setShuffleMode到 " + controller.getPackageName() + " 失败: " + e.getMessage());
                            }
                        }
                    }
                }
            }
            Log.d(TAG, "设置随机播放: " + enabled);
        } catch (Exception e) {
            Log.e(TAG, "设置随机播放失败", e);
        }
    }
    
    /**
     * 获取随机播放状态
     *
     * @return 是否启用随机播放
     */
    public boolean isShuffleEnabled() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                MediaSessionManager mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
                if (mediaSessionManager != null) {
                    List<MediaController> controllers = mediaSessionManager.getActiveSessions(null);
                    if (controllers != null && !controllers.isEmpty()) {
                        for (MediaController controller : controllers) {
                            try {
                                int shuffleMode = controller.getShuffleMode();
                                return shuffleMode != SHUFFLE_MODE_NONE;
                            } catch (Exception e) {
                                Log.w(TAG, "获取随机播放状态失败: " + e.getMessage());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "获取随机播放状态失败", e);
        }
        return false; // 默认不随机
    }
}