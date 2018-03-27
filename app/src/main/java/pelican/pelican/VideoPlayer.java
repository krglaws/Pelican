package pelican.pelican;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

/**
 * Created by Sebastian on 3/27/2018.
 */

public class VideoPlayer extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player);


        String dir = getCacheDir().getAbsolutePath();
        dir += "/video.mp4";
        VideoView mVideoView = (VideoView)findViewById(R.id.videoView);
        mVideoView.setVideoPath(dir);
        mVideoView.start();
    }
}
