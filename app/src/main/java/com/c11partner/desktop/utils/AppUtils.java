package com.c11partner.desktop.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * 应用工具类
 * 提供应用管理相关的工具方法，包括获取已安装应用、隐藏应用、快速启动应用等操作
 */
public class AppUtils {
    // 需要保留的系统应用包名白名单（根据设备厂商不同可能需要调整）
    private static final Set<String> SYSTEM_APP_WHITELIST = new HashSet<String>() {{
        add("com.android.dialer");          // 电话
        add("com.android.contacts");        // 通讯录
        add("com.android.mms");             // 短信
        add("com.android.camera");          // 相机
        add("com.android.browser");         // 浏览器
        add("com.android.chrome");          // Chrome浏览器
        add("com.google.android.dialer");   // Google电话
        add("com.huawei.camera");           // 华为相机
        add("com.sec.android.app.camera");  // 三星相机
    }};

    /**
     * 获取过滤后的应用列表（保留用户应用+关键系统应用）
     * 此方法会过滤掉隐藏的应用和不需要显示的系统应用
     *
     * @param context 应用上下文
     * @return 过滤后的应用列表，每个元素包含name、packageName和icon
     */
    public static List<Map<String, Object>> getInstalledApps(Context context) {
        long startTime = System.currentTimeMillis();
        List<Map<String, Object>> appList = new ArrayList<>();
        PackageManager pm = context.getPackageManager();

        // 新增：用于记录已处理的包名
        Set<String> processedPackages = new HashSet<>();
        // 查询所有带启动图标的APP（自动过滤服务类应用）
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LAUNCHER);
        
        // 优化：添加查询标志以提高性能
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(launcherIntent,
                PackageManager.MATCH_ALL);

        // 获取隐藏应用列表

        for (ResolveInfo info : resolveInfos) {
            try {
                // 优化：直接从ResolveInfo获取包名，避免重复调用
                String packageName = info.activityInfo.packageName;
                
                // 新增：跳过已处理的包
                if (processedPackages.contains(packageName)) {
                    continue;
                }
                processedPackages.add(packageName); // 记录已处理包名
                
                // 跳过自身应用
                if(packageName.equals("com.c11partner.desktop.)){
                    continue;
                }


                // 优化：直接获取ApplicationInfo，避免重复调用
                ApplicationInfo appInfo = info.activityInfo.applicationInfo;
                
                // 双重验证启动意图
                if (pm.getLaunchIntentForPackage(packageName) == null) continue;

                // 构建应用信息
                Map<String, Object> appData = new HashMap<>();
                appData.put("name", appInfo.loadLabel(pm));
                appData.put("packageName", packageName);
                
                // 优化：延迟加载图标，在需要时再加载
                appData.put("icon", appInfo.loadIcon(pm));
                appList.add(appData);
            } catch (Exception e) {
                // 优化：捕获所有异常，避免单个应用出错影响整个列表
                Log.w("AppUtils", "处理应用时出错: " + info.activityInfo.packageName, e);
            }
        }

        // 中文拼音排序
        long sortStartTime = System.currentTimeMillis();
        sortByChineseName(appList);
        long sortEndTime = System.currentTimeMillis();
        Log.d("AppUtils", "排序耗时: " + (sortEndTime - sortStartTime) + "ms");
        
        long endTime = System.currentTimeMillis();
        Log.d("AppUtils", "获取应用列表完成，总数: " + appList.size() + "，耗时: " + (endTime - startTime) + "ms");
        return appList;
    }



    /**
     * 中文拼音排序
     * 对应用列表按应用名称的中文拼音进行排序
     *
     * @param appList 要排序的应用列表
     */
    private static void sortByChineseName(List<Map<String, Object>> appList) {
        Collator collator = Collator.getInstance(Locale.CHINA);
        Collections.sort(appList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> app1, Map<String, Object> app2) {
                String name1 = (String) app1.get("name");
                String name2 = (String) app2.get("name");
                return collator.compare(name1, name2);
            }
        });
    }
}