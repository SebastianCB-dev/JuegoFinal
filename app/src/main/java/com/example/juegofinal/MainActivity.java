package com.example.juegofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Variables
    Button btnRegistrar, btnIngresar;
    EditText etUser, etPassword;

    // Petición JSON
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Definición Variables
        btnRegistrar = findViewById(R.id.buttonGoLogin);
        etUser       = findViewById(R.id.inputUsername);
        etPassword   = findViewById(R.id.inputPassword);
        btnIngresar  = findViewById(R.id.buttonLogin);

        // Request
        request = Volley.newRequestQueue(this);

        // Eventos
        btnRegistrar.setOnClickListener(this);
        btnIngresar.setOnClickListener(this::ingresar);
    }

    private void ingresar(View view) {
        // Validaciones
        if( etUser.getText().toString().length() == 0 || etUser.getText().toString().length() > 50 ) {
            Snackbar.make(view, "La cantidad maxima para el username es entre 1 y 50 caracteres!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        if( etPassword.getText().toString().length() == 0 || etPassword.getText().toString().length() > 50 ) {
            Snackbar.make(view, "La cantidad maxima para el password es entre 1 y 50 caracteres!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        
        String url = "http://192.168.0.4/videojuego_moviles/login.php?username="+
                etUser.getText().toString().trim()+"&password="+
                etPassword.getText().toString().trim();
        url.replace(" ","%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this::ingresarResponse, this::ingresarError);
        request.add(jsonObjectRequest);

    }

    private void ingresarError(VolleyError volleyError) {
        Snackbar.make(getCurrentFocus(), "Error en la petición!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void ingresarResponse(JSONObject jsonObject) {

        String response = "";
        try {
            response = jsonObject.getJSONArray("user").getJSONObject(0).get("user_username").toString();
        } catch (JSONException e) {
            Snackbar.make(getCurrentFocus(), "Ocurrio un error...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        if( response.equals("No existe") ) {
            Snackbar.make(getCurrentFocus(), "Usuario y/o contraseña incorrecto!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }else{
            try {
                String username = jsonObject.getJSONArray("user").getJSONObject(0).get("user_username").toString();
                String id = jsonObject.getJSONArray("user").getJSONObject(0).get("user_id").toString();
                String last_access = jsonObject.getJSONArray("user").getJSONObject(0).get("user_last_access").toString();
                User.username = username;
                User.id = id;
                User.last_access = last_access;
            }  catch (JSONException e) {
                Snackbar.make(getCurrentFocus(), "Ocurrio un error al obtener la información", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            // Cambio de actividad a Juego
            Intent intent = new Intent(this, MainGame.class);
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View v) {
        // Cambio de actividad a Register
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);

    }
    @Override
    public void onBackPressed() {

    }
}