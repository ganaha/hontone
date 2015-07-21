package click.enblo.hontone.fragments;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import click.enblo.hontone.R;
import click.enblo.hontone.activities.AppCompatPreferenceActivity;
import click.enblo.hontone.util.ColorUtil;

/**
 * Settings - Design
 */
public class DesignPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    public static final String SETTING_DESIGN_COLOR = "setting_design_color";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_design);

        // 背景色
        Preference preference = findPreference(SETTING_DESIGN_COLOR);
        preference.setOnPreferenceChangeListener(this);
        String newValue = PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), "");
        onPreferenceChange(preference, newValue);
    }

    /**
     *
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String stringValue = newValue.toString();

        ListPreference listPreference = (ListPreference) preference;
        int index = listPreference.findIndexOfValue(stringValue);

        preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

        // 色の反映
        ColorUtil.changeColor(stringValue, ((AppCompatPreferenceActivity) getActivity()).getSupportActionBar(), getActivity().getWindow());

        return true;
    }

}
