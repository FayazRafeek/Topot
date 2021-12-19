package com.example.topot2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaDrmException;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences pref = getSharedPreferences("PREF", MODE_PRIVATE);

                if(pref.getBoolean("LOGIN",false))
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                else
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                finish();
            }
        },2500);
    }
}
