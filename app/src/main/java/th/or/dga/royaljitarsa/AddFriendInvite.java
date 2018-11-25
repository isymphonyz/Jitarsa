package th.or.dga.royaljitarsa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import th.or.dga.royaljitarsa.utils.AppPreference;

public class AddFriendInvite extends AppCompatActivity {

    private String TAG = AddFriendInvite.this.getClass().getSimpleName();

    private ImageView imgQRCode;

    private String qrCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_me_friend_add_friend_invite);

        initUI();
        initValue();
        setUI();
        setListener();
    }

    private void initValue() {
        qrCode = AppPreference.getInstance(this).getQRCode();
    }

    private void initUI() {
        imgQRCode = (ImageView) findViewById(R.id.imgQRCode);
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
}
