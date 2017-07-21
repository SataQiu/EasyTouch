package com.example.aria.easytouch.widget.easytouch.camera;

import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;
import android.widget.Toast;

import com.example.aria.easytouch.R;

import java.util.List;

/**
 * Created by Aria on 2017/7/21.
 */

public class CameraImpl implements LightCamera{

    private static final String TAG = "CameraImpl";

    private Context context;
    private Camera camera;
    private boolean isOpenCamera = false;


    public CameraImpl(Context context){
        this.context = context;
        camera = Camera.open();
    }

    @Override
    public boolean getOpenCamera() {
        return isOpenCamera;
    }

    @Override
    public void turnOnLight() {

        if (!isSupportFlash()){
            Toast.makeText(context,context.getString(R.string.msg_not_support_flashlight),Toast.LENGTH_SHORT).show();
            return;
        }

        if (isOpenCamera) {
            isOpenCamera = false;
            Log.d("MainActivity","openCamera");
                turnOnCamera(camera);
        }else {
            isOpenCamera = true;
            Log.d("MainActivity","closeCamera");
                turnOffCamera(camera);
        }
    }

    private void turnOnCamera(Camera camera){
        Camera.Parameters parameters = camera.getParameters();
        if (parameters == null)return;
        List<String> flashModes = parameters.getSupportedFlashModes();
        if (flashModes == null)return;
        String flashMode = parameters.getFlashMode();
        if (!Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode)){
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)){
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameters);
            }else {}
        }
    }

    public void turnOffCamera(Camera mCamera) {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        String flashMode = parameters.getFlashMode();
        // Check if camera flash exists
        if (flashModes == null) {
            return;
        }
        Log.i(TAG, "Flash mode: " + flashMode);
        Log.i(TAG, "Flash modes: " + flashModes);
        if (!Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
            // Turn off the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(parameters);
            } else {
                Log.e(TAG, "FLASH_MODE_OFF not supported");
            }
        }
    }

    private boolean isSupportFlash(){

        boolean flag = false;
        PackageManager manager = context.getPackageManager();
        FeatureInfo[] featureInfos = manager.getSystemAvailableFeatures();
        for (FeatureInfo info: featureInfos){
            if (PackageManager.FEATURE_CAMERA_FLASH.equals(info.name))
                flag = true;
            else flag = false;
        }
        return flag;
    }
}