package com.c11partner.desktop.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 壁纸下载工具类
 * 负责从网络下载壁纸并保存到本地存储
 */
public class WallpaperDownloadUtils {
    private static final String TAG = "WallpaperDownloadUtils";
    
    /**
     * 下载壁纸并保存到指定目录
     * @param context 应用上下文
     * @param wallpaperUrl 壁纸URL
     * @param categoryId 分类ID
     * @return 保存的文件路径，失败返回null
     */
    public static String downloadAndSaveWallpaper(Context context, String wallpaperUrl, String categoryId) {
        try {
            // 检查URL是否有效
            if (wallpaperUrl == null || wallpaperUrl.isEmpty()) {
                Log.e(TAG, "壁纸URL为空");
                return null;
            }
            
            // 构建保存路径 /sdcard/dipartner/分类id/
            File rootDir = Environment.getExternalStorageDirectory();
            File fstartDir = new File(rootDir, "c11partner");
            File categoryDir = new File(fstartDir, categoryId);
            
            // 创建目录
            if (!createDirectory(categoryDir)) {
                Log.e(TAG, "创建目录失败: " + categoryDir.getAbsolutePath());
                return null;
            }
            
            // 生成文件名（使用时间戳确保唯一性）
            String fileName = System.currentTimeMillis() + ".jpg";
            File wallpaperFile = new File(categoryDir, fileName);
            
            // 下载壁纸
            if (downloadFile(wallpaperUrl, wallpaperFile)) {
                Log.d(TAG, "壁纸下载并保存成功: " + wallpaperFile.getAbsolutePath());
                return wallpaperFile.getAbsolutePath();
            } else {
                Log.e(TAG, "壁纸下载失败: " + wallpaperUrl);
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "下载壁纸时出错", e);
            return null;
        }
    }
    
    /**
     * 下载壁纸并返回Base64编码的图片数据
     * @param wallpaperUrl 壁纸URL
     * @return Base64编码的图片数据，失败返回null
     */
    public static String downloadAndEncodeWallpaper(String wallpaperUrl) {
        try {
            // 检查URL是否有效
            if (wallpaperUrl == null || wallpaperUrl.isEmpty()) {
                Log.e(TAG, "壁纸URL为空");
                return null;
            }
            
            // 下载图片数据
            byte[] imageData = downloadImageData(wallpaperUrl);
            if (imageData != null) {
                // 将图片数据转换为Base64编码
                return Base64.encodeToString(imageData, Base64.NO_WRAP);
            }
        } catch (Exception e) {
            Log.e(TAG, "下载并编码壁纸时出错", e);
        }
        return null;
    }
    
    /**
     * 从URL下载图片数据
     * @param imageUrl 图片URL
     * @return 图片数据字节数组，失败返回null
     */
    private static byte[] downloadImageData(String imageUrl) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        
        try {
            URL url = new URL(imageUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000); // 10秒连接超时
            connection.setReadTimeout(30000);    // 30秒读取超时
            
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                Log.e(TAG, "HTTP响应码错误: " + responseCode);
                return null;
            }
            
            inputStream = connection.getInputStream();
            outputStream = new ByteArrayOutputStream();
            
            byte[] buffer = new byte[4096];
            int bytesRead;
            
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            
            return outputStream.toByteArray();
        } catch (Exception e) {
            Log.e(TAG, "下载图片数据时出错", e);
            return null;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e) {
                Log.e(TAG, "关闭资源时出错", e);
            }
        }
    }
    
    /**
     * 创建目录，包含更完善的错误处理
     * @param directory 要创建的目录
     * @return 是否成功创建
     */
    private static boolean createDirectory(File directory) {
        // 检查目录是否已存在
        if (directory.exists()) {
            if (directory.isDirectory()) {
                return true;
            } else {
                Log.e(TAG, "路径存在但不是目录: " + directory.getAbsolutePath());
                return false;
            }
        }
        
        // 尝试创建目录
        if (directory.mkdirs()) {
            return true;
        }
        
        // 如果创建失败，尝试检查父目录权限
        File parent = directory.getParentFile();
        if (parent != null && !parent.exists()) {
            if (!createDirectory(parent)) {
                Log.e(TAG, "创建父目录失败: " + parent.getAbsolutePath());
                return false;
            }
        }
        
        // 再次尝试创建目录
        return directory.mkdirs();
    }
    
    /**
     * 从URL下载文件并保存到指定位置
     * @param fileUrl 文件URL
     * @param outputFile 输出文件
     * @return 是否成功
     */
    private static boolean downloadFile(String fileUrl, File outputFile) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        
        try {
            URL url = new URL(fileUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000); // 10秒连接超时
            connection.setReadTimeout(30000);    // 30秒读取超时
            
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                Log.e(TAG, "HTTP响应码错误: " + responseCode);
                return false;
            }
            
            inputStream = connection.getInputStream();
            outputStream = new FileOutputStream(outputFile);
            
            byte[] buffer = new byte[4096];
            int bytesRead;
            
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            
            outputStream.flush();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "下载文件时出错", e);
            return false;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e) {
                Log.e(TAG, "关闭资源时出错", e);
            }
        }
    }
    
    /**
     * 获取本地指定分类的壁纸文件列表
     * @param context 应用上下文
     * @param categoryId 分类ID
     * @return 壁纸文件路径列表
     */
    public static List<String> getLocalWallpapers(Context context, String categoryId) {
        try {
            File rootDir = Environment.getExternalStorageDirectory();
            File categoryDir = new File(new File(rootDir, "c11partner"), categoryId);
            
            if (!categoryDir.exists() || !categoryDir.isDirectory()) {
                return new java.util.ArrayList<>();
            }
            
            File[] files = categoryDir.listFiles((dir, name) -> 
                name.toLowerCase().endsWith(".jpg") || 
                name.toLowerCase().endsWith(".png") || 
                name.toLowerCase().endsWith(".jpeg"));
            
            List<String> wallpaperPaths = new java.util.ArrayList<>();
            if (files != null) {
                for (File file : files) {
                    wallpaperPaths.add(file.getAbsolutePath());
                }
            }
            
            return wallpaperPaths;
        } catch (Exception e) {
            Log.e(TAG, "获取本地壁纸列表时出错", e);
            return new java.util.ArrayList<>();
        }
    }
    
    /**
     * 从本地壁纸中随机选择一张
     * @param context 应用上下文
     * @param categoryId 分类ID
     * @return 壁纸文件路径，如果没有壁纸则返回null
     */
    public static String getRandomLocalWallpaper(Context context, String categoryId) {
        List<String> wallpapers = getLocalWallpapers(context, categoryId);
        
        if (wallpapers.isEmpty()) {
            return null;
        }
        
        // 随机选择一张壁纸
        java.util.Random random = new java.util.Random();
        return wallpapers.get(random.nextInt(wallpapers.size()));
    }
    
    /**
     * 下载壁纸并返回Base64编码的图片数据
     * @param wallpaperUrl 壁纸URL
     * @return Base64编码的图片数据，失败返回空字符串
     */
    public static String downloadAndEncodeToBase64(String wallpaperUrl) {
        String result = downloadAndEncodeWallpaper(wallpaperUrl);
        return result != null ? result : "";
    }
    
    /**
     * 将图片文件转换为Base64编码
     * @param imagePath 图片文件路径
     * @return Base64编码的图片数据
     */
    public static String encodeImageToBase64(String imagePath) {
        try {
            // 处理file://前缀
            if (imagePath.startsWith("file://")) {
                imagePath = imagePath.substring(7);
            }
            
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                return "";
            }
            
            // 读取图片文件
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            if (bitmap == null) {
                return "";
            }
            
            // 将Bitmap转换为Base64
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        } catch (Exception e) {
            Log.e(TAG, "编码图片到Base64时出错", e);
            return "";
        }
    }
}