package com.example.administrator.canchatdemo.util;

import java.io.*;
import java.net.*;
/**
 * Created by Administrator on 2016/4/12.
 * 暂时没用
 */
public class CanChatTcpServer extends Thread {
    //服务器主程序 监听端口号10001
    public void run(){
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(10001);
            while (true) {
                Socket s = ss.accept();
                new Thread(new ChatTcpServer(s)).start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (ss != null)
                    ss.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    //服务器连接子对象
    class ChatTcpServer implements Runnable{
        private Socket s;
        public ChatTcpServer(Socket s){
            this.s = s;
        }
        public void run(){
            try{
                String ip = s.getInetAddress().getHostAddress();
                System.out.println("connected+++"+ip);
                InputStream in = s.getInputStream();
                OutputStream out = s.getOutputStream();
                byte[] buf = new byte[1024];
                int len = 0;
                while((len = in.read(buf)) != -1){
                    String data = new String(buf,0,len);
                    //System.out.println(data);
                    out.write(("got the messsga: " + data).getBytes());
                }
            }catch(Exception e){
                throw new RuntimeException(e);
            }finally{
                try{
                    if(s!=null)
                        s.close();
                }catch(Exception e){

                }
            }
        }
    }
}


