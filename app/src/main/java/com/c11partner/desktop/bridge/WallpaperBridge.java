package com.c11partner.desktop.bridge;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.c11partner.desktop.MainActivity;
import com.c11partner.desktop.utils.WallpaperManager;
import com.c11partner.desktop.utils.WallpaperCategoryApiUtils;
import com.c11partner.desktop.utils.WallpaperDownloadUtils;
import com.c11partner.desktop.database.WallpaperCategoryDatabaseHelper;
import com.c11partner.desktop.database.WallpaperSettingsDatabaseHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import android.os.Environment;
/**
 * 壁纸功能模块 Bridge
 * 
 * 负责所有壁纸相关的 JS 接口实现
 * 模块化拆分：从 WebViewBridge 中拆分出来
 * 
 * @author C11Partner
 * @version 1.2.0
 */
public class WallpaperBridge extends BaseBridge {
    
    private static final String TAG = "WallpaperBridge";
    
    // 壁纸管理相关
    private WallpaperManager wallpaperManager;
    private WallpaperCategoryDatabaseHelper wallpaperDbHelper;
    private WallpaperSettingsDatabaseHelper wallpaperSettingsDbHelper;
    
    /**
     * 构造函数
     * @param context 上下文
     * @param activity MainActivity 实例
     */
    public WallpaperBridge(Context context, MainActivity activity) {
        super(context, activity);
        this.wallpaperManager = WallpaperManager.getInstance(context);
        this.wallpaperDbHelper = WallpaperCategoryDatabaseHelper.getInstance(context);
        this.wallpaperSettingsDbHelper = WallpaperSettingsDatabaseHelper.getInstance(context);
    }
    
    // ==================== 壁纸设置相关 ====================
    
    /**
     * 设置壁纸
     * @param path 壁纸路径
     * @return 是否成功
     */
    public boolean setWallpaper(String path) {
        try {
            wallpaperManager.setWallpaperPath(path);
            Log.d(TAG, "设置壁纸: " + path);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "设置壁纸失败", e);
            return false;
        }
    }
    
    /**
     * 获取当前壁纸
     * @return 当前壁纸路径
     */
    public String getCurrentWallpaper() {
        try {
            return wallpaperManager.getCurrentWallpaperUrl();
        } catch (Exception e) {
            Log.e(TAG, "获取当前壁纸失败", e);
            return "images/default_bg_1.jpg";
        }
    }
    
    /**
     * 获取壁纸设置（V2版本）
     * @return 壁纸设置JSON字符串
     */
    public String getWallpaperSettingsV2() {
        try {
            return wallpaperManager.getAllSettingsJson();
        } catch (Exception e) {
            Log.e(TAG, "获取壁纸设置失败", e);
            return "{}";
        }
    }
    
    /**
     * 设置壁纸类型
     * @param type 壁纸类型：0=默认,1=必应,2=本地图片,3=本地视频,4=本地文件夹,5=iframe
     */
    public void setWallpaperType(int type) {
        try {
            wallpaperManager.setWallpaperType(type);
            Log.d(TAG, "设置壁纸类型: " + type);
        } catch (Exception e) {
            Log.e(TAG, "设置壁纸类型失败", e);
        }
    }
    
    /**
     * 获取下一张壁纸URL（用于轮播）
     * @return 下一张壁纸URL
     */
    public String getNextWallpaperUrl() {
        try {
            return wallpaperManager.getNextWallpaperUrl();
        } catch (Exception e) {
            Log.e(TAG, "获取下一张壁纸URL失败", e);
            return "images/default_bg_1.jpg";
        }
    }
    
    /**
     * 保存壁纸轮播设置
     * @param enabled 是否启用轮播
     * @return 是否成功
     */
    public boolean saveWallpaperCarouselSetting(boolean enabled) {
        try {
            wallpaperSettingsDbHelper.updateWallpaperCarousel(enabled);
            // 重启壁纸轮播
            if (mActivity != null) {
                mActivity.restartWallpaperCarousel();
                // 发送壁纸设置更改广播
                Intent intent = new Intent(MainActivity.ACTION_WALLPAPER_SETTINGS_CHANGED);
                mActivity.sendBroadcast(intent);
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, "保存壁纸轮播设置时出错", e);
            return false;
        }
    }
    
    /**
     * 保存壁纸轮播时间间隔
     * @param interval 间隔时间（秒）
     * @return 是否成功
     */
    public boolean saveWallpaperSwitchInterval(int interval) {
        try {
            wallpaperSettingsDbHelper.updateSwitchInterval(interval);
            // 重启壁纸轮播
            if (mActivity != null) {
                mActivity.restartWallpaperCarousel();
                // 发送壁纸设置更改广播
                Intent intent = new Intent(MainActivity.ACTION_WALLPAPER_SETTINGS_CHANGED);
                mActivity.sendBroadcast(intent);
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, "保存壁纸轮播时间间隔时出错", e);
            return false;
        }
    }
    
    /**
     * 获取所有壁纸设置
     * @return 壁纸设置JSON字符串
     */
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
     * 暂停壁纸轮播
     */
    public void pauseWallpaperCarousel() {
        try {
            if (mActivity != null) {
                mActivity.pauseWallpaperCarousel();
                Log.d(TAG, "壁纸轮播已暂停");
            }
        } catch (Exception e) {
            Log.e(TAG, "暂停壁纸轮播失败", e);
        }
    }
    
    /**
     * 恢复壁纸轮播
     */
    public void resumeWallpaperCarousel() {
        try {
            if (mActivity != null) {
                mActivity.resumeWallpaperCarousel();
                Log.d(TAG, "壁纸轮播已恢复");
            }
        } catch (Exception e) {
            Log.e(TAG, "恢复壁纸轮播失败", e);
        }
    }
    
    /**
     * 删除当前壁纸
     * @return 是否成功
     */
    public boolean deleteCurrentWallpaper() {
        try {
            if (mActivity != null) {
                return mActivity.deleteCurrentWallpaper();
            }
            return false;
        } catch (Exception e) {
            Log.e(TAG, "删除当前壁纸失败", e);
            return false;
        }
    }
    
    // ==================== 壁纸分类相关 ====================
    
    /**
     * 更新壁纸分类
     * 从网络获取壁纸分类数据并保存到数据库
     */
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
                    runOnUiThread(() -> {
                        Toast.makeText(mContext, "壁纸分类已更新", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(mContext, "无法获取壁纸分类数据", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (Exception e) {
                Log.e(TAG, "更新壁纸分类时出错", e);
                runOnUiThread(() -> {
                    Toast.makeText(mContext, "更新壁纸分类时出错: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
    
    /**
     * 更新壁纸分类启用状态
     *
     * @param categoryId 分类ID
     * @param enabled    是否启用
     */
    public void updateCategoryEnabled(String categoryId, boolean enabled) {
        try {
            wallpaperDbHelper.updateCategoryEnabled(categoryId, enabled);
        } catch (Exception e) {
            Log.e(TAG, "更新分类启用状态时出错", e);
        }
    }
    
    /**
     * 获取已启用的分类ID列表
     *
     * @return JSON格式的分类ID列表
     */
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
     * 获取壁纸分类列表
     * @return 分类列表 JSON
     */
    public String getWallpaperCategories() {
        try {
            List<Map<String, Object>> categories = wallpaperDbHelper.getAllCategories();
            JSONArray categoriesArray = new JSONArray();
            for (Map<String, Object> category : categories) {
                JSONObject categoryObj = new JSONObject();
                categoryObj.put("id", category.get("id"));
                categoryObj.put("name", category.get("name"));
                categoryObj.put("enabled", category.get("enabled"));
                categoryObj.put("count", category.get("count"));
                categoriesArray.put(categoryObj);
            }
            return categoriesArray.toString();
        } catch (Exception e) {
            Log.e(TAG, "获取壁纸分类失败", e);
            return "[]";
        }
    }
    
    /**
     * 获取指定分类的壁纸列表
     * @param categoryId 分类ID
     * @return 壁纸列表 JSON
     */
    public String getWallpaperList(String categoryId) {
        try {
            // TODO: 实现获取壁纸列表逻辑
            return "[]";
        } catch (Exception e) {
            Log.e(TAG, "获取壁纸列表失败", e);
            return "[]";
        }
    }
    
    // ==================== 在线壁纸相关 ====================
    
    /**
     * 获取在线壁纸列表
     * @param page 页码
     * @param pageSize 每页数量
     * @return 壁纸列表 JSON
     */
    public String getOnlineWallpapers(int page, int pageSize) {
        try {
            // TODO: 实现在线壁纸获取逻辑
            return "[]";
        } catch (Exception e) {
            Log.e(TAG, "获取在线壁纸失败", e);
            return "[]";
        }
    }
    
    /**
     * 下载在线壁纸
     * @param url 壁纸URL
     * @param categoryId 分类ID
     * @return 是否成功
     */
    public boolean downloadWallpaper(String url, String categoryId) {
        try {
            // TODO: 实现壁纸下载逻辑
            Log.d(TAG, "下载壁纸: " + url);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "下载壁纸失败", e);
            return false;
        }
    }
    
    // ==================== 壁纸管理相关 ====================
    
    /**
     * 添加壁纸到收藏
     * @param path 壁纸路径
     * @return 是否成功
     */
    public boolean addToFavorites(String path) {
        try {
            // TODO: 实现收藏逻辑
            return true;
        } catch (Exception e) {
            Log.e(TAG, "添加收藏失败", e);
            return false;
        }
    }
    
    /**
     * 从收藏中移除壁纸
     * @param path 壁纸路径
     * @return 是否成功
     */
    public boolean removeFromFavorites(String path) {
        try {
            // TODO: 实现移除收藏逻辑
            return true;
        } catch (Exception e) {
            Log.e(TAG, "移除收藏失败", e);
            return false;
        }
    }
    
    /**
     * 获取收藏壁纸列表
     * @return 收藏列表 JSON
     */
    public String getFavoriteWallpapers() {
        try {
            // TODO: 实现获取收藏列表逻辑
            return "[]";
        } catch (Exception e) {
            Log.e(TAG, "获取收藏列表失败", e);
            return "[]";
        }
    }
    
    // ==================== 随机壁纸相关 ====================
    
    /**
     * 获取随机壁纸
     * @return 随机壁纸路径
     */
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
            return selectedFile.getAbsolutePath();
        } catch (Exception e) {
            Log.e(TAG, "从fstart/00目录获取随机壁纸时出错", e);
            return null;
        }
    }
    
    /**
     * 获取指定分类的随机壁纸
     * @param categoryId 分类ID
     * @return 随机壁纸路径
     */
    public String getRandomWallpaperByCategory(String categoryId) {
        try {
            // TODO: 实现分类随机壁纸逻辑
            return "";
        } catch (Exception e) {
            Log.e(TAG, "获取分类随机壁纸失败", e);
            return "";
        }
    }
    
    // ==================== 自动切换相关 ====================
    
    /**
     * 设置自动切换壁纸
     * @param enabled 是否启用
     * @param interval 切换间隔（分钟）
     * @return 是否成功
     */
    public boolean setAutoChangeWallpaper(boolean enabled, int interval) {
        try {
            // TODO: 实现自动切换逻辑
            return true;
        } catch (Exception e) {
            Log.e(TAG, "设置自动切换失败", e);
            return false;
        }
    }
    
    /**
     * 获取自动切换状态
     * @return 是否启用
     */
    public boolean isAutoChangeWallpaperEnabled() {
        try {
            // TODO: 实现获取自动切换状态逻辑
            return false;
        } catch (Exception e) {
            Log.e(TAG, "获取自动切换状态失败", e);
            return false;
        }
    }
}