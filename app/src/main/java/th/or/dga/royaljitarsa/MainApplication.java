package th.or.dga.royaljitarsa;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import th.or.dga.royaljitarsa.utils.FontsOverride;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());

        //FacebookSdk.sdkInitialize(getApplicationContext());

        //set Custom Typeface
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/sukhumvitset.ttf");
    }
}
