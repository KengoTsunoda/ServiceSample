package com.example.kengo.servicesample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SoundStartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_start);

        Intent intent = getIntent();
        // 通知タップからの引継ぎデータを取得
        boolean fromNotification = intent.getBooleanExtra("fromNotification", false);
        // 引継ぎデータが存在、つまり通知のタップからならば...
        if(fromNotification){
            // 再生ボタンをタップ不可に、停止ボタンをタップ可に変更
            Button btPlay = findViewById(R.id.btPlay);
            Button btStop = findViewById(R.id.btStop);
            btPlay.setEnabled(false);
            btStop.setEnabled(true);
        }
    }

    public void onPlayButtonClick(View view){
        Intent intent = new Intent(SoundStartActivity.this, SoundManagerService.class);
        // サービスを起動
        startService(intent);
        // 再生ボタンをタップ不可に、停止ボタンをタップ可に変更
        Button btPlay = findViewById(R.id.btPlay);
        Button btStop = findViewById(R.id.btStop);
        btPlay.setEnabled(false);
        btStop.setEnabled(true);
    }

    public void onStopButtonClick(View view){
        Intent intent = new Intent(SoundStartActivity.this, SoundManagerService.class);
        stopService(intent);
        Button btPlay = findViewById(R.id.btPlay);
        Button btStop = findViewById(R.id.btStop);
        btPlay.setEnabled(true);
        btStop.setEnabled(false);
    }
}
