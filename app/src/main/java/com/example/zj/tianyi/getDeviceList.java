package com.example.zj.tianyi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zj on 16-10-15.
 */
public class getDeviceList {
    public String author;
    public String id;
    public Myhandle Myhandle;
    //private List<Map<String,String>> list;
    private returnDevices r;

    public getDeviceList(String id,String author){
        this.id=id;
        this.author=author;
    }
    public void get(){
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("获取设备列表","++++++"+id+author);
                //deviceList=new ArrayList<>();
                BufferedReader in=null;
                String result="";
                URL url;
                try {
                    url = new URL("https://wifi.loocha.cn/"+id+"/wifi/status");
                    HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Authorization", author);
                    Log.e("status",conn.getResponseCode()+"  "+id);
                    switch(conn.getResponseCode()) {
                        case 200:
                            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            String line;
                            while ((line = in.readLine()) != null) {
                                result += line;
                            }
                            //Log.e("result",result);
                            JSONObject json = new JSONObject(result);
                            JSONObject wifiOnlines = json.getJSONObject("wifiOnlines");
                            JSONArray array = wifiOnlines.getJSONArray("onlines");
                            int num=array.length();
                            //Log.e("result",num+"");
                            String[] brasIp=new String[num];
                            String[] ip=new String[num];
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject temp = (JSONObject) array.get(i);
                                brasIp[i] = temp.getString("brasIp");
                                //ip[i] = temp.getString("ip");
                            }
                            Message msg=new Message();
                            Bundle b=new Bundle();
                            b.putInt("num",num);
                            //b.putStringArray("brasIp",brasIp);
                            msg.setData(b);
                            Myhandle.sendMessage(msg);
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    Log.e("error",e.getMessage());
                    e.printStackTrace();
                } finally {
                    if(in!=null)
                        try {
                            in.close();
                        } catch (IOException e) {
                            Log.e("error",e.getMessage());
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
            //String[] brasIp=b.getStringArray("brasIp");
            String[] brasIp=new String[]{"++"};
            int n=b.getInt("num");
            Log.e("设备",n+"");
            r.returninfo(brasIp,n);
        }
    }

    public interface returnDevices{
        void returninfo(String[] brasip,int n);
    }

    public void setreturn(returnDevices r){
        this.r=r;
    }
}
