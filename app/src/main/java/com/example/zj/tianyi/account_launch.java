package com.example.zj.tianyi;

import android.content.SharedPreferences;
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
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by zj on 16-10-13.
 */
public class account_launch {
    private String id;
    private String did;
    private String ip;
    private String passwd=null;
    private String qrcode=null;
    private String bascip;
    private String author;
    private handle myhandle;
    private returnString r;

    public account_launch(String ip,String author,String bascip){
        this.ip=ip;
        this.author=author;
        this.bascip=bascip;
    }
    Thread t1=new Thread(new Runnable() {
        @Override
        public void run() {
            Log.e("passwd", "获取passwd");
            try {
                URL url = new URL("https://www.loocha.com.cn/" + id + "/wifi?server_did=" + did + "&imsi=460026519326265");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Authorization", author);
                //Log.e("status",connection.getResponseCode()+"");
                switch(connection.getResponseCode()) {
                    case 200:
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String result = "";
                        String line = null;
                        while ((line = in.readLine()) != null) {
                            result += line;
                        }
                        JSONObject json = new JSONObject(result);
                        int status = json.getInt("status");
                        if (status == 0) {
                            String password = json.getJSONObject("telecomWifiRes").getString("password");
                            Message msg=new Message();
                            Bundle b=new Bundle();
                            b.putString("password",password);
                            b.putInt("0",1);
                            msg.setData(b);
                            myhandle.sendMessage(msg);
                            Log.e("passwd:", password);
                        } else {
                            Log.e("passwd", "获取失败！");
                        }
                        break;
                    case 401:
                        Log.e("author+id+did:",author+id+did);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {

            }
        }
    });



    Thread t2=new Thread(new Runnable() {
        @Override
        public void run() {
            Log.e("qrcode", "获取qrcode");
            try {
                URL url = new URL("https://wifi.loocha.cn/0/wifi/qrcode" + "?brasip="+bascip+ "&ulanip=" + ip + "&wlanip=" + ip);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //Log.e("status",connection.getResponseCode()+"");
                switch(connection.getResponseCode()) {
                    case 200:
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String result = "";
                        String line = null;
                        while ((line = in.readLine()) != null) {
                            result += line;
                        }
                        //Log.e("result:", result);
                        JSONObject json = new JSONObject(result);
                        int status = json.getInt("status");
                        if (status == 0) {
                            String code = json.getJSONObject("telecomWifiRes").getString("password");
                            Message msg=new Message();
                            Bundle b=new Bundle();
                            b.putString("qrcode",code);
                            b.putInt("0",0);
                            msg.setData(b);
                            myhandle.sendMessage(msg);
                            Log.e("qrcode:", code);
                        }
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    });

        Thread t3=new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    URL url = new URL("https://www.loocha.com.cn:8443/login");
                    HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Authorization", author);
                    switch(conn.getResponseCode()){
                        case 401:
                            Log.e("t3statue",""+conn.getResponseCode());
                            break;
                        case 200:
                            //int status=conn.getResponseCode();
                            Log.e("t3statue",""+conn.getResponseCode());
                            BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream())) ;
                            String result="";
                            String line=null;
                            while ((line = in.readLine()) != null) {
                                result += line;
                            }
                            JSONObject json= new JSONObject(result);
                            int status=json.getInt("status");
                            if(status==0){
                                String accountid=json.getJSONObject("user").getString("id");
                                String did=json.getJSONObject("user").getString("did");
                                did=did.substring(0,did.indexOf("#"));
                                Message msg=new Message();
                                Bundle b=new Bundle();
                                b.putString("accountid",accountid);
                                b.putString("did",did);
                                b.putInt("0",2);
                                msg.setData(b);
                                myhandle.sendMessage(msg);
                            }else{
                                Log.e("statue","获取失败！");
                            }
                            break;
                        default:
                            Log.e("statue",""+conn.getResponseCode());
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("statue","21");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("statue","2121");
                }
            }
        });

    public void st(){
        Thread t4=new Thread(new Runnable() {
            @Override
            public void run() {
                String param = "qrcode=" + qrcode + "&code=" + passwd;
                BufferedReader in = null;
                String result = "";
                try {
                    URL realUrl = new URL("https://wifi.loocha.cn/" + id + "/wifi/enable?" + param);
                    HttpsURLConnection conn = (HttpsURLConnection) realUrl.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Authorization", author);
                    conn.setRequestProperty("connection", "Keep-Alive");
                    conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    Log.e("status",conn.getResponseCode()+"");
                    switch(conn.getResponseCode()) {
                        case 200:
                            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            String line;
                            while ((line = in.readLine()) != null) {
                                result += line;
                            }
                            JSONObject json = new JSONObject(result);
                            int status = json.getInt("status");
                            Log.e("status",status+"");
                            Log.e("result",result);
                            if(status==0) {
                                Log.e("success","success");
                                Message msg=new Message();
                                Bundle b=new Bundle();
                                b.putInt("0",3);
                                msg.setData(b);
                                myhandle.sendMessage(msg);
                            }
                            break;
                        default:
                            break;
                    }
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t4.start();
    }

    public class handle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle b = msg.getData();
            int n=b.getInt("0");
            if(n==0){
                qrcode=b.getString("qrcode");
            }else if(n==1){
                passwd=b.getString("password");
            }else if(n==2){
                id=b.getString("accountid");
                did=b.getString("did");
                t1.start();
            }
            else if(n==3){
                r.returninfo(passwd);
            }
            if(!(qrcode==null)&&!(passwd==null)&&(n!=3)) {
                st();
            }
        }
    }

    public void start(){
        myhandle=new handle();
        t3.start();
        t2.start();
    }

    //回调
    public interface returnString{
        void returninfo(String passwd);
    }

    public void setret(returnString r){
        this.r=r;
    }

}
