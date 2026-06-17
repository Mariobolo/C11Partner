package com.c11partner.desktop.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.c11partner.desktop.R;
import com.c11partner.desktop.utils.MusicUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 音乐服务
 * 监控系统日志中的音乐控制命令，执行相应的音乐播放控制操作
 */
public class MusicService extends Service {
    private static final String TAG = "MusicService";
    private static final int PROCESS_CHECK_INTERVAL = 5000;
    private MusicUtils headsetController;

    // 前台服务配置
    private static final String CHANNEL_ID = "MusicServiceChannel";
    private static final int NOTIFICATION_ID = 1001;

    private static final Pattern MUSIC_COMMAND_PATTERN = Pattern.compile("(暂停播放|继续播放|上一首|下一首)");

    private final IBinder binder = new LocalBinder();

    private HandlerThread musicLogThread;
    private Handler musicLogHandler;
    private Process musicLogcatProcess;

    private boolean isRunning = false;

    /**
     * 本地Binder类
     * 用于绑定服务
     */
    public class LocalBinder extends Binder {
        /**
         * 获取服务实例
         *
         * @return MusicService实例
         */
        public MusicService getService() {
            return MusicService.this;
        }
    }

    /**
     * 服务创建时调用
     * 初始化通知渠道，创建处理线程和音乐控制工具
     */
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel(); // 创建通知渠道（Android 8.0+必需）

        musicLogThread = new HandlerThread("Music-Logcat-Thread");
        musicLogThread.start();
        musicLogHandler = new Handler(musicLogThread.getLooper());

        headsetController = new MusicUtils(this);
        //showToast("音乐服务已启动...");
    }

    /**
     * 服务启动时调用
     * 启动前台服务和双日志监控
     *
     * @param intent  启动意图
     * @param flags   启动标志
     * @param startId 启动ID
     * @return 启动模式
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 启动前台服务（核心保活修改）
        Notification notification = buildNotification();
        startForeground(NOTIFICATION_ID, notification);

        startDualLogMonitoring();
        return START_STICKY;
    }

    /**
     * 创建通知渠道
     * 为Android 8.0及以上版本创建通知渠道
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "START",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("后台音乐控制服务运行中");
            // 添加前台服务类型
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                channel.setAllowBubbles(false);
            }
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * 构建前台服务通知
     * 创建用于保活的前台服务通知
     *
     * @return 前台服务通知
     */
    private Notification buildNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("START")
                .setContentText("MusicService Monitoring...")
                .setSmallIcon(R.drawable.ic_launcher) // 替换为您的图标资源
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true) // 不可清除的通知
                .build();
    }

    /**
     * 绑定服务时调用
     *
     * @param intent 绑定意图
     * @return Binder对象
     */
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * 服务销毁时调用
     * 停止日志监控，清理资源，移除前台通知
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopDualLogMonitoring();
        if (musicLogThread != null) {
            musicLogThread.quitSafely();
        }
        stopForeground(true); // 移除通知
        showToast("音乐服务已停止...");
    }

    /**
     * 启动双日志监控
     * 启动音乐日志监控线程和进程状态检查
     */
    private void startDualLogMonitoring() {
        if (isRunning) return;
        isRunning = true;
        musicLogHandler.post(this::runMusicLogcat);
        musicLogHandler.postDelayed(this::checkMusicProcessStatus, PROCESS_CHECK_INTERVAL);
    }

    /**
     * 停止双日志监控
     * 停止音乐日志监控进程
     */
    private void stopDualLogMonitoring() {
        isRunning = false;
        if (musicLogcatProcess != null) {
            musicLogcatProcess.destroy();
            musicLogcatProcess = null;
        }
    }

    /**
     * 音乐日志监控线程
     * 监控系统日志中的音乐控制命令
     */
    private void runMusicLogcat() {
        BufferedReader reader = null;
        try {
            musicLogcatProcess = new ProcessBuilder("logcat", "-c").redirectErrorStream(true).start();
            musicLogcatProcess.waitFor();

            musicLogcatProcess = new ProcessBuilder(
                    "logcat",
                    "-v", "threadtime",
                    "-e", "唤醒2成功"
            ).redirectErrorStream(true).start();

            reader = new BufferedReader(
                    new InputStreamReader(musicLogcatProcess.getInputStream())
            );

            String line;
            while (isRunning && (line = reader.readLine()) != null) {
                parseMusicLogLine(line);
            }
        } catch (IOException | InterruptedException e) {
            Log.e(TAG, "音乐日志读取失败: " + e.getMessage());
            restartMusicLogcat();
        } finally {
            closeQuietly(reader);
            if (musicLogcatProcess != null) {
                musicLogcatProcess.destroy();
            }
            restartMusicLogcat();
        }
    }

    /**
     * 解析音乐日志
     * 解析日志行中的音乐控制命令并执行相应操作
     *
     * @param line 日志行
     */
    private void parseMusicLogLine(String line) {
        Matcher matcher = MUSIC_COMMAND_PATTERN.matcher(line);
        if (matcher.find()) {
            String command = matcher.group(1);
            switch (command) {
                case "暂停播放":
                    headsetController.pause();
                    break;
                case "继续播放":
                    headsetController.play();
                    break;
                case "上一首":
                    headsetController.previous();
                    break;
                case "下一首":
                    headsetController.next();
                    break;
            }
            showToast("执行音乐操作: " + command);
        }
    }

    /**
     * 重启音乐日志进程
     * 当音乐日志进程异常时重启进程
     */
    private void restartMusicLogcat() {
        stopMusicProcess();
        musicLogHandler.postDelayed(this::runMusicLogcat, 1000);
    }

    /**
     * 检查音乐进程状态
     * 定期检查音乐日志进程是否正常运行
     */
    private void checkMusicProcessStatus() {
        if (!isRunning) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (musicLogcatProcess == null || !musicLogcatProcess.isAlive()) {
                Log.w(TAG, "音乐日志进程异常，正在重启...");
                restartMusicLogcat();
            }
        }
        musicLogHandler.postDelayed(this::checkMusicProcessStatus, PROCESS_CHECK_INTERVAL);
    }

    /**
     * 停止音乐进程
     * 销毁音乐日志进程
     */
    private void stopMusicProcess() {
        if (musicLogcatProcess != null) {
            musicLogcatProcess.destroy();
            musicLogcatProcess = null;
        }
    }

    /**
     * 在主线程中执行操作
     * 确保操作在主线程中执行
     *
     * @param action 要执行的操作
     */
    private void runOnMainThread(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }

    /**
     * 安静地关闭资源
     * 关闭可关闭的资源，不抛出异常
     *
     * @param closable 可关闭的资源
     */
    private void closeQuietly(AutoCloseable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (Exception e) {
                Log.e(TAG, "资源关闭失败: " + e.getMessage());
            }
        }
    }

    /**
     * 显示Toast消息
     * 在主线程中显示Toast消息
     *
     * @param msg 消息内容
     */
    private void showToast(String msg) {
        runOnMainThread(() ->
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        );
    }
}