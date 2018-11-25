package th.or.dga.royaljitarsa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import th.or.dga.royaljitarsa.connection.RegisterAPI;
import th.or.dga.royaljitarsa.customview.SukhumvitEditText;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;

public class Register extends AppCompatActivity implements RegisterAPI.RegisterAPIListener {

    private String TAG = Register.this.getClass().getSimpleName();

    private ProgressBar progressBar;
    private SukhumvitEditText inputEmail;
    private SukhumvitEditText inputName;
    private SukhumvitEditText inputUsername;
    private SukhumvitEditText inputPassword;
    private SukhumvitEditText inputConfirmPassword;
    private SukhumvitTextView btnSend;
    private SukhumvitTextView txtSignUp;
    private CheckBox btnAgreement;

    private RegisterAPI registerAPI;
    private RegisterAPI.RegisterAPIListener registerAPIListener;

    private boolean isAcceptAgreement = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        initUI();
        initValue();
        setListener();
    }

    private void initValue() {

    }

    private void initUI() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        inputEmail = (SukhumvitEditText) findViewById(R.id.inputEmail);
        inputName = (SukhumvitEditText) findViewById(R.id.inputName);
        inputUsername = (SukhumvitEditText) findViewById(R.id.inputUsername);
        inputPassword = (SukhumvitEditText) findViewById(R.id.inputPassword);
        inputConfirmPassword = (SukhumvitEditText) findViewById(R.id.inputConfirmPassword);
        btnSend = (SukhumvitTextView) findViewById(R.id.btnSend);
        txtSignUp = (SukhumvitTextView) findViewById(R.id.txtSignUp);
        btnAgreement = (CheckBox) findViewById(R.id.btnAgreement);
    }
    
    private void setListener() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                String name = inputName.getText().toString();
                String username = inputUsername.getText().toString();
                String password = inputPassword.getText().toString();
                String confirmPassword = inputConfirmPassword.getText().toString();

                if(password.equals(confirmPassword) && isAcceptAgreement) {
                    registerAPI = new RegisterAPI();
                    registerAPI.setEmail(email);
                    registerAPI.setFullName(name);
                    registerAPI.setTokenID("1");
                    registerAPI.setPassword(password);
                    registerAPI.setListener(new RegisterAPI.RegisterAPIListener() {
                        @Override
                        public void onRegisterAPIPreExecuteConcluded() {
                            progressBar.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onRegisterAPIPostExecuteConcluded(String result) {
                            progressBar.setVisibility(View.GONE);
                            try {
                                JSONObject jObj = new JSONObject(result);
                                int status = jObj.optInt("status");
                                String statusDetail = jObj.optString("status_detail");

                                Log.d(TAG, "statusDetail: " + statusDetail);
                                if(status == 200) {
                                    Toast.makeText(getApplicationContext(), statusDetail, Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getApplicationContext(), VerifyUser.class);
                                    startActivity(intent);

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
                    registerAPI.execute("");
                } else if(!isAcceptAgreement) {
                    Toast.makeText(getApplicationContext(), getText(R.string.register_alert_donot_accept_agreement), Toast.LENGTH_SHORT).show();
                    Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(500);
                } else {
                    Toast.makeText(getApplicationContext(), getText(R.string.register_alert_password_miss_match), Toast.LENGTH_SHORT).show();
                    Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(500);
                }
            }
        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Agreement.class);
                startActivity(intent);
                //finish();
            }
        });

        btnAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isAcceptAgreement = b;
            }
        });
    }

    @Override
    public void onRegisterAPIPreExecuteConcluded() {

    }

    @Override
    public void onRegisterAPIPostExecuteConcluded(String result) {

    }
}
