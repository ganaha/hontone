package click.enblo.hontone.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import click.enblo.hontone.R;

/**
 * 有料版宣伝ダイアログ
 */
public class PremiumDialogFragment extends DialogFragment {

    public static final String TAG_DIALOG = "premium_dialog";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.premium_upgrade_title)
                .setMessage(R.string.premium_upgrade_message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Google Play 有料版への案内
                        goPremium();
                    }
                })
                .setNegativeButton(android.R.string.no, null).create();

        // ダイアログ外をタップして閉じないように設定
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    /**
     * 有料版ページへ遷移。
     */
    private void goPremium() {
        Uri uri = Uri.parse(getString(R.string.hontone_plus_url));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

}
