package th.or.dga.royaljitarsa.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import th.or.dga.royaljitarsa.R;

public class FragmentMe extends Fragment {

    private LinearLayout layoutPost;
    private LinearLayout layoutHistory;
    private LinearLayout layoutImage;
    private LinearLayout layoutFriend;

    private FragmentTransaction transaction;
    private Fragment selectedFragment = null;

    private String menu = "";

    public static FragmentMe newInstance() {
        FragmentMe fragment = new FragmentMe();
        return fragment;
    }

    public static FragmentMe newInstance(Bundle bundle) {
        FragmentMe fragment = new FragmentMe();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_me, container, false);

        initValue();
        initUI(rootView);
        setUI();
        setListener();

        checkMenu();

        return rootView;
    }

    private void initValue() {
        menu = getArguments().getString("menu");
    }

    private void initUI(View rootView) {
        layoutPost = (LinearLayout) rootView.findViewById(R.id.layoutPost);
        layoutHistory = (LinearLayout) rootView.findViewById(R.id.layoutHistory);
        layoutImage = (LinearLayout) rootView.findViewById(R.id.layoutImage);
        layoutFriend = (LinearLayout) rootView.findViewById(R.id.layoutFriend);
    }

    private void setUI() {

    }

    private void setListener() {
        layoutPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFragment = FragmentMePost.newInstance();
                displayFragment(selectedFragment);
            }
        });
        layoutHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFragment = FragmentMeHistory.newInstance();
                displayFragment(selectedFragment);
            }
        });
        layoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFragment = FragmentMeImage.newInstance();
                displayFragment(selectedFragment);
            }
        });
        layoutFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFragment = FragmentMeFriend.newInstance();
                displayFragment(selectedFragment);
            }
        });
    }

    public void displayFragment(Fragment selectedFragment) {
        //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutContent, selectedFragment);
        transaction.commit();
    }

    public void displayFragment(FragmentTransaction transaction, Fragment selectedFragment) {
        //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutContent, selectedFragment);
        transaction.commit();
    }

    private void checkMenu() {
        if(menu.equals("post")) {
            layoutPost.performClick();
        } else {
            layoutHistory.performClick();
        }
    }
}
