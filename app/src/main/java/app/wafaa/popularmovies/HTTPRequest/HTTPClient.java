package app.wafaa.popularmovies.HTTPRequest;

import android.util.Log;

import org.xml.sax.ErrorHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HTTPClient {

    /**
     * Constructor of a request which assigns the parameters and properties of the request
     * @param StrUrl string URL of request to execute
     */
    public static void HTTPRequest(String StrUrl, CustomCallback callback) {
        HttpURLConnection urlConnection = null;
        String readStream = null;
        Log.d("URL++++", StrUrl);
        try {
            URL url =new URL(StrUrl);
            urlConnection = (HttpURLConnection)url.openConnection();

            urlConnection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));//, "UTF-8"));


            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }


            readStream = buffer.toString();

            reader.close();

            int mResponseCode = urlConnection.getResponseCode();

            if (mResponseCode != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Expected 200 but got " +
                        mResponseCode + ", with body " + readStream);
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        if(readStream!=null) {
            System.out.println("result@@@@@@@-->" + readStream);
            callback.success(readStream);
        }else{
            callback.failure(new Exception("No data has returned from TmDB"));
        }
    }
}



