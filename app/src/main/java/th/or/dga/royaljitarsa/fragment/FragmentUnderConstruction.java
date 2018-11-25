package th.or.dga.royaljitarsa.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import th.or.dga.royaljitarsa.R;

public class FragmentUnderConstruction extends Fragment {

    private String TAG = FragmentUnderConstruction.this.getClass().getSimpleName();

    public static FragmentUnderConstruction newInstance() {
        FragmentUnderConstruction fragment = new FragmentUnderConstruction();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_under_construction, container, false);

        initValue();
        initUI(rootView);
        setUI();
        setListener();

        return rootView;
    }

    private void initValue() {

    }

    private void initUI(View rootView) {

    }

    private void setUI() {

    }

    private void setListener() {

    }
}
