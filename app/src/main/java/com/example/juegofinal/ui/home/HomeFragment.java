package com.example.juegofinal.ui.home;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.Visibility;


import com.example.juegofinal.R;
import com.example.juegofinal.databinding.FragmentHomeBinding;

import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private VideoView videoBG;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;

    Button btnStart;
    ImageView nave;
    TextView tvPuntaje;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        videoBG = (VideoView) root.findViewById(R.id.videoView);
        // Build your video Uri

        Uri uri = Uri.parse("android.resource://" // First start with this,
                + getActivity().getPackageName() // then retrieve your package name,
                + "/" // add a slash,
                + R.raw.videobackground); // and then finally add your video resource. Make sure it is stored
        // in the raw folder.

        // Set the new Uri to our VideoView
        videoBG.setVideoURI(uri);
        // Start the VideoView
        videoBG.start();

        btnStart = root.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);

        nave = root.findViewById(R.id.nave);
        tvPuntaje = root.findViewById(R.id.tvPuntaje);

        

        //final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Restart the video when resuming the Activity
        //videoBG.start();
    }

    //Comienze el juego"
    @Override
    public void onClick(View v) {
        btnStart.setVisibility(View.INVISIBLE);
        nave.setVisibility(View.VISIBLE);
        //Juego j = new Juego();
        //j.start();

    }

    public class Juego extends Thread{

        public Juego() {
        }

        public void run() {
            new Timer().scheduleAtFixedRate(new TimerTask(){
                int points = 0;
                @Override
                public void run(){
                    tvPuntaje.setText(String.valueOf(this.points));
                    points++;
                }
            },1000,1000);

        }


    }

}


