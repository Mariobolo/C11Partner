package com.c11partner.desktop.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 壁纸管理器
 * 统一管理各种类型的壁纸：默认、必应、本地图片、本地视频、本地文件夹、iframe（预留）
 */
public class WallpaperManager {
    private static final String TAG = "WallpaperManager";
    
    // 壁纸类型
    public static final int TYPE_DEFAULT = 0;      // 默认壁纸
    public static final int TYPE_BING = 1;         // 必应壁纸
    public static final int TYPE_LOCAL_IMAGE = 2;  // 本地图片
    public static final int TYPE_LOCAL_VIDEO = 3;  // 本地视频
    public static final int TYPE_LOCAL_FOLDER = 4; // 本地文件夹轮播
    public static final int TYPE_IFRAME = 5;       // iframe壁纸（预留）
    
    // 填充模式
    public static final int FILL_MODE_COVER = 0;   // 填充（裁剪）
    public static final int FILL_MODE_CONTAIN = 1; // 包含（留白）
    public static final int FILL_MODE_STRETCH = 2; // 拉伸
    
    // SharedPreferences 键名
    private static final String PREFS_NAME = "wallpaper_settings";
    private static final String KEY_WALLPAPER_TYPE = "wallpaper_type";
    private static final String KEY_WALLPAPER_PATH = "wallpaper_path";
    private static final String KEY_CAROUSEL_ENABLED = "carousel_enabled";
    private static final String KEY_CAROUSEL_INTERVAL = "carousel_interval";
    private static final String KEY_FILL_MODE = "fill_mode";
    private static final String KEY_BING_LAST_DATE = "bing_last_date";
    private static final String KEY_BING_CACHED_URL = "bing_cached_url";
    
    private static WallpaperManager instance;
    private Context mContext;
    private SharedPreferences mPrefs;
    private Random mRandom;
    
    // 文件夹轮播缓存
    private List<String> mFolderWallpapers = null;
    private int mCurrentFolderIndex = 0;
    
    /**
     * 私有构造函数
     */
    private WallpaperManager(Context context) {
        mContext = context.getApplicationContext();
        mPrefs = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mRandom = new Random();
    }
    
    /**
     * 获取单例实例
     */
    public static synchronized WallpaperManager getInstance(Context context) {
        if (instance == null) {
            instance = new WallpaperManager(context);
        }
        return instance;
    }
    
    // ==================== 设置相关 ====================
    
    /**
     * 获取壁纸类型
     * 已弃用 TYPE_DEFAULT，自动迁移为 TYPE_BING
     */
    public int getWallpaperType() {
        int type = mPrefs.getInt(KEY_WALLPAPER_TYPE, TYPE_BING);
        if (type == TYPE_DEFAULT) {
            type = TYPE_BING;
            mPrefs.edit().putInt(KEY_WALLPAPER_TYPE, TYPE_BING).apply();
        }
        return type;
    }
    
    /**
     * 设置壁纸类型
     */
    public void setWallpaperType(int type) {
        mPrefs.edit().putInt(KEY_WALLPAPER_TYPE, type).apply();
        // 清除文件夹缓存
        mFolderWallpapers = null;
        mCurrentFolderIndex = 0;
    }
    
    /**
     * 获取壁纸路径
     */
    public String getWallpaperPath() {
        return mPrefs.getString(KEY_WALLPAPER_PATH, "");
    }
    
    /**
     * 设置壁纸路径
     */
    public void setWallpaperPath(String path) {
        mPrefs.edit().putString(KEY_WALLPAPER_PATH, path).apply();
        // 清除文件夹缓存
        mFolderWallpapers = null;
        mCurrentFolderIndex = 0;
    }
    
    /**
     * 是否启用轮播
     */
    public boolean isCarouselEnabled() {
        return mPrefs.getBoolean(KEY_CAROUSEL_ENABLED, false);
    }
    
    /**
     * 设置是否启用轮播
     */
    public void setCarouselEnabled(boolean enabled) {
        mPrefs.edit().putBoolean(KEY_CAROUSEL_ENABLED, enabled).apply();
    }
    
    /**
     * 获取轮播间隔（毫秒）
     */
    public int getCarouselInterval() {
        return mPrefs.getInt(KEY_CAROUSEL_INTERVAL, 15000);
    }
    
    /**
     * 设置轮播间隔（毫秒）
     */
    public void setCarouselInterval(int intervalMs) {
        mPrefs.edit().putInt(KEY_CAROUSEL_INTERVAL, intervalMs).apply();
    }
    
    /**
     * 获取填充模式
     */
    public int getFillMode() {
        return mPrefs.getInt(KEY_FILL_MODE, FILL_MODE_COVER);
    }
    
    /**
     * 设置填充模式
     */
    public void setFillMode(int mode) {
        mPrefs.edit().putInt(KEY_FILL_MODE, mode).apply();
    }
    
    // ==================== 壁纸获取 ====================
    
    /**
     * 获取当前壁纸URL（用于前端显示）
     * @return 壁纸URL（file:// 或 http:// 或 data:）
     */
    public String getCurrentWallpaperUrl() {
        int type = getWallpaperType();
        switch (type) {
            case TYPE_BING:
                // 检查是否需要刷新必应壁纸
                String url = getBingWallpaperUrl();
                if (url.equals(getDefaultWallpaperUrl())) {
                    // 没有缓存，触发后台获取
                    refreshBingWallpaperIfNeeded();
                }
                return url;
            case TYPE_LOCAL_IMAGE:
                return getLocalImageWallpaperUrl();
            case TYPE_LOCAL_VIDEO:
                return getLocalVideoWallpaperUrl();
            case TYPE_LOCAL_FOLDER:
                return getNextFolderWallpaperUrl();
            case TYPE_IFRAME:
                return getIframeWallpaperUrl();
            case TYPE_DEFAULT:
            default:
                return "images/default_bg_1.jpg";
        }
    }
    
    /**
     * 获取下一张壁纸URL（用于轮播）
     */
    public String getNextWallpaperUrl() {
        int type = getWallpaperType();
        switch (type) {
            case TYPE_BING:
                // 必应壁纸每天只有一张，尝试刷新
                return refreshBingWallpaperIfNeeded();
            case TYPE_LOCAL_FOLDER:
                return getNextFolderWallpaperUrl();
            case TYPE_DEFAULT:
            case TYPE_LOCAL_IMAGE:
            case TYPE_LOCAL_VIDEO:
            case TYPE_IFRAME:
            default:
                // 单张壁纸，返回当前的
                return getCurrentWallpaperUrl();
        }
    }
    
    // ==================== 默认壁纸 ====================
    
    /**
     * 获取默认壁纸URL
     */
    private String getDefaultWallpaperUrl() {
        return "images/default_bg_1.jpg";
    }
    
    // ==================== 必应壁纸 ====================
    
    /**
     * 获取必应壁纸URL
     */
    private String getBingWallpaperUrl() {
        String cachedUrl = mPrefs.getString(KEY_BING_CACHED_URL, "");
        if (!cachedUrl.isEmpty()) {
            return cachedUrl;
        }
        // 没有缓存，返回默认壁纸
        return getDefaultWallpaperUrl();
    }
    
    /**
     * 如果需要，刷新必应壁纸
     * @return 壁纸URL
     */
    private String refreshBingWallpaperIfNeeded() {
        // 检查是否是新的一天
        String lastDate = mPrefs.getString(KEY_BING_LAST_DATE, "");
        String today = getTodayDateString();
        
        if (!today.equals(lastDate)) {
            // 新的一天，尝试刷新必应壁纸
            new Thread(new Runnable() {
                @Override
                public void run() {
                    fetchBingWallpaper();
                }
            }).start();
        }
        
        return getBingWallpaperUrl();
    }
    
    /**
     * 获取今日日期字符串
     */
    private String getTodayDateString() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new java.util.Date());
    }
    
    /**
     * 从网络获取必应壁纸
     */
    private void fetchBingWallpaper() {
        try {
            String apiUrl = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=zh-CN";
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                InputStream is = conn.getInputStream();
                String jsonStr = readStream(is);
                is.close();
                
                JSONObject json = new JSONObject(jsonStr);
                JSONArray images = json.getJSONArray("images");
                if (images.length() > 0) {
                    JSONObject image = images.getJSONObject(0);
                    String imageUrl = "https://cn.bing.com" + image.getString("url");
                    
                    // 下载并缓存图片
                    String cachedPath = downloadAndCacheImage(imageUrl, "bing_wallpaper.jpg");
                    if (cachedPath != null) {
                        mPrefs.edit()
                                .putString(KEY_BING_CACHED_URL, "file://" + cachedPath)
                                .putString(KEY_BING_LAST_DATE, getTodayDateString())
                                .apply();
                        Log.d(TAG, "必应壁纸更新成功: " + cachedPath);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "获取必应壁纸失败", e);
        }
    }
    
    /**
     * 下载并缓存图片
     */
    private String downloadAndCacheImage(String imageUrl, String fileName) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();
            
            InputStream is = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            
            if (bitmap != null) {
                File cacheDir = mContext.getCacheDir();
                File imageFile = new File(cacheDir, fileName);
                FileOutputStream fos = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.close();
                return imageFile.getAbsolutePath();
            }
        } catch (Exception e) {
            Log.e(TAG, "下载图片失败", e);
        }
        return null;
    }
    
    /**
     * 读取输入流为字符串
     */
    private String readStream(InputStream is) {
        try {
            java.io.ByteArrayOutputStream bo = new java.io.ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                bo.write(buffer, 0, len);
            }
            return bo.toString("UTF-8");
        } catch (Exception e) {
            return "";
        }
    }
    
    // ==================== 本地图片壁纸 ====================
    
    /**
     * 获取本地图片壁纸URL
     */
    private String getLocalImageWallpaperUrl() {
        String path = getWallpaperPath();
        if (!path.isEmpty() && new File(path).exists()) {
            return "file://" + path;
        }
        return getDefaultWallpaperUrl();
    }
    
    // ==================== 本地视频壁纸 ====================
    
    /**
     * 获取本地视频壁纸URL
     */
    private String getLocalVideoWallpaperUrl() {
        String path = getWallpaperPath();
        if (!path.isEmpty() && new File(path).exists()) {
            return "file://" + path;
        }
        return getDefaultWallpaperUrl();
    }
    
    // ==================== 本地文件夹轮播 ====================
    
    /**
     * 获取文件夹中的下一张壁纸
     */
    private String getNextFolderWallpaperUrl() {
        loadFolderWallpapersIfNeeded();
        
        if (mFolderWallpapers == null || mFolderWallpapers.isEmpty()) {
            return getDefaultWallpaperUrl();
        }
        
        if (mCurrentFolderIndex >= mFolderWallpapers.size()) {
            mCurrentFolderIndex = 0;
        }
        
        String wallpaper = mFolderWallpapers.get(mCurrentFolderIndex);
        mCurrentFolderIndex++;
        
        return "file://" + wallpaper;
    }
    
    /**
     * 加载文件夹中的壁纸列表
     */
    private void loadFolderWallpapersIfNeeded() {
        if (mFolderWallpapers != null) {
            return;
        }
        
        mFolderWallpapers = new ArrayList<>();
        String folderPath = getWallpaperPath();
        
        if (folderPath.isEmpty()) {
            return;
        }
        
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            return;
        }
        
        File[] files = folder.listFiles();
        if (files == null) {
            return;
        }
        
        // 支持的图片和视频格式
        String[] imageExts = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"};
        String[] videoExts = {".mp4", ".avi", ".mkv", ".mov", ".wmv"};
        
        for (File file : files) {
            if (file.isFile()) {
                String name = file.getName().toLowerCase();
                boolean isImage = false;
                boolean isVideo = false;
                
                for (String ext : imageExts) {
                    if (name.endsWith(ext)) {
                        isImage = true;
                        break;
                    }
                }
                
                for (String ext : videoExts) {
                    if (name.endsWith(ext)) {
                        isVideo = true;
                        break;
                    }
                }
                
                if (isImage || isVideo) {
                    mFolderWallpapers.add(file.getAbsolutePath());
                }
            }
        }
        
        // 随机打乱顺序
        java.util.Collections.shuffle(mFolderWallpapers, mRandom);
    }
    
    // ==================== iframe壁纸（预留） ====================
    
    /**
     * 获取iframe壁纸URL
     */
    private String getIframeWallpaperUrl() {
        // 暂时禁用，返回默认壁纸
        return getDefaultWallpaperUrl();
    }
    
    // ==================== 设置导出 ====================
    
    /**
     * 获取所有设置为JSON
     */
    public String getAllSettingsJson() {
        try {
            JSONObject json = new JSONObject();
            json.put("wallpaper_type", getWallpaperType());
            json.put("wallpaper_path", getWallpaperPath());
            json.put("carousel_enabled", isCarouselEnabled());
            json.put("carousel_interval", getCarouselInterval());
            json.put("fill_mode", getFillMode());
            return json.toString();
        } catch (Exception e) {
            Log.e(TAG, "获取设置JSON失败", e);
            return "{}";
        }
    }
    
    /**
     * 从JSON加载设置
     */
    public void loadSettingsFromJson(String jsonStr) {
        try {
            JSONObject json = new JSONObject(jsonStr);
            if (json.has("wallpaper_type")) {
                setWallpaperType(json.getInt("wallpaper_type"));
            }
            if (json.has("wallpaper_path")) {
                setWallpaperPath(json.getString("wallpaper_path"));
            }
            if (json.has("carousel_enabled")) {
                setCarouselEnabled(json.getBoolean("carousel_enabled"));
            }
            if (json.has("carousel_interval")) {
                setCarouselInterval(json.getInt("carousel_interval"));
            }
            if (json.has("fill_mode")) {
                setFillMode(json.getInt("fill_mode"));
            }
        } catch (Exception e) {
            Log.e(TAG, "加载设置JSON失败", e);
        }
    }
}
