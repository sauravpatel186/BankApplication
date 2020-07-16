package com.example.bankapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=3000;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pb=findViewById(R.id.progressbar);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(i);
                pb.setVisibility(View.VISIBLE);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
