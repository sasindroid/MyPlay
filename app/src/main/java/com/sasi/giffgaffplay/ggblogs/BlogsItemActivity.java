package com.sasi.giffgaffplay.ggblogs;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sasi.giffgaffplay.R;
import com.sasi.giffgaffplay.data.BlogContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BlogsItemActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "AppCompatActivity";
    WebView wv_body;
    int blog_id;
    int width = 0;
    int height = 0;

    private static final int CURSOR_LOADER_ID = 2;

    private boolean temp_blog_avatar = true;
    private boolean temp_color_mode = false;

    public void tempSetBool() {
        temp_blog_avatar = BlogsActivity.getMyBoolVal("BLOG_AVATAR", this);
        temp_color_mode = BlogsActivity.getMyBoolVal("BLOG_COLOR_MODE", this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogs_item_new);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("BLOG_ID")) {
            blog_id = intent.getIntExtra("BLOG_ID", 0);
        }

        setScreenMetrics();
        tempSetBool();

        wv_body = (WebView) findViewById(R.id.wv_body);
        wv_body.getSettings().setJavaScriptEnabled(true);
        wv_body.getSettings().setBuiltInZoomControls(true);
//        wv_body.getSettings().setSupportZoom(true);

        // Don't show zoom controls.
        wv_body.getSettings().setDisplayZoomControls(false);

        myWebChromeClient = new MyWebChromeClient();

        wv_body.setWebChromeClient(myWebChromeClient);

        if (blog_id > 0) {
            // Initiate the Loader Task (Background task)
            getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        }

//        #scenetransition
        // Being here means we are in animation mode
//        supportPostponeEnterTransition();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_blog, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // Handle the Action bar home up button - Custom
            case android.R.id.home:
//                #scenetransition
                supportFinishAfterTransition();
//                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.blog_item_kudos:
                Toast.makeText(this, "Kudos revoked!", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        Log.d(TAG, "onCreateLoader called");

        Uri uri = BlogContract.BlogEntry.CONTENT_URI;
        String[] projection = new String[]{BlogContract.BlogEntry._ID,
                BlogContract.BlogEntry.COLUMN_BLOG_ID, BlogContract.BlogEntry.COLUMN_POST_TIME, BlogContract.BlogEntry.COLUMN_LAST_EDIT_TIME,
                BlogContract.BlogEntry.COLUMN_LAST_EDIT_AUTHOR, BlogContract.BlogEntry.COLUMN_KUDOS, BlogContract.BlogEntry.COLUMN_LABEL,
                BlogContract.BlogEntry.COLUMN_TEASER, BlogContract.BlogEntry.COLUMN_BODY, BlogContract.BlogEntry.COLUMN_VIEWS,
                BlogContract.BlogEntry.COLUMN_SUBJECT, BlogContract.BlogEntry.COLUMN_AUTHOR, BlogContract.BlogEntry.COLUMN_AUTHOR_URL
        };
        String selection = BlogContract.BlogEntry.COLUMN_BLOG_ID + " = " + blog_id;
        return new android.support.v4.content.CursorLoader(this, uri, projection, selection, null, null);
    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is <em>not</em> allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.  See {@link //FragmentManager#beginTransaction()
     * FragmentManager.openTransaction()} for further discussion on this.
     * <p/>
     * <p>This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     * <p/>
     * <ul>
     * <li> <p>The Loader will monitor for changes to the data, and report
     * them to you through new calls here.  You should not monitor the
     * data yourself.  For example, if the data is a {@link Cursor}
     * and you place it in a {@link CursorAdapter}, use
     * the {@link CursorAdapter#CursorAdapter(Context,
     * Cursor, int)} constructor <em>without</em> passing
     * in either {@link CursorAdapter#FLAG_AUTO_REQUERY}
     * or {@link CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
     * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
     * from doing its own observing of the Cursor, which is not needed since
     * when a change happens you will get a new Cursor throw another call
     * here.
     * <li> The Loader will release the data once it knows the application
     * is no longer using it.  For example, if the data is
     * a {@link Cursor} from a {@link CursorLoader},
     * you should not call close() on it yourself.  If the Cursor is being placed in a
     * {@link CursorAdapter}, you should use the
     * {@link CursorAdapter#swapCursor(Cursor)}
     * method so that the old Cursor is not closed.
     * </ul>
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.moveToFirst()) {

            String bodyStr = data.getString(data.getColumnIndex(BlogContract.BlogEntry.COLUMN_BODY));
            int views = data.getInt(data.getColumnIndex(BlogContract.BlogEntry.COLUMN_VIEWS));
            String author = data.getString(data.getColumnIndex(BlogContract.BlogEntry.COLUMN_AUTHOR));
            int kudos = data.getInt(data.getColumnIndex(BlogContract.BlogEntry.COLUMN_KUDOS));
            String time = data.getString(data.getColumnIndex(BlogContract.BlogEntry.COLUMN_POST_TIME));
            String subject = data.getString(data.getColumnIndex(BlogContract.BlogEntry.COLUMN_SUBJECT));

            TextView tv_views = (TextView) findViewById(R.id.tv_views);
            final TextView tv_author = (TextView) findViewById(R.id.tv_author);
            TextView tv_kudos = (TextView) findViewById(R.id.tv_kudos);
            TextView tv_time = (TextView) findViewById(R.id.tv_time);
            ImageView iv_author_avatar = (ImageView) findViewById(R.id.iv_author_avatar);
            TextView tv_subject = (TextView) findViewById(R.id.tv_subject);

            // Disable scrolling using ScrollView, instead use the WebView.
            NestedScrollView detail_container = (NestedScrollView) findViewById(R.id.detail_container);

            tv_subject.setText(Html.fromHtml(subject));
            tv_views.setText(views + " Views");
            tv_author.setText(author);

            if (temp_blog_avatar) {
                Glide.with(this).load(data.getString(data.getColumnIndex(BlogContract.BlogEntry.COLUMN_AUTHOR_URL))).into(iv_author_avatar);

            } else {
                iv_author_avatar.setImageResource(R.drawable.ic_account_circle_black_24dp);
            }

            tv_kudos.setText(kudos + " Kudos");
            tv_time.setText(formatDateTime(time));

            loadWebView(bodyStr);
        }

//        #scenetransition
//        AppCompatActivity activity = (AppCompatActivity) BlogsItemActivity.this;
//        Toolbar toolbarView = (Toolbar) activity.findViewById(R.id.toolbar);
//
//        activity.supportStartPostponedEnterTransition();
//
//        if (null != toolbarView) {
//            activity.setSupportActionBar(toolbarView);
//
//            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
//            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void loadWebView(String bodyData) {

        Log.d(TAG, "WIDTH IN: " + width);

        if (width == 0) {
            width = 300;
            height = width;
        } else if (width > 350) {
            width = 350;
            height = 350;
        } else {

            width = width - 20;
            height = width;
        }

        Log.d(TAG, "WIDTH OUT: " + width);

        if (bodyData != null) {
            wv_body.loadData(
                    "<style>img{display: inline;height: auto;max-width: 100%;}"
//                            + "iframe{display: inline;height: 350px;width: 350px}"
                            + "iframe{display: inline;height: " + height + "px;width: " + width + "px}"
//                            + "iframe{display: inline;height: auto;max-width: 100%}"
                            + "</style>"
                            + bodyData, "text/html; charset=UTF-8", null);

//            StringBuilder html = new StringBuilder();
//            html.append("<html>");
//            html.append("<head>");
//
//            html.append("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css\">");
//            html.append("</head>");
//            html.append("<body>");
//            html.append(bodyData);
//            html.append("</body>");
//            html.append("</html>");
//
//            wv_body.loadData(html.toString(), "text/html; charset=UTF-8", null);
        }
    }

    private View mCustomView;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private MyWebChromeClient myWebChromeClient;
    private CoordinatorLayout mContentView;
    private FrameLayout mCustomViewContainer;

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

            mContentView = (CoordinatorLayout) findViewById(R.id.clParentView);
            mContentView.setVisibility(View.GONE);
            mCustomViewContainer = new FrameLayout(BlogsItemActivity.this);
            mCustomViewContainer.setLayoutParams(LayoutParameters);
            mCustomViewContainer.setBackgroundResource(android.R.color.black);
            view.setLayoutParams(LayoutParameters);
            mCustomViewContainer.addView(view);
            mCustomView = view;
            mCustomViewCallback = callback;
            mCustomViewContainer.setVisibility(View.VISIBLE);
            BlogsItemActivity.this.setContentView(mCustomViewContainer);
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

    @Override
    public void onBackPressed() {

        if (mCustomViewContainer != null)

            myWebChromeClient.onHideCustomView();
        else if (wv_body.canGoBack())
            wv_body.goBack();
        else
            super.onBackPressed();
    }

    private void setScreenMetrics() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
    }

    private String formatDateTime(String time) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.UK);
        try {
            Date date = dateFormat.parse(time);

            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.UK);

            return dateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

}
