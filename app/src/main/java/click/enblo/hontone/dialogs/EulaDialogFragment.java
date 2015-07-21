package click.enblo.hontone.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import click.enblo.hontone.R;
import click.enblo.hontone.activities.GridActivity;
import click.enblo.hontone.util.Preference;

/**
 * 利用許諾ダイアログ
 */
public class EulaDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_eula, null, false);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setMessage(getString(R.string.dialog_eula_title))
                .setPositiveButton(getString(R.string.dialog_eula_agree), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 同意する
                        agree();
                    }
                })
                .setNegativeButton(getString(R.string.dialog_eula_not_agree), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 同意しない場合
                        closeApp();
                    }
                }).create();

        // ダイアログ外をタップして閉じないように設定
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    /**
     * 利用許諾に同意する。
     */
    private void agree() {
        Preference pref = new Preference(getActivity());
        pref.agree();
    }


    /**
     * アプリを終了させる。
     */
    private void closeApp() {
        GridActivity grid = (GridActivity) getActivity();
        grid.finish();
    }
}
