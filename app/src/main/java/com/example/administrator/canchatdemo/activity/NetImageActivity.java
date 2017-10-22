package com.example.administrator.canchatdemo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.canchatdemo.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetImageActivity extends AppCompatActivity {

    private EditText netImageUrlText;
    private ImageView netImageView;
    private Button getNetImageBtn;

    private final int HANDLER_CHANGE_UI = 1;
    private final int HANDLER_FAILURE =3;


    //处理图片
    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == HANDLER_CHANGE_UI){
                netImageView.setImageBitmap((Bitmap)msg.obj);
                Toast.makeText(NetImageActivity.this,"获取图片成功",Toast.LENGTH_SHORT).show();
            }else if(msg.what == HANDLER_FAILURE){
                //Toast也属于UI部分要放在主线程操作
                Toast.makeText(NetImageActivity.this,"获取图片失败",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_image);

        netImageUrlText = (EditText)findViewById(R.id.netImageUrlText);
        netImageView = (ImageView)findViewById(R.id.netImageView);
        getNetImageBtn = (Button)findViewById(R.id.getNetImageBtn);

        getNetImageBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    public void run(){
                        String urlPath = netImageUrlText.getText().toString().trim();
                        if(TextUtils.isEmpty(urlPath)){
                            Toast.makeText(NetImageActivity.this,"地址不能为空",Toast.LENGTH_SHORT).show();
                        }else{
                            try {
                                URL url = new URL(urlPath);
                                //根据url发送Http请求
                                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                                //设置请求方式
                                conn.setRequestMethod("GET");
                                conn.setConnectTimeout(5000);
                                //conn.setReadTimeout(5000);
                                conn.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; Shuame)");

                                //得到服务器返回码
                                int code = conn.getResponseCode();
                                if(code == 200){
                                    InputStream is = conn.getInputStream();
                                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                                    //传递bitmap给主线程handler
                                    Message msg = new Message();
                                    msg.what = HANDLER_CHANGE_UI;
                                    msg.obj = bitmap;
                                    myHandler.sendMessage(msg);
                                }else{
                                    //接受失败提示
                                    Message msg = new Message();
                                    msg.what = HANDLER_FAILURE;
                                    myHandler.sendMessage(msg);

                                }

                            } catch (Exception e) {
                                Message msg = new Message();
                                msg.what = HANDLER_FAILURE;
                                myHandler.sendMessage(msg);
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
        });
    }
}
