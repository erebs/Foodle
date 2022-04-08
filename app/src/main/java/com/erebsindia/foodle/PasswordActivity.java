package com.erebsindia.foodle;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.erebsindia.foodle.libraries.JRequest;
import com.erebsindia.foodle.libraries.SmsContents;
import com.erebsindia.foodle.libraries.SmsRequest;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class PasswordActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText Otp,Pass1,Pass2;
    ShimmerFrameLayout LoginLoader;

    SmsContents smsContents;
    SmsRequest smsRequest;
    TextView PhoneText, ResendBtn,CntBtn;
    String otp="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        getSupportActionBar().hide();
        LoginLoader = findViewById(R.id.LoginLoader2);

        sharedPreferences = getSharedPreferences("NISC",MODE_PRIVATE);
        PhoneText = findViewById(R.id.PhoneText);
        ResendBtn = findViewById(R.id.ResendBtn);
        Otp = findViewById(R.id.otp_pass);
        Pass1 = findViewById(R.id.pass1_pass);
        Pass2 = findViewById(R.id.pass2_pass);
        CntBtn = findViewById(R.id.CntBtn);
        PhoneText.setText("+91 "+sharedPreferences.getString("phone", "")+" ");
        RequeestOtp(sharedPreferences.getString("phone", ""));

        if(sharedPreferences.getString("ftype", "").equalsIgnoreCase("forgot"))
        {
            Pass1.setHint("New Password");
            Pass2.setHint("Confirm new Password");
            CntBtn.setText("Reset");
        }
        else
        {
            Pass1.setHint("Password");
            Pass2.setHint("Confirm Password");
            CntBtn.setText("Continue");
        }

        Otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() >0)
                {


                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()==4)
                {
                    if(s.toString().equalsIgnoreCase(otp))
                    {
                        Pass1.setEnabled(true);
                        Pass2.setEnabled(true);
                    }else
                    {
                        showMsg("Invalid OTP !");
                    }

                }else
                {
                    Pass1.setEnabled(false);
                    Pass2.setEnabled(false);
                }

            }
        });


    }


    public void GotoNext(View view)
    {
        if(Otp.length()==4 && Pass1.length()>2)
        {
            if(Pass1.getText().toString().equalsIgnoreCase(Pass2.getText().toString()))
            {

                CtnApi();


            }else
            {
                showMsg("Passwords do not match !");
            }

        }
        else{
            showMsg("Some required fields are empty !");
        }
        }


    public void ResendCounter()
    {
        ResendBtn.setClickable(false);
        new CountDownTimer(20000, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                String resendMsg = "  Resend in "+millisUntilFinished / 1000+" sec</font>";
                ResendBtn.setText(Html.fromHtml(resendMsg));
            }

            public void onFinish() {
                ResendBtn.setText(Html.fromHtml("<b> Resend Now</b>"));
                ResendBtn.setClickable(true);
            }

        }.start();

    }

    public void ResendBtn(View view)
    {
        RequeestOtp(sharedPreferences.getString("phone", ""));
    }

    public void RequeestOtp(String Number)
    {
        ResendCounter();
        Random rand = new Random();
        otp = String.format("%04d", rand.nextInt(10000));

//        showMsg("otp"+otp);

        String msg = otp+" is the OTP for your FoodleMart app. Please do not share this with anyone.";

        smsRequest = new SmsRequest(msg, Number, this, new SmsRequest.TaskCompleteListener(){
            @Override
            public void onTaskComplete(String result) throws JSONException
            {


                try
                {
                    JSONObject Res = new JSONObject(result);
                    String sts = Res.getString("sts");
                }
                catch (Exception e)
                { Log.e("catcherror", e + "d"); }
            }
        });
        smsRequest.execute();
    }

    public void CtnApi()
    {
        String apiPath="";
        JRequest jRequest;
        LoginLoader.setVisibility(View.VISIBLE);
        try {
            JSONObject RequestJson = new JSONObject();
            if(sharedPreferences.getString("ftype", "").equalsIgnoreCase("forgot"))
            {
                apiPath="change/password";
            }
            else
            {
                RequestJson.put("name", sharedPreferences.getString("name", ""));
                RequestJson.put("email", sharedPreferences.getString("email", ""));
                apiPath="register";
            }
            RequestJson.put("number", sharedPreferences.getString("phone", ""));
            RequestJson.put("password", Pass1.getText().toString());
            //RequestJson.put("access_token", sharedPreferences.getString("token", ""));

            jRequest = new JRequest(RequestJson, apiPath, true, this, new JRequest.TaskCompleteListener(){
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
                            editor.putString("password", Pass1.getText().toString());
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