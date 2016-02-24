package com.example.abhilash.movie;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();


    private MovieDbHelper mOpenHelper;

    public static final int MOVIE = 100;
    public static final int MOVIE_WITH_SORT = 101;
    public static final int MOVIE_WITH_SORT_WITH_ID = 102;
    public static final int SORT = 200;

    private static final SQLiteQueryBuilder sMoviebySortSetiingQueryBuilder;

    static {

        sMoviebySortSetiingQueryBuilder = new SQLiteQueryBuilder();
//This is a inner join which looks like:
        // movie INNER JOIN sort on movie.sort = sort._id
        sMoviebySortSetiingQueryBuilder.setTables(MovieContract.MovieEntry.TABLE_NAME + " INNER JOIN " +
                MovieContract.SortEntry.TABLE_NAME +
                " ON " + MovieContract.MovieEntry.TABLE_NAME +
                "." + MovieContract.MovieEntry.COLUMN_SORT_KEY +
                " = " + MovieContract.SortEntry.TABLE_NAME +
                "." + MovieContract.SortEntry._ID);
    }

    //sort setting = ? AND movie id = ?
    private static final String sSortSettingWithMovieIdSelection = MovieContract.SortEntry.TABLE_NAME +
            "." + MovieContract.SortEntry.COLUMN_SORT_SETTING + " = ? AND " + MovieContract.MovieEntry.COLUMN_MOVIE_ID
            + "= ?";

    //sort setting =?
    private static final String sSortSettingSelection = MovieContract.SortEntry.TABLE_NAME +
            "." + MovieContract.SortEntry.COLUMN_SORT_SETTING + " = ? ";

    private Cursor getMovieBySortSetting(Uri uri, String[] projection, String sortOrder) {

        String SortSetting = MovieContract.MovieEntry.getSortSettingFromUri(uri);

        String selection;
        String[] selectionArgs;

        selection = sSortSettingSelection;
        selectionArgs = new String[]{SortSetting};

        return sMoviebySortSetiingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getMovieBySortSettingAndMovieID(Uri uri, String[] projection, String sortOrder) {

        String SortSetting = MovieContract.MovieEntry.getSortSettingFromUri(uri);
        String MovieId = MovieContract.MovieEntry.getMovieIdFromUri(uri);

        String selection;
        String[] selectionArgs;

        selection = sSortSettingWithMovieIdSelection;
        selectionArgs = new String[]{SortSetting, MovieId};

        return sMoviebySortSetiingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", MOVIE_WITH_SORT);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*/*", MOVIE_WITH_SORT_WITH_ID);

        matcher.addURI(authority, MovieContract.PATH_SORT, SORT);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {

            case MOVIE_WITH_SORT:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case SORT:
                return MovieContract.SortEntry.CONTENT_TYPE;
            case MOVIE_WITH_SORT_WITH_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor = null;
        //movie/
        //sort/
        //movie/id/sort
        //movie/sort
        switch (sUriMatcher.match(uri)) {

            // "movie"
            case MOVIE: {
                retCursor = mOpenHelper.getReadableDatabase().query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            // "sort"
            case SORT: {
                retCursor = mOpenHelper.getReadableDatabase().query(MovieContract.SortEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            // "movie/*/*"
            case MOVIE_WITH_SORT_WITH_ID: {
                retCursor = getMovieBySortSettingAndMovieID(uri, projection, sortOrder);
                break;
            }
            // "movie/*"
            case MOVIE_WITH_SORT: {
                retCursor = getMovieBySortSetting(uri, projection, sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case MOVIE: {
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);

                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;

            }
            case SORT: {
                long _id = db.insert(MovieContract.SortEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.SortEntry.buildSortUri(_id);

                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;

            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case MOVIE:
                rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case SORT:
                rowsDeleted = db.delete(MovieContract.SortEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_WITH_SORT_WITH_ID:
                rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME,selection,selectionArgs);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIE:
                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case SORT:
                rowsUpdated = db.update(MovieContract.SortEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}

