package th.or.dga.royaljitarsa.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.adapter.FragmentSearchListAdapter;

public class FragmentSearch extends Fragment {
    private String TAG = getClass().getSimpleName();

    private ListView listView;
    private FragmentSearchListAdapter adapter;

    private ArrayList<String> imageList;
    private ArrayList<String> projectList;
    private ArrayList<String> dateList;

    public static FragmentSearch newInstance() {
        FragmentSearch fragment = new FragmentSearch();
        return fragment;
    }

    public static FragmentSearch newInstance(Bundle bundle) {
        FragmentSearch fragment = new FragmentSearch();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        String keyword = getArguments().getString("keyword");
        Log.d(TAG, "Keyword: " + keyword);

        initValue();
        initUI(rootView);
        setUI();
        setListener();

        return rootView;
    }

    private void initValue() {
        imageList = new ArrayList<>();
        projectList = new ArrayList<>();
        dateList = new ArrayList<>();

        for(int x=0; x<10; x++) {
            imageList.add("x: " + x);
            projectList.add("Project: " + x);
            dateList.add("Date: " + x);
        }
    }

    private void initUI(View rootView) {
        listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new FragmentSearchListAdapter(getActivity());
    }

    private void setUI() {
        adapter.setImageList(imageList);
        adapter.setDateList(dateList);
        adapter.setProjectList(projectList);

        listView.setAdapter(adapter);
    }

    private void setListener() {

    }
}
