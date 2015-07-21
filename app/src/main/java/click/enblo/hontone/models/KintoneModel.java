package click.enblo.hontone.models;

import android.content.Context;

import com.google.gson.Gson;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;

import click.enblo.hontone.BuildConfig;
import click.enblo.hontone.R;
import click.enblo.hontone.dto.ItemDto;
import click.enblo.hontone.dto.kintone.BookRequest;
import click.enblo.hontone.dto.kintone.Record;
import click.enblo.hontone.exception.LimitException;
import click.enblo.hontone.util.HttpClientUtil;
import click.enblo.hontone.util.Preference;

/**
 * kintone モデル。
 */
public class KintoneModel {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static final String FIELD_CODE_ISBN = "ISBN";

    public static final String RESPONSE_KINTONE_ID = "id";

    private String mSubDomain;
    private String mAppId;
    private String mApiToken;

    public KintoneModel(Context context) throws Exception {
        Preference pref = new Preference(context);
        mSubDomain = pref.getSubDomain();
        mAppId = pref.getAppId();
        mApiToken = pref.getApiToken();
    }

    /**
     * 取得（複数件）
     */
    public String getRecords(String isbn) throws Exception {

        String url = String.format(HttpClientUtil.URL_GET, mSubDomain, FIELD_CODE_ISBN, mAppId, isbn);

        Headers.Builder headers = new Headers.Builder();
        headers.add("X-Cybozu-API-Token", mApiToken);

        return HttpClientUtil.getInstance().doGet(url, headers.build());
    }

    /**
     * 登録（１件）
     */
    public String addRecord(String json) throws IOException {

        String url = String.format(HttpClientUtil.URL_ADD, mSubDomain);

        RequestBody body = RequestBody.create(JSON, json);

        Headers.Builder headers = new Headers.Builder();
        headers.add("X-Cybozu-API-Token", mApiToken);
        headers.add("Host", mSubDomain + ".cybozu.com:443");
        headers.add("Content-Type", "application/json");
        headers.add("X-HTTP-Method-Override", "POST");

        return HttpClientUtil.getInstance().doPost(url, body, headers.build());
    }

    /**
     * アップロード制限を超えたかどうか
     */
    public boolean isLimit(Context context, int count) {

        // 有料版は制限なし
        if (BuildConfig.FLAVOR.equals("premium")) {
            return false;
        }

        // 無料版はlimit値まで
        int limit = Integer.parseInt(context.getString(R.string.upload_count_limit));
        return limit <= count;
    }

    /**
     * アップロード制限を超えたかどうか。
     * 超えた場合、LimitException例外が発生する。
     */
    public void checkLimit(Context context, int count) throws LimitException {

        // 有料版は制限なし
        if (BuildConfig.FLAVOR.equals("premium")) {
            return;
        }

        // 無料版はlimit値まで
        if (Integer.parseInt(context.getString(R.string.upload_count_limit)) <= count) {
            throw new LimitException();
        }
    }

    /**
     * kintoneへ送信するレコードを生成する。
     */
    public Record createRecord(ItemDto dto) {
        Record record = new Record();
        record.isbn.value = dto.isbn;
        record.title.value = dto.title;
        record.author.value = dto.author;
        record.itemCaption.value = dto.itemCaption;
        record.itemPrice.value = String.valueOf(dto.itemPrice);
        record.largeImageUrl.value = dto.largeImageUrl;
        return record;
    }

    /**
     * リクエスト用のjsonデータを生成する。
     */
    public String createRequestJson(Record record) {

        BookRequest req = new BookRequest(mAppId, record);

        // obj -> json
        Gson gson = new Gson();

        return gson.toJson(req, BookRequest.class);
    }
}
