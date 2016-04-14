package com.sasi.giffgaffplay;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

public class TestCarrier extends AppCompatActivity {

    TextView tvShowDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_carrier);

        tvShowDetails = (TextView) findViewById(R.id.tvShowDetails);

    }

    public void showDetails(View v) {

        String s = "";

        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        s = manager.getNetworkOperator()
                + "-" + manager.getNetworkOperatorName()
                + "-" + manager.getNetworkType()
//                + "-" + manager.getLine1Number()
                + "-" + manager.getSimSerialNumber()


        ;

        tvShowDetails.setText(s);
    }
}
