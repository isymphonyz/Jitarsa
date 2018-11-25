package th.or.dga.royaljitarsa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import th.or.dga.royaljitarsa.customview.SukhumvitTextView;

public class Approve extends AppCompatActivity {

    private SukhumvitTextView btnAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.approve);

        initUI();
        initValue();
        setListener();
    }

    private void initValue() {

    }

    private void initUI() {
        btnAccept = (SukhumvitTextView) findViewById(R.id.btnAccept);
    }
    
    private void setListener() {
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
