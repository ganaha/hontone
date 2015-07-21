package click.enblo.hontone.util;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.view.Window;

/**
 * 色関連クラス
 */
public class ColorUtil {

    /**
     * 色を反映させる。
     */
    public static void changeColor(String color, ActionBar actionBar, Window window) {

        int iColor = Color.parseColor(color);

        // アクションバー
        if (actionBar != null) {
            ColorDrawable colorDrawable = new ColorDrawable();
            colorDrawable.setColor(iColor);
            actionBar.setBackgroundDrawable(colorDrawable);
        }

        // Android5以上
        if (window != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // ステータスバー
            window.setStatusBarColor(iColor);
            // ナビゲーションバー
            window.setNavigationBarColor(iColor);
        }
    }
}
