package click.enblo.hontone.listeners;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import click.enblo.hontone.activities.IntroActivity;
import click.enblo.hontone.activities.SettingsActivity;
import click.enblo.hontone.dialogs.AboutDialogFragment;


/**
 * メニューアイテムクリックリスナー
 */
public class MenuItemClickListener implements AdapterView.OnItemClickListener {

    private Context mContext;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawer;

    public MenuItemClickListener(Context context, DrawerLayout drawerLayout, ListView drawer) {
        mContext = context;
        mDrawerLayout = drawerLayout;
        mDrawer = drawer;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case 0:
                // Settings
                Intent settings = new Intent(mContext, SettingsActivity.class);
                mContext.startActivity(settings);
                break;
            case 1:
                // Help
                Intent help = new Intent(mContext, IntroActivity.class);
                mContext.startActivity(help);
                break;
            case 2:
                // About
                DialogFragment about = new AboutDialogFragment();
                about.show(((AppCompatActivity) mContext).getFragmentManager(), AboutDialogFragment.TAG_DIALOG);
                break;
        }

        mDrawerLayout.closeDrawer(mDrawer);
    }
}
