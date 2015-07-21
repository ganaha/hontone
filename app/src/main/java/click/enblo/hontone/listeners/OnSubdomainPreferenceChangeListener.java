package click.enblo.hontone.listeners;

import android.content.Context;
import android.preference.Preference;

import click.enblo.hontone.R;

/**
 * kintoneサブドメイン変更リスナー。
 */
public class OnSubdomainPreferenceChangeListener implements Preference.OnPreferenceChangeListener {

    private Context mContext;

    public OnSubdomainPreferenceChangeListener(Context context) {
        mContext = context;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String subdomain = newValue.toString();
        preference.setSummary(subdomain + mContext.getString(R.string.settings_kintone_domain));
        return true;
    }
}
