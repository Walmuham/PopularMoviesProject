package app.wafaa.popularmovies.MoiveUtilities;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.wafaa.popularmovies.data.MovieContract;


public class JsonParser {
    private static final String LOG_TAG =JsonParser.class.getSimpleName();

    private static final String TMDB_LIST="results";
    private static final String TMDB_ID = "id";


    /**
     * Take the String representing all movies in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     */
    public static ArrayList<String> extractMoviesDataFromJson(Context context,Uri tableUri,String Data)
            throws JSONException{

            ArrayList<String> movie_Ids=new ArrayList<>();

            List<ContentValues> contentValList = new ArrayList<>();
            JSONObject JsonObj = new JSONObject(Data);
            JSONArray JsonArr= JsonObj.getJSONArray(TMDB_LIST);

        System.out.println("JsonArr --->" + JsonArr);


        // Loop through JsonArray of Movies, add each to an instance of ContentValues
        // in the list of ContentValues
        for(int i = 0; i < JsonArr.length(); i++) {
            // Get the JSON object representing the movie
            JSONObject mInfo = JsonArr.getJSONObject(i);

            ContentValues mVal=new ContentValues();

            String movie_id=  mInfo.getString(TMDB_ID);
            movie_Ids.add(movie_id);

            //get poster urls and store them in DB:

            if(!mInfo.has(MovieContract.COL_POSTER_PATH))
            {continue;}


            mVal.put(MovieContract.COL_MOVIE_ID, movie_id);
            mVal.put(MovieContract.COL_TITLE, mInfo.getString(MovieContract.COL_TITLE));
            mVal.put(MovieContract.COL_RELEASE_DATE, mInfo.getString(MovieContract.COL_RELEASE_DATE));
            mVal.put(MovieContract.COL_POPULARITY, mInfo.getDouble(MovieContract.COL_POPULARITY));
            mVal.put(MovieContract.COL_VOTE_AVERAGE, mInfo.getDouble(MovieContract.COL_VOTE_AVERAGE));
            mVal.put(MovieContract.COL_VOTE_COUNT, mInfo.getInt(MovieContract.COL_VOTE_COUNT));
            mVal.put(MovieContract.COL_POSTER_PATH, mInfo.getString(MovieContract.COL_POSTER_PATH));
            mVal.put(MovieContract.COL_BACkDROP_PATH, mInfo.getString(MovieContract.COL_BACkDROP_PATH));
            mVal.put(MovieContract.COL_PLOT, mInfo.getString(MovieContract.COL_PLOT));

            contentValList.add(mVal);
        }

        // bulkInsert our ContentValues array


        if ( contentValList.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[contentValList.size()];
            contentValList.toArray(cvArray);
           int rowsAdd= context.getContentResolver().bulkInsert(tableUri, cvArray);

        }
        Log.d(LOG_TAG, "Sync Complete. " + contentValList.size() + " Inserted");

        return movie_Ids;
    }



    public static void extractTrailDataFromJson(Context context,String Data, String movie_id)
            throws JSONException {

        List<ContentValues> contentValList = new ArrayList<>();
        JSONObject JsonObj = new JSONObject(Data);
        JSONArray JsonArr = JsonObj.getJSONArray(TMDB_LIST);

        // Loop through JsonArray of Movies, add each to an instance of ContentValues
        // in the list of ContentValues
        for (int i = 0; i < JsonArr.length(); i++) {
            // Get the JSON object representing the movie
            JSONObject tInfo = JsonArr.getJSONObject(i);
            ContentValues tVal = new ContentValues();

            tVal.put(MovieContract.COL_MOVIE_ID, movie_id);
            tVal.put(MovieContract.COL_T_ID, tInfo.getString( TMDB_ID));
            tVal.put(MovieContract.COL_T_KEY, tInfo.getString(MovieContract.COL_T_KEY));
            tVal.put(MovieContract.COL_T_NAME, tInfo.getString(MovieContract.COL_T_NAME));

            contentValList.add(tVal);



            // add to database
            if (contentValList.size() > 0) {
                ContentValues[] cvArray = new ContentValues[contentValList.size()];
                contentValList.toArray(cvArray);
                int rowsAdd = context.getContentResolver().bulkInsert(MovieContract.TrailerEntry.CONTENT_URI, cvArray);
            }
            Log.d(LOG_TAG, "Sync Complete for Trail. " + contentValList.size() + " Inserted");

        }
    }

    public static void extractReviewDataFromJson(Context context,String Data, String movie_id)
            throws JSONException{
        List<ContentValues> contentValList = new ArrayList<>();
        JSONObject JsonObj = new JSONObject(Data);
        JSONArray JsonArr = JsonObj.getJSONArray(TMDB_LIST);


        // Loop through JsonArray of Movies, add each to an instance of ContentValues
        // in the list of ContentValues
        for (int i = 0; i < JsonArr.length(); i++) {
            // Get the JSON object representing the movie
            JSONObject vInfo = JsonArr.getJSONObject(i);
            ContentValues vVal = new ContentValues();


            vVal.put(MovieContract.COL_MOVIE_ID, movie_id);
            vVal.put(MovieContract.COL_R_ID, vInfo.getString(TMDB_ID));
            vVal.put(MovieContract.COL_R_AUTHOR, vInfo.getString(MovieContract.COL_R_AUTHOR));
            vVal.put(MovieContract.COL_R_CONTENT, vInfo.getString(MovieContract.COL_R_CONTENT));


            contentValList.add(vVal);

            // add to database
            if (contentValList.size() > 0) {
                ContentValues[] cvArray = new ContentValues[contentValList.size()];
                contentValList.toArray(cvArray);
                int rowsAdd = context.getContentResolver().bulkInsert(MovieContract.ReviewEntry.CONTENT_URI, cvArray);
            }
            Log.d(LOG_TAG, "Sync Complete for Review. " + contentValList.size() + " Inserted");

        }
    }
}
