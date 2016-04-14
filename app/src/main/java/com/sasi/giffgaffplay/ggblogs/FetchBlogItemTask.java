package com.sasi.giffgaffplay.ggblogs;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.sasi.giffgaffplay.data.BlogContract;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sasikumarlakshmanan on 13/04/16.
 */
public class FetchBlogItemTask extends AsyncTask<String, Void, Void> {

    private final String TAG = FetchBlogItemTask.class.getSimpleName();

    private final Context mContext;
    private ArrayList<Integer> mBlogItemArrayList = null;

    public FetchBlogItemTask(Context context, ArrayList<Integer> blogItemArrayList) {
        mContext = context;
        mBlogItemArrayList = blogItemArrayList;
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Void doInBackground(String... params) {

        if (mBlogItemArrayList != null && mBlogItemArrayList.size() > 0) {

            for (int i = 0; i < mBlogItemArrayList.size(); i++) {

                int blog_id = mBlogItemArrayList.get(i);

                String jsonStr = connectAndGetData(blog_id);

                if (jsonStr != null) {
                    updateBlogItem(jsonStr, blog_id);
                }
            }
        }

        return null;
    }


    private String connectAndGetData(int blog_id) {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String jsonStr = null;

        Uri builtUri;

        try {
            URL url = new URL("https://community.giffgaff.com/restapi/vc/messages/id/" + blog_id + "?restapi.response_format=json");

//            Log.d(TAG, "Getting " + url.toString());

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
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }

        return jsonStr;
    }

    private void updateBlogItem(String jsonStr, int blog_id) {

        String bodyStr = parseData(jsonStr);

        if (bodyStr != null) {
            ContentValues cv = new ContentValues();
            cv.put(BlogContract.BlogEntry.COLUMN_BODY, bodyStr);

            mContext.getContentResolver().update(BlogContract.BlogEntry.CONTENT_URI, cv,
                    BlogContract.BlogEntry.COLUMN_BLOG_ID + " = " + blog_id, null);
        }
    }

    private String parseData(String jsonStr) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonStr);

            JSONObject responseObject = jsonObject.getJSONObject("response");
            JSONObject messageObject = responseObject.getJSONObject("message");
            JSONObject bodyObject = messageObject.getJSONObject("body");

            String bodyStr = bodyObject.getString("$");

//            Log.d(TAG, "bodyStr: " + bodyStr);

            return bodyStr;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
