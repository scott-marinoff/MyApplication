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

    public static final String projectToken = "a3fc8ed5d0de1dd3bc9df7ab9a6f005a";
    public final Tweak<Boolean> showAds = MixpanelAPI.booleanTweak("Show ads", false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);
        // show loading indicator of some sort (splash screen, spinner...)
        mixpanel.identify(mixpanel.getDistinctId());
        mixpanel.getPeople().identify(mixpanel.getDistinctId());

        try {
            JSONObject props = new JSONObject();
            props.put("Test", true);
            props.put("First launch", false);
            props.put("Experiment setup", true);
            mixpanel.track("App launched", props);
        } catch (JSONException e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }

        mixpanel.getPeople().addOnMixpanelTweaksUpdatedListener(new OnMixpanelTweaksUpdatedListener() {
            @Override
            public void onMixpanelTweakUpdated(Set<String> updatedTweaksName) {
                mixpanel.getPeople().joinExperimentIfAvailable();
                if (showAds.get()) {
                    // Remove loading indicator
                    Log.d("msg", "We're tweaking!");
                    TextView mainText = (TextView)findViewById(R.id.main_text);
                    mainText.setText("Hello Mixpanel!");
                }
            }
        });
    }
}
