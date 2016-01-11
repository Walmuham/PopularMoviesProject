package app.wafaa.popularmovies.Sync;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;

import app.wafaa.popularmovies.Activities.MovieActivity;
import app.wafaa.popularmovies.HTTPRequest.CustomCallback;
import app.wafaa.popularmovies.HTTPRequest.HTTPClient;
import app.wafaa.popularmovies.MoiveUtilities.Constants;
import app.wafaa.popularmovies.MoiveUtilities.JsonParser;
import app.wafaa.popularmovies.R;
import app.wafaa.popularmovies.MoiveUtilities.Utils;
import app.wafaa.popularmovies.data.MovieContract;


/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String LOG_TAG = MovieSyncAdapter.class.getSimpleName();

    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int MOVIE_NOTIFICATION_ID = 4021;


    /**
     * Set up the sync adapter
     */
    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }


    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        String SortingBy = extras.getString(Constants.SORT_TYPE_KEY);

        String MovieUri;
        final Uri Movie_Uri;

        Log.d(LOG_TAG,"SortingBy " +SortingBy );

        //fetch movie list from Api call
        if(SortingBy!= null) {
            if (SortingBy.equals(Constants.highRateSort)) {
                MovieUri = Utils.getMoviesURL(Constants.voteDecSort);
                Movie_Uri=MovieContract.HighRatedMovieEntry.CONTENT_URI;
                Log.d(LOG_TAG,"######Constants.highRateSort");

            } else {
                MovieUri = Utils.getMoviesURL(Constants.popDescSort);
                Movie_Uri=MovieContract.MovieEntry.CONTENT_URI;
                Log.d(LOG_TAG, "######Constants.popSort");
            }


            HTTPClient.HTTPRequest(MovieUri, new CustomCallback() {
                @Override
                public void success(String result) {

                    try {
                        // pares data from Json and save it in DB
                        ArrayList<String> movie_ids_List =
                                JsonParser.extractMoviesDataFromJson(getContext(),
                                        Movie_Uri, result);

                        for (final String movie_id : movie_ids_List) {
                            Log.d(LOG_TAG, "movie_id  -- >" + movie_id );

                            String trailUrl = Utils.getTrailersURL(movie_id);
                            HTTPClient.HTTPRequest(trailUrl, new CustomCallback() {
                                @Override
                                public void success(String result) {

                                    try {
                                        JsonParser.extractTrailDataFromJson(getContext(), result, movie_id);
                                    } catch (JSONException e) {
                                        Log.e(LOG_TAG, e.getMessage(), e);
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void failure(Exception error) {
                                }
                            });

                            String ReviewUrl = Utils.getReviewsURL(movie_id);
                            HTTPClient.HTTPRequest(ReviewUrl, new CustomCallback() {
                                @Override
                                public void success(String result) {
                                    try {
                                        JsonParser.extractReviewDataFromJson(getContext(), result, movie_id);
                                    } catch (JSONException e) {
                                        Log.e(LOG_TAG, e.getMessage(), e);
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void failure(Exception error) {
                                    Log.e(LOG_TAG, "No data return from TmDB");
                                }
                            });
                        }
                    } catch (JSONException e) {
                        Log.e(LOG_TAG, e.getMessage(), e);
                        e.printStackTrace();
                    }
                    notifyChange();
                }

                @Override
                public void failure(Exception error) {
                    Log.e(LOG_TAG, "No data return from TmDB");
                }
            });
        }else
            Log.e(LOG_TAG, " NO Sorting type is determined");
    }

    private boolean isLastUpdateMoreOneDay(long lastSync) {

        if (System.currentTimeMillis() - lastSync >= DAY_IN_MILLIS) {
            // Last sync was more than 1 day ago
            return true;
        }
        return false;
    }



    private void notifyChange() {
        Context context = getContext();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String displayNotificationsKey = context.getString(R.string.pref_enable_not_key);
        boolean displayNotifications = prefs.getBoolean(displayNotificationsKey,
                Boolean.parseBoolean(context.getString(R.string.pref_enable_not_default)));

        if (!displayNotifications) {
            return;
        }

        String lastNotificationKey = context.getString(R.string.pref_last_not);
        long lastSync = prefs.getLong(lastNotificationKey, 0);

        if (isLastUpdateMoreOneDay(lastSync))
        {
            int smallIcon = R.mipmap.ic_launcher;
            Bitmap largeIcon = BitmapFactory.decodeResource(
                    getContext().getResources(),
                    R.mipmap.ic_launcher);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext())
                   // .setSmallIcon(smallIcon)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(getContext().getString(R.string.app_name))
                    .setContentText(getContext().getString(R.string.not_text));

            Intent intent= new Intent(getContext(), MovieActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntent(intent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager =
                    (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(MOVIE_NOTIFICATION_ID, mBuilder.build());

            //refreshing last sync
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(lastNotificationKey, System.currentTimeMillis());
            editor.commit();

        }

    }
    ////////////////////////////////////////////////////////////////////


    public static void syncImmediately(Context context, String sortType) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putString(Constants.SORT_TYPE_KEY, sortType);
        ContentResolver.setIsSyncable(getSyncAccount(context), context.getString(R.string.content_authority), 1);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    public static Account getSyncAccount(Context context) {

        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);


        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        if ( null == accountManager.getPassword(newAccount) ) {


            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        MovieSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        Log.e(LOG_TAG, "@@@@@@@@ onAccountCreated");
        Log.e(LOG_TAG, "@@@@@@@@  SortBy " );

        syncImmediately(context,Constants.popSort);
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    public static void initializeSyncAdapter(Context context) {
        Log.e(LOG_TAG, "@@ initializeSyncAdapter");

        getSyncAccount(context);
    }

}
