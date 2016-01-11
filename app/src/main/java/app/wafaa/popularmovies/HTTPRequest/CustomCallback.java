package app.wafaa.popularmovies.HTTPRequest;


public interface CustomCallback {


    /** Successful HTTP response. */
    void success(String result);

    /**
     * Unsuccessful HTTP response due to network failure, non-2XX status code, or unexpected
     * exception.
     */
    void failure(Exception error);

}
