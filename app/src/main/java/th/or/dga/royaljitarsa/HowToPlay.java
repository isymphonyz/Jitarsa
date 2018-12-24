package th.or.dga.royaljitarsa;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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

public class HowToPlay extends AppCompatActivity {

    private SukhumvitTextView txtSkip;
    private ViewPager viewPager;
    private CircleIndicator indicator;

    private ArrayList<Integer> imageList;
    //private ArrayList<String> imageList;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    private Bundle extras;
    private boolean isFromHome = false;
    private boolean isGallery = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.how_to_play);

        addLog();
        initUI();
        initValue();
        setListener();
        launchImageSlider();
    }

    @Override
    protected void onStop() {
        swipeTimer.cancel();
        super.onStop();
    }

    private void addLog() {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseLogTracking firebaseLogTracking = new FirebaseLogTracking(mFirebaseAnalytics);
        firebaseLogTracking.addLogActivity(getText(R.string.log_param_how_to_play).toString());
    }

    private void initValue() {
        imageList = new ArrayList<>();
        imageList.add(R.mipmap.title_01);
        imageList.add(R.mipmap.title_02);
        imageList.add(R.mipmap.title_03);
        //imageList.add(R.mipmap.title_04);
        //imageList.add(R.mipmap.title_05);

        /*imageList = new ArrayList<>();
        imageList.add("http://plearnengineering.com/360innovative/jitarsa/howtoplay/title_01.png");
        imageList.add("http://plearnengineering.com/360innovative/jitarsa/howtoplay/title_02.png");
        imageList.add("http://plearnengineering.com/360innovative/jitarsa/howtoplay/title_03.png");*/

        NUM_PAGES = imageList.size();

        extras = getIntent().getExtras();
        isFromHome = extras.getBoolean("isFromHome");
        isGallery = extras.getBoolean("isGallery");
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
                if(!isFromHome) {
                    //Intent intent = new Intent(getApplicationContext(), Login.class);
                    Intent intent = new Intent(getApplicationContext(), Home.class);
                    startActivity(intent);
                }
                finish();
                AppPreference.getInstance(getApplicationContext()).setSkipHowToPlay(true);
            }
        });

        viewPager.setAdapter(new ImageSlidePagerAdapter(getApplicationContext(), imageList));
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

    Timer swipeTimer = new Timer();
    Handler handler = new Handler();
    Runnable Update;
    private void launchImageSlider() {
        // Auto start of viewpager

        Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
    }
}
