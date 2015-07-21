package click.enblo.hontone.listeners;

import android.preference.Preference;

/**
 * APP ID プレファレンス変更リスナー。
 */
public class OnAppIdPreferenceChangeListener implements Preference.OnPreferenceChangeListener {

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String appId = newValue.toString();
        preference.setSummary(appId);
        return true;
    }
}
