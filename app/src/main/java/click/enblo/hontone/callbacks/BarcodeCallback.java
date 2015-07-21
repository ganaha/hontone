package click.enblo.hontone.callbacks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;
import java.util.List;

import click.enblo.hontone.R;
import click.enblo.hontone.adapters.GridAdapter;
import click.enblo.hontone.dto.BookDto;
import click.enblo.hontone.dto.ItemDto;
import click.enblo.hontone.exception.HontoneException;
import click.enblo.hontone.loaders.BookApiAsyncTaskLoader;
import click.enblo.hontone.models.GridModel;
import click.enblo.hontone.util.Preference;

/**
 * 書籍情報の取得コールバック。
 */
public class BarcodeCallback implements LoaderManager.LoaderCallbacks<String> {

    public static final String ARGS_ISBN = "ISBN";
    private Context mContext;
    private GridView mGridView;
    private GridAdapter mGridAdapter;
    private List<ItemDto> mBooks;

    public BarcodeCallback(Context context, GridView grid, GridAdapter adapter, List<ItemDto> books) {
        mContext = context;
        mGridView = grid;
        mGridAdapter = adapter;
        mBooks = books;
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {

        // ローダー生成
        String isbn = args.getString(ARGS_ISBN);
        BookApiAsyncTaskLoader loader = new BookApiAsyncTaskLoader(mContext, isbn);

        // ローダー開始
        loader.forceLoad();

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        try {
            // JSONパース
            Gson gson = new Gson();
            BookDto book = gson.fromJson(data, BookDto.class);

            // 存在チェック
            if (book.Items.size() == 0) {
                throw new HontoneException(mContext.getString(R.string.book_not_found));
            }

            // 現在時刻を設定
            Date now = new Date();
            book.Items.get(0).Item.registered = DateFormatUtils.format(now, "yyyy/MM/dd HH:mm:ss");

            // 保存
            Preference pref = new Preference(mContext);
            pref.addBook(book);

            // adapter 追加
            List<ItemDto> books = pref.getBooks();
            mGridAdapter = new GridAdapter(mContext, books);
            mGridView.setAdapter(mGridAdapter);

            // 再描画
            GridModel.updateGridView(mGridView, mGridAdapter);

        } catch (HontoneException e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            // 失敗の場合
            Toast.makeText(mContext, R.string.oops, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
