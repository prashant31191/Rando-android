package com.github.randoapp;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.randoapp.activity.BaseActivity;
import com.github.randoapp.log.Log;
import com.github.randoapp.service.SyncService;
import com.github.randoapp.task.CropImageTask;
import com.github.randoapp.task.UploadTask;
import com.github.randoapp.task.callback.OnDone;
import com.github.randoapp.task.callback.OnError;
import com.github.randoapp.task.callback.OnOk;
import com.github.randoapp.util.BitmapUtil;
import com.github.randoapp.util.FileUtil;
import com.github.randoapp.util.LocationUpdater;
import com.github.randoapp.view.RandoSurfaceView;
import com.makeramen.RoundedImageView;

import java.util.Map;

import static android.graphics.ImageFormat.JPEG;
import static android.view.View.VISIBLE;
import static com.github.randoapp.Constants.JPEG_QUALITY;

public class CameraActivity extends BaseActivity {
    private RandoSurfaceView randoSurfaceView;
    private Camera camera;
    private FrameLayout preview;

    private static final int REQ_CODE_SELECT_PHOTO = 100;
    private LocationUpdater locationUpdater = new LocationUpdater();

    public static Location currentLocation;
    public static String picFileName = null;
    private ImageView uploadPictureButton;
    private ImageView flashLightButton;

    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            String tmpFile = FileUtil.writeImageToTempFile(data);
            new CropImageTask(tmpFile)
                    .onOk(new OnOk() {
                        @Override
                        public void onOk(Map<String, Object> data) {
                            picFileName = (String) data.get(Constants.FILEPATH);
                            showUploadScreen();
                        }
                    })
                    .onError(new OnError() {
                        @Override
                        public void onError(Map<String, Object> data) {
                            Toast.makeText(CameraActivity.this, "Crop Failed.",
                                    Toast.LENGTH_LONG).show();
                        }
                    })
                    .onDone(new OnDone() {
                        @Override
                        public void onDone(Map<String, Object> data) {
                            hideProgressbar();
                        }
                    })
                    .execute();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        updateLocation();
        setBackButtonListener();
        setFlashLightButtonListener();
        setTakePictureButtonListener();
        setUploadButtonListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    showProgressbar("Processing...");
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(
                            selectedImage, filePathColumn, null, null, null);
                    if (cursor == null) {
                        Log.w(CameraActivity.class, "Selecting from Album failed.");
                        break;
                    }
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();
                    new CropImageTask(filePath)
                            .onOk(new OnOk() {
                                @Override
                                public void onOk(Map<String, Object> data) {
                                    picFileName = (String) data.get(Constants.FILEPATH);
                                    showUploadScreen();
                                }
                            })
                            .onError(new OnError() {
                                @Override
                                public void onError(Map<String, Object> data) {
                                    Toast.makeText(CameraActivity.this, "Crop Failed.",
                                            Toast.LENGTH_LONG).show();
                                }
                            })
                            .onDone(new OnDone() {
                                @Override
                                public void onDone(Map<String, Object> data) {
                                    hideProgressbar();
                                }
                            })
                            .execute();
                }
                break;
        }
    }

    private void showUploadScreen() {
        showUploadButton();
        showPictureOnPreview();
    }

    private void showPictureOnPreview() {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picFileName, options);

        // Calculate inSampleSize
        options.inSampleSize = BitmapUtil.calculateInSampleSize(options, preview.getWidth(), preview.getWidth());

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(picFileName, options);
        preview.removeAllViews();

        RoundedImageView imagePreview = new RoundedImageView(getApplicationContext());
        imagePreview.setOval(true);
        imagePreview.setImageBitmap(bitmap);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(preview.getWidth(), preview.getWidth());
        imagePreview.setLayoutParams(layoutParams);
        preview.addView(imagePreview);
    }

    private void createCameraPreview() {
        camera = getCameraInstance();

        if (camera != null) {
            Camera.Parameters params = camera.getParameters();
            //disable flashlight button if flash_background light not supported
            if (params.getFlashMode() == null) {
                flashLightButton.setEnabled(false);
                flashLightButton.setImageResource(R.drawable.flash_disable);
            }
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
            params.setPictureFormat(JPEG);
            params.setJpegQuality(JPEG_QUALITY);
            params.set("jpeg-quality", JPEG_QUALITY);
            params.setRotation(90);
            params.set("orientation", "portrait");
            params.set("rotation", 90);
            camera.setParameters(params);

            randoSurfaceView = new com.github.randoapp.view.RandoSurfaceView(getApplicationContext(), camera);

            preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.removeAllViews();
            preview.addView(randoSurfaceView);
        } else {
            //TODO: Handle camera not available.
        }
    }

    private void showUploadButton() {
        findViewById(R.id.capture_button).setVisibility(View.GONE);
        findViewById(R.id.flash_button).setVisibility(View.GONE);
        uploadPictureButton.setEnabled(true);
        findViewById(R.id.upload_button).setVisibility(VISIBLE);
        findViewById(R.id.circle_mask).setVisibility(View.GONE);
    }

    private void hideUploadButton() {
        findViewById(R.id.capture_button).setVisibility(VISIBLE);
        findViewById(R.id.flash_button).setVisibility(VISIBLE);
        findViewById(R.id.upload_button).setVisibility(View.GONE);
        findViewById(R.id.circle_mask).setVisibility(VISIBLE);
    }

    @Override
    public void onBackPressed() {
        picFileName = null;
        releaseCamera();
        super.onBackPressed();
    }

    private void setBackButtonListener() {
        ImageView backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setOnClickListener(new ImageView.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                setResult(RESULT_CANCELED);
                if (TextUtils.isEmpty(picFileName)) {
                    finish();
                } else {
                    picFileName = null;
                    releaseCamera();
                    hideUploadButton();
                    createCameraPreview();
                }
            }
        });
    }

    private void setFlashLightButtonListener() {
        flashLightButton = (ImageView) findViewById(R.id.flash_button);
        flashLightButton.setOnClickListener(new ImageView.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Camera.Parameters params = camera.getParameters();
                String flashMode = params.getFlashMode();

                if (Camera.Parameters.FLASH_MODE_AUTO.equals(flashMode)) {
                    flashLightButton.setImageResource(R.drawable.flash_disable);
                    flashMode = Camera.Parameters.FLASH_MODE_OFF;
                } else if (Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
                    flashLightButton.setImageResource(R.drawable.flash_background);
                    flashMode = Camera.Parameters.FLASH_MODE_ON;
                } else if (Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {
                    flashLightButton.setImageResource(R.drawable.flash_auto);
                    flashMode = Camera.Parameters.FLASH_MODE_AUTO;
                }
                params.setFlashMode(flashMode);

                camera.setParameters(params);
            }
        });
    }

    private void setTakePictureButtonListener() {
        ImageView takePictureButton = (ImageView) findViewById(R.id.capture_button);
        takePictureButton.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.i(CropImageTask.class, "TOTAL 0:" + Runtime.getRuntime().totalMemory() / (1024 * 1024));
                Log.i(CropImageTask.class, "0:" + Runtime.getRuntime().freeMemory() / (1024 * 1024));
                showProgressbar("processing...");
                camera.takePicture(null, null, pictureCallback);
            }
        });
    }

    private void setUploadButtonListener() {
        uploadPictureButton = (ImageView) findViewById(R.id.upload_button);
        uploadPictureButton.setOnClickListener(new ImageView.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (picFileName != null) {
                    showProgressbar("uploading...");
                    uploadPictureButton.setEnabled(false);
                    new UploadTask(picFileName).onOk(new OnOk() {
                        @Override
                        public void onOk(Map<String, Object> data) {
                            picFileName = null;
                            SyncService.run();
                            hideProgressbar();
                            Toast.makeText(CameraActivity.this,
                                    R.string.photo_upload_ok,
                                    Toast.LENGTH_LONG).show();
                            CameraActivity.this.setResult(Activity.RESULT_OK);
                            CameraActivity.this.finish();
                        }
                    }).onError(new OnError() {
                        @Override
                        public void onError(Map<String, Object> data) {
                            hideProgressbar();
                            Toast.makeText(CameraActivity.this, R.string.photo_upload_failed,
                                    Toast.LENGTH_LONG).show();

                            if (uploadPictureButton != null) {
                                uploadPictureButton.setEnabled(true);
                            }
                        }
                    }).execute();

                }
            }
        });
    }

    private void updateLocation() {
        LocationUpdater.LocationResult locationResult = new LocationUpdater.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                currentLocation = location;
            }
        };
        locationUpdater.getLocation(getApplicationContext(), locationResult);
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    private Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private void releaseCamera() {
        if (preview != null) {
            preview.removeAllViews();
        }
        if (camera != null) {
            camera.release();        // release the camera for other applications
            camera = null;
        }
    }

    private void resumeUploadScreenIfNeed() {
        if (!TextUtils.isEmpty(picFileName)) {
            showUploadScreen();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        createCameraPreview();
        resumeUploadScreenIfNeed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
        locationUpdater.cancelTimer();
    }
}
