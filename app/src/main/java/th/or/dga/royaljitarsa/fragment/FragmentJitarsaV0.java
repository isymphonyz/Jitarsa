package th.or.dga.royaljitarsa.fragment;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.connection.ProjectAPI;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;
import th.or.dga.royaljitarsa.utils.AppPreference;
import th.or.dga.royaljitarsa.utils.MyConfiguration;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

public class FragmentJitarsaV0 extends Fragment {

    private String TAG = FragmentJitarsaV0.this.getClass().getSimpleName();

    private ProgressBar progressBar;
    private SukhumvitTextView txtTitle;
    private LinearLayout layoutContent;
    private ImageView img;
    private SukhumvitTextView txtName;
    private SukhumvitTextView txtDate;
    private SukhumvitTextView txtDescription;

    private ArrayList<String> imageList;
    private ArrayList<String> youtubeList;
    private ArrayList<String> nameList;
    private ArrayList<String> dateList;
    private ArrayList<String> descriptionList;
    private ArrayList<String> likeList;
    private ArrayList<Integer> typeList;

    private String image = "";
    private String name = "";
    private String date = "";
    private String description = "";
    private String like = "";
    
    private ProjectAPI projectAPI;
    private String categoryID = MyConfiguration.CATEGORY_JITARSA_ID;
    private String keyword = "";

    public static FragmentJitarsaV0 newInstance() {
        FragmentJitarsaV0 fragment = new FragmentJitarsaV0();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_jitarsa, container, false);

        initValue();
        initUI(rootView);
        setUI();
        setListener();
        
        callProjectAPI(keyword, date);

        return rootView;
    }

    private void initValue() {
        imageList = new ArrayList<>();
        youtubeList = new ArrayList<>();
        nameList = new ArrayList<>();
        dateList = new ArrayList<>();
        descriptionList = new ArrayList<>();
        likeList = new ArrayList<>();
        typeList = new ArrayList<>();
    }

    private void initUI(View rootView) {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        txtTitle = (SukhumvitTextView) rootView.findViewById(R.id.txtTitle);
        layoutContent = (LinearLayout) rootView.findViewById(R.id.layoutContent);
        img = (ImageView) rootView.findViewById(R.id.img);
        txtName = (SukhumvitTextView) rootView.findViewById(R.id.txtName);
        txtDate = (SukhumvitTextView) rootView.findViewById(R.id.txtDate);
        txtDescription = (SukhumvitTextView) rootView.findViewById(R.id.txtDescription);
    }

    private void setUI() {
        txtTitle.setText(getText(R.string.fragment_jitarsa_txt_title));
        txtName.setVisibility(View.GONE);
        txtDate.setVisibility(View.GONE);
    }

    private void setListener() {

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
                        JSONArray jArrayContent = jObj.optJSONArray("content");
                        for(int x=0; x<jArrayContent.length(); x++) {
                            nameList.add(jArrayContent.optJSONObject(x).optString("title"));
                            dateList.add(jArrayContent.optJSONObject(x).optString("schedule_date"));

                            //txtName.setText(nameList.get(x));
                            //txtDate.setText(dateList.get(x));
                            SukhumvitTextView txtTitle = new SukhumvitTextView(getActivity());
                            txtTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            txtTitle.setText(nameList.get(x));
                            txtTitle.setTypeface(Typeface.BOLD);
                            layoutContent.addView(txtTitle);

                            SukhumvitTextView txtSchedule = new SukhumvitTextView(getActivity());
                            txtSchedule.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            txtSchedule.setText(dateList.get(x));
                            layoutContent.addView(txtSchedule);

                            JSONArray jArrayDetail = jArrayContent.optJSONObject(x).optJSONArray("detail");
                            imageList.clear();
                            youtubeList.clear();
                            descriptionList.clear();
                            likeList.clear();
                            typeList.clear();
                            for(int y=0; y<jArrayDetail.length(); y++) {
                                imageList.add(jArrayDetail.optJSONObject(y).optString("image"));
                                youtubeList.add(jArrayDetail.optJSONObject(y).optString("youtube"));
                                descriptionList.add(jArrayDetail.optJSONObject(y).optString("discription"));
                                likeList.add("0");
                                typeList.add(jArrayDetail.optJSONObject(y).optInt("type_id"));

                                if(typeList.get(y) == 1) {
                                    ImageView imageView = new ImageView(getActivity());
                                    imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    imageView.setAdjustViewBounds(true);
                                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                    Glide.with(getActivity())
                                            .load(imageList.get(y))
                                            .apply(fitCenterTransform()
                                                    //.placeholder(R.mipmap.ic_launcher)
                                                    //.error(R.mipmap.ic_launcher)
                                                    .priority(Priority.HIGH))
                                            .into(imageView);
                                    layoutContent.addView(imageView);
                                    Log.d(TAG, "layoutContent.addView(imageView);");
                                } else if(typeList.get(y) == 2) {
                                    VideoView videoView = new VideoView(getActivity());
                                    videoView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                                    layoutContent.addView(videoView);
                                    Log.d(TAG, "layoutContent.addView(videoView);");
                                } else if(typeList.get(y) == 3) {
                                    SukhumvitTextView textView = new SukhumvitTextView(getActivity());
                                    textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    //textView.setText(descriptionList.get(y));
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        textView.setText(Html.fromHtml(descriptionList.get(y), Html.FROM_HTML_MODE_LEGACY));
                                    } else {
                                        textView.setText(Html.fromHtml(descriptionList.get(y)));
                                    }
                                    layoutContent.addView(textView);
                                    Log.d(TAG, "layoutContent.addView(textView);");
                                }
                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), statusDetail, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
}
