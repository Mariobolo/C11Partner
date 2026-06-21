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
 * 快速启动应用数据库帮助类
 * 用于管理快速启动应用列表的SQLite数据库
 */
public class QuickAppDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "QuickAppDatabaseHelper";
    private static final String DATABASE_NAME = "quick_apps.db";
    private static final int DATABASE_VERSION = 1;
    
    // 表名
    public static final String TABLE_QUICK_APPS = "quick_apps";
    
    // 列名
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PACKAGE_NAME = "package_name";
    public static final String COLUMN_ICON = "icon"; // 图标Base64编码
    
    // 创建表的SQL语句
    private static final String CREATE_TABLE_QUICK_APPS = 
            "CREATE TABLE " + TABLE_QUICK_APPS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_PACKAGE_NAME + " TEXT NOT NULL UNIQUE, " +
            COLUMN_ICON + " TEXT);";
    
    private static QuickAppDatabaseHelper instance;
    
    /**
     * 私有构造函数
     * @param context 应用上下文
     */
    private QuickAppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    /**
     * 获取单例实例
     * @param context 应用上下文
     * @return QuickAppDatabaseHelper实例
     */
    public static synchronized QuickAppDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new QuickAppDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表
        db.execSQL(CREATE_TABLE_QUICK_APPS);
        Log.d(TAG, "快速启动应用数据库表创建成功");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 升级数据库时删除旧表并重新创建
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUICK_APPS);
        onCreate(db);
    }
    
    /**
     * 插入快速启动应用
     * @param name 应用名称
     * @param packageName 应用包名
     * @param iconBase64 图标Base64编码
     * @return 插入的行ID，失败返回-1
     */
    public long insertQuickApp(String name, String packageName, String iconBase64) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PACKAGE_NAME, packageName);
        values.put(COLUMN_ICON, iconBase64);
        
        long result = db.insert(TABLE_QUICK_APPS, null, values);
        db.close();
        return result;
    }
    
    /**
     * 删除快速启动应用
     * @param packageName 应用包名
     * @return 删除的行数
     */
    public int deleteQuickApp(String packageName) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_QUICK_APPS, COLUMN_PACKAGE_NAME + "=?", new String[]{packageName});
        db.close();
        return result;
    }
    
    /**
     * 获取所有快速启动应用
     * @return 快速启动应用列表
     */
    public List<Map<String, Object>> getAllQuickApps() {
        List<Map<String, Object>> quickApps = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_QUICK_APPS, null, null, null, null, null, COLUMN_ID + " ASC");
        
        if (cursor.moveToFirst()) {
            do {
                Map<String, Object> app = new HashMap<>();
                app.put("name", cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                app.put("packageName", cursor.getString(cursor.getColumnIndex(COLUMN_PACKAGE_NAME)));
                app.put("icon", cursor.getString(cursor.getColumnIndex(COLUMN_ICON)));
                quickApps.add(app);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return quickApps;
    }
    
    /**
     * 检查应用是否已添加到快速启动
     * @param packageName 应用包名
     * @return 是否已添加
     */
    public boolean isQuickApp(String packageName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_QUICK_APPS, new String[]{COLUMN_ID}, 
                COLUMN_PACKAGE_NAME + "=?", new String[]{packageName}, null, null, null);
        
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }
}