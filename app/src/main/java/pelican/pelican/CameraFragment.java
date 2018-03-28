package pelican.pelican;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;


/**
 * Created by Sebastian on 3/4/2018.
 */
public class CameraFragment extends Fragment implements TextureView.SurfaceTextureListener {
    private Camera mCamera;
    private TextureView mTextureView;
    private MediaRecorder mMediaRecorder;
    private SurfaceTexture mSurface;
    private String dir;
    private int currentCamera = Camera.CameraInfo.CAMERA_FACING_BACK;

    public static CameraFragment newInstance(){
        CameraFragment fragment = new CameraFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        mTextureView = view.findViewById(R.id.textureView);
        mTextureView.setSurfaceTextureListener(this);

        Context mContext = getActivity();
        dir = mContext.getCacheDir().getAbsolutePath();
        dir += "/video.mp4";

        final ImageView swapButton = view.findViewById(R.id.swapButton);
        ImageView recordButton = view.findViewById(R.id.recordButton);

        recordButton.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Log.i("TAG", "ACTION_DOWN");
                            startMediaRecorder();
                            swapButton.setVisibility(View.GONE);
                            break;
                        /*case MotionEvent.ACTION_MOVE:
                            Log.i("TAG", "moving");
                            break;*/
                        case MotionEvent.ACTION_UP:
                            Log.i("TAG", "ACTION_UP");
                            // stop recording and release camera
                            try {
                                mMediaRecorder.stop();  // stop the recording
                            }
                            catch (RuntimeException e) {
                                Log.d("TAG", "RuntimeException: stop() is called immediately after start()");
                                swapButton.setVisibility(View.VISIBLE);
                                break;
                            }
                            releaseMediaRecorder();
                            startActivity(new Intent(getActivity(), VideoPlayer.class));
                            break;
                    }
                return true;
            }
        });

        swapButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mCamera.stopPreview();
                //NB: if you don't release the current camera before switching, you app will crash
                mCamera.release();
                //swap the id of the camera to be used
                if(currentCamera == Camera.CameraInfo.CAMERA_FACING_BACK){
                    currentCamera = Camera.CameraInfo.CAMERA_FACING_FRONT;
                    swapButton.setImageResource(R.drawable.ic_camera_rear_24dp);
                }
                else {
                    currentCamera = Camera.CameraInfo.CAMERA_FACING_BACK;
                    swapButton.setImageResource(R.drawable.ic_camera_front_24dp);
                }
                mCamera = Camera.open(currentCamera);
                mCamera.setDisplayOrientation(90);
                try {
                    mCamera.setPreviewTexture(mSurface);
                    mCamera.startPreview();
                }
                catch (IOException ioe) {
                    // Something bad happened
                }
            }
        });
        return view;
    }


    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurface = surface;
        mCamera = Camera.open();

        Camera.Parameters parameters;
        parameters = mCamera.getParameters();

        mCamera.setDisplayOrientation(90);
        parameters.setPreviewFrameRate(30);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);

        mCamera.setParameters(parameters);

        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
        }
        catch (IOException ioe) {
            // Something bad happened
        }
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // Ignored, Camera does all the work for us
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mCamera.stopPreview();
        mCamera.release();
        releaseMediaRecorder();
        return true;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // Invoked every time there's a new Camera preview frame
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseMediaRecorder();
    }

    public void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mCamera.lock();
        }
    }

    public void startMediaRecorder() {
        mMediaRecorder = new MediaRecorder();
        if(currentCamera == Camera.CameraInfo.CAMERA_FACING_BACK) {
            mMediaRecorder.setOrientationHint(90);
        }
        else mMediaRecorder.setOrientationHint(270);
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        mMediaRecorder.setProfile(profile);

        mMediaRecorder.setOutputFile(dir);
        mMediaRecorder.setMaxDuration(7000);

        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        }
        catch (IllegalStateException e) {
            releaseMediaRecorder();
        }
        catch (IOException e) {
            e.printStackTrace();
            releaseMediaRecorder();
        }
    }
}