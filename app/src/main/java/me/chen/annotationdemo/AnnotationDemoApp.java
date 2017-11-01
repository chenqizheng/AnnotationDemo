package me.chen.annotationdemo;

import android.app.Application;

/**
 * Created by Chen on 2017/10/31.
 */

public class AnnotationDemoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TalkingData.init(this);
    }
}
