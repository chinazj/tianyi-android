package com.example.zj.tianyi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import sun.misc.BASE64Decoder;

public class login extends AppCompatActivity {
    private EditText account;
    private EditText passwd;
    private EditText Eip;
    private TextView server_ip;
    private Button btn;
    private Button btn2;

    private String Spasswd=null;
    private String ip=null;
    private String id=null;
    private String did_=null;
    private String Saccount=null;
    private String author=null;
    private String serverip=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.account=(EditText)findViewById(R.id.account);
        this.passwd=(EditText)findViewById(R.id.passwd);
        this.Eip=(EditText)findViewById(R.id.ip);

        this.server_ip=(TextView)findViewById(R.id.serverip);

        this.btn=(Button)findViewById(R.id.btn);
        this.btn2=(Button)findViewById(R.id.btn2);

        //
        account.setText("17751776505");
        passwd.setText("147258");
        //

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Saccount=account.getText().toString().trim();
                Spasswd=passwd.getText().toString().trim();
                ip=Eip.getText().toString().trim();
                Log.e("onClick","qwe");
                if((!Saccount.equals(""))&&(!Spasswd.equals(""))) {
                    author=base64();
                    ssl();
                    Log.e("onClick","qwe");
                    getaccountid getid = new getaccountid(Saccount, Spasswd);
                    getid.start();
                    getid.setreturn(new getaccountid.returnstring() {
                        @Override
                        public void info(String accountid, String did,int status) {
                            id=accountid;
                            did_=did;
                            Log.e("status",did_+" "+id);
                            if(status==401&&id.equals(""))
                                Toast.makeText(login.this, "密码错误", Toast.LENGTH_SHORT).show();
                            else{
                                if((!did_.equals(""))&&(!id.equals(""))) {
                                    save_accounoidt();
                                    Intent intent = new Intent();
                                    intent.setClass(login.this, MainMenu.class);
                                    login.this.startActivity(intent);
                                }
                            }
                        }
                    });


                    //status=getid.getNet_status();
                    author=getid.getAuthor();
                    //Log.e("author status :",author+"  "+status);                  //

                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ssl();
                brasip brasip=new brasip();
                brasip.getip();
                brasip.setreturn(new brasip.returnbascip() {
                    @Override
                    public void returnString(String bascip,String wanip) {
                        serverip=bascip;
                        ip=wanip;
                        server_ip.setText(serverip);
                        Eip.setText(ip);
                    }
                });
            }
        });
    }



    public void save_accounoidt(){
        try {
            SharedPreferences sp = getSharedPreferences("account", MODE_PRIVATE);
            Editor editor = sp.edit();
            editor.putString("account", account.getText().toString().trim());
            editor.putString("passwd", passwd.getText().toString().trim());
            editor.putString("author",author);
            editor.putString("ip",ip);
            editor.putString("id",id);
            //Log.e("id",id);
            editor.putString("serverip",serverip);
            editor.commit();
        }catch (Exception e){
                Log.e("异常",e.getMessage());
        }
    }

    public String base64(){
        String ap=Saccount+":"+Spasswd;
        String encoder;
        encoder=(new sun.misc.BASE64Encoder()).encode(ap.getBytes());
        //Log.e("encoder",encoder);
        encoder="Basic "+encoder;
        return encoder;
    }

    public static void ssl(){
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }


}
