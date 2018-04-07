package pelican.pelican;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;


// for VideoUploadTask
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Sebastian on 3/27/2018.
 */

public class VideoPlayer extends Activity {

    private static final String TAG = "VideoPlayer";
    VideoView mVideoView;
    int stopPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.video_player);

        final String dir = getCacheDir().getAbsolutePath()+"/video.mp4";

        mVideoView = findViewById(R.id.videoView);
        mVideoView.setVideoPath(dir);
        mVideoView.setOnPreparedListener (new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        mVideoView.start();

        ImageView closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent mIntent = new Intent(VideoPlayer.this, MainActivity.class);
                mIntent.putExtra("pos", 1);
                startActivity(mIntent);
            }
        });

        ImageView uploadButton = findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                VideoUploadTask uploadTask = new VideoUploadTask();
                uploadTask.execute(dir);
                Intent mIntent = new Intent(VideoPlayer.this, MainActivity.class);
                mIntent.putExtra("pos", 1);
                startActivity(mIntent);
            }
        });
    }
    @Override
    public void onPause() {
        Log.d(TAG, "onPause called");
        super.onPause();
        stopPosition = mVideoView.getCurrentPosition();
        mVideoView.pause();
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        mVideoView.seekTo(stopPosition);
        mVideoView.start();
    }
}
