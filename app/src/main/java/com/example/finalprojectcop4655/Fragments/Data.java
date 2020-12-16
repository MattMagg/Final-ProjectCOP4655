package com.example.finalprojectcop4655.Fragments;

import android.os.Parcel;
import android.os.Parcelable;


public class Data implements Parcelable {
    public String mTitle;
    public String mType;
    public String mHeaderTitle;
    public String mOccurence;
    public String mId;
    public String mReview;
    public String mImage_url;


    public Data(String title,String type,String id,String review,String image_url){
        this.mTitle = title;
        this.mType = type;
        this.mId = id;
        this.mImage_url = image_url;
        this.mReview = review;
    }

    public Data(String type,String headerTitle,String occurence){
        this.mType = type;
        this.mHeaderTitle = headerTitle;
        this.mOccurence = occurence;
    }

    public Data(Parcel in) {
        mTitle = in.readString();
        mType = in.readString();
        mHeaderTitle = in.readString();
        mOccurence = in.readString();
        mId = in.readString();
        mReview = in.readString();
        mImage_url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mTitle);
        out.writeString(mType);
        out.writeString(mHeaderTitle);
        out.writeString(mOccurence);
        out.writeString(mId);
        out.writeString(mReview);
        out.writeString(mImage_url);
    }

    public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return new Data[size];
        }
    };


    public Data() {
    }

}
