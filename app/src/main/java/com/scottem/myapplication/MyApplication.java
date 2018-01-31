package com.scottem.myapplication;

import android.app.Application;
import android.util.Log;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.mixpanel.android.mpmetrics.OnMixpanelTweaksUpdatedListener;
import com.mixpanel.android.mpmetrics.Tweak;

import java.util.Set;


public class MyApplication extends Application {

    public static final String projectToken = "a3fc8ed5d0de1dd3bc9df7ab9a6f005a";
    public static final Tweak<Boolean> showAds = MixpanelAPI.booleanTweak("Show ads", false);

    private MixpanelAPI mMixpanel;
    @Override
    public void onCreate() {
        super.onCreate();

        mMixpanel = MixpanelAPI.getInstance(this, projectToken);
        String distinctId = "Scott";
        mMixpanel.identify(distinctId);
        mMixpanel.getPeople().identify(distinctId);
        mMixpanel.getPeople().set("$email", "scott@mixpanel.com");
        mMixpanel.getPeople().addOnMixpanelTweaksUpdatedListener(new OnMixpanelTweaksUpdatedListener() {
            @Override
            public void onMixpanelTweakUpdated(Set<String> updatedTweaksName) {
                Log.d("scott", "onMixpanelTweakUpdated");
                mMixpanel.getPeople().joinExperimentIfAvailable();
            }
        });



    }

    public MixpanelAPI getAnalyticsTracker() {
        return mMixpanel;
    }
}
