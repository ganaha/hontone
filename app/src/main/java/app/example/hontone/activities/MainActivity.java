package app.example.hontone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import app.example.hontone.R;


public class MainActivity extends AppCompatActivity implements Runnable {

    // スプラッシュ表示時間
    private int SPLASH_WAIT_TIME_MS = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // スプラッシュ表示(続きはrun()で)
        new Handler().postDelayed(this, SPLASH_WAIT_TIME_MS);
    }

    @Override
    public void run() {
        // 既にアプリが終了されていたらキャンセル
        if (this.isFinishing()) {
            return;
        }

        Intent intent = new Intent(this, GridActivity.class);
        startActivity(intent);

        // フェード効果
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        this.finish();
    }

}
