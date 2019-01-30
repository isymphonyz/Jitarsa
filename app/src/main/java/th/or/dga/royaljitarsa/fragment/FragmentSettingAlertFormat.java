package th.or.dga.royaljitarsa.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.warkiz.widget.IndicatorSeekBar;

import java.util.ArrayList;

import th.or.dga.royaljitarsa.Home;
import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.customview.SwitchButton;
import th.or.dga.royaljitarsa.utils.AppPreference;
import th.or.dga.royaljitarsa.utils.FirebaseLogTracking;

public class FragmentSettingAlertFormat extends Fragment {

    private SwitchButton switchSound;
    private SwitchButton switchVibration;

    private FragmentTransaction transaction;

    private AppPreference appPreference;

    public static FragmentSettingAlertFormat newInstance() {
        FragmentSettingAlertFormat fragment = new FragmentSettingAlertFormat();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting_alert_format, container, false);

        addLog();
        initValue();
        initUI(rootView);
        setUI();
        setListener();

        return rootView;
    }

    private void addLog() {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        FirebaseLogTracking firebaseLogTracking = new FirebaseLogTracking(mFirebaseAnalytics);
        firebaseLogTracking.addLogActivity(getText(R.string.log_param_setting).toString());
    }

    private void initValue() {
        appPreference = new AppPreference(getActivity());
        transaction = Home.getInstance(getActivity().getApplicationContext()).transaction;
    }

    private void initUI(View rootView) {
        switchSound = (SwitchButton) rootView.findViewById(R.id.switchSound);
        switchVibration = (SwitchButton) rootView.findViewById(R.id.switchVibration);
    }

    private void setUI() {
        switchSound.setChecked(appPreference.getSettingSound());
        switchVibration.setChecked(appPreference.getSettingVibration());
    }

    private void setListener() {
        switchSound.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                appPreference.setSettingSound(isChecked);
            }
        });
        switchVibration.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                appPreference.setSettingSound(isChecked);
            }
        });
    }

    private void displayFragment(Fragment selectedFragment) {
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.commit();
    }
}
