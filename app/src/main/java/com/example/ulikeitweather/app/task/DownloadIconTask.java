package com.example.ulikeitweather.app.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ulikeitweather.app.listener.OnIconDownloadedListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * Created by Asus on 19.6.2014.
 */
public class DownloadIconTask extends AsyncTask<String, Void, Bitmap>{

    private Context mContext;
    private OnIconDownloadedListener mIconDownloadedListener = null;
    private int mPictureNumber = -1;
    private String mIconUrl = "";


    public DownloadIconTask(Context context, OnIconDownloadedListener iconDownloadedListener, int iconNumber) {
        mContext = context;
        this.mContext = context;
        this.mIconDownloadedListener = iconDownloadedListener;
        mPictureNumber = iconNumber;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        return downloadBitmap(params[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(bitmap != null) {
            mIconDownloadedListener.OnIconDownloaded(bitmap, mPictureNumber);
        }
    }

    /*
    downloads a bitmap from the specified url
    @params: url from which the bitmap is to be downloaded
    @return: bitmap if the url was valid and the download was successful
     */

    private Bitmap downloadBitmap(String url) {
        final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        final HttpGet getRequest = new HttpGet(url);

        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();

                    BufferedInputStream bis = new BufferedInputStream(inputStream);

                    //Read bytes to the Buffer until there is nothing more to read(-1).
                    ByteArrayBuffer baf = new ByteArrayBuffer(5000);
                    int current;
                    while ((current = bis.read()) != -1) {
                        baf.append((byte) current);
                    }
                    //gets bitmap from the byte array
                    byte[] byteArray = baf.toByteArray();

                    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // Could provide a more explicit error message for IOException or IllegalStateException
            getRequest.abort();
            Log.w("ImageDownloader Error while retrieving bitmap from " + url, e.toString());
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return null;
    }
}
