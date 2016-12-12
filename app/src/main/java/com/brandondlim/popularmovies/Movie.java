package com.brandondlim.popularmovies;

public class Movie {
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
