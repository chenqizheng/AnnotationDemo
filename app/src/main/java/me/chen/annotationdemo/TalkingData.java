package me.chen.annotationdemo;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Chen on 2017/10/31.
 */

public class TalkingData {
    public static HashMap<String, String> mTalkingDataMap = new HashMap<>();

    static Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            if (mTalkingDataMap.containsKey(activity.getClass().getName())) {
                String name = mTalkingDataMap.get(activity.getClass().getName());
                Log.i("TalkingData", name + "  onCreate");
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };


    public static void init(Application application) {
        TalkingDataInjectImpl.inject(mTalkingDataMap);
        application.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
    }
}
