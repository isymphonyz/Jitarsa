package th.or.dga.royaljitarsa.fragment;

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
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.util.ArrayList;
import java.util.Calendar;

import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.connection.ProjectAPI;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;
import th.or.dga.royaljitarsa.utils.MyConfiguration;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

public class FragmentHomeDetail extends Fragment {

    private String TAG = FragmentHomeDetail.this.getClass().getSimpleName();

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
    private ArrayList<Integer> typeIDList;
    private ArrayList<String> typeList;

    private String title = "";
    private String image = "";
    private String name = "";
    private String date = "";
    private String description = "";
    private String like = "";
    
    private ProjectAPI projectAPI;
    private String categoryID = MyConfiguration.CATEGORY_JITARSA_ID;

    public static FragmentHomeDetail newInstance() {
        FragmentHomeDetail fragment = new FragmentHomeDetail();
        return fragment;
    }

    public static FragmentHomeDetail newInstance(Bundle bundle) {
        FragmentHomeDetail fragment = new FragmentHomeDetail();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_jitarsa, container, false);

        initValue();
        initUI(rootView);
        setUI();
        setListener();

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

        title = getArguments().getString("title");
        name = getArguments().getString("name");
        date = getArguments().getString("date");
        like = getArguments().getString("like");

        typeIDList = getArguments().getIntegerArrayList("typeIDList");
        typeList = getArguments().getStringArrayList("typeList");
        imageList = getArguments().getStringArrayList("imageList");
        youtubeList = getArguments().getStringArrayList("youtubeList");
        descriptionList = getArguments().getStringArrayList("descriptionList");
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
        txtTitle.setText(title);
        txtTitle.setVisibility(View.GONE);

        txtName.setText(name);
        txtDate.setText(date);

        for(int y=0; y<typeIDList.size(); y++) {
            if(typeIDList.get(y) == 1) {
                ImageView imageView = new ImageView(getActivity());
                imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                imageView.setAdjustViewBounds(true);
                Glide.with(getActivity())
                        .load(imageList.get(y))
                        .apply(fitCenterTransform()
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .error(R.drawable.ic_launcher_background)
                                .priority(Priority.HIGH))
                        .into(imageView);
                layoutContent.addView(imageView);
                Log.d(TAG, "layoutContent.addView(imageView);");
            } else if(typeIDList.get(y) == 2) {
                VideoView videoView = new VideoView(getActivity());
                videoView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                layoutContent.addView(videoView);
                Log.d(TAG, "layoutContent.addView(videoView);");
            } else if(typeIDList.get(y) == 3) {
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

    private void setListener() {

    }

    private String getDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String date = year + "-" + month;
        return date;
    }
}
