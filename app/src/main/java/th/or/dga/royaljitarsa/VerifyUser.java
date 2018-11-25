package th.or.dga.royaljitarsa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import th.or.dga.royaljitarsa.connection.InvestigateAPI;
import th.or.dga.royaljitarsa.customview.SukhumvitEditText;
import th.or.dga.royaljitarsa.customview.SukhumvitTextView;
import th.or.dga.royaljitarsa.utils.AppPreference;
import th.or.dga.royaljitarsa.utils.MyConfiguration;

public class VerifyUser extends AppCompatActivity {

    private ProgressBar progressBar;
    private SukhumvitEditText inputCitizenID;
    private SukhumvitEditText inputLaserCode;
    private SukhumvitTextView btnVerify;
    private SukhumvitTextView txtSkip;

    private AppPreference appPreference;
    private String urlInvestigate = MyConfiguration.URL_INVESTIGATE;
    private String userID = "";
    private String identificationCard = "";
    private String laserCode = "";

    private InvestigateAPI investigateAPI;
    private InvestigateAPI.InvestigateAPIListener investigateAPIListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_user);

        initUI();
        initValue();
        setListener();
    }

    private void initValue() {
        appPreference = new AppPreference(this);
        userID = appPreference.getUserID();
        identificationCard = appPreference.getIdentificationCard();
        laserCode = appPreference.getLaserCode();
    }

    private void initUI() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        inputCitizenID = (SukhumvitEditText) findViewById(R.id.inputCitizenID);
        inputLaserCode = (SukhumvitEditText) findViewById(R.id.inputLaserCode);
        btnVerify = (SukhumvitTextView) findViewById(R.id.btnVerify);
        txtSkip = (SukhumvitTextView) findViewById(R.id.txtSkip);
    }
    
    private void setListener() {
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                investigateAPI = new InvestigateAPI();
                investigateAPI.setUserID(userID);
                investigateAPI.setIdentificationCard(identificationCard);
                investigateAPI.setLaserCode(laserCode);
                investigateAPI.setListener(new InvestigateAPI.InvestigateAPIListener() {
                    @Override
                    public void onInvestigateAPIPreExecuteConcluded() {
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onInvestigateAPIPostExecuteConcluded(String result) {
                        progressBar.setVisibility(View.GONE);

                        Intent intent = new Intent(getApplicationContext(), VerifyUser.class);
                        startActivity(intent);

                        finish();
                    }
                });
                investigateAPI.execute("");
            }
        });

        txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VerifyData.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
