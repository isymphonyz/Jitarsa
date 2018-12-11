package th.or.dga.royaljitarsa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import th.or.dga.royaljitarsa.connection.ResetPasswordAPI;
import th.or.dga.royaljitarsa.customview.SukhumvitEditText;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;
import th.or.dga.royaljitarsa.utils.FirebaseLogTracking;

public class ChangePassword extends AppCompatActivity {

    private String TAG = ChangePassword.class.getSimpleName();

    private ProgressBar progressBar;
    private SukhumvitEditText inputEmail;
    private SukhumvitEditText inputOTP;
    private SukhumvitEditText inputNewPassword;
    private SukhumvitEditText inputConfirmPassword;
    private SukhumvitTextView btnSend;
    private SukhumvitTextView txtSignUp;

    private ResetPasswordAPI resetPasswordAPI;

    private Bundle extras;
    private boolean isFromHome = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        addLog();
        initUI();
        initValue();
        setListener();
    }

    private void addLog() {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseLogTracking firebaseLogTracking = new FirebaseLogTracking(mFirebaseAnalytics);
        firebaseLogTracking.addLogActivity(getText(R.string.log_param_change_password).toString());
    }

    private void initValue() {
        extras = getIntent().getExtras();
        isFromHome = extras.getBoolean("isFromHome");
    }

    private void initUI() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        inputEmail = (SukhumvitEditText) findViewById(R.id.inputEmail);
        inputOTP = (SukhumvitEditText) findViewById(R.id.inputOTP);
        inputNewPassword = (SukhumvitEditText) findViewById(R.id.inputNewPassword);
        inputConfirmPassword = (SukhumvitEditText) findViewById(R.id.inputConfirmPassword);
        btnSend = (SukhumvitTextView) findViewById(R.id.btnSend);
        txtSignUp = (SukhumvitTextView) findViewById(R.id.txtSignUp);
    }
    
    private void setListener() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                String otp = inputOTP.getText().toString();
                String newPassword = inputNewPassword.getText().toString();
                String confirmPassword = inputConfirmPassword.getText().toString();

                //username = "aaa@gmail.com";
                //password = "aaa";

                //executeLogin(username, password);
                //new LoginAPI().execute("");

                resetPasswordAPI = new ResetPasswordAPI();
                resetPasswordAPI.setEmail(email);
                resetPasswordAPI.setPassword(newPassword);
                resetPasswordAPI.setOTP(otp);
                resetPasswordAPI.setListener(new ResetPasswordAPI.ResetPasswordAPIListener() {
                    @Override
                    public void onResetPasswordAPIPreExecuteConcluded() {
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResetPasswordAPIPostExecuteConcluded(String result) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject jObj = new JSONObject(result);
                            int status = jObj.optInt("status");
                            String statusDetail = jObj.optString("status_detail");

                            Log.d(TAG, "statusDetail: " + statusDetail);
                            if(status == 200) {
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), statusDetail, Toast.LENGTH_SHORT).show();
                                Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                v.vibrate(500);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                resetPasswordAPI.execute("");
            }
        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
