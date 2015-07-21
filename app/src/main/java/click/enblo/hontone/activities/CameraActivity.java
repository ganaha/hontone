package click.enblo.hontone.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import click.enblo.hontone.AnalyticsApplication;
import click.enblo.hontone.R;
import click.enblo.hontone.models.CameraModel;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback,
        Camera.AutoFocusCallback, Camera.PreviewCallback {

    public final static String KEY_CAMERA_BARCODE = "KEY_CAMERA_BARCODE";
    public final static int REQUEST_CODE_CAMERA = 1;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    private static final int MIN_PREVIEW_PIXELS = 470 * 320;
    private static final int MAX_PREVIEW_PIXELS = 1280 * 720;
    // TAG
    private final String TAG = "CameraActivity";

    // Google Analytics Tracker.
    private Tracker mTracker;

    private EditText mEditText;
    private Camera mCamera;
    private Point mPreviewSize;
    private float mPreviewWidthRatio;
    private float mPreviewHeightRatio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        // UI要素の取得
        mEditText = (EditText) findViewById(R.id.camera_edit);

        initCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Google Analytics
        mTracker.setScreenName(getString(R.string.ga_screen_camera));
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        try {
            mCamera = Camera.open(0);
            setPreviewSize();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            // パーミッションチェック
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.not_found_camera, Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "onPause");
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
            }
        } catch (IOException exception) {
            Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged");
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(mPreviewSize.x, mPreviewSize.y);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
            mCamera.autoFocus(this);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    private void initCamera() {
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(this);
    }

    private void setPreviewSize() {
        Camera.Parameters parameters = mCamera.getParameters();
        List<Size> rawPreviewSizes = parameters.getSupportedPreviewSizes();
        List<Size> supportPreviewSizes = new ArrayList<>(rawPreviewSizes);
        Collections.sort(supportPreviewSizes, new Comparator<Size>() {
            @Override
            public int compare(Size lSize, Size rSize) {
                int lPixels = lSize.width * lSize.height;
                int rPixels = rSize.width * rSize.height;
                if (rPixels < lPixels) {
                    return -1;
                }
                if (rPixels > lPixels) {
                    return 1;
                }
                return 0;
            }
        });
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        float screenAspectRatio = (float) screenWidth / (float) screenHeight;
        Point bestSize = null;
        float diff = Float.POSITIVE_INFINITY;
        for (Size supportPreviewSize : supportPreviewSizes) {
            int supportWidth = supportPreviewSize.width;
            int supportHeight = supportPreviewSize.height;
            int pixels = supportWidth * supportHeight;
            if (pixels < MIN_PREVIEW_PIXELS || pixels > MAX_PREVIEW_PIXELS) {
                continue;
            }
            boolean isPortrait = supportWidth < supportHeight;
            int previewWidth = isPortrait ? supportHeight : supportWidth;
            int previewHeight = isPortrait ? supportWidth : supportHeight;
            if (previewWidth == screenWidth && previewHeight == screenHeight) {
                mPreviewSize = new Point(supportWidth, supportHeight);
                mPreviewWidthRatio = 1;
                mPreviewHeightRatio = 1;
                return;
            }
            float aspectRatio = (float) previewWidth / (float) previewHeight;
            float newDiff = Math.abs(aspectRatio - screenAspectRatio);
            if (newDiff < diff) {
                bestSize = new Point(supportWidth, supportHeight);
                diff = newDiff;
            }
        }
        if (bestSize == null) {
            Size defaultSize = parameters.getPreviewSize();
            bestSize = new Point(defaultSize.width, defaultSize.height);
        }
        mPreviewSize = bestSize;
        mPreviewWidthRatio = (float) mPreviewSize.x / (float) screenWidth;
        mPreviewHeightRatio = (float) mPreviewSize.y / (float) screenHeight;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Log.d(TAG, "onPreviewFrame");
        Result rawResult;
        View target = findViewById(R.id.target);
        int left = (int) (target.getLeft() * mPreviewWidthRatio);
        int top = (int) (target.getTop() * mPreviewHeightRatio);
        int width = (int) (target.getWidth() * mPreviewWidthRatio);
        int height = (int) (target.getHeight() * mPreviewHeightRatio);
        PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, mPreviewSize.x,
                mPreviewSize.y, left, top, width, height, false);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        try {
            rawResult = multiFormatReader.decode(bitmap);
            // グリッド画面に戻る
            returnActivity(rawResult.getText());
        } catch (ReaderException re) {
            // バーコードの読込が上手くいかなかった場合
            Log.d(TAG, "バーコードが認識できませんでした。");
        }
        camera.autoFocus(this);
    }

    /**
     * グリッド画面に戻る。
     */
    private void returnActivity(String barcode) {
        Intent intent = new Intent();
        intent.putExtra(KEY_CAMERA_BARCODE, barcode);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        Log.d(TAG, "onAutoFocus");
        if (success) {
            mCamera.setOneShotPreviewCallback(this);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent");

        try {
            if (mCamera != null) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mCamera.autoFocus(this);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return super.onTouchEvent(event);
    }

    /**
     * ISBN手動入力決定ボタン押下処理
     */
    public void onClickIsbn(View view) {

        String isbn = mEditText.getText().toString();

        CameraModel model = new CameraModel();

        // 入力チェック
        if (!model.validIsbn(isbn)) {
            Toast.makeText(this, getText(R.string.msg_valid_isbn), Toast.LENGTH_SHORT).show();
            return;
        }

        // Grid画面に戻る
        returnActivity(isbn);
    }

}
