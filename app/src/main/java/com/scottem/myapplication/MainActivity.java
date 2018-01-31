package com.scottem.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.mixpanel.android.mpmetrics.OnMixpanelTweaksUpdatedListener;
import com.mixpanel.android.mpmetrics.Tweak;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private TextView mMainText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainText = (TextView)findViewById(R.id.main_text);

        trackAppLaunched();

        final MixpanelAPI mixpanel = ((MyApplication) getApplication()).getAnalyticsTracker();
        mixpanel.getPeople().addOnMixpanelTweaksUpdatedListener(new OnMixpanelTweaksUpdatedListener() {
            @Override
            public void onMixpanelTweakUpdated(Set<String> updatedTweaksName) {
                mixpanel.getPeople().joinExperimentIfAvailable();
                Log.d("scott", "onMixpanelTweakUpdated inside activity");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI();
                    }
                });
            }
        });

        updateUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((MyApplication) getApplication()).getAnalyticsTracker().reset();
    }

    public void updateUI() {
        Log.d("scott", "updateUI in thread " + Thread.currentThread().getName());
        if (MyApplication.showAds.get()) {
            // Remove loading indicator
            mMainText.setText("Hello Mixpanel!");
        } else {
            mMainText.setText("Hello World!");
        }
    }

    public void trackAppLaunched() {
        MixpanelAPI mixpanel = ((MyApplication) getApplication()).getAnalyticsTracker();
        try {
            JSONObject props = new JSONObject();
            props.put("Test", true);
            props.put("First launch", false);
            props.put("Experiment setup", true);
            mixpanel.track("App launched", props);
        } catch (JSONException e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }
    }
}
