package th.or.dga.royaljitarsa.fragment;

import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
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
import th.or.dga.royaljitarsa.customview.SukhumvitZoomableV2TextView;
import th.or.dga.royaljitarsa.utils.AppJavaScriptProxy;
import th.or.dga.royaljitarsa.utils.MyConfiguration;
import th.or.dga.royaljitarsa.utils.WebViewClientImpl;

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
    private LinearLayout.LayoutParams params;

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

    private int margin8dp = 0;

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
        Resources r = getActivity().getResources();
        margin8dp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8,
                r.getDisplayMetrics()
        );

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

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(margin8dp, margin8dp, margin8dp, margin8dp);
    }

    private void setUI() {
        txtTitle.setText(title);
        txtTitle.setVisibility(View.GONE);

        txtName.setText(name);
        txtDate.setText(date);

        txtName.setTypeface(Typeface.BOLD);

        for(int y=0; y<typeIDList.size(); y++) {
            if(typeIDList.get(y) == 1) {
                ImageView imageView = new ImageView(getActivity());
                imageView.setLayoutParams(params);
                imageView.setAdjustViewBounds(true);
                Glide.with(getActivity())
                        .load(imageList.get(y))
                        .apply(fitCenterTransform()
                                //.placeholder(R.mipmap.ic_launcher)
                                //.error(R.mipmap.ic_launcher)
                                .priority(Priority.HIGH))
                        .into(imageView);
                layoutContent.addView(imageView);
                Log.d(TAG, "layoutContent.addView(imageView);");
            } else if(typeIDList.get(y) == 2) {
                /*VideoView videoView = new VideoView(getActivity());
                videoView.setLayoutParams(params);
                layoutContent.addView(videoView);
                Log.d(TAG, "layoutContent.addView(videoView);");*/

                setYoutubePlayer(youtubeList.get(y));
            } else if(typeIDList.get(y) == 3) {
                //SukhumvitTextView textView = new SukhumvitTextView(getActivity());
                SukhumvitZoomableV2TextView textView = new SukhumvitZoomableV2TextView(getActivity());
                textView.setLayoutParams(params);
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

    private void setYoutubePlayer(String urlYoutube) {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int heightPixels = metrics.heightPixels;
        float widthPixels = metrics.widthPixels;
        int densityDpi = metrics.densityDpi;
        float xdpi = metrics.xdpi;
        float ydpi = metrics.ydpi;
        Log.d(TAG, "width: " + widthPixels);

        float height = widthPixels/16;
        Log.d(TAG, "height: " + height);

        WebView webView = new WebView(getActivity());
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)height*9);
        webView.setLayoutParams(param);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        WebViewClientImpl webViewClient = new WebViewClientImpl(getActivity());
        webView.setWebViewClient(webViewClient);
        //webView.setWebViewClient(new Browser());
        //webView.setWebChromeClient(new MyWebClient());

        webView.addJavascriptInterface(new AppJavaScriptProxy(getActivity(), webView), "androidAppProxy");
        //webView.loadUrl(url);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("fromAndroid()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    //store / process result received from executing Javascript.
                }
            });
        }

        webView.loadUrl(MyConfiguration.YOUTUBE_PREFIX + urlYoutube + MyConfiguration.YOUTUBE_SUFFIX);
        webView.setWebChromeClient(new WebChromeClient());
        layoutContent.addView(webView);
    }
}
