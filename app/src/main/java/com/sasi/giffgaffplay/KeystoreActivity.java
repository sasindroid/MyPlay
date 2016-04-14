package com.sasi.giffgaffplay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.q42.qlassified.Qlassified;
import com.q42.qlassified.Storage.QlassifiedSharedPreferencesService;

public class KeystoreActivity extends AppCompatActivity {

    TextView tv1, tv2;
    EditText et1;

    String myalias = "myalias";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keystore);

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        et1 = (EditText) findViewById(R.id.et1);
    }

//    public void encrypt(View view) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
//
//        // Create a start and end time, for the validity range of the key pair that's about to be
//        // generated.
//        Calendar start = new GregorianCalendar();
//        Calendar end = new GregorianCalendar();
//        end.add(Calendar.YEAR, 1);
//
//        // Create spec
//        KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(this)
//                .setAlias(myalias)
//                .setSubject(new X500Principal("CN=" + myalias))
//                .setSerialNumber(BigInteger.valueOf(1337))
//                .setStartDate(start.getTime())
//                .setEndDate(end.getTime())
//                .build();
//
//        // Create keypair
//        // Initialize a KeyPair generator using the the intended algorithm (in this example, RSA
//        // and the KeyStore.  This example uses the AndroidKeyStore.
//        KeyPairGenerator kpGenerator = KeyPairGenerator.getInstance("RSA",
//                "AndroidKeyStore");
//        kpGenerator.initialize(spec);
//
//        KeyPair keyPair = kpGenerator.generateKeyPair();
//
//        Log.d("KeystoreActivity", "Public key is: " + keyPair.getPublic().toString() + "\n\n" + keyPair.getPrivate().toString());
//
//
//    }

    public void encrypt(View view) {
        // Initialize the Qlassified service
        Qlassified.Service.start(this);
        Qlassified.Service.setStorageService(new QlassifiedSharedPreferencesService(this, et1.getText().toString()));

        // Save a key/value pair encrypted
        Qlassified.Service.put("SomeKey", "SomeValue");
    }

    public void decrypt(View view) {
        String getVal = Qlassified.Service.getString("SomeKey");
        tv2.setText(getVal);
    }
}
