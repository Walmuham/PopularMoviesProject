package app.wafaa.popularmovies.Sync;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


/**
 * Define a Service that returns an IBinder for the
 * sync adapter class, allowing the sync adapter framework to call
 * onPerformSync().
 */
public class MovieSyncService extends Service{


    // Object to use as a thread-safe lock
    private static final Object movieSyncAdapterLock = new Object();

    // Storage for an instance of the sync adapter
    private static MovieSyncAdapter movieSyncAdapter = null;


    /*
   * Instantiate the sync adapter object.
   */
    @Override
    public void onCreate() {
        Log.d("MovieSyncService", "onCreate - MovieSyncService");

         /*
         * Create the sync adapter as a singleton.
         * Set the sync adapter as syncable
         * Disallow parallel syncs
         */
        synchronized (movieSyncAdapterLock) {
            if (movieSyncAdapter == null) {
                movieSyncAdapter =
                        new MovieSyncAdapter(getApplicationContext(), true);
            }
        }
    }
    /**
     * Return an object that allows the system to invoke
     * the sync adapter.
     *
     */

    @Override
    public IBinder onBind(Intent intent) {
         /*
         * Get the object that allows external processes
         * to call onPerformSync(). The object is created
         * in the base class code when the SyncAdapter
         * constructors call super()
         */
        return movieSyncAdapter.getSyncAdapterBinder();
    }

}
