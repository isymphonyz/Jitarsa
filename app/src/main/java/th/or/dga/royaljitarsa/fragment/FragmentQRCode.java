package th.or.dga.royaljitarsa.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import th.or.dga.royaljitarsa.R;
import th.or.dga.royaljitarsa.utils.AppPreference;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;

public class FragmentQRCode extends Fragment {

    private String TAG = FragmentQRCode.this.getClass().getSimpleName();

    private ImageView imgQRCode;

    private String qrCode = "";

    public static FragmentQRCode newInstance() {
        FragmentQRCode fragment = new FragmentQRCode();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_qr_code, container, false);

        initValue();
        initUI(rootView);
        setUI();
        setListener();

        return rootView;
    }

    private void initValue() {
        qrCode = AppPreference.getInstance(getActivity()).getQRCode();
    }

    private void initUI(View rootView) {
        imgQRCode = (ImageView) rootView.findViewById(R.id.imgQRCode);
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
}
