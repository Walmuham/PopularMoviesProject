package app.wafaa.popularmovies.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import app.wafaa.popularmovies.data.MovieContract.MovieEntry;

public class MovieDBHelper extends SQLiteOpenHelper{
    public static final String LOG_TAG = MovieDBHelper.class.getSimpleName();

    private static final String DATABASE_NAME="popularmovies.db";
    private static final int DATABASE_VERSION= 6;

    public MovieDBHelper(Context context){super(context,DATABASE_NAME, null, DATABASE_VERSION);}

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.TABLE_MOVIE_NAME);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + MovieContract.TABLE_MOVIE_NAME + "'");

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.TABLE_HIGH_RATING_NAME);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + MovieContract.TABLE_HIGH_RATING_NAME + "'");

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.TABLE_FAVORITE_NAME);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + MovieContract.TABLE_FAVORITE_NAME + "'");

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.TABLE_TRAILER_NAME);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + MovieContract.TABLE_TRAILER_NAME + "'");

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.TABLE_REVIEW_NAME);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + MovieContract.TABLE_REVIEW_NAME + "'");


        // re-create database
        onCreate(sqLiteDatabase);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /**
         * The SQL code that creates a Table for storing Movies in.
         * */

        final String SQL_MOVIES_TABLE = "CREATE TABLE " + MovieContract.TABLE_MOVIE_NAME + " ( "
                + MovieEntry._ID + " INTEGER PRIMARY KEY, "
                + MovieContract.COL_MOVIE_ID + " INTEGER NOT NULL, "
                + MovieContract.COL_TITLE + " TEXT NOT NULL, "
                + MovieContract.COL_RELEASE_DATE + " TEXT NOT NULL, "
                + MovieContract.COL_POPULARITY + " REAL NOT NULL, "
                + MovieContract.COL_VOTE_AVERAGE + " REAL NOT NULL, "
                + MovieContract.COL_VOTE_COUNT + " INTEGER NOT NULL, "
                + MovieContract.COL_POSTER_PATH + " TEXT NOT NULL, "
                + MovieContract.COL_BACkDROP_PATH + " TEXT NOT NULL, "
                + MovieContract.COL_PLOT + " TEXT NOT NULL, "
                + " UNIQUE (" + MovieContract.COL_TITLE + ") ON CONFLICT REPLACE);";

        final String SQL_HIGH_RATING_TABLE = "CREATE TABLE " + MovieContract.TABLE_HIGH_RATING_NAME + " ( "
                + MovieEntry._ID + " INTEGER PRIMARY KEY, "
                + MovieContract.COL_MOVIE_ID + " INTEGER NOT NULL, "
                + MovieContract.COL_TITLE + " TEXT NOT NULL, "
                + MovieContract.COL_RELEASE_DATE + " TEXT NOT NULL, "
                + MovieContract.COL_POPULARITY + " REAL NOT NULL, "
                + MovieContract.COL_VOTE_AVERAGE + " REAL NOT NULL, "
                + MovieContract.COL_VOTE_COUNT + " INTEGER NOT NULL, "
                + MovieContract.COL_POSTER_PATH + " TEXT NOT NULL, "
                + MovieContract.COL_BACkDROP_PATH + " TEXT NOT NULL, "
                + MovieContract.COL_PLOT + " TEXT NOT NULL, "
                + " UNIQUE (" + MovieContract.COL_TITLE + ") ON CONFLICT REPLACE);";


        final String SQL_FAVORITE_TABLE = "CREATE TABLE " + MovieContract.TABLE_FAVORITE_NAME+ " ( "
                + MovieEntry._ID + " INTEGER PRIMARY KEY, "
                + MovieContract.COL_MOVIE_ID + " INTEGER NOT NULL, "
                + MovieContract.COL_TITLE + " TEXT NOT NULL, "
                + MovieContract.COL_RELEASE_DATE + " TEXT NOT NULL, "
                + MovieContract.COL_VOTE_AVERAGE + " REAL NOT NULL, "
                + MovieContract.COL_POSTER_PATH + " TEXT NOT NULL, "
                + MovieContract.COL_BACkDROP_PATH + " TEXT NOT NULL, "
                + MovieContract.COL_PLOT + " TEXT NOT NULL, "
                + " UNIQUE (" + MovieContract.COL_TITLE + ") ON CONFLICT REPLACE);";



        final String SQL_TRAIL_TABLE = "CREATE TABLE " + MovieContract.TABLE_TRAILER_NAME + " ( "
                + MovieContract.TrailerEntry._ID + " INTEGER PRIMARY KEY, "
                + MovieContract.COL_MOVIE_ID + " INTEGER NOT NULL, "
                + MovieContract.COL_T_ID + " TEXT NOT NULL, "
                + MovieContract.COL_T_KEY + " TEXT NOT NULL, "
                + MovieContract.COL_T_NAME + " TEXT NOT NULL, "
                + " FOREIGN KEY (" + MovieContract.COL_MOVIE_ID + ") REFERENCES "
                + MovieContract.TABLE_MOVIE_NAME + " (" + MovieContract.COL_MOVIE_ID + "),"
                + " FOREIGN KEY (" + MovieContract.COL_MOVIE_ID + ") REFERENCES "
                + MovieContract.TABLE_HIGH_RATING_NAME + " (" + MovieContract.COL_MOVIE_ID + "),"
                + "UNIQUE (" + MovieContract.COL_T_ID + ") ON CONFLICT REPLACE);";

        final String SQL_REVIEW_TABLE = "CREATE TABLE " + MovieContract.TABLE_REVIEW_NAME + " ( "
                + MovieContract.ReviewEntry._ID + " INTEGER PRIMARY KEY, "
                + MovieContract.COL_MOVIE_ID + " INTEGER NOT NULL, "
                + MovieContract.COL_R_ID + " TEXT NOT NULL, "
                + MovieContract.COL_R_AUTHOR + " TEXT NOT NULL, "
                + MovieContract.COL_R_CONTENT + " TEXT NOT NULL, "
                + " FOREIGN KEY (" + MovieContract.COL_MOVIE_ID + ") REFERENCES "
                + MovieContract.TABLE_MOVIE_NAME + " (" + MovieContract.COL_MOVIE_ID + "),"
                + " FOREIGN KEY (" + MovieContract.COL_MOVIE_ID + ") REFERENCES "
                + MovieContract.TABLE_HIGH_RATING_NAME + " (" + MovieContract.COL_MOVIE_ID + "),"
                + "UNIQUE (" + MovieContract.COL_R_ID + ") ON CONFLICT REPLACE);";


        sqLiteDatabase.execSQL(SQL_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_HIGH_RATING_TABLE);
        sqLiteDatabase.execSQL(SQL_FAVORITE_TABLE );
        sqLiteDatabase.execSQL(SQL_TRAIL_TABLE );
        sqLiteDatabase.execSQL(SQL_REVIEW_TABLE);
    }

}
