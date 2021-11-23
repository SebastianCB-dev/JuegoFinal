package com.example.juegofinal.ui.home;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

    Button btnStart,btnLeft,btnRight;
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
        btnLeft = root.findViewById(R.id.buttonLeft);
        btnRight = root.findViewById(R.id.buttonRight);
        btnStart.setOnClickListener(this);
        btnRight.setOnTouchListener(this::moverNaveIzq);
        btnLeft.setOnTouchListener(this::moverNaveDer);
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

    private boolean moverNaveDer(View view, MotionEvent motionEvent) {

        if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if( nave.getX() - 20f < -39.00) {
                nave.setX(-39.00f);
            }
            else {
                nave.setX(nave.getX() - 20f);
            }
        }

        return true;
    }

    private boolean moverNaveIzq(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_UP
        ) {
            if( nave.getX() + 20f > 837.00) {
                nave.setX(837.00f);

            }
            else {
                nave.setX(nave.getX() + 20f);
            }
        }

        return true;
    }
    //nave.setX(nave.getX() - 3f);
    

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
        btnLeft.setVisibility(View.VISIBLE);
        btnRight.setVisibility(View.VISIBLE);
        nave.setVisibility(View.VISIBLE);
        Contador j = new Contador();
        Meteoros m = new Meteoros();
        j.start();
        m.start();
    }


    public class Contador extends Thread {
        private int contador = 0;

        public int getContador() {
            return this.contador;
        }

        public void setContador(int contador) {
            this.contador = contador;
        }

        public Contador() { }

        public void run() {
            new Timer().scheduleAtFixedRate(new TimerTask(){
                @Override
                public void run(){
                    tvPuntaje.setText(String.valueOf(getContador()));
                    setContador(getContador() + 1 );
                }
            },1000,1000);

        }


    }

    public class Meteoros extends Thread {

        public Meteoros() { }

        public void run() {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // Generar Randomico
                    double a = 1 + (Math.random() * getView().getWidth() - 1);
                    String randomico = String.valueOf(a);
                    ImageView meteoro = new ImageView(getContext());
                    meteoro.setX(Float.parseFloat(randomico));
                    meteoro.setY(240);
                    meteoro.setImageResource(R.drawable.asteroid);
                    getActivity().addContentView(meteoro, new ViewGroup.LayoutParams(100, 100));

                    new Timer().scheduleAtFixedRate(new TimerTask(){
                        @Override
                        public void run(){

                            meteoro.setY(meteoro.getY()+ 1);
                            if(meteoro.getY() > getView().getWidth() ) {
                                meteoro.setVisibility(View.INVISIBLE);
                            }
                        }
                    },10,10);
                }
            });





        }

    }

}


