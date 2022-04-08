package com.erebsindia.foodle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.erebsindia.foodle.libraries.JRequest;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText PhoneLogin, PasswordLogin;
    SharedPreferences sharedPreferences;
    ShimmerFrameLayout LoginLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        sharedPreferences = getSharedPreferences("NISC", MODE_PRIVATE);
        LoginLoader = findViewById(R.id.LoginLoader);
        PhoneLogin = findViewById(R.id.phone_login);
        PasswordLogin = findViewById(R.id.password_login);

    }

    public void ForgotBtn(View view) {
        if (PhoneLogin.length() == 10) {

            ForgotApi();
        } else {

            CookieBar.build(this)
                    .setMessage("Please enter your Mobile number !")
                    .setCustomView(R.layout.ebs_toast).setCookiePosition(CookieBar.BOTTOM)
                    .setDuration(5000).show();
        }
    }

    public void ForgotApi() {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("otptype", "forgot");
        editor.putString("phone", PhoneLogin.getText().toString());
        editor.apply();

        Intent intents = new Intent(LoginActivity.this, PasswordActivity.class);
        startActivity(intents);

    }

    public void goReg(View v) {

        Intent intents = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intents);

    }

    public void LoginBtn(View view)
    {
        if(PhoneLogin.length()==10 && PasswordLogin.length()>2)
            LoginApi();
        else {
            CookieBar.build(this)
                    .setMessage("Invalid Login Details !")
                    .setCustomView(R.layout.ebs_toast).setCookiePosition(CookieBar.BOTTOM)
                    .setDuration(5000).show();
        }
    }

    public void LoginApi()
    {
        JRequest jRequest;
        LoginLoader.setVisibility(View.VISIBLE);
        try {
            JSONObject RequestJson = new JSONObject();
            RequestJson.put("emailormobile", PhoneLogin.getText().toString());
            RequestJson.put("password", PasswordLogin.getText().toString());
            //RequestJson.put("access_token", sharedPreferences.getString("token", ""));

            jRequest = new JRequest(RequestJson, "login", true, this, new JRequest.TaskCompleteListener(){
                @Override
                public void onTaskComplete(String result) throws JSONException
                {
                    LoginLoader.setVisibility(View.GONE);

                    try
                    {
                        JSONObject Res = new JSONObject(result);
                        String sts = Res.getString("sts");
                        String msg = Res.getString("msg");

                        if (sts.equalsIgnoreCase("01"))
                        {
                            String userObj = Res.getString("user");
                            JSONObject UserObj = new JSONObject(userObj);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("id", UserObj.getString("id"));
                            editor.putString("email", UserObj.getString("email"));
                            editor.putString("name", UserObj.getString("name"));
                            editor.putString("phone", UserObj.getString("mobile"));
                            editor.putString("password", PasswordLogin.getText().toString());
                            editor.apply();

                            Intent intent;
                            intent = new Intent(getApplicationContext(),MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }

                        else
                        {
                            showMsg(msg);
                        }


                    }
                    catch (Exception e)
                    { Log.e("catcherror", e + "d"); }
                }
            });
            jRequest.execute();
        }
        catch (JSONException e)
        { e.printStackTrace(); }
    }

    public void showMsg(String msg)
    {
        CookieBar.build(this)
                .setMessage(msg)
                .setCustomView(R.layout.ebs_toast).setCookiePosition(CookieBar.BOTTOM)
                .setDuration(5000).show();
    }



    public void goBack(View view)
    {
        super.onBackPressed();
    }
}