package com.example.juegofinal.ui.home;

import android.graphics.Rect;
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
import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.Visibility;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.juegofinal.R;
import com.example.juegofinal.User;
import com.example.juegofinal.databinding.FragmentHomeBinding;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private VideoView videoBG;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;

    Button btnStart, btnLeft, btnRight;
    ImageView nave;
    TextView tvPuntaje, tvPuntajeFinal, tvGameOver, tvtuPuntajeFue;
    Contador j;
    Meteoros m;
    ViewGroup layout;

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

        nave = new ImageView(getActivity().getBaseContext());
        nave.setImageResource(R.drawable.nave);
        nave.setVisibility(View.INVISIBLE);

        layout = (ViewGroup) root.findViewById(R.id.constraint);
        layout.addView(nave, new ViewGroup.LayoutParams(200, 200));

        j = new Contador();
        m = new Meteoros();
        btnStart.setOnClickListener(this);
        btnRight.setOnTouchListener(this::moverNaveIzq);
        btnLeft.setOnTouchListener(this::moverNaveDer);
        tvPuntaje = root.findViewById(R.id.tvPuntaje);
        tvPuntajeFinal = root.findViewById(R.id.tvPuntajeFinal);
        tvGameOver = root.findViewById(R.id.tvGameOver);
        tvtuPuntajeFue = root.findViewById(R.id.tvTuPuntajeFue);

        //final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    private boolean moverNaveDer(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if (nave.getX() - 20f < -2.00) {
                nave.setX(-2.00f);
            } else {
                nave.setX(nave.getX() - 20f);
            }
        }

        return true;
    }

    private boolean moverNaveIzq(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP
        ) {
            if (nave.getX() + 20f > 890.00) {
                nave.setX(890.00f);

            } else {
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
        nave.setX(420);
        nave.setY(1300);
        nave.setVisibility(View.VISIBLE);
        j.setPriority(1);
        m.setPriority(2);
        j.start();
        m.start();
    }


    public class Contador extends Thread {
        public int contador = 0;
        public int aumento = 1;

        public int getContador() {
            return this.contador;
        }

        public int getAumento() {
            return aumento;
        }

        public void setAumento(int aumento) {
            this.aumento = aumento;
        }

        public void setContador(int contador) {
            this.contador = contador;
        }

        public Contador() {
        }

        public void run() {
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    tvPuntaje.setText(String.valueOf(getContador()));
                    setContador(getContador() + aumento);
                }
            }, 1000, 1000);

        }


    }

    public class Meteoros extends Thread {

        public Meteoros() {
        }

        public void run() {

            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // Generar Randomico
                    double a = 100 + (Math.random() * getView().getWidth() - 50);
                    String randomico = String.valueOf(a);
                    ImageView meteoro = new ImageView(getActivity().getBaseContext());
                    meteoro.setImageResource(R.drawable.asteroid);
                    meteoro.setX(Float.parseFloat(randomico));
                    meteoro.setY(0);
                    //meteoro.setTop(100);
                    //meteoro.setRight(100);
                    layout = (ViewGroup) getActivity().findViewById(R.id.constraint);
                    layout.addView(meteoro, new ViewGroup.LayoutParams(100, 100));

                    new Timer().scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            meteoro.setY(meteoro.getY() + 1);
                            //Choque
                            // Dimension de nave:    X: 106   Y:80
                            // Dimension de meteoro: X: 100   Y:100
                            Rect R1 = new Rect();

                            nave.getHitRect(R1);

                            Rect R2 = new Rect();
                            meteoro.getHitRect(R2);

                            //Colisión
                            if (Rect.intersects(R1, R2)) {
                                try {
                                    this.cancel();
                                    j.aumento = 0;
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            btnLeft.setVisibility(View.INVISIBLE);
                                            btnRight.setVisibility(View.INVISIBLE);
                                            getActivity().findViewById(R.id.tvGameOver).setVisibility(View.VISIBLE);
                                            getActivity().findViewById(R.id.tvTuPuntajeFue).setVisibility(View.VISIBLE);
                                            tvPuntajeFinal.setText(String.valueOf(j.getContador()));
                                            getActivity().findViewById(R.id.tvPuntajeFinal).setVisibility(View.VISIBLE);

                                            String puntaje = String.valueOf(j.getContador());
                                            String id = User.id;
                                            String username = User.username;
                                            String url = "http://192.168.0.4/videojuego_moviles/score.php?username=" +
                                                    username.trim() + "&id=" +
                                                    id + "&points=" +
                                                    puntaje;
                                            url.replace(" ", "%20");
                                            RequestQueue request;
                                            JsonObjectRequest jsonObjectRequest;
                                            request = Volley.newRequestQueue(getContext());
                                            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this::scoreResponse, this::scoreResponseError);
                                            request.add(jsonObjectRequest);
                                        }

                                        private void scoreResponseError(VolleyError volleyError) {
                                            Snackbar.make(getView(), "Error al realizar la petición!", Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                        }

                                        private void scoreResponse(JSONObject jsonObject) {

                                            try {
                                                jsonObject.getJSONArray("user").getJSONObject(0).get("score_user_username").toString();
                                            } catch (JSONException e) {
                                                Snackbar.make(getView(), "Ocurrio un error contacte al administrador!", Snackbar.LENGTH_LONG)
                                                        .setAction("Action", null).show();
                                            }

                                        }

                                    });


                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                }


                            }

                            if (meteoro.getY() > getView().getHeight() - 30) {
                                getActivity().runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        layout.removeView(meteoro);
                                    }
                                    });

                                j.contador = Integer.parseInt(tvPuntaje.getText().toString()) + 50;
                                try {
                                    this.cancel();
                                    m.run();
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                }

                            }
                        }
                    }, 10, 10);
                }
            });
        }

    }

}


