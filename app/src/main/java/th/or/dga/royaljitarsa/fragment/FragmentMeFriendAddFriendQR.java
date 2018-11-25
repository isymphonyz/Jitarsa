package th.or.dga.royaljitarsa.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import th.or.dga.royaljitarsa.R;

public class FragmentMeFriendAddFriendQR extends Fragment {

    public static FragmentMeFriendAddFriendQR newInstance() {
        FragmentMeFriendAddFriendQR fragment = new FragmentMeFriendAddFriendQR();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        return rootView;
    }
}
