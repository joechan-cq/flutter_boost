package com.idlefish.flutterboost.containers;

import android.content.Context;
import android.content.Intent;

import com.idlefish.flutterboost.FlutterBoost;
import com.idlefish.flutterboost.FlutterBoostUtils;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import io.flutter.embedding.android.FlutterActivityLaunchConfigs;
import io.flutter.embedding.android.FlutterFragment;
import io.flutter.embedding.android.FlutterFragmentActivity;
import io.flutter.embedding.android.TransparencyMode;

import static com.idlefish.flutterboost.containers.FlutterActivityLaunchConfigs.EXTRA_BACKGROUND_MODE;
import static com.idlefish.flutterboost.containers.FlutterActivityLaunchConfigs.EXTRA_CACHED_ENGINE_ID;
import static com.idlefish.flutterboost.containers.FlutterActivityLaunchConfigs.EXTRA_DESTROY_ENGINE_WITH_ACTIVITY;
import static com.idlefish.flutterboost.containers.FlutterActivityLaunchConfigs.EXTRA_UNIQUE_ID;
import static com.idlefish.flutterboost.containers.FlutterActivityLaunchConfigs.EXTRA_URL;
import static com.idlefish.flutterboost.containers.FlutterActivityLaunchConfigs.EXTRA_URL_PARAM;

/**
 * @author : Joe Chan
 * @date : 2024/12/17 10:27
 */
public class BoostFragmentActivity extends FlutterFragmentActivity {

    private FlutterBoostFragment boostFragment;

    @NonNull
    @Override
    protected FlutterFragment createFlutterFragment() {
        Intent intent = getIntent();
        FlutterBoostFragment.CachedEngineFragmentBuilder builder =
                new FlutterBoostFragment.CachedEngineFragmentBuilder();
        builder.destroyEngineWithFragment(intent.getBooleanExtra(EXTRA_DESTROY_ENGINE_WITH_ACTIVITY, false))
                .url(intent.getStringExtra(EXTRA_URL))
                .urlParams((Map<String, Object>) intent.getSerializableExtra(EXTRA_URL_PARAM))
                .uniqueId(intent.getStringExtra(EXTRA_UNIQUE_ID));
        String backgroundMode = intent.getStringExtra(EXTRA_BACKGROUND_MODE);
        if (FlutterActivityLaunchConfigs.BackgroundMode.transparent.name().equalsIgnoreCase(backgroundMode)) {
            builder.transparencyMode(TransparencyMode.transparent);
        } else {
            builder.transparencyMode(TransparencyMode.opaque);
        }
        boostFragment =  builder.build();
        return boostFragment;
    }

    @Override
    public void onBackPressed() {
        if (boostFragment != null) {
            boostFragment.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    public static class CachedEngineIntentBuilder {
        private final Class<? extends BoostFragmentActivity> activityClass;
        private boolean destroyEngineWithActivity = false;
        private String backgroundMode = FlutterActivityLaunchConfigs.BackgroundMode.opaque.name();
        private String url;
        private HashMap<String, Object> params;
        private String uniqueId;

        public CachedEngineIntentBuilder() {
            this.activityClass = BoostFragmentActivity.class;
        }

        public CachedEngineIntentBuilder(Class<? extends BoostFragmentActivity> activityClass) {
            this.activityClass = activityClass;
        }


        public BoostFragmentActivity.CachedEngineIntentBuilder destroyEngineWithActivity(boolean destroyEngineWithActivity) {
            this.destroyEngineWithActivity = destroyEngineWithActivity;
            return this;
        }


        public BoostFragmentActivity.CachedEngineIntentBuilder backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode backgroundMode) {
            this.backgroundMode = backgroundMode.name();
            return this;
        }

        public BoostFragmentActivity.CachedEngineIntentBuilder url(String url) {
            this.url = url;
            return this;
        }

        public BoostFragmentActivity.CachedEngineIntentBuilder urlParams(Map<String, Object> params) {
            this.params = (params instanceof HashMap) ? (HashMap) params : new HashMap<String,
                    Object>(params);
            return this;
        }

        public BoostFragmentActivity.CachedEngineIntentBuilder uniqueId(String uniqueId) {
            this.uniqueId = uniqueId;
            return this;
        }

        public Intent build(Context context) {
            return new Intent(context, activityClass)
                    .putExtra(EXTRA_CACHED_ENGINE_ID, FlutterBoost.ENGINE_ID) // default engine
                    .putExtra(EXTRA_DESTROY_ENGINE_WITH_ACTIVITY, destroyEngineWithActivity)
                    .putExtra(EXTRA_BACKGROUND_MODE, backgroundMode)
                    .putExtra(EXTRA_URL, url)
                    .putExtra(EXTRA_URL_PARAM, params)
                    .putExtra(EXTRA_UNIQUE_ID, uniqueId != null ? uniqueId :
                            FlutterBoostUtils.createUniqueId(url));
        }
    }
}
