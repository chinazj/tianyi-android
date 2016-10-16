package com.example.zj.tianyi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by zj on 16-10-15.
 */
public class brasip {
    private String bascip;
    private Myhandle Myhandle;
    private returnbascip r;
    //public brasip(){}

    public void getip(){

        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {

                URL url = null;
                HttpURLConnection conn = null;
                try {

                    url = new URL("http://test.f-young.cn/");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setInstanceFollowRedirects(false);
                    Log.e("code",conn.getResponseCode()+"");
                    if(conn.getResponseCode()==302) {
                        String bascip=conn.getHeaderField("Location").split("&")[1].split("=")[1];
                        String wanip=conn.getHeaderField("Location").split("&")[0].split("=")[1];

                        Log.e("location", bascip);
                        Log.e("wan",wanip);

                        Message msg=new Message();
                        Bundle b= new Bundle();
                        b.putString("bascip",bascip);
                        b.putString("wanip",wanip);
                        msg.setData(b);
                        Myhandle.sendMessage(msg);
                    }
                } catch (MalformedURLException e) {
                    Log.e("error", "recursiveTracePath MalformedURLException");
                }  catch (Exception e) {
                    Log.e("error", e.getMessage());
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        });
        Myhandle=new Myhandle();
        t.start();
    }

    public class Myhandle extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle b=msg.getData();
            String bascip=b.getString("bascip");
            String wanip=b.getString("wanip");
            r.returnString(bascip,wanip);
        }
    }

    public interface returnbascip{
        void returnString(String bascip,String wanip);
    }
    public void setreturn(returnbascip r){
        this.r=r;
    }
}
