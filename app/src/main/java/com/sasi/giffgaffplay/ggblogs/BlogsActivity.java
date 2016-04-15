package com.sasi.giffgaffplay.ggblogs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.Toast;

import com.sasi.giffgaffplay.R;
import com.sasi.giffgaffplay.data.BlogContract;

public class BlogsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "BlogsActivity";
    RecyclerView rv_blogs;
    BlogsAdapter mBlogsCursorAdapter;
    Cursor mCursor;

    private static final int CURSOR_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogs);

        rv_blogs = (RecyclerView) findViewById(R.id.rv_blogs);
        rv_blogs.setLayoutManager(new LinearLayoutManager(this));

        // Initiate the Loader Task (Background task)
        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        // Prepare the empty textview to be shown when no data.
        View emptyView = findViewById(R.id.tv_recyclerview_blogs_empty);

        // Create the adapter.
        mBlogsCursorAdapter = new BlogsAdapter(this, null, emptyView, new BlogsAdapter.HandleClickInterface() {
            @Override
            public void onClick(int blog_id) {
                Log.d(TAG, "BLOG ID: " + blog_id);

                Intent blogItemIntent = new Intent(BlogsActivity.this, BlogsItemActivity.class);
                blogItemIntent.putExtra("BLOG_ID", blog_id);

                startActivity(blogItemIntent);
            }
        });

        // Set the adapter
        rv_blogs.setAdapter(mBlogsCursorAdapter);

        if(savedInstanceState == null) {
            new FetchBlogsTask(this).execute();
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
                BlogContract.BlogEntry.COLUMN_TEASER, BlogContract.BlogEntry.COLUMN_VIEWS, BlogContract.BlogEntry.COLUMN_SUBJECT,
                BlogContract.BlogEntry.COLUMN_AUTHOR, BlogContract.BlogEntry.COLUMN_AUTHOR_URL
        };

        return new CursorLoader(this, uri, projection, null, null, null);
    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is <em>not</em> allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.  See {@link //FragmentManager#beginTransaction()
     * FragmentManager.openTransaction()} for further discussion on this.
     * <p>
     * <p>This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     * <p>
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

        Log.d(TAG, "onLoadFinished called");

        mBlogsCursorAdapter.swapCursor(data);
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
        mBlogsCursorAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_blog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.blog_refresh:
                setMyBoolVal("BLOG_REFRESH", true, this);
                return true;

            case R.id.blog_color:
                setMyBoolVal("BLOG_COLOR_MODE", true, this);
                return true;

            case R.id.blog_nocolor:
                setMyBoolVal("BLOG_COLOR_MODE", false, this);
                restartLoader();
                return true;

            case R.id.blog_noavatar:
                setMyBoolVal("BLOG_AVATAR", false, this);
                return true;

            case R.id.blog_avatar:
                setMyBoolVal("BLOG_AVATAR", true, this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void restartLoader() {
        getSupportLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    public static void setMyBoolVal(String key, boolean value, Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getMyBoolVal(String key, Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(key, false);
    }

    @Override
    protected void onResume() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    protected void onPause() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    /**
     * Called when a shared preference is changed, added, or removed. This
     * may be called even if a preference is set to its existing value.
     * <p>
     * <p>This callback will be run on your main thread.
     *
     * @param sharedPreferences The {@link SharedPreferences} that received
     *                          the change.
     * @param key               The key of the preference that was changed, added, or
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        switch (key) {
            case "BLOG_REFRESH":
                new FetchBlogsTask(this).execute();
                break;

            default:
                restartLoader();
        }
    }
}
