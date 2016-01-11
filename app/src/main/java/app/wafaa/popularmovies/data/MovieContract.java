package app.wafaa.popularmovies.data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the Movies database.
 */
public class MovieContract {

//    // All URIs share these parts
//    public static final String AUTHORITY = "com.example.providerexample.provider";
//    public static final String SCHEME = "content://";
//
//    // URIs
//    // Used for all persons
//    public static final String PERSONS = SCHEME + AUTHORITY + "/person";
//    public static final Uri URI_PERSONS = Uri.parse(PERSONS);
//    // Used for a single person, just add the id to the end
//    public static final String PERSON_BASE = PERSONS + "/";


    /**
     * All URIs share these parts
     */

    // The "Authority" is a name for the entire content provider.
    public static final String AUTHORITY = "app.wafaa.popularmovies";
    public static final String SCHEME = "content://";
    // Use AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);

    /**
     * Database table names:
     */

    /**
     * Possible paths (appended to base content URI for possible URI's)
     * content://app.wafaa.popularmovies/movies/ is a valid path for
     */

    // SQL convention says Table name should be "singular", so not movies
    public static final String TABLE_MOVIE_NAME = "movies";
    public static final String TABLE_HIGH_RATING_NAME = "high_rating";
    public static final String TABLE_FAVORITE_NAME = "favorites";

    public static final String TABLE_TRAILER_NAME="trailer";
    public static final String TABLE_REVIEW_NAME="review";



    /**
     * Database table columns:
     */


    // MOVIE table columns:
    public final static String COL_MOVIE_ID = "movie_id";
    public final static String COL_TITLE = "original_title";
    public final static String COL_RELEASE_DATE = "release_date";
    public final static String COL_POPULARITY = "popularity";
    public final static String COL_POSTER_PATH = "poster_path";
    public final static String COL_BACkDROP_PATH ="backdrop_path";
    public final static String COL_VOTE_COUNT = "vote_count";
    public final static String COL_VOTE_AVERAGE = "vote_average";
    public final static String COL_PLOT="overview";

   // public final static String COL_FAVORITE = "favorite";

    // Trailer table columns:
    public final static String COL_T_ID="trailer_id";
    public final static String COL_T_KEY="key";
    public final static String COL_T_NAME="name";

    // Review table columns:
    public final static String COL_R_ID="review_id";
    public final static String COL_R_CONTENT="content";
    public final static String COL_R_AUTHOR="author";


    /* Inner class that defines the table contents of the movie table */
    public static final class MovieEntry implements BaseColumns {
        //create content URL
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_MOVIE_NAME).build();

        //create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_MOVIE_NAME;

        //create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_MOVIE_NAME;

        //for building URL's on insertion
        public static Uri buildMoviesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMovieUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the movie table */
    public static final class HighRatedMovieEntry implements BaseColumns {
        //create content URL
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_HIGH_RATING_NAME).build();

        //create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_HIGH_RATING_NAME;

        //create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_HIGH_RATING_NAME;

        //for building URL's on insertion
        public static Uri buildHighRatedMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildHighRatedMovieUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the movie table */
    public static final class FavoriteMovieEntry implements BaseColumns {
        //create content URL
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_FAVORITE_NAME).build();

        //create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_FAVORITE_NAME;

        //create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_FAVORITE_NAME;

        //for building URL's on insertion
        public static Uri buildFavoriteMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildFavoriteMovieUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


    /* Inner class that defines the table contents of the trailer table */
    public static final class TrailerEntry implements BaseColumns {
        //create content URL
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_TRAILER_NAME).build();

        //create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_TRAILER_NAME;

        //create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_TRAILER_NAME;

        public static Uri buildTrailerUrl(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the trailer table */
    public static final class ReviewEntry implements BaseColumns {
        //create content URL
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_REVIEW_NAME).build();

        //create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" +TABLE_REVIEW_NAME;

        //create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_REVIEW_NAME;

        public static Uri buildReviewUrl(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


}


