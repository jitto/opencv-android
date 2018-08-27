package com.example.jitto.cameraapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


public class MainActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {
    static {
        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Initialization error");
        } else {
            Log.d("OpenCV", "Initialization successful");
        }
    }
    private CameraBridgeViewBase mOpenCvCameraView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        checkCameraPermission();
        Log.i("OpenCV", "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mOpenCvCameraView = (CameraBridgeViewBase) new JavaCameraView(this, -1);
        Log.i("OpenCV", "" + mOpenCvCameraView);
        setContentView(mOpenCvCameraView);
        mOpenCvCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    public void checkCameraPermission() {
        int permissionCheck = checkSelfPermission(Manifest.permission.CAMERA);
        Log.i("OpenCV", "PermissionCheck " + permissionCheck);
        if (permissionCheck== PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    @Override
    public void onResume() {
        mOpenCvCameraView.enableView();
        super.onResume();
    }
    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }
    public void onCameraViewStarted(int width, int height) {
    }
    public void onCameraViewStopped() {
    }
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return processFrame(inputFrame.rgba(), inputFrame.gray());
    }

    private Mat processFrame(Mat rgba, Mat gray) {
//        Imgproc.cvtColor(inputFrame.gray(), mRgba, Imgproc.COLOR_GRAY2RGBA, 4);
//        Mat mIntermediateMat = rgba.clone();
        Imgproc.Canny(gray, rgba, 80, 100);
//        Imgproc.cvtColor(mIntermediateMat, rgba, Imgproc.COLOR_GRAY2RGBA, 4);
//        Mat mGray = inputFrame.gray();
//        FindFeatures(mGray.getNativeObjAddr(), mRgba.getNativeObjAddr());
        return rgba;
    }
}