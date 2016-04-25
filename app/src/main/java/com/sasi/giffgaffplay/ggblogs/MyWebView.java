package com.sasi.giffgaffplay.ggblogs;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

/**
 * Created by sasikumarlakshmanan on 21/04/16.
 */
public class MyWebView extends WebView {

    private MyOnScrollChangeListener onScrollChangeListener;

    /**
     * Constructs a new WebView with a Context object.
     *
     * @param context a Context object used to access application assets
     */
    public MyWebView(Context context) {
        super(context);
    }

    /**
     * Constructs a new WebView with layout parameters.
     *
     * @param context a Context object used to access application assets
     * @param attrs   an AttributeSet passed to our parent
     */
    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructs a new WebView with layout parameters and a default style.
     *
     * @param context      a Context object used to access application assets
     * @param attrs        an AttributeSet passed to our parent
     * @param defStyleAttr an attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     */
    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Constructs a new WebView with layout parameters and a default style.
     *
     * @param context      a Context object used to access application assets
     * @param attrs        an AttributeSet passed to our parent
     * @param defStyleAttr an attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     *                     the view. Can be 0 to not look for defaults.
     * @param defStyleRes  a resource identifier of a style resource that
     *                     supplies default values for the view, used only if
     *                     defStyleAttr is 0 or can not be found in the theme. Can be 0
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        Log.d("MyWebView", "In onScrollChanged");

        if (onScrollChangeListener != null) {
            onScrollChangeListener.onScrollChange(this, l, t, oldl, oldt);
        }

        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void setMyOnScrollChangeListener(MyOnScrollChangeListener onScrollChangeListener) {
        this.onScrollChangeListener = onScrollChangeListener;
    }

    public MyOnScrollChangeListener getMyOnScrollChangeListener() {
        return onScrollChangeListener;
    }

    public interface MyOnScrollChangeListener {
        /**
         * Called when the scroll position of a view changes.
         *
         * @param v          The view whose scroll position has changed.
         * @param scrollX    Current horizontal scroll origin.
         * @param scrollY    Current vertical scroll origin.
         * @param oldScrollX Previous horizontal scroll origin.
         * @param oldScrollY Previous vertical scroll origin.
         */
        void onScrollChange(WebView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }
}
