package pelican.pelican;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by Sebastian on 3/4/2018.
 */

public class HomeFragment extends Fragment{

    private ListView mVideoListView;
    private List<Video> mVideoList = new ArrayList<>();
    private Set<String> mURLSet = new HashSet<>();
    private VideoAdapter mVideoAdapter;

    public static HomeFragment newInstance(){
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    ImageView profileButton;
    ImageView refreshButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mVideoListView = view.findViewById(R.id.videoList);

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
        for (int i = 1; i < videos.length; i+=5) {
            videoName = videos[i]; //vid is every 5 spots
            if (mURLSet.add(videoName)) {
                String displayName = videos[i + 1];
                Video video = new Video("http://198.187.213.142:5000/pelican?filename=" + videoName, displayName);
                mVideoList.add(video);
            }
        }

        mVideoAdapter = new VideoAdapter(getContext(), mVideoList);
        mVideoListView.setAdapter(mVideoAdapter);

        profileButton = view.findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(getActivity(), Profile.class);
                startActivity(profileIntent);
            }
        });

        refreshButton = view.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().recreate();
            }
        });
        return view;
    }
}
