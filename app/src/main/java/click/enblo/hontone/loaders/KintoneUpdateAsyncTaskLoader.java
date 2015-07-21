package click.enblo.hontone.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import click.enblo.hontone.R;
import click.enblo.hontone.dto.ItemDto;
import click.enblo.hontone.dto.kintone.Record;
import click.enblo.hontone.exception.LimitException;
import click.enblo.hontone.models.KintoneModel;
import click.enblo.hontone.util.Preference;

/**
 * kintone API Request Async Task Loader.
 */
public class KintoneUpdateAsyncTaskLoader extends AsyncTaskLoader<String> {

    private static final String TAG = "KintoneUpdateAsyncTaskLoader";

    private Context mContext;

    public KintoneUpdateAsyncTaskLoader(Context context) {
        super(context);

        mContext = context;
    }

    /**
     */
    @Override
    public String loadInBackground() {

        Preference pref = new Preference(mContext);

        // アップロード件数
        int uploadCount = pref.getUploadCount();

        String res = null;
        try {

            KintoneModel model = new KintoneModel(mContext);

            // アップロード制限数内かどうかチェックする
            model.checkLimit(mContext, uploadCount);

            // リクエスト対象の書籍情報を取得
            List<Record> records = getUploadBooks();

            // チェック処理
            String errMsg = checkRecord(records);
            if (errMsg != null) return errMsg;

            for (Record record : records) {

                // アップロード制限数内かどうかチェックする
                model.checkLimit(mContext, uploadCount);

                // json
                String json = model.createRequestJson(record);
                // Insert
                res = model.addRecord(json);

                JsonObject insertResponseObject = new Gson().fromJson(res, JsonObject.class);
                String id = insertResponseObject.get(KintoneModel.RESPONSE_KINTONE_ID).getAsString();

                // 取得したIDを登録
                pref.updateKintoneId(record.isbn.value, id);
                res = mContext.getString(R.string.uploaded);

                // アップロード件数をカウントアップ
                uploadCount++;
            }
        } catch (LimitException e) {
            // 制限数エラー
            res = mContext.getString(R.string.limit_exceeded);
        } catch (Exception e) {
            e.printStackTrace();
            res = e.getMessage();
            if (res.length() > 40) {
                res = res.substring(0, 40) + "...";
            }
        } finally {
            // アップロード数を保存
            pref.saveUploadCount(uploadCount);
        }

        return res;
    }

    private String checkRecord(List<Record> records) {
        // 0件チェック
        if (records.size() == 0) {
            return mContext.getString(R.string.books_not_found);
        }
        return null;
    }

    /**
     * リクエスト対象の書籍情報を取得する。
     */
    private List<Record> getUploadBooks() throws Exception {

        // 登録した書籍情報を取得
        Preference pref = new Preference(mContext);
        List<ItemDto> books = pref.getBooks();

        // kintone IDのないものだけ抽出
        List<ItemDto> filtered = new ArrayList<>();
        for (ItemDto dto : books) {
            if (dto.kintoneId == null) {
                filtered.add(dto);
            }
        }

        KintoneModel model = new KintoneModel(mContext);

        // 詰め替え
        List<Record> records = new ArrayList<>();
        for (ItemDto dto : filtered) {
            Record record = model.createRecord(dto);
            records.add(record);
        }

        return records;
    }

}
