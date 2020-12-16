package com.example.finalprojectcop4655.Utility;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class Common {
    public static final String TERM = "term";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String LOCATION = "location";
    public static final String CATEGORY = "categories";
    public static final String HEADER = "header";
    public static final String RESTAURANTS_ALL = "restaurants,ALL";
    public static final String SORT_BY = "sort_by";
    public static final String TYPE = "type";
    public static final String ID = "id";
    public static final String BEST_MATCH = "best_match";
    public static final String RATING = "rating";
    public static final String REVIEW_COUNT = "review_count";
    public static final String DISTANCE = "distance";
    public static final String EMPTY_VALUE = " ";
    public static final String NOT_AVAILABLE = "not available";
    public static final String RESTURANT_ID = "restaurant_id";
    public static final String LIST = "list";

    public static int converttoDp(int pixels, Context context) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, pixels, context.getResources().getDisplayMetrics()));
    }

    public static void loadImages(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .fitCenter()
                .apply(new RequestOptions().override(Common.converttoDp(400, context), Common.converttoDp(400, context)))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
    }

    public static boolean ActiveNetwork(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static void hideSoftKeyboard(Activity activity) {
        final InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (activity.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

}
