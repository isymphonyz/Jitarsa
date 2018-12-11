package th.or.dga.royaljitarsa.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.adapter.FragmentSaveListAdapter;
import th.or.dga.royaljitarsa.utils.FirebaseLogTracking;

public class FragmentSave extends Fragment {

    private ListView listView;
    private FragmentSaveListAdapter adapter;

    private ArrayList<String> imageList;
    private ArrayList<String> nameList;
    private ArrayList<String> projectList;
    private ArrayList<String> dateList;

    public static FragmentSave newInstance() {
        FragmentSave fragment = new FragmentSave();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_save, container, false);

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
        firebaseLogTracking.addLogActivity(getText(R.string.log_param_save).toString());
    }

    private void initValue() {
        imageList = new ArrayList<>();
        imageList.add("");
        imageList.add("");
        imageList.add("");

        nameList = new ArrayList<>();
        nameList.add("");
        nameList.add("");
        nameList.add("");

        projectList = new ArrayList<>();
        projectList.add("");
        projectList.add("");
        projectList.add("");

        dateList = new ArrayList<>();
        dateList.add("");
        dateList.add("");
        dateList.add("");
    }

    private void initUI(View rootView) {
        listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new FragmentSaveListAdapter(getActivity());
    }

    private void setUI() {
        adapter.setImageList(imageList);
        adapter.setDateList(dateList);
        adapter.setNameList(nameList);
        adapter.setProjectList(projectList);

        listView.setAdapter(adapter);
    }

    private void setListener() {

    }
}
