package th.or.dga.royaljitarsa.fragment;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.connection.AboutAPI;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;
import th.or.dga.royaljitarsa.customview.SukhumvitZoomableTextView;
import th.or.dga.royaljitarsa.customview.SukhumvitZoomableV2TextView;
import th.or.dga.royaljitarsa.utils.FirebaseLogTracking;
import th.or.dga.royaljitarsa.utils.MyConfiguration;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

public class FragmentAbout extends Fragment {

    private String TAG = FragmentAbout.this.getClass().getSimpleName();

    private LinearLayout layout;
    private LinearLayout.LayoutParams layoutParams;
    private LinearLayout.LayoutParams layoutParamsWebView;
    private ProgressBar progressBar;

    private String urlAbout = MyConfiguration.URL_ABOUT;

    private AboutAPI aboutAPI;
    private AboutAPI.AboutAPIListener aboutAPIListener;

    private String youtubeVideoID = "";

    final static float STEP = 200;
    float mRatio = 1.0f;
    int mBaseDist;
    float mBaseRatio;
    float fontsize = 13;

    private int margin8dp;

    public static FragmentAbout newInstance() {
        FragmentAbout fragment = new FragmentAbout();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        addLog();
        initUI(rootView);
        setUI();
        getAbout();

        return rootView;
    }

    private void addLog() {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        FirebaseLogTracking firebaseLogTracking = new FirebaseLogTracking(mFirebaseAnalytics);
        firebaseLogTracking.addLogActivity(getText(R.string.log_param_about).toString());
    }

    private void initUI(View rootView) {
        Resources r = getActivity().getResources();
        margin8dp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8,
                r.getDisplayMetrics()
        );

        layout = (LinearLayout) rootView.findViewById(R.id.layout);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(margin8dp, margin8dp, margin8dp, margin8dp);

        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics());
        layoutParamsWebView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
    }

    private void setUI() {

    }

    private void getAbout() {
        aboutAPI = new AboutAPI();
        aboutAPI.setListener(new AboutAPI.AboutAPIListener() {
            @Override
            public void onAboutAPIPreExecuteConcluded() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAboutAPIPostExecuteConcluded(String result) {
                progressBar.setVisibility(View.GONE);
                setAbout(result);
            }
        });
        aboutAPI.execute("");
    }

    private void setAbout(String result) {
        try {
            Log.d(TAG, "result: " + result);
            JSONObject jObj = new JSONObject(result);
            int status = jObj.optInt("status");
            String statusDetail = jObj.optString("status_detail");

            Log.d(TAG, "statusDetail: " + statusDetail);
            if(status == 200) {
                //Toast.makeText(getActivity(), statusDetail, Toast.LENGTH_SHORT).show();
                JSONArray jArrayContent = jObj.optJSONArray("content");
                JSONArray jArrayDetail = jArrayContent.optJSONObject(0).optJSONArray("detail");

                for(int x=0; x<jArrayDetail.length(); x++) {
                    String type = jArrayDetail.optJSONObject(x).optString("type");
                    if(type.toLowerCase().equals("image")) {
                        /*JSONArray jArrayImage = jArrayDetail.optJSONObject(x).optJSONArray("image");
                        for(int y=0; y<jArrayImage.length(); y++) {
                            insertImageView(jArrayImage.optJSONObject(y).optJSONObject("lg").optString("source"));
                        }*/
                        JSONObject jObjImage = jArrayDetail.optJSONObject(x).optJSONObject("image");
                        insertImageView(jObjImage.optJSONObject("lg").optString("source"));
                    } else if(type.toLowerCase().equals("youtube")) {
                        insertVideoView(jArrayDetail.optJSONObject(x).optString("youtube"));
                    } else {
                        insertTextView(jArrayDetail.optJSONObject(x).optString("discription"));
                    }
                }
            } else {
                Toast.makeText(getActivity(), statusDetail, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void insertImageView(String url) {
        //ScaleImageView imageView = new ScaleImageView(getActivity());
        ImageView imageView = new ImageView(getActivity());
        imageView.setLayoutParams(layoutParams);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Glide.with(getActivity())
                .load(url)
                .apply(fitCenterTransform()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_background)
                        .priority(Priority.HIGH))
                .into(imageView);
        layout.addView(imageView);
    }

    private void insertVideoView(String url) {
        /*final VideoView videoView = new VideoView(getActivity());
        videoView.setLayoutParams(layoutParams);
        videoView.setVideoPath(url);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                videoView.stopPlayback();
                return false;
            }
        });
        layout.addView(videoView);*/
        
        /*YouTubePlayerView youTubePlayerView = new YouTubePlayerView(getActivity());
        youTubePlayerView.initialize(getActivity().getText(R.string.google_maps_key).toString(), this);
        youTubePlayerView.setLayoutParams(layoutParams);
        layout.addView(youTubePlayerView);*/
        
        final WebView webView = new WebView(getActivity());
        webView.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.bg_youtube_01, null));
        webView.setLayoutParams(layoutParamsWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d(TAG, "webView onTouch");
                webView.loadUrl("http://www.youtube.com/embed/" + youtubeVideoID + "?autoplay=1&vq=small");
                return false;
            }
        });
        layout.addView(webView);
    }

    //SukhumvitZoomableV2TextView textView;
    private void insertTextView(String text) {
        SukhumvitZoomableV2TextView textView = new SukhumvitZoomableV2TextView(getActivity());
        //textView = new SukhumvitZoomableV2TextView(getActivity());
        textView.setLayoutParams(layoutParams);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(text));
        }
        layout.addView(textView);
    }
}
