package th.or.dga.royaljitarsa;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.SliderTypes.DefaultSliderView;
import com.glide.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;

import th.or.dga.royaljitarsa.customview.SukhumvitTextView;
import th.or.dga.royaljitarsa.utils.AppPreference;

public class LandingPage extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener  {

    private RelativeLayout layoutImage;
    private ImageView imageView;
    private SukhumvitTextView txtVersion;

    protected boolean _active = true;
    protected int _splashTime = 5000;

    private RequestOptions requestOptions;
    private ArrayList<Integer> imageList;

    private boolean skipHowToPlay = false;
    private int loginStatus = 0;

    private String appVersion = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        AppPreference.getInstance(this).setScreenWidth(width);
        AppPreference.getInstance(this).setScreenHeight(height);

        initUI();
        initValue();
        setListener();
        launchSplashScreen();
    }

    private void initValue() {
        skipHowToPlay = AppPreference.getInstance(getApplicationContext()).getSkipHowToPlay();
        loginStatus = AppPreference.getInstance(getApplicationContext()).getLoginStatus();

        requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        
        imageList = new ArrayList<>();
        imageList.add(R.mipmap.title_01);
        imageList.add(R.mipmap.title_02);
        imageList.add(R.mipmap.title_03);
        imageList.add(R.mipmap.title_04);

        for(int i = 0; i < imageList.size(); i++) {
            DefaultSliderView sliderView = new DefaultSliderView(this);
            sliderView.image(imageList.get(i))
                    .setRequestOption(requestOptions)
                    .setBackgroundColor(Color.WHITE)
                    .setProgressBarVisible(true)
                    .setOnSliderClickListener(this);
        }
    }

    private void initUI() {
        imageView = (ImageView) findViewById(R.id.imageView);
        txtVersion = (SukhumvitTextView) findViewById(R.id.txtVersion);

        txtVersion.setText("Ver. " + appVersion);
    }
    
    private void setListener() {

    }

    private void launchSplashScreen() {
        // thread for displaying the SplashScreen
        final Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                        }
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    if(!skipHowToPlay) {
                        Intent intent = new Intent(getApplicationContext(), HowToPlay.class);
                        //intent.putExtra("fragmentNumber", 0);
                        startActivity(intent);
                        finish();
                    } else if(skipHowToPlay && loginStatus != 200) {
                        //Intent intent = new Intent(getApplicationContext(), Login.class);
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        //intent.putExtra("fragmentNumber", 0);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        //intent.putExtra("fragmentNumber", 0);
                        startActivity(intent);
                        finish();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //stuff that updates ui

                            //layoutImage.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.GONE);
                        }
                    });
                }
            }
        };
        splashTread.start();
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        //Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
