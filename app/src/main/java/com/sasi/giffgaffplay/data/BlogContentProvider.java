package com.sasi.giffgaffplay.data;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.RemoteException;

import java.util.ArrayList;

public class BlogContentProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private BlogDBHelper mOpenHelper;

    static final int BLOG = 100;

    static UriMatcher buildUriMatcher() {
        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        final String authority = BlogContract.CONTENT_AUTHORITY;

        // For each type of Uri create a corresponding code.
        matcher.addURI(authority, BlogContract.PATH_BLOG, BLOG);

        return matcher;
    }


    public BlogContentProvider() {
    }

    @Override
    public boolean onCreate() {
        // Implement this to initialize your content provider on startup.
        mOpenHelper = new BlogDBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        // Implement this to handle requests for the MIME type of the data
        // at the given URI.

        // Use the Uri matcher to determine what kind of Uri this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case BLOG:
                return BlogContract.BlogEntry.CONTENT_TYPE;

            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Implement this to handle requests to insert a new row.

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case BLOG:

                // In case of a conflict replace it.
                long _id = 0;

                try {
                    _id = db.insertWithOnConflict(BlogContract.BlogEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_ABORT);
                } catch (SQLiteConstraintException e) {
                    db.update(BlogContract.BlogEntry.TABLE_NAME, values,
                            BlogContract.BlogEntry.COLUMN_BLOG_ID + " = " + values.get(BlogContract.BlogEntry.COLUMN_BLOG_ID), null);
                }

                if (_id > -1) {
                    returnUri = BlogContract.BlogEntry.buildBlogUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int rowsDeleted = 0;

        // This makes delete all rows.
        if (null == selection) {
            selection = "1";
        }

        switch (match) {
            case BLOG:

                rowsDeleted = db.delete(BlogContract.BlogEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // Implement this to handle requests to update one or more rows.

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int rowsUpdated = 0;

        switch (match) {

            case BLOG:

                rowsUpdated = db.update(BlogContract.BlogEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // Implement this to handle query requests from clients.

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        Cursor retCursor;

        switch (match) {

            case BLOG:

                retCursor = db.query(BlogContract.BlogEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case BLOG:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(BlogContract.BlogEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    public static int bulkUpdateAuthorURL(Context context, ContentValues[] values) {

        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (ContentValues value : values) {


            operations.add(
                    ContentProviderOperation.newUpdate(BlogContract.BlogEntry.CONTENT_URI)
                            .withSelection(BlogContract.BlogEntry.COLUMN_BLOG_ID + " = " + value.getAsInteger(BlogContract.BlogEntry.COLUMN_BLOG_ID), null)
                            .withValue(BlogContract.BlogEntry.COLUMN_AUTHOR_URL, value.getAsString(BlogContract.BlogEntry.COLUMN_AUTHOR_URL))
                            .build()
            );

//            operations.add(
//                    ContentProviderOperation.newUpdate(uri)
//                            .withSelection(selection, selectionArgs)
//                            .withValue(key, value.getAsString(BlogContract.BlogEntry.COLUMN_AUTHOR_URL))
//                            .build()
//            );
        }

        if (operations != null && operations.size() > 0) {
            try {
                ContentProviderResult[] result = context.getContentResolver().applyBatch(BlogContract.CONTENT_AUTHORITY, operations);
                return result.length;
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            }
        }

        return 0;
    }

    public static int bulkUpdateBodyData(Context context, ContentValues[] values) {

        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (ContentValues value : values) {


            operations.add(
                    ContentProviderOperation.newUpdate(BlogContract.BlogEntry.CONTENT_URI)
                            .withSelection(BlogContract.BlogEntry.COLUMN_BLOG_ID + " = " + value.getAsInteger(BlogContract.BlogEntry.COLUMN_BLOG_ID), null)
                            .withValue(BlogContract.BlogEntry.COLUMN_BODY, value.getAsString(BlogContract.BlogEntry.COLUMN_BODY))
                            .build()
            );
        }

        if (operations != null && operations.size() > 0) {
            try {
                ContentProviderResult[] result = context.getContentResolver().applyBatch(BlogContract.CONTENT_AUTHORITY, operations);
                return result.length;
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            }
        }

        return 0;
    }
}
