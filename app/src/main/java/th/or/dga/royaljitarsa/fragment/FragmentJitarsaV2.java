package th.or.dga.royaljitarsa.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;
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
import th.or.dga.royaljitarsa.utils.Utils;

public class FragmentJitarsaV2 extends Fragment {

    private String TAG = FragmentJitarsaV2.this.getClass().getSimpleName();

    private ProgressBar progressBar;
    private LinearLayout layout;
    private TextView btnHistory;
    private TextView btnSuggest;
    private TextView btnProperty;
    private TextView btnSchool;
    private TextView btnKing;
    private TextView btnImage;
    private LinearLayout.LayoutParams params;

    private ArrayList<String> categoryIDList;
    private ArrayList<String> idList;
    //private ArrayList<String> imageCoverList;
    private ArrayList<String> nameList;
    private ArrayList<String> dateList;
    private ArrayList<String> provinceList;
    private ArrayList<String> placeList;
    private ArrayList<String> likeList;
    private ArrayList<String> shortDescriptionList;
    private ArrayList<String> publicLinkList;

    private HashMap<String, ArrayList<String>> imageCoverMap;
    private HashMap<String, ArrayList<String>> imageMap;
    private HashMap<String, ArrayList<String>> descriptionMap;
    private HashMap<String, ArrayList<String>> youtubeMap;
    private HashMap<String, ArrayList<String>> typeMap;
    private HashMap<String, ArrayList<Integer>> typeIDMap;

    private ProjectAPI projectAPI;
    private String categoryID = MyConfiguration.CATEGORY_JITARSA_ID;
    private String keyword = "";
    private String date = "";

    private Utils utils;
    private int dpTopx4 = 0;
    private int dpTopx16 = 0;

    public static FragmentJitarsaV2 newInstance() {
        FragmentJitarsaV2 fragment = new FragmentJitarsaV2();
        return fragment;
    }

    private View rootView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        if(rootView == null) {
            rootView = inflater.inflate(R.layout.jitarsa, container, false);

            addLog();
            initValue();
            initUI(rootView);
            setUI();
            setListener();

            callProjectAPI(keyword, date);
        }

        return rootView;
    }

    private void addLog() {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        FirebaseLogTracking firebaseLogTracking = new FirebaseLogTracking(mFirebaseAnalytics);
        firebaseLogTracking.addLogActivity(getText(R.string.log_param_jitarsa).toString());
    }

    private void initValue() {
        utils = new Utils(getActivity());
        dpTopx4 = utils.convertDpToPx(4);
        dpTopx16 = utils.convertDpToPx(16);

        categoryIDList = new ArrayList<>();
        idList = new ArrayList<>();
        //imageCoverList = new ArrayList<>();
        nameList = new ArrayList<>();
        dateList = new ArrayList<>();
        provinceList = new ArrayList<>();
        placeList = new ArrayList<>();
        likeList = new ArrayList<>();
        shortDescriptionList = new ArrayList<>();
        publicLinkList = new ArrayList<>();

        imageCoverMap = new HashMap<>();
        imageMap = new HashMap<>();
        descriptionMap = new HashMap<>();
        youtubeMap = new HashMap<>();
        typeIDMap = new HashMap<>();
        typeMap = new HashMap<>();
    }

    private void initUI(View rootView) {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        layout = (LinearLayout) rootView.findViewById(R.id.layout);
        /*btnHistory = (TextView) rootView.findViewById(R.id.btnHistory);
        btnSuggest = (TextView) rootView.findViewById(R.id.btnSuggest);
        btnProperty = (TextView) rootView.findViewById(R.id.btnProperty);
        btnSchool = (TextView) rootView.findViewById(R.id.btnSchool);
        btnKing = (TextView) rootView.findViewById(R.id.btnKing);
        btnImage = (TextView) rootView.findViewById(R.id.btnImage);*/

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(dpTopx4, dpTopx4, dpTopx4, dpTopx4);
    }

    private void setUI() {

    }

    private void setListener() {

        /*btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnSuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnKing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    private void callProjectAPI(String keyword, String date) {
        projectAPI = new ProjectAPI();
        projectAPI.setCategoryID(categoryID);
        projectAPI.setUserID(AppPreference.getInstance(getActivity().getApplicationContext()).getUserID());
        projectAPI.setLimit(MyConfiguration.PROJECT_LIMIT_PER_PAGE);
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
                        clearData();
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
                            publicLinkList.add(jArrayContent.optJSONObject(x).optString("public_link"));

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
                createButton();
            }
        });
        projectAPI.execute("");
    }

    private void clearData() {
        categoryIDList.clear();
        idList.clear();
        //imageCoverList.clear();
        nameList.clear();
        dateList.clear();
        provinceList.clear();
        placeList.clear();
        likeList.clear();
        shortDescriptionList.clear();
        publicLinkList.clear();

        imageCoverMap.clear();
        imageMap.clear();
        descriptionMap.clear();
        youtubeMap.clear();
        typeIDMap.clear();
        typeMap.clear();
    }

    private String getDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String date = year + "-" + month;
        return date;
    }

    private void createButton() {
        for(int x=0; x<nameList.size(); x++) {
            SukhumvitTextView btn = new SukhumvitTextView(getActivity());
            btn.setLayoutParams(params);
            btn.setPadding(dpTopx16, dpTopx16, dpTopx16, dpTopx16);
            btn.setGravity(Gravity.CENTER);
            btn.setTextColor(Color.WHITE);
            btn.setText(nameList.get(x));
            btn.setTag(x);
            btn.setBackgroundResource(R.mipmap.bg_menu_impress);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", getText(R.string.fragment_jitarsa_txt_title).toString());
                    bundle.putString("name", nameList.get((int)v.getTag()));
                    bundle.putString("date", dateList.get((int)v.getTag()));
                    bundle.putString("like", likeList.get((int)v.getTag()));
                    bundle.putString("publicLink", publicLinkList.get((int)v.getTag()));
                    bundle.putStringArrayList("imageList", imageMap.get(idList.get((int)v.getTag())));
                    bundle.putStringArrayList("youtubeList", youtubeMap.get(idList.get((int)v.getTag())));
                    bundle.putStringArrayList("descriptionList", descriptionMap.get(idList.get((int)v.getTag())));
                    bundle.putIntegerArrayList("typeIDList", typeIDMap.get(idList.get((int)v.getTag())));
                    bundle.putStringArrayList("typeList", typeMap.get(idList.get((int)v.getTag())));
                    //displayFragment(FragmentSearch.newInstance(bundle));

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, FragmentActivityDetail.newInstance(bundle));
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
            layout.addView(btn);
        }
    }
}
