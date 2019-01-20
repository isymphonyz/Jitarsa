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

import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.adapter.FragmentMeImageListAdapter;
import th.or.dga.royaljitarsa.connection.GetAlbumAPI;
import th.or.dga.royaljitarsa.utils.AppPreference;

public class FragmentMeImage extends Fragment {

    private String TAG = FragmentMeImage.this.getClass().getSimpleName();

    private ProgressBar progressBar;
    private LinearLayout layoutCreateAlbum;
    private ListView listView;
    private FragmentMeImageListAdapter adapter;

    private AppPreference appPreference;
    private ArrayList<String> imageList;
    private ArrayList<String> numberList;
    private ArrayList<String> nameList;

    public static FragmentMeImage newInstance() {
        FragmentMeImage fragment = new FragmentMeImage();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_me_image, container, false);

        initUI(rootView);
        initValue();
        setUI();
        setListener();

        return rootView;
    }

    private void initValue() {
        appPreference = new AppPreference(getActivity().getApplicationContext());
        imageList = new ArrayList<>();
        numberList = new ArrayList<>();
        nameList = new ArrayList<>();

        for(int x=0; x<10; x++) {
            //imageList.add("Image: " + x);
            //numberList.add("Number: " + x);
            //nameList.add("Name: " + x);
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //stuff that updates ui
                getAlbum();
            }
        });
    }

    private void initUI(View rootView) {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        layoutCreateAlbum = (LinearLayout) rootView.findViewById(R.id.layoutCreateAlbum);
        listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new FragmentMeImageListAdapter(getActivity());
    }

    private void setUI() {
        adapter.setImageList(imageList);
        adapter.setNumberList(numberList);
        adapter.setNameList(nameList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.invalidateViews();
    }

    private void setListener() {
        layoutCreateAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentMe.newInstance().displayFragment(transaction, FragmentMeImageCreate.newInstance());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
            }
        });
    }

    private void getAlbum() {
        GetAlbumAPI getAlbumAPI = new GetAlbumAPI();
        getAlbumAPI.setUserID(appPreference.getUserID());
        getAlbumAPI.setListener(new GetAlbumAPI.GetAlbumAPIListener() {
            @Override
            public void onGetAlbumAPIPreExecuteConcluded() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onGetAlbumAPIPostExecuteConcluded(final String result) {
                setAlbum(result);
                progressBar.setVisibility(View.GONE);
            }
        });
        getAlbumAPI.execute("");
    }

    private void setAlbum(String result) {
        try {
            JSONObject jObj = new JSONObject(result);
            int status = jObj.optInt("status");
            String statusDetail = jObj.optString("status_detail");

            Log.d(TAG, "statusDetail: " + statusDetail);
            if(status == 200) {
                JSONArray jArrayAlbum = jObj.optJSONArray("album");
                for(int x=0; x<jArrayAlbum.length(); x++) {
                    Log.d(TAG, "x: " + x);
                    nameList.add(jArrayAlbum.optJSONObject(x).optString("album_name"));
                    imageList.add(jArrayAlbum.optJSONObject(x).optJSONArray("data_image").optJSONObject(0).optString("image"));
                    numberList.add("" + jArrayAlbum.optJSONObject(x).optJSONArray("data_image").length());
                }

                adapter.setNameList(nameList);
                adapter.setImageList(imageList);
                adapter.setNumberList(numberList);
                adapter.notifyDataSetChanged();
                listView.invalidateViews();
            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
