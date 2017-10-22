package com.example.administrator.canchatdemo.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.canchatdemo.R;

public class SetNameActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_username);

        username = (EditText)findViewById(R.id.username);

        Button saveBtn = (Button)findViewById(R.id.saveBtn);
        Button backBtn = (Button)findViewById(R.id.backBtn);
        saveBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String name = sp.getString("userName","");
        username.setText(name);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveBtn:
                //储存昵称
                String name = username.getText().toString();
                if(name.equals("")) {
                    Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("userName", name);
                editor.apply();

                Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();

                break;
            case R.id.backBtn:
                finish();
                overridePendingTransition(R.anim.stay_in,R.anim.slide_out_down);
                break;
            default:
        }
    }
}
