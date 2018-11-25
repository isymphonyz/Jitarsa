package th.or.dga.royaljitarsa.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.bumptech.glide.Glide;
import com.google.zxing.Result;

import th.or.dga.royaljitarsa.Home;
import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.customview.SukhumvitEditText;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;

public class FragmentMeFriendAddFriend extends Fragment {

    private String TAG = FragmentMeFriendAddFriend.this.getClass().getSimpleName();

    private SukhumvitEditText inputEmail;
    private SukhumvitTextView btnAddFriend;
    private LinearLayout layoutButtonAddFriend;
    private LinearLayout layoutQRCode;
    private LinearLayout layoutScan;
    private LinearLayout layoutInvite;
    private LinearLayout layoutMyQRCode;
    private LinearLayout layoutScanQRCode;
    private ImageView imgQRCode;

    private CodeScannerView scannerView;
    private CodeScanner mCodeScanner;

    private String qrCode = "";

    private static final int RC_PERMISSION = 10;
    private boolean mPermissionGranted;

    public static FragmentMeFriendAddFriend newInstance() {
        FragmentMeFriendAddFriend fragment = new FragmentMeFriendAddFriend();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_me_friend_add_friend, container, false);

        permission();
        initValue();
        initUI(rootView);
        setUI();
        setListener();

        return rootView;
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

    }

    private void initUI(View rootView) {
        inputEmail = (SukhumvitEditText) rootView.findViewById(R.id.inputEmail);
        btnAddFriend = (SukhumvitTextView) rootView.findViewById(R.id.btnAddFriend);
        layoutButtonAddFriend = (LinearLayout) rootView.findViewById(R.id.layoutButtonAddFriend);
        layoutQRCode = (LinearLayout) rootView.findViewById(R.id.layoutQRCode);
        layoutScan = (LinearLayout) rootView.findViewById(R.id.layoutScan);
        layoutInvite = (LinearLayout) rootView.findViewById(R.id.layoutInvite);
        layoutMyQRCode = (LinearLayout) rootView.findViewById(R.id.layoutMyQRCode);
        layoutScanQRCode = (LinearLayout) rootView.findViewById(R.id.layoutScanQRCode);
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
        qrCode = "http://demo.360innovative.com/2018/360jitarsa/cdn/img/qrcode/10.png";
        Log.d(TAG, "qrCode: " + qrCode);
        Glide.with(this)
                .load(qrCode)
                .into(imgQRCode);
    }

    private void setListener() {
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Home.getInstance(getActivity()).displayFragment(transaction, FragmentMeFriendAddFriendResponse.newInstance());
            }
        });
        layoutQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutButtonAddFriend.setVisibility(View.GONE);
                layoutMyQRCode.setVisibility(View.VISIBLE);
                layoutScanQRCode.setVisibility(View.GONE);
            }
        });
        layoutScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutButtonAddFriend.setVisibility(View.GONE);
                layoutMyQRCode.setVisibility(View.GONE);
                layoutScanQRCode.setVisibility(View.VISIBLE);
            }
        });
        layoutInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Home.getInstance(getActivity()).displayFragment(transaction, FragmentMeFriendAddFriendInvite.newInstance());
            }
        });
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
