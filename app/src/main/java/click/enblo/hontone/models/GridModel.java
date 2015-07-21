package click.enblo.hontone.models;

import android.widget.GridView;

import click.enblo.hontone.adapters.GridAdapter;

/**
 * グリッドビューモデルクラス。
 */
public class GridModel {

    /**
     * GridViewの更新処理
     */
    public static void updateGridView(GridView grid, GridAdapter adapter) {
        adapter.notifyDataSetChanged();
        grid.invalidateViews();
    }
}
