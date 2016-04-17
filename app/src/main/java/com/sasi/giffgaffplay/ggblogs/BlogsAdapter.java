package com.sasi.giffgaffplay.ggblogs;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sasi.giffgaffplay.R;
import com.sasi.giffgaffplay.data.BlogContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sasikumarlakshmanan on 13/04/16.
 */
public class BlogsAdapter extends CursorRecyclerViewAdapter<BlogsAdapter.ViewHolder> {

    private static final String TAG = "BlogsAdapter";
    private static Context mContext;
    private View mEmptyView;
    private Cursor mCursor;

    public static final String LABEL_MOBILE_NEWS = "MOBILE-NEWS";

    public static final String LABEL_GIFFGAFF_NEWS = "GIFFGAFF-NEWS";
    public static final String LABEL_GIFFGAFF_NEWS_NEWSLETTER = "NEWS-NEWSLETTER";
    public static final String LABEL_GIFFGAFF_NEWS_SERVICE_UPDATES = "NEWS-SERVICE-UPDATES";
    public static final String LABEL_GIFFGAFF_NEWS_LATEST = "NEWS-LATEST-NEWS";
    public static final String LABEL_GIFFGAFF_NEWS_COMPETITIONS = "NEWS-COMPETITIONS";

    public static final String LABEL_PHONE_REVIEWS = "PHONE-REVIEWS";
    public static final String LABEL_PHONE_REVIEWS_ANDROID = "PHONE-ANDROID";
    public static final String LABEL_PHONE_REVIEWS_IPHONE = "PHONE-IPHONE";
    public static final String LABEL_PHONE_REVIEWS_WINDOWS = "PHONE-WINDOWS";
    public static final String LABEL_PHONE_REVIEWS_BLACKBERRY = "PHONE-BLACKBERRY";
    public static final String LABEL_PHONE_REVIEWS_TABLETS = "PHONE-TABLETS";
    public static final String LABEL_PHONE_REVIEWS_COMPARISON = "PHONE-COMPARISON";
    public static final String LABEL_PHONE_REVIEWS_TIPS = "PHONE-TIPS";
    public static final String LABEL_PHONE_REVIEWS_ACCESSORIES = "PHONE-ACCESSORIES";

    public static final String LABEL_APP_REVIEWS = "APP-REVIEWS";
    public static final String LABEL_APP_REVIEWS_ANDROID = "APP-ANDROID";
    public static final String LABEL_APP_REVIEWS_IPHONE = "APP-IPHONE";
    public static final String LABEL_APP_REVIEWS_WINDOWS = "APP-WINDOWS";
    public static final String LABEL_APP_REVIEWS_BLACKBERRY = "APP-BLACKBERRY";
    public static final String LABEL_APP_REVIEWS_TABLETS = "APP-TABLETS";


    public static final String LABEL_JUST_FOR_FUN = "JUST-FOR-FUN";
    public static final String LABEL_JUST_FOR_FUN_5_A_DAY = "FUN-5-A-DAY";
    public static final String LABEL_JUST_FOR_FUN_FROM_THE_COMMUNITY = "FUN-FROM-THE-COMMUNITY";
    public static final String LABEL_JUST_FOR_FUN_STAFF_POSTS = "FUN-STAFF-POSTS";
    public static final String LABEL_JUST_FOR_FUN_OTHER_FUN_STUFF = "FUN-OTHER-FUN-STUFF";
    public static final String LABEL_JUST_FOR_FUN_FRIDAY_POLLS = "FUN-FRIDAY-POLLS";

    public static final String LABEL_PHONE_UNLOCKING = "PHONE-UNLOCKING";
    public static final String LABEL_PHONE_UNLOCKING_ANDROID = "UNLOCKING-ANDROID";
    public static final String LABEL_PHONE_UNLOCKING_IPHONE = "UNLOCKING-IPHONE";
    public static final String LABEL_PHONE_UNLOCKING_WINDOWS = "UNLOCKING-WINDOWS";
    public static final String LABEL_PHONE_UNLOCKING_BLACKBERRY = "UNLOCKING-BLACKBERRY";
    public static final String LABEL_PHONE_UNLOCKING_TABLETS = "UNLOCKING-TABLETS";

    HandleClickInterface mHandleClickInterface;

    private boolean temp_blog_avatar = true;
    private boolean temp_color_mode = false;

    public static interface HandleClickInterface {
        void onClick(int blog_id);
    }

    public BlogsAdapter(Context context, Cursor cursor, View emptyView, HandleClickInterface handleClickInterface) {
        super(context, cursor);
        mContext = context;
        this.mEmptyView = emptyView;
        mHandleClickInterface = handleClickInterface;
        mCursor = cursor;
    }


    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #//onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #//onBindViewHolder(ViewHolder, int)
     */
    @Override
    public BlogsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_item_card_view, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(final BlogsAdapter.ViewHolder viewHolder, Cursor cursor) {

        int blog_id = cursor.getInt(cursor.getColumnIndex(BlogContract.BlogEntry.COLUMN_BLOG_ID));
        String label = cursor.getString(cursor.getColumnIndex(BlogContract.BlogEntry.COLUMN_LABEL));
        String subject = cursor.getString(cursor.getColumnIndex(BlogContract.BlogEntry.COLUMN_SUBJECT));
        String teaser = cursor.getString(cursor.getColumnIndex(BlogContract.BlogEntry.COLUMN_TEASER));
        int views = cursor.getInt(cursor.getColumnIndex(BlogContract.BlogEntry.COLUMN_VIEWS));
        String author = cursor.getString(cursor.getColumnIndex(BlogContract.BlogEntry.COLUMN_AUTHOR));
        int kudos = cursor.getInt(cursor.getColumnIndex(BlogContract.BlogEntry.COLUMN_KUDOS));
        String time = cursor.getString(cursor.getColumnIndex(BlogContract.BlogEntry.COLUMN_POST_TIME));

        // Set the color before formatting the label.
        // THIS IS CAUSING A BIT LAG.
        int colorRes = getMatchingColor(label.toUpperCase());
        int colorVal = ContextCompat.getColor(mContext, colorRes);

        viewHolder.tv_label.setBackgroundColor(colorVal);
        viewHolder.vBorderTop.setBackgroundColor(colorVal);

        // Capitalize and replace "-" with " "
        label = label.replace("-", " ").toUpperCase();

        viewHolder.tv_label.setText(label);

        viewHolder.tv_subject.setText(Html.fromHtml(subject));
        viewHolder.tv_teaser.setText(Html.fromHtml(teaser));
        viewHolder.tv_views.setText(views + " Views");

        //        viewHolder.wv_teaser.loadData("<style>img{display: inline;height: auto;max-width: 100%;}</style>"
//                + parseTeaser(teaser), "text/html; charset=UTF-8", null);

        viewHolder.tv_author.setText(author);

        if (temp_blog_avatar) {
            Glide.with(mContext).load(cursor.getString(cursor.getColumnIndex(BlogContract.BlogEntry.COLUMN_AUTHOR_URL)))
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>(48, 48) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            viewHolder.tv_author.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(mContext.getResources(), resource), null, null, null);
                        }
                    });
        } else {
            viewHolder.tv_author.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources()
                    .getDrawable(R.drawable.ic_account_circle_black_24dp), null, null, null);
        }

        if (temp_color_mode) {
            viewHolder.card_view.setCardBackgroundColor(colorVal);
        } else {
            viewHolder.card_view.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        }

        viewHolder.tv_kudos.setText(kudos + " Kudos");
        viewHolder.tv_time.setText(formatDateTime(time));
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

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        tempSetBool();
        mCursor = newCursor;
        Cursor c = super.swapCursor(newCursor);

        if (mEmptyView != null) {
            mEmptyView.setVisibility((getItemCount() == 0) ? View.VISIBLE : View.GONE);
        }

        return c;
    }

    public void tempSetBool() {
        temp_blog_avatar = BlogsActivity.getMyBoolVal("BLOG_AVATAR", mContext);
        temp_color_mode = BlogsActivity.getMyBoolVal("BLOG_COLOR_MODE", mContext);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final LinearLayout ll_parent_card_view;
        public final LinearLayout ll_card_view;
        public final CardView card_view;
        public final TextView tv_label;
        public final TextView tv_subject;
        public final TextView tv_teaser;
        public final TextView tv_views;
        public final TextView tv_author;
        public final TextView tv_kudos;
        public final TextView tv_time;
        public final View vBorderTop;
//        public final WebView wv_teaser;

        public ViewHolder(View itemView) {
            super(itemView);
            this.ll_parent_card_view = (LinearLayout) itemView.findViewById(R.id.ll_parent_card_view);
            this.ll_card_view = (LinearLayout) itemView.findViewById(R.id.ll_card_view);
            this.card_view = (CardView) itemView.findViewById(R.id.card_view);
            this.tv_label = (TextView) itemView.findViewById(R.id.tv_label);
            this.tv_subject = (TextView) itemView.findViewById(R.id.tv_subject);
            this.tv_teaser = (TextView) itemView.findViewById(R.id.tv_teaser);
            this.tv_views = (TextView) itemView.findViewById(R.id.tv_views);
            this.tv_author = (TextView) itemView.findViewById(R.id.tv_author);
            this.tv_kudos = (TextView) itemView.findViewById(R.id.tv_kudos);
            this.tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            this.vBorderTop = itemView.findViewById(R.id.vBorderTop);
//            this.wv_teaser = (WebView) itemView.findViewById(R.id.wv_teaser);

            itemView.setOnClickListener(this);
        }


        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {

            Log.d(TAG, "ON CLICK !!!!");

            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int blog_id = mCursor.getInt(mCursor.getColumnIndex(BlogContract.BlogEntry.COLUMN_BLOG_ID));

            mHandleClickInterface.onClick(blog_id);
        }
    }

    private int getMatchingColor(String label) {

        int colorRes;

        switch (label) {
            case LABEL_MOBILE_NEWS:
                colorRes = R.color.blog_label_mobile_news;
                break;

            case LABEL_GIFFGAFF_NEWS:
            case LABEL_GIFFGAFF_NEWS_NEWSLETTER:
            case LABEL_GIFFGAFF_NEWS_SERVICE_UPDATES:
            case LABEL_GIFFGAFF_NEWS_LATEST:
            case LABEL_GIFFGAFF_NEWS_COMPETITIONS:
                colorRes = R.color.blog_label_giffgaff_news;
                break;

            case LABEL_PHONE_REVIEWS:
            case LABEL_PHONE_REVIEWS_ANDROID:
            case LABEL_PHONE_REVIEWS_IPHONE:
            case LABEL_PHONE_REVIEWS_WINDOWS:
            case LABEL_PHONE_REVIEWS_BLACKBERRY:
            case LABEL_PHONE_REVIEWS_TABLETS:
            case LABEL_PHONE_REVIEWS_COMPARISON:
            case LABEL_PHONE_REVIEWS_TIPS:
            case LABEL_PHONE_REVIEWS_ACCESSORIES:
                colorRes = R.color.blog_label_phone_reviews;
                break;

            case LABEL_APP_REVIEWS:
            case LABEL_APP_REVIEWS_ANDROID:
            case LABEL_APP_REVIEWS_IPHONE:
            case LABEL_APP_REVIEWS_WINDOWS:
            case LABEL_APP_REVIEWS_BLACKBERRY:
            case LABEL_APP_REVIEWS_TABLETS:
                colorRes = R.color.blog_label_app_reviews;
                break;

            case LABEL_JUST_FOR_FUN:
            case LABEL_JUST_FOR_FUN_5_A_DAY:
            case LABEL_JUST_FOR_FUN_FROM_THE_COMMUNITY:
            case LABEL_JUST_FOR_FUN_STAFF_POSTS:
            case LABEL_JUST_FOR_FUN_OTHER_FUN_STUFF:
            case LABEL_JUST_FOR_FUN_FRIDAY_POLLS:
                colorRes = R.color.blog_label_just_for_fun;
                break;

            case LABEL_PHONE_UNLOCKING:
            case LABEL_PHONE_UNLOCKING_ANDROID:
            case LABEL_PHONE_UNLOCKING_IPHONE:
            case LABEL_PHONE_UNLOCKING_WINDOWS:
            case LABEL_PHONE_UNLOCKING_BLACKBERRY:
            case LABEL_PHONE_UNLOCKING_TABLETS:
                colorRes = R.color.blog_label_phone_unlocking;
                break;

            default:
                colorRes = R.color.blog_label_default;
        }

        return colorRes;
    }
}
