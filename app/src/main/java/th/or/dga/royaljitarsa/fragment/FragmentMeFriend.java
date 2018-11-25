package th.or.dga.royaljitarsa.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import th.or.dga.royaljitarsa.Home;
import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.adapter.FragmentMeFriendListAdapter;
import th.or.dga.royaljitarsa.connection.GetFriendAPI;
import th.or.dga.royaljitarsa.utils.AppPreference;

public class FragmentMeFriend extends Fragment {

    private String TAG = FragmentMeFriend.this.getClass().getSimpleName();

    private ProgressBar progressBar;
    private LinearLayout layoutAddFriend;
    private ListView listView;
    private FragmentMeFriendListAdapter adapter;

    private AppPreference appPreference;
    private ArrayList<String> imageList;
    private ArrayList<String> nameList;
    private ArrayList<String> ageList;

    public static FragmentMeFriend newInstance() {
        FragmentMeFriend fragment = new FragmentMeFriend();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_me_friend, container, false);

        initUI(rootView);
        initValue();
        setUI();
        setListener();

        return rootView;
    }

    private void initValue() {
        appPreference = new AppPreference(getActivity().getApplicationContext());

        imageList = new ArrayList<>();
        nameList = new ArrayList<>();
        ageList = new ArrayList<>();

        for(int x=0; x<10; x++) {
            imageList.add("Image: " + x);
            //nameList.add("Name: " + x);
            //ageList.add("Age: " + x);
        }

        getFriend();
    }

    private void initUI(View rootView) {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        layoutAddFriend = (LinearLayout) rootView.findViewById(R.id.layoutAddFriend);
        listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new FragmentMeFriendListAdapter(getActivity());
    }

    private void setUI() {
        adapter.setImageList(imageList);
        adapter.setNameList(nameList);
        adapter.setAgeList(ageList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.invalidateViews();
    }

    private void setListener() {
        layoutAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Home.getInstance(getActivity()).displayFragment(transaction, FragmentMeFriendAddFriend.newInstance());
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private void getFriend() {
        GetFriendAPI getFriendAPI = new GetFriendAPI();
        getFriendAPI.setUserID(appPreference.getUserID());
        getFriendAPI.setListener(new GetFriendAPI.GetFriendAPIListener() {
            @Override
            public void onGetFriendAPIPreExecuteConcluded() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onGetFriendAPIPostExecuteConcluded(String result) {
                setFriend(result);
                progressBar.setVisibility(View.GONE);
            }
        });
        getFriendAPI.execute("");
    }

    private void setFriend(String result) {
        try {
            JSONObject jObj = new JSONObject(result);
            int status = jObj.optInt("status");
            String statusDetail = jObj.optString("status_detail");

            Log.d(TAG, "statusDetail: " + statusDetail);
            if(status == 200) {
                JSONArray jArrayFriend = jObj.optJSONArray("friend");
                for(int x=0; x<jArrayFriend.length(); x++) {
                    Log.d(TAG, "x: " + x);
                    nameList.add(jArrayFriend.optJSONObject(x).optString("friend_name"));
                    ageList.add(jArrayFriend.optJSONObject(x).optString("friend_id"));
                }

                adapter.setNameList(nameList);
                adapter.setAgeList(ageList);
                adapter.notifyDataSetChanged();
                listView.invalidateViews();
            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
