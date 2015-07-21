package click.enblo.hontone.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import click.enblo.hontone.BuildConfig;
import click.enblo.hontone.R;

/**
 * このアプリについてダイアログ。
 */
public class AboutDialogFragment extends DialogFragment {

    public static final String TAG_DIALOG = "about";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_about_title)
                .setMessage(getString(R.string.dialog_about_message, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE))
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .create();

        // ダイアログ外をタップして閉じないように設定
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

}
