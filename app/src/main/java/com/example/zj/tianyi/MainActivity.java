package com.example.zj.tianyi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.SharedPreferences.Editor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int a;
        try {
            SharedPreferences sp = getSharedPreferences("first", MODE_PRIVATE);
            a=sp.getInt("first",0);
            if(a==0){
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, login.class);
                MainActivity.this.startActivity(intent);
                Editor editor = sp.edit();
                editor.putInt("first",1);
                editor.commit();
            }else{
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, MainMenu.class);
                MainActivity.this.startActivity(intent);
            }
            Log.e("first",a+"");
        }catch (Exception e){
            Log.e("异常",e.getMessage());
        }


    }
}
