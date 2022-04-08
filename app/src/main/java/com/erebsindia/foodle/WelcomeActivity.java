package com.erebsindia.foodle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.erebsindia.foodle.models.WelcomeSliderModel;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    List<WelcomeSliderModel> welcomeSliderModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();
    }

    public void goBack(View view)
    {
        super.onBackPressed();
    }
}