package com.example.freplace;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final String KEY_REPLACE = "replace_text";
    private static final Uri CONTENT_URI = Uri.parse("content://com.example.freplace.provider/config");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText edit = findViewById(R.id.edit_replace);
        
        // 通过 ContentProvider 读取配置
        String replaceText = loadConfigFromProvider();
        edit.setText(replaceText);
        
        Button btn = findViewById(R.id.btn_save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 通过 ContentProvider 保存配置
                saveConfigToProvider(edit.getText().toString());
                // 提示用户重启目标应用
                Toast.makeText(MainActivity.this, "设置已保存，请重启目标应用使配置生效", Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private String loadConfigFromProvider() {
        try {
            ContentResolver resolver = getContentResolver();
            Cursor cursor = resolver.query(CONTENT_URI, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int valueIndex = cursor.getColumnIndex("value");
                if (valueIndex != -1) {
                    String value = cursor.getString(valueIndex);
                    cursor.close();
                    return value;
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "FFF"; // 默认值
    }
    
    private void saveConfigToProvider(String value) {
        ConfigProvider.updateConfig(this, KEY_REPLACE, value);
    }
}
