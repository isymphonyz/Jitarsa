package th.or.dga.royaljitarsa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import th.or.dga.royaljitarsa.customview.SukhumvitEditText;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;

public class ForgetPassword extends AppCompatActivity {

    private SukhumvitEditText inputEmail;
    private SukhumvitEditText inputOTP;
    private SukhumvitEditText inputNewPassword;
    private SukhumvitEditText inputConfirmPassword;
    private SukhumvitTextView btnSend;
    private SukhumvitTextView txtSignUp;

    private Bundle extras;
    private boolean isFromHome = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);

        initUI();
        initValue();
        setListener();
    }

    private void initValue() {
        extras = getIntent().getExtras();
        isFromHome = extras.getBoolean("isFromHome");

    }

    private void initUI() {
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
