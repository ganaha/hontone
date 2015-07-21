package click.enblo.hontone.activities;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

import click.enblo.hontone.AnalyticsApplication;
import click.enblo.hontone.R;
import click.enblo.hontone.adapters.GridAdapter;
import click.enblo.hontone.adapters.MenuListAdapter;
import click.enblo.hontone.callbacks.BarcodeCallback;
import click.enblo.hontone.callbacks.UpdateCallback;
import click.enblo.hontone.dialogs.EulaDialogFragment;
import click.enblo.hontone.dto.ItemDto;
import click.enblo.hontone.listeners.MenuItemClickListener;
import click.enblo.hontone.models.GridModel;
import click.enblo.hontone.util.ColorUtil;
import click.enblo.hontone.util.Preference;

/**
 * グリッド画面。
 */
public class GridActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener, DrawerLayout.DrawerListener {

    // 書籍情報
    public List<ItemDto> mBooks;

    // Google Analytics Tracker.
    private Tracker mTracker;

    // 保存
    private Preference mPref;

    // UI
    private GridView mGridView;
    private GridAdapter mAdapter;

    private ListView mListView;

    private FloatingActionButton mFloatingActionButton;
    private ActionBarDrawerToggle mDrawerToggle;

    /**
     * 本Activityに遷移する為のインテントを生成する。
     * startActivity(GridActivity.createIntent(this, barcode));
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, GridActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        // Preference
        mPref = new Preference(this);

        /* グリッドビュー */

        // グリッドビュー
        mGridView = (GridView) findViewById(R.id.grid);

        // カメラボタン
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.btn_camera);

        // 長押しリスナー登録
        mGridView.setOnItemLongClickListener(this);

        // 書籍情報
        mBooks = mPref.getBooks();

        // アダプター
        mAdapter = new GridAdapter(this, mBooks);
        mGridView.setAdapter(mAdapter);

        // 更新
        GridModel.updateGridView(mGridView, mAdapter);

        // 使用許諾
        if (!mPref.hasAgreed()) {
            showEula();
        }

        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(false);
        }

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(this);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        /* リストビュー */

        // リストビュー（サイドメニュー）
        mListView = (ListView) findViewById(R.id.side_menu);

        // リストビューアダプター
        mListView.setAdapter(new MenuListAdapter(this, R.layout.menu_item, MenuListAdapter.createMenuItems(this)));

        // クリックリスナー
        mListView.setOnItemClickListener(new MenuItemClickListener(this, mDrawerLayout, mListView));

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Google Analytics
        mTracker.setScreenName(getString(R.string.ga_screen_grid));
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        // 設定画面で登録した色を取得
        String color = mPref.getColor();
        updateColor(color);
    }

    /**
     * 色を反映させる。
     */
    private void updateColor(String color) {

        int iColor = Color.parseColor(color);

        // カメラボタンの背景色を設定
        mFloatingActionButton.setBackgroundTintList(ColorStateList.valueOf(iColor));

        // サイドメニュー
        mListView.setBackgroundColor(iColor);

        // 色の設定
        ColorUtil.changeColor(color, getSupportActionBar(), getWindow());
    }

    /**
     * 使用許諾契約書を表示する。
     */
    private void showEula() {
        String TAG_EULA = "eula";
        Fragment fragment = getFragmentManager().findFragmentByTag(TAG_EULA);
        if (fragment != null) {
            // 回転等で複数立ち上がらないようにする。
            return;
        }
        DialogFragment eula = new EulaDialogFragment();
        eula.show(getFragmentManager(), TAG_EULA);
    }

    /**
     * カメラボタン押下処理
     */
    public void onClickCamera(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, CameraActivity.REQUEST_CODE_CAMERA);
    }

    /**
     * カメラから取得したバーコードを元に、APIリクエストを投げる。
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CameraActivity.REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String barcode = bundle.getString(CameraActivity.KEY_CAMERA_BARCODE);
            requestBookApi(barcode);
        }
    }

    /**
     * 取得したバーコードを元にAPIリクエスト
     */
    private void requestBookApi(String isbn) {

        // Loader呼び出し
        Bundle args = new Bundle();
        args.putString(BarcodeCallback.ARGS_ISBN, isbn);

        // 1. onCreateLoader を実行
        getSupportLoaderManager().restartLoader(0, args, new BarcodeCallback(this, mGridView, mAdapter, mBooks));
    }

    //region メニュー制御

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState(); // アクティビティの状態とActionBarDrawerToggleの状態をsyncさせるため
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_grid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_upload:
                requestKintoneApi();
                break;
//            case R.id.action_clear:
//                // デバッグ用
//                Preference pref = new Preference(this);
//                pref.resetUploadFlag();
//                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //endregion

    /**
     * kintone アップロード処理。
     */
    private void requestKintoneApi() {

        // 1. onCreateLoader を実行
        getSupportLoaderManager().restartLoader(1, null, new UpdateCallback(this, mAdapter, mGridView));
    }

    /**
     * 長押し処理。
     */
    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {

        new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_remove)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 削除
                        removeItem(parent, position);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();

        return false;
    }

    /**
     * 削除する。
     */
    public void removeItem(AdapterView<?> parent, int position) {

        try {
            GridView grid = (GridView) parent;

            // Preference からも削除
            ItemDto dto = (ItemDto) grid.getItemAtPosition(position);
            Preference pref = new Preference(this);
            pref.removeBook(dto.isbn);

            // 削除
            GridAdapter adapter = (GridAdapter) grid.getAdapter();
            adapter.remove(position);

            // 更新
            GridModel.updateGridView(grid, adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        mDrawerToggle.onDrawerOpened(drawerView);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        mDrawerToggle.onDrawerClosed(drawerView);
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        mDrawerToggle.onDrawerStateChanged(newState);
    }
}
