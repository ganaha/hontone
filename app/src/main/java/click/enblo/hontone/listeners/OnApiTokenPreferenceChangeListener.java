package click.enblo.hontone.listeners;

import android.preference.Preference;

/**
 * API Token プレファレンス変更リスナー。
 */
public class OnApiTokenPreferenceChangeListener implements Preference.OnPreferenceChangeListener {

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String subdomain = newValue.toString();
        preference.setSummary(subdomain);
        return true;
    }

}
