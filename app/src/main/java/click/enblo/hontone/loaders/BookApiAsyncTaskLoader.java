package click.enblo.hontone.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;

import click.enblo.hontone.models.RakutenModel;

/**
 * HTTPリクエストローダー
 */
public class BookApiAsyncTaskLoader extends AsyncTaskLoader<String> {

    private String mIsbn;

    public BookApiAsyncTaskLoader(Context context, String isbn) {
        super(context);

        mIsbn = isbn;
    }

    @Override
    public String loadInBackground() {

        String res = null;
        try {
            RakutenModel model = new RakutenModel();
            res = model.getRecords(mIsbn);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }


}
