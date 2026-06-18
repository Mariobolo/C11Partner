package com.c11partner.desktop.bridge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
import com.c11partner.desktop.adb.AdbCommandProcessor;

import com.c11partner.desktop.MainActivity;
import com.c11partner.desktop.database.AppDatabaseHelper;
import com.c11partner.desktop.database.ComponentConfigDatabaseHelper;
import com.c11partner.desktop.database.ConfigAppDatabaseHelper;
import com.c11partner.desktop.database.QuickAppDatabaseHelper;
import com.c11partner.desktop.database.WallpaperCategoryDatabaseHelper;
import com.c11partner.desktop.database.WallpaperSettingsDatabaseHelper;
import com.c11partner.desktop.service.MediaSessionService;
import com.c11partner.desktop.utils.AppUtils;
import com.c11partner.desktop.utils.WallpaperCategoryApiUtils;
import com.c11partner.desktop.utils.WallpaperDownloadUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * WebView与原生代码交互的桥梁类
 * 
 * 功能说明：
 * 1. 提供JavaScript与Java代码的交互接口
 * 2. 处理前端发起的各种请求（应用列表、壁纸、音乐等）
 * 3. 管理数据库操作和系统服务调用
 * 4. 实现缓存机制，提高性能
 * 
 * 主要功能：
 * - 应用管理：获取应用列表、启动应用、快速启动管理
 * - 壁纸管理：壁纸轮播、壁纸下载、壁纸分类
 * - 音乐控制：播放控制、上一首、下一首
 * - 系统功能：ADB命令、WiFi连接、蓝牙连接
 * - 配置管理：组件配置、壁纸设置
 */
public class WebViewBridge {
    private static final String TAG = "WebViewBridge";
    private Context mContext;
    private MainActivity mActivity;

    // 数据库帮助类
    private AppDatabaseHelper dbHelper;
    private QuickAppDatabaseHelper quickAppDbHelper;
    private WallpaperCategoryDatabaseHelper wallpaperDbHelper;
    private WallpaperSettingsDatabaseHelper wallpaperSettingsDbHelper;
    private ConfigAppDatabaseHelper configAppDbHelper;
    private ComponentConfigDatabaseHelper componentConfigDbHelper;

    // 媒体会话服务
    private MediaSessionService mediaSessionService;
    private boolean isMediaSessionServiceBound = false;

    // 音乐工具类
    private com.c11partner.desktop.utils.MusicUtils musicUtils;

    // USB调试连接
    private com.c11partner.desktop.adb.UsbDebugConnection usbDebugConnection;

    // 应用图标Base64缓存
    private final Map<String, String> appIconCache = new java.util.concurrent.ConcurrentHashMap<>();
    private final Map<String, Long> cacheTimestamps = new java.util.concurrent.ConcurrentHashMap<>();
    private static final long CACHE_EXPIRATION_TIME = 5 * 60 * 1000; // 5分钟缓存过期时间

    // 应用列表缓存
    private String cachedAppList = null;
    private long cachedAppListTime = 0;
    private static final long CACHE_DURATION = 5 * 60 * 1000; // 5分钟缓存

    /**
     * 构造函数
     * 初始化所有数据库帮助类和工具类
     *
     * @param activity MainActivity实例
     * @param context  应用上下文
     */
    public WebViewBridge(MainActivity activity, Context context) {
        this.mActivity = activity;
        this.mContext = context;

        // 初始化数据库帮助类
        this.dbHelper = AppDatabaseHelper.getInstance(context);
        this.quickAppDbHelper = QuickAppDatabaseHelper.getInstance(context);
        this.wallpaperDbHelper = WallpaperCategoryDatabaseHelper.getInstance(context);
        this.wallpaperSettingsDbHelper = WallpaperSettingsDatabaseHelper.getInstance(context);
        this.configAppDbHelper = ConfigAppDatabaseHelper.getInstance(context);
        this.componentConfigDbHelper = ComponentConfigDatabaseHelper.getInstance(context);

        // 初始化音乐工具类
        this.musicUtils = new com.c11partner.desktop.utils.MusicUtils(context);
        
        // 初始化USB调试连接
        this.usbDebugConnection = new com.c11partner.desktop.adb.UsbDebugConnection(context);
    }

    /**
     * 设置媒体会话服务
     *
     * @param service 媒体会话服务实例
     * @param isBound 是否已绑定
     */
    public void setMediaSessionService(MediaSessionService service, boolean isBound) {
        this.mediaSessionService = service;
        this.isMediaSessionServiceBound = isBound;
    }

    // ==================== 应用管理相关方法 ====================

    /**
     * 获取应用列表（按字母分组）
     * 使用缓存机制提高性能，缓存有效期为5分钟
     * 
     * @return JSON格式的应用列表，按字母分组
     */
    @JavascriptInterface
    public String getAppList() {
        // 检查缓存，如果缓存未过期则直接返回
        if (cachedAppList != null && (System.currentTimeMillis() - cachedAppListTime) < CACHE_DURATION) {
            Log.d(TAG, "使用缓存的应用列表");
            return cachedAppList;
        }

        // 添加耗时统计
        long startTime = System.currentTimeMillis();
        Log.d(TAG, "开始获取应用列表");

        // 获取应用列表（包含图标）
        long startGetAppsTime = System.currentTimeMillis();
        List<Map<String, Object>> allApps = AppUtils.getInstalledApps(mContext);
        long endGetAppsTime = System.currentTimeMillis();
        Log.d(TAG, "获取到 " + allApps.size() + " 个应用，耗时: " + (endGetAppsTime - startGetAppsTime) + "ms");

        // 创建按字母分组的应用列表
        JSONObject appsData = new JSONObject();

        try {
            // 按首字母分组应用
            long startGroupTime = System.currentTimeMillis();
            Map<String, List<Map<String, Object>>> groupedApps = new java.util.HashMap<>();

            for (Map<String, Object> app : allApps) {
                String name = (String) app.get("name");
                String letter = getFirstLetter(name);  // 获取首字母

                if (!groupedApps.containsKey(letter)) {
                    groupedApps.put(letter, new java.util.ArrayList<Map<String, Object>>());
                }
                groupedApps.get(letter).add(app);
            }
            long endGroupTime = System.currentTimeMillis();
            Log.d(TAG, "分组完成，耗时: " + (endGroupTime - startGroupTime) + "ms");

            // 对每个分组内的应用按名称排序
            long startSortTime = System.currentTimeMillis();
            for (List<Map<String, Object>> appList : groupedApps.values()) {
                Collections.sort(appList, new Comparator<Map<String, Object>>() {
                    @Override
                    public int compare(Map<String, Object> app1, Map<String, Object> app2) {
                        String name1 = (String) app1.get("name");
                        String name2 = (String) app2.get("name");
                        return name1.compareToIgnoreCase(name2);
                    }
                });
            }
            long endSortTime = System.currentTimeMillis();
            Log.d(TAG, "排序完成，耗时: " + (endSortTime - startSortTime) + "ms");

            // 创建排序后的字母列表，确保#排在第一位，然后是A-Z
            long startLetterSortTime = System.currentTimeMillis();
            List<String> sortedLetters = new ArrayList<>(groupedApps.keySet());
            Collections.sort(sortedLetters, (letter1, letter2) -> {
                // 如果letter1是#，排在前面
                if ("#".equals(letter1)) {
                    return -1;
                }
                // 如果letter2是#，排在后面
                if ("#".equals(letter2)) {
                    return 1;
                }
                // 其他情况按字母顺序排序
                return letter1.compareTo(letter2);
            });
            long endLetterSortTime = System.currentTimeMillis();
            Log.d(TAG, "字母排序完成，耗时: " + (endLetterSortTime - startLetterSortTime) + "ms");

            // 转换为JSON格式（按排序后的顺序）
            long startJsonTime = System.currentTimeMillis();
            for (String letter : sortedLetters) {
                List<Map<String, Object>> apps = groupedApps.get(letter);

                // 转换为JSON数组
                JSONArray appArray = new JSONArray();
                for (Map<String, Object> app : apps) {
                    JSONObject appObj = new JSONObject();
                    appObj.put("name", app.get("name"));
                    appObj.put("packageName", app.get("packageName"));

                    // 优化：使用缓存的图标Base64数据，避免重复转换
                    String iconBase64 = getCachedAppIconBase64((String) app.get("packageName"));
                    if (iconBase64 != null && !iconBase64.isEmpty()) {
                        appObj.put("icon", "data:image/png;base64," + iconBase64);
                    } else {
                        // 获取应用图标并转换为Base64
                        Drawable icon = (Drawable) app.get("icon");
                        iconBase64 = drawableToBase64(icon);
                        if (!iconBase64.isEmpty()) {
                            appObj.put("icon", "data:image/png;base64," + iconBase64);
                            // 缓存图标数据
                            cacheAppIconBase64((String) app.get("packageName"), iconBase64);
                        } else {
                            appObj.put("icon", "images/ic_launcher.png"); // 使用默认图标
                        }
                    }

                    appArray.put(appObj);
                }

                appsData.put(letter, appArray);
            }
            long endJsonTime = System.currentTimeMillis();
            Log.d(TAG, "JSON转换完成，耗时: " + (endJsonTime - startJsonTime) + "ms");

            // 缓存结果
            cachedAppList = appsData.toString();
            cachedAppListTime = System.currentTimeMillis();

            // 记录耗时
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            Log.d(TAG, "应用列表处理完成，总耗时: " + duration + "ms，分组数量: " + groupedApps.size());
        } catch (JSONException e) {
            Log.e(TAG, "获取应用列表时出错", e);
            // 返回空的JSON对象
            return "{}";
        } catch (OutOfMemoryError e) {
            Log.e(TAG, "内存不足", e);
            // 清除缓存并返回空结果
            clearAppIconCache();
            return "{}";
        } catch (Exception e) {
            Log.e(TAG, "获取应用列表时出现未预期的错误", e);
            return "{}";
        }

        return cachedAppList;
    }

    /**
     * 异步获取应用列表（按字母分组）
     *
     * @param callbackId 回调ID，用于JavaScript端识别回调
     */
    @JavascriptInterface
    public void getAppListAsync(final String callbackId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取应用列表数据
                final String result = getAppList();

                // 在UI线程中执行JavaScript回调
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mActivity.webView != null) {
                            // 使用JSONObject.quote()方法正确转义JSON字符串
                            String javascript = String.format(
                                    "javascript:window.handleGetAppListCallback('%s', %s)",
                                    callbackId, org.json.JSONObject.quote(result));
                            mActivity.webView.loadUrl(javascript);
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 异步保存壁纸轮播设置
     *
     * @param enabled    是否启用
     * @param callbackId 回调ID，用于JavaScript端识别回调
     */
    @JavascriptInterface
    public void saveWallpaperCarouselSettingAsync(final boolean enabled, final String callbackId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    wallpaperSettingsDbHelper.updateWallpaperCarousel(enabled);
                    // 重启壁纸轮播
                    mActivity.restartWallpaperCarousel();
                    // 发送壁纸设置更改广播
                    Intent intent = new Intent(MainActivity.ACTION_WALLPAPER_SETTINGS_CHANGED);
                    mActivity.sendBroadcast(intent);
                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.handleSaveWallpaperCarouselSettingCallback('%s', %s)",
                                        callbackId, "true");
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "保存壁纸轮播设置时出错", e);
                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.handleSaveWallpaperCarouselSettingCallback('%s', %s)",
                                        callbackId, "false");
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 保存壁纸轮播设置
     *
     * @param enabled 是否启用
     * @return 是否保存成功
     */
    @JavascriptInterface
    public boolean saveWallpaperCarouselSetting(boolean enabled) {
        try {
            wallpaperSettingsDbHelper.updateWallpaperCarousel(enabled);
            // 重启壁纸轮播
            mActivity.restartWallpaperCarousel();
            // 发送壁纸设置更改广播
            Intent intent = new Intent(MainActivity.ACTION_WALLPAPER_SETTINGS_CHANGED);
            mActivity.sendBroadcast(intent);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "保存壁纸轮播设置时出错", e);
            return false;
        }
    }

    /**
     * 异步保存壁纸轮播时间间隔
     *
     * @param interval   时间间隔(毫秒)
     * @param callbackId 回调ID，用于JavaScript端识别回调
     */
    @JavascriptInterface
    public void saveWallpaperSwitchIntervalAsync(final int interval, final String callbackId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    wallpaperSettingsDbHelper.updateSwitchInterval(interval);
                    // 重启壁纸轮播
                    mActivity.restartWallpaperCarousel();
                    // 发送壁纸设置更改广播
                    Intent intent = new Intent(MainActivity.ACTION_WALLPAPER_SETTINGS_CHANGED);
                    mActivity.sendBroadcast(intent);
                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.handleSaveWallpaperSwitchIntervalCallback('%s', %s)",
                                        callbackId, "true");
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "保存壁纸轮播时间间隔时出错", e);
                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.handleSaveWallpaperSwitchIntervalCallback('%s', %s)",
                                        callbackId, "false");
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 保存壁纸轮播时间间隔
     *
     * @param interval 时间间隔(毫秒)
     * @return 是否保存成功
     */
    @JavascriptInterface
    public boolean saveWallpaperSwitchInterval(int interval) {
        try {
            wallpaperSettingsDbHelper.updateSwitchInterval(interval);
            // 重启壁纸轮播
            mActivity.restartWallpaperCarousel();
            // 发送壁纸设置更改广播
            Intent intent = new Intent(MainActivity.ACTION_WALLPAPER_SETTINGS_CHANGED);
            mActivity.sendBroadcast(intent);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "保存壁纸轮播时间间隔时出错", e);
            return false;
        }
    }

    /**
     * 发送壁纸设置更改广播
     */
    @JavascriptInterface
    public void sendWallpaperSettingsChangedBroadcast() {
        try {
            Intent intent = new Intent(MainActivity.ACTION_WALLPAPER_SETTINGS_CHANGED);
            mContext.sendBroadcast(intent);
            Log.d(TAG, "已发送壁纸设置更改广播");
        } catch (Exception e) {
            Log.e(TAG, "发送壁纸设置更改广播时出错", e);
        }
    }

    /**
     * 获取随机壁纸URL
     *
     * @return 壁纸URL或本地文件路径
     */
    @JavascriptInterface
    public String getRandomWallpaper() {
        try {
            // 检查是否启用了本地壁纸分类模式
            boolean isRandomMode = wallpaperSettingsDbHelper.getAllSettings().get("random_mode").equals(true);
            boolean isSpecifiedMode = wallpaperSettingsDbHelper.getAllSettings().get("specified_mode").equals(true);

            // 如果启用了随机模式，读取SD卡下fstart目录下除了00文件夹下的图片
            if (isRandomMode) {
                String wallpaperPath = getRandomWallpaperFromFstartExcept00();
                // 更新MainActivity中的壁纸状态
                if (mActivity != null && wallpaperPath != null) {
                    mActivity.isUsingDefaultWallpaper = false;
                    mActivity.currentWallpaperPath = wallpaperPath;
                }
                return wallpaperPath;
            }

            // 如果启用了指定模式，读取SD卡下fstart目录下00文件夹下的图片
            if (isSpecifiedMode) {
                String wallpaperPath = getRandomWallpaperFromFstart00();
                // 更新MainActivity中的壁纸状态
                if (mActivity != null && wallpaperPath != null) {
                    mActivity.isUsingDefaultWallpaper = false;
                    mActivity.currentWallpaperPath = wallpaperPath;
                }
                return wallpaperPath;
            }

            // 如果都没有启用，获取已启用的分类
            List<Map<String, Object>> enabledCategories = wallpaperDbHelper.getAllCategories();
            Log.d(TAG, "获取到的所有分类数量: " + enabledCategories.size());

            // 过滤出已启用的分类
            List<Map<String, Object>> filteredCategories = new ArrayList<>();
            for (Map<String, Object> category : enabledCategories) {
                if ((boolean) category.get("enabled")) {
                    filteredCategories.add(category);
                    Log.d(TAG, "已启用的分类: " + category.get("id"));
                }
            }

            // 如果没有启用的分类，返回null
            if (filteredCategories.isEmpty()) {
                Log.d(TAG, "没有已启用的分类");
                return null;
            }

            // 随机选择一个分类
            Random random = new Random();
            Map<String, Object> selectedCategory = filteredCategories.get(random.nextInt(filteredCategories.size()));

            // 获取分类ID
            String categoryId = (String) selectedCategory.get("id");
            Log.d(TAG, "随机选择的分类ID: " + categoryId);

            // 从本地获取该分类的随机壁纸
            String wallpaperPath = WallpaperDownloadUtils.getRandomLocalWallpaper(mContext, categoryId);
            Log.d(TAG, "获取到的壁纸路径: " + wallpaperPath);
            // 更新MainActivity中的壁纸状态
            if (mActivity != null && wallpaperPath != null) {
                mActivity.isUsingDefaultWallpaper = false;
                mActivity.currentWallpaperPath = wallpaperPath;
            }
            return wallpaperPath;
        } catch (Exception e) {
            Log.e(TAG, "获取随机壁纸时出错", e);
            return null;
        }
    }

    /**
     * 获取本地随机壁纸
     *
     * @return 壁纸文件路径，如果没有壁纸则返回null
     */
    private String getRandomLocalWallpaper() {
        try {
            // 获取壁纸设置
            Map<String, Object> settings = wallpaperSettingsDbHelper.getAllSettings();
            boolean isRandomMode = settings.get("random_mode").equals(true);
            boolean isSpecifiedMode = settings.get("specified_mode").equals(true);

            // 如果启用了随机模式，读取SD卡下fstart目录下除了00文件夹下的图片
            if (isRandomMode) {
                return getRandomWallpaperFromFstartExcept00();
            }

            // 如果启用了指定模式，读取SD卡下fstart目录下00文件夹下的图片
            if (isSpecifiedMode) {
                return getRandomWallpaperFromFstart00();
            }

            // 如果都没有启用，使用默认逻辑
            // 获取已启用的分类
            List<Map<String, Object>> enabledCategories = wallpaperDbHelper.getAllCategories();

            // 过滤出已启用的分类
            List<Map<String, Object>> filteredCategories = new ArrayList<>();
            for (Map<String, Object> category : enabledCategories) {
                if ((boolean) category.get("enabled")) {
                    filteredCategories.add(category);
                }
            }

            // 如果没有启用的分类，返回null
            if (filteredCategories.isEmpty()) {
                return null;
            }

            // 随机选择一个分类
            Random random = new Random();
            Map<String, Object> selectedCategory = filteredCategories.get(random.nextInt(filteredCategories.size()));

            // 获取分类ID
            String categoryId = (String) selectedCategory.get("id");

            // 从本地获取该分类的随机壁纸
            return WallpaperDownloadUtils.getRandomLocalWallpaper(mContext, categoryId);
        } catch (Exception e) {
            Log.e(TAG, "获取本地随机壁纸时出错", e);
            return null;
        }
    }

    /**
     * 从SD卡下fstart目录下除了00文件夹下的图片中随机选择一张
     *
     * @return 壁纸文件路径，如果没有壁纸则返回null
     */
    private String getRandomWallpaperFromFstartExcept00() {
        try {
            File rootDir = Environment.getExternalStorageDirectory();
            File fstartDir = new File(rootDir, "c11partner");

            // 检查fstart目录是否存在
            if (!fstartDir.exists() || !fstartDir.isDirectory()) {
                return null;
            }

            // 获取所有子目录，除了00
            File[] subDirs = fstartDir.listFiles(File::isDirectory);
            List<File> validDirs = new ArrayList<>();

            if (subDirs != null) {
                for (File dir : subDirs) {
                    // 排除00文件夹
                    if (!"00".equals(dir.getName())) {
                        validDirs.add(dir);
                    }
                }
            }

            // 如果没有有效的目录，返回null
            if (validDirs.isEmpty()) {
                return null;
            }

            // 收集所有有效的图片文件
            List<String> wallpaperPaths = new ArrayList<>();

            for (File dir : validDirs) {
                File[] files = dir.listFiles((fileDir, name) ->
                        name.toLowerCase().endsWith(".jpg") ||
                                name.toLowerCase().endsWith(".png") ||
                                name.toLowerCase().endsWith(".jpeg"));

                if (files != null) {
                    for (File file : files) {
                        wallpaperPaths.add(file.getAbsolutePath());
                    }
                }
            }

            // 如果没有图片文件，返回null
            if (wallpaperPaths.isEmpty()) {
                return null;
            }

            // 随机选择一张壁纸
            Random random = new Random();
            return wallpaperPaths.get(random.nextInt(wallpaperPaths.size()));
        } catch (Exception e) {
            Log.e(TAG, "从fstart目录获取随机壁纸时出错", e);
            return null;
        }
    }

    /**
     * 从SD卡下fstart目录下00文件夹下的图片中随机选择一张
     *
     * @return 壁纸文件路径，如果没有壁纸则返回null
     */
    private String getRandomWallpaperFromFstart00() {
        try {
            File rootDir = Environment.getExternalStorageDirectory();
            File fstartDir = new File(rootDir, "c11partner");
            File zeroDir = new File(fstartDir, "00");

            // 检查00目录是否存在
            if (!zeroDir.exists() || !zeroDir.isDirectory()) {
                return null;
            }

            // 获取00目录下的所有图片文件
            File[] files = zeroDir.listFiles((fileDir, name) ->
                    name.toLowerCase().endsWith(".jpg") ||
                            name.toLowerCase().endsWith(".png") ||
                            name.toLowerCase().endsWith(".jpeg") ||
                            name.toLowerCase().endsWith(".png_bak")
            );

            // 如果没有图片文件，返回null
            if (files == null || files.length == 0) {
                return null;
            }

            // 随机选择一个文件
            Random random = new Random();
            File selectedFile = files[random.nextInt(files.length)];
            String filePath = selectedFile.getAbsolutePath();

            // 若文件是.png_bak格式，替换为.png
            /*if (filePath.toLowerCase().endsWith(".png_bak")) {
                filePath = filePath.replace(".png_bak", ".png");
            }*/

            return filePath;
        } catch (Exception e) {
            Log.e(TAG, "从fstart/00目录获取随机壁纸时出错", e);
            return null;
        }
    }

    /**
     * 获取在线随机壁纸并直接返回Base64编码的图片数据
     *
     * @return Base64编码的图片数据或URL
     */
    private String getRandomOnlineWallpaper() {
        try {
            // 获取已启用的分类
            List<Map<String, Object>> enabledCategories = wallpaperDbHelper.getAllCategories();
            List<Map<String, Object>> filteredCategories = new ArrayList<>();

            // 过滤出已启用的分类
            for (Map<String, Object> category : enabledCategories) {
                if ((boolean) category.get("enabled")) {
                    filteredCategories.add(category);
                }
            }

            // 如果没有启用的分类，返回空字符串
            if (filteredCategories.isEmpty()) {
                return "";
            }

            // 随机选择一个分类
            Random random = new Random();
            Map<String, Object> selectedCategory = filteredCategories.get(random.nextInt(filteredCategories.size()));

            // 获取分类ID和数量
            String categoryId = (String) selectedCategory.get("id");
            int count = (int) selectedCategory.get("count");

            // 随机生成起始位置
            int start = random.nextInt(count) + 1;

            // 调用工具类获取壁纸URL
            String wallpaperUrl = getRandomWallpaperUrl(categoryId, start);

            // 如果获取到URL，下载并保存壁纸
            if (wallpaperUrl != null && !wallpaperUrl.isEmpty()) {
                // 先尝试下载壁纸并保存到本地
                String savedPath = WallpaperDownloadUtils.downloadAndSaveWallpaper(
                        mContext, wallpaperUrl, categoryId);

                if (savedPath != null) {
                    Log.d(TAG, "壁纸已保存到: " + savedPath);
                    // 返回本地文件路径而不是网络URL
                    return "file://" + savedPath;
                }
            }

            return wallpaperUrl;
        } catch (Exception e) {
            Log.e(TAG, "获取在线随机壁纸时出错", e);
            return "";
        }
    }

    /**
     * 获取在线随机壁纸的Base64编码数据
     *
     * @return Base64编码的图片数据
     */
    private String getRandomOnlineWallpaperBase64() {
        try {
            // 获取已启用的分类
            List<Map<String, Object>> enabledCategories = wallpaperDbHelper.getAllCategories();
            List<Map<String, Object>> filteredCategories = new ArrayList<>();

            // 过滤出已启用的分类
            for (Map<String, Object> category : enabledCategories) {
                if ((boolean) category.get("enabled")) {
                    filteredCategories.add(category);
                }
            }

            // 如果没有启用的分类，返回空字符串
            if (filteredCategories.isEmpty()) {
                return "";
            }

            // 随机选择一个分类
            Random random = new Random();
            Map<String, Object> selectedCategory = filteredCategories.get(random.nextInt(filteredCategories.size()));

            // 获取分类ID和数量
            String categoryId = (String) selectedCategory.get("id");
            int count = (int) selectedCategory.get("count");

            // 随机生成起始位置
            int start = random.nextInt(count) + 1;

            // 调用工具类获取壁纸URL
            String wallpaperUrl = getRandomWallpaperUrl(categoryId, start);

            // 如果获取到URL，下载并转换为Base64
            if (wallpaperUrl != null && !wallpaperUrl.isEmpty()) {
                // 下载壁纸并转换为Base64
                return WallpaperDownloadUtils.downloadAndEncodeToBase64(wallpaperUrl);
            }

            return "";
        } catch (Exception e) {
            Log.e(TAG, "获取在线随机壁纸Base64时出错", e);
            return "";
        }
    }

    /**
     * 获取随机壁纸URL
     *
     * @param categoryId 分类ID
     * @param start      起始位置
     * @return 壁纸URL
     */
    private String getRandomWallpaperUrl(String categoryId, int start) {
        try {
            return WallpaperCategoryApiUtils.fetchWallpaperUrl(categoryId, start);
        } catch (Exception e) {
            Log.e(TAG, "获取随机壁纸URL时出错", e);
            return "";
        }
    }

    /**
     * 将图片文件转换为Base64编码
     *
     * @param imagePath 图片文件路径
     * @return Base64编码的图片数据
     */
    private String encodeImageToBase64(String imagePath) {
        try {
            return WallpaperDownloadUtils.encodeImageToBase64(imagePath);
        } catch (Exception e) {
            Log.e(TAG, "将图片转换为Base64时出错", e);
            return "";
        }
    }

    /**
     * 将Drawable转换为Base64编码的PNG图片
     *
     * @param drawable Drawable对象
     * @return Base64编码的PNG图片字符串
     */
    private String drawableToBase64(Drawable drawable) {
        if (drawable == null) {
            return ""; // 返回空字符串而不是null
        }

        // 将Drawable转换为Bitmap
        Bitmap bitmap = drawableToBitmap(drawable);

        // 优化：缩放图标以减少数据大小
        if (bitmap != null) {
            bitmap = scaleBitmap(bitmap, 96, 96); // 缩放到96x96像素
        }

        // 将Bitmap转换为Base64字符串
        return bitmapToBase64(bitmap);
    }

    /**
     * 将Drawable转换为Bitmap
     *
     * @param drawable Drawable对象
     * @return Bitmap对象
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        // 如果Drawable已经是BitmapDrawable，直接获取Bitmap
        if (drawable instanceof android.graphics.drawable.BitmapDrawable) {
            return ((android.graphics.drawable.BitmapDrawable) drawable).getBitmap();
        }

        // 优化：使用更合适的尺寸创建Bitmap
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        // 如果没有固有尺寸，使用默认尺寸
        if (width <= 0 || height <= 0) {
            width = 96;
            height = 96;
        }

        // 创建Bitmap
        Bitmap bitmap = Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.ARGB_8888
        );

        // 在Canvas上绘制Drawable
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * 缩放Bitmap到指定尺寸
     *
     * @param original     原始Bitmap
     * @param targetWidth  目标宽度
     * @param targetHeight 目标高度
     * @return 缩放后的Bitmap
     */
    private Bitmap scaleBitmap(Bitmap original, int targetWidth, int targetHeight) {
        if (original == null) {
            return null;
        }

        // 如果原始尺寸已经小于目标尺寸，直接返回
        if (original.getWidth() <= targetWidth && original.getHeight() <= targetHeight) {
            return original;
        }

        // 使用高质量的缩放算法
        return Bitmap.createScaledBitmap(original, targetWidth, targetHeight, true);
    }

    /**
     * 将Bitmap转换为Base64编码的PNG图片
     *
     * @param bitmap Bitmap对象
     * @return Base64编码的PNG图片字符串
     */
    private String bitmapToBase64(Bitmap bitmap) {
        if (bitmap == null) {
            return ""; // 返回空字符串而不是null
        }

        // 将Bitmap转换为字节数组
        android.graphics.Bitmap.CompressFormat format = android.graphics.Bitmap.CompressFormat.PNG;
        int quality = 80; // 降低质量以减少数据大小
        java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
        bitmap.compress(format, quality, byteArrayOutputStream);

        // 将字节数组转换为Base64字符串
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    /**
     * 获取字符串的首字母（改进的实现）
     *
     * @param str 字符串
     * @return 首字母
     */
    private String getFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return "#";
        }

        char firstChar = str.charAt(0);
        // 判断是否为英文字母
        if ((firstChar >= 'A' && firstChar <= 'Z') || (firstChar >= 'a' && firstChar <= 'z')) {
            return String.valueOf(Character.toUpperCase(firstChar));
        }
        // 判断是否为中文字符
        else if (isChineseChar(firstChar)) {
            // 获取中文字符的拼音首字母
            return getChineseFirstLetter(firstChar);
        }
        // 其他字符归类到#号
        else {
            return "#";
        }
    }

    /**
     * 判断字符是否为中文字符
     *
     * @param c 字符
     * @return 是否为中文字符
     */
    private boolean isChineseChar(char c) {
        return (c >= 0x4E00 && c <= 0x9FFF) || // 基本汉字
                (c >= 0x3400 && c <= 0x4DBF) || // 扩展A
                (c >= 0x20000 && c <= 0x2A6DF) || // 扩展B
                (c >= 0x2A700 && c <= 0x2B73F) || // 扩展C
                (c >= 0x2B740 && c <= 0x2B81F) || // 扩展D
                (c >= 0x2B820 && c <= 0x2CEAF);   // 扩展E
    }

    /**
     * 获取中文字符的拼音首字母
     *
     * @param c 中文字符
     * @return 拼音首字母
     */
    private String getChineseFirstLetter(char c) {
        try {
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            format.setVCharType(HanyuPinyinVCharType.WITH_V);

            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, format);
            if (pinyinArray != null && pinyinArray.length > 0) {
                return String.valueOf(pinyinArray[0].charAt(0));
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            Log.e(TAG, "获取中文字符拼音时出错", e);
        }
        return "#";
    }

    /**
     * 清除过期的图标缓存
     */
    private void clearAppIconCache() {
        long currentTime = System.currentTimeMillis();
        Iterator<Map.Entry<String, Long>> iterator = cacheTimestamps.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            if (currentTime - entry.getValue() > CACHE_EXPIRATION_TIME) {
                iterator.remove();
                appIconCache.remove(entry.getKey());
            }
        }
    }

    /**
     * 缓存应用图标Base64数据
     *
     * @param packageName 应用包名
     * @param iconBase64  图标Base64数据
     */
    private void cacheAppIconBase64(String packageName, String iconBase64) {
        appIconCache.put(packageName, iconBase64);
        cacheTimestamps.put(packageName, System.currentTimeMillis());
    }

    /**
     * 获取缓存的应用图标Base64数据
     *
     * @param packageName 应用包名
     * @return 图标Base64数据，如果缓存不存在或过期则返回null
     */
    private String getCachedAppIconBase64(String packageName) {
        Long timestamp = cacheTimestamps.get(packageName);
        if (timestamp != null && (System.currentTimeMillis() - timestamp) < CACHE_EXPIRATION_TIME) {
            return appIconCache.get(packageName);
        }
        return null;
    }

    /**
     * 通知前端更新壁纸
     */
    public void notifyWallpaperUpdate() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mActivity.webView != null) {
                    String javascript = "javascript:window.handleWallpaperUpdateNotification()";
                    mActivity.webView.loadUrl(javascript);
                }
            }
        });
    }

    /**
     * 启动应用
     *
     * @param packageName 应用包名
     */
    @JavascriptInterface
    public void launchApp(String packageName) {
        try {
            Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(packageName);
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            } else {
                Log.e(TAG, "无法找到应用: " + packageName);
            }
        } catch (Exception e) {
            Log.e(TAG, "启动应用时出错: " + packageName, e);
        }
    }

    /**
     * 暂停壁纸轮播
     */
    @JavascriptInterface
    public void pauseWallpaperCarousel() {
        try {
            if (mActivity != null) {
                mActivity.pauseWallpaperCarousel();
                Log.d(TAG, "壁纸轮播已暂停");
            }
        } catch (Exception e) {
            Log.e(TAG, "暂停壁纸轮播时出错", e);
        }
    }

    /**
     * 恢复壁纸轮播
     */
    @JavascriptInterface
    public void resumeWallpaperCarousel() {
        try {
            if (mActivity != null) {
                mActivity.resumeWallpaperCarousel();
                Log.d(TAG, "壁纸轮播已恢复");
            }
        } catch (Exception e) {
            Log.e(TAG, "恢复壁纸轮播时出错", e);
        }
    }

    /**
     * 删除当前壁纸
     * @return 是否删除成功
     */
    @JavascriptInterface
    public boolean deleteCurrentWallpaper() {
        try {
            if (mActivity != null) {
                return mActivity.deleteCurrentWallpaper();
            }
        } catch (Exception e) {
            Log.e(TAG, "删除壁纸时出错", e);
        }
        return false;
    }

    /**
     * 触发USB调试授权
     * 当用户点击"一键ADB授权"按钮时调用此方法，会在设备上弹出允许USB调试的授权弹窗
     */
    @JavascriptInterface
    public void triggerUsbDebugAuthorization() {
        new Thread(() -> {
            try {
                if (usbDebugConnection != null) {
                    // 检查是否有USB设备连接
                    if (usbDebugConnection.hasUsbDevice()) {
                        // 获取USB设备信息用于日志记录
                        String usbDeviceInfo = usbDebugConnection.getUsbDeviceInfo();
                        Log.d(TAG, "检测到USB设备:\n" + usbDeviceInfo);
                    } else {
                        Log.d(TAG, "未检测到USB设备，尝试本地ADB连接");
                    }
                    
                    // 触发ADB调试授权（支持USB和本地连接）
                    boolean result = usbDebugConnection.triggerUsbDebugAuthorization();
                    
                    if (result) {
                        Log.d(TAG, "成功触发ADB调试授权弹窗");
                        showToastOnUiThread("正在请求ADB调试授权，请在设备上确认");
                    } else {
                        Log.e(TAG, "触发ADB调试授权失败");
                        showToastOnUiThread("触发ADB调试授权失败");
                    }
                } else {
                    Log.e(TAG, "USB调试连接未初始化");
                    showToastOnUiThread("ADB调试连接未初始化");
                }
            } catch (Exception e) {
                Log.e(TAG, "触发ADB调试授权时发生异常", e);
                showToastOnUiThread("触发ADB调试授权时发生错误: " + e.getMessage());
            }
        }).start();
    }
    
    /**
     * 触发本地ADB授权
     * 为本地ADB连接设计的授权方法
     */
    @JavascriptInterface
    public void triggerWirelessAdbAuthorization() {
        new Thread(() -> {
            try {
                Log.d(TAG, "开始触发本地ADB授权");
                
                // 初始化ADB加密密钥
                com.c11partner.desktop.adb.AdbManager.setAppContext(mContext);
                com.c11partner.desktop.adb.AdbManager.initCrypto();
                
                // 尝试连接到本地ADB服务
                int[] commonPorts = {5555, 5554, 5556, 5557, 5558, 5559};
                boolean connected = false;
                int connectedPort = -1;
                
                for (int port : commonPorts) {
                    try {
                        Log.d(TAG, "尝试连接到本地ADB端口: " + port);
                        boolean result = com.c11partner.desktop.adb.AdbManager.connect("127.0.0.1", port);
                        if (result) {
                            Log.d(TAG, "本地ADB连接成功，端口: " + port);
                            connected = true;
                            connectedPort = port;
                            break;
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "连接端口 " + port + " 失败: " + e.getMessage());
                    }
                }
                
                if (connected) {
                    // 连接成功后执行授权命令
                    executeWirelessAdbAuthorization(connectedPort);
                    showToastOnUiThread("本地ADB连接成功，正在请求授权...");
                } else {
                    Log.e(TAG, "所有本地ADB端口都无法连接");
                    showToastOnUiThread("无法连接本地ADB，请检查:\n1. ADB调试是否已启用\n2. 设备是否已授权");
                }
            } catch (Exception e) {
                Log.e(TAG, "触发本地ADB授权时出错", e);
                showToastOnUiThread("本地ADB授权出错: " + e.getMessage());
            }
        }).start();
    }
    
    /**
     * 获取设备的WiFi IP地址
     */
    private String getDeviceIpAddress() {
        try {
            Log.d(TAG, "开始获取设备IP地址");
            
            // 方法1：尝试直接获取wlan0接口
            java.net.NetworkInterface networkInterface = java.net.NetworkInterface.getByName("wlan0");
            if (networkInterface == null) {
                Log.d(TAG, "wlan0接口不存在，尝试eth0");
                networkInterface = java.net.NetworkInterface.getByName("eth0");
            }
            
            if (networkInterface != null && networkInterface.isUp()) {
                Log.d(TAG, "找到网络接口: " + networkInterface.getName());
                java.util.Enumeration<java.net.InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    java.net.InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && address instanceof java.net.Inet4Address) {
                        String ip = address.getHostAddress();
                        Log.d(TAG, "获取到IP地址: " + ip);
                        return ip;
                    }
                }
            }
            
            Log.d(TAG, "方法1失败，尝试方法2");
            
            // 方法2：使用ConnectivityManager获取WiFi IP
            android.net.ConnectivityManager cm = (android.net.ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                android.net.Network activeNetwork = cm.getActiveNetwork();
                if (activeNetwork != null) {
                    android.net.NetworkCapabilities nc = cm.getNetworkCapabilities(activeNetwork);
                    if (nc != null && nc.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI)) {
                        java.net.InetAddress ip = cm.getLinkProperties(activeNetwork).getLinkAddresses().get(0).getAddress();
                        if (ip != null && !ip.isLoopbackAddress() && ip instanceof java.net.Inet4Address) {
                            String ipAddress = ip.getHostAddress();
                            Log.d(TAG, "通过ConnectivityManager获取到IP: " + ipAddress);
                            return ipAddress;
                        }
                    }
                }
            }
            
            Log.d(TAG, "方法2失败，返回null");
            
        } catch (Exception e) {
            Log.e(TAG, "获取设备IP地址失败", e);
        }
        return null;
    }
    
    /**
     * 在UI线程显示Toast消息
     */
    private void showToastOnUiThread(final String message) {
        mActivity.runOnUiThread(() -> {
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        });
    }
    
    /**
     * 执行本地ADB授权命令
     * 零跑C11专用：授予READ_LOGS、DUMP、WRITE_SECURE_SETTINGS三大核心权限
     */
    private void executeWirelessAdbAuthorization(int port) {
        Log.d(TAG, "executeWirelessAdbAuthorization 被调用，端口: " + port);
        
        new Thread(() -> {
            try {
                Log.d(TAG, "开始执行本地ADB授权，端口: " + port);
                
                String packageName = mContext.getPackageName();
                boolean allSuccess = true;
                
                // 1. 授予READ_LOGS权限（日志监控）
                String command1 = "pm grant " + packageName + " android.permission.READ_LOGS";
                Log.d(TAG, "尝试通过ADB授予权限: " + command1);
                boolean result1 = com.c11partner.desktop.adb.AdbManager.connectAndExecute("127.0.0.1", port, command1);
                if (!result1) {
                    Log.e(TAG, "READ_LOGS权限授权失败");
                    allSuccess = false;
                }
                
                // 2. 授予DUMP权限（系统状态获取）
                String command2 = "pm grant " + packageName + " android.permission.DUMP";
                Log.d(TAG, "尝试通过ADB授予权限: " + command2);
                boolean result2 = com.c11partner.desktop.adb.AdbManager.connectAndExecute("127.0.0.1", port, command2);
                if (!result2) {
                    Log.e(TAG, "DUMP权限授权失败");
                    allSuccess = false;
                }
                
                // 3. 授予WRITE_SECURE_SETTINGS权限（车控功能）
                String command3 = "pm grant " + packageName + " android.permission.WRITE_SECURE_SETTINGS";
                Log.d(TAG, "尝试通过ADB授予权限: " + command3);
                boolean result3 = com.c11partner.desktop.adb.AdbManager.connectAndExecute("127.0.0.1", port, command3);
                if (!result3) {
                    Log.e(TAG, "WRITE_SECURE_SETTINGS权限授权失败");
                    allSuccess = false;
                }
                
                if (allSuccess) {
                    Log.d(TAG, "三大核心权限授权成功");
                    showToastOnUiThread("三大核心权限授权成功！\nREAD_LOGS + DUMP + WRITE_SECURE_SETTINGS");
                } else {
                    Log.e(TAG, "部分权限授权失败");
                    String msg = "ADB权限授权结果：\n";
                    msg += "READ_LOGS: " + (result1 ? "✅" : "❌") + " ";
                    msg += "DUMP: " + (result2 ? "✅" : "❌") + " ";
                    msg += "WRITE_SECURE_SETTINGS: " + (result3 ? "✅" : "❌");
                    showToastOnUiThread(msg);
                }
                
            } catch (Exception e) {
                Log.e(TAG, "执行本地ADB授权命令时出错", e);
                showToastOnUiThread("执行授权命令时出错: " + e.getMessage());
            }
        }).start();
    }
    
    /**
     * 执行ADB权限授权命令
     * 零跑C11专用：授予READ_LOGS、DUMP、WRITE_SECURE_SETTINGS三大核心权限
     */
    @JavascriptInterface
    public void executeAdbPermissionGrant() {
        new Thread(() -> {
            try {
                String packageName = mContext.getPackageName();
                Log.d(TAG, "开始执行ADB权限授权，包名: " + packageName);
                
                // 使用ADB命令处理器执行权限授权
                AdbCommandProcessor processor = new AdbCommandProcessor(mContext);
                
                // 1. 授予READ_LOGS权限（日志监控）
                String command1 = "pm grant " + packageName + " android.permission.READ_LOGS";
                Log.d(TAG, "执行命令: " + command1);
                boolean result1 = processor.executeCommand(command1);
                
                // 2. 授予DUMP权限（系统状态获取）
                String command2 = "pm grant " + packageName + " android.permission.DUMP";
                Log.d(TAG, "执行命令: " + command2);
                boolean result2 = processor.executeCommand(command2);
                
                // 3. 授予WRITE_SECURE_SETTINGS权限（车控功能）
                String command3 = "pm grant " + packageName + " android.permission.WRITE_SECURE_SETTINGS";
                Log.d(TAG, "执行命令: " + command3);
                boolean result3 = processor.executeCommand(command3);
                
                // 显示结果
                if (result1 && result2 && result3) {
                    showToastOnUiThread("三大核心权限授权成功！\nREAD_LOGS + DUMP + WRITE_SECURE_SETTINGS");
                } else {
                    String errorMsg = "ADB权限授权结果：\n";
                    errorMsg += "READ_LOGS: " + (result1 ? "✅ 成功" : "❌ 失败") + "\n";
                    errorMsg += "DUMP: " + (result2 ? "✅ 成功" : "❌ 失败") + "\n";
                    errorMsg += "WRITE_SECURE_SETTINGS: " + (result3 ? "✅ 成功" : "❌ 失败");
                    showToastOnUiThread(errorMsg);
                }
                
            } catch (Exception e) {
                Log.e(TAG, "执行ADB权限授权时出错", e);
                showToastOnUiThread("执行ADB权限授权时出错: " + e.getMessage());
            }
        }).start();
    }
    
    /**
     * 通过ADB连接打开系统多任务
     */
    @JavascriptInterface
    public void openRecentTasks() {
        new Thread(() -> {
            try {
                // 使用AdbCommandProcessor执行命令
                com.c11partner.desktop.adb.AdbCommandProcessor processor = new com.c11partner.desktop.adb.AdbCommandProcessor(mContext);
                boolean success = processor.executeCommand("input keyevent 187");
                
                if (success) {
                    Log.d(TAG, "ADB命令执行成功: input keyevent 187");
                } else {
                    Log.e(TAG, "ADB命令执行失败");
                    showToastOnUiThread("无法连接ADB，请检查:\n1. 无线调试是否已启用\n2. 防火墙设置");
                }
                
            } catch (Exception e) {
                Log.e(TAG, "打开多任务时出错", e);
                showToastOnUiThread("打开多任务时出错: " + e.getMessage());
            }
        }).start();
    }
    
    /**
     * 打开系统多任务页面
     * 使用adb命令通过ADB连接执行
     */
    @JavascriptInterface
    public void openRecents() {
        new Thread(() -> {
            try {
                Log.d(TAG, "通过ADB命令打开多任务页面");
                
                // 使用ADB命令处理器执行命令
                AdbCommandProcessor processor = new AdbCommandProcessor(mContext);
                // KEYCODE_APP_SWITCH = 187
                String command = "input keyevent 187";
                boolean success = processor.executeCommand(command);
                
                if (!success) {
                    Log.e(TAG, "打开多任务页面失败，无法连接ADB");
                }
            } catch (Exception e) {
                Log.e(TAG, "打开多任务页面时出错", e);
            }
        }).start();
    }

    /**
     * 通过ADB连接设置默认桌面
     */
    @JavascriptInterface
    public void setDefaultDesktopViaAdb() {
        Log.d(TAG, "setDefaultDesktopViaAdb方法被调用");
        
        new Thread(() -> {
            try {
                // 初始化ADB加密密钥
                com.c11partner.desktop.adb.AdbManager.setAppContext(mContext);
                com.c11partner.desktop.adb.AdbManager.initCrypto();
                
                // 获取设备IP地址
                String deviceIp = getDeviceIpAddress();
                if (deviceIp == null || deviceIp.isEmpty()) {
                    Log.e(TAG, "无法获取设备IP地址");
                    showToastOnUiThread("无法获取设备IP地址");
                    return;
                }
                
                Log.d(TAG, "设备IP地址: " + deviceIp);
                
                // 尝试连接到ADB服务并执行清除默认桌面命令
                int[] commonPorts = {5555, 5554, 5556};
                boolean connected = false;
                int connectedPort = -1;
                
                for (int port : commonPorts) {
                    try {
                        Log.d(TAG, "尝试连接到 " + deviceIp + ":" + port);
                        connected = com.c11partner.desktop.adb.AdbManager.connectAndExecute(deviceIp, port, "pm clear-defaults android.intent.category.HOME");
                        if (connected) {
                            connectedPort = port;
                            break;
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "连接端口 " + port + " 失败: " + e.getMessage());
                    }
                }
                
                if (connected) {
                    Log.d(TAG, "ADB连接成功，已清除默认桌面应用");
                    showToastOnUiThread("已清除默认桌面应用");
                } else {
                    Log.e(TAG, "所有ADB端口都无法连接");
                    showToastOnUiThread("无法连接ADB，请检查:\n1. WiFi是否已连接\n2. 无线调试是否已启用\n3. 防火墙设置");
                }
                
            } catch (Exception e) {
                Log.e(TAG, "设置默认桌面时出错", e);
                showToastOnUiThread("设置默认桌面时出错: " + e.getMessage());
            }
        }).start();
    }

    // ==================== 配置应用相关方法 ====================

    /**
     * 保存或更新配置的应用信息
     *
     * @param buttonId    按钮ID
     * @param appName     应用名称
     * @param packageName 应用包名
     * @param appIcon     应用图标Base64编码
     */
    @JavascriptInterface
    public void saveConfigApp(String buttonId, String appName, String packageName, String appIcon) {
        try {
            long result = configAppDbHelper.saveOrUpdateConfigApp(buttonId, appName, packageName, appIcon);
            if (result != -1) {
                Log.d(TAG, "配置应用保存成功: " + buttonId + " -> " + packageName);
            } else {
                Log.e(TAG, "配置应用保存失败: " + buttonId);
            }
        } catch (Exception e) {
            Log.e(TAG, "保存配置应用时出错", e);
        }
    }

    /**
     * 根据按钮ID获取配置的应用信息
     *
     * @param buttonId 按钮ID
     * @return JSON格式的应用信息
     */
    @JavascriptInterface
    public String getConfigApp(String buttonId) {
        try {
            Map<String, String> appInfo = configAppDbHelper.getConfigAppByButtonId(buttonId);
            if (appInfo != null) {
                JSONObject appObj = new JSONObject();
                appObj.put("button_id", appInfo.get("button_id"));
                appObj.put("app_name", appInfo.get("app_name"));
                appObj.put("package_name", appInfo.get("package_name"));
                appObj.put("app_icon", appInfo.get("app_icon"));
                return appObj.toString();
            }
        } catch (Exception e) {
            Log.e(TAG, "获取配置应用时出错", e);
        }
        return "{}";
    }

    // ==================== 组件配置相关方法 ====================

    /**
     * 保存或更新组件配置
     *
     * @param componentName 组件名称
     * @param isEnabled     是否启用
     * @return 是否保存成功
     */
    @JavascriptInterface
    public boolean saveComponentConfig(String componentName, boolean isEnabled) {
        try {
            if (componentConfigDbHelper != null) {
                long result = componentConfigDbHelper.saveOrUpdateComponentConfig(componentName, isEnabled);
                if (result != -1) {
                    Log.d(TAG, "组件配置保存成功: " + componentName + " -> " + isEnabled);
                    return true;
                } else {
                    Log.e(TAG, "组件配置保存失败: " + componentName);
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            Log.e(TAG, "保存组件配置时出错", e);
            return false;
        }
    }

    /**
     * 获取组件配置
     *
     * @param componentName 组件名称
     * @return 是否启用
     */
    @JavascriptInterface
    public boolean isComponentEnabled(String componentName) {
        try {
            if (componentConfigDbHelper != null) {
                return componentConfigDbHelper.isComponentEnabled(componentName);
            }
        } catch (Exception e) {
            Log.e(TAG, "获取组件配置时出错", e);
        }
        return true; // 默认启用
    }

    /**
     * 获取所有组件配置
     *
     * @return JSON格式的组件配置
     */
    @JavascriptInterface
    public String getAllComponentConfigs() {
        try {
            if (componentConfigDbHelper != null) {
                Map<String, Boolean> configs = componentConfigDbHelper.getAllComponentConfigs();
                JSONObject configObj = new JSONObject();
                for (Map.Entry<String, Boolean> entry : configs.entrySet()) {
                    configObj.put(entry.getKey(), entry.getValue());
                }
                return configObj.toString();
            }
        } catch (Exception e) {
            Log.e(TAG, "获取所有组件配置时出错", e);
        }
        return "{}";
    }

    // ==================== 壁纸相关方法 ====================

    /**
     * 更新壁纸分类
     */
    @JavascriptInterface
    public void updateWallpaperCategories() {
        new Thread(() -> {
            try {
                // 从网络获取壁纸分类数据
                List<Map<String, Object>> categories = WallpaperCategoryApiUtils.fetchCategoriesFromApi();

                if (!categories.isEmpty()) {
                    // 清空数据库中的分类数据
                    wallpaperDbHelper.clearCategories();

                    // 将分类数据保存到数据库
                    wallpaperDbHelper.bulkInsertOrUpdateCategories(categories);

                    mActivity.runOnUiThread(() -> {
                        Toast.makeText(mContext, "壁纸分类已更新", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    mActivity.runOnUiThread(() -> {
                        Toast.makeText(mContext, "无法获取壁纸分类数据", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (Exception e) {
                Log.e(TAG, "更新壁纸分类时出错", e);
                mActivity.runOnUiThread(() -> {
                    Toast.makeText(mContext, "更新壁纸分类时出错: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    /**
     * 更新壁纸分类启用状态（供JavaScript调用）
     *
     * @param categoryId 分类ID
     * @param enabled    是否启用
     */
    @JavascriptInterface
    public void updateCategoryEnabled(String categoryId, boolean enabled) {
        try {
            wallpaperDbHelper.updateCategoryEnabled(categoryId, enabled);
        } catch (Exception e) {
            Log.e(TAG, "更新分类启用状态时出错", e);
        }
    }

    /**
     * 异步更新壁纸分类启用状态
     *
     * @param categoryId 分类ID
     * @param enabled    是否启用
     * @param callbackId 回调ID，用于JavaScript端识别回调
     */
    @JavascriptInterface
    public void updateCategoryEnabledAsync(final String categoryId, final boolean enabled, final String callbackId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    wallpaperDbHelper.updateCategoryEnabled(categoryId, enabled);
                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.handleUpdateCategoryEnabledCallback('%s', '%s')",
                                        callbackId, "true");
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "更新分类启用状态时出错", e);
                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.handleUpdateCategoryEnabledCallback('%s', '%s')",
                                        callbackId, "false");
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 获取已启用的分类ID列表
     *
     * @return JSON格式的分类ID列表
     */
    @JavascriptInterface
    public String getEnabledCategories() {
        try {
            List<Map<String, Object>> enabledCategories = wallpaperDbHelper.getAllCategories();
            JSONArray enabledCategoriesArray = new JSONArray();

            for (Map<String, Object> category : enabledCategories) {
                if ((boolean) category.get("enabled")) {
                    enabledCategoriesArray.put(category.get("id"));
                }
            }

            return enabledCategoriesArray.toString();
        } catch (Exception e) {
            Log.e(TAG, "获取已启用分类时出错", e);
            return "[]";
        }
    }

    /**
     * 异步获取已启用的分类ID列表
     *
     * @param callbackId 回调ID，用于JavaScript端识别回调
     */
    @JavascriptInterface
    public void getEnabledCategoriesAsync(final String callbackId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Map<String, Object>> enabledCategories = wallpaperDbHelper.getAllCategories();
                    JSONArray enabledCategoriesArray = new JSONArray();

                    for (Map<String, Object> category : enabledCategories) {
                        if ((boolean) category.get("enabled")) {
                            enabledCategoriesArray.put(category.get("id"));
                        }
                    }

                    final String result = enabledCategoriesArray.toString();

                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.handleGetEnabledCategoriesCallback('%s', '%s')",
                                        callbackId, result);
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "获取已启用分类时出错", e);

                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.handleGetEnabledCategoriesCallback('%s', '%s')",
                                        callbackId, "[]");
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 获取所有壁纸设置
     *
     * @return JSON格式的设置
     */
    @JavascriptInterface
    public String getWallpaperSettings() {
        try {
            Map<String, Object> settings = wallpaperSettingsDbHelper.getAllSettings();
            JSONObject settingsObj = new JSONObject();

            settingsObj.put("wallpaper_carousel", settings.get("wallpaper_carousel"));
            settingsObj.put("local_wallpaper", settings.get("local_wallpaper"));
            settingsObj.put("online_wallpaper", settings.get("online_wallpaper"));
            settingsObj.put("switch_interval", settings.get("switch_interval"));
            settingsObj.put("random_mode", settings.get("random_mode"));
            settingsObj.put("specified_mode", settings.get("specified_mode"));
            settingsObj.put("boot_greeting", settings.get("boot_greeting"));

            return settingsObj.toString();
        } catch (Exception e) {
            Log.e(TAG, "获取壁纸设置时出错", e);
            return "{}";
        }
    }

    /**
     * 异步获取所有壁纸设置
     *
     * @param callbackId 回调ID，用于JavaScript端识别回调
     */
    @JavascriptInterface
    public void getWallpaperSettingsAsync(final String callbackId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> settings = wallpaperSettingsDbHelper.getAllSettings();
                    JSONObject settingsObj = new JSONObject();

                    settingsObj.put("wallpaper_carousel", settings.get("wallpaper_carousel"));
                    settingsObj.put("local_wallpaper", settings.get("local_wallpaper"));
                    settingsObj.put("online_wallpaper", settings.get("online_wallpaper"));
                    settingsObj.put("switch_interval", settings.get("switch_interval"));
                    settingsObj.put("random_mode", settings.get("random_mode"));
                    settingsObj.put("specified_mode", settings.get("specified_mode"));
                    settingsObj.put("boot_greeting", settings.get("boot_greeting"));

                    final String result = settingsObj.toString();

                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.handleGetWallpaperSettingsCallback('%s', %s)",
                                        callbackId, org.json.JSONObject.quote(result));
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "获取壁纸设置时出错", e);
                    final String result = "{}";

                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.handleGetWallpaperSettingsCallback('%s', %s)",
                                        callbackId, org.json.JSONObject.quote(result));
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 异步获取随机壁纸URL
     *
     * @param callbackId 回调ID，用于JavaScript端识别回调
     */
    @JavascriptInterface
    public void getRandomWallpaperAsync(final String callbackId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 检查是否启用了本地壁纸分类模式
                    boolean isRandomMode = wallpaperSettingsDbHelper.getAllSettings().get("random_mode").equals(true);
                    boolean isSpecifiedMode = wallpaperSettingsDbHelper.getAllSettings().get("specified_mode").equals(true);

                    // 如果启用了随机模式，读取SD卡下fstart目录下除了00文件夹下的图片
                    String result;
                    if (isRandomMode) {
                        result = getRandomWallpaperFromFstartExcept00();
                    }
                    // 如果启用了指定模式，读取SD卡下fstart目录下00文件夹下的图片
                    else if (isSpecifiedMode) {
                        result = getRandomWallpaperFromFstart00();
                    }
                    // 如果都没有启用，获取已启用的分类
                    else {
                        // 获取已启用的分类
                        List<Map<String, Object>> enabledCategories = wallpaperDbHelper.getAllCategories();
                        Log.d(TAG, "获取到的所有分类数量: " + enabledCategories.size());

                        // 过滤出已启用的分类
                        List<Map<String, Object>> filteredCategories = new ArrayList<>();
                        for (Map<String, Object> category : enabledCategories) {
                            if ((boolean) category.get("enabled")) {
                                filteredCategories.add(category);
                                Log.d(TAG, "已启用的分类: " + category.get("id"));
                            }
                        }

                        // 如果没有启用的分类，返回null
                        if (filteredCategories.isEmpty()) {
                            Log.d(TAG, "没有已启用的分类");
                            result = null;
                        } else {
                            // 随机选择一个分类
                            Random random = new Random();
                            Map<String, Object> selectedCategory = filteredCategories.get(random.nextInt(filteredCategories.size()));

                            // 获取分类ID
                            String categoryId = (String) selectedCategory.get("id");
                            Log.d(TAG, "随机选择的分类ID: " + categoryId);

                            // 从本地获取该分类的随机壁纸
                            result = WallpaperDownloadUtils.getRandomLocalWallpaper(mContext, categoryId);
                            Log.d(TAG, "获取到的壁纸路径: " + result);
                            
                            // 如果该分类没有壁纸，尝试其他分类
                            if (result == null && filteredCategories.size() > 1) {
                                Log.d(TAG, "该分类没有壁纸，尝试其他分类");
                                // 移除当前分类
                                filteredCategories.remove(selectedCategory);
                                // 重新随机选择
                                selectedCategory = filteredCategories.get(random.nextInt(filteredCategories.size()));
                                categoryId = (String) selectedCategory.get("id");
                                Log.d(TAG, "尝试其他分类ID: " + categoryId);
                                result = WallpaperDownloadUtils.getRandomLocalWallpaper(mContext, categoryId);
                                Log.d(TAG, "尝试其他分类获取到的壁纸路径: " + result);
                            }
                        }
                    }

                    final String finalResult = result;

                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.handleGetRandomWallpaperCallback('%s', %s)",
                                        callbackId, org.json.JSONObject.quote(finalResult != null ? finalResult : ""));
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "获取随机壁纸时出错", e);

                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.handleGetRandomWallpaperCallback('%s', %s)",
                                        callbackId, org.json.JSONObject.quote(""));
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 获取随机壁纸的Base64编码数据
     *
     * @return Base64编码的图片数据
     */
    @JavascriptInterface
    public String getRandomWallpaperBase64() {
        try {
            // 检查是否启用了本地壁纸分类模式
            boolean isRandomMode = wallpaperSettingsDbHelper.getAllSettings().get("random_mode").equals(true);
            boolean isSpecifiedMode = wallpaperSettingsDbHelper.getAllSettings().get("specified_mode").equals(true);

            // 如果启用了随机模式，读取SD卡下fstart目录下除了00文件夹下的图片
            if (isRandomMode) {
                String localWallpaper = getRandomWallpaperFromFstartExcept00();
                if (localWallpaper != null) {
                    // 更新MainActivity中的壁纸状态
                    if (mActivity != null) {
                        mActivity.isUsingDefaultWallpaper = false;
                        mActivity.currentWallpaperPath = localWallpaper;
                    }
                    return encodeImageToBase64(localWallpaper);
                }
            }

            // 如果启用了指定模式，读取SD卡下fstart目录下00文件夹下的图片
            if (isSpecifiedMode) {
                String localWallpaper = getRandomWallpaperFromFstart00();
                if (localWallpaper != null) {
                    // 更新MainActivity中的壁纸状态
                    if (mActivity != null) {
                        mActivity.isUsingDefaultWallpaper = false;
                        mActivity.currentWallpaperPath = localWallpaper;
                    }
                    return encodeImageToBase64(localWallpaper);
                }
            }

            // 如果都没有启用，获取已启用的分类
            List<Map<String, Object>> enabledCategories = wallpaperDbHelper.getAllCategories();

            // 过滤出已启用的分类
            List<Map<String, Object>> filteredCategories = new ArrayList<>();
            for (Map<String, Object> category : enabledCategories) {
                if ((boolean) category.get("enabled")) {
                    filteredCategories.add(category);
                }
            }

            // 如果没有启用的分类，返回默认壁纸
            if (filteredCategories.isEmpty()) {
                // 更新MainActivity中的壁纸状态
                if (mActivity != null) {
                    mActivity.isUsingDefaultWallpaper = true;
                    mActivity.currentWallpaperPath = "";
                }
                return encodeImageToBase64("file:///android_asset/images/nav_car.png");
            }

            // 随机选择一个分类
            Random random = new Random();
            Map<String, Object> selectedCategory = filteredCategories.get(random.nextInt(filteredCategories.size()));

            // 获取分类ID
            String categoryId = (String) selectedCategory.get("id");

            // 从本地获取该分类的随机壁纸
            String localWallpaper = WallpaperDownloadUtils.getRandomLocalWallpaper(mContext, categoryId);
            if (localWallpaper != null) {
                // 更新MainActivity中的壁纸状态
                if (mActivity != null) {
                    mActivity.isUsingDefaultWallpaper = false;
                    mActivity.currentWallpaperPath = localWallpaper;
                }
                return encodeImageToBase64(localWallpaper);
            }

            // 如果没有找到壁纸，返回默认壁纸
            // 更新MainActivity中的壁纸状态
            if (mActivity != null) {
                mActivity.isUsingDefaultWallpaper = true;
                mActivity.currentWallpaperPath = "";
            }
            return encodeImageToBase64("file:///android_asset/images/nav_car.png");
        } catch (Exception e) {
            Log.e(TAG, "获取随机壁纸Base64数据时出错", e);
            return "";
        }
    }

    /**
     * 异步获取随机壁纸的Base64编码数据
     *
     * @param callbackId 回调ID，用于JavaScript端识别回调
     */
    @JavascriptInterface
    public void getRandomWallpaperBase64Async(final String callbackId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 检查是否启用了本地壁纸分类模式
                    boolean isRandomMode = wallpaperSettingsDbHelper.getAllSettings().get("random_mode").equals(true);
                    boolean isSpecifiedMode = wallpaperSettingsDbHelper.getAllSettings().get("specified_mode").equals(true);

                    String result;
                    // 如果启用了随机模式，读取SD卡下fstart目录下除了00文件夹下的图片
                    if (isRandomMode) {
                        String localWallpaper = getRandomWallpaperFromFstartExcept00();
                        if (localWallpaper != null) {
                            // 更新MainActivity中的壁纸状态
                            if (mActivity != null) {
                                mActivity.isUsingDefaultWallpaper = false;
                                mActivity.currentWallpaperPath = localWallpaper;
                            }
                            result = encodeImageToBase64(localWallpaper);
                        } else {
                            result = "";
                        }
                    }
                    // 如果启用了指定模式，读取SD卡下fstart目录下00文件夹下的图片
                    else if (isSpecifiedMode) {
                        String localWallpaper = getRandomWallpaperFromFstart00();
                        if (localWallpaper != null) {
                            // 更新MainActivity中的壁纸状态
                            if (mActivity != null) {
                                mActivity.isUsingDefaultWallpaper = false;
                                mActivity.currentWallpaperPath = localWallpaper;
                            }
                            result = encodeImageToBase64(localWallpaper);
                        } else {
                            result = "";
                        }
                    }
                    // 如果都没有启用，获取已启用的分类
                    else {
                        // 获取已启用的分类
                        List<Map<String, Object>> enabledCategories = wallpaperDbHelper.getAllCategories();

                        // 过滤出已启用的分类
                        List<Map<String, Object>> filteredCategories = new ArrayList<>();
                        for (Map<String, Object> category : enabledCategories) {
                            if ((boolean) category.get("enabled")) {
                                filteredCategories.add(category);
                            }
                        }

                        // 如果没有启用的分类，返回默认壁纸
                        if (filteredCategories.isEmpty()) {
                            // 更新MainActivity中的壁纸状态
                            if (mActivity != null) {
                                mActivity.isUsingDefaultWallpaper = true;
                                mActivity.currentWallpaperPath = "";
                            }
                            result = encodeImageToBase64("file:///android_asset/images/nav_car.png");
                        } else {
                            // 随机选择一个分类
                            Random random = new Random();
                            Map<String, Object> selectedCategory = filteredCategories.get(random.nextInt(filteredCategories.size()));

                            // 获取分类ID
                            String categoryId = (String) selectedCategory.get("id");

                            // 从本地获取该分类的随机壁纸
                            String localWallpaper = WallpaperDownloadUtils.getRandomLocalWallpaper(mContext, categoryId);
                            if (localWallpaper != null) {
                                // 更新MainActivity中的壁纸状态
                                if (mActivity != null) {
                                    mActivity.isUsingDefaultWallpaper = false;
                                    mActivity.currentWallpaperPath = localWallpaper;
                                }
                                result = encodeImageToBase64(localWallpaper);
                            } else {
                                result = "";
                            }
                        }
                    }

                    final String finalResult = result;

                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.handleGetRandomWallpaperBase64Callback('%s', %s)",
                                        callbackId, org.json.JSONObject.quote(finalResult != null ? finalResult : ""));
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "获取随机壁纸Base64数据时出错", e);

                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.handleGetRandomWallpaperBase64Callback('%s', %s)",
                                        callbackId, org.json.JSONObject.quote(""));
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                }
            }
        }).start();
    }

    // ==================== 快速启动应用相关方法 ====================

    /**
     * 获取快速启动应用列表
     *
     * @return JSON格式的应用列表
     */
    @JavascriptInterface
    public String getQuickAppList() {
        try {
            List<Map<String, Object>> quickApps = quickAppDbHelper.getAllQuickApps();
            JSONArray quickAppsArray = new JSONArray();

            for (Map<String, Object> app : quickApps) {
                JSONObject appObj = new JSONObject();
                appObj.put("name", app.get("name"));
                appObj.put("packageName", app.get("packageName"));
                appObj.put("icon", app.get("icon"));
                quickAppsArray.put(appObj);
            }

            return quickAppsArray.toString();
        } catch (Exception e) {
            Log.e(TAG, "获取快速启动应用列表时出错", e);
            return "[]";
        }
    }

    /**
     * 添加到快速启动应用
     *
     * @param name        应用名称
     * @param packageName 应用包名
     * @param iconBase64  图标Base64编码
     */
    @JavascriptInterface
    public void addQuickApp(String name, String packageName, String iconBase64) {
        try {
            long result = quickAppDbHelper.insertQuickApp(name, packageName, iconBase64);
            if (result != -1) {
                mActivity.runOnUiThread(() -> {
                    Toast.makeText(mContext, "已添加到快速启动: " + name, Toast.LENGTH_SHORT).show();
                });
            } else {
                mActivity.runOnUiThread(() -> {
                    Toast.makeText(mContext, "添加到快速启动失败: " + name, Toast.LENGTH_SHORT).show();
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "添加到快速启动应用时出错", e);
            mActivity.runOnUiThread(() -> {
                Toast.makeText(mContext, "添加到快速启动失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    /**
     * 删除快速启动应用
     *
     * @param packageName 应用包名
     */
    @JavascriptInterface
    public void removeQuickApp(String packageName) {
        try {
            int result = quickAppDbHelper.deleteQuickApp(packageName);
            if (result > 0) {
                mActivity.runOnUiThread(() -> {
                    Toast.makeText(mContext, "已从快速启动移除", Toast.LENGTH_SHORT).show();
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "删除快速启动应用时出错", e);
            mActivity.runOnUiThread(() -> {
                Toast.makeText(mContext, "移除快速启动失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    /**
     * 检查应用是否已添加到快速启动
     *
     * @param packageName 应用包名
     * @return 是否已添加
     */
    @JavascriptInterface
    public boolean isQuickApp(String packageName) {
        try {
            return quickAppDbHelper.isQuickApp(packageName);
        } catch (Exception e) {
            Log.e(TAG, "检查快速启动应用时出错", e);
            return false;
        }
    }

    // ==================== 音乐可视化相关方法 ====================

    /**
     * 开始音乐可视化
     */
    @JavascriptInterface
    public void startMusicVisualizer() {
        Log.d(TAG, "收到启动音乐可视化的JavaScript调用");
        mActivity.runOnUiThread(() -> {
            if (mActivity.musicVisualizer != null) {
                try {
                    Log.d(TAG, "正在启动音乐可视化");
                    mActivity.musicVisualizer.startVisualizer();
                    Log.d(TAG, "音乐可视化启动完成");
                } catch (Exception e) {
                    Log.e(TAG, "启动音乐可视化时出错", e);
                }
            } else {
                Log.d(TAG, "musicVisualizer对象为空");
            }
        });
    }

    /**
     * 停止音乐可视化
     */
    @JavascriptInterface
    public void stopMusicVisualizer() {
        Log.d(TAG, "收到停止音乐可视化的JavaScript调用");
        mActivity.runOnUiThread(() -> {
            if (mActivity.musicVisualizer != null) {
                try {
                    mActivity.musicVisualizer.stopVisualizer();
                    Log.d(TAG, "音乐可视化已停止");
                } catch (Exception e) {
                    Log.e(TAG, "停止音乐可视化时出错", e);
                }
            }
        });
    }

    /**
     * 检查音乐是否正在播放
     *
     * @return 音乐播放状态
     */
    @JavascriptInterface
    public boolean isMusicPlaying() {
        boolean isMusicPlaying = mActivity.isMusicPlaying;
        //Log.d(TAG, "检查音乐播放状态: " + isMusicPlaying);
        return isMusicPlaying;
    }


    /**
     * 获取当前音乐名称
     */
    @JavascriptInterface
    public String getCurrentMusicName() {
        if (isMediaSessionServiceBound && mediaSessionService != null) {
            return mediaSessionService.getCurrentMusicName();
        }
        return "此刻无声，佳音已备候君启...";
    }

    /**
     * 获取音乐播放进度信息
     */
    @JavascriptInterface
    public String getMusicProgressInfo() {
        try {
            if (isMediaSessionServiceBound && mediaSessionService != null) {
                org.json.JSONObject progressInfo = new org.json.JSONObject();
                progressInfo.put("isPlaying", mediaSessionService.isPlaying());
                progressInfo.put("currentPosition", mediaSessionService.getCurrentPosition());
                progressInfo.put("duration", mediaSessionService.getDuration());
                return progressInfo.toString();
            }
        } catch (Exception e) {
            Log.e(TAG, "获取音乐进度信息失败", e);
        }
        try {
            org.json.JSONObject defaultInfo = new org.json.JSONObject();
            defaultInfo.put("isPlaying", false);
            defaultInfo.put("currentPosition", 0);
            defaultInfo.put("duration", 0);
            return defaultInfo.toString();
        } catch (Exception e) {
            return "{\"isPlaying\":false,\"currentPosition\":0,\"duration\":0}";
        }
    }

     *
     * @return WiFi连接状态
     */
    @JavascriptInterface
    public boolean isWifiConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false; // 无连接管理器，视为未连接
        }

        // 适配 Android 10 及以上版本（API 29+）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) {
                return false; // 无活动网络
            }
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            return capabilities != null
                    && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) // 是 WiFi 网络
                    && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET); // 有互联网访问能力
        } else {
            // 适配 Android 10 以下版本（API < 29）
            android.net.NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return wifiInfo != null && wifiInfo.isConnected(); // 已连接到 WiFi 且网络可用
        }
    }

    /**
     * 检查蓝牙是否已连接
     *
     * @return 蓝牙连接状态
     */
    @JavascriptInterface
    public boolean isBluetoothConnected() {
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                android.bluetooth.BluetoothManager bluetoothManager =
                        (android.bluetooth.BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
                if (bluetoothManager != null) {
                    android.bluetooth.BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
                    // 检查蓝牙是否启用
                    if (bluetoothAdapter != null) {

                        if (bluetoothAdapter.isEnabled()) {
                            // 检查所有可能的蓝牙配置文件连接
                            int[] profiles = { 
                                android.bluetooth.BluetoothProfile.GATT,
                                android.bluetooth.BluetoothProfile.GATT_SERVER,
                                android.bluetooth.BluetoothProfile.A2DP,
                                android.bluetooth.BluetoothProfile.HEADSET,
                                android.bluetooth.BluetoothProfile.HEALTH
                            };
                            
                            for (int profile : profiles) {
                                try {
                                    List<android.bluetooth.BluetoothDevice> connectedDevices = 
                                            bluetoothManager.getConnectedDevices(profile);
                                    if (connectedDevices != null && !connectedDevices.isEmpty()) {
                                        Log.d(TAG, "检测到已连接的设备，配置文件: " + profile + ", 数量: " + connectedDevices.size());
                                        for (android.bluetooth.BluetoothDevice device : connectedDevices) {
                                            Log.d(TAG, "已连接设备: " + device.getName() + " (" + device.getAddress() + ")");
                                        }
                                        return true;
                                    } else {
                                        Log.d(TAG, "配置文件 " + profile + " 没有已连接的设备");
                                    }
                                } catch (Exception e) {
                                    Log.d(TAG, "检查配置文件 " + profile + " 时出错: " + e.getMessage());
                                }
                            }
                            
                            // 尝试使用传统方法检查已配对设备的连接状态
                            try {
                                java.util.Set<android.bluetooth.BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
                                Log.d(TAG, "已配对设备数量: " + (bondedDevices != null ? bondedDevices.size() : 0));
                                
                                if (bondedDevices != null) {
                                    for (android.bluetooth.BluetoothDevice device : bondedDevices) {
                                        Log.d(TAG, "已配对设备: " + device.getName() + " (" + device.getAddress() + "), 状态: " + device.getBondState());
                                        
                                        // 尝试检查设备是否处于连接状态
                                        try {
                                            Method isConnectedMethod = android.bluetooth.BluetoothDevice.class.getMethod("isConnected");
                                            boolean isConnected = (boolean) isConnectedMethod.invoke(device);
                                            Log.d(TAG, "设备 " + device.getName() + " 连接状态: " + isConnected);
                                            if (isConnected) {
                                                return true;
                                            }
                                        } catch (Exception e) {
                                            Log.d(TAG, "无法检查设备连接状态: " + e.getMessage());
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                Log.d(TAG, "检查已配对设备时出错: " + e.getMessage());
                            }
                            
                            // 如果没有检测到任何连接的设备，返回false
                            Log.d(TAG, "没有检测到已连接的蓝牙设备");
                            return false;
                        }
                    }
                }
            } else {
                // 对于较老的Android版本，返回false，因为无法准确检测连接状态
                Log.d(TAG, "Android版本过低，无法准确检测蓝牙连接状态");
                return false;
            }
        } catch (Exception e) {
            Log.e("MainActivity", "检查蓝牙连接状态时出错", e);
        }
        Log.d(TAG, "蓝牙连接状态检查完成，返回false");
        return false;
    }


    /**
     * 上一首
     */
    @JavascriptInterface
    public void playPause() {
        Log.d(TAG, "playPause方法被调用");
        musicUtils = new com.c11partner.desktop.utils.MusicUtils(mContext);
        boolean isPlaying = musicUtils.isMusicPlaying();
        Log.d(TAG, "当前音乐播放状态: " + isPlaying);
        if(isPlaying){
            Log.d(TAG, "调用pause方法");
            musicUtils.pause();
        }else {
            Log.d(TAG, "调用play方法");
            musicUtils.play();
        }
    }

    /**
     * 下一首
     */
    @JavascriptInterface
    public void playNext() {
        Log.d(TAG, "playNext方法被调用");
        // 初始化音乐工具类
        musicUtils = new com.c11partner.desktop.utils.MusicUtils(mContext);
        Log.d(TAG, "调用next方法");
        musicUtils.next();
    }

    /**
     * 上一首
     */
    @JavascriptInterface
    public void playPrevious() {
        Log.d(TAG, "playPrevious方法被调用");
        // 初始化音乐工具类
        musicUtils = new com.c11partner.desktop.utils.MusicUtils(mContext);
        Log.d(TAG, "调用previous方法");
        musicUtils.previous();
    }


    // ==================== 蓝牙相关方法 ====================



    // ==================== 系统设置相关方法 ====================

    /**
     * 保存原桌面自启设置
     *
     * @param enabled 是否启用
     * @return 是否保存成功
     */
    @JavascriptInterface
    public boolean saveSystemLauncherSetting(boolean enabled) {
        try {
            wallpaperSettingsDbHelper.updateSystemLauncher(enabled);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "保存原桌面自启设置时出错", e);
            return false;
        }
    }

    /**
     * 保存开机问候语设置
     *
     * @param enabled 是否启用
     * @return 是否保存成功
     */
    @JavascriptInterface
    public boolean saveBootGreetingSetting(boolean enabled) {
        try {
            wallpaperSettingsDbHelper.updateBootGreeting(enabled);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "保存开机问候语设置时出错", e);
            return false;
        }
    }

    /**
     * 保存随机模式设置
     *
     * @param enabled 是否启用
     * @return 是否保存成功
     */
    @JavascriptInterface
    public boolean saveRandomModeSetting(boolean enabled) {
        try {
            wallpaperSettingsDbHelper.updateRandomMode(enabled);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "保存随机模式设置时出错", e);
            return false;
        }
    }

    /**
     * 获取当前农历日期
     *
     * @return 格式化的农历日期字符串
     */
    @JavascriptInterface
    public String getLunarCalendar() {
        try {
            return com.c11partner.desktop.utils.LunarCalendarUtils.lunarCalendar();
        } catch (Exception e) {
            Log.e(TAG, "获取农历日期时出错", e);
            return "农历日期获取失败";
        }
    }

    /**
     * 保存指定模式设置
     *
     * @param enabled 是否启用
     * @return 是否保存成功
     */
    @JavascriptInterface
    public boolean saveSpecifiedModeSetting(boolean enabled) {
        try {
            wallpaperSettingsDbHelper.updateSpecifiedMode(enabled);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "保存指定模式设置时出错", e);
            return false;
        }
    }

    /**
     * 异步保存原桌面自启设置
     *
     * @param enabled    是否启用
     * @param callbackId 回调ID，用于JavaScript端识别回调
     */
    @JavascriptInterface
    public void saveSystemLauncherSettingAsync(final boolean enabled, final String callbackId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    wallpaperSettingsDbHelper.updateSystemLauncher(enabled);
                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.handleSaveSystemLauncherSettingCallback('%s', %s)",
                                        callbackId, "true");
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "保存原桌面自启设置时出错", e);
                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.handleSaveSystemLauncherSettingCallback('%s', %s)",
                                        callbackId, "false");
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 异步保存开机问候语设置
     *
     * @param enabled    是否启用
     * @param callbackId 回调ID，用于JavaScript端识别回调
     */
    @JavascriptInterface
    public void saveBootGreetingSettingAsync(final boolean enabled, final String callbackId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    wallpaperSettingsDbHelper.updateBootGreeting(enabled);
                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.handleSaveBootGreetingSettingCallback('%s', %s)",
                                        callbackId, "true");
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "保存开机问候语设置时出错", e);
                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.handleSaveBootGreetingSettingCallback('%s', %s)",
                                        callbackId, "false");
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 异步保存随机模式设置
     *
     * @param enabled    是否启用
     * @param callbackId 回调ID，用于JavaScript端识别回调
     */
    @JavascriptInterface
    public void saveRandomModeSettingAsync(final boolean enabled, final String callbackId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    wallpaperSettingsDbHelper.updateRandomMode(enabled);
                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.handleSaveRandomModeSettingCallback('%s', %s)",
                                        callbackId, "true");
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "保存随机模式设置时出错", e);
                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.handleSaveRandomModeSettingCallback('%s', %s)",
                                        callbackId, "false");
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 异步保存指定模式设置
     *
     * @param enabled    是否启用
     * @param callbackId 回调ID，用于JavaScript端识别回调
     */
    @JavascriptInterface
    public void saveSpecifiedModeSettingAsync(final boolean enabled, final String callbackId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    wallpaperSettingsDbHelper.updateSpecifiedMode(enabled);
                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.handleSaveSpecifiedModeSettingCallback('%s', %s)",
                                        callbackId, "true");
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "保存指定模式设置时出错", e);
                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.handleSaveSpecifiedModeSettingCallback('%s', %s)",
                                        callbackId, "false");
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 主动更新时间显示
     * 该方法由后端定时调用，主动推送时间数据到前端
     *
     * @param time 时间字符串 (HH:mm:ss)
     * @param date 日期字符串 (yyyy年MM月dd日 星期X)
     * @param lunarDate 农历日期字符串
     */
    @JavascriptInterface
    public void updateTimeDisplay(String time, String date, String lunarDate) {
        try {
            //Log.d(TAG, "收到时间更新请求: " + time + ", " + date + ", " + lunarDate);
            
            // 构建传递给前端的JSON数据
            JSONObject timeData = new JSONObject();
            timeData.put("time", time);
            timeData.put("date", date);
            timeData.put("lunarDate", lunarDate);
            
            // 在UI线程中执行JavaScript回调
            final String jsonData = timeData.toString();
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mActivity.webView != null) {
                        // 先检查函数是否存在，避免报错
                        String javascript = String.format(
                                "javascript:if(typeof window.updateTimeDisplay === 'function') { window.updateTimeDisplay(%s); }",
                                jsonData);
                        //Log.d(TAG, "执行JavaScript: " + javascript);
                        mActivity.webView.loadUrl(javascript);
                    } else {
                        Log.w(TAG, "WebView为空，无法执行JavaScript");
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "更新时间显示时出错", e);
        }
    }

    /**
     * 异步更新时间显示
     * 该方法由后端定时调用，主动推送时间数据到前端
     *
     * @param callbackId 回调ID，用于JavaScript端识别回调
     */
    @JavascriptInterface
    public void updateTimeDisplayAsync(final String callbackId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 获取当前时间数据
                    java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault());
                    java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy年MM月dd日", java.util.Locale.getDefault());
                    java.util.Date now = new java.util.Date();
                    
                    String time = timeFormat.format(now);
                    String date = dateFormat.format(now) + " " + getWeekDay(now);
                    String lunarDate = com.c11partner.desktop.utils.LunarCalendarUtils.lunarCalendar();
                    
                    // 构建传递给前端的JSON数据
                    JSONObject timeData = new JSONObject();
                    timeData.put("time", time);
                    timeData.put("date", date);
                    timeData.put("lunarDate", lunarDate);
                    
                    // 在UI线程中执行JavaScript回调
                    final String jsonData = timeData.toString();
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                // 先检查函数是否存在，避免报错
                                String javascript = String.format(
                                        "javascript:if(typeof window.updateTimeDisplay === 'function') { window.updateTimeDisplay(%s); }",
                                        jsonData);
                                mActivity.webView.loadUrl(javascript);
                                
                                // 执行回调通知前端更新完成
                                String callbackScript = String.format(
                                        "javascript:if(window.AsyncCallbackManager) window.AsyncCallbackManager.execute('%s', true)",
                                        callbackId);
                                mActivity.webView.loadUrl(callbackScript);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "异步更新时间显示时出错", e);
                    // 执行回调通知前端更新失败
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String callbackScript = String.format(
                                        "javascript:if(window.AsyncCallbackManager) window.AsyncCallbackManager.execute('%s', false)",
                                        callbackId);
                                mActivity.webView.loadUrl(callbackScript);
                            }
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 获取星期几
     *
     * @param date 日期对象
     * @return 星期几字符串
     */
    private String getWeekDay(java.util.Date date) {
        String[] weekdays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        int weekIndex = cal.get(java.util.Calendar.DAY_OF_WEEK) - 1;
        return weekdays[weekIndex];
    }

    /**
     * 初始化空调状态
     * 通过反射获取空调的当前状态并传递给前端
     */
    @JavascriptInterface
    public void initializeAcStatus() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Class<?> acDeviceClass = Class.forName("android.hardware.bydauto.ac.BYDAutoAcDevice");
                    java.lang.reflect.Method getInstanceMethod = acDeviceClass.getMethod("getInstance", Context.class);
                    Object acDeviceInstance = getInstanceMethod.invoke(null, mContext);
                    
                    if (acDeviceInstance == null) {
                        Log.e(TAG, "空调设备实例未初始化");
                        return;
                    }

                    Class<?> deviceClass = acDeviceInstance.getClass();
                    
                    // 获取空调开关状态
                    java.lang.reflect.Method getAcStartStateMethod = deviceClass.getMethod("getAcStartState");
                    int acStartState = (int) getAcStartStateMethod.invoke(acDeviceInstance);
                    
                    // 获取温度
                    java.lang.reflect.Method getTempratureMethod = deviceClass.getMethod("getTemprature", int.class);
                    int AC_TEMPERATURE_MAIN = getStaticIntValue(deviceClass, "AC_TEMPERATURE_MAIN");
                    int temperature = (int) getTempratureMethod.invoke(acDeviceInstance, AC_TEMPERATURE_MAIN);
                    
                    // 获取风量
                    java.lang.reflect.Method getAcWindLevelMethod = deviceClass.getMethod("getAcWindLevel");
                    int windLevel = (int) getAcWindLevelMethod.invoke(acDeviceInstance);
                    
                    // 获取前除霜状态
                    java.lang.reflect.Method getAcDefrostStateMethod = deviceClass.getMethod("getAcDefrostState", int.class);
                    int AC_DEFROST_AREA_FRONT = getStaticIntValue(deviceClass, "AC_DEFROST_AREA_FRONT");
                    int defrostState = (int) getAcDefrostStateMethod.invoke(acDeviceInstance, AC_DEFROST_AREA_FRONT);
                    
                    // 获取常量值
                    int AC_POWER_ON = getStaticIntValue(deviceClass, "AC_POWER_ON");
                    int AC_DEFROST_STATE_ON = getStaticIntValue(deviceClass, "AC_DEFROST_STATE_ON");
                    
                    // 构建传递给前端的JSON数据
                    JSONObject acData = new JSONObject();
                    acData.put("acOn", acStartState == AC_POWER_ON);
                    acData.put("temperature", temperature);
                    acData.put("windLevel", windLevel);
                    acData.put("defrostOn", defrostState == AC_DEFROST_STATE_ON);
                    
                    final String jsonData = acData.toString();
                    Log.d(TAG, "空调状态: " + jsonData);
                    
                    // 在UI线程中执行JavaScript回调
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.webView != null) {
                                String javascript = String.format(
                                        "javascript:window.initializeAcStatus(%s)",
                                        jsonData);
                                mActivity.webView.loadUrl(javascript);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "初始化空调状态时出错", e);
                }
            }
        }).start();
    }

    /**
     * 重启应用
     */
    @JavascriptInterface
    public void restartApp() {
        Log.d(TAG, "收到重启应用的JavaScript调用");
        mActivity.runOnUiThread(() -> {
            // 发送重启应用的广播
            Intent intent = new Intent("com.c11partner.desktop.RESTART_APP");
            mActivity.sendBroadcast(intent);
        });
    }

    /**
     * 检测高德地图是否安装
     */
    private boolean isAmapInstalled(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo("com.autonavi.minimap", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 启动高德地图导航到家
     */
    @JavascriptInterface
    public void navigateToHome() {
        try {
            // 检查高德地图是否安装
            if (!isAmapInstalled(mContext)) {
                mActivity.runOnUiThread(() -> {
                    Toast.makeText(mContext, "请安装高德地图", Toast.LENGTH_SHORT).show();
                });
                return;
            }
            
            // 家的经纬度
            double latitude = 39.917366;
            double longitude = 116.397389;


            // 构造高德地图导航URI
            String uri = "androidamap://route?sourceApplication=DiPartner&sname=我的位置&dlat=" + latitude + "&dlon=" + longitude + "&dname=故宫博物院&dev=0&t=0";
            
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.autonavi.minimap"); // 精准唤起，避免浏览器拦截
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            
            Log.d(TAG, "成功启动高德地图导航到家");
        } catch (Exception e) {
            Log.e(TAG, "启动高德地图导航到家时出错", e);
            mActivity.runOnUiThread(() -> {
                Toast.makeText(mContext, "启动高德地图失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });
        }
    }

    /**
     * 启动高德地图导航到公司
     */
    @JavascriptInterface
    public void navigateToCompany() {
        try {
            // 检查高德地图是否安装
            if (!isAmapInstalled(mContext)) {
                mActivity.runOnUiThread(() -> {
                    Toast.makeText(mContext, "请安装高德地图", Toast.LENGTH_SHORT).show();
                });
                return;
            }
            
            // 公司的经纬度
            double latitude = 39.944219;
            double longitude = 116.482533;

            // 构造高德地图导航URI
            String uri = "androidamap://route?sourceApplication=DiPartner&sname=我的位置&dlat=" + latitude + "&dlon=" + longitude + "&dname=公司&dev=0&t=0";
            
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.autonavi.minimap"); // 精准唤起，避免浏览器拦截
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            
            Log.d(TAG, "成功启动高德地图导航到公司");
        } catch (Exception e) {
            Log.e(TAG, "启动高德地图导航到公司时出错", e);
            mActivity.runOnUiThread(() -> {
                Toast.makeText(mContext, "启动高德地图失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });
        }
    }

    /**
     * 设置为默认桌面
     */
    @JavascriptInterface
    public void setDefaultDesktop() {
        Log.d(TAG, "收到设置为默认桌面的JavaScript调用");
        mActivity.runOnUiThread(() -> {
            try {
                // 创建Intent指向设置默认桌面的界面
                Intent intent = new Intent(Settings.ACTION_HOME_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, "启动默认桌面设置界面时出错", e);
                // 如果无法直接启动设置界面，则尝试打开应用详情页面
                try {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } catch (Exception ex) {
                    Log.e(TAG, "启动应用详情页面时出错", ex);
                    mActivity.runOnUiThread(() -> {
                        Toast.makeText(mContext, "无法打开默认桌面设置，请手动在系统设置中设置", Toast.LENGTH_LONG).show();
                    });
                }
            }
        });
    }
    

    
    /**
     * 自动连接无线ADB调试
     * 应用启动时自动尝试连接无线ADB调试
     */
    public void autoConnectWirelessAdb() {
        Log.d(TAG, "开始自动连接无线ADB调试");
        new Thread(() -> {
            try {
                // 检查USB调试是否已启用
                if (isUsbDebuggingEnabled()) {
                    Log.d(TAG, "USB调试已启用，尝试连接无线ADB");
                    
                    // 初始化ADB加密密钥
                    com.c11partner.desktop.adb.AdbManager.setAppContext(mContext);
                    com.c11partner.desktop.adb.AdbManager.initCrypto();
                    
                    // 尝试连接到本地ADB服务（通常是localhost:5555）
                    try {
                        com.c11partner.desktop.adb.AdbManager.connect("localhost", 5555);
                        Log.d(TAG, "无线ADB连接成功");
                        
                        // 连接成功后执行授权命令
                        executeAdbCommandsAfterConnection();
                    } catch (Exception connectException) {
                        Log.e(TAG, "无线ADB连接失败", connectException);
                        
                        // 尝试其他常见的ADB端口，包括5554端口
                        int[] commonPorts = {5555, 5554, 5556, 5557, 5558, 5559};
                        boolean connected = false;
                        
                        for (int port : commonPorts) {
                            try {
                                com.c11partner.desktop.adb.AdbManager.connect("localhost", port);
                                Log.d(TAG, "通过端口 " + port + " 连接无线ADB成功");
                                connected = true;
                                
                                // 连接成功后执行授权命令
                                executeAdbCommandsAfterConnection();
                                break;
                            } catch (Exception e) {
                                Log.d(TAG, "端口 " + port + " 连接失败: " + e.getMessage());
                            }
                        }
                        
                        if (!connected) {
                            Log.e(TAG, "所有常见端口都无法连接无线ADB");
                            mActivity.runOnUiThread(() -> {
                                Toast.makeText(mContext, "无法连接无线ADB，请检查设备设置", Toast.LENGTH_LONG).show();
                            });
                        }
                    }
                } else {
                    Log.d(TAG, "USB调试未启用，跳过无线ADB连接");
                    mActivity.runOnUiThread(() -> {
                        Toast.makeText(mContext, "请先启用USB调试", Toast.LENGTH_LONG).show();
                    });
                }
            } catch (Exception e) {
                Log.e(TAG, "自动连接无线ADB时出错", e);
                mActivity.runOnUiThread(() -> {
                    Toast.makeText(mContext, "连接无线ADB时出错: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }
    
    /**
     * 执行ADB授权命令
     */
    private void executeAdbAuthorization() {
        // 执行授权命令
        String packageName = mContext.getPackageName();
        String command = "pm grant " + packageName + " android.permission.DUMP";
        
        // 使用简化的ADB连接来触发授权
        new Thread(() -> {
            try {
                com.c11partner.desktop.adb.AdbManager.setAppContext(mContext);
                com.c11partner.desktop.adb.AdbManager.initCrypto();
                
                // 尝试连接到本地ADB服务
                boolean success = com.c11partner.desktop.adb.AdbManager.connect("127.0.0.1", 5555);
                
                mActivity.runOnUiThread(() -> {
                    if (success) {
                        Toast.makeText(mContext, "ADB连接尝试成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "ADB连接尝试失败", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "执行ADB授权时出错", e);
            }
        }).start();
    }
    
    /**
     * 检查USB调试是否已启用
     * @return 是否已启用
     */
    private boolean isUsbDebuggingEnabled() {
        try {
            // 通过ADB命令检查USB调试状态
            String result = android.provider.Settings.Global.getString(
                mContext.getContentResolver(), 
                android.provider.Settings.Global.ADB_ENABLED
            );
            return "1".equals(result);
        } catch (Exception e) {
            Log.e(TAG, "检查USB调试状态时出错", e);
            return false;
        }
    }
    
    /**
     * 执行ADB命令
     */
    private void executeAdbCommands() {
        try {
            // 初始化ADB加密密钥
            com.c11partner.desktop.adb.AdbManager.setAppContext(mContext);
            com.c11partner.desktop.adb.AdbManager.initCrypto();
            
            // 尝试连接到本地ADB服务，优先尝试5554端口
            int[] portsToTry = {5554, 5555, 5556, 5557, 5558, 5559};
            boolean connected = false;
            
            for (int port : portsToTry) {
                try {
                    com.c11partner.desktop.adb.AdbManager.connect("localhost", port);
                    Log.d(TAG, "ADB连接成功，端口: " + port);
                    connected = true;
                    
                    // 连接成功后执行命令
                    executeAdbCommandsAfterConnection();
                    break;
                } catch (Exception connectException) {
                    Log.d(TAG, "ADB连接端口 " + port + " 失败: " + connectException.getMessage());
                }
            }
            
            if (!connected) {
                Log.e(TAG, "所有ADB端口都无法连接");
                mActivity.runOnUiThread(() -> {
                    // 提供详细的连接失败信息和解决方案
                    String errorMessage = "ADB连接失败: 无法连接到任何ADB端口\n\n可能的解决方案:\n" +
                        "1. 确保设备已启用USB调试\n" +
                        "2. 检查USB线缆连接是否正常\n" +
                        "3. 确认设备已授权此电脑的RSA密钥\n" +
                        "4. 尝试重新插拔USB线缆\n" +
                        "5. 检查防火墙设置是否阻止了连接";
                    
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
                    builder.setTitle("ADB连接失败")
                        .setMessage(errorMessage)
                        .setPositiveButton("确定", null)
                        .show();
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "初始化ADB时出错", e);
            mActivity.runOnUiThread(() -> {
                Toast.makeText(mContext, "初始化ADB时出错: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });
        }
    }
    
    /**
     * 连接成功后执行ADB命令
     */
    private void executeAdbCommandsAfterConnection() {
        // 执行授权命令
        String packageName = mContext.getPackageName();
        String command = "pm grant " + packageName + " android.permission.DUMP";
        
        // 使用简化的ADB连接来触发授权
        new Thread(() -> {
            try {
                com.c11partner.desktop.adb.AdbManager.setAppContext(mContext);
                com.c11partner.desktop.adb.AdbManager.initCrypto();
                
                // 尝试连接到本地ADB服务
                boolean success = com.c11partner.desktop.adb.AdbManager.connect("127.0.0.1", 5555);
                
                mActivity.runOnUiThread(() -> {
                    if (success) {
                        Toast.makeText(mContext, "ADB连接尝试成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "ADB连接尝试失败", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "执行ADB命令时出错", e);
            }
        }).start();
    }

    /**
     * 空调控制 - 使用反射调用BYDAutoAcDevice
     */
    private Object acDeviceInstance = null;
    private boolean acDeviceInitialized = false;

    /**
     * 初始化空调设备实例（使用反射）
     */
    private void initAcDevice() {
        if (acDeviceInitialized) {
            return;
        }
        
        try {
            Class<?> acDeviceClass = Class.forName("android.hardware.bydauto.ac.BYDAutoAcDevice");
            java.lang.reflect.Method getInstanceMethod = acDeviceClass.getMethod("getInstance", Context.class);
            acDeviceInstance = getInstanceMethod.invoke(null, mContext);
            acDeviceInitialized = true;
            Log.d(TAG, "空调设备实例初始化成功");
        } catch (Exception e) {
            Log.e(TAG, "初始化空调设备实例失败", e);
            acDeviceInitialized = false;
        }
    }

    /**
     * 切换空调开关状态
     */
    @JavascriptInterface
    public void toggleAirConditioning() {
        new Thread(() -> {
            Log.i(TAG, "开始切换空调开关状态");
            
            try {
                com.c11partner.desktop.adb.AdbIntentForwarder.setContext(mContext);
                
                String currentState = com.c11partner.desktop.adb.AdbIntentForwarder.getAcStartState();
                
                boolean isOn = currentState.contains("ac_start=true") || currentState.contains("1");
                
                String result = com.c11partner.desktop.adb.AdbIntentForwarder.toggleAirConditioning(!isOn);
                
                if (result.contains("执行成功") || result.contains("Broadcast completed")) {
                    Log.i(TAG, "空调开关切换成功：" + result);
                } else {
                    Log.e(TAG, "切换空调开关状态失败：" + result);
                }
            } catch (Exception e) {
                Log.e(TAG, "切换空调开关状态异常", e);
            }
        }).start();
    }

    /**
     * 调整空调温度
     * @param delta 温度变化值（正数为增加，负数为减少）
     */
    @JavascriptInterface
    public void adjustTemperature(int delta) {
        try {
            initAcDevice();
            if (acDeviceInstance == null) {
                Log.e(TAG, "空调设备实例未初始化");
                return;
            }

            Class<?> acDeviceClass = acDeviceInstance.getClass();
            
            // 获取当前温度
            java.lang.reflect.Method getTempratureMethod = acDeviceClass.getMethod("getTemprature", int.class);
            int AC_TEMPERATURE_MAIN = getStaticIntValue(acDeviceClass, "AC_TEMPERATURE_MAIN");
            int currentTemp = (int) getTempratureMethod.invoke(acDeviceInstance, AC_TEMPERATURE_MAIN);
            
            // 计算新温度（限制在17-30度之间）
            int newTemp = Math.max(17, Math.min(30, currentTemp + delta));
            
            // 设置新温度
            java.lang.reflect.Method setAcTemperatureMethod = acDeviceClass.getMethod("setAcTemperature", int.class, int.class, int.class, int.class);
            int AC_CTRL_SOURCE_UI_KEY = getStaticIntValue(acDeviceClass, "AC_CTRL_SOURCE_UI_KEY");
            int AC_TEMPERATURE_UNIT_OC = getStaticIntValue(acDeviceClass, "AC_TEMPERATURE_UNIT_OC");
            int AC_TEMPERATURE_MAIN_DEPUTY = getStaticIntValue(acDeviceClass, "AC_TEMPERATURE_MAIN_DEPUTY");
            
            setAcTemperatureMethod.invoke(acDeviceInstance, AC_TEMPERATURE_MAIN_DEPUTY, newTemp, AC_CTRL_SOURCE_UI_KEY, AC_TEMPERATURE_UNIT_OC);
            Log.d(TAG, "温度已调整: " + currentTemp + " -> " + newTemp);
        } catch (Exception e) {
            Log.e(TAG, "调整空调温度失败", e);
        }
    }

    /**
     * 调整空调风量
     * @param delta 风量变化值（正数为增加，负数为减少）
     */
    @JavascriptInterface
    public void adjustWindLevel(int delta) {
        try {
            initAcDevice();
            if (acDeviceInstance == null) {
                Log.e(TAG, "空调设备实例未初始化");
                return;
            }

            Class<?> acDeviceClass = acDeviceInstance.getClass();
            
            // 获取当前风量
            java.lang.reflect.Method getAcWindLevelMethod = acDeviceClass.getMethod("getAcWindLevel");
            int currentWindLevel = (int) getAcWindLevelMethod.invoke(acDeviceInstance);
            
            // 计算新风量（限制在0-7级之间）
            int newWindLevel = Math.max(0, Math.min(7, currentWindLevel + delta));
            
            // 设置新风量
            java.lang.reflect.Method setAcWindLevelMethod = acDeviceClass.getMethod("setAcWindLevel", int.class, int.class);
            int AC_CTRL_SOURCE_UI_KEY = getStaticIntValue(acDeviceClass, "AC_CTRL_SOURCE_UI_KEY");
            
            setAcWindLevelMethod.invoke(acDeviceInstance, AC_CTRL_SOURCE_UI_KEY, newWindLevel);
            Log.d(TAG, "风量已调整: " + currentWindLevel + " -> " + newWindLevel);
        } catch (Exception e) {
            Log.e(TAG, "调整空调风量失败", e);
        }
    }

    /**
     * 切换前除霜状态
     */
    @JavascriptInterface
    public void toggleDefrost() {
        try {
            initAcDevice();
            if (acDeviceInstance == null) {
                Log.e(TAG, "空调设备实例未初始化");
                return;
            }

            Class<?> acDeviceClass = acDeviceInstance.getClass();
            
            // 获取当前除霜状态
            java.lang.reflect.Method getAcDefrostStateMethod = acDeviceClass.getMethod("getAcDefrostState", int.class);
            int AC_DEFROST_AREA_FRONT = getStaticIntValue(acDeviceClass, "AC_DEFROST_AREA_FRONT");
            int AC_DEFROST_STATE_ON = getStaticIntValue(acDeviceClass, "AC_DEFROST_STATE_ON");
            int AC_DEFROST_STATE_OFF = getStaticIntValue(acDeviceClass, "AC_DEFROST_STATE_OFF");
            int AC_CTRL_SOURCE_UI_KEY = getStaticIntValue(acDeviceClass, "AC_CTRL_SOURCE_UI_KEY");
            
            int currentState = (int) getAcDefrostStateMethod.invoke(acDeviceInstance, AC_DEFROST_AREA_FRONT);
            
            if (currentState == AC_DEFROST_STATE_ON) {
                // 关闭前除霜
                java.lang.reflect.Method setAcDefrostStateMethod = acDeviceClass.getMethod("setAcDefrostState", int.class, int.class, int.class);
                setAcDefrostStateMethod.invoke(acDeviceInstance, AC_CTRL_SOURCE_UI_KEY, AC_DEFROST_AREA_FRONT, AC_DEFROST_STATE_OFF);
                Log.d(TAG, "前除霜已关闭");
            } else {
                // 开启前除霜
                java.lang.reflect.Method setAcDefrostStateMethod = acDeviceClass.getMethod("setAcDefrostState", int.class, int.class, int.class);
                setAcDefrostStateMethod.invoke(acDeviceInstance, AC_CTRL_SOURCE_UI_KEY, AC_DEFROST_AREA_FRONT, AC_DEFROST_STATE_ON);
                Log.d(TAG, "前除霜已开启");
            }
        } catch (Exception e) {
            Log.e(TAG, "切换前除霜状态失败", e);
        }
    }

    /**
     * 检查是否有通知监听权限
     *
     * @return 是否有通知监听权限
     */
    @JavascriptInterface
    public boolean hasNotificationAccess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            android.content.ComponentName componentName = new android.content.ComponentName(
                    mContext,
                    com.c11partner.desktop.service.MusicNotificationListenerService.class
            );
            String packageName = mContext.getPackageName();
            String flat = android.provider.Settings.Secure.getString(
                    mContext.getContentResolver(),
                    "enabled_notification_listeners"
            );
            return flat != null && flat.contains(packageName);
        }
        return false;
    }


    /**
     * 获取静态整型常量值
     */
    private int getStaticIntValue(Class<?> clazz, String fieldName) {
        try {
            java.lang.reflect.Field field = clazz.getField(fieldName);
            return field.getInt(null);
        } catch (Exception e) {
            Log.e(TAG, "获取静态常量失败: " + fieldName, e);
            return 0;
        }
    }
    
    // ==================== 零跑C11车辆监控相关方法 ====================
    
    /**
     * 获取当前车辆状态
     * 
     * @return JSON格式的车辆状态信息
     */
    @JavascriptInterface
    public String getCarState() {
        try {
            if (mActivity.getLogcatMonitorService() != null) {
                com.c11partner.desktop.LeapMotorCarState state = mActivity.getLogcatMonitorService().getCurrentState();
                if (state != null) {
                    JSONObject stateJson = new JSONObject();
                    stateJson.put("gear", state.getGear());
                    stateJson.put("gearText", state.getGearText());
                    stateJson.put("leftTurnLight", state.getLeftTurnLight());
                    stateJson.put("rightTurnLight", state.getRightTurnLight());
                    stateJson.put("speed", state.getSpeed());
                    stateJson.put("frontLeftDoor", state.getFrontLeftDoor());
                    stateJson.put("frontRightDoor", state.getFrontRightDoor());
                    stateJson.put("rearLeftDoor", state.getRearLeftDoor());
                    stateJson.put("rearRightDoor", state.getRearRightDoor());
                    stateJson.put("trunkDoor", state.getTrunkDoor());
                    stateJson.put("hoodDoor", state.getHoodDoor());
                    stateJson.put("sunroof", state.getSunroof());
                    stateJson.put("sunshade", state.getSunshade());
                    stateJson.put("isAnyDoorOpen", state.isAnyDoorOpen());
                    stateJson.put("isLocked", state.isLocked());
                    return stateJson.toString();
                }
            }
            return "{}";
        } catch (Exception e) {
            Log.e(TAG, "获取车辆状态失败", e);
            return "{}";
        }
    }
    
    /**
     * 手动触发360全景
     * 
     * @return 是否触发成功
     */
    @JavascriptInterface
    public boolean startCamera360() {
        try {
            com.c11partner.desktop.LeapMotorCamera360.startCamera360(mContext, "手动触发");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "手动触发360全景失败", e);
            return false;
        }
    }
    
    /**
     * 检查日志监控服务是否正在运行
     * 
     * @return 是否正在运行
     */
    @JavascriptInterface
    public boolean isLogServiceRunning() {
        return mActivity.isLogServiceBound();
    }
    
    /**
     * 检查是否有READ_LOGS权限
     * 
     * @return 是否有权限
     */
    @JavascriptInterface
    public boolean hasReadLogsPermission() {
        try {
            int result = mContext.checkCallingOrSelfPermission("android.permission.READ_LOGS");
            return result == PackageManager.PERMISSION_GRANTED;
        } catch (Exception e) {
            Log.e(TAG, "检查READ_LOGS权限失败", e);
            return false;
        }
    }
    
    /**
     * 检查是否有WRITE_SECURE_SETTINGS权限
     * 
     * @return 是否有权限
     */
    @JavascriptInterface
    public boolean hasWriteSecureSettingsPermission() {
        try {
            int result = mContext.checkCallingOrSelfPermission("android.permission.WRITE_SECURE_SETTINGS");
            return result == PackageManager.PERMISSION_GRANTED;
        } catch (Exception e) {
            Log.e(TAG, "检查WRITE_SECURE_SETTINGS权限失败", e);
            return false;
        }
    }
    
    /**
     * 检查是否有DUMP权限
     * 
     * @return 是否有权限
     */
    @JavascriptInterface
    public boolean hasDumpPermission() {
        try {
            int result = mContext.checkCallingOrSelfPermission("android.permission.DUMP");
            return result == PackageManager.PERMISSION_GRANTED;
        } catch (Exception e) {
            Log.e(TAG, "检查DUMP权限失败", e);
            return false;
        }
    }
}

