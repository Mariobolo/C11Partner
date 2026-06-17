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

// 添加pinyin4j库的导入
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 应用数据库帮助类
 * 用于管理应用列表的SQLite数据库
 */
public class AppDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "AppDatabaseHelper";
    private static final String DATABASE_NAME = "apps.db";
    private static final int DATABASE_VERSION = 1;
    
    // 表名
    public static final String TABLE_APPS = "apps";
    
    // 列名
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PACKAGE_NAME = "package_name";
    public static final String COLUMN_LETTER = "letter"; // 首字母，用于A-Z分类
    
    // 创建表的SQL语句
    private static final String CREATE_TABLE_APPS = 
            "CREATE TABLE " + TABLE_APPS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_PACKAGE_NAME + " TEXT NOT NULL UNIQUE, " +
            COLUMN_LETTER + " TEXT NOT NULL);";
    
    private static AppDatabaseHelper instance;
    
    /**
     * 私有构造函数
     * @param context 应用上下文
     */
    private AppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    /**
     * 获取单例实例
     * @param context 应用上下文
     * @return AppDatabaseHelper实例
     */
    public static synchronized AppDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new AppDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表
        db.execSQL(CREATE_TABLE_APPS);
        Log.d(TAG, "应用数据库表创建成功");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 升级数据库时删除旧表并重新创建
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPS);
        onCreate(db);
    }
    
    /**
     * 插入或更新应用信息
     * @param name 应用名称
     * @param packageName 应用包名
     * @param letter 首字母
     * @return 插入或更新的行ID，失败返回-1
     */
    public long insertOrUpdateApp(String name, String packageName, String letter) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PACKAGE_NAME, packageName);
        values.put(COLUMN_LETTER, letter);
        
        // 使用replace方法实现插入或更新
        long result = db.replace(TABLE_APPS, null, values);
        db.close();
        return result;
    }
    
    /**
     * 批量插入或更新应用信息
     * @param apps 应用列表
     */
    public void bulkInsertOrUpdateApps(List<Map<String, Object>> apps) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Map<String, Object> app : apps) {
                String name = (String) app.get("name");
                String packageName = (String) app.get("packageName");
                String letter = getFirstLetter(name);
                
                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME, name);
                values.put(COLUMN_PACKAGE_NAME, packageName);
                values.put(COLUMN_LETTER, letter);
                
                db.replace(TABLE_APPS, null, values);
            }
            db.setTransactionSuccessful();
            Log.d(TAG, "批量插入或更新应用信息成功，共处理 " + apps.size() + " 个应用");
        } catch (Exception e) {
            Log.e(TAG, "批量插入或更新应用信息失败", e);
        } finally {
            db.endTransaction();
            db.close();
        }
    }
    
    /**
     * 获取所有应用信息
     * @return 应用列表
     */
    public List<Map<String, Object>> getAllApps() {
        List<Map<String, Object>> apps = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_APPS, null, null, null, null, null, COLUMN_LETTER + " ASC");
        
        if (cursor.moveToFirst()) {
            do {
                Map<String, Object> app = new HashMap<>();
                app.put("name", cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                app.put("packageName", cursor.getString(cursor.getColumnIndex(COLUMN_PACKAGE_NAME)));
                // 注意：这里没有图标信息，因为图标无法直接存储在数据库中
                apps.add(app);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return apps;
    }
    
    /**
     * 根据首字母获取应用信息
     * @param letter 首字母
     * @return 应用列表
     */
    public List<Map<String, Object>> getAppsByLetter(String letter) {
        List<Map<String, Object>> apps = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_APPS, null, COLUMN_LETTER + "=?", 
                new String[]{letter}, null, null, COLUMN_NAME + " ASC");
        
        if (cursor.moveToFirst()) {
            do {
                Map<String, Object> app = new HashMap<>();
                app.put("name", cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                app.put("packageName", cursor.getString(cursor.getColumnIndex(COLUMN_PACKAGE_NAME)));
                apps.add(app);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return apps;
    }
    
    /**
     * 获取所有首字母列表
     * @return 首字母列表
     */
    public List<String> getAllLetters() {
        List<String> letters = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_APPS, new String[]{COLUMN_LETTER}, 
                null, null, COLUMN_LETTER, null, COLUMN_LETTER + " ASC", null);
        
        if (cursor.moveToFirst()) {
            do {
                letters.add(cursor.getString(cursor.getColumnIndex(COLUMN_LETTER)));
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return letters;
    }
    
    /**
     * 清空应用表
     */
    public void clearApps() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_APPS, null, null);
        db.close();
        Log.d(TAG, "应用表已清空");
    }
    
    /**
     * 获取字符串的首字母（简化的实现）
     * @param str 字符串
     * @return 首字母
     */
    private String getFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return "#";
        }
        
        char firstChar = str.charAt(0);
        // 判断是否为英文字母
        if ((firstChar >= 'A' && firstChar <= 'Z') || (firstChar >= 'a' && firstChar <= 'z')) {
            return String.valueOf(Character.toUpperCase(firstChar));
        }
        // 判断是否为中文字符
        else if (isChineseChar(firstChar)) {
            // 获取中文字符的拼音首字母
            return getChineseFirstLetter(firstChar);
        }
        // 其他字符归类到#号
        else {
            return "#";
        }
    }
    
    /**
     * 判断字符是否为中文字符
     * @param c 字符
     * @return 是否为中文字符
     */
    private boolean isChineseChar(char c) {
        return (c >= 0x4E00 && c <= 0x9FFF) || 
               (c >= 0x3400 && c <= 0x4DBF) || 
               (c >= 0x20000 && c <= 0x2A6DF) || 
               (c >= 0x2A700 && c <= 0x2B73F) || 
               (c >= 0x2B740 && c <= 0x2B81F) || 
               (c >= 0x2B820 && c <= 0x2CEAF);
    }
    
    /**
     * 获取中文字符的拼音首字母
     * @param ch 中文字符
     * @return 拼音首字母
     */
    private String getChineseFirstLetter(char ch) {
        // 创建拼音输出格式
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);  // 大写
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  // 不带声调
        format.setVCharType(HanyuPinyinVCharType.WITH_V);  // 使用v表示ü
        
        try {
            // 获取拼音数组
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(ch, format);
            
            // 如果拼音数组不为空且长度大于0，返回第一个拼音的首字母
            if (pinyinArray != null && pinyinArray.length > 0) {
                String pinyin = pinyinArray[0];
                if (pinyin != null && pinyin.length() > 0) {
                    return String.valueOf(pinyin.charAt(0));
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            Log.e("AppDatabaseHelper", "拼音转换出错", e);
        }
        
        // 如果没有找到匹配的拼音首字母，则归类到#号
        return "#";
    }
}