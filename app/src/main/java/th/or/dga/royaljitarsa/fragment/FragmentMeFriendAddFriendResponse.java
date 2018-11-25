package th.or.dga.royaljitarsa.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.adapter.FragmentMeFriendAddFriendResponseListAdapter;
import th.or.dga.royaljitarsa.connection.GetFriendRequestAPI;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;
import th.or.dga.royaljitarsa.utils.AppPreference;

public class FragmentMeFriendAddFriendResponse extends Fragment {

    private String TAG = FragmentMeFriendAddFriendResponse.this.getClass().getSimpleName();

    private ProgressBar progressBar;
    private SukhumvitTextView txtInviteFriend;
    private ListView listView;
    private FragmentMeFriendAddFriendResponseListAdapter adapter;

    private AppPreference appPreference;
    private ArrayList<String> imageList;
    private ArrayList<String> nameList;
    private ArrayList<String> dateList;
    private ArrayList<String> statusList;

    public static FragmentMeFriendAddFriendResponse newInstance() {
        FragmentMeFriendAddFriendResponse fragment = new FragmentMeFriendAddFriendResponse();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_me_friend_add_friend_invite, container, false);

        initValue();
        initUI(rootView);
        setUI();
        setListener();
        getFriendRequest();

        return rootView;
    }

    private void initValue() {
        appPreference = new AppPreference(getActivity());
        imageList = new ArrayList<>();
        nameList = new ArrayList<>();
        dateList = new ArrayList<>();
        statusList = new ArrayList<>();

        /*for(int x=0; x<10; x++) {
            imageList.add("x: " + x);
            nameList.add("x: " + x);
            statusList.add("x: " + x);
        }*/
    }

    private void initUI(View rootView) {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        txtInviteFriend = (SukhumvitTextView) rootView.findViewById(R.id.txtInviteFriend);
        listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new FragmentMeFriendAddFriendResponseListAdapter(getActivity());
    }

    private void setUI() {
        txtInviteFriend.setText(getText(R.string.fragment_me_friend_add_friend_response_txt_title));
        adapter.setImageList(imageList);
        adapter.setStatusList(statusList);
        adapter.setNameList(nameList);
        adapter.setDateList(dateList);
        listView.setAdapter(adapter);
    }

    private void setListener() {

    }

    private void getFriendRequest() {
        GetFriendRequestAPI getFriendRequestAPI = new GetFriendRequestAPI();
        getFriendRequestAPI.setUserID(appPreference.getUserID());
        getFriendRequestAPI.setListener(new GetFriendRequestAPI.GetFriendRequestAPIListener() {
            @Override
            public void onGetFriendRequestAPIPreExecuteConcluded() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onGetFriendRequestAPIPostExecuteConcluded(String result) {
                setFriendRequest(result);
                progressBar.setVisibility(View.GONE);
            }
        });
        getFriendRequestAPI.execute("");
    }

    private void setFriendRequest(String result) {
        try {
            JSONObject jObj = new JSONObject(result);
            int status = jObj.optInt("status");
            String statusDetail = jObj.optString("status_detail");

            Log.d(TAG, "statusDetail: " + statusDetail);
            if(status == 200) {
                JSONArray jArrayFriend = jObj.optJSONArray("friend");
                for(int x=0; x<jArrayFriend.length(); x++) {
                    Log.d(TAG, "x: " + x);
                    //nameList.add(jArrayFriend.optJSONObject(x).optString("friend_name"));
                    //ageList.add(jArrayFriend.optJSONObject(x).optString("friend_id"));
                }

                adapter.setImageList(imageList);
                adapter.setStatusList(statusList);
                adapter.setNameList(nameList);
                adapter.notifyDataSetChanged();
                listView.invalidateViews();
            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
