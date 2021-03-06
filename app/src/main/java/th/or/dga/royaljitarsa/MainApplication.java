package th.or.dga.royaljitarsa;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.MapsInitializer;
import com.google.firebase.FirebaseApp;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import th.or.dga.royaljitarsa.utils.FontsOverride;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //FirebaseApp.initializeApp(this);

        MapsInitializer.initialize(this);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("scgexpress.realm").build();
        Realm.setDefaultConfiguration(config);

        Fabric.with(this, new Crashlytics());

        //FacebookSdk.sdkInitialize(getApplicationContext());

        //set Custom Typeface
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/sukhumvitset.ttf");
    }
}
