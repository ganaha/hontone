package click.enblo.hontone.activities;

import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import click.enblo.hontone.AnalyticsApplication;
import click.enblo.hontone.R;

public class IntroActivity extends AppIntro2 {

    // Google Analytics Tracker.
    private Tracker mTracker;

    @Override
    public void init(Bundle savedInstanceState) {

        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest

        // kintoneの設定
        addSlide(AppIntroFragment.newInstance(getString(R.string.help_step_1), getString(R.string.help_message_create_app), R.drawable.help_create_app, getColor(R.color.color_palette_red)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.help_step_2), getString(R.string.help_message_create_fields), R.drawable.help_create_fields, getColor(R.color.color_palette_pink)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.help_step_3), getString(R.string.help_message_app_id), R.drawable.help_app_code, getColor(R.color.color_palette_purple)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.help_step_4), getString(R.string.help_message_api_token), R.drawable.help_api_token, getColor(R.color.color_palette_deep_purple)));
        // アプリの設定
        addSlide(AppIntroFragment.newInstance(getString(R.string.help_step_5), getString(R.string.help_message_1), R.drawable.help_image_1, getColor(R.color.color_palette_indigo)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.help_step_6), getString(R.string.help_message_2), R.drawable.help_image_2, getColor(R.color.color_palette_blue)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.help_step_7), getString(R.string.help_message_3), R.drawable.help_image_3, getColor(R.color.color_palette_light_blue)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.help_step_8), getString(R.string.help_message_4), R.drawable.help_image_4, getColor(R.color.color_palette_cyan)));
    }

    @Override
    public void onDonePressed() {
        finish();
    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onSlideChanged() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Google Analytics
        mTracker.setScreenName(getString(R.string.ga_screen_grid));
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }
}
