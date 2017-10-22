package com.example.administrator.canchatdemo.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.administrator.canchatdemo.R;
import com.example.administrator.canchatdemo.fragment.ListFragment;
import com.example.administrator.canchatdemo.fragment.InfoFragment;
import com.example.administrator.canchatdemo.fragment.FunFragment;
import com.example.administrator.canchatdemo.util.CanChatUdpReceiver;
import com.example.administrator.canchatdemo.util.CanChatUdpSend;
import com.example.administrator.canchatdemo.util.MessageDao;
import com.example.administrator.canchatdemo.util.Udp;

import org.w3c.dom.Text;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.concurrent.Delayed;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity implements OnClickListener{


    public static String localHostIp = null;
    public static String ipToAll = null;

    public final static int GET_IP_FAILURE = 1;



    //主界面菜单
    private ImageView listImage;
    private ImageView funImage;
    private ImageView infoImage;

    //fragement
    private Fragment myFragment;
    private ListFragment listFragment =new ListFragment();
    private InfoFragment infoFragment =new InfoFragment();
    private FunFragment funFragment =new FunFragment();

    //接受信息服务器
    private CanChatUdpReceiver udpAllReceiver;


    //获取IP失败提示
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == GET_IP_FAILURE){
                Toast.makeText(MainActivity.this, "获取Ip失败，请检查网络", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(){
            public void run(){
                boolean flag = true;
                while(localHostIp == null || ipToAll==null) {
                    try {
                        //Log.e("提示：", "获取ip开始");
                        localHostIp = Udp.getIp();
                        ipToAll = Udp.getIpToAll();
                        if (localHostIp == null || ipToAll==null) {
                            if(flag) {
                                Message msg = new Message();
                                msg.what = GET_IP_FAILURE;
                                handler.sendMessage(msg);
                                flag = false;
                            }
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        Log.e(e.getMessage(), "获取ip失败");

                    }
                }
            }
        }.start();


        //主菜单界面 菜单切换
        listImage = (ImageView)findViewById(R.id.listImage);
        funImage = (ImageView)findViewById(R.id.funImage);
        infoImage = (ImageView)findViewById(R.id.infoImage);
        LinearLayout listMenu = (LinearLayout)findViewById(R.id.listMenu);
        LinearLayout funMenu = (LinearLayout)findViewById(R.id.funMenu);
        LinearLayout infoMenu = (LinearLayout)findViewById(R.id.infoMenu);
        listMenu.setOnClickListener(this);
        funMenu.setOnClickListener(this);
        infoMenu.setOnClickListener(this);

        //设置默认Fragment
        setFragment(listFragment);

        //启动接受服务器//当获取到IP时启动
        new Thread(){
            public void run(){
                boolean flag = true;
                while(flag){
                    if(localHostIp != null && ipToAll!=null) {
                        if (udpAllReceiver == null) {
                            //启动群接受服务
                            Log.e(""+ipToAll,""+localHostIp);
                            udpAllReceiver = new CanChatUdpReceiver(MainActivity.this, Udp.PORT_ALL);
                            udpAllReceiver.start();
                            flag = false;
                        }
                    }
                }
            }
        }.start();

        //启动线程发送测试在线人数
        new Thread(new checkedOnline()).start();
    }

    //启动线程发送测试在线人数
    class checkedOnline implements Runnable{
        public void run(){
            try {
                while (true) {
                    String userName = getUserName();
                    int imageId = getUserImageId();
                    String data = getData(userName, imageId);//分装数据处理
                    new Thread(new CanChatUdpSend(data, ipToAll, Udp.PORT_ALL)).start();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //封装数据
    private String getData(String userName,int imageId){
        String data =Udp.CHECKED_CODE + userName+"##"+imageId;
        return data;
    }

    //获取用户昵称
    public String getUserName(){
        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userName = sp.getString("userName","");
        return userName;
    }

    //获取用户头像
    public int getUserImageId(){
        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        int imageId = sp.getInt("userImageId",0);
        return imageId;
    }


    //设置Fragment
    private void setFragment(Fragment fragment){
        //判断是否为空，则进行初始化
        if(myFragment == null){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(R.id.mainFragment,fragment);
            myFragment = fragment;//传递参数值，显示的fragment
            transaction.commit();
        }else{
            if(myFragment != fragment){
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                //先影藏，然后判断是否添加过，没有则添加，有则显示
                if(fragment.isAdded()){
                    transaction.hide(myFragment).show(fragment);
                }else{
                    transaction.hide(myFragment).add(R.id.mainFragment,fragment);
                }
                myFragment = fragment;//传递参数值，显示的fragment
                transaction.commit();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.listMenu:
                //设置菜单颜色图片
                setSelectedMenu(listImage);
                //设置Fragment
                setFragment(listFragment);

                break;
            case R.id.funMenu:
                //设置菜单颜色图片
                setSelectedMenu(funImage);
                //设置Fragment
                setFragment(funFragment);
                break;
            case R.id.infoMenu:
                //设置菜单颜色图片
                setSelectedMenu(infoImage);
                //设置Fragment
                setFragment(infoFragment);

                break;
            default:
        }
    }

    //设置菜单选中状态
    private void setSelectedMenu(ImageView selected){
        if(selected == listImage)
            listImage.setImageResource(R.drawable.skin_tab_icon_conversation_selected);
        else
            listImage.setImageResource(R.drawable.skin_tab_icon_conversation_normal);

        if(selected == funImage)
            funImage.setImageResource(R.drawable.skin_tab_icon_plugin_selected);
        else
            funImage.setImageResource(R.drawable.skin_tab_icon_plugin_normal);

        if(selected == infoImage)
            infoImage.setImageResource(R.drawable.skin_tab_icon_contact_selected);
        else
            infoImage.setImageResource(R.drawable.skin_tab_icon_contact_normal);
    }
}


