package com.forager.meal.ui.fragment;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.forager.meal.application.MyApplication;
import com.forager.meal.ui.R;
import com.forager.meal.ui.activity.DashBoardActivity;

/**
 * Created by Chithra on 9/28/2015.
 */
public class WorkFragment extends Fragment implements View.OnClickListener {

    DisplayMetrics dm;
    VideoView videoView;
    MediaController mediaController;
    ImageButton mBackbtn;
    TextView title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);
        findViews(rootView);
        playVideo();

        return rootView;
    }

    private void findViews(View rootView) {
        PlanControllerFragment.flag=false;
        MyApplication.setContext(getActivity());
        title=(TextView) rootView.findViewById(R.id.title_video);
        title.setTypeface(MyApplication.getFont());
        videoView = (VideoView) rootView.findViewById(R.id.video);
        mBackbtn = (ImageButton) rootView.findViewById(R.id.videoback);
        mBackbtn.setOnClickListener(this);
    }

    private void playVideo() {
        Uri video1 = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.foragerprointrovideo);
        videoView.setVideoURI(video1);
       // videoView.start();

        //Creating MediaController

        mediaController = new MediaController(this.getActivity());

        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.seekTo(10);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaController.show(0);

            }
        });
    }

    private void callBack() {
        Intent supplementsIntent = new Intent(getActivity(), DashBoardActivity.class);
        getActivity().overridePendingTransition(0, 0);
        supplementsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        supplementsIntent.putExtra("dashboard", "moretag");
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
        getActivity().startActivity(supplementsIntent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.videoback:
              callBack();
        }

    }
}
