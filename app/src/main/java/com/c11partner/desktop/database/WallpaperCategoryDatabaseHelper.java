package com.c11partner.desktop.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 在线壁纸分类数据库帮助类
 * 用于管理在线壁纸分类信息的SQLite数据库
 */
public class WallpaperCategoryDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "WallpaperCategoryDB";
    private static final String DATABASE_NAME = "wallpaper_categories.db";
    private static final int DATABASE_VERSION = 1;
    
    // 表名
    public static final String TABLE_CATEGORIES = "categories";
    
    // 列名
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_COUNT = "count";
    public static final String COLUMN_ENABLED = "enabled";
    
    // 创建表的SQL语句
    private static final String CREATE_TABLE_CATEGORIES = 
            "CREATE TABLE " + TABLE_CATEGORIES + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CATEGORY_ID + " TEXT NOT NULL UNIQUE, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_COUNT + " INTEGER NOT NULL, " +
            COLUMN_ENABLED + " INTEGER NOT NULL DEFAULT 0);";
    
    private static WallpaperCategoryDatabaseHelper instance;
    
    /**
     * 私有构造函数
     * @param context 应用上下文
     */
    private WallpaperCategoryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    /**
     * 获取单例实例
     * @param context 应用上下文
     * @return WallpaperCategoryDatabaseHelper实例
     */
    public static synchronized WallpaperCategoryDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new WallpaperCategoryDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表
        db.execSQL(CREATE_TABLE_CATEGORIES);
        Log.d(TAG, "壁纸分类数据库表创建成功");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 升级数据库时删除旧表并重新创建
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }
    
    /**
     * 插入或更新分类信息
     * @param categoryId 分类ID
     * @param name 分类名称
     * @param count 壁纸数量
     * @param enabled 是否启用
     * @return 插入或更新的行ID，失败返回-1
     */
    public long insertOrUpdateCategory(String categoryId, String name, int count, boolean enabled) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_ID, categoryId);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_COUNT, count);
        values.put(COLUMN_ENABLED, enabled ? 1 : 0);
        
        // 使用replace方法实现插入或更新
        long result = db.replace(TABLE_CATEGORIES, null, values);
        db.close();
        return result;
    }
    
    /**
     * 批量插入或更新分类信息
     * @param categories 分类列表
     */
    public void bulkInsertOrUpdateCategories(List<Map<String, Object>> categories) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Map<String, Object> category : categories) {
                String categoryId = (String) category.get("id");
                String name = (String) category.get("name");
                int count = (int) category.get("count");
                boolean enabled = (boolean) category.get("enabled");
                
                ContentValues values = new ContentValues();
                values.put(COLUMN_CATEGORY_ID, categoryId);
                values.put(COLUMN_NAME, name);
                values.put(COLUMN_COUNT, count);
                values.put(COLUMN_ENABLED, enabled ? 1 : 0);
                
                db.replace(TABLE_CATEGORIES, null, values);
            }
            db.setTransactionSuccessful();
            Log.d(TAG, "批量插入或更新分类信息成功，共处理 " + categories.size() + " 个分类");
        } catch (Exception e) {
            Log.e(TAG, "批量插入或更新分类信息失败", e);
        } finally {
            db.endTransaction();
            db.close();
        }
    }
    
    /**
     * 获取所有分类信息
     * @return 分类列表
     */
    public List<Map<String, Object>> getAllCategories() {
        List<Map<String, Object>> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORIES, null, null, null, null, null, COLUMN_ID + " ASC");
        
        if (cursor.moveToFirst()) {
            do {
                Map<String, Object> category = new HashMap<>();
                category.put("id", cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_ID)));
                category.put("name", cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                category.put("count", cursor.getInt(cursor.getColumnIndex(COLUMN_COUNT)));
                category.put("enabled", cursor.getInt(cursor.getColumnIndex(COLUMN_ENABLED)) == 1);
                categories.add(category);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return categories;
    }
    
    /**
     * 根据分类ID获取分类信息
     * @param categoryId 分类ID
     * @return 分类信息
     */
    public Map<String, Object> getCategoryById(String categoryId) {
        Map<String, Object> category = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORIES, null, COLUMN_CATEGORY_ID + "=?", 
                new String[]{categoryId}, null, null, null);
        
        if (cursor.moveToFirst()) {
            category = new HashMap<>();
            category.put("id", cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_ID)));
            category.put("name", cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            category.put("count", cursor.getInt(cursor.getColumnIndex(COLUMN_COUNT)));
            category.put("enabled", cursor.getInt(cursor.getColumnIndex(COLUMN_ENABLED)) == 1);
        }
        
        cursor.close();
        db.close();
        return category;
    }
    
    /**
     * 更新分类启用状态
     * @param categoryId 分类ID
     * @param enabled 是否启用
     */
    public void updateCategoryEnabled(String categoryId, boolean enabled) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        // 检查分类是否存在
        Cursor cursor = db.query(TABLE_CATEGORIES, null, COLUMN_CATEGORY_ID + "=?", 
                new String[]{categoryId}, null, null, null);
        
        if (cursor.moveToFirst()) {
            // 分类存在，更新状态
            ContentValues values = new ContentValues();
            values.put(COLUMN_ENABLED, enabled ? 1 : 0);
            
            int rowsUpdated = db.update(TABLE_CATEGORIES, values, COLUMN_CATEGORY_ID + "=?", new String[]{categoryId});
            Log.d(TAG, "更新分类启用状态: " + categoryId + " -> " + enabled + "，影响行数: " + rowsUpdated);
        } else {
            // 分类不存在，创建新记录
            ContentValues values = new ContentValues();
            values.put(COLUMN_CATEGORY_ID, categoryId);
            values.put(COLUMN_NAME, "未知分类" + categoryId);
            values.put(COLUMN_COUNT, 0);
            values.put(COLUMN_ENABLED, enabled ? 1 : 0);
            
            long newRowId = db.insert(TABLE_CATEGORIES, null, values);
            Log.d(TAG, "创建新分类并设置启用状态: " + categoryId + " -> " + enabled + "，新行ID: " + newRowId);
        }
        
        cursor.close();
        db.close();
    }
    
    /**
     * 清空分类表
     */
    public void clearCategories() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORIES, null, null);
        db.close();
        Log.d(TAG, "分类表已清空");
    }
    
    /**
     * 检查是否启用了在线壁纸
     * @return 是否启用在线壁纸
     */
    public boolean isOnlineWallpaperEnabled() {
        // 这个方法应该在WallpaperSettingsDatabaseHelper中实现，而不是这里
        // 返回默认值false
        return false;
    }
    
    /**
     * 检查是否启用了随机模式
     * @return 是否启用随机模式
     */
    public boolean isRandomModeEnabled() {
        // 这个方法应该在WallpaperSettingsDatabaseHelper中实现，而不是这里
        // 返回默认值true
        return true;
    }
    
    /**
     * 检查是否启用了指定模式
     * @return 是否启用指定模式
     */
    public boolean isSpecifiedModeEnabled() {
        // 这个方法应该在WallpaperSettingsDatabaseHelper中实现，而不是这里
        // 返回默认值false
        return false;
    }
}