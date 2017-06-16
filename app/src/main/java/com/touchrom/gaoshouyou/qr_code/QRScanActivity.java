package com.touchrom.gaoshouyou.qr_code;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.arialyy.frame.util.AndroidUtils;
import com.arialyy.frame.util.DensityUtils;
import com.arialyy.frame.util.show.L;
import com.dtr.zbar.build.ZBarDecoder;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.base.BaseActivity;
import com.touchrom.gaoshouyou.databinding.ActivityQrScanBinding;
import com.touchrom.gaoshouyou.entity.WebEntity;
import com.touchrom.gaoshouyou.help.DownloadHelp;
import com.touchrom.gaoshouyou.help.RegexHelp;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.util.S;
import com.touchrom.gaoshouyou.widget.TempView;

import java.io.File;
import java.io.IOException;

import butterknife.InjectView;

/**
 * Created by lk on 2016/5/5.
 * 二维码扫描界面
 */
public class QRScanActivity extends BaseActivity<ActivityQrScanBinding> {
    static final int IMG_LOAD = 2;
    @InjectView(R.id.capture_preview)
    FrameLayout mPreLayout;
    @InjectView(R.id.scan_box)
    FrameLayout mScanBox;
    @InjectView(R.id.scan_bar)
    ImageView mScanBar;
    @InjectView(R.id.light)
    Button mLightBt;

    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    private CameraManager mCameraManager;
    private Rect mCropRect = null;
    private boolean previewing = true;
    private ObjectAnimator mScanBarAnim;
    private ZBarDecoder mDecoder;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        showTempView(TempView.LOADING);
        mToolbar.setTitle("二维码扫描");
        initCamera();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startScan();
                hintTempView();
            }
        }, 1000);
    }

    private void startScan() {
        mPreLayout.removeView(mPreview);
        mPreLayout.addView(mPreview);
        mCamera.setPreviewCallback(mPreviewCb);
        mCamera.startPreview();
        mCamera.autoFocus(mAutoFocusCB);
        previewing = true;
        mCurrentTime = System.currentTimeMillis();
    }


    private void initCamera() {
        mDecoder = new ZBarDecoder();
        mCropRect = new Rect();
        autoFocusHandler = new Handler();
        mCameraManager = new CameraManager(this);
        handleLight(false);
        try {
            mCameraManager.openDriver();
        } catch (IOException e) {
            e.printStackTrace();
            finish();
        }
        mCamera = mCameraManager.getCamera();
        mPreview = new CameraPreview(this, mCamera, mPreviewCb, mAutoFocusCB);
        int startY = DensityUtils.dp2px(20);
        int endY = DensityUtils.dp2px(250);
        mScanBarAnim = ObjectAnimator.ofFloat(mScanBar, "translationY", startY, endY - (startY * 2), startY);
        mScanBarAnim.setRepeatCount(Integer.MAX_VALUE);
        mScanBarAnim.setDuration(3000);
        mScanBarAnim.setRepeatMode(ObjectAnimator.RESTART);
        mScanBarAnim.start();
    }

    Long mCurrentTime;
    private boolean isDecoding = false;
    Camera.PreviewCallback mPreviewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(final byte[] data, Camera camera) {
            if (System.currentTimeMillis() - mCurrentTime > 200 && !isDecoding) {
                isDecoding = true;
                final Camera.Size size = camera.getParameters().getPreviewSize();
                final byte[] rotatedData = new byte[data.length];
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int y = 0; y < size.height; y++) {
                            for (int x = 0; x < size.width; x++)
                                rotatedData[x * size.height + size.height - y - 1] = data[x + y * size.width];
                        }

                        // 调整宽高
                        int tmp = size.width;
                        size.width = size.height;
                        size.height = tmp;
                        initCrop();
                        String result = mDecoder.decodeCrop(rotatedData, size.width, size.height, mCropRect.left,
                                mCropRect.top, mCropRect.width(), mCropRect.height());
                        isDecoding = false;
//                        L.d(TAG, result + "");
                        mDecodHandler.obtainMessage(0, result).sendToTarget();
                    }
                }).start();
                mCurrentTime = System.currentTimeMillis();
            }
        }
    };

    private Handler mDecodHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (!TextUtils.isEmpty(result)) {
                handleResult(result);
                if (mScanBarAnim.isRunning()) {
                    mScanBarAnim.cancel();
                }
            }
        }
    };

    private Runnable mAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(mAutoFocusCB);
        }
    };

    Camera.AutoFocusCallback mAutoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(mAutoFocus, 1000);
        }
    };

    private void handleResult(String result) {
        if (TextUtils.isEmpty(result)) {
            return;
        }
        previewing = false;
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
        }
        String id = RegexHelp.getQrCodeId(result);
        if (!TextUtils.isEmpty(id)) {
            DownloadHelp.newInstance().qrGamenDownload(QRScanActivity.this, Integer.parseInt(id));
            finish();
        } else if (RegexHelp.isHtml(result)) {
            WebEntity entity = new WebEntity();
            entity.setContentUrl(result);
            TurnHelp.turnNormalWeb(QRScanActivity.this, entity);
            finish();
        } else {
            S.i(mRootView, "不能识别该二维码, 点击重试？", "好的", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startScan();
                }
            });
        }
    }

    @Override
    public void finish() {
        previewing = false;
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.cancelAutoFocus();
            mCamera.release();
            mCamera = null;
        }
        mPreLayout.removeView(mPreview);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                QRScanActivity.super.finish();
            }
        }, 100);
    }

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = mCameraManager.getCameraResolution().y;
        int cameraHeight = mCameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        mScanBox.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - AndroidUtils.getStatusBarHeight(this);

        int cropWidth = mScanBox.getWidth();
        int cropHeight = mScanBox.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = mRootView.getWidth();
        int containerHeight = mRootView.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect.set(x, y, width + x, height + y);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.light:
                handleLight(true);
                break;
            case R.id.photo:
                decodeQrCodeFromPhoto();
                break;
        }
    }

    private void decodeQrCodeFromPhoto() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, IMG_LOAD);
    }

    /**
     * 控制闪光灯
     *
     * @param openFeatures
     */
    private void handleLight(boolean openFeatures) {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters p = mCamera.getParameters();
        String str = p.getFlashMode();
        if (str.equals(Camera.Parameters.FLASH_MODE_TORCH)) {
            if (openFeatures) {
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(p);
            }
            mLightBt.setText("打开闪光灯");
        } else if (str.equals(Camera.Parameters.FLASH_MODE_OFF)) {
            if (openFeatures) {
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(p);
            }
            mLightBt.setText("关闭闪光灯");
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_qr_scan;
    }

    private void decoderImg(String imgPath) {
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
        byte[] bytes = Bitmap2Bytes(bitmap);
        String str = mDecoder.decodeRaw(bytes, bitmap.getWidth(), bitmap.getHeight());
        L.d(TAG, str + "");
        if (!TextUtils.isEmpty(str)) {
            handleResult(str);
        }
    }

    /**
     * 获取bitmap像素数组
     */
    public byte[] Bitmap2Bytes(Bitmap bitmap) {
        int w = bitmap.getWidth(), h = bitmap.getHeight();
        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int index = y * w + x;
                int R = (pix[index] >> 16) & 0xff;     //bitwise shifting
                int G = (pix[index] >> 8) & 0xff;
                int B = pix[index] & 0xff;
                pix[index] = 0xff000000 | (R << 16) | (G << 8) | B;
            }
        }
        byte[] greyData = new byte[w * h];
        for (int i = 0; i < greyData.length; i++) {
            greyData[i] = (byte) ((((pix[i] & 0x00ff0000) >> 16)
                    * 19595 + ((pix[i] & 0x0000ff00) >> 8)
                    * 38469 + ((pix[i] & 0x000000ff)) * 7472) >> 16);
        }
        return greyData;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMG_LOAD && resultCode == Activity.RESULT_OK && data != null) {
            Uri img = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(img, filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgPath = cursor.getString(columnIndex);
                cursor.close();
                File file = new File(imgPath);
                if (file.exists()) {
                    decoderImg(imgPath);
                }
            }
        }
    }
}
