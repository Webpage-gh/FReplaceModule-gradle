package com.example.freplace;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ConfigProvider extends ContentProvider {
    private static final String AUTHORITY = "com.example.freplace.provider";
    private static final String PATH_CONFIG = "config";
    private static final int CODE_CONFIG = 1;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    
    static {
        uriMatcher.addURI(AUTHORITY, PATH_CONFIG, CODE_CONFIG);
    }
    
    @Override
    public boolean onCreate() {
        return true;
    }
    
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, 
                       String[] selectionArgs, String sortOrder) {
        if (uriMatcher.match(uri) == CODE_CONFIG) {
            // 返回配置数据
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            String replaceText = prefs.getString("replace_text", "FFF");
            
            MatrixCursor cursor = new MatrixCursor(new String[]{"key", "value"});
            cursor.addRow(new Object[]{"replace_text", replaceText});
            return cursor;
        }
        throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    
    @Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + PATH_CONFIG;
    }
    
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Not supported");
    }
    
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not supported");
    }
    
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not supported");
    }
    
    // 公开方法供 MainActivity 调用，更新配置
    public static void updateConfig(android.content.Context context, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(key, value).apply();
    }
}
