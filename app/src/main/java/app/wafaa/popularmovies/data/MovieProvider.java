package app.wafaa.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;


public class MovieProvider extends ContentProvider{
    public static final String LOG_TAG = MovieProvider.class.getSimpleName();

    /**
     * Codes for multiple rows in DB tables.
     */
    public static final int POP_MOVIE_CODE = 100;
    public static final int HIGH_RATED_MOVIE_CODE=200;
    public static final int FAVORITE_MOVIE_CODE=300;
//
    public static final int TRAILER_CODE = 400;
    public static final int REVIEW_CODE = 500;

    /*
     * Codes for a single row .
     */
    public static final int POP_MOVIE_W_ID_CODE = 101;
    public static final int POP_MOVIE_W_POSTER_CODE = 102;

    public static final int HIGH_RATED_W_ID_CODE=201;
    public static final int HIGH_RATED_W_POSTER_CODE=202;

    public static final int FAVORITE_MOVIE_W_ID_CODE=301;
    public static final int FAVORITE_MOVIE_W_POSTER_CODE=302;



    public static final int TRAILER_W_MOVIE_ID_CODE= 401;
    public static final int REVIEW_W_MOVIE_ID_CODE= 501;

    // Creates a UriMatcher object:
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDBHelper mOpenHelper;


    /*
     * buildUriMatcher() calls addURI() , for all of the content URI patterns that the provider
     * should recognize. Here all calls for DB tables are shown.
     */

    public static UriMatcher buildUriMatcher() {

        // Build a UriMatcher by adding a specific code to return based on a match
        // It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.AUTHORITY;

        /**
         * Sets Codes which are the integer value for multiple rows in DB tables.
         * Notice that no wildcard is used in the path
         * */

        matcher.addURI(MovieContract.AUTHORITY, MovieContract.TABLE_MOVIE_NAME, POP_MOVIE_CODE);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.TABLE_HIGH_RATING_NAME, HIGH_RATED_MOVIE_CODE);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.TABLE_FAVORITE_NAME, FAVORITE_MOVIE_CODE);

        matcher.addURI(MovieContract.AUTHORITY, MovieContract.TABLE_TRAILER_NAME, TRAILER_CODE);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.TABLE_REVIEW_NAME, REVIEW_CODE);


        /**
         * Sets Codes for a single row. In this case, the "#" and "/*" wildcard are
         * used. Eg. "content://app.wafaa.popularmovies/{table_name}/{code}" matches, but
         * "content://app.wafaa.popularmovies/{table_name}{code} doesn't.
         */
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.TABLE_MOVIE_NAME + "/#", POP_MOVIE_W_ID_CODE);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.TABLE_MOVIE_NAME + "/*", POP_MOVIE_W_POSTER_CODE);


        matcher.addURI(MovieContract.AUTHORITY, MovieContract.TABLE_HIGH_RATING_NAME + "/#", HIGH_RATED_W_ID_CODE);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.TABLE_HIGH_RATING_NAME+ "/*", HIGH_RATED_W_POSTER_CODE);

        matcher.addURI(MovieContract.AUTHORITY, MovieContract.TABLE_MOVIE_NAME + "/#", POP_MOVIE_W_ID_CODE);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.TABLE_MOVIE_NAME + "/*", POP_MOVIE_W_POSTER_CODE);

        matcher.addURI(MovieContract.AUTHORITY, MovieContract.TABLE_TRAILER_NAME + "/#", TRAILER_W_MOVIE_ID_CODE);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.TABLE_REVIEW_NAME + "/#", REVIEW_W_MOVIE_ID_CODE);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDBHelper(getContext());
        return true;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match){
            // If the incoming URI was for all of Movie table
            case POP_MOVIE_CODE:{
                return MovieContract.MovieEntry.CONTENT_DIR_TYPE;
            }
            // If the incoming URI was for a single row in Movie table
            case POP_MOVIE_W_ID_CODE:{
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            }
            case POP_MOVIE_W_POSTER_CODE:{
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            }

            case HIGH_RATED_MOVIE_CODE:{
                return MovieContract.HighRatedMovieEntry.CONTENT_DIR_TYPE;
            }
            // If the incoming URI was for a single row in Movie table
            case HIGH_RATED_W_ID_CODE:{
                return MovieContract.HighRatedMovieEntry.CONTENT_ITEM_TYPE;
            }
            case HIGH_RATED_W_POSTER_CODE:{
                return MovieContract.HighRatedMovieEntry.CONTENT_ITEM_TYPE;
            }

            case FAVORITE_MOVIE_CODE:{
                return MovieContract.FavoriteMovieEntry.CONTENT_DIR_TYPE;
            }
            // If the incoming URI was for a single row in Movie table
            case FAVORITE_MOVIE_W_ID_CODE:{
                return MovieContract.FavoriteMovieEntry.CONTENT_ITEM_TYPE;
            }
            case FAVORITE_MOVIE_W_POSTER_CODE:{
                return MovieContract.FavoriteMovieEntry.CONTENT_ITEM_TYPE;
            }

            case TRAILER_CODE:{
                return MovieContract.TrailerEntry.CONTENT_DIR_TYPE;
            }
            case TRAILER_W_MOVIE_ID_CODE:{
                return MovieContract.TrailerEntry.CONTENT_ITEM_TYPE;
            }

            case REVIEW_CODE:{
                return MovieContract.ReviewEntry.CONTENT_DIR_TYPE;
            }
            case REVIEW_W_MOVIE_ID_CODE:{
                return MovieContract.ReviewEntry.CONTENT_ITEM_TYPE;
            }
            default:{
                // error handling if the URI is not recognized.
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }


    /**
     * Inserts multiple rows into a table at the given URL.
     *
     * This function make no guarantees about the atomicity of the insertions.
     *
     * @param uri The URL of the table to insert into.
     * @param values The initial values for the newly inserted rows. The key is the column name for
     *               the field. Passing null will create an empty row.
     * @return the number of newly created rows.
     */
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            case POP_MOVIE_CODE: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.TABLE_MOVIE_NAME, null, value);
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
            }

            case HIGH_RATED_MOVIE_CODE: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.TABLE_HIGH_RATING_NAME, null, value);
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
            }


            case TRAILER_CODE:{
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.TABLE_TRAILER_NAME, null, value);
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
            }

            case REVIEW_CODE: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.TABLE_REVIEW_NAME, null, value);
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
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }

    /**
     * Inserts a row into a table at the given URL.
     *
     * If the content provider supports transactions the insertion will be atomic.
     *
     * @param uri The URL of the table to insert into.
     * @param values The initial values for the newly inserted row. The key is the column name for
     *               the field. Passing an empty ContentValues will create an empty row.
     * @return the URL of the newly created row.
     */
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            case POP_MOVIE_CODE: {
                long _id = db.insert(MovieContract.TABLE_MOVIE_NAME, null, values);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    returnUri =MovieContract.MovieEntry.buildMoviesUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            case HIGH_RATED_MOVIE_CODE: {
                long _id = db.insert(MovieContract.TABLE_HIGH_RATING_NAME, null, values);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    returnUri =MovieContract.HighRatedMovieEntry.buildHighRatedMovieUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            case FAVORITE_MOVIE_CODE: {
                long _id = db.insert(MovieContract.TABLE_FAVORITE_NAME, null, values);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    returnUri =MovieContract.FavoriteMovieEntry.buildFavoriteMovieUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }


            case TRAILER_CODE:{
                long _id = db.insert(MovieContract.TABLE_TRAILER_NAME, null, values);
                if (_id > 0) {
                    returnUri =MovieContract.TrailerEntry.buildTrailerUrl(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            case REVIEW_CODE:{
                long _id = db.insert(MovieContract.TABLE_REVIEW_NAME, null, values);
                if (_id > 0) {
                    returnUri =MovieContract.ReviewEntry.buildReviewUrl(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;

    }

    /**
     * Update row(s) in a content URI.
     *
     * If the content provider supports transactions the update will be atomic.
     *
     * @param uri The URI to modify.
     * @param values The new field values. The key is the column name for the field.
     *               A null value will remove an existing field value.
     * @param selection A filter to apply to rows before updating, formatted as an SQL WHERE clause
     *                  (excluding the WHERE itself).
     * @return the number of rows updated.
     * @throws NullPointerException if uri or values are null
     */


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsUpdated;

        switch (sUriMatcher.match(uri)) {
            case POP_MOVIE_CODE:
                rowsUpdated = db.update(
                        MovieContract.TABLE_MOVIE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;

            case HIGH_RATED_MOVIE_CODE:
                rowsUpdated = db.update(
                        MovieContract.TABLE_HIGH_RATING_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;

            case FAVORITE_MOVIE_CODE:
                rowsUpdated = db.update(
                        MovieContract.TABLE_FAVORITE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;

            case TRAILER_CODE:
                rowsUpdated = db.update(
                        MovieContract.TABLE_TRAILER_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;

            case REVIEW_CODE:
                rowsUpdated = db.update(
                        MovieContract.TABLE_REVIEW_NAME,
                        values,
                        selection,
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


    /**
     * Deletes row(s) specified by a content URI.
     *
     * If the content provider supports transactions, the deletion will be atomic.
     *
     * @param uri The URL of the row to delete.
     * @param selection A filter to apply to rows before deleting, formatted as an SQL WHERE clause
    (excluding the WHERE itself).
     * @return The number of rows deleted.
     */

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch ( sUriMatcher.match(uri)) {
            case POP_MOVIE_CODE:
                rowsDeleted = db.delete(
                        MovieContract.TABLE_MOVIE_NAME, selection, selectionArgs);
                break;

            case HIGH_RATED_MOVIE_CODE:
                rowsDeleted = db.delete(
                        MovieContract.TABLE_HIGH_RATING_NAME, selection, selectionArgs);
                break;

            case FAVORITE_MOVIE_CODE:
                rowsDeleted = db.delete(
                        MovieContract.TABLE_FAVORITE_NAME, selection, selectionArgs);
                break;

            case TRAILER_CODE:
                rowsDeleted = db.delete(
                        MovieContract.TABLE_TRAILER_NAME, selection, selectionArgs);;
                break;
            case REVIEW_CODE:
                rowsDeleted = db.delete(
                        MovieContract.TABLE_REVIEW_NAME, selection, selectionArgs);;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }


    /**
     * Implements ContentProvider.query()
     */

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.

        Cursor retCursor;
        /*
         * Choose the table to query and a sort order based on the code returned for the incoming URI.
         */
        switch (sUriMatcher.match(uri)) {
            // All Movies selected
            case POP_MOVIE_CODE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TABLE_MOVIE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            // Individual movie based on Id selected
            case POP_MOVIE_W_ID_CODE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TABLE_MOVIE_NAME,
                        projection,
                        MovieContract.MovieEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                break;
            }

            case POP_MOVIE_W_POSTER_CODE: {
                Log.d(LOG_TAG, "poster url: " + uri.getPathSegments().get(1));
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TABLE_MOVIE_NAME,
                        projection,
                        MovieContract.COL_POSTER_PATH + " = ?",
                        new String[]{uri.getPathSegments().get(1)},
                        null,
                        null,
                        sortOrder);
                break;
            }

            case HIGH_RATED_MOVIE_CODE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TABLE_HIGH_RATING_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            // Individual movie based on Id selected
            case HIGH_RATED_W_ID_CODE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TABLE_HIGH_RATING_NAME,
                        projection,
                        MovieContract.HighRatedMovieEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                break;
            }

            case HIGH_RATED_W_POSTER_CODE: {
                Log.d(LOG_TAG, "poster url: " + uri.getPathSegments().get(1));
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TABLE_HIGH_RATING_NAME,
                        projection,
                        MovieContract.COL_POSTER_PATH + " = ?",
                        new String[]{uri.getPathSegments().get(1)},
                        null,
                        null,
                        sortOrder);
                break;
            }

            case FAVORITE_MOVIE_CODE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TABLE_FAVORITE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            // Individual movie based on Id selected
            case FAVORITE_MOVIE_W_ID_CODE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TABLE_FAVORITE_NAME,
                        projection,
                        MovieContract.FavoriteMovieEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                break;
            }

            case FAVORITE_MOVIE_W_POSTER_CODE: {
                Log.d(LOG_TAG, "poster url: " + uri.getPathSegments().get(1));
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TABLE_FAVORITE_NAME,
                        projection,
                        MovieContract.COL_POSTER_PATH + " = ?",
                        new String[]{uri.getPathSegments().get(1)},
                        null,
                        null,
                        sortOrder);
                break;
            }

            case TRAILER_CODE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TABLE_TRAILER_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case TRAILER_W_MOVIE_ID_CODE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TABLE_TRAILER_NAME,
                        projection,
                        MovieContract.COL_MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                break;
            }

            case REVIEW_CODE:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TABLE_REVIEW_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case REVIEW_W_MOVIE_ID_CODE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TABLE_REVIEW_NAME,
                        projection,
                        MovieContract.COL_MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                break;
            }

            default: {
                // error handling if the URI is not recognized.
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }
}
