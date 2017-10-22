package com.example.administrator.canchatdemo.util;

import android.content.Context;
import android.widget.Toast;

import com.example.administrator.canchatdemo.activity.MainChatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Administrator on 2016/4/13.
 * 暂时没用
 */
public class CanChatTcpClient {
    /*private Context context;
    private Socket s;
    public TcpChatSimple(Context context){
        this.context = context;
    }
    public void run(){
        OutputStream bufw=null;
        InputStream in =null;
        BufferedReader bufr = null;
        try{
            s = new Socket("192.168.1.28",10001);
            String line =null;
            int count =0 ;
            while(true){
                Thread.sleep(2000);
                line = "this is a test"+ count++;
                if(line.equals("over"))
                    break;
                bufw = s.getOutputStream();
                in = s.getInputStream();

                bufw.write(line.getBytes());
                byte[] by = new byte[1024];
                int len =  in.read(by);
                String data = new String(by,0,len);
                //text.setText("adsfsdf");
                //System.out.println(data);
            }

            s.shutdownOutput();

        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(MainChatActivity.this, "连接失败！", Toast.LENGTH_SHORT);
        }finally{
            try{
                if(s!=null)
                    s.close();
                if(bufr!=null)
                    bufr.close();
            }catch(Exception i){
                Toast.makeText(MainChatActivity.this,"连接失败！",Toast.LENGTH_SHORT);
                //throw new RuntimeException(i);
            }
        }
    }*/
}
