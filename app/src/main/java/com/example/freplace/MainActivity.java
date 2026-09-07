package com.example.freplace;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
    private static final String KEY_REPLACE = "replace_text";
    private static final String PREFS_NAME = "freplace_prefs";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText edit = findViewById(R.id.edit_replace);
        
        // 使用默认模式（MODE_PRIVATE），XSharedPreferences 会自动处理跨应用读取
        final SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        edit.setText(prefs.getString(KEY_REPLACE, "FFF"));
        
        Button btn = findViewById(R.id.btn_save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.edit().putString(KEY_REPLACE, edit.getText().toString()).apply();
                // 保存后不退出，让用户可以继续修改
            }
        });
    }
}
