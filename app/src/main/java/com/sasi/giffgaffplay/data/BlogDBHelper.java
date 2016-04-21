package com.sasi.giffgaffplay.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sasikumarlakshmanan on 12/04/16.
 */
public class BlogDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 18;
    static final String DATABASE_NAME = "blogs.db";


    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     * @param name    of the database file, or null for an in-memory database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
     *                {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                newer, {@link #onDowngrade} will be used to downgrade the database
     */
    public BlogDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public BlogDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_BLOGS_TABLE = "CREATE TABLE " + BlogContract.BlogEntry.TABLE_NAME
                + " ("
                + BlogContract.BlogEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BlogContract.BlogEntry.COLUMN_BLOG_ID + " INTEGER UNIQUE NOT NULL, "
                + BlogContract.BlogEntry.COLUMN_POST_TIME + " TEXT NOT NULL, "
                + BlogContract.BlogEntry.COLUMN_LAST_EDIT_TIME + " TEXT, "
                + BlogContract.BlogEntry.COLUMN_LAST_EDIT_AUTHOR + " TEXT, "
                + BlogContract.BlogEntry.COLUMN_KUDOS + " INTEGER DEFAULT 0, "
                + BlogContract.BlogEntry.COLUMN_LABEL + " TEXT, "
                + BlogContract.BlogEntry.COLUMN_TEASER + " TEXT, "
                + BlogContract.BlogEntry.COLUMN_BODY + " TEXT, "
                + BlogContract.BlogEntry.COLUMN_VIEWS + " INTEGER DEFAULT 0, "
                + BlogContract.BlogEntry.COLUMN_SUBJECT + " TEXT NOT NULL, "
                + BlogContract.BlogEntry.COLUMN_AUTHOR + " TEXT NOT NULL, "
                + BlogContract.BlogEntry.COLUMN_AUTHOR_URL + " TEXT );";

        db.execSQL(SQL_CREATE_BLOGS_TABLE);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + BlogContract.BlogEntry.TABLE_NAME);
        onCreate(db);
    }
}
