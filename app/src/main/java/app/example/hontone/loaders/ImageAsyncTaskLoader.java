package app.example.hontone.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by t.ganaha on 15/07/05.
 */
public class ImageAsyncTaskLoader extends AsyncTaskLoader<Bitmap> {

    private String mUrl;

    public ImageAsyncTaskLoader(Context context, String url) {
        super(context);

        mUrl = url;
    }

    @Override
    public Bitmap loadInBackground() {

        InputStream is = null;
        try {
            URL url = new URL(mUrl);
            is = url.openStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return BitmapFactory.decodeStream(is);
    }


}
