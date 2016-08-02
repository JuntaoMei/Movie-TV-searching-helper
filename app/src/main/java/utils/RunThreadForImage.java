package utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by juntao on 7/28/16.
 */
public class RunThreadForImage extends Thread{
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private final static String key = "&api_key=16f1d4452bf58e6649c066f3a9bc7b63";
    private Bitmap bitmap;
    private String image;
    {
        bitmap = null;
    }
    public RunThreadForImage(String image){
        this.image = image;
    }
    public void run() {

        StringBuilder baseUrlBuilder = new StringBuilder(IMAGE_BASE_URL);
        baseUrlBuilder.append(image);
        baseUrlBuilder.append(key);
        String imageUrl = baseUrlBuilder.toString();
        URL myFileUrl = null;
        try {
            myFileUrl = new URL(imageUrl);
            Log.w("==============", "myFileUrl: " + myFileUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
            Log.w("Bitmap: ",bitmap.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public Bitmap getRunLog(){
        return this.bitmap;
    }
}
