package com.c11partner.desktop.bridge;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import com.c11partner.desktop.MainActivity;

import java.util.ArrayList;
import java.util.List;

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
     * 获取所有已安装应用列表
     * @return 应用列表 JSON
     */
    public String getAllApps() {
        try {
            PackageManager pm = mContext.getPackageManager();
            List<PackageInfo> packages = pm.getInstalledPackages(0);
            // TODO: 转换为 JSON 格式
            return "[]";
        } catch (Exception e) {
            Log.e(TAG, "获取应用列表失败", e);
            return "[]";
        }
    }
    
    /**
     * 获取用户安装的应用列表
     * @return 应用列表 JSON
     */
    public String getUserApps() {
        try {
            PackageManager pm = mContext.getPackageManager();
            List<PackageInfo> packages = pm.getInstalledPackages(0);
            List<PackageInfo> userApps = new ArrayList<>();
            
            for (PackageInfo pkg : packages) {
                if ((pkg.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    userApps.add(pkg);
                }
            }
            // TODO: 转换为 JSON 格式
            return "[]";
        } catch (Exception e) {
            Log.e(TAG, "获取用户应用列表失败", e);
            return "[]";
        }
    }
    
    /**
     * 获取系统应用列表
     * @return 应用列表 JSON
     */
    public String getSystemApps() {
        try {
            PackageManager pm = mContext.getPackageManager();
            List<PackageInfo> packages = pm.getInstalledPackages(0);
            List<PackageInfo> systemApps = new ArrayList<>();
            
            for (PackageInfo pkg : packages) {
                if ((pkg.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    systemApps.add(pkg);
                }
            }
            // TODO: 转换为 JSON 格式
            return "[]";
        } catch (Exception e) {
            Log.e(TAG, "获取系统应用列表失败", e);
            return "[]";
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
            // TODO: 转换为 JSON 格式
            return "{}";
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
            // TODO: 获取应用图标并转换为 Base64
            return "";
        } catch (Exception e) {
            Log.e(TAG, "获取应用图标失败: " + packageName, e);
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
}
