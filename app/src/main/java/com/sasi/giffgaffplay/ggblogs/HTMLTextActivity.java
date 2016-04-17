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
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

    String jsToast = "<script type=\"text/javascript\">function showAndroidToast(toast) {Android.showToast(toast);}</script>";

    private View mCustomView;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;

    private RelativeLayout mContentView;
    private FrameLayout mCustomViewContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_htmltext);

        wv = (WebView) findViewById(R.id.wv);
        wv.setWebChromeClient(new MyWebChromeClient());
        wv.addJavascriptInterface(new WebAppInterface(this), "Android");

        setupWebView();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        int height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        String htmlText = "<html><body>You scored <b>192</b> points. <A title=\"Intelligent Energy\" href=\"http://www.digitaltrends.com/mobile/intelligent-energy-iphone-fuel-cell-battery-news/\" target=\"_blank\" rel=\"nofollow\">Intelligent Energy</A></body></html>";


        // 315 560
        String htmlVideo =
//                "<P><IFRAME onClick=\"showAndroidToast('IFRAME SRC!')\" src=\"https://www.youtube.com/embed/lfA3Mzqpbic\" allowfullscreen=\"allowfullscreen\" frameborder=\"0\" height=" + 100 + " width=" + 300 + "></IFRAME></P>" +
                "<img onClick=\"showAndroidToast('Hello Android!')\" src=\"https://napa.i.lithium.com/t5/image/serverpage/image-id/128337i2F90323374D02577/image-size/medium?v=lz-1&px=400\" alt=\"iPhone SE & iPhone 6S side-by-side\" title=\"20160407_190111.jpg\" />" +
                        "<input type=\"button\" value=\"Say hello\" onClick=\"showAndroidToast('Hello Android!')\" />\n" + jsToast;
        String style = "style=width: 560px; height: 315px; left: 0px; top: 0px; transform: none;";

//        ViewGroup.LayoutParams vc = wv.getLayoutParams();
//        vc.width = width;
//        wv.setLayoutParams(vc);

//        wv.loadData(htmlText, "text/html", null);
//        wv.loadData("<style>img{display: inline;height: auto;max-width: 100%;}</style>" + htmlVideo, "text/html; charset=UTF-8", null);

        wv.loadData("<style>iframe{width: 560px; height: 315px; left: 0px; top: 0px; transform: none;}</style>" + htmlVideo, "text/html; charset=UTF-8", null);


//        wv.loadUrl("<style>iframe{width: 560px; height: 315px; left: 0px; top: 0px; transform: none;}</style>" + "https://community.giffgaff.com/t5/Blog/Sony-Xperia-M5-Video-amp-Review-by-marktiddy/ba-p/18282829");
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

            String replStr = "<img onClick=\"showAndroidToast('Hello Android!')\"";

            bodyStr = bodyStr.replaceAll("<img", replStr);
            bodyStr = bodyStr + jsToast;

            Log.d("HTMLTextActivity", "PARSED DATA: " + bodyStr);

            return bodyStr;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void loadWebView(String bodyData) {

        if (bodyData != null) {
            wv.loadData("<style>img{display: inline;height: auto;max-width: 100%;}</style>" + bodyData, "text/html; charset=UTF-8", null);
//            wv.loadData(bodyData, "text/html; charset=UTF-8", null);
        }
    }

    private void setupWebView() {

        wv.getSettings().setLoadWithOverviewMode(true);
//        wv.getSettings().setUseWideViewPort(true);

        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new MyWebViewClient());

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

            Log.d(TAG, "Handler url: " + url);

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

    private class MyWebChromeClient extends WebChromeClient {

        FrameLayout.LayoutParams LayoutParameters = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {

            Log.d(TAG, "In onShowCustomView");

//            super.onShowCustomView(view, callback);

            // if a view already exists then immediately terminate the new one
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }

            mContentView = (RelativeLayout) findViewById(R.id.rlParentView);
            mContentView.setVisibility(View.GONE);
            mCustomViewContainer = new FrameLayout(HTMLTextActivity.this);
            mCustomViewContainer.setLayoutParams(LayoutParameters);
            mCustomViewContainer.setBackgroundResource(android.R.color.black);
            view.setLayoutParams(LayoutParameters);
            mCustomViewContainer.addView(view);
            mCustomView = view;
            mCustomViewCallback = callback;
            mCustomViewContainer.setVisibility(View.VISIBLE);
            HTMLTextActivity.this.setContentView(mCustomViewContainer);
        }

        @Override
        public void onHideCustomView() {

            Log.d(TAG, "In onHideCustomView");

            if (mCustomView == null) {
                return;
            } else {
                // Hide the custom view.
                mCustomView.setVisibility(View.GONE);
                // Remove the custom view from its container.
                mCustomViewContainer.removeView(mCustomView);
                mCustomView = null;
                mCustomViewContainer.setVisibility(View.GONE);
                mCustomViewCallback.onCustomViewHidden();
                // Show the content view.
                mContentView.setVisibility(View.VISIBLE);
                setContentView(mContentView);
            }
        }
    }

    public class WebAppInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
    }

}
