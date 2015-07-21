package click.enblo.hontone.models;


import org.apache.commons.lang.math.NumberUtils;

/**
 * カメラモデル
 */
public class CameraModel {

    /**
     * ISBNが正常値かどうか判定する。
     */
    public boolean validIsbn(String isbn) {

        // 桁数チェック
        if (isbn.length() != 13) {
            return false;
        }

        // 接頭記号チェック
        if (!(isbn.startsWith("978") || isbn.startsWith("979"))) {
            return false;
        }

        // 数値チェック
        return NumberUtils.isNumber(isbn);

    }

}
