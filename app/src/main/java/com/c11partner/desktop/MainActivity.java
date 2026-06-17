package com.c11partner.desktop;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.content.ServiceConnection;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.c11partner.desktop.adb.UsbDebugConnection;
import com.c11partner.desktop.adb.AdbCommandProcessor;
import com.c11partner.desktop.database.AppDatabaseHelper;
import com.c11partner.desktop.database.ConfigAppDatabaseHelper;
import com.c11partner.desktop.database.QuickAppDatabaseHelper;
import com.c11partner.desktop.database.WallpaperCategoryDatabaseHelper;
import com.c11partner.desktop.database.WallpaperSettingsDatabaseHelper;
import com.c11partner.desktop.database.ComponentConfigDatabaseHelper;
import com.c11partner.desktop.service.MediaSessionService;
import com.c11partner.desktop.service.MusicService;
import com.c11partner.desktop.utils.AppUtils;
import com.c11partner.desktop.utils.MusicVisualizer;
import com.c11partner.desktop.utils.WallpaperDownloadUtils;
import com.c11partner.desktop.utils.QuoteApiUtils;
import com.c11partner.desktop.utils.InitManager;
import com.c11partner.desktop.utils.TaskManager;
import com.c11partner.desktop.utils.ServiceManager;
import com.c11partner.desktop.bridge.WebViewBridge;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 主活动类
 * 
 * 功能说明：
 * 1. 桌面应用的主界面，负责显示和管理所有桌面组件
 * 2. 处理壁纸轮播、音乐播放、应用列表等核心功能
 * 3. 管理与WebView的交互，实现前后端通信
 * 4. 处理权限申请、服务绑定、广播接收等系统级操作
 * 
 * 主要模块：
 * - WebView管理：负责加载和显示HTML界面
 * - 壁纸轮播：实现壁纸的自动切换功能
 * - 音乐播放：监控音乐播放状态和可视化效果
 * - 应用管理：预加载和显示应用列表
 * - ADB连接：处理ADB命令执行
 * - TTS语音：实现文字转语音功能
 */
public class MainActivity extends AppCompatActivity {
    // 广播Action常量
    public static final String ACTION_WALLPAPER_SETTINGS_CHANGED = "com.c11partner.desktop.WALLPAPER_SETTINGS_CHANGED";

    // 权限请求码常量
    private static final int REQUEST_CODE_OVERLAY_PERMISSION = 1001;
    private static final int REQUEST_SET_DEFAULT_LAUNCHER = 1003;
    private static final int REQUEST_OVERLAY_PERMISSION = 1004;
    private static final int REQUEST_STORAGE_PERMISSION = 1005;
    private static final int REQUEST_MANAGE_STORAGE = 1006;
    private static final int REQUEST_TTS_PERMISSION = 1008;

    // UI组件
    public ImageView backgroundImageView;
    public static MainActivity instance;
    public WebView webView;

    // 数据库辅助类
    public AppDatabaseHelper dbHelper;
    public QuickAppDatabaseHelper quickAppDbHelper;
    public WallpaperCategoryDatabaseHelper wallpaperDbHelper;
    public WallpaperSettingsDatabaseHelper wallpaperSettingsDbHelper;
    public ConfigAppDatabaseHelper configAppDbHelper;
    public ComponentConfigDatabaseHelper componentConfigDbHelper;

    // 音乐相关组件
    public MusicVisualizer musicVisualizer;
    public MediaSessionService mediaSessionService;
    public AudioManager audioManager;
    private MusicPlaybackReceiver musicPlaybackReceiver;
    public boolean isMusicPlaying = false;
    public Runnable musicCheckRunnable;
    public boolean isMediaSessionServiceBound = false;

    // WebView桥接器
    public WebViewBridge webViewBridge;

    // TTS语音合成
    private TextToSpeech textToSpeech;

    // API工具类
    public QuoteApiUtils quoteApiUtils;

    // ADB连接
    public UsbDebugConnection usbDebugConnection;

    // 应用列表预加载
    private String preloadedAppList;

    // 壁纸轮播相关
    public Runnable wallpaperRunnable;
    public boolean isWallpaperCarouselEnabled = false;
    public int wallpaperSwitchInterval = 15000;
    public boolean isAppInForeground = false;

    // 延迟启动任务
    public boolean bydAutoStartEnabled = false;
    public boolean bootGreetingEnabled = false;
    public Runnable bydAutoStartRunnable;
    public Runnable bootGreetingRunnable;

    // 壁纸状态
    public boolean isUsingDefaultWallpaper = true;
    public String currentWallpaperPath = "";

    // 时间更新
    private Handler timeUpdateHandler = new Handler();
    private Handler delayedStartHandler = new Handler();
    private Runnable timeUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                // 检查WebViewBridge和WebView是否可用
                if (webViewBridge != null && webView != null) {
                    // 格式化时间、日期和农历日期
                    java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("HH:mm:ss",
                            java.util.Locale.getDefault());
                    java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy年MM月dd日",
                            java.util.Locale.getDefault());
                    java.util.Date now = new java.util.Date();

                    String time = timeFormat.format(now);
                    String date = dateFormat.format(now) + " " + getWeekDay(now);
                    String lunarDate = com.c11partner.desktop.utils.LunarCalendarUtils.lunarCalendar();

                    // 调用WebViewBridge的updateTimeDisplay方法更新时间
                    webViewBridge.updateTimeDisplay(time, date, lunarDate);
                }
            } catch (Exception e) {
                Log.e("MainActivity", "更新时间时出错", e);
            } finally {
                // 继续下一次更新
                timeUpdateHandler.postDelayed(this, 1000);
            }
        }
    };

    // 壁纸设置更改广播接收器
    private WallpaperSettingsChangedReceiver wallpaperSettingsChangedReceiver;

    // 工具类
    private InitManager initManager;
    private TaskManager taskManager;
    private ServiceManager serviceManager;

    /**
     * 活动创建方法
     * 初始化所有组件和服务，设置UI布局
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置屏幕方向为用户横向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        super.onCreate(savedInstanceState);
        instance = this;

        // 初始化工具类
        initManager = new InitManager(this);
        taskManager = new TaskManager(this);
        serviceManager = new ServiceManager(this);

        // 初始化核心组件
        initManager.initCoreComponents();

        // 初始化数据库辅助类
        initManager.initDatabaseHelpers();

        // 初始化配置（只执行一次）
        initSettings();

        // 初始化WebView
        initWebView();

        // 预加载应用列表
        preloadAppList();

        // 初始化广播接收器
        initManager.initReceivers();

        // 初始化组件
        initManager.initComponents();

        // 请求权限
        initManager.initPermissions();

        // 注册音乐播放广播接收器
        registerMusicPlaybackReceiver();

        // 初始化服务
        initManager.initServices();

        // 启动各项任务
        initManager.startTasks();

    }

    /**
     * 活动启动方法
     * 应用进入前台时调用，恢复壁纸轮播
     */
    @Override
    protected void onStart() {
        super.onStart();
        isAppInForeground = true;
        resumeWallpaperCarousel();
        Log.d("MainActivity", "应用进入前台");
    }

    /**
     * 活动恢复方法
     * 恢复WebView和全屏模式
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
        }
        setFullscreenMode();
    }

    /**
     * 活动暂停方法
     * 暂停WebView
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
        }
    }

    /**
     * 活动停止方法
     * 应用进入后台时调用，暂停壁纸轮播
     */
    @Override
    protected void onStop() {
        super.onStop();
        isAppInForeground = false;
        taskManager.pauseWallpaperCarousel();
        Log.d("MainActivity", "应用进入后台");
    }

    /**
     * 活动销毁方法
     * 清理所有资源，停止所有服务和定时器
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimeUpdate();
        taskManager.stopWallpaperCarousel();
        taskManager.stopMusicPlaybackCheck();
        taskManager.cancelDelayedStartupTasks();
        serviceManager.unbindAllServices();
        unregisterReceivers();

        // 释放TTS资源
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech = null;
        }

        // 销毁WebView
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
        instance = null;
    }

    /**
     * 返回键处理方法
     * 返回到系统桌面而不是退出应用
     */
    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "悬浮窗权限未授予", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_MANAGE_STORAGE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    createDirectoriesInRoot();
                } else {
                    Toast.makeText(this, "请授予所有文件访问权限", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1007) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("MainActivity", "录音权限已授予");
            } else {
                Log.d("MainActivity", "录音权限被拒绝");
            }
        }
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length >= 2 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                createDirectoriesInRoot();
            } else {
                Toast.makeText(this, "存储权限被拒绝，无法操作根目录", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 初始化数据库辅助类
     * 创建所有数据库辅助类的单例实例
     */
    private void initDatabaseHelpers() {
        dbHelper = AppDatabaseHelper.getInstance(this);
        quickAppDbHelper = QuickAppDatabaseHelper.getInstance(this);
        wallpaperDbHelper = WallpaperCategoryDatabaseHelper.getInstance(this);
        wallpaperSettingsDbHelper = WallpaperSettingsDatabaseHelper.getInstance(this);
        configAppDbHelper = ConfigAppDatabaseHelper.getInstance(this);
        componentConfigDbHelper = ComponentConfigDatabaseHelper.getInstance(this);
    }

    /**
     * 初始化壁纸设置更改广播接收器
     * 监听壁纸设置更改广播，在设置更改时更新壁纸轮播
     */
    public void initWallpaperSettingsReceiver() {
        wallpaperSettingsChangedReceiver = new WallpaperSettingsChangedReceiver();
        IntentFilter wallpaperSettingsFilter = new IntentFilter(ACTION_WALLPAPER_SETTINGS_CHANGED);
        registerReceiver(wallpaperSettingsChangedReceiver, wallpaperSettingsFilter);
    }

    /**
     * 初始化重启应用广播接收器
     * 监听重启应用广播，在收到广播时重启应用
     */
    public void initRestartAppReceiver() {
        RestartAppReceiver restartAppReceiver = new RestartAppReceiver();
        IntentFilter restartAppFilter = new IntentFilter("com.c11partner.desktop.RESTART_APP");
        registerReceiver(restartAppReceiver, restartAppFilter);
    }

    /**
     * 初始化WebView
     * 配置WebView设置，启用JavaScript，加载HTML界面
     */
    private void initWebView() {
        webView = new WebView(this);
        setContentView(webView);

        // 配置WebView设置
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        // 添加JavaScript接口
        webView.addJavascriptInterface(webViewBridge, "Android");

        // 设置WebView客户端
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 页面加载完成后注册时间更新监听器
                view.loadUrl("javascript:registerTimeUpdateListener()");
                // 初始化AC状态
                webViewBridge.initializeAcStatus();
            }
        });

        // 加载HTML页面
        webView.loadUrl("file:///android_asset/index.html");
    }

    /**
     * 初始化TTS语音合成
     * 创建TextToSpeech实例，设置语言和参数
     * 支持中文和英文，如果中文不可用则使用英文作为备选
     */
    public void initTextToSpeech() {
        Log.d("MainActivity", "开始初始化TTS");
        try {
            // 检查TTS引擎是否可用
            checkTTSEngines();

            // 创建TextToSpeech实例
            createTTSInstance();
        } catch (Exception e) {
            Log.e("MainActivity", "TTS初始化异常", e);
        }
    }

    /**
     * 检查TTS引擎是否可用
     */
    private void checkTTSEngines() {
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        PackageManager pm = getPackageManager();
        List<ResolveInfo> ttsEngines = pm.queryIntentActivities(checkTTSIntent, 0);
        Log.d("MainActivity", "可用的TTS引擎数量: " + ttsEngines.size());
        for (ResolveInfo info : ttsEngines) {
            Log.d("MainActivity", "TTS引擎: " + info.activityInfo.packageName);
        }
    }

    /**
     * 创建TTS实例并设置语言
     * 优先使用小米小爱TTS引擎，如果不可用则使用系统默认引擎
     */
    private void createTTSInstance() {
        // 尝试使用小米小爱TTS引擎
        String xiaomiTtsPackage = "com.xiaomi.mibrain.speech";
        
        // 检查小米小爱TTS引擎是否可用
        if (isTtsEngineAvailable(xiaomiTtsPackage)) {
            Log.d("MainActivity", "使用小米小爱TTS引擎: " + xiaomiTtsPackage);
            textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    Log.d("MainActivity", "TTS初始化回调，状态码: " + status);
                    if (status == TextToSpeech.SUCCESS) {
                        Log.d("MainActivity", "TTS初始化成功，开始设置语言");
                        setupTTSLanguage();
                    } else {
                        Log.e("MainActivity", "小米小爱TTS初始化失败，尝试使用系统默认引擎");
                        createDefaultTtsInstance();
                    }
                }
            }, xiaomiTtsPackage);
        } else {
            Log.d("MainActivity", "小米小爱TTS引擎不可用，使用系统默认引擎");
            createDefaultTtsInstance();
        }
    }

    /**
     * 创建默认TTS实例
     */
    private void createDefaultTtsInstance() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Log.d("MainActivity", "TTS初始化回调，状态码: " + status);
                if (status == TextToSpeech.SUCCESS) {
                    Log.d("MainActivity", "TTS初始化成功，开始设置语言");
                    setupTTSLanguage();
                } else {
                    handleTTSInitFailure(status);
                }
            }
        });
    }

    /**
     * 检查TTS引擎是否可用
     */
    private boolean isTtsEngineAvailable(String packageName) {
        try {
            Intent checkTTSIntent = new Intent();
            checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            checkTTSIntent.setPackage(packageName);
            PackageManager pm = getPackageManager();
            List<ResolveInfo> ttsEngines = pm.queryIntentActivities(checkTTSIntent, 0);
            return !ttsEngines.isEmpty();
        } catch (Exception e) {
            Log.e("MainActivity", "检查TTS引擎可用性时出错", e);
            return false;
        }
    }

    /**
     * 设置TTS语言
     */
    private void setupTTSLanguage() {
        // 尝试设置中文语言
        int result = textToSpeech.setLanguage(Locale.CHINESE);
        Log.d("MainActivity", "设置中文语言结果: " + result);

        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e("MainActivity", "TTS不支持中文");
            Toast.makeText(MainActivity.this, "TTS不支持中文", Toast.LENGTH_SHORT).show();

            // 尝试设置简体中文
            setupSimplifiedChineseLanguage();
        } else {
            Log.d("MainActivity", "TTS语言设置成功");
            configureTTSParams();
        }
    }

    /**
     * 设置简体中文语言
     */
    private void setupSimplifiedChineseLanguage() {
        Locale chineseLocale = Locale.SIMPLIFIED_CHINESE;
        int simplifiedResult = textToSpeech.setLanguage(chineseLocale);
        Log.d("MainActivity", "设置简体中文语言结果: " + simplifiedResult);

        if (simplifiedResult == TextToSpeech.LANG_MISSING_DATA || simplifiedResult == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e("MainActivity", "TTS不支持简体中文");
            setupEnglishLanguage();
        } else {
            Log.d("MainActivity", "TTS简体中文语言设置成功");
            configureTTSParams();
        }
    }

    /**
     * 设置英文语言作为备选
     */
    private void setupEnglishLanguage() {
        int englishResult = textToSpeech.setLanguage(Locale.ENGLISH);
        Log.d("MainActivity", "设置英文语言结果: " + englishResult);
        if (englishResult == TextToSpeech.LANG_AVAILABLE || englishResult == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
            Log.d("MainActivity", "TTS已设置为英文");
            configureTTSParams();
        } else {
            Log.e("MainActivity", "TTS也不支持英文");
            Toast.makeText(MainActivity.this, "TTS不支持任何语言", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 配置TTS参数
     */
    private void configureTTSParams() {
        textToSpeech.setSpeechRate(0.8f);
        textToSpeech.setPitch(1.0f);
        Log.d("MainActivity", "TTS全部初始化完成");
    }

    /**
     * 处理TTS初始化失败
     */
    private void handleTTSInitFailure(int status) {
        Log.e("MainActivity", "TTS初始化失败，状态码: " + status);
        // 尝试启动TTS设置界面
        Intent installTTSIntent = new Intent();
        installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        startActivity(installTTSIntent);
    }

    // 音乐相关方法

    /**
     * 初始化音乐可视化
     */
    public void initMusicVisualizer() {
        try {
            Log.d("MainActivity", "开始初始化音乐可视化");
            createMusicVisualizer();
            setupVisualizerListener();
            Log.d("MainActivity", "音乐可视化监听器设置成功");
        } catch (Exception e) {
            Log.e("MainActivity", "初始化音乐可视化时出错", e);
        }
    }

    /**
     * 创建音乐可视化对象
     */
    private void createMusicVisualizer() {
        musicVisualizer = new MusicVisualizer(this);
        Log.d("MainActivity", "MusicVisualizer对象创建成功");
    }

    /**
     * 设置音乐可视化监听器
     */
    private void setupVisualizerListener() {
        musicVisualizer.setOnVisualizerUpdateListener(new MusicVisualizer.OnVisualizerUpdateListener() {
            private long lastUpdate = 0;
            private static final long MIN_UPDATE_INTERVAL = 200;

            @Override
            public void onVisualizerUpdate(byte[] waveform, byte[] frequency) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastUpdate < MIN_UPDATE_INTERVAL) {
                    return;
                }
                lastUpdate = currentTime;

                if (webView != null && (waveform != null || frequency != null)) {
                    runOnUiThread(() -> updateVisualizationData(waveform, frequency));
                }
            }
        });
    }

    /**
     * 更新可视化数据并发送到前端
     */
    private void updateVisualizationData(byte[] waveform, byte[] frequency) {
        String data = generateVisualizationData(waveform, frequency);
        sendVisualizationDataToFrontend(data);
    }

    /**
     * 生成可视化数据
     */
    private String generateVisualizationData(byte[] waveform, byte[] frequency) {
        if (frequency != null && frequency.length > 0) {
            return generateFrequencyData(frequency);
        } else if (waveform != null && waveform.length > 0) {
            return generateWaveformData(waveform);
        }
        return "";
    }

    /**
     * 生成频率数据
     */
    private String generateFrequencyData(byte[] frequency) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < Math.min(frequency.length, 64); i++) {
            int adjustedValue = (frequency[i] & 0xFF) / 3;
            sb.append(adjustedValue);
            if (i < Math.min(frequency.length, 64) - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 生成波形数据
     */
    private String generateWaveformData(byte[] waveform) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < Math.min(waveform.length, 64); i++) {
            int adjustedValue = (waveform[i] & 0xFF) / 5;
            sb.append(adjustedValue);
            if (i < Math.min(waveform.length, 64) - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 发送可视化数据到前端
     */
    private void sendVisualizationDataToFrontend(String data) {
        String jsCode = "javascript:updateMusicVisualization('" + data + "')";
        webView.evaluateJavascript(jsCode, result -> {
            // Log.d("MainActivity", "JavaScript执行完成，返回结果: " + result);
        });
    }

    /**
     * 注册音乐播放广播接收器
     */
    private void registerMusicPlaybackReceiver() {
        musicPlaybackReceiver = new MusicPlaybackReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.android.music.metachanged");
        filter.addAction("com.android.music.playstatechanged");
        filter.addAction("com.android.music.playbackcomplete");
        filter.addAction("com.android.music.queuechanged");
        filter.addAction("com.htc.music.metachanged");
        filter.addAction("fm.last.android.metachanged");
        filter.addAction("com.sec.android.Music.metachanged");
        filter.addAction("com.nullsoft.winamp.metachanged");
        filter.addAction("com.amazon.mp3.metachanged");
        filter.addAction("com.miui.player.metachanged");
        filter.addAction("com.real.IMP.metachanged");
        filter.addAction("com.sonyericsson.music.metachanged");
        filter.addAction("com.rdio.android.metachanged");
        filter.addAction("com.samsung.MusicPlayer.metachanged");
        filter.addAction("com.andrew.apollo.metachanged");

        registerReceiver(musicPlaybackReceiver, filter);
    }

    /**
     * 启动音乐播放状态检查
     */
    public void startMusicPlaybackCheck() {
        taskManager.startMusicPlaybackCheck();
    }

    /**
     * 检查音乐播放状态
     */
    public void checkMusicPlaybackState() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                boolean isMusicActive = am.isMusicActive();
                updateMusicPlaybackState(isMusicActive);
            }
        } catch (Exception e) {
            Log.e("MainActivity", "检查音乐播放状态时出错", e);
        }
    }

    /**
     * 更新音乐播放状态
     */
    private void updateMusicPlaybackState(boolean isPlaying) {
        if (isPlaying != isMusicPlaying) {
            isMusicPlaying = isPlaying;

            if (webView != null) {
                runOnUiThread(() -> {
                    // 先检查函数是否存在，避免报错
                    String jsCode = "javascript:if(typeof window.updateMusicPlaybackState === 'function') { window.updateMusicPlaybackState("
                            + isPlaying + "); }";
                    webView.evaluateJavascript(jsCode, result -> {
                        // Log.d("MainActivity", "JavaScript执行完成，返回结果: " + result);
                    });
                });
            }

            if (isPlaying && musicVisualizer != null) {
                try {
                    Log.d("MainActivity", "音乐开始播放，启动音乐可视化");
                    musicVisualizer.startVisualizer();
                } catch (Exception e) {
                    Log.e("MainActivity", "启动音乐可视化时出错", e);
                }
            } else if (!isPlaying && musicVisualizer != null) {
                try {
                    Log.d("MainActivity", "音乐停止播放，停止音乐可视化");
                    musicVisualizer.stopVisualizer();
                } catch (Exception e) {
                    Log.e("MainActivity", "停止音乐可视化时出错", e);
                }
            }
        }
    }

    /**
     * 静态方法：更新音乐状态到前端
     * 供MediaSessionService调用
     * 
     * @param musicName 音乐名称
     * @param isPlaying 是否正在播放
     * @param currentPosition 当前播放位置（毫秒）
     * @param duration 总时长（毫秒）
     */
    public static void updateMusicStatus(String musicName, boolean isPlaying, long currentPosition, long duration) {
        if (instance != null && instance.webView != null) {
            instance.runOnUiThread(() -> {
                try {
                    // 构建音乐状态JSON
                    org.json.JSONObject musicStatus = new org.json.JSONObject();
                    musicStatus.put("musicName", musicName);
                    musicStatus.put("isPlaying", isPlaying);
                    musicStatus.put("currentPosition", currentPosition);
                    musicStatus.put("duration", duration);
                    
                    // 调用前端更新函数
                    String jsCode = "javascript:if(typeof window.updateMusicStatus === 'function') { window.updateMusicStatus(" 
                            + musicStatus.toString() + "); } else { console && console.log && console.log('updateMusicStatus function not found'); }";
                    instance.webView.evaluateJavascript(jsCode, result -> {
                        // Log.d("MainActivity", "音乐状态更新完成: " + result);
                    });
                } catch (Exception e) {
                    Log.e("MainActivity", "更新音乐状态到前端时出错", e);
                }
            });
        }
    }

    /**
     * 启动壁纸轮播
     * 从数据库读取壁纸轮播设置，根据设置启动定时器
     * 只有在应用在前台且启用壁纸轮播时才会启动
     */
    public void startWallpaperCarousel() {
        try {
            // 从数据库读取壁纸设置
            Map<String, Object> settings = wallpaperSettingsDbHelper.getAllSettings();
            isWallpaperCarouselEnabled = (boolean) settings.get("wallpaper_carousel");
            wallpaperSwitchInterval = (int) settings.get("switch_interval");
        } catch (Exception e) {
            Log.e("MainActivity", "获取壁纸设置时出错", e);
            isWallpaperCarouselEnabled = false;
            wallpaperSwitchInterval = 15000;
        }

        // 使用taskManager启动壁纸轮播
        taskManager.startWallpaperCarousel();
    }

    /**
     * 暂停壁纸轮播
     * 应用进入后台时调用，停止壁纸轮播定时器
     */
    public void pauseWallpaperCarousel() {
        taskManager.pauseWallpaperCarousel();
    }

    /**
     * 恢复壁纸轮播
     * 应用进入前台时调用，恢复壁纸轮播
     */
    public void resumeWallpaperCarousel() {
        if (isWallpaperCarouselEnabled) {
            startWallpaperCarousel();
            Log.d("MainActivity", "壁纸轮播已恢复（应用进入前台）");
        }
    }

    /**
     * 删除当前壁纸
     * 将当前壁纸移动到删除目录，实现壁纸删除功能
     * 
     * @return 是否删除成功
     */
    public boolean deleteCurrentWallpaper() {
        try {
            // 检查是否可以删除壁纸
            if (!canDeleteCurrentWallpaper()) {
                return false;
            }

            // 准备删除目录
            File deletedDir = prepareDeletedDirectory();
            if (deletedDir == null) {
                return false;
            }

            // 执行删除操作
            return performWallpaperDeletion(deletedDir);
        } catch (Exception e) {
            Log.e("MainActivity", "删除壁纸时出错", e);
            return false;
        }
    }

    /**
     * 检查是否可以删除当前壁纸
     */
    private boolean canDeleteCurrentWallpaper() {
        // 检查是否使用默认壁纸
        if (isUsingDefaultWallpaper) {
            Log.d("MainActivity", "当前使用的是默认壁纸，无法删除");
            return false;
        }

        // 检查当前壁纸路径是否存在
        if (currentWallpaperPath == null || currentWallpaperPath.isEmpty()) {
            Log.d("MainActivity", "当前壁纸路径为空，无法删除");
            return false;
        }

        // 检查当前壁纸是否是本地指定的壁纸（保存在/sdcard/c11partner/目录下）
        File currentWallpaperFile = new File(currentWallpaperPath);
        if (!currentWallpaperFile.exists()) {
            Log.d("MainActivity", "当前壁纸文件不存在，无法删除");
            return false;
        }

        // 检查是否在fstart目录下
        String fstartPath = Environment.getExternalStorageDirectory() + "/c11partner";
        if (!currentWallpaperPath.startsWith(fstartPath)) {
            Log.d("MainActivity", "当前壁纸不是本地指定的壁纸，无法删除");
            return false;
        }

        return true;
    }

    /**
     * 准备删除目录
     */
    private File prepareDeletedDirectory() {
        File deletedDir = new File(Environment.getExternalStorageDirectory(), "fstart_deleted");
        if (!deletedDir.exists()) {
            if (!deletedDir.mkdirs()) {
                Log.e("MainActivity", "无法创建删除目录");
                return null;
            }
        }
        return deletedDir;
    }

    /**
     * 执行壁纸删除操作
     */
    private boolean performWallpaperDeletion(File deletedDir) {
        File currentWallpaperFile = new File(currentWallpaperPath);

        // 生成新的文件名（使用时间戳确保唯一性）
        String fileName = System.currentTimeMillis() + "_" + currentWallpaperFile.getName();
        File deletedFile = new File(deletedDir, fileName);

        // 将壁纸文件移动到删除目录
        boolean success = currentWallpaperFile.renameTo(deletedFile);
        if (success) {
            Log.d("MainActivity", "壁纸已成功删除并移动到: " + deletedFile.getAbsolutePath());
            // 清除当前壁纸路径
            currentWallpaperPath = "";
            return true;
        } else {
            Log.e("MainActivity", "壁纸删除失败");
            return false;
        }
    }

    /**
     * 开始更新时间显示
     * 启动时间更新任务，每秒更新一次
     */
    public void startTimeUpdate() {
        timeUpdateHandler.post(timeUpdateRunnable);
    }

    /**
     * 初始化配置（只在应用启动时执行一次）
     * 从数据库读取配置，包括桌面自启和开机问候设置
     */
    private void initSettings() {
        try {
            Map<String, Object> settings = wallpaperSettingsDbHelper.getAllSettings();
            bydAutoStartEnabled = (boolean) settings.getOrDefault("byd_auto_start", false);
            bootGreetingEnabled = (boolean) settings.getOrDefault("boot_greeting", false);
            Log.d("MainActivity", "初始化配置完成 - bydAutoStartEnabled: " + bydAutoStartEnabled + ", bootGreetingEnabled: "
                    + bootGreetingEnabled);
        } catch (Exception e) {
            Log.e("MainActivity", "初始化配置时出错", e);
            bydAutoStartEnabled = false;
            bootGreetingEnabled = false;
        }
    }

    /**
     * 预加载应用列表
     * 在应用启动时后台预加载应用列表，减少首次打开应用列表的等待时间
     * 预加载完成后将应用列表传递给前端JavaScript缓存
     */
    private void preloadAppList() {
        new Thread(() -> {
            try {
                long startTime = System.currentTimeMillis();
                Log.d("MainActivity", "开始预加载应用列表");

                // 创建WebViewBridge实例并调用getAppList()方法
                WebViewBridge webViewBridge = new WebViewBridge(this, this);
                String appList = webViewBridge.getAppList();

                // 存储预加载的应用列表
                preloadedAppList = appList;
                Log.d("MainActivity", "应用列表已存储到缓存");

                long endTime = System.currentTimeMillis();
                Log.d("MainActivity", "应用列表预加载完成，耗时: " + (endTime - startTime) + "ms");
                Log.d("MainActivity", "预加载的应用列表长度: " + (appList != null ? appList.length() : 0));

                // 当WebView加载完成后，将预加载的应用列表传递给前端
                if (webView != null && !appList.isEmpty()) {
                    runOnUiThread(() -> {
                        webView.post(() -> {
                            try {
                                String javascript = String.format("javascript:setAppListCache('%s')",
                                        appList.replace("'", "\\'"));
                                webView.loadUrl(javascript);
                                Log.d("MainActivity", "已将预加载的应用列表传递给前端");
                            } catch (Exception e) {
                                Log.e("MainActivity", "传递预加载应用列表时出错", e);
                            }
                        });
                    });
                }
            } catch (Exception e) {
                Log.e("MainActivity", "预加载应用列表时出错", e);
            }
        }).start();
    }

    /**
     * 安排延迟启动任务
     * 根据配置安排30秒后执行的任务：
     * 1. 启动原桌面（如果启用了桌面自启）
     * 2. 播放开机问候语（如果启用了开机问候）
     */
    public void scheduleDelayedStartupTasks() {
        taskManager.scheduleDelayedStartupTasks();
    }

    public void requestAudioPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.RECORD_AUDIO }, 1007);
        }
    }

    public void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                showManageStorageDialog();
            } else {
                createDirectoriesInRoot();
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE }, REQUEST_STORAGE_PERMISSION);
            } else {
                createDirectoriesInRoot();
            }
        } else {
            createDirectoriesInRoot();
        }
    }

    private void showManageStorageDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
            createDirectoriesInRoot();
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("存储权限必要")
                        .setMessage("需要存储权限以保存应用配置和壁纸")
                        .setPositiveButton("继续申请", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                    try {
                                        Intent intent = new Intent(
                                                Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivityForResult(intent, REQUEST_MANAGE_STORAGE);
                                    } catch (Exception e) {
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                        startActivityForResult(intent, REQUEST_MANAGE_STORAGE);
                                    }
                                } else {
                                    ActivityCompat.requestPermissions(MainActivity.this,
                                            new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                    Manifest.permission.READ_EXTERNAL_STORAGE },
                                            REQUEST_STORAGE_PERMISSION);
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this, "未获得存储权限，部分功能可能无法正常使用", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });
    }

    private void createDirectoriesInRoot() {
        File rootDir = Environment.getExternalStorageDirectory();
        Log.d("Storage", "外部存储根目录: " + rootDir.getAbsolutePath());

        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.e("Storage", "外部存储不可用");
            Toast.makeText(this, "外部存储不可用", Toast.LENGTH_SHORT).show();
            return;
        }

        File targetDir = new File(rootDir, "c11partner");
        Log.d("Storage", "目标目录: " + targetDir.getAbsolutePath());

        File parentDir = targetDir.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            Log.d("Storage", "父目录不存在，尝试创建: " + parentDir.getAbsolutePath());
            boolean parentCreated = parentDir.mkdirs();
            if (!parentCreated) {
                Log.e("Storage", "父目录创建失败: " + parentDir.getAbsolutePath());
                Toast.makeText(this, "父目录创建失败: " + parentDir.getAbsolutePath(), Toast.LENGTH_LONG).show();
                return;
            } else {
                Log.d("Storage", "父目录创建成功: " + parentDir.getAbsolutePath());
            }
        }

        if (!targetDir.exists()) {
            boolean created = targetDir.mkdirs();
            if (created) {
                Log.i("Storage", "目录创建成功：" + targetDir.getAbsolutePath());
                Toast.makeText(this, "目录创建成功：" + targetDir.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                createTestFile(targetDir);
            } else {
                Log.e("Storage", "目录创建失败：" + targetDir.getAbsolutePath());
                Toast.makeText(this, "目录创建失败：" + targetDir.getAbsolutePath() + "，请检查权限", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.i("Storage", "目录已存在：" + targetDir.getAbsolutePath());
        }
    }

    private void createTestFile(File parentDir) {
        File testFile = new File(parentDir, "test.txt");
        try {
            boolean created = testFile.createNewFile();
            if (created) {
                try (FileWriter writer = new FileWriter(testFile)) {
                    writer.write("This is a test file in root directory.");
                }
                Log.i("Storage", "测试文件创建成功：" + testFile.getAbsolutePath());
            } else {
                Log.w("Storage", "测试文件已存在或创建失败：" + testFile.getAbsolutePath());
            }
        } catch (IOException e) {
            Log.e("Storage", "测试文件创建失败：" + testFile.getAbsolutePath(), e);
            Toast.makeText(this, "测试文件创建失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 播放语音文本
     * 使用TTS引擎将文本转换为语音并播放
     * 
     * @param text 要播放的文本
     */
    private void speakText(String text) {
        if (textToSpeech == null) {
            Log.e("MainActivity", "TTS未初始化，无法播放语音");
            return;
        }

        // 如果TTS正在播放，跳过本次请求
        if (textToSpeech.isSpeaking()) {
            Log.d("MainActivity", "TTS正在播放，跳过本次请求");
            return;
        }

        try {
            int result = textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            if (result == TextToSpeech.SUCCESS) {
                Log.d("MainActivity", "TTS播放请求成功: " + text);
            } else {
                Log.e("MainActivity", "TTS播放请求失败，错误码: " + result);
            }
        } catch (Exception e) {
            Log.e("MainActivity", "TTS播放异常", e);
        }
    }

    /**
     * 播放随机问候语
     * 从API获取每日一句问候语，并使用TTS播放
     */
    public void playRandomGreeting() {
        if (quoteApiUtils != null) {
            quoteApiUtils.getDailyQuote(new QuoteApiUtils.QuoteListener() {
                @Override
                public void onQuoteReceived(String quote) {
                    if (quote != null && !quote.isEmpty()) {
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, quote, Toast.LENGTH_LONG).show();
                            Log.d("MainActivity", "播放问候语: " + quote);
                            speakText(quote);
                        });
                    }
                }
            });
        }
    }

    /**
     * 启动原桌面
     * 使用ADB命令后台启动com.android.launcher3，不显示界面
     */
    public void launchBydHome() {
        new Thread(() -> {
            try {
                String packageName = "com.android.launcher3";
                String activityName = "com.android.launcher3.Launcher";

                // 使用 ADB 命令启动原桌面进程（后台启动，不显示界面）
                String command = "am start -n " + packageName + "/" + activityName
                        + " -f 0x00000004 -f 0x00008000 -f 0x00000080 && input keyevent KEYCODE_HOME";

                Log.d("MainActivity", "执行ADB命令启动原桌面: " + command);

                // 使用ADB命令处理器执行命令
                AdbCommandProcessor processor = new AdbCommandProcessor(this);
                boolean success = processor.executeCommand(command);

                if (success) {
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "已启动原桌面", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "启动原桌面失败，无法连接ADB", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (Exception e) {
                Log.e("MainActivity", "启动原桌面失败", e);
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "启动原桌面失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    /**
     * 设置全屏模式
     * 隐藏系统导航栏和状态栏，实现沉浸式体验
     */
    private void setFullscreenMode() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    /**
     * 停止时间更新
     * 停止时间更新定时器
     */
    private void stopTimeUpdate() {
        timeUpdateHandler.removeCallbacks(timeUpdateRunnable);
    }

    /**
     * 停止壁纸轮播
     * 移除壁纸轮播的定时任务
     */
    public void stopWallpaperCarousel() {
        taskManager.stopWallpaperCarousel();
    }

    /**
     * 重启壁纸轮播
     * 重新启动壁纸轮播定时器
     */
    public void restartWallpaperCarousel() {
        startWallpaperCarousel();
    }

    /**
     * 停止音乐检查
     * 停止音乐播放状态检查定时器
     */
    private void stopMusicCheck() {
        taskManager.stopMusicPlaybackCheck();
    }

    /**
     * 取消延迟启动任务
     * 取消所有延迟启动任务
     */
    private void cancelDelayedStartupTasks() {
        taskManager.cancelDelayedStartupTasks();
    }

    /**
     * 解绑服务
     * 解绑所有已绑定的服务
     */
    private void unbindServices() {
        serviceManager.unbindAllServices();
    }

    /**
     * 注销广播接收器
     * 注销所有已注册的广播接收器
     */
    private void unregisterReceivers() {
        if (musicPlaybackReceiver != null) {
            try {
                unregisterReceiver(musicPlaybackReceiver);
            } catch (Exception e) {
                Log.e("MainActivity", "注销音乐播放接收器时出错", e);
            }
        }

        if (wallpaperSettingsChangedReceiver != null) {
            try {
                unregisterReceiver(wallpaperSettingsChangedReceiver);
            } catch (Exception e) {
                Log.e("MainActivity", "注销壁纸设置更改接收器时出错", e);
            }
        }
    }

    /**
     * 重新安排延迟启动任务
     * 取消现有任务并重新安排
     */
    public void rescheduleDelayedStartupTasks() {
        if (delayedStartHandler != null) {
            if (bydAutoStartRunnable != null) {
                delayedStartHandler.removeCallbacks(bydAutoStartRunnable);
            }
            if (bootGreetingRunnable != null) {
                delayedStartHandler.removeCallbacks(bootGreetingRunnable);
            }
        }
        scheduleDelayedStartupTasks();
    }

    /**
     * 发送壁纸设置更改广播
     * 通知其他组件壁纸设置已更改
     */
    public void sendWallpaperSettingsChangedBroadcast() {
        try {
            Intent intent = new Intent(ACTION_WALLPAPER_SETTINGS_CHANGED);
            sendBroadcast(intent);
            Log.d("MainActivity", "已发送壁纸设置更改广播");
        } catch (Exception e) {
            Log.e("MainActivity", "发送壁纸设置更改广播时出错", e);
        }
    }

    /**
     * 获取星期几
     * 
     * @param date 日期对象
     * @return 星期几的字符串表示
     */
    private String getWeekDay(java.util.Date date) {
        String[] weekdays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        int weekIndex = cal.get(java.util.Calendar.DAY_OF_WEEK) - 1;
        return weekdays[weekIndex];
    }

    /**
     * 获取系统属性
     * 
     * @param key 属性键
     * @return 属性值
     */
    private String getSystemProperty(String key) {
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method method = clazz.getMethod("get", String.class);
            return (String) method.invoke(null, key);
        } catch (Exception e) {
            Log.e("MainActivity", "获取系统属性时出错", e);
            return "";
        }
    }

    private void initAppListToDatabase() {
        new Thread(() -> {
            try {
                List<Map<String, Object>> apps = AppUtils.getInstalledApps(MainActivity.this);
                dbHelper.clearApps();
                dbHelper.bulkInsertOrUpdateApps(apps);
                Log.d("MainActivity", "应用列表已初始化到数据库");
            } catch (Exception e) {
                Log.e("MainActivity", "初始化应用列表到数据库时出错", e);
            }
        }).start();
    }

    public boolean triggerUsbDebugAuthorization() {
        if (usbDebugConnection != null) {
            return usbDebugConnection.triggerUsbDebugAuthorization();
        }
        Log.e("MainActivity", "USB调试连接对象未初始化");
        return false;
    }

    /**
     * 媒体会话服务连接
     * 用于绑定和监听媒体会话服务，实现音乐播放控制
     */
    public ServiceConnection mediaSessionServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 服务连接成功，获取服务实例
            MediaSessionService.LocalBinder binder = (MediaSessionService.LocalBinder) service;
            mediaSessionService = binder.getService();
            isMediaSessionServiceBound = true;
            webViewBridge.setMediaSessionService(mediaSessionService, true);
            Log.d("MainActivity", "媒体会话服务已绑定");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 服务连接断开
            isMediaSessionServiceBound = false;
            webViewBridge.setMediaSessionService(null, false);
            Log.d("MainActivity", "媒体会话服务已断开");
        }
    };

    public class MusicPlaybackReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("MainActivity", "收到音乐播放广播: " + action);
            checkMusicPlaybackState();
        }
    }

    public static class AppChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (instance != null) {
                instance.runOnUiThread(() -> {
                    Toast.makeText(context, "应用列表已更新", Toast.LENGTH_SHORT).show();
                });
            }
        }
    }

    public class WallpaperSettingsChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (instance != null) {
                Log.d("MainActivity", "收到壁纸设置更改广播");
                instance.restartWallpaperCarousel();
                instance.rescheduleDelayedStartupTasks();
            }
        }
    }

    public class RestartAppReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.c11partner.desktop.RESTART_APP".equals(intent.getAction())) {
                Log.d("MainActivity", "收到重启应用广播");
                Intent restartIntent = new Intent(context, MainActivity.class);
                restartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(restartIntent);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    }

    public class AndroidInterface {
        Context mContext;

        private final Map<String, String> appIconCache = new java.util.concurrent.ConcurrentHashMap<>();
        private final Map<String, Long> cacheTimestamps = new java.util.concurrent.ConcurrentHashMap<>();
        private static final long CACHE_EXPIRATION_TIME = 5 * 60 * 1000;

        private String cachedAppList = null;
        private long cachedAppListTime = 0;
        private static final long CACHE_DURATION = 30 * 1000;

        public AndroidInterface(Context c) {
            mContext = c;
        }

        private String getCachedAppIconBase64(String packageName) {
            if (appIconCache.containsKey(packageName)) {
                long currentTime = System.currentTimeMillis();
                long cacheTime = cacheTimestamps.getOrDefault(packageName, 0L);
                if (currentTime - cacheTime < CACHE_EXPIRATION_TIME) {
                    return appIconCache.get(packageName);
                } else {
                    appIconCache.remove(packageName);
                    cacheTimestamps.remove(packageName);
                }
            }
            return null;
        }

        private void cacheAppIconBase64(String packageName, String iconBase64) {
            appIconCache.put(packageName, iconBase64);
            cacheTimestamps.put(packageName, System.currentTimeMillis());
        }

        private void cleanExpiredCache() {
            long currentTime = System.currentTimeMillis();
            Iterator<Map.Entry<String, Long>> iterator = cacheTimestamps.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Long> entry = iterator.next();
                if (currentTime - entry.getValue() >= CACHE_EXPIRATION_TIME) {
                    iterator.remove();
                    appIconCache.remove(entry.getKey());
                }
            }
        }

        private void clearAppIconCache() {
            appIconCache.clear();
            cacheTimestamps.clear();
        }

        private String getRandomLocalWallpaper() {
            try {
                Map<String, Object> settings = wallpaperSettingsDbHelper.getAllSettings();
                boolean isRandomMode = (boolean) settings.get("random_mode");
                boolean isSpecifiedMode = (boolean) settings.get("specified_mode");

                if (isRandomMode) {
                    return getRandomWallpaperFromFstartExcept00();
                }

                if (isSpecifiedMode) {
                    return getRandomWallpaperFromFstart00();
                }

                List<Map<String, Object>> enabledCategories = wallpaperDbHelper.getAllCategories();
                List<Map<String, Object>> filteredCategories = new ArrayList<>();
                for (Map<String, Object> category : enabledCategories) {
                    if ((boolean) category.get("enabled")) {
                        filteredCategories.add(category);
                    }
                }

                if (filteredCategories.isEmpty()) {
                    return null;
                }

                Random random = new Random();
                Map<String, Object> selectedCategory = filteredCategories
                        .get(random.nextInt(filteredCategories.size()));
                String categoryId = (String) selectedCategory.get("id");

                return WallpaperDownloadUtils.getRandomLocalWallpaper(MainActivity.this, categoryId);
            } catch (Exception e) {
                Log.e("MainActivity", "获取本地随机壁纸时出错", e);
                return null;
            }
        }

        private String getRandomWallpaperFromFstartExcept00() {
            try {
                File rootDir = Environment.getExternalStorageDirectory();
                File fstartDir = new File(rootDir, "c11partner");

                if (!fstartDir.exists() || !fstartDir.isDirectory()) {
                    return null;
                }

                File[] subDirs = fstartDir.listFiles(File::isDirectory);
                List<File> validDirs = new ArrayList<>();

                if (subDirs != null) {
                    for (File dir : subDirs) {
                        if (!"00".equals(dir.getName())) {
                            validDirs.add(dir);
                        }
                    }
                }

                if (validDirs.isEmpty()) {
                    return null;
                }

                List<String> wallpaperPaths = new ArrayList<>();

                for (File dir : validDirs) {
                    File[] files = dir.listFiles((fileDir, name) -> name.toLowerCase().endsWith(".jpg")
                            || name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpeg"));

                    if (files != null) {
                        for (File file : files) {
                            wallpaperPaths.add(file.getAbsolutePath());
                        }
                    }
                }

                if (wallpaperPaths.isEmpty()) {
                    return null;
                }

                Random random = new Random();
                return wallpaperPaths.get(random.nextInt(wallpaperPaths.size()));
            } catch (Exception e) {
                Log.e("MainActivity", "从fstart目录获取随机壁纸时出错", e);
                return null;
            }
        }

        private String getRandomWallpaperFromFstart00() {
            try {
                File rootDir = Environment.getExternalStorageDirectory();
                File fstartDir = new File(rootDir, "c11partner");
                File zeroDir = new File(fstartDir, "00");

                if (!zeroDir.exists() || !zeroDir.isDirectory()) {
                    return null;
                }

                File[] files = zeroDir.listFiles(
                        (fileDir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png")
                                || name.toLowerCase().endsWith(".jpeg") || name.toLowerCase().endsWith(".png_bak"));

                if (files == null || files.length == 0) {
                    return null;
                }

                Random random = new Random();
                File selectedFile = files[random.nextInt(files.length)];
                String filePath = selectedFile.getAbsolutePath();

                if (filePath.toLowerCase().endsWith(".png_bak")) {
                    filePath = filePath.replace(".png_bak", ".png");
                }

                return filePath;
            } catch (Exception e) {
                Log.e("MainActivity", "从fstart/00目录获取随机壁纸时出错", e);
                return null;
            }
        }

        private String getRandomOnlineWallpaper() {
            try {
                List<Map<String, Object>> enabledCategories = wallpaperDbHelper.getAllCategories();
                List<Map<String, Object>> filteredCategories = new ArrayList<>();

                for (Map<String, Object> category : enabledCategories) {
                    if ((boolean) category.get("enabled")) {
                        filteredCategories.add(category);
                    }
                }

                if (filteredCategories.isEmpty()) {
                    return "";
                }

                Random random = new Random();
                Map<String, Object> selectedCategory = filteredCategories
                        .get(random.nextInt(filteredCategories.size()));
                String categoryId = (String) selectedCategory.get("id");
                int count = (int) selectedCategory.get("count");
                int start = random.nextInt(count) + 1;

                String wallpaperUrl = getRandomWallpaperUrl(categoryId, start);

                if (wallpaperUrl != null && !wallpaperUrl.isEmpty()) {
                    String savedPath = WallpaperDownloadUtils.downloadAndSaveWallpaper(MainActivity.this, wallpaperUrl,
                            categoryId);

                    if (savedPath != null) {
                        Log.d("MainActivity", "壁纸已保存到: " + savedPath);
                        return "file://" + savedPath;
                    }
                }

                return wallpaperUrl;
            } catch (Exception e) {
                Log.e("MainActivity", "获取在线随机壁纸时出错", e);
                return "";
            }
        }

        private String getRandomOnlineWallpaperBase64() {
            try {
                List<Map<String, Object>> enabledCategories = wallpaperDbHelper.getAllCategories();
                List<Map<String, Object>> filteredCategories = new ArrayList<>();

                for (Map<String, Object> category : enabledCategories) {
                    if ((boolean) category.get("enabled")) {
                        filteredCategories.add(category);
                    }
                }

                if (filteredCategories.isEmpty()) {
                    return "";
                }

                Random random = new Random();
                Map<String, Object> selectedCategory = filteredCategories
                        .get(random.nextInt(filteredCategories.size()));
                String categoryId = (String) selectedCategory.get("id");
                int count = (int) selectedCategory.get("count");
                int start = random.nextInt(count) + 1;

                String wallpaperUrl = getRandomWallpaperUrl(categoryId, start);

                if (wallpaperUrl != null && !wallpaperUrl.isEmpty()) {
                    String base64Data = WallpaperDownloadUtils.downloadAndEncodeWallpaper(wallpaperUrl);
                    if (base64Data != null) {
                        new Thread(() -> {
                            String savedPath = WallpaperDownloadUtils.downloadAndSaveWallpaper(MainActivity.this,
                                    wallpaperUrl, categoryId);
                            if (savedPath != null) {
                                Log.d("MainActivity", "壁纸已保存到: " + savedPath);
                            }
                        }).start();

                        return base64Data;
                    }
                }
            } catch (Exception e) {
                Log.e("MainActivity", "获取在线随机壁纸Base64数据时出错", e);
            }
            return "";
        }

        private String encodeImageToBase64(String imagePath) {
            try {
                if (imagePath.startsWith("file://")) {
                    imagePath = imagePath.substring(7);
                }

                File imageFile = new File(imagePath);
                if (!imageFile.exists()) {
                    return "";
                }

                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                if (bitmap == null) {
                    return "";
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                byte[] imageBytes = baos.toByteArray();
                return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
            } catch (Exception e) {
                Log.e("MainActivity", "编码图片到Base64时出错", e);
                return "";
            }
        }

        private String getRandomWallpaperUrl(String categoryId, int start) {
            try {
                String apiUrl = "http://wallpaper.apc.360.cn/index.php?c=WallPaperAndroid&a=getAppsByCategory&cid="
                        + categoryId + "&start=" + start + "&count=1";

                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    if ("0".equals(jsonResponse.optString("errno"))) {
                        JSONArray dataArray = jsonResponse.getJSONArray("data");
                        if (dataArray.length() > 0) {
                            JSONObject wallpaperObj = dataArray.getJSONObject(0);
                            return wallpaperObj.getString("url");
                        }
                    }
                }

                connection.disconnect();
            } catch (Exception e) {
                Log.e("MainActivity", "获取壁纸URL时出错", e);
            }

            return "";
        }

        private String drawableToBase64(Drawable drawable) {
            if (drawable == null) {
                return "";
            }

            Bitmap bitmap = drawableToBitmap(drawable);

            if (bitmap != null) {
                bitmap = scaleBitmap(bitmap, 96, 96);
            }

            return bitmapToBase64(bitmap);
        }

        private Bitmap drawableToBitmap(Drawable drawable) {
            if (drawable == null) {
                return null;
            }

            if (drawable instanceof android.graphics.drawable.BitmapDrawable) {
                return ((android.graphics.drawable.BitmapDrawable) drawable).getBitmap();
            }

            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();

            if (width <= 0 || height <= 0) {
                width = 96;
                height = 96;
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            return bitmap;
        }

        private Bitmap scaleBitmap(Bitmap original, int targetWidth, int targetHeight) {
            if (original == null) {
                return null;
            }

            if (original.getWidth() <= targetWidth && original.getHeight() <= targetHeight) {
                return original;
            }

            return Bitmap.createScaledBitmap(original, targetWidth, targetHeight, true);
        }

        private String bitmapToBase64(Bitmap bitmap) {
            if (bitmap == null) {
                return "";
            }

            android.graphics.Bitmap.CompressFormat format = android.graphics.Bitmap.CompressFormat.PNG;
            int quality = 80;
            java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
            bitmap.compress(format, quality, byteArrayOutputStream);

            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.NO_WRAP);
        }

        private String getFirstLetter(String str) {
            if (str == null || str.isEmpty()) {
                return "#";
            }

            char firstChar = str.charAt(0);
            if ((firstChar >= 'A' && firstChar <= 'Z') || (firstChar >= 'a' && firstChar <= 'z')) {
                return String.valueOf(Character.toUpperCase(firstChar));
            } else if (isChineseChar(firstChar)) {
                return getChineseFirstLetter(firstChar);
            } else {
                return "#";
            }
        }

        private boolean isChineseChar(char c) {
            return (c >= 0x4E00 && c <= 0x9FFF) || (c >= 0x3400 && c <= 0x4DBF) || (c >= 0x20000 && c <= 0x2A6DF)
                    || (c >= 0x2A700 && c <= 0x2B73F) || (c >= 0x2B740 && c <= 0x2B81F)
                    || (c >= 0x2B820 && c <= 0x2CEAF);
        }

        private String getChineseFirstLetter(char ch) {
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            format.setVCharType(HanyuPinyinVCharType.WITH_V);

            try {
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(ch, format);

                if (pinyinArray != null && pinyinArray.length > 0) {
                    String pinyin = pinyinArray[0];
                    if (pinyin != null && pinyin.length() > 0) {
                        return String.valueOf(pinyin.charAt(0));
                    }
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                Log.e("MainActivity", "拼音转换出错", e);
            }

            return "#";
        }
    }
}
