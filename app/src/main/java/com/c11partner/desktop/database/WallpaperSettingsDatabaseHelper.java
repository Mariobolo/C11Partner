package com.c11partner.desktop.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * 壁纸设置数据库帮助类
 * 用于管理壁纸设置配置信息的SQLite数据库
 */
public class WallpaperSettingsDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "WallpaperSettingsDB";
    private static final String DATABASE_NAME = "wallpaper_settings.db";
    private static final int DATABASE_VERSION = 3; // 更新数据库版本
    
    // 表名
    public static final String TABLE_SETTINGS = "settings";
    
    // 列名
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_WALLPAPER_CAROUSEL = "wallpaper_carousel";
    public static final String COLUMN_LOCAL_WALLPAPER = "local_wallpaper";
    public static final String COLUMN_ONLINE_WALLPAPER = "online_wallpaper";
    public static final String COLUMN_SWITCH_INTERVAL = "switch_interval";
    public static final String COLUMN_SYSTEM_LAUNCHER = "system_launcher"; // 新增：原桌面自启
    public static final String COLUMN_BOOT_GREETING = "boot_greeting"; // 新增：开机问候语
    public static final String COLUMN_RANDOM_MODE = "random_mode"; // 新增：随机模式
    public static final String COLUMN_SPECIFIED_MODE = "specified_mode"; // 新增：指定模式
    
    // 创建表的SQL语句
    private static final String CREATE_TABLE_SETTINGS = 
            "CREATE TABLE " + TABLE_SETTINGS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_WALLPAPER_CAROUSEL + " INTEGER NOT NULL DEFAULT 0, " +
            COLUMN_LOCAL_WALLPAPER + " INTEGER NOT NULL DEFAULT 1, " +
            COLUMN_ONLINE_WALLPAPER + " INTEGER NOT NULL DEFAULT 0, " +
            COLUMN_SWITCH_INTERVAL + " INTEGER NOT NULL DEFAULT 15000, " +
            COLUMN_SYSTEM_LAUNCHER + " INTEGER NOT NULL DEFAULT 0, " + // 默认不启用原桌面自启
            COLUMN_BOOT_GREETING + " INTEGER NOT NULL DEFAULT 0, " + // 默认不启用开机问候语
            COLUMN_RANDOM_MODE + " INTEGER NOT NULL DEFAULT 1, " + // 默认启用随机模式
            COLUMN_SPECIFIED_MODE + " INTEGER NOT NULL DEFAULT 0);"; // 默认不启用指定模式
    
    private static WallpaperSettingsDatabaseHelper instance;
    
    /**
     * 私有构造函数
     * @param context 应用上下文
     */
    private WallpaperSettingsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    /**
     * 获取单例实例
     * @param context 应用上下文
     * @return WallpaperSettingsDatabaseHelper实例
     */
    public static synchronized WallpaperSettingsDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new WallpaperSettingsDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表
        db.execSQL(CREATE_TABLE_SETTINGS);
        Log.d(TAG, "壁纸设置数据库表创建成功");
        
        // 插入默认设置
        ContentValues values = new ContentValues();
        values.put(COLUMN_WALLPAPER_CAROUSEL, 0); // 默认不启用壁纸轮播
        values.put(COLUMN_LOCAL_WALLPAPER, 1);    // 默认启用本地壁纸
        values.put(COLUMN_ONLINE_WALLPAPER, 0);   // 默认不启用在线壁纸
        values.put(COLUMN_SWITCH_INTERVAL, 15000); // 默认轮播间隔15秒
        values.put(COLUMN_SYSTEM_LAUNCHER, 0); // 默认不启用原桌面自启
        values.put(COLUMN_BOOT_GREETING, 0); // 默认不启用开机问候语
        values.put(COLUMN_RANDOM_MODE, 1); // 默认启用随机模式
        values.put(COLUMN_SPECIFIED_MODE, 0); // 默认不启用指定模式
        db.insert(TABLE_SETTINGS, null, values);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 根据版本进行升级
        if (oldVersion < 2) {
            // 添加新的列
            db.execSQL("ALTER TABLE " + TABLE_SETTINGS + " ADD COLUMN " + COLUMN_SYSTEM_LAUNCHER + " INTEGER NOT NULL DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_SETTINGS + " ADD COLUMN " + COLUMN_BOOT_GREETING + " INTEGER NOT NULL DEFAULT 0");
        } else if (oldVersion < 3) {
            // 添加随机模式和指定模式列
            db.execSQL("ALTER TABLE " + TABLE_SETTINGS + " ADD COLUMN " + COLUMN_RANDOM_MODE + " INTEGER NOT NULL DEFAULT 1");
            db.execSQL("ALTER TABLE " + TABLE_SETTINGS + " ADD COLUMN " + COLUMN_SPECIFIED_MODE + " INTEGER NOT NULL DEFAULT 0");
        } else {
            // 升级数据库时删除旧表并重新创建
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
            onCreate(db);
        }
    }
    
    /**
     * 获取所有壁纸设置
     * @return 壁纸设置Map
     */
    public Map<String, Object> getAllSettings() {
        Map<String, Object> settings = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SETTINGS, null, null, null, null, null, null);
        
        if (cursor.moveToFirst()) {
            settings.put("wallpaper_carousel", cursor.getInt(cursor.getColumnIndex(COLUMN_WALLPAPER_CAROUSEL)) == 1);
            settings.put("local_wallpaper", cursor.getInt(cursor.getColumnIndex(COLUMN_LOCAL_WALLPAPER)) == 1);
            settings.put("online_wallpaper", cursor.getInt(cursor.getColumnIndex(COLUMN_ONLINE_WALLPAPER)) == 1);
            settings.put("switch_interval", cursor.getInt(cursor.getColumnIndex(COLUMN_SWITCH_INTERVAL)));
            settings.put("system_launcher", cursor.getInt(cursor.getColumnIndex(COLUMN_SYSTEM_LAUNCHER)) == 1); // 新增
            settings.put("boot_greeting", cursor.getInt(cursor.getColumnIndex(COLUMN_BOOT_GREETING)) == 1); // 新增
            settings.put("random_mode", cursor.getInt(cursor.getColumnIndex(COLUMN_RANDOM_MODE)) == 1); // 新增
            settings.put("specified_mode", cursor.getInt(cursor.getColumnIndex(COLUMN_SPECIFIED_MODE)) == 1); // 新增
        } else {
            // 如果没有数据，返回默认值
            settings.put("wallpaper_carousel", false);
            settings.put("local_wallpaper", true);
            settings.put("online_wallpaper", false);
            settings.put("switch_interval", 15000);
            settings.put("system_launcher", false); // 默认值
            settings.put("boot_greeting", false); // 默认值
            settings.put("random_mode", true); // 默认值
            settings.put("specified_mode", false); // 默认值
        }
        
        cursor.close();
        db.close();
        return settings;
    }
    
    /**
     * 更新壁纸轮播设置
     * @param enabled 是否启用
     */
    public void updateWallpaperCarousel(boolean enabled) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WALLPAPER_CAROUSEL, enabled ? 1 : 0);
        
        db.update(TABLE_SETTINGS, values, null, null);
        db.close();
        Log.d(TAG, "更新壁纸轮播设置: " + enabled);
    }
    
    /**
     * 更新本地壁纸设置
     * @param enabled 是否启用
     */
    public void updateLocalWallpaper(boolean enabled) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOCAL_WALLPAPER, enabled ? 1 : 0);
        
        db.update(TABLE_SETTINGS, values, null, null);
        db.close();
        Log.d(TAG, "更新本地壁纸设置: " + enabled);
    }
    
    /**
     * 更新在线壁纸设置
     * @param enabled 是否启用
     */
    public void updateOnlineWallpaper(boolean enabled) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ONLINE_WALLPAPER, enabled ? 1 : 0);
        
        db.update(TABLE_SETTINGS, values, null, null);
        db.close();
        Log.d(TAG, "更新在线壁纸设置: " + enabled);
    }
    
    /**
     * 更新壁纸轮播时间间隔
     * @param interval 时间间隔(毫秒)
     */
    public void updateSwitchInterval(int interval) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SWITCH_INTERVAL, interval);
        
        db.update(TABLE_SETTINGS, values, null, null);
        db.close();
        Log.d(TAG, "更新壁纸轮播时间间隔: " + interval);
    }
    
    /**
     * 更新原桌面自启设置
     * @param enabled 是否启用
     */
    public void updateSystemLauncher(boolean enabled) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SYSTEM_LAUNCHER, enabled ? 1 : 0);
        
        db.update(TABLE_SETTINGS, values, null, null);
        db.close();
        Log.d(TAG, "更新原桌面自启设置: " + enabled);
    }
    
    /**
     * 更新开机问候语设置
     * @param enabled 是否启用
     */
    public void updateBootGreeting(boolean enabled) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOT_GREETING, enabled ? 1 : 0);
        
        db.update(TABLE_SETTINGS, values, null, null);
        db.close();
        Log.d(TAG, "更新开机问候语设置: " + enabled);
    }
    
    /**
     * 更新随机模式设置
     * @param enabled 是否启用
     */
    public void updateRandomMode(boolean enabled) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RANDOM_MODE, enabled ? 1 : 0);
        
        db.update(TABLE_SETTINGS, values, null, null);
        db.close();
        Log.d(TAG, "更新随机模式设置: " + enabled);
    }
    
    /**
     * 更新指定模式设置
     * @param enabled 是否启用
     */
    public void updateSpecifiedMode(boolean enabled) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SPECIFIED_MODE, enabled ? 1 : 0);
        
        db.update(TABLE_SETTINGS, values, null, null);
        db.close();
        Log.d(TAG, "更新指定模式设置: " + enabled);
    }
}