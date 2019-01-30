package th.or.dga.royaljitarsa.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.adapter.FragmentHomeListAdapter;
import th.or.dga.royaljitarsa.adapter.FragmentMeHistoryListAdapter;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;
import th.or.dga.royaljitarsa.utils.AppPreference;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

public class FragmentMePost extends Fragment {

    private String TAG = FragmentMePost.class.getSimpleName();

    private CircleImageView imgProfile;
    private SukhumvitTextView txtName;
    private LinearLayout layoutImageOrMessage;
    private ListView listView;
    private FragmentHomeListAdapter adapter;

    private AppPreference appPreference;
    private ArrayList<String> dateList;
    private ArrayList<String> projectList;
    private String urlImageProfile = "";
    private String name = "";

    private FragmentTransaction transaction;
    private Fragment selectedFragment = null;

    public static FragmentMePost newInstance() {
        FragmentMePost fragment = new FragmentMePost();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post, container, false);

        initValue();
        initUI(rootView);
        setUI();
        setListener();

        return rootView;
    }

    private void initValue() {
        appPreference = new AppPreference(getActivity());
        urlImageProfile = appPreference.getProfileImage();
        name = appPreference.getFullname();

        projectList = new ArrayList<>();
        dateList = new ArrayList<>();

        for(int x=0; x<10; x++) {
            projectList.add("Project: " + x);
            dateList.add("Date: " + x);
        }
    }

    private void initUI(View rootView) {
        imgProfile = (CircleImageView) rootView.findViewById(R.id.imgProfile);
        txtName = (SukhumvitTextView) rootView.findViewById(R.id.txtName);
        layoutImageOrMessage = (LinearLayout) rootView.findViewById(R.id.layoutImageOrMessage);
        listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new FragmentHomeListAdapter(getActivity());
    }

    private void setUI() {
        if(urlImageProfile.equals("")) {
            Glide.with(this)
                    .load(R.mipmap.ic_launcher)
                    .apply(fitCenterTransform()
                            //.placeholder(R.mipmap.ic_launcher)
                            //.error(R.mipmap.ic_launcher)
                            .priority(Priority.HIGH))
                    .into(imgProfile);
        } else {
            Glide.with(this)
                    .load(urlImageProfile)
                    .apply(fitCenterTransform()
                            //.placeholder(R.mipmap.ic_launcher)
                            //.error(R.mipmap.ic_launcher)
                            .priority(Priority.HIGH))
                    .into(imgProfile);
        }

        txtName.setText(name);
    }

    private void setListener() {
        layoutImageOrMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFragment(FragmentMePostImageOrMessage.newInstance());
            }
        });
    }

    public void displayFragment(Fragment selectedFragment) {
        //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutContent, selectedFragment);
        transaction.commit();
    }
}
