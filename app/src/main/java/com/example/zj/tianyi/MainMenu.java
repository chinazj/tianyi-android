package com.example.zj.tianyi;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenu extends AppCompatActivity {
    private TextView device_status;
    private TextView account_passwd;

    private Button launch;
    private Button inlaunch;
    private Button login_out;
    private Button update;

    private String ip;
    private String id;
    private String did;
    private String author;
    private String basip;
    private String Saccount;
    private String Spasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        this.device_status=(TextView)findViewById(R.id.device_status);
        this.account_passwd=(TextView)findViewById(R.id.account_passwd);

        this.launch=(Button)findViewById(R.id.launch);
        this.inlaunch=(Button)findViewById(R.id.inlaunch);
        this.login_out=(Button)findViewById(R.id.login_out);
        this.update=(Button)findViewById(R.id.update);

        launch.setOnClickListener(new MyOnclikListener());
        inlaunch.setOnClickListener(new MyOnclikListener());
        login_out.setOnClickListener(new MyOnclikListener());
        update.setOnClickListener(new MyOnclikListener());

        login.ssl();
        getip();
        getDivice();

    }
    class MyOnclikListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.launch:

                    account_launch al=new account_launch(ip,author,basip);
                    al.start();
                    al.setret(new account_launch.returnString() {
                        @Override
                        public void returninfo(String passwd) {
                            account_passwd.setText(passwd);
                            Toast.makeText(getApplicationContext(), "上线成功", Toast.LENGTH_SHORT).show();
                            //getbasip();
                        }
                    });
                    break;
                case R.id.inlaunch:
                    Ainlaunch a=new Ainlaunch(ip,basip,id,author);
                    a.stop();
                    a.setreturn(new Ainlaunch.returnDevices() {
                        @Override
                        public void returninfo(int n) {
                            if(n==1){
                                Toast.makeText(getApplicationContext(), "下线成功", Toast.LENGTH_SHORT).show();
                                //getbasip();
                            }
                        }
                    });
                    break;
                case R.id.login_out:
                    Intent intent=new Intent();
                    intent.setClass(MainMenu.this, login.class);
                    MainMenu.this.startActivity(intent);
                    break;
                case R.id.update:
                    getDivice();
                    break;
            }
        }
    }


    public void getip(){
        try {
            SharedPreferences sp = getSharedPreferences("account", MODE_PRIVATE);
            ip=sp.getString("ip","");
            author=sp.getString("author","");
            basip=sp.getString("serverip","");
            id=sp.getString("id","");
            Spasswd=sp.getString("passwd","");
            Saccount=sp.getString("account","");
            Log.e("ip id did author wip",ip+"  "+author+" "+id+" "+basip);
        }catch (Exception e){
            Log.e("异常",e.getMessage());
        }
    }

    public void getDivice(){
        getDeviceList gdl=new getDeviceList(id,author);
        gdl.get();
        gdl.setreturn(new getDeviceList.returnDevices() {
            @Override
            public void returninfo(String[] brasip, int n) {
                Log.e("brasip",brasip[0]+n);
                device_status.setText(n+"个设备在线");
            }
        });
    }
}
