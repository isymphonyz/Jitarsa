package th.or.dga.royaljitarsa.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.adapter.FragmentJitarsaListAdapter;
import th.or.dga.royaljitarsa.connection.ProjectAPI;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;
import th.or.dga.royaljitarsa.utils.AppPreference;
import th.or.dga.royaljitarsa.utils.FirebaseLogTracking;
import th.or.dga.royaljitarsa.utils.MyConfiguration;

public class FragmentJitarsa extends Fragment {

    private String TAG = FragmentJitarsa.this.getClass().getSimpleName();

    private ProgressBar progressBar;
    private SukhumvitTextView txtTitle;
    private ListView listView;
    private FragmentJitarsaListAdapter adapter;
    private ImageView imgUp;
    private ImageView imgDown;

    private ArrayList<String> categoryIDList;
    private ArrayList<String> idList;
    //private ArrayList<String> imageCoverList;
    private ArrayList<String> nameList;
    private ArrayList<String> dateList;
    private ArrayList<String> provinceList;
    private ArrayList<String> placeList;
    private ArrayList<String> likeList;
    private ArrayList<String> shortDescriptionList;

    private HashMap<String, ArrayList<String>> imageCoverMap;
    private HashMap<String, ArrayList<String>> imageMap;
    private HashMap<String, ArrayList<String>> descriptionMap;
    private HashMap<String, ArrayList<String>> youtubeMap;
    private HashMap<String, ArrayList<String>> typeMap;
    private HashMap<String, ArrayList<Integer>> typeIDMap;

    private ProjectAPI projectAPI;
    private String categoryID = MyConfiguration.CATEGORY_JITARSA_ID;

    public static FragmentJitarsa newInstance() {
        FragmentJitarsa fragment = new FragmentJitarsa();
        return fragment;
    }

    private View rootView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_news, container, false);

            addLog();
            initValue();
            initUI(rootView);
            setUI();
            setListener();

            callProjectAPI();
        }

        return rootView;
    }

    private void addLog() {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        FirebaseLogTracking firebaseLogTracking = new FirebaseLogTracking(mFirebaseAnalytics);
        firebaseLogTracking.addLogActivity(getText(R.string.log_param_jitarsa).toString());
    }

    private void initValue() {
        categoryIDList = new ArrayList<>();
        idList = new ArrayList<>();
        //imageCoverList = new ArrayList<>();
        nameList = new ArrayList<>();
        dateList = new ArrayList<>();
        provinceList = new ArrayList<>();
        placeList = new ArrayList<>();
        likeList = new ArrayList<>();
        shortDescriptionList = new ArrayList<>();

        imageCoverMap = new HashMap<>();
        imageMap = new HashMap<>();
        descriptionMap = new HashMap<>();
        youtubeMap = new HashMap<>();
        typeIDMap = new HashMap<>();
        typeMap = new HashMap<>();
    }

    private void initUI(View rootView) {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        txtTitle = (SukhumvitTextView) rootView.findViewById(R.id.txtTitle);
        listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new FragmentJitarsaListAdapter(getActivity());
        imgUp = (ImageView) rootView.findViewById(R.id.imgUp);
        imgDown = (ImageView) rootView.findViewById(R.id.imgDown);
    }

    private void setUI() {
        txtTitle.setText(getText(R.string.fragment_jitarsa_txt_title));

        adapter.setFragment(this);
        adapter.setCategoryIDList(categoryIDList);
        adapter.setIDList(idList);
        adapter.setImageMap(imageMap);
        adapter.setImageCoverMap(imageCoverMap);
        //adapter.setImageList(imageCoverList);
        adapter.setNameList(nameList);
        adapter.setDateList(dateList);
        adapter.setDescriptionList(shortDescriptionList);
        adapter.setLikeList(likeList);

        listView.setAdapter(adapter);
    }

    private void setListener() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(totalItemCount != listView.getLastVisiblePosition() + 1){
                    imgUp.setVisibility(View.GONE);
                    imgDown.setVisibility(View.VISIBLE);
                }else{
                    imgUp.setVisibility(View.VISIBLE);
                    imgDown.setVisibility(View.GONE);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "listView.setOnItemClickListener: " + i);
                Bundle bundle = new Bundle();
                bundle.putString("title", getText(R.string.fragment_jitarsa_txt_title).toString());
                bundle.putString("name", nameList.get(i));
                bundle.putString("date", dateList.get(i));
                bundle.putString("like", likeList.get(i));
                bundle.putStringArrayList("imageList", imageMap.get(idList.get(i)));
                bundle.putStringArrayList("youtubeList", youtubeMap.get(idList.get(i)));
                bundle.putStringArrayList("descriptionList", descriptionMap.get(idList.get(i)));
                bundle.putIntegerArrayList("typeIDList", typeIDMap.get(idList.get(i)));
                bundle.putStringArrayList("typeList", typeMap.get(idList.get(i)));
                //displayFragment(FragmentSearch.newInstance(bundle));

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, FragmentActivityDetail.newInstance(bundle));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "listView.setOnItemSelectedListener: " + i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void callProjectAPI() {
        projectAPI = new ProjectAPI();
        projectAPI.setCategoryID(categoryID);
        projectAPI.setUserID(AppPreference.getInstance(getActivity().getApplicationContext()).getUserID());
        projectAPI.setLimit("10");
        projectAPI.setOffset("0");
        projectAPI.setDate(getDate());
        projectAPI.setListener(new ProjectAPI.ProjectAPIListener() {
            @Override
            public void onProjectAPIPreExecuteConcluded() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onProjectAPIPostExecuteConcluded(String result) {
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jObj = new JSONObject(result);
                    int status = jObj.optInt("status");
                    String statusDetail = jObj.optString("status_detail");

                    Log.d(TAG, "statusDetail: " + statusDetail);
                    if(status == 200) {
                        JSONArray jArrayContent = jObj.optJSONArray("content");
                        for(int x=0; x<jArrayContent.length(); x++) {
                            categoryIDList.add("" + jArrayContent.optJSONObject(x).optInt("category_id"));
                            idList.add("" + jArrayContent.optJSONObject(x).optInt("id"));
                            nameList.add(jArrayContent.optJSONObject(x).optString("title"));
                            dateList.add(jArrayContent.optJSONObject(x).optString("project_date"));
                            provinceList.add(jArrayContent.optJSONObject(x).optJSONArray("province").optJSONObject(0).optString("province_name"));
                            placeList.add(jArrayContent.optJSONObject(x).optString("place"));
                            likeList.add(jArrayContent.optJSONObject(x).optString("like_count"));
                            shortDescriptionList.add(jArrayContent.optJSONObject(x).optString("short_description"));

                            ArrayList<String> imageCoverList = new ArrayList<>();
                            JSONArray jArrayImageCover = jArrayContent.optJSONObject(x).optJSONArray("image_cover");
                            for(int y=0; y<jArrayImageCover.length(); y++) {
                                imageCoverList.add(jArrayImageCover.optJSONObject(y).optJSONObject("lg").optString("source"));
                            }
                            imageCoverMap.put(idList.get(x), imageCoverList);

                            ArrayList<Integer> typeIDList = new ArrayList<>();
                            ArrayList<String> typeList = new ArrayList<>();
                            ArrayList<String> imageList = new ArrayList<>();
                            ArrayList<String> youtubeList = new ArrayList<>();
                            ArrayList<String> descriptionList = new ArrayList<>();
                            JSONArray jArrayDetail = jArrayContent.optJSONObject(x).optJSONArray("detail");
                            for(int z=0; z<jArrayDetail.length(); z++) {
                                typeIDList.add(jArrayDetail.optJSONObject(z).optInt("type_id"));
                                typeList.add(jArrayDetail.optJSONObject(z).optString("type"));
                                try {
                                    //imageList.add(jArrayDetail.optJSONObject(z).optJSONArray("image").optJSONObject(0).optJSONObject("lg").optString("source"));
                                    imageList.add(jArrayDetail.optJSONObject(z).optJSONObject("image").optJSONObject("lg").optString("source"));
                                } catch(Exception e) {
                                    imageList.add("");
                                }
                                youtubeList.add(jArrayDetail.optJSONObject(z).optString("youtube"));
                                descriptionList.add(jArrayDetail.optJSONObject(z).optString("discription"));
                            }
                            typeIDMap.put(idList.get(x), typeIDList);
                            typeMap.put(idList.get(x), typeList);
                            imageMap.put(idList.get(x), imageList);
                            youtubeMap.put(idList.get(x), youtubeList);
                            descriptionMap.put(idList.get(x), descriptionList);
                        }
                    } else {
                        Toast.makeText(getActivity(), statusDetail, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        });
        projectAPI.execute("");
    }

    private String getDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String date = year + "-" + month;
        return date;
    }

    public void goToDetail(int i) {
        listView.performItemClick(
                listView.getAdapter().getView(i, null, null),
                i,
                listView.getAdapter().getItemId(i));
    }
}
