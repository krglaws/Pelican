package pelican.pelican;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

/**
 * Created by Sebastian on 3/27/2018.
 */

public class VideoPlayer extends Activity {
    VideoView mVideoView;
    int stopPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.video_player);

        String dir = getCacheDir().getAbsolutePath();
        dir += "/video.mp4";
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
    }
    @Override
    public void onPause() {
        Log.d("TAG", "onPause called");
        super.onPause();
        stopPosition = mVideoView.getCurrentPosition();
        mVideoView.pause();
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d("TAG", "onResume called");
        mVideoView.seekTo(stopPosition);
        mVideoView.start();
    }
}
