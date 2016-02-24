package com.example.abhilash.movie;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + "( " +
                MovieContract.MovieEntry._ID + "PRIMARY KEY," +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT  ," +
                MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT  NOT NULL," +
                MovieContract.MovieEntry.COLUMN_MOVIE_POSTER + " TEXT  NOT NULL," +
                MovieContract.MovieEntry.COLUMN_ABOUT + " TEXT  NOT NULL," +
                MovieContract.MovieEntry.COLUMN_SORT_KEY + " TEXT  NOT NULL," +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT  NOT NULL," +
                MovieContract.MovieEntry.COLUMN_RATING + " TEXT  NOT NULL," +
                MovieContract.MovieEntry.COLUMN_REVIEW_CONTENT + " TEXT ," +
                MovieContract.MovieEntry.COLUMN_REVIEW_AUTHOR + " TEXT ," +
                MovieContract.MovieEntry.COLUMN_VIDEO_PATH + " TEXT  NOT NULL," +
                MovieContract.MovieEntry.COLUMN_TRAILER_NAME + " TEXT  NOT NULL," +

                " FOREIGN KEY (" + MovieContract.MovieEntry.COLUMN_SORT_KEY + ") REFERENCES " +
                MovieContract.SortEntry.TABLE_NAME + " (" + MovieContract.SortEntry._ID + ")" +
                " UNIQUE (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ", " +
                MovieContract.MovieEntry.COLUMN_SORT_KEY + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_SORT_TABLE = "CREATE TABLE " + MovieContract.SortEntry.TABLE_NAME + "(" +
                MovieContract.SortEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.SortEntry.COLUMN_SORT_SETTING + " TEXT  NOT NULL " + "); "; /* +*/
        db.execSQL(SQL_CREATE_SORT_TABLE);
        db.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.SortEntry.TABLE_NAME);
        onCreate(db);
    }
}
