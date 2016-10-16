package com.example.zj.tianyi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by zj on 16-10-15.
 */
public class Ainlaunch {
    private  String wanip;
    private String basip;
    private String id;
    private String author;
    private returnDevices r;
    public Myhandle Myhandle;

    public Ainlaunch(String wanip,String basip,String id,String author){
        this.basip=basip;
        this.id=id;
        this.wanip=wanip;
        this.author=author;
    }

    public int stop(){
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                URL url= null;
                BufferedReader in = null;
                String result = "";
                try {
                    url = new URL("https://wifi.loocha.cn/"+id+"/wifi/kickoff?wanip="+wanip+"&brasip="+basip);
                    HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Authorization", author);
                    conn.setRequestMethod("DELETE");
                    int code=conn.getResponseCode();
                    Log.e("下线code",code+"");
                    if(code==200){
                        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String line;
                        while ((line = in.readLine()) != null) {
                            result += line;
                        }
                        JSONObject json = new JSONObject(result);
                        //int status = json.getInt("status");
                        //Log.e("status",status+"");
                        Log.e("result",result);
                        Log.e("下线成功",code+"");
                        Message msg=new Message();
                        Bundle b=new Bundle();
                        b.putInt("num",1);
                        //b.putStringArray("brasIp",brasIp);
                        msg.setData(b);
                        Myhandle.sendMessage(msg);
                    }
                } catch (Exception e) {
                    Log.e("error ",e.getMessage());
                } finally {
//                    try {
//                        in.close();
//                    } catch (IOException e) {
//                        Log.e("I/Oerror ",e.getMessage());
//                    }
                }

            }
        });
        Myhandle=new Myhandle();
        t.start();
        return 0;
    }

    public class Myhandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle b=msg.getData();
            //String[] brasIp=b.getStringArray("brasIp");
            //String[] brasIp=new String[]{"++"};
            int n=b.getInt("num");
            Log.e("下线",n+"");
            r.returninfo(n);
        }
    }

    public interface returnDevices{
        void returninfo(int n);
    }

    public void setreturn(returnDevices r){
        this.r=r;
    }
}
