package com.c11partner.desktop.bridge;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.c11partner.desktop.MainActivity;
import com.c11partner.desktop.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.c11partner.desktop.database.AppDatabaseHelper;
import com.c11partner.desktop.database.QuickAppDatabaseHelper;
import com.c11partner.desktop.database.ComponentConfigDatabaseHelper;
import com.c11partner.desktop.database.ConfigAppDatabaseHelper;

/**
 * 应用管理模块 Bridge
 * 
 * 负责所有应用管理相关的 JS 接口实现
 * 模块化拆分：从 WebViewBridge 中拆分出来
 * 
 * @author C11Partner
 * @version 1.2.0
 */
public class AppBridge extends BaseBridge {
    
    private static final String TAG = "AppBridge";
    
    // 应用管理相关
    private AppDatabaseHelper appDbHelper;
    private QuickAppDatabaseHelper quickAppDbHelper;
    private ComponentConfigDatabaseHelper componentConfigDbHelper;
    private ConfigAppDatabaseHelper configAppDbHelper;
    private PackageManager packageManager;
    
    // 应用列表缓存
    private String cachedAppList = null;
    private long cachedAppListTime = 0;
    private static final long CACHE_DURATION = 5 * 60 * 1000; // 5分钟缓存
    
    /**
     * 构造函数
     * @param context 上下文
     * @param activity MainActivity 实例
     */
    public AppBridge(Context context, MainActivity activity) {
        super(context, activity);
        this.appDbHelper = AppDatabaseHelper.getInstance(context);
        this.quickAppDbHelper = QuickAppDatabaseHelper.getInstance(context);
        this.componentConfigDbHelper = ComponentConfigDatabaseHelper.getInstance(context);
        this.configAppDbHelper = ConfigAppDatabaseHelper.getInstance(context);
        this.packageManager = context.getPackageManager();
    }
    
    // ==================== 应用列表相关 ====================
    
    /**
     * 获取应用列表（按字母分组）
     * 使用缓存机制提高性能，缓存有效期为5分钟
     * 
     * @return JSON格式的应用列表，按字母分组
     */
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
                    appObj.put("isSystemApp", app.get("isSystemApp"));
                    
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
            String result = appsData.toString();
            cachedAppList = result;
            cachedAppListTime = System.currentTimeMillis();
            
            long endTime = System.currentTimeMillis();
            Log.d(TAG, "获取应用列表总耗时: " + (endTime - startTime) + "ms");
            
            return result;
        } catch (Exception e) {
            Log.e(TAG, "获取应用列表时出错", e);
            return "{}";
        }
    }
    
    /**
     * 获取所有已安装应用列表（按字母分组）
     * @return 应用列表 JSON
     */
    public String getAllApps() {
        try {
            // 使用 AppUtils 获取所有已安装应用
            List<Map<String, Object>> allApps = AppUtils.getInstalledApps(mContext);
            return buildGroupedAppListJson(allApps);
        } catch (Exception e) {
            Log.e(TAG, "获取应用列表失败", e);
            return "{}";
        }
    }
    
    /**
     * 获取用户安装的应用列表
     * @return 应用列表 JSON
     */
    public String getUserApps() {
        try {
            List<Map<String, Object>> allApps = AppUtils.getInstalledApps(mContext);
            List<Map<String, Object>> userApps = new ArrayList<>();
            
            for (Map<String, Object> app : allApps) {
                Boolean isSystem = (Boolean) app.get("isSystemApp");
                if (isSystem == null || !isSystem) {
                    userApps.add(app);
                }
            }
            
            return buildGroupedAppListJson(userApps);
        } catch (Exception e) {
            Log.e(TAG, "获取用户应用列表失败", e);
            return "{}";
        }
    }
    
    /**
     * 获取系统应用列表
     * @return 应用列表 JSON
     */
    public String getSystemApps() {
        try {
            List<Map<String, Object>> allApps = AppUtils.getInstalledApps(mContext);
            List<Map<String, Object>> systemApps = new ArrayList<>();
            
            for (Map<String, Object> app : allApps) {
                Boolean isSystem = (Boolean) app.get("isSystemApp");
                if (isSystem != null && isSystem) {
                    systemApps.add(app);
                }
            }
            
            return buildGroupedAppListJson(systemApps);
        } catch (Exception e) {
            Log.e(TAG, "获取系统应用列表失败", e);
            return "{}";
        }
    }
    
    /**
     * 构建按字母分组的应用列表JSON
     * @param apps 应用列表
     * @return JSON字符串
     */
    private String buildGroupedAppListJson(List<Map<String, Object>> apps) {
        try {
            // 按首字母分组应用
            Map<String, List<Map<String, Object>>> groupedApps = new HashMap<>();
            
            for (Map<String, Object> app : apps) {
                String name = (String) app.get("name");
                String letter = getFirstLetter(name);
                
                if (!groupedApps.containsKey(letter)) {
                    groupedApps.put(letter, new ArrayList<Map<String, Object>>());
                }
                groupedApps.get(letter).add(app);
            }
            
            // 对每个分组内的应用按名称排序
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
            
            // 构建JSON结果
            JSONObject result = new JSONObject();
            JSONArray lettersArray = new JSONArray();
            JSONObject appsObj = new JSONObject();
            
            // 获取排序后的字母
            List<String> sortedLetters = new ArrayList<>(groupedApps.keySet());
            Collections.sort(sortedLetters);
            
            for (String letter : sortedLetters) {
                lettersArray.put(letter);
                JSONArray appArray = new JSONArray();
                for (Map<String, Object> app : groupedApps.get(letter)) {
                    JSONObject appObj = new JSONObject();
                    appObj.put("name", app.get("name"));
                    appObj.put("packageName", app.get("packageName"));
                    appObj.put("icon", app.get("icon"));
                    appObj.put("isSystemApp", app.get("isSystemApp"));
                    appArray.put(appObj);
                }
                appsObj.put(letter, appArray);
            }
            
            result.put("letters", lettersArray);
            result.put("apps", appsObj);
            result.put("total", apps.size());
            
            return result.toString();
        } catch (Exception e) {
            Log.e(TAG, "构建应用列表JSON失败", e);
            return "{}";
        }
    }
    
    /**
     * 获取名称的首字母
     * @param name 应用名称
     * @return 首字母（大写）
     */
    private String getFirstLetter(String name) {
        if (name == null || name.isEmpty()) {
            return "#";
        }
        
        char firstChar = Character.toUpperCase(name.charAt(0));
        if (Character.isLetter(firstChar)) {
            return String.valueOf(firstChar);
        } else if (firstChar >= '0' && firstChar <= '9') {
            return "#";
        } else {
            return "#";
        }
    }
    
    // ==================== 应用启动相关 ====================
    
    /**
     * 启动应用
     * @param packageName 应用包名
     */
    public void launchApp(String packageName) {
        try {
            Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(packageName);
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                Log.d(TAG, "启动应用: " + packageName);
            } else {
                Log.e(TAG, "无法找到应用: " + packageName);
            }
        } catch (Exception e) {
            Log.e(TAG, "启动应用时出错: " + packageName, e);
        }
    }
    
    /**
     * 打开应用信息页面
     * @param packageName 应用包名
     */
    public void openAppInfo(String packageName) {
        try {
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(android.net.Uri.parse("package:" + packageName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            Log.d(TAG, "打开应用信息: " + packageName);
        } catch (Exception e) {
            Log.e(TAG, "打开应用信息失败: " + packageName, e);
        }
    }
    
    // ==================== 快捷应用相关 ====================
    
    /**
     * 获取快捷应用列表
     * @return 快捷应用列表 JSON
     */
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
     * 添加快捷应用
     * @param name 应用名称
     * @param packageName 应用包名
     * @param iconBase64 应用图标Base64
     */
    public void addQuickApp(String name, String packageName, String iconBase64) {
        try {
            long result = quickAppDbHelper.insertQuickApp(name, packageName, iconBase64);
            if (result != -1) {
                runOnUiThread(() -> {
                    Toast.makeText(mContext, "已添加到快速启动: " + name, Toast.LENGTH_SHORT).show();
                });
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(mContext, "添加到快速启动失败: " + name, Toast.LENGTH_SHORT).show();
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "添加到快速启动应用时出错", e);
            runOnUiThread(() -> {
                Toast.makeText(mContext, "添加到快速启动失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }
    
    /**
     * 删除快速启动应用
     * @param packageName 应用包名
     */
    public void removeQuickApp(String packageName) {
        try {
            int result = quickAppDbHelper.deleteQuickApp(packageName);
            if (result > 0) {
                runOnUiThread(() -> {
                    Toast.makeText(mContext, "已从快速启动移除", Toast.LENGTH_SHORT).show();
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "删除快速启动应用时出错", e);
            runOnUiThread(() -> {
                Toast.makeText(mContext, "移除快速启动失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }
    
    /**
     * 检查应用是否已添加到快速启动
     * @param packageName 应用包名
     * @return 是否已添加
     */
    public boolean isQuickApp(String packageName) {
        try {
            return quickAppDbHelper.isQuickApp(packageName);
        } catch (Exception e) {
            Log.e(TAG, "检查快速启动应用时出错", e);
            return false;
        }
    }
    
    // ==================== 配置应用相关 ====================
    
    /**
     * 保存或更新配置的应用信息
     *
     * @param buttonId    按钮ID
     * @param appName     应用名称
     * @param packageName 应用包名
     * @param appIcon     应用图标Base64编码
     */
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
    
    // ==================== 应用信息相关 ====================
    
    /**
     * 获取应用信息
     * @param packageName 包名
     * @return 应用信息 JSON
     */
    public String getAppInfo(String packageName) {
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pkgInfo = pm.getPackageInfo(packageName, 0);
            ApplicationInfo appInfo = pkgInfo.applicationInfo;
            
            JSONObject result = new JSONObject();
            result.put("name", appInfo.loadLabel(pm).toString());
            result.put("packageName", packageName);
            result.put("versionName", pkgInfo.versionName);
            result.put("versionCode", pkgInfo.versionCode);
            result.put("isSystemApp", (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
            
            return result.toString();
        } catch (Exception e) {
            Log.e(TAG, "获取应用信息失败: " + packageName, e);
            return "{}";
        }
    }
    
    /**
     * 获取应用图标
     * @param packageName 包名
     * @return 图标 Base64
     */
    public String getAppIcon(String packageName) {
        try {
            PackageManager pm = mContext.getPackageManager();
            Drawable icon = pm.getApplicationIcon(packageName);
            return drawableToBase64(icon);
        } catch (Exception e) {
            Log.e(TAG, "获取应用图标失败: " + packageName, e);
            return "";
        }
    }
    
    /**
     * 将Drawable转换为Base64编码
     * @param drawable Drawable对象
     * @return Base64字符串
     */
    private String drawableToBase64(Drawable drawable) {
        if (drawable == null) {
            return "";
        }
        
        try {
            Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888
            );
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.NO_WRAP);
        } catch (Exception e) {
            Log.e(TAG, "Drawable转Base64失败", e);
            return "";
        }
    }
    
    /**
     * 检查应用是否已安装
     * @param packageName 包名
     * @return 是否已安装
     */
    public boolean isAppInstalled(String packageName) {
        try {
            PackageManager pm = mContext.getPackageManager();
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        } catch (Exception e) {
            Log.e(TAG, "检查应用安装状态失败: " + packageName, e);
            return false;
        }
    }
    
    // ==================== 应用图标缓存相关 ====================
    
    // 应用图标Base64缓存
    private final Map<String, String> appIconCache = new java.util.concurrent.ConcurrentHashMap<>();
    private final Map<String, Long> cacheTimestamps = new java.util.concurrent.ConcurrentHashMap<>();
    private static final long CACHE_EXPIRATION_TIME = 5 * 60 * 1000; // 5分钟缓存过期时间
    
    /**
     * 清理过期的应用图标缓存
     */
    private void clearAppIconCache() {
        long currentTime = System.currentTimeMillis();
        java.util.Iterator<Map.Entry<String, Long>> iterator = cacheTimestamps.entrySet().iterator();
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
     * @param packageName 应用包名
     * @param iconBase64 图标Base64数据
     */
    private void cacheAppIconBase64(String packageName, String iconBase64) {
        appIconCache.put(packageName, iconBase64);
        cacheTimestamps.put(packageName, System.currentTimeMillis());
    }
    
    /**
     * 获取缓存的应用图标Base64数据
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
    
    // ==================== 异步方法 ====================
    
    /**
     * 异步获取应用列表（按字母分组）
     * @param callbackId 回调ID
     */
    public void getAppListAsync(final String callbackId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 使用 AppUtils 获取所有已安装应用
                    List<Map<String, Object>> allApps = AppUtils.getInstalledApps(mContext);
                    final String result = buildGroupedAppListJson(allApps);
                    
                    // 在UI线程中执行JavaScript回调
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity != null && mActivity.getWebView() != null) {
                                String javascript = String.format(
                                        "javascript:window.handleGetAppListCallback('%s', %s)",
                                        callbackId, org.json.JSONObject.quote(result));
                                mActivity.getWebView().loadUrl(javascript);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "异步获取应用列表时出错", e);
                    // 在UI线程中执行JavaScript回调
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity != null && mActivity.getWebView() != null) {
                                String javascript = String.format(
                                        "javascript:window.handleGetAppListCallback('%s', %s)",
                                        callbackId, org.json.JSONObject.quote("{}"));
                                mActivity.getWebView().loadUrl(javascript);
                            }
                        }
                    });
                }
            }
        }).start();
    }
}