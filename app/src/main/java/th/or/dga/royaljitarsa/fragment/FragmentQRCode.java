package th.or.dga.royaljitarsa.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.zxing.Result;

import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.utils.AppPreference;
import th.or.dga.royaljitarsa.utils.FirebaseLogTracking;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;

public class FragmentQRCode extends Fragment {

    private String TAG = FragmentQRCode.this.getClass().getSimpleName();

    private ImageView imgQRCode;

    private CodeScannerView scannerView;
    private CodeScanner mCodeScanner;

    private String qrCode = "";

    private static final int RC_PERMISSION = 10;
    private boolean mPermissionGranted;

    public static FragmentQRCode newInstance() {
        FragmentQRCode fragment = new FragmentQRCode();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_qr_code, container, false);

        addLog();
        permission();
        initValue();
        initUI(rootView);
        setUI();
        setListener();

        return rootView;
    }

    private void addLog() {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        FirebaseLogTracking firebaseLogTracking = new FirebaseLogTracking(mFirebaseAnalytics);
        firebaseLogTracking.addLogActivity(getText(R.string.log_param_scan_qr_code).toString());
    }

    private void permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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
        qrCode = AppPreference.getInstance(getActivity()).getQRCode();
    }

    private void initUI(View rootView) {
        imgQRCode = (ImageView) rootView.findViewById(R.id.imgQRCode);

        scannerView = rootView.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(getActivity(), scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), result.getText(), Toast.LENGTH_SHORT).show();
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
        /*Glide.with(this)
                .load(qrCode)
                .into(imgQRCode);*/

        qrCode = "http://demo.360innovative.com/2018/360jitarsa/cdn/img/1538399399.JPG";
        Log.d(TAG, "qrCode: " + qrCode);
        Glide.with(this)
                .load(qrCode)
                .apply(centerCropTransform()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_background)
                        .priority(Priority.HIGH))
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
        if (mPermissionGranted) {
            mCodeScanner.startPreview();
        }
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}
