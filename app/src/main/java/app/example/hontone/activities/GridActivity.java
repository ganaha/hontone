package app.example.hontone.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import app.example.hontone.R;
import app.example.hontone.adapters.GridAdapter;
import app.example.hontone.dialogs.EulaDialogFragment;
import app.example.hontone.util.Preference;


public class GridActivity extends AppCompatActivity {

    private Preference mPref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        // Preference
        mPref = new Preference(this);

        // グリッドビュー
        GridView gridView = (GridView) findViewById(R.id.grid);
        gridView.setAdapter(new GridAdapter(this));

        // 使用許諾
        if (!mPref.hasAgreed()) {
            showEula();
        }

    }

    /**
     * 使用許諾契約書を表示する。
     */
    private void showEula() {
        DialogFragment eula = new EulaDialogFragment();
        eula.show(getFragmentManager(), "eula");
    }

    /**
     * カメラボタン押下処理
     */
    public void onClickCamera(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
