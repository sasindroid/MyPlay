package com.sasi.giffgaffplay;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

public class ToggleButtonCustom extends AppCompatActivity {

    ToggleButton toggleButton1, toggleButton2;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toggle_button_custom);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
        toggleButton2 = (ToggleButton) findViewById(R.id.toggleButton2);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                for (int j = 0; j < radioGroup.getChildCount(); j++) {
                    final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
//                    view.setChecked(view.getId() == checkedId);

                    if(view.getId() == checkedId) {
                        view.setChecked(true);
                        view.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.green_material));
                    }
                    else {
                        view.setChecked(false);
                        view.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.white));
                    }

                }
            }
        });
    }

    public void onToggle(View view) {
        ((RadioGroup) view.getParent()).check(view.getId());
        // app specific stuff ..
    }
}
