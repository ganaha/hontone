package click.enblo.hontone.util;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * HTTPクライアントクラス
 */
public class HttpClientUtil {

    // kintone API
    public static final String URL_ADD = "https://%s.cybozu.com/k/v1/record.json";
    public static final String URL_GET = "https://%s.cybozu.com/k/v1/records.json?app=%s&query=%s = \"%s\"&fields[0]=$id";

    // 書籍情報取得 API
    public static final String URL_BOOK_API = "https://app.rakuten.co.jp/services/api/BooksBook/Search/20130522?applicationId=1016615024760881133&isbn=%s";

    private static final HttpClientUtil INSTANCE = new HttpClientUtil();

    /**
     * コンストラクタ.
     */
    private HttpClientUtil() {
    }

    /**
     * インスタンスを取得
     */
    public static HttpClientUtil getInstance() {
        return INSTANCE;
    }

    /**
     * GET
     */
    public String doGet(String url, Headers headers) throws IOException {

        if (headers == null) {
            headers = new Headers.Builder().build();
        }

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .get()
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    /**
     * POST
     */
    public String doPost(String url, RequestBody body, Headers headers) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();
    }

}
