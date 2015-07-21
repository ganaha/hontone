package click.enblo.hontone.activities;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import java.util.Arrays;
import java.util.List;

import click.enblo.hontone.R;
import click.enblo.hontone.fragments.DesignPreferenceFragment;
import click.enblo.hontone.fragments.KintonePreferenceFragment;
import click.enblo.hontone.util.ColorUtil;
import click.enblo.hontone.util.Preference;

/**
 * 設定画面
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    // 保存
    private Preference mPref;

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Preference
        mPref = new Preference(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 色の反映
        String color = mPref.getColor();
        ColorUtil.changeColor(color, getSupportActionBar(), getWindow());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {

        // 許可するフラグメント
        String[] allowFragments = {
                KintonePreferenceFragment.class.getName(),
                DesignPreferenceFragment.class.getName()
        };

        // 許可するフラグメントかチェック
        return Arrays.asList(allowFragments).contains(fragmentName);
    }

}
