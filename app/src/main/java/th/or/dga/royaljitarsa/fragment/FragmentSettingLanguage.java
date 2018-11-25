package th.or.dga.royaljitarsa.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.utils.AppPreference;

public class FragmentSettingLanguage extends Fragment {

    private RelativeLayout layoutThai;
    private RelativeLayout layoutEnglish;
    private ImageView imgThai;
    private ImageView imgEnglish;

    private AppPreference appPreference;
    private String language = "";

    public static FragmentSettingLanguage newInstance() {
        FragmentSettingLanguage fragment = new FragmentSettingLanguage();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting_language, container, false);

        initValue();
        initUI(rootView);
        setUI();
        setListener();

        return rootView;
    }

    private void initValue() {
        appPreference = new AppPreference(getActivity());
        language = appPreference.getLanguage();
    }

    private void initUI(View rootView) {
        layoutThai = (RelativeLayout) rootView.findViewById(R.id.layoutThai);
        layoutEnglish = (RelativeLayout) rootView.findViewById(R.id.layoutEnglish);
        imgThai = (ImageView) rootView.findViewById(R.id.imgThai);
        imgEnglish = (ImageView) rootView.findViewById(R.id.imgEnglish);
    }

    private void setUI() {

    }

    private void setListener() {
        layoutThai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgThai.setVisibility(View.VISIBLE);
                imgEnglish.setVisibility(View.GONE);
            }
        });

        layoutEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgThai.setVisibility(View.GONE);
                imgEnglish.setVisibility(View.VISIBLE);
            }
        });
    }
}
