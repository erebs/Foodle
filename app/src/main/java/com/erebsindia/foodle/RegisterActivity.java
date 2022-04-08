package com.erebsindia.foodle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.aviran.cookiebar2.CookieBar;

public class RegisterActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText Name,Email,Phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        sharedPreferences = getSharedPreferences("NISC",MODE_PRIVATE);
        Name = findViewById(R.id.name_reg);
        Email = findViewById(R.id.email_reg);
        Phone = findViewById(R.id.phone_reg);

    }

    public void goBack(View view)
    {
        super.onBackPressed();
    }

    public void regBtn(View v)
    {
       if(Phone.length()==10 && Name.length()>2 && Email.length()>2)
       {
           SharedPreferences.Editor editor = sharedPreferences.edit();
           editor.putString("otptype", "forgot");
           editor.putString("name", Name.getText().toString());
           editor.putString("email", Email.getText().toString());
           editor.putString("phone", Phone.getText().toString());
           editor.apply();

           Intent intents = new Intent(RegisterActivity.this, PasswordActivity.class);
           startActivity(intents);
       }
       else
       showMsg("Some required fields are empty !");
    }

    public void showMsg(String msg)
    {
        CookieBar.build(this)
                .setMessage(msg)
                .setCustomView(R.layout.ebs_toast).setCookiePosition(CookieBar.BOTTOM)
                .setDuration(5000).show();
    }

}