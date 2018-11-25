package th.or.dga.royaljitarsa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import th.or.dga.royaljitarsa.customview.SukhumvitEditText;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;

public class VerifyData extends AppCompatActivity {

    private SukhumvitEditText inputCitizenID;
    private SukhumvitEditText inputLaserCode;
    private SukhumvitEditText inputName;
    private SukhumvitEditText inputAge;
    private SukhumvitEditText inputBirthDate;
    private SukhumvitEditText inputRegistedDate;
    private SukhumvitEditText inputEmail;
    private SukhumvitEditText inputTel;
    private SukhumvitTextView btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_data);

        initUI();
        initValue();
        setListener();
    }

    private void initValue() {

    }

    private void initUI() {
        inputCitizenID = (SukhumvitEditText) findViewById(R.id.inputCitizenID);
        inputLaserCode = (SukhumvitEditText) findViewById(R.id.inputLaserCode);
        inputName = (SukhumvitEditText) findViewById(R.id.inputName);
        inputAge = (SukhumvitEditText) findViewById(R.id.inputAge);
        inputBirthDate = (SukhumvitEditText) findViewById(R.id.inputBirthDate);
        inputRegistedDate = (SukhumvitEditText) findViewById(R.id.inputRegistedDate);
        inputEmail = (SukhumvitEditText) findViewById(R.id.inputEmail);
        inputTel = (SukhumvitEditText) findViewById(R.id.inputTel);
        btnSave = (SukhumvitTextView) findViewById(R.id.btnSave);
    }
    
    private void setListener() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
