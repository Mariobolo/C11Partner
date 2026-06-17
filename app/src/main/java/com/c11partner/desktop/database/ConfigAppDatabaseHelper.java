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
 * 配置应用数据库帮助类
 * 用于存储长按配置的应用信息
 */
public class ConfigAppDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "ConfigAppDB";
    private static final String DATABASE_NAME = "config_apps.db";
    private static final int DATABASE_VERSION = 1;
    
    // 表名
    public static final String TABLE_CONFIG_APPS = "config_apps";
    
    // 列名
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_BUTTON_ID = "button_id"; // 按钮ID (如 goHomeBtn, goCompanyBtn)
    public static final String COLUMN_APP_NAME = "app_name";   // 应用名称
    public static final String COLUMN_PACKAGE_NAME = "package_name"; // 应用包名
    public static final String COLUMN_APP_ICON = "app_icon";   // 应用图标Base64编码
    
    // 创建表的SQL语句
    private static final String CREATE_TABLE_CONFIG_APPS = 
            "CREATE TABLE " + TABLE_CONFIG_APPS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_BUTTON_ID + " TEXT NOT NULL UNIQUE, " + // 按钮ID唯一
            COLUMN_APP_NAME + " TEXT, " +
            COLUMN_PACKAGE_NAME + " TEXT, " +
            COLUMN_APP_ICON + " TEXT);";
    
    private static ConfigAppDatabaseHelper instance;
    
    /**
     * 私有构造函数
     * @param context 应用上下文
     */
    private ConfigAppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    /**
     * 获取单例实例
     * @param context 应用上下文
     * @return ConfigAppDatabaseHelper实例
     */
    public static synchronized ConfigAppDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ConfigAppDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表
        db.execSQL(CREATE_TABLE_CONFIG_APPS);
        Log.d(TAG, "配置应用数据库表创建成功");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 升级数据库时删除旧表并重新创建
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIG_APPS);
        onCreate(db);
    }
    
    /**
     * 保存或更新配置的应用信息
     * @param buttonId 按钮ID
     * @param appName 应用名称
     * @param packageName 应用包名
     * @param appIcon 应用图标Base64编码
     * @return 插入或更新的行数
     */
    public long saveOrUpdateConfigApp(String buttonId, String appName, String packageName, String appIcon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BUTTON_ID, buttonId);
        values.put(COLUMN_APP_NAME, appName);
        values.put(COLUMN_PACKAGE_NAME, packageName);
        values.put(COLUMN_APP_ICON, appIcon);
        
        // 先尝试更新
        int rowsAffected = db.update(TABLE_CONFIG_APPS, values, COLUMN_BUTTON_ID + "=?", new String[]{buttonId});
        
        // 如果没有更新任何行，则插入新记录
        if (rowsAffected == 0) {
            long result = db.insert(TABLE_CONFIG_APPS, null, values);
            db.close();
            return result;
        } else {
            db.close();
            return rowsAffected;
        }
    }
    
    /**
     * 根据按钮ID获取配置的应用信息
     * @param buttonId 按钮ID
     * @return 应用信息Map，如果没有找到则返回null
     */
    public Map<String, String> getConfigAppByButtonId(String buttonId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONFIG_APPS, null, COLUMN_BUTTON_ID + "=?", new String[]{buttonId}, null, null, null);
        
        Map<String, String> appInfo = null;
        if (cursor.moveToFirst()) {
            appInfo = new HashMap<>();
            appInfo.put("button_id", cursor.getString(cursor.getColumnIndex(COLUMN_BUTTON_ID)));
            appInfo.put("app_name", cursor.getString(cursor.getColumnIndex(COLUMN_APP_NAME)));
            appInfo.put("package_name", cursor.getString(cursor.getColumnIndex(COLUMN_PACKAGE_NAME)));
            appInfo.put("app_icon", cursor.getString(cursor.getColumnIndex(COLUMN_APP_ICON)));
        }
        
        cursor.close();
        db.close();
        return appInfo;
    }
    
    /**
     * 删除配置的应用信息
     * @param buttonId 按钮ID
     * @return 删除的行数
     */
    public int deleteConfigApp(String buttonId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_CONFIG_APPS, COLUMN_BUTTON_ID + "=?", new String[]{buttonId});
        db.close();
        return rowsDeleted;
    }
    
    /**
     * 获取所有配置的应用信息
     * @return 应用信息列表
     */
    public Map<String, Map<String, String>> getAllConfigApps() {
        Map<String, Map<String, String>> allApps = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONFIG_APPS, null, null, null, null, null, null);
        
        while (cursor.moveToNext()) {
            String buttonId = cursor.getString(cursor.getColumnIndex(COLUMN_BUTTON_ID));
            Map<String, String> appInfo = new HashMap<>();
            appInfo.put("button_id", buttonId);
            appInfo.put("app_name", cursor.getString(cursor.getColumnIndex(COLUMN_APP_NAME)));
            appInfo.put("package_name", cursor.getString(cursor.getColumnIndex(COLUMN_PACKAGE_NAME)));
            appInfo.put("app_icon", cursor.getString(cursor.getColumnIndex(COLUMN_APP_ICON)));
            allApps.put(buttonId, appInfo);
        }
        
        cursor.close();
        db.close();
        return allApps;
    }
}