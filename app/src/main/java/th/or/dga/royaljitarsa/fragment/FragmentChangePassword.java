package th.or.dga.royaljitarsa.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import th.or.dga.royaljitarsa.R;

public class FragmentChangePassword extends Fragment {

    public static FragmentChangePassword newInstance() {
        FragmentChangePassword fragment = new FragmentChangePassword();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.forget_password, container, false);
    }
}
