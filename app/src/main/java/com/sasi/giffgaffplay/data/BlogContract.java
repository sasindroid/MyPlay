package com.sasi.giffgaffplay.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by sasikumarlakshmanan on 12/04/16.
 */
public class BlogContract {

    public static final String CONTENT_AUTHORITY = "com.sasi.giffgaffplay";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_BLOG = "blog";

    public static final class BlogEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_BLOG).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                + CONTENT_AUTHORITY + "/" + PATH_BLOG;
        public static final String CONTENT_ITEM_BASE_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PATH_BLOG;

        public static final String TABLE_NAME = "blogs";

        public static final String COLUMN_BLOG_ID = "id";
        public static final String COLUMN_POST_TIME = "post_time";
        public static final String COLUMN_LAST_EDIT_TIME = "last_edit_time";
        public static final String COLUMN_LAST_EDIT_AUTHOR = "last_edit_author";
        public static final String COLUMN_KUDOS = "kudos";
        public static final String COLUMN_LABEL = "label";
        public static final String COLUMN_TEASER = "teaser";
        public static final String COLUMN_VIEWS = "views";
        public static final String COLUMN_SUBJECT = "subject";
        public static final String COLUMN_BODY = "body";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_AUTHOR_URL = "author_url";

        public static Uri buildBlogUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);

        }

    }

}
