package th.or.dga.royaljitarsa;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.SliderTypes.DefaultSliderView;
import com.glide.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import th.or.dga.royaljitarsa.customview.SukhumvitTextView;
import th.or.dga.royaljitarsa.utils.AppPreference;

public class LandingPage extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener  {

    private static final String TAG = LandingPage.class.getSimpleName();

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

    private static final int RC_BARCODE_CAPTURE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);

        //FirebaseApp.initializeApp(this);

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

        firebaseSubscriptTopic();
        initUI();
        initValue();
        setListener();
        launchSplashScreen();
    }

    private void firebaseSubscriptTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(getText(R.string.firebase_messaging_subscribe_topic_android).toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Success to Subscript Topic : Android";
                        if (!task.isSuccessful()) {
                            msg = "Fail to Subscript Topic : Android";
                        }
                        Log.d(TAG, msg);
                    }
                });

        /*FirebaseMessaging.getInstance().subscribeToTopic(getText(R.string.firebase_messaging_subscribe_topic_testandroid).toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Success to Subscript Topic : Android";
                        if (!task.isSuccessful()) {
                            msg = "Fail to Subscript Topic : Android";
                        }
                        Log.d(TAG, msg);
                    }
                });*/
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

                    Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                    intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                    intent.putExtra(BarcodeCaptureActivity.UseFlash, false);

                    //startActivityForResult(intent, RC_BARCODE_CAPTURE);

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    //statusMessage.setText(R.string.barcode_success);
                    //barcodeValue.setText(barcode.displayValue);
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    //statusMessage.setText(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                //statusMessage.setText(String.format(getString(R.string.barcode_error), CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
