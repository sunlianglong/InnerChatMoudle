package com.example.administrator.canchatdemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.canchatdemo.R;

public class MainWelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_welcome);
        new Thread(){
            public void run(){
                try {
                    Thread.sleep(2000);//二秒后进入主界面
                    finish();
                    startActivity(new Intent(MainWelcomeActivity.this,MainActivity.class));
                    overridePendingTransition(R.anim.stay_in,R.anim.slide_out_right);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
