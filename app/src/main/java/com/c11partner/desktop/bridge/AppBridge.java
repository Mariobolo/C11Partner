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
}