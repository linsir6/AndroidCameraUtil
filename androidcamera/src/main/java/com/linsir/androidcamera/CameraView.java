package com.linsir.androidcamera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Created by linSir
 * date at 2017/9/5.
 * describe:
 */

public class CameraView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {


    private Camera mCamera;

    private int mPreviewRotation = 90;
    private int mCamId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private PreviewCallback mPrevCb;
    private byte[] mYuvPreviewFrame;
    private int previewWidth;
    private int previewHeight;

    private Camera.Parameters params;

    public interface PreviewCallback {
        void onGetYuvFrame(byte[] data);
    }

    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPreviewRotation(int rotation) {
        mPreviewRotation = rotation;
    }

    public void switchCamera() {
        if (mCamId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            mCamId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }else {
            mCamId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
    }

    public void setPreviewCallback(PreviewCallback cb) {
        mPrevCb = cb;
        getHolder().addCallback(this);
    }

    public void setPreviewResolution(int width, int height) {
        previewWidth = width;
        previewHeight = height;
    }

    public boolean startCamera() {
        if (mCamera != null) {
            return false;
        }
        if (mCamId > (Camera.getNumberOfCameras() - 1) || mCamId < 0) {
            return false;
        }

        mCamera = Camera.open(mCamId);

        params = mCamera.getParameters();
        Camera.Size size = mCamera.new Size(previewWidth, previewHeight);

        mYuvPreviewFrame = new byte[previewWidth * previewHeight * 3 / 2];

        params.setPreviewSize(previewWidth, previewHeight);

        params.setPreviewFormat(ImageFormat.NV21);

        List<String> supportedFocusModes = params.getSupportedFocusModes();


        if (!supportedFocusModes.isEmpty()) {
            if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
        }

        mCamera.setParameters(params);

        mCamera.setDisplayOrientation(mPreviewRotation);

//
//        for(int i = 0;i<mCamera.getParameters().getSupportedPreviewSizes().size();i++){
//            LinLog.lLog("---lin---> " + mCamera.getParameters().getSupportedPreviewSizes().get(i)
//                    .height);
//
//            LinLog.lLog("---lin---> " + mCamera.getParameters().getSupportedPreviewSizes().get(i)
//                    .width);
//            LinLog.lLog("---lin---> ");
//        }


        mCamera.addCallbackBuffer(mYuvPreviewFrame);
        mCamera.setPreviewCallbackWithBuffer(this);
        try {
            mCamera.setPreviewDisplay(getHolder());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();

        return true;
    }

    public void stopCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        mPrevCb.onGetYuvFrame(data);
        camera.addCallbackBuffer(mYuvPreviewFrame);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(getHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
    }

    public void isOpenLight(boolean isOpen) {
        if (isOpen) {
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(params);
        } else {
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(params);
        }
    }

}
