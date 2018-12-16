package th.or.dga.royaljitarsa;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.zxing.Result;

import th.or.dga.royaljitarsa.utils.AppPreference;
import th.or.dga.royaljitarsa.utils.FirebaseLogTracking;

public class QRCode extends AppCompatActivity {

    private String TAG = QRCode.this.getClass().getSimpleName();

    private ImageView imgQRCode;

    private LinearLayout layoutScanQRCode;
    private CodeScannerView scannerView;
    private CodeScanner mCodeScanner;

    private String qrCode = "";

    private static final int RC_PERMISSION = 10;
    private boolean mPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_qr_code);

        addLog();
        permission();
        initUI();
        initValue();
        setUI();
        setListener();
    }

    private void addLog() {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseLogTracking firebaseLogTracking = new FirebaseLogTracking(mFirebaseAnalytics);
        firebaseLogTracking.addLogActivity(getText(R.string.log_param_scan_qr_code).toString());
    }

    private void permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = false;
                requestPermissions(new String[] {Manifest.permission.CAMERA}, RC_PERMISSION);
            } else {
                mPermissionGranted = true;
            }
        } else {
            mPermissionGranted = true;
        }
    }

    private void initValue() {
        qrCode = AppPreference.getInstance(this).getQRCode();
    }

    private void initUI() {
        imgQRCode = (ImageView) findViewById(R.id.imgQRCode);

        layoutScanQRCode = (LinearLayout) findViewById(R.id.layoutScanQRCode);
        scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                QRCode.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "result: " + result.getText());
                        Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show();

                        if(Patterns.WEB_URL.matcher(result.getText()).matches()) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(result.getText()));
                            startActivity(intent);
                        } else if(Patterns.PHONE.matcher(result.getText()).matches()) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(result.getText()));
                            startActivity(intent);
                        } else if(Patterns.EMAIL_ADDRESS.matcher(result.getText()).matches()) {
                            Intent intent = new Intent(Intent.ACTION_SENDTO)
                                    .setType("plain/text")
                                    .setData(Uri.parse(result.getText()))
                                    .setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(result.getText()));
                            startActivity(intent);
                        }
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    private void setUI() {
        qrCode = "http://demo.360innovative.com/2018/360jitarsa/cdn/img/qrcode/10.png";
        Log.d(TAG, "qrCode: " + qrCode);
        Glide.with(this)
                .load(qrCode)
                .into(imgQRCode);
    }
    
    private void setListener() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == RC_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = true;
                mCodeScanner.startPreview();
            } else {
                mPermissionGranted = false;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "mPermissionGranted: " + mPermissionGranted);
        if (mPermissionGranted) {
            mCodeScanner.startPreview();
        }
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
}
