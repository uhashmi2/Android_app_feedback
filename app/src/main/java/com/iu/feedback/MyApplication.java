package com.iu.feedback;

import android.app.Application;

import com.iu.feedback.model.TypefaceUtil;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Quicksand-Medium.ttf");

    }

}