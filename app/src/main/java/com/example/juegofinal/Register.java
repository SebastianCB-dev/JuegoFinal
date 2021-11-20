package com.example.juegofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Register extends AppCompatActivity implements View.OnClickListener {

    //Variables
    Button btnIrLogin, btnEnviar;
    EditText etUsername;
    EditText etPassword;
    TextView error;

    // Petición JSON
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Definición Variables
        btnIrLogin = findViewById(R.id.buttonGoLogin);
        btnEnviar = findViewById(R.id.buttonLogin);
        etUsername = findViewById(R.id.inputUsername);
        etPassword = findViewById(R.id.inputPassword);
        error = findViewById(R.id.messageError);

        // Request
        request = Volley.newRequestQueue(this);

        // Eventos
        btnIrLogin.setOnClickListener(this);
        btnEnviar.setOnClickListener(this::registrarUsuario);



    }

    private void registrarUsuario(View view) {
        // Validaciones
        if( etUsername.getText().toString().length() == 0 || etUsername.getText().toString().length() > 50 ) {
            Snackbar.make(view, "La cantidad maxima para el username es entre 1 y 50 caracteres!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        if( etPassword.getText().toString().length() == 0 || etPassword.getText().toString().length() > 50 ) {
            Snackbar.make(view, "La cantidad maxima para el password es entre 1 y 50 caracteres!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        //http://localhost/videojuego_moviles/register.php?username=jscarrill02&password=123
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date myDate = new Date();
        String filename = timeStampFormat.format(myDate);
        String url = "http://192.168.0.4/videojuego_moviles/register.php?username="+
                    etUsername.getText().toString().trim()+"&password="+
                    etPassword.getText().toString().trim()+"&last_access="+
                    filename;
        url.replace(" ","%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this::registrarResponse, this::registrarError);
        request.add(jsonObjectRequest);
    }

    public void registrarError(VolleyError volleyError) {
        Snackbar.make(getCurrentFocus(), "Error al realizar la petición!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void registrarResponse(JSONObject jsonObject) {
        String response = "";
        try {
             response = jsonObject.getJSONArray("user").getJSONObject(0).get("user_username").toString();
        } catch (JSONException e) {
            Snackbar.make(getCurrentFocus(), "Ocurrio un error contacte al administrador!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        if( response.equals("No insertado") ) {
            Snackbar.make(getCurrentFocus(), "Ya existe un usuario con ese username!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }else{
            Snackbar.make(getCurrentFocus(), "Usuario registrado correctamente!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
             etUsername.setText("");
             etPassword.setText("");
        }
    }


    @Override
    public void onClick(View v) {
        // Cambio de actividad a Login
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}