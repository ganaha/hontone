package app.example.hontone.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 保存処理ユーティリティクラス
 */
public class Preference {

    private SharedPreferences mPref = null;

    public Preference(Context context) {
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
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
        editor.commit();
    }
}
