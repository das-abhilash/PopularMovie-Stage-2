package com.example.abhilash.movie;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.AccessControlContext;
import java.util.Vector;


public class Downloadtask extends AsyncTask<String, Void, Void> {

    private final Context mContext;

    public Downloadtask(Context context) {
        this.mContext = context;
    }

    private final String LOG_TAG = Downloadtask.class.getSimpleName();

    public void getValuesFromJSONStr(String movieJsnStr, String SortSetting) {
        final String title = "original_title";
        final String poster_path = "poster_path";
        final String Synopsis = "overview";
        final String release_date = "release_date";
        final String user_rating = "vote_average";
        final String id = "id";
        final String result = "results";

        try {
            JSONObject movieJsn = new JSONObject(movieJsnStr);
            JSONArray resultArray = movieJsn.getJSONArray(result);
            Vector<ContentValues> cVVector = new Vector<ContentValues>(resultArray.length());

            for (int i = 0; i < 20; i++) {
                String Msynopsis, Mtitle, Mpath, Mrelease, Mrating, Mid;

                JSONObject c = resultArray.getJSONObject(i);
                Mtitle = c.getString(title);
                Mpath = "http://image.tmdb.org/t/p/w185/" + c.getString(poster_path);
                Msynopsis = c.getString(Synopsis);
                Mrelease = c.getString(release_date);
                Mrating = c.getString(user_rating);
                Mid = c.getString(id);

                movie m = new movie();

                m.setTitle(Mtitle);
                m.setPoster(Mpath);
                m.setSynopsis(Msynopsis);
                m.setRelease(Mrelease);
                m.setRating(Mrating);
                m.setId(Mid);

                DownloadReview(Mid, m);
                DownloadTrailer(Mid, m);


                long sortKey = Utility.getSort(SortSetting, mContext);

                ContentValues movieValues = new ContentValues();
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, Mid);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, Mtitle);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, Mpath);
                movieValues.put(MovieContract.MovieEntry.COLUMN_ABOUT, Msynopsis);
                movieValues.put(MovieContract.MovieEntry.COLUMN_SORT_KEY, sortKey);
                movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, Mrelease);
                movieValues.put(MovieContract.MovieEntry.COLUMN_RATING, Mrating + "/10");
                movieValues.put(MovieContract.MovieEntry.COLUMN_REVIEW_CONTENT, m.getContent());
                movieValues.put(MovieContract.MovieEntry.COLUMN_REVIEW_AUTHOR, m.getAuthor());
                movieValues.put(MovieContract.MovieEntry.COLUMN_TRAILER_NAME, m.getName());
                movieValues.put(MovieContract.MovieEntry.COLUMN_VIDEO_PATH, m.getKey());
                cVVector.add(movieValues);

            }

            int inserted = 0;
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = mContext.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);

            }

            Log.d(LOG_TAG, "FetchWeatherTask Complete. " + inserted + " Inserted");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieJsnStr = "";

        final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
        final String SORT_BY = "sort_by";
        final String APPID_PARAM = "api_key";

        URL url = null;

        String SortSetting = params[0];
        Uri builtUri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(SORT_BY, SortSetting + ".desc")
                .appendQueryParameter(APPID_PARAM, BuildConfig.API_KEY)
                .build();
        InputStream inputStream = null;

        try {
            url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuffer buffer = new StringBuffer();
        if (inputStream == null) {
            return null;
        }
        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (buffer.length() == 0) {

            return null;
        }

        movieJsnStr = buffer.toString();
        getValuesFromJSONStr(movieJsnStr, SortSetting);
        return null;
    }


    public void DownloadReview(String id, movie m) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String reviewJsnStr = "";


        final String BASE_URL = "http://api.themoviedb.org/3/movie/";
        final String MOVIE_ID = id;
        final String REVIEW = "reviews";
        final String APPID_PARAM = "api_key";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon().appendPath(MOVIE_ID).appendPath(REVIEW)
                .appendQueryParameter(APPID_PARAM, BuildConfig.API_KEY)
                .build();

        final String URL_review = "http://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=ef3c1c58e12e6441d1f71be6ea987a36";


        InputStream inputStream = null;

        try {
            URL url_review = new URL(URL_review);//builtUri.toString()
            urlConnection = (HttpURLConnection) url_review.openConnection();
            urlConnection.setRequestMethod("GET");

            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuffer buffer = new StringBuffer();
        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        reviewJsnStr = buffer.toString();
        final String author = "author";
        final String content = "content";
        final String result = "results";

        String Author = "";
        String Content = "";

        try {
            JSONObject reviewJsn = new JSONObject(reviewJsnStr);
            JSONArray resultArray = reviewJsn.getJSONArray(result);
            int length = resultArray.length();
            for (int i = 0; i < length; i++) {
                String Mauthor;
                String Mcontent;
                JSONObject c = resultArray.getJSONObject(i);

                Mauthor = c.getString(author);
                Mcontent = c.getString(content);

                Author = Author + "`" + Mauthor;
                Content = Content + "`" + Mcontent;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        m.setAuthor(Author);
        m.setContent(Content);
    }

    public void DownloadTrailer(String id, movie m) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String videoJsnStr = "";


        final String BASE_URL = "http://api.themoviedb.org/3/movie/";
        final String MOVIE_ID = id;
        final String REVIEW = "videos"; // trailers
        final String APPID_PARAM = "api_key";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon().appendPath(MOVIE_ID).appendPath(REVIEW)
                .appendQueryParameter(APPID_PARAM, BuildConfig.API_KEY)
                .build();

        final String URL_trailer = "http://api.themoviedb.org/3/movie/" + id + "/videos?api_key=ef3c1c58e12e6441d1f71be6ea987a36";

        InputStream inputStream = null;

        try {

            URL url_trailer = new URL(URL_trailer);  //

            urlConnection = (HttpURLConnection) url_trailer.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuffer buffer = new StringBuffer();

        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        videoJsnStr = buffer.toString();

        final String key = "key";
        final String name = "name";

        final String result = "results";
        String TName = "";
        String TKey = "";


        try {
            JSONObject videoJsn = new JSONObject(videoJsnStr);
            JSONArray resultArray = videoJsn.getJSONArray(result);
            int lenght = resultArray.length();
            for (int i = 0; i < lenght; i++) {
                String Mkey;
                String Mname;
                JSONObject c = resultArray.getJSONObject(i);

                Mkey = "https://www.youtube.com/watch?v=" + c.getString(key);
                TKey = TKey + "`" + Mkey;

                Mname = c.getString(name);
                TName = TName + "`" + Mname;
            }
            m.setName(TName);
            m.setKey(TKey);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
