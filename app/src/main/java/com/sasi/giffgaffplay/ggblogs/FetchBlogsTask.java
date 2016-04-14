package com.sasi.giffgaffplay.ggblogs;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.sasi.giffgaffplay.data.BlogContract;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sasikumarlakshmanan on 12/04/16.
 */
public class FetchBlogsTask extends AsyncTask<Void, Void, Void> {

    private final String TAG = FetchBlogsTask.class.getSimpleName();

    private final Context mContext;

    public FetchBlogsTask(Context context) {
        mContext = context;
    }

    private ArrayList<Integer> mBlogIDArrayList = new ArrayList<>();
    private ArrayList<String> mAuthorArrayList = new ArrayList<>();

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p/>
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
    protected Void doInBackground(Void... params) {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String jsonStr;

        Uri builtUri;


        try {
            URL url = new URL("https://community.giffgaff.com/restapi/vc/blogs/id/giffgaff/topics?restapi.response_format=json");

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

            if (jsonStr != null) {
                parseDataAndInsert(mContext, jsonStr);
            }

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

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        // Now get the blog items.
        if (mBlogIDArrayList != null && mBlogIDArrayList.size() > 0) {

//            for (int i = 0; i < mBlogIDArrayList.size(); i++) {
//
//                Log.d(TAG, "GETTING BLOG ITEMS FOR ID: " + mBlogIDArrayList.get(i));
//
//                new FetchBlogItemTask(mContext).execute(String.valueOf(mBlogIDArrayList.get(i)));
//            }

            new FetchBlogItemTask(mContext, mBlogIDArrayList).execute();
            new FetchBlogAuthorImageTask(mContext, mBlogIDArrayList, mAuthorArrayList).execute();
        }
    }

    private void parseDataAndInsert(Context mContext, String jsonData) {

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(jsonData);

            JSONObject responseObject = jsonObject.getJSONObject("response");
            JSONObject nodeMessageObject = responseObject.getJSONObject("node_message_context");
            JSONArray messageArray = nodeMessageObject.getJSONArray("message");

//            Log.d(TAG, "messageArray.length(): " + messageArray.length());

            // Loop through the Message Array.
            for (int i = 0; i < messageArray.length(); i++) {

                int blog_id, kudos, views;
                String post_time, last_edit_time, last_edit_author, teaser, subject, author, author_id;
                String label = null;

                JSONObject messageObject = messageArray.getJSONObject(i);

                blog_id = messageObject.getJSONObject("id").getInt("$");
                post_time = messageObject.getJSONObject("post_time").getString("$");
                last_edit_time = messageObject.getJSONObject("last_edit_time").getString("$");
                last_edit_author = messageObject.getJSONObject("last_edit_author").getJSONObject("login").getString("$");
                kudos = messageObject.getJSONObject("kudos").getJSONObject("count").getInt("$");

                JSONArray labelsArray = messageObject.getJSONObject("labels").getJSONArray("label");

                // Get the first label.
                if (labelsArray.length() == 1) {

                    label = labelsArray.getJSONObject(0).getJSONObject("text").getString("$");
                } else if (labelsArray.length() > 1) {

                    // Get the last label from the array.
                    label = labelsArray.getJSONObject(labelsArray.length() - 1).getJSONObject("text").getString("$");
                }

                teaser = messageObject.getJSONObject("teaser").getString("$");
                views = messageObject.getJSONObject("views").getJSONObject("count").getInt("$");

                subject = messageObject.getJSONObject("subject").getString("$");
                author = messageObject.getJSONObject("author").getJSONObject("login").getString("$");
                author_id = messageObject.getJSONObject("author").getString("href");

                ContentValues cv = new ContentValues();
                cv.put(BlogContract.BlogEntry.COLUMN_BLOG_ID, blog_id);
                cv.put(BlogContract.BlogEntry.COLUMN_POST_TIME, post_time);
                cv.put(BlogContract.BlogEntry.COLUMN_LAST_EDIT_TIME, last_edit_time);
                cv.put(BlogContract.BlogEntry.COLUMN_LAST_EDIT_AUTHOR, last_edit_author);
                cv.put(BlogContract.BlogEntry.COLUMN_KUDOS, kudos);
                cv.put(BlogContract.BlogEntry.COLUMN_LABEL, label);
                cv.put(BlogContract.BlogEntry.COLUMN_TEASER, parseTeaser(teaser, blog_id));
                cv.put(BlogContract.BlogEntry.COLUMN_VIEWS, views);
                cv.put(BlogContract.BlogEntry.COLUMN_SUBJECT, subject);
                cv.put(BlogContract.BlogEntry.COLUMN_AUTHOR, author);

                // Add to the array list for getting the blog items.
                mBlogIDArrayList.add(blog_id);
                mAuthorArrayList.add(author_id);

                mContext.getContentResolver().insert(BlogContract.BlogEntry.CONTENT_URI, cv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String parseTeaser(String teaser, int blog_id) {

        final Document document = Jsoup.parse(teaser);

        Elements elements = document.getElementsByTag("P");

        if (elements != null && elements.size() > 0) {

            String elementTeaser = "";

//            elementTeaser = elements.get(elements.size() - 1).toString();
//            elementTeaser = elementTeaser.replace("&nbsp;", "");

            for (int i = 0; i < elements.size(); i++) {
//                Log.d(TAG, "BLOG_ID: " + blog_id + ": " + elements.get(i).toString());

                String tempElementStr = elements.get(i).toString();

                if (tempElementStr.indexOf("<img src") >= 0) {
                    // skip this, it's an image.
                    continue;
                }

                // Replace the space to "".
                tempElementStr = tempElementStr.replace("&nbsp;", "");

                elementTeaser = elementTeaser + tempElementStr;
            }

//            Log.d(TAG, "ELEMENT: " + elementTeaser);
            return elementTeaser;
        }

        return null;
    }

}
