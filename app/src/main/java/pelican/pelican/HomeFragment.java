package pelican.pelican;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Sebastian on 3/4/2018.
 */

public class HomeFragment extends Fragment{

    private ListView mVideoListView;
    private List<Video> mVideoList = new ArrayList<>();
    private VideoAdapter mVideoAdapter;

    public static HomeFragment newInstance(){
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    ImageView profileButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        View listItem = inflater.inflate(R.layout.list_row, container, false);

        mVideoListView = view.findViewById(R.id.videoList);
        TextView displayNameView = listItem.findViewById(R.id.displayName);

        //create videos
        String pelicanUrl = "http://198.187.213.142:5000/pelican/index";
        GetRequest mGetRequest = new GetRequest();
        String data = "";
        try {
            data = mGetRequest.execute(pelicanUrl).get();
            Log.e("TAG", "" + data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        String[] videos = data.split(" ");
        String videoName;
        for (int i = 1; i < videos.length; i+=5){
            videoName = videos[i]; //vid is every 5 spots
            String displayName = videos[i+1];
            Log.e("TAG",displayName);
            Video video = new Video("http://198.187.213.142:5000/pelican?filename=" + videoName);
            video.setVideoOwner(displayName);
            displayNameView.setText(displayName);
            mVideoList.add(video);
        }

        mVideoAdapter = new VideoAdapter(this.getContext(), mVideoList);
        mVideoListView.setAdapter(mVideoAdapter);

        profileButton = view.findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(getActivity(), Profile.class);
                startActivity(profileIntent);
            }
        });
        return view;
    }

}
