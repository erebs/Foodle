package com.erebsindia.foodle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        sharedPreferences = getSharedPreferences("NISC",MODE_PRIVATE);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(sharedPreferences.getString("phone", "").length()==10 && sharedPreferences.getString("id", "").length()>0)
                {
                    Intent intents = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intents);
                    finish();
                }else {

                    Intent intents = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intents);
                    finish();
                }
            }
        }, 2000);

    }



    public void goBack(View view)
    {
        super.onBackPressed();
    }

}