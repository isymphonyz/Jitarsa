package th.or.dga.royaljitarsa;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import th.or.dga.royaljitarsa.adapter.ImageSlidePagerAdapter;
import th.or.dga.royaljitarsa.adapter.ImageSlidePagerStringAdapter;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;
import th.or.dga.royaljitarsa.utils.AppPreference;
import th.or.dga.royaljitarsa.utils.FirebaseLogTracking;

public class GalleryActivity extends AppCompatActivity {

    private SukhumvitTextView txtSkip;
    private ViewPager viewPager;
    private CircleIndicator indicator;

    private ArrayList<String> imageList;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);

        addLog();
        initUI();
        initValue();
        setListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void addLog() {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseLogTracking firebaseLogTracking = new FirebaseLogTracking(mFirebaseAnalytics);
        firebaseLogTracking.addLogActivity(getText(R.string.log_param_gallery).toString());
    }

    private void initValue() {
        /*imageList = new ArrayList<>();
        imageList.add("https://www.matichonweekly.com/wp-content/uploads/2017/07/%E0%B8%88%E0%B8%B4%E0%B8%95%E0%B8%AD%E0%B8%B2%E0%B8%AA%E0%B8%B23.jpg");
        imageList.add("https://www.thaiquote.org/wp-content/uploads/2018/07/maxresdefault.jpg");
        imageList.add("http://www.ch7.com/images/dfcda35a485980dc18b50065f812d599/596b66bfa33dee7a4888654c_7.jpg");*/
        //imageList.add(R.mipmap.title_04);
        //imageList.add(R.mipmap.title_05);

        //NUM_PAGES = imageList.size();

        extras = getIntent().getExtras();
        imageList = extras.getStringArrayList("imageList");
    }

    private void initUI() {
        txtSkip = (SukhumvitTextView) findViewById(R.id.txtSkip);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
    }

    private void setListener() {
        txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        viewPager.setAdapter(new ImageSlidePagerStringAdapter(getApplicationContext(), imageList, true));
        indicator.setViewPager(viewPager);

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }
}
