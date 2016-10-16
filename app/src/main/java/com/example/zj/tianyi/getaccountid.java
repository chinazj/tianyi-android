package com.example.zj.tianyi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;


/**
 * Created by zj on 16-10-12.
 */
public class getaccountid {
    private String Account;
    private String Passwd;
    private URL url;
    private String author;
    private handle hander;
    private String id;
    private returnstring r;

    public getaccountid(String account,String passwd){
        this.Account=account;
        this.Passwd=passwd;
        base64();
    }

    public void start()  {
        Log.e("author",author);
        Thread t=new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    url = new URL("https://www.loocha.com.cn:8443/login");
                    HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Authorization", author);
//                    conn.setDoOutput(true);
//                    conn.setDoInput(true);
//                    connection.setRequestMethod("GET");
//                    connection.setRequestProperty("Content-Language", "zh-CN");
//                    connection.setConnectTimeout(30000);
//                    connection.setReadTimeout(30000);
                    switch(conn.getResponseCode()){
                        case 401:
                            Log.e("statue",""+conn.getResponseCode());
                            break;
                        case 200:
                            //int status=conn.getResponseCode();
                            Log.e("statue",""+conn.getResponseCode());
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
                                b.putInt("status",conn.getResponseCode());
                                msg.setData(b);
                                hander.sendMessage(msg);
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
        hander=new handle();
        t.start();
    }

    public String getAuthor() {
        return author;
    }

    public String base64(){
        String ap=Account+":"+Passwd;
        String encoder;
        encoder=(new sun.misc.BASE64Encoder()).encode(ap.getBytes());
        //Log.e("encoder",encoder);
        author="Basic "+encoder;
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                // TODO Auto-generated method stub
                return true;
            }
        });
        return author;
    }

    public class handle extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle b = msg.getData();
            String accountid=b.getString("accountid");
            String did=b.getString("did");
            int status=b.getInt("status");
            r.info(accountid,did,status);
        }
    }

    public interface returnstring{
        void info(String accountid,String did,int status);
    }

    public void setreturn(returnstring r){
        this.r=r;
    }

}
