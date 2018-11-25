package th.or.dga.royaljitarsa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import th.or.dga.royaljitarsa.customview.SukhumvitTextView;

public class Agreement extends AppCompatActivity {

    private SukhumvitTextView txtAgreement;
    private SukhumvitTextView btnAccept;
    private SukhumvitTextView btnCancel;
    private SukhumvitTextView txtSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agreement);

        initUI();
        initValue();
        setListener();
    }

    private void initValue() {

    }

    private void initUI() {
        txtAgreement = (SukhumvitTextView) findViewById(R.id.txtAgreement);
        btnAccept = (SukhumvitTextView) findViewById(R.id.btnAccept);
        btnCancel = (SukhumvitTextView) findViewById(R.id.btnCancel);
        txtSkip = (SukhumvitTextView) findViewById(R.id.txtSkip);
    }
    
    private void setListener() {
        txtAgreement.setMovementMethod(new ScrollingMovementMethod());

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Agreement.class);
                startActivity(intent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VerifyUser.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
