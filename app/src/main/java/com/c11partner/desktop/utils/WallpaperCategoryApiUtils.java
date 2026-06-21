package com.c11partner.desktop.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 在线壁纸分类API工具类
 * 用于从网络获取壁纸分类数据
 */
public class WallpaperCategoryApiUtils {
    private static final String TAG = "WallpaperCategoryApi";
    private static final String API_URL = "http://wallpaper.apc.360.cn/index.php?c=WallPaperAndroid&a=getAllCategories";
    
    /**
     * 从网络获取壁纸分类数据
     * @return 分类列表
     */
    public static List<Map<String, Object>> fetchCategoriesFromApi() {
        List<Map<String, Object>> categories = new ArrayList<>();
        
        try {
            URL url = new URL(API_URL);
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
                
                // 解析JSON响应
                JSONObject jsonResponse = new JSONObject(response.toString());
                if (jsonResponse.has("data")) {
                    JSONArray dataArray = jsonResponse.getJSONArray("data");
                    
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject categoryObj = dataArray.getJSONObject(i);
                        Map<String, Object> category = new HashMap<>();
                        category.put("id", categoryObj.getString("id"));
                        category.put("name", categoryObj.getString("name"));
                        category.put("count", categoryObj.getInt("totalcnt"));
                        category.put("enabled", false); // 默认不启用
                        
                        categories.add(category);
                    }
                }
            } else {
                Log.e(TAG, "HTTP请求失败，响应码: " + responseCode);
            }
            
            connection.disconnect();
        } catch (Exception e) {
            Log.e(TAG, "获取壁纸分类数据时出错", e);
        }
        
        return categories;
    }
    
    /**
     * 获取随机壁纸URL
     * @param categoryId 分类ID
     * @param start 起始位置
     * @return 壁纸URL
     */
    public static String fetchWallpaperUrl(String categoryId, int start) {
        try {
            String apiUrl = "http://wallpaper.apc.360.cn/index.php?c=WallPaperAndroid&a=getAppsByCategory&cid=" + 
                           categoryId + "&start=" + start + "&count=1";
            
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
                
                // 解析JSON响应
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
            Log.e(TAG, "获取壁纸URL时出错", e);
        }
        
        return "";
    }
}