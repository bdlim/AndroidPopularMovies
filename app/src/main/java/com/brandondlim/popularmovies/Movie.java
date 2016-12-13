package com.brandondlim.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String mTitle;
    private String mThumbnail;
    private String mSynopsis;
    private Double mUserRating;
    private String mReleaseDate;
    private Integer mId;

    public Movie(String title, String thumbnail, String synopsis,
                 Double userRating, String releaseDate, Integer id) {
        mTitle = title;
        mThumbnail = thumbnail;
        mSynopsis = synopsis;
        mUserRating = userRating;
        mReleaseDate = releaseDate;
        mId = id;
    }

    public Movie(Parcel in) {
        mTitle = in.readString();
        mThumbnail = in.readString();
        mSynopsis = in.readString();
        mUserRating = in.readDouble();
        mReleaseDate = in.readString();
        mId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int f) {
        dest.writeString(mTitle);
        dest.writeString(mThumbnail);
        dest.writeString(mSynopsis);
        dest.writeDouble(mUserRating);
        dest.writeString(mReleaseDate);
        dest.writeInt(mId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    static final Creator CREATOR = new Creator() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[0];
        }
    };

    public String getTitle() {
        return mTitle;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public Double getUserRating() {
        return mUserRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public Integer getId() {
        return mId;
    }

    @Override
    public String toString() {
        String movieString = new StringBuilder()
                .append("Title: " + mTitle)
                .append(", Thumbnail: " + mThumbnail)
                .append(", Synopsis: " + mSynopsis)
                .append(", User Rating: " + mUserRating)
                .append(", Release Date: " + mReleaseDate)
                .toString();

        return movieString;
    }
}
