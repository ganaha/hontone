package click.enblo.hontone.models;

import java.io.IOException;

import click.enblo.hontone.util.HttpClientUtil;

/**
 * 楽天APIモデル。
 */
public class RakutenModel {

    public String getRecords(String isbn) throws IOException {

        // 楽天API https://app.rakuten.co.jp/services/api/BooksBook/Search/20130522?applicationId=1016615024760881133&isbn=<ISBN>
        String url = String.format(HttpClientUtil.URL_BOOK_API, isbn);

        return HttpClientUtil.getInstance().doGet(url, null);
    }
}
