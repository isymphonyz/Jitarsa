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
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.util.ArrayList;

import th.or.dga.royaljitarsa.Home;
import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.customview.SwitchButton;
import th.or.dga.royaljitarsa.utils.AppPreference;
import th.or.dga.royaljitarsa.utils.FirebaseLogTracking;

public class FragmentSetting extends Fragment {

    private RelativeLayout layoutAlertFormat;
    private RelativeLayout layoutShare;
    private RelativeLayout layoutLanguage;
    private SwitchButton switchAlert;
    private SwitchButton switchLocation;
    private SwitchButton switchSchedule;
    private SwitchButton switchNearBy;
    private IndicatorSeekBar seekBarFollow;

    private FragmentTransaction transaction;

    private Typeface typeface;
    private ArrayList<String> followList;

    private AppPreference appPreference;

    public static FragmentSetting newInstance() {
        FragmentSetting fragment = new FragmentSetting();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);

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

        followList = new ArrayList<>();
        followList.add(getText(R.string.fragment_setting_txt_follow_500_m).toString());
        followList.add(getText(R.string.fragment_setting_txt_follow_1_km).toString());
        followList.add(getText(R.string.fragment_setting_txt_follow_2_km).toString());

        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/sukhumvitset.ttf");
    }

    private void initUI(View rootView) {
        layoutAlertFormat = (RelativeLayout) rootView.findViewById(R.id.layoutAlertFormat);
        layoutShare = (RelativeLayout) rootView.findViewById(R.id.layoutShare);
        layoutLanguage = (RelativeLayout) rootView.findViewById(R.id.layoutLanguage);
        switchAlert = (SwitchButton) rootView.findViewById(R.id.switchAlert);
        switchLocation = (SwitchButton) rootView.findViewById(R.id.switchLocation);
        seekBarFollow = (IndicatorSeekBar) rootView.findViewById(R.id.seekBarFollow);
        switchSchedule = (SwitchButton) rootView.findViewById(R.id.switchSchedule);
        switchNearBy = (SwitchButton) rootView.findViewById(R.id.switchNearBy);
    }

    private void setUI() {
        seekBarFollow.customTickTextsTypeface(typeface);

        switchAlert.setChecked(appPreference.getSettingNotification());
        switchLocation.setChecked(appPreference.getSettingLocation());
        seekBarFollow.setProgress((float) (appPreference.getSettingDistance()*100/2));
        switchSchedule.setChecked(appPreference.getSettingNotificationActivity());
        switchNearBy.setChecked(appPreference.getSettingNotificationActivityNearBy());
    }

    private void setListener() {
        layoutAlertFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, FragmentSettingAlertFormat.newInstance());
                transaction.commit();*/
                //Home.getInstance(getActivity().getApplicationContext()).displayFragment(FragmentSettingAlertFormat.newInstance());
                displayFragment(FragmentSettingAlertFormat.newInstance());
            }
        });

        layoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Home.getInstance(getActivity().getApplicationContext()).displayFragment(FragmentSettingShare.newInstance());
                displayFragment(FragmentSettingShare.newInstance());
            }
        });

        layoutLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayFragment(FragmentSettingLanguage.newInstance());
            }
        });
        switchAlert.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                appPreference.setSettingNotification(isChecked);
            }
        });
        switchLocation.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                appPreference.setSettingLocation(isChecked);
            }
        });
        switchSchedule.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                appPreference.setSettingNotificationActivity(isChecked);
            }
        });
        switchNearBy.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                appPreference.setSettingNotificationActivityNearBy(isChecked);
            }
        });
        seekBarFollow.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });
    }

    private void displayFragment(Fragment selectedFragment) {
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
