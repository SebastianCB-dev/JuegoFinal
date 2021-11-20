package com.example.juegofinal;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.juegofinal.databinding.ActivityMainGameBinding;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainGame extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainGameBinding binding;
    TextView tvId,tvLast_access, tvUsername;

    // Petición JSON
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.appBarMainGame.toolbar);
        /*binding.appBarMainGame.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_cerrarsesion)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_game);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_game, menu);
        tvId = findViewById(R.id.tvId);
        tvUsername = findViewById(R.id.tvUser);
        tvLast_access = findViewById(R.id.tvLast_access);

        tvLast_access.setText("Último Acceso: " + User.last_access);
        tvUsername.setText(User.username);
        tvId.setText("ID: "+ User.id);
        request = Volley.newRequestQueue(this);
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date myDate = new Date();
        String filename = timeStampFormat.format(myDate);

        String url = "http://192.168.0.4/videojuego_moviles/update.php?last_access="+
                filename+"&id="+User.id;
        url.replace(" ","%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this::actualizarResponse, this::actualizarError);
        request.add(jsonObjectRequest);
        return true;
    }

    private void actualizarError(VolleyError volleyError) {
    }

    private void actualizarResponse(JSONObject jsonObject) {
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_game);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}