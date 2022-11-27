package com.sfu.cmpt276.coopachievement;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/*
* Main Menu is responsible for switching between ViewConfigList Activity and Option Activity
*/

public class MainMenuActivity extends AppCompatActivity {
    private static final int PERMISSION_CODE =1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    private static final String CAMERA_TAG = "AndroidCameraApi";

    private Button captureBtn;
    private TextureView textureViewImage;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0,90);
        ORIENTATIONS.append(Surface.ROTATION_90,0);
        ORIENTATIONS.append(Surface.ROTATION_180,270);
        ORIENTATIONS.append(Surface.ROTATION_270,180);
    }

    private String cameraId;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSession;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    private File file;
    private File folder;
    private String folderName = "MyPhotoDirectory";
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private Handler myBackgroundHandler;
    private HandlerThread myBackgroundThread;



/* Camera API guidelines referred from https://www.youtube.com/watch?v=MhsG3jYEsek&t=22s :
* CAMERA 2 API FULL
* */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ActionBar toolbar = getSupportActionBar();
        //toolbar.setTitle("Main Menu");


        ImageView bugImage = findViewById(R.id.main_banner);
        bugImage.startAnimation(AnimationUtils.loadAnimation(
                getApplicationContext(),
                R.anim.fadein
        ));

        textureViewImage = findViewById(R.id.capture_image);
        if (textureViewImage !=null){
            textureViewImage.setSurfaceTextureListener(textureListener);
        }
        captureBtn = findViewById(R.id.capture_button);
        if (captureBtn!=null){
            captureBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takePhoto();
                }
            });
        }




        setupConfigButton();
        setupOptionButton();

    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
            //open camera
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
            //resize image

        }

        @Override
        public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

        }
    };

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            //call when camera is opened

            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            cameraDevice.close();

        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;

        }
    };

    protected void startBackgroundThread(){
        myBackgroundThread = new HandlerThread("Camera Background");
        myBackgroundThread.start();
        myBackgroundHandler = new Handler(myBackgroundThread.getLooper());

    }

    protected void stopBackgroundThread(){
        myBackgroundThread.quitSafely();
        try{
            myBackgroundThread.join();
            myBackgroundThread=null;
            myBackgroundHandler=null;
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    protected void takePhoto(){

        if(cameraDevice == null){
            Log.e(CAMERA_TAG, "Cam device is null!");
            return;
        }

        if(!isExtStorageAvailableForRW()||isExtStorageReadOnly()){
            captureBtn.setEnabled(false);
        }

        if (isStoragePermissionGranted()){
            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            try {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraDevice.getId());
                Size[] jpegSizes = null;
                if (cameraCharacteristics != null) {
                    jpegSizes = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
                }
                int width = 640;
                int height = 480;
                if (jpegSizes != null && jpegSizes.length > 0) {
                    width = jpegSizes[0].getWidth();
                    height = jpegSizes[0].getHeight();
                }
                ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
                List<Surface> outputSurfaces = new ArrayList<>(2);
                outputSurfaces.add(reader.getSurface());
                outputSurfaces.add(new Surface(textureViewImage.getSurfaceTexture()));
                final CaptureRequest.Builder capturedBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                capturedBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

                //orientation
                int rotation = getWindowManager().getDefaultDisplay().getRotation();
                capturedBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
                file = null;
                folder = new File(folderName);
                String timeStamp = new SimpleDateFormat("MMddyyyy_HHmmss").format(new Date());
                String imageFileName = "IMG_" + timeStamp + ".jpg";
                file = new File(getExternalFilesDir(folderName), "/" + imageFileName);
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                    @Override
                    public void onImageAvailable(ImageReader reader) {
                        Image image = null;
                        try {
                            image = reader.acquireLatestImage();
                            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                            byte[] bytes = new byte[buffer.capacity()];
                            buffer.get(bytes);
                            save(bytes);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (image != null) {
                                image.close();
                            }
                        }
                    }

                    private void save(byte[] bytes) throws IOException{
                        OutputStream outputStream = null;
                        try{
                            outputStream=new FileOutputStream(file);
                            outputStream.write(bytes);
                        }finally {
                            if(null!=outputStream){
                                outputStream.close();
                            }
                        }
                    }
                };

                reader.setOnImageAvailableListener(readerListener,myBackgroundHandler);
                final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                    @Override
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                        super.onCaptureCompleted(session, request, result);
                        Toast.makeText(MainMenuActivity.this,"Image Saved: " + file,Toast.LENGTH_SHORT).show();
                        createCameraPreview();
                    }
                };
                cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {

                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession session) {
                        SurfaceTexture texture = textureViewImage.getSurfaceTexture();
                        assert texture != null;
                        texture.setDefaultBufferSize(imageDimension.getWidth(),imageDimension.getHeight());
                        Surface surface = new Surface(texture);
                        try{
                            captureRequestBuilder.addTarget(surface);
                            session.capture(capturedBuilder.build(),captureListener,myBackgroundHandler);
                        }catch (CameraAccessException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                    }
                },myBackgroundHandler);
            }catch (CameraAccessException e){
                e.printStackTrace();
            }
        }
    }

    private static boolean isExtStorageReadOnly(){
        String extStorageState = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)){
            return true;
        }
        return false;
    }

    private boolean isExtStorageAvailableForRW(){
        //check if ext storage is good for read and write by calling
        //Environmental.getExtStorageState method . if return state is media_mounted,
        //it's ok to read and write and will return true.

        String extStorageState = Environment.getExternalStorageState();
        if(extStorageState.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }
        return false;
    }

    private boolean isStoragePermissionGranted(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                return true;
            }
            else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                return false;
            }
        }
        else
            return true;
    }

    protected void createCameraPreview(){
        try{
            SurfaceTexture texture = textureViewImage.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(),imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if(null == cameraDevice){
                        return;
                    }
                    //when session is ready, displaying the preview
                    cameraCaptureSession = session;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Toast.makeText(MainMenuActivity.this,"Config change",Toast.LENGTH_SHORT).show();
                }
            },null);
        }catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    private void openCamera(){
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.e(CAMERA_TAG,"is camera open ");
        try {
            cameraId = cameraManager.getCameraIdList()[0];
            CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null ;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

            //add permission for cam and let user grant permission here
            if ( ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                    PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainMenuActivity.this,new String[]
                        {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CAMERA_PERMISSION);
                return;
            }
            cameraManager.openCamera(cameraId,stateCallback,null);
        }catch (CameraAccessException e){
            e.printStackTrace();
        }
        Log.e(CAMERA_TAG,"open camera");
    }

    protected void updatePreview(){
        if (null == cameraDevice){
            Log.e(CAMERA_TAG,"update error");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE,CameraMetadata.CONTROL_MODE_AUTO);
        try{
            cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(),null,myBackgroundHandler);
        }catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CAMERA_PERMISSION){
            if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                //close app
                Toast.makeText(MainMenuActivity.this,"Cannot open app without granting permission",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();
        if(textureViewImage.isAvailable()){
            openCamera();
        }
        else{
            textureViewImage.setSurfaceTextureListener(textureListener);
        }
    }

    @Override
    protected void onPause() {

        //close camera
        stopBackgroundThread();
        super.onPause();

    }

    private void setupConfigButton() {
        Button btn = findViewById(R.id.configBtn);
        btn.setOnClickListener(v -> {
            Intent i = ViewConfigListActivity.makeIntent(MainMenuActivity.this);
            startActivity(i);
        });
    }

    private void setupOptionButton() {
        Button btn = findViewById(R.id.optionBtn);
        btn.setOnClickListener(v -> {
            Intent i = OptionActivity.makeIntent(MainMenuActivity.this,0);
            startActivity(i);
        });

    }
}