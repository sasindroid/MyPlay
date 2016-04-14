package com.sasi.giffgaffplay.ggblogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sasi.giffgaffplay.R;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HTMLTextActivity extends AppCompatActivity {

    private static final String TAG = "HTMLTextActivity";
    WebView wv;
    int width = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_htmltext);

        wv = (WebView) findViewById(R.id.wv);

        setupWebView();


        String htmlText = "<html><body>You scored <b>192</b> points. <A title=\"Intelligent Energy\" href=\"http://www.digitaltrends.com/mobile/intelligent-energy-iphone-fuel-cell-battery-news/\" target=\"_blank\" rel=\"nofollow\">Intelligent Energy</A></body></html>";


        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        int height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

//        ViewGroup.LayoutParams vc = wv.getLayoutParams();
//        vc.width = width;
//        wv.setLayoutParams(vc);

//        wv.loadData(htmlText, "text/html", null);
        wv.loadData("<style>img{display: inline;height: auto;max-width: 100%;}</style>" + htmlText, "text/html; charset=UTF-8", null);
//        wv.getSettings().setUseWideViewPort(false);

        new FetchJsonTask(this).execute();

//        new FetchBlogsTask(this).execute();

    }

    public class FetchJsonTask extends AsyncTask<Void, Void, String> {


        private final String LOG_TAG = FetchJsonTask.class.getSimpleName();

        private final Context mContext;

        public FetchJsonTask(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(Void... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String jsonStr;

            Uri builtUri;


            try {
//                18284109 18441179

                URL url = new URL("https://community.giffgaff.com/restapi/vc/messages/id/18441179?restapi.response_format=json");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                jsonStr = buffer.toString();

//                Log.d(LOG_TAG, "JSON Returned: " + jsonStr);

                return jsonStr;

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String str) {

            String bodyData = parseData(str);

            if (bodyData != null) {

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("JSON", bodyData);
                editor.commit();

//                wv.loadData(bodyData, "text/html", null);
//                wv.loadData("<style>img{display: inline;height: auto;max-width: 100%;}</style>" + bodyData, "text/html; charset=UTF-8", null);
                loadWebView(bodyData);
            } else {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                bodyData = sp.getString("JSON", "");

//                wv.loadData(bodyData, "text/html", null);
                loadWebView(bodyData);
            }
        }
    }

    private String parseData(String str) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(str);

            JSONObject responseObject = jsonObject.getJSONObject("response");
            JSONObject messageObject = responseObject.getJSONObject("message");
            JSONObject bodyObject = messageObject.getJSONObject("body");

            String bodyStr = bodyObject.getString("$");

            Log.d("HTMLTextActivity", "PARSED DATA: " + bodyStr);

            return bodyStr;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void loadWebView(String bodyData) {

        if (bodyData != null) {
            wv.loadData("<style>img{display: inline;height: auto;max-width: 100%;}video{display: inline;height: auto;max-width: 100%;}</style>" + bodyData, "text/html; charset=UTF-8", null);
//            wv.loadData(bodyData, "text/html; charset=UTF-8", null);
        }
    }

    private void setupWebView() {

//        wv.getSettings().setLoadWithOverviewMode(true);
//        wv.getSettings().setUseWideViewPort(true);

        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new MyWebViewClient());

//        wv.setClickable(true);
//        wv.setFocusable(true);

        wv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebView.HitTestResult result = wv.getHitTestResult();

                Log.d(TAG, "HITTESTRESULT1: " + result.getType() + " " + result.toString());
            }
        });

        wv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                WebView.HitTestResult result = wv.getHitTestResult();

                Log.d(TAG, "HITTESTRESULT2: " + result.getType() + " " + result.toString());

                if (result.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    Message msg = mHander.obtainMessage();
                    wv.requestFocusNodeHref(msg);
                }


                return false;
            }
        });

        wv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.d(TAG, "ACTION: " + event.getAction());

                return false;
            }
        });
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            return super.shouldOverrideUrlLoading(view, url);

            String imageBaseUrl = "https://napa.i.lithium.com/t5/image/serverpage/image-id/";

            Log.d(TAG, "URL coming in: " + url);

            // If the image is clicked open it on a new window.
            if (url.indexOf(imageBaseUrl) >= 0) {
                Log.d(TAG, "URL: " + url);
            }

            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);

            String url = (String) msg.getData().get("url");

            Log.d(TAG, "Hander url: " + url);

        }
    };

    private void parseUsingJsoup(String bodyData) {

        String sampleData = "<P><span class=\\\"lia-inline-image-display-wrapper lia-image-align-inline\\\" style=\\\"width: 600px;\\\"><img src=\\\"https:\\/\\/napa.i.lithium.com\\/t5\\/image\\/serverpage\\/image-id\\/128700i24C3249BD25535A4\\/image-size\\/large?v=lz-1&px=600\\\" alt=\\\"Phone-reviews.jpg\\\" title=\\\"Phone-reviews.jpg\\\" \\/><\\/span>\uFEFF<\\/P>\\n<P> <\\/P>\\n<P>Today, many of the latest high-end smartphones contain a new technology known as Qualcomm Quick Charge. The technology potentially allows you to charge your smartphone up to four times faster than with a normal charger.<\\/P>";

        final Document document = Jsoup.parse(sampleData);

        Log.d(TAG, "JSOUP DOCUMENT: " + document.toString());

        JSONObject jsonObject = null;
    }

    private String getTeaserText(String bodyData) {

//        Log.d(TAG, "JSOUP DOCUMENT: " + document.toString());


        return null;
    }

}
