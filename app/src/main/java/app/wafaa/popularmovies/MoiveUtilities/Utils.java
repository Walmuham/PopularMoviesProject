package app.wafaa.popularmovies.MoiveUtilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


import app.wafaa.popularmovies.R;


public class Utils {



    /**
     * URL request to fetch trailers  /movie/{id}/videos endpoint.
     */


    //   http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=[YOUR API KEY]
    /**
     * @return the URL for the most popular movies
     */
    public static String  getMoviesURL(String sort){
        Uri builtAuthoUri= Uri.parse(Constants.BASE_URL).buildUpon()
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("sort_by", sort)
                .appendQueryParameter("api_key", Constants.API_KEY)
                .build();
        return builtAuthoUri.toString();
    }

    /**
     * @return the URL to fetch trailers
     */
    public static String  getTrailersURL(String movie_id){
        Uri builtAuthoUri= Uri.parse(Constants.BASE_URL).buildUpon()
                .appendPath("movie")
                .appendPath(movie_id)
                .appendPath("videos")
                .appendQueryParameter("api_key", Constants.API_KEY)
                .build();
        return builtAuthoUri.toString();
    }

    /**
     * @return the URL to fetch reviews
     */
    public static String getReviewsURL(String movie_id){
        Uri builtAuthoUri= Uri.parse(Constants.BASE_URL).buildUpon()
                .appendPath("movie")
                .appendPath(movie_id)
                .appendPath("reviews")
                .appendQueryParameter("api_key", Constants.API_KEY)
                .build();
        return builtAuthoUri.toString();
    }


    /**
     * @return the URL to build the complete url to fetch the image using Picasso.
     */

    public static String getPosterURL(String poster_path, String poster_size){
        return Uri.parse(Constants.FETCH_IMAGE_URL).buildUpon()
                .appendPath(poster_size)
                .appendPath((poster_path).substring(1))
                .build().toString();
    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String getDate(String date) {
         final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        if (TextUtils.isEmpty(date)) return null;
        try {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DATE_FORMAT.parse(date));
        return calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH)+" "+
            (Calendar.DATE) + ", " + calendar.get(Calendar.YEAR);
        } catch (ParseException e) {
            Log.e(e.toString(), "Failed to parse release date.");
            return null;
        }
    }
}
