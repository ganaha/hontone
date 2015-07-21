package click.enblo.hontone.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import click.enblo.hontone.R;
import click.enblo.hontone.dto.BookDto;
import click.enblo.hontone.dto.ItemDto;
import click.enblo.hontone.dto.ItemsDto;
import click.enblo.hontone.fragments.DesignPreferenceFragment;

/**
 * 保存処理ユーティリティクラス
 */
public class Preference {

    private SharedPreferences mPref;
    private SharedPreferences mBook;
    private SharedPreferences mSetting;
    private Context mContext;

    // アップロード件数KEY
    private String PREF_UPLOAD_COUNT = "uploadCount";

    public Preference(Context context) {
        mPref = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        mBook = context.getSharedPreferences("books", Context.MODE_PRIVATE);
        mSetting = PreferenceManager.getDefaultSharedPreferences(context);
        mContext = context;
    }

    /**
     * 利用許諾に同意済みかどうかを取得する。
     */
    public boolean hasAgreed() {
        return mPref.getBoolean("eula", false);
    }

    /**
     * 利用許諾に同意する。
     */
    public void agree() {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean("eula", true);
        editor.apply();
    }

    /**
     * 書籍情報を登録する。
     */
    public void addBook(BookDto book) {

        // 書籍情報だけを抽出
        List<ItemsDto> items = book.Items;
        ItemDto item = items.get(0).Item;

        Gson gson = new Gson();

        // 保存
        SharedPreferences.Editor editor = mBook.edit();
        editor.putString(item.isbn, gson.toJson(item));
        editor.apply();
    }

    /**
     * 書籍情報を登録する。
     */
    public void addBook(ItemDto book) {

        Gson gson = new Gson();

        // 保存
        SharedPreferences.Editor editor = mBook.edit();
        editor.putString(book.isbn, gson.toJson(book));
        editor.apply();
    }

    /**
     * 書籍情報一覧を取得する。
     */
    public List<ItemDto> getBooks() {

        List<ItemDto> results = new ArrayList<>();

        Map<String, String> books = (Map<String, String>) mBook.getAll();

        Gson gson = new Gson();

        for (Map.Entry<String, String> e : books.entrySet()) {
            ItemDto dto = gson.fromJson(e.getValue(), ItemDto.class);
            results.add(dto);
        }

        return results;
    }

    /**
     * kintone ID を設定する。
     */
    public boolean updateKintoneId(String isbn, String kintoneId) throws Exception {

        // 保存
        SharedPreferences.Editor editor = mBook.edit();

        String book = mBook.getString(isbn, null);

        Gson gson = new Gson();

        ItemDto dto = gson.fromJson(book, ItemDto.class);
        dto.kintoneId = kintoneId;

        editor.putString(isbn, gson.toJson(dto));

        editor.apply();

        return true;
    }

    /**
     * 対象ISBNの書籍情報を削除する。
     */
    public void removeBook(String isbn) {
        SharedPreferences.Editor editor = mBook.edit();
        editor.remove(isbn);
        editor.apply();
    }

    /**
     * アップロードカウントを取得する。
     */
    public int getUploadCount() {
        return mPref.getInt(PREF_UPLOAD_COUNT, 0);
    }

    /**
     * アップロードカウントを登録する。
     */
    public void saveUploadCount(int uploadCount) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt(PREF_UPLOAD_COUNT, uploadCount);
        editor.apply();
    }

    /**
     * 設定画面で登録したサブドメインを取得する。
     */
    public String getSubDomain() throws Exception {
        String subdomain = mSetting.getString("setting_kintone_subdomain", null);
        if (subdomain == null) {
            throw new Exception(mContext.getString(R.string.not_found_subdomain));
        }
        return subdomain;
    }

    /**
     * 設定画面で登録したアプリIDを取得する。
     */
    public String getAppId() throws Exception {
        String appId = mSetting.getString("setting_kintone_app_id", null);
        if (appId == null) {
            throw new Exception(mContext.getString(R.string.not_found_app_id));
        }
        return appId;
    }

    /**
     * 設定画面で登録したAPIトークンを取得する。
     */
    public String getApiToken() throws Exception {
        String apiToken = mSetting.getString("setting_kintone_api_token", null);
        if (apiToken == null) {
            throw new Exception(mContext.getString(R.string.not_found_api_token));
        }
        return apiToken;
    }

    /**
     * 設定画面で登録した背景色を取得する。
     */
    public String getColor() {
        return mSetting.getString(DesignPreferenceFragment.SETTING_DESIGN_COLOR, mContext.getResources().getStringArray(R.array.settings_design_color_values)[0]);
    }

    /**
     * 保存されている書籍情報からアップロード情報を削除する。（デバッグ用）
     */
    public void resetUploadFlag() {

        // カウントリセット
        SharedPreferences.Editor pref = mPref.edit();
        pref.putInt(PREF_UPLOAD_COUNT, 0);
        pref.apply();

        List<ItemDto> books = getBooks();
        for (ItemDto dto : books) {
            dto.kintoneId = null;
            addBook(dto);
        }

    }
}
