package com.example.freplace;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.File;
import de.robv.android.xposed.XSharedPreferences;

public class MainActivity extends Activity {
    private static final String KEY_REPLACE = "replace_text";
    private static final String PREFS_NAME = "freplace_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText edit = findViewById(R.id.edit_replace);
        
        // 使用 XSharedPreferences 读取配置，确保一致性
        XSharedPreferences xsp = new XSharedPreferences("com.example.freplace", PREFS_NAME);
        xsp.makeWorldReadable();
        xsp.reload();
        edit.setText(xsp.getString(KEY_REPLACE, "FFF"));
        
        Button btn = findViewById(R.id.btn_save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用 MODE_PRIVATE 写入配置
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                prefs.edit().putString(KEY_REPLACE, edit.getText().toString()).apply();
                // ★ 关键：写入之后立刻把文件设为 world-readable
                makePrefsWorldReadable();
            }
        });
    }
    
    private void makePrefsWorldReadable() {
        try {
            File prefsDir = new File(getApplicationInfo().dataDir, "shared_prefs");
            prefsDir.setReadable(true, false);   // 目录也要可读，否则进不去
            prefsDir.setExecutable(true, false);
            File prefsFile = new File(prefsDir, PREFS_NAME + ".xml");
            prefsFile.setReadable(true, false);  // 644 权限
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
