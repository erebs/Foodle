package com.erebsindia.foodle.libraries;

public class SmsContents {

    public String OtpContent(String Otp)
    {
        String Msg = Otp+" is the OTP for your FoodleMart app. Please do not share this with anyone.";
        return Msg;
    }

}
