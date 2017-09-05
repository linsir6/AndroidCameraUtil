package com.linsir.androidcameralibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.linsir.androidcamera.CameraView;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class TakePhotoDetails extends AppCompatActivity {


    RelativeLayout mFirst;
    RelativeLayout mSecond;
    private CameraView mCameraView;
    private static final int CAMERA_VIEW_WIDTH = 1280;
    private static final int CAMERA_VIEW_HEIGHT = 720;

    private byte[] temp;

    private ImageView imageView;

    private boolean isLight = false;

    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo_details);
        mFirst = (RelativeLayout) findViewById(R.id.first);
        mSecond = (RelativeLayout) findViewById(R.id.second);


        mCameraView = (CameraView) findViewById(R.id.camera_view);
        mCameraView.setPreviewResolution(CAMERA_VIEW_WIDTH, CAMERA_VIEW_HEIGHT);
        imageView = (ImageView) findViewById(R.id.snap_img);

        mCameraView.setPreviewCallback(new CameraView.PreviewCallback() {
            @Override
            public void onGetYuvFrame(byte[] data) {
                temp = data;
            }
        });

        mCameraView.startCamera();



        findViewById(R.id.light).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLight) {

                    mCameraView.isOpenLight(true);
                    isLight = !isLight;

                } else {
                    mCameraView.isOpenLight(false);
                    isLight = !isLight;

                }
            }
        });

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCameraView.stopCamera();

                final YuvImage image = new YuvImage(temp, ImageFormat.NV21, 1280, 720, null);
                ByteArrayOutputStream os = new ByteArrayOutputStream(temp.length);
                if (!image.compressToJpeg(new Rect(0, 0, 1280, 720), 100, os)) {
                    return;
                }
                byte[] tmp = os.toByteArray();
                Bitmap bmp = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);

                Matrix matrix = new Matrix();
                matrix.setRotate(90f);

                Bitmap newBM = Bitmap.createBitmap(bmp, 0, 0, 1280, 720, matrix, false);

                imageView.setImageBitmap(newBM);

                mBitmap = newBM;

                mFirst.setVisibility(View.GONE);
                mSecond.setVisibility(View.VISIBLE);

            }
        });


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraView.isOpenLight(false);
                mCameraView.stopCamera();
                finish();
            }
        });


        findViewById(R.id.back2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });



        findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = Util.saveBitmapFile(mBitmap);
            }
        });


    }
}
