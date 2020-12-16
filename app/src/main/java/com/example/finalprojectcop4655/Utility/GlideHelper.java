package com.example.finalprojectcop4655.Utility;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class GlideHelper {

    private static GlideHelper instance;

    public static GlideHelper getInstance(Context context){
        if(instance == null){
            instance = new GlideHelper();
        }
        return instance;
    }

    public void initialise(Context context, String url, ImageView imageView){
        Glide.with(context)
                .load(url)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
    }

}