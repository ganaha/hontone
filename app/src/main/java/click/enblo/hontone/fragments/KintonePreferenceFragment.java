package click.enblo.hontone.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import click.enblo.hontone.R;
import click.enblo.hontone.listeners.OnApiTokenPreferenceChangeListener;
import click.enblo.hontone.listeners.OnAppIdPreferenceChangeListener;
import click.enblo.hontone.listeners.OnSubdomainPreferenceChangeListener;

/**
 * Settings - kintone
 */
public class KintonePreferenceFragment extends PreferenceFragment {

    public static final String KEY_KINTONE_SUBDOMAIN = "setting_kintone_subdomain";
    public static final String KEY_KINTONE_APP_ID = "setting_kintone_app_id";
    public static final String KEY_KINTONE_API_TOKEN = "setting_kintone_api_token";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_kintone);

        // Subdomain
        Preference prefSub = findPreference(KEY_KINTONE_SUBDOMAIN);
        OnSubdomainPreferenceChangeListener subdomainListener = new OnSubdomainPreferenceChangeListener(getActivity());
        prefSub.setOnPreferenceChangeListener(subdomainListener);
        String subdomain = PreferenceManager.getDefaultSharedPreferences(prefSub.getContext()).getString(prefSub.getKey(), "");
        subdomainListener.onPreferenceChange(prefSub, subdomain);

        // App ID
        Preference prefApp = findPreference(KEY_KINTONE_APP_ID);
        OnAppIdPreferenceChangeListener appIdListener = new OnAppIdPreferenceChangeListener();
        prefApp.setOnPreferenceChangeListener(appIdListener);
        String appId = PreferenceManager.getDefaultSharedPreferences(prefApp.getContext()).getString(prefApp.getKey(), "");
        appIdListener.onPreferenceChange(prefApp, appId);

        // API Token
        Preference prefApi = findPreference(KEY_KINTONE_API_TOKEN);
        OnApiTokenPreferenceChangeListener apiTokenListener = new OnApiTokenPreferenceChangeListener();
        prefApi.setOnPreferenceChangeListener(apiTokenListener);
        String apiToken = PreferenceManager.getDefaultSharedPreferences(prefApi.getContext()).getString(prefApi.getKey(), "");
        apiTokenListener.onPreferenceChange(prefApi, apiToken);
    }
}
