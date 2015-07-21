package click.enblo.hontone.callbacks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.widget.GridView;
import android.widget.Toast;

import java.util.List;

import click.enblo.hontone.R;
import click.enblo.hontone.activities.GridActivity;
import click.enblo.hontone.adapters.GridAdapter;
import click.enblo.hontone.dialogs.PremiumDialogFragment;
import click.enblo.hontone.dto.ItemDto;
import click.enblo.hontone.loaders.KintoneUpdateAsyncTaskLoader;
import click.enblo.hontone.models.GridModel;
import click.enblo.hontone.util.Preference;

/**
 * kintone アップロードコールバック。
 */
public class UpdateCallback implements android.support.v4.app.LoaderManager.LoaderCallbacks<String> {

    private Context mContext;
    private GridAdapter mAdapter;
    private GridView mGridView;

    public UpdateCallback(Context context, GridAdapter adapter, GridView gridView) {
        mContext = context;
        mAdapter = adapter;
        mGridView = gridView;
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {

        // ローダー生成
        KintoneUpdateAsyncTaskLoader loader = new KintoneUpdateAsyncTaskLoader(mContext);

        // ローダー開始
        loader.forceLoad();

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        // 書籍情報を再読み込み
        Preference pref = new Preference(mContext);
        List<ItemDto> books = pref.getBooks();
        mAdapter = new GridAdapter(mContext, books);
        mGridView.setAdapter(mAdapter);

        // 更新
        GridModel.updateGridView(mGridView, mAdapter);

        // 結果
        if (data.equals(mContext.getString(R.string.limit_exceeded))) {
            PremiumDialogFragment dialog = new PremiumDialogFragment();
            dialog.show(((GridActivity) mContext).getFragmentManager(), PremiumDialogFragment.TAG_DIALOG);
        } else {
            Toast.makeText(mContext, data, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
