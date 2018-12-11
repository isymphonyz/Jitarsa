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
import th.or.dga.royaljitarsa.utils.FirebaseLogTracking;

public class FragmentSetting extends Fragment {

    private RelativeLayout layoutAlertFormat;
    private RelativeLayout layoutShare;
    private RelativeLayout layoutLanguage;
    private SwitchButton switchAlert;
    private IndicatorSeekBar seekBarFollow;

    private FragmentTransaction transaction;

    private Typeface typeface;
    private ArrayList<String> followList;

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
        seekBarFollow = (IndicatorSeekBar) rootView.findViewById(R.id.seekBarFollow);
    }

    private void setUI() {
        seekBarFollow.customTickTextsTypeface(typeface);
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
    }

    private void displayFragment(Fragment selectedFragment) {
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.commit();
    }
}
