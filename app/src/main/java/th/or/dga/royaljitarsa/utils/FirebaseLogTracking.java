package th.or.dga.royaljitarsa.utils;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseLogTracking {

    private String TAG = FirebaseLogTracking.class.getSimpleName();

    private FirebaseAnalytics mFirebaseAnalytics;

    public FirebaseLogTracking(FirebaseAnalytics mFirebaseAnalytics) {
        this.mFirebaseAnalytics = mFirebaseAnalytics;
    }

    public void addLogActivity(String screenName) {
        Log.d(TAG, "screenName: " + screenName);
        Bundle bundle = new Bundle();
        bundle.putString(MyConfiguration.FIREBASE_ANALYTIC_PARAM_SCREEN, screenName);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public void addLogContent(String categoryName, String contentID) {
        Log.d(TAG, "categoryName: " + categoryName + ", contentID: " + contentID);
        Bundle bundle = new Bundle();
        bundle.putString(MyConfiguration.FIREBASE_ANALYTIC_PARAM_CONTENT_ID, contentID);
        bundle.putString(MyConfiguration.FIREBASE_ANALYTIC_PARAM_CATEGORY_NAME, categoryName);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

}
