package com.sasi.giffgaffplay;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

public class TextViewPlay extends AppCompatActivity {

    TextView tv3;
    boolean bool = false;
    String s3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view_play);

        tv3 = (TextView) findViewById(R.id.tv3);

        final String s = "Hi Sasi, how are you? How is android doing? budget is " ;
        final String s1 = "100";

        tv3.setText(s);

        new CountDownTimer(10000, 500) {

            /**
             * Callback fired on regular interval.
             *
             * @param millisUntilFinished The amount of time until finished.
             */
            @Override
            public void onTick(long millisUntilFinished) {
                if(bool) {
                    bool = false;
                    s3 = s;
                }
                else {
                    bool = true;
                    s3 = s + s1;
                }
                tv3.setText(s3);
            }

            /**
             * Callback fired when the time is up.
             */
            @Override
            public void onFinish() {
                tv3.setText(s + s1);
            }
        }.start();

    }
}
