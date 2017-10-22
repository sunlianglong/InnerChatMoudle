package com.example.administrator.canchatdemo.util;

import android.util.Log;
import android.widget.Toast;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by Administrator on 2016/4/14.
 * 定义常量
 */
public class Udp {
    //定义单独端口
    public static final int PORT_OWN = 52000;
    //定义群聊端口
    public static final int PORT_ALL = 51000;



    public static final String CHECKED_CODE = "check_code_123456789";


    //获取255ip
    public static String getIpToAll(){
        try {
            String ip = getIp();
            if(ip == null)
                return  null;
            return getIp().substring(0, 10) + "255";
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //获取本地ip
    public static String getIp(){
        try{
            for(Enumeration<NetworkInterface> en= NetworkInterface.getNetworkInterfaces();en.hasMoreElements();){
                NetworkInterface intf = en.nextElement();
                for(Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();enumIpAddr.hasMoreElements();){
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if(!inetAddress.isLoopbackAddress()&&inetAddress instanceof Inet4Address){
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
