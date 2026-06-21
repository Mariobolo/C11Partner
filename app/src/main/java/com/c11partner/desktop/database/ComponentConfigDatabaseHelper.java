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
 * 组件配置数据库帮助类
 * 用于管理组件配置的SQLite数据库
 */
public class ComponentConfigDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "ComponentConfigDatabaseHelper";
    private static final String DATABASE_NAME = "component_config.db";
    private static final int DATABASE_VERSION = 1;
    
    // 表名
    public static final String TABLE_COMPONENT_CONFIG = "component_config";
    
    // 列名
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_COMPONENT_NAME = "component_name";
    public static final String COLUMN_IS_ENABLED = "is_enabled";
    
    // 创建表的SQL语句
    private static final String CREATE_TABLE_COMPONENT_CONFIG = 
            "CREATE TABLE " + TABLE_COMPONENT_CONFIG + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_COMPONENT_NAME + " TEXT NOT NULL UNIQUE, " +
            COLUMN_IS_ENABLED + " INTEGER NOT NULL DEFAULT 0);";
    
    private static ComponentConfigDatabaseHelper instance;
    
    /**
     * 私有构造函数
     * @param context 应用上下文
     */
    private ComponentConfigDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    /**
     * 获取单例实例
     * @param context 应用上下文
     * @return ComponentConfigDatabaseHelper实例
     */
    public static synchronized ComponentConfigDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ComponentConfigDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表
        db.execSQL(CREATE_TABLE_COMPONENT_CONFIG);
        Log.d(TAG, "组件配置数据库表创建成功");
        
        // 初始化默认配置（所有组件默认启用）
        initDefaultConfig(db);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 升级数据库时删除旧表并重新创建
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPONENT_CONFIG);
        onCreate(db);
    }
    
    /**
     * 初始化默认配置
     * @param db 数据库实例
     */
    private void initDefaultConfig(SQLiteDatabase db) {
        String[] componentNames = {
            "music_component",
            "map_component", 
            "app_component",
            "tire_pressure_component",
            "weather_component"
        };
        
        for (String componentName : componentNames) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_COMPONENT_NAME, componentName);
            values.put(COLUMN_IS_ENABLED, 0); // 默认不启用
            db.insertWithOnConflict(TABLE_COMPONENT_CONFIG, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }
        
        Log.d(TAG, "默认组件配置初始化完成");
    }
    
    /**
     * 保存或更新组件配置
     * @param componentName 组件名称
     * @param isEnabled 是否启用
     * @return 插入或更新的行ID，失败返回-1
     */
    public long saveOrUpdateComponentConfig(String componentName, boolean isEnabled) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPONENT_NAME, componentName);
        values.put(COLUMN_IS_ENABLED, isEnabled ? 1 : 0);
        
        // 使用replace方法实现插入或更新
        long result = db.replace(TABLE_COMPONENT_CONFIG, null, values);
        db.close();
        return result;
    }
    
    /**
     * 获取组件配置
     * @param componentName 组件名称
     * @return 是否启用，未找到则返回false（默认不启用）
     */
    public boolean isComponentEnabled(String componentName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COMPONENT_CONFIG, 
                new String[]{COLUMN_IS_ENABLED}, 
                COLUMN_COMPONENT_NAME + "=?", 
                new String[]{componentName}, 
                null, null, null);
        
        boolean isEnabled = false; // 默认不启用
        if (cursor.moveToFirst()) {
            isEnabled = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_ENABLED)) ==1;
        }
        
        cursor.close();
        db.close();
        return isEnabled;
    }
    
    /**
     * 获取所有组件配置
     * @return 组件配置映射
     */
    public Map<String, Boolean> getAllComponentConfigs() {
        Map<String, Boolean> configs = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COMPONENT_CONFIG, 
                new String[]{COLUMN_COMPONENT_NAME, COLUMN_IS_ENABLED}, 
                null, null, null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                String componentName = cursor.getString(cursor.getColumnIndex(COLUMN_COMPONENT_NAME));
                boolean isEnabled = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_ENABLED)) == 1;
                configs.put(componentName, isEnabled);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return configs;
    }
}