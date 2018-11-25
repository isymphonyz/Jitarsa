package th.or.dga.royaljitarsa.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.adapter.FragmentMeImageCreateListAdapter;
import th.or.dga.royaljitarsa.connection.GetAlbumAPI;
import th.or.dga.royaljitarsa.utils.AppPreference;

public class FragmentMeImageCreate extends Fragment {

    private String TAG = FragmentMeImageCreate.this.getClass().getSimpleName();

    private ProgressBar progressBar;
    private LinearLayout layoutCreateAlbum;
    private GridView gridView;
    private FragmentMeImageCreateListAdapter adapter;

    private AppPreference appPreference;
    private ArrayList<String> imageList;
    private ArrayList<Boolean> selectStatusList;
    private ArrayList<String> nameList;

    public static FragmentMeImageCreate newInstance() {
        FragmentMeImageCreate fragment = new FragmentMeImageCreate();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_me_image_create, container, false);

        initUI(rootView);
        initValue();
        setUI();
        setListener();
        getImageFromDevice();

        return rootView;
    }

    private void initValue() {
        appPreference = new AppPreference(getActivity().getApplicationContext());
        imageList = new ArrayList<>();
        selectStatusList = new ArrayList<>();
        nameList = new ArrayList<>();

        for(int x=0; x<10; x++) {
            //imageList.add("Image: " + x);
            //nameList.add("Name: " + x);
            //selectStatusList.add(false);
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //stuff that updates ui
                //getAlbum();
            }
        });
    }

    private void initUI(View rootView) {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        layoutCreateAlbum = (LinearLayout) rootView.findViewById(R.id.layoutCreateAlbum);
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        adapter = new FragmentMeImageCreateListAdapter(getActivity());
    }

    private void setUI() {
        adapter.setImageList(imageList);
        adapter.setNameList(nameList);
        adapter.setSelectStatusList(selectStatusList);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        gridView.invalidateViews();
    }

    private void setListener() {
        layoutCreateAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentMe.newInstance().displayFragment(FragmentMeFriendAddFriend.newInstance());
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: " + i);
                selectStatusList.set(i, !selectStatusList.get(i));
                adapter.notifyDataSetChanged();
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
                    //numberList.add("" + jArrayAlbum.optJSONObject(x).optJSONArray("data_image").length());
                }

                adapter.setNameList(nameList);
                adapter.setImageList(imageList);
                adapter.notifyDataSetChanged();
                gridView.invalidateViews();
            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getImageFromDevice() {
        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media._ID;
        //Stores all the images from the gallery in Cursor
        Cursor cursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        //Total number of images
        int count = cursor.getCount();

        //Create an array to store path to all the images
        //imagePath = new String[count];

        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            //Store the path of the image
            //arrPath[i]= cursor.getString(dataColumnIndex);

            String path = cursor.getString(dataColumnIndex);
            imageList.add(path);
            nameList.add(path.split("/")[path.split("/").length-1]);
            selectStatusList.add(false);
            Log.d(TAG, (i+1) + ": " + nameList.get(i));
        }
        adapter.notifyDataSetChanged();
    }

}
