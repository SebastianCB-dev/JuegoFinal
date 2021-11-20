package com.example.juegofinal.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.juegofinal.R;
import com.example.juegofinal.User;
import com.example.juegofinal.databinding.FragmentSlideshowBinding;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class SlideshowFragment extends Fragment implements View.OnClickListener {

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;

    EditText username, id, newPassword, confirmPassword;
    Button btnUpdateInfo;

    // Petición JSON
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    String url;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        username = root.findViewById(R.id.tvConfiguracionUsername);
        id = root.findViewById(R.id.tvConfiguracionID);
        newPassword = root.findViewById(R.id.tvConfiguracionNewPassword);
        confirmPassword = root.findViewById(R.id.tvConfiguracionConfirmPassword);
        btnUpdateInfo = root.findViewById(R.id.btnUpdateInfo);

        btnUpdateInfo.setOnClickListener(this);

        username.setText(User.username);
        id.setText(User.id);

        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        // Validaciones
        if(!newPassword.getText().toString().equals(confirmPassword.getText().toString())){
            Snackbar.make(getView(), "Error!, las contraseñas no coinciden", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
            return;
        }

        if( newPassword.getText().toString().length() == 0 || newPassword.getText().toString().length() > 50 ) {
            Snackbar.make(getView(), "La cantidad maxima para el username es entre 1 y 50 caracteres!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        // Request
        request = Volley.newRequestQueue(getContext());

        String url = "http://192.168.0.4/videojuego_moviles/update.php?id="+
                User.id+"&password="+newPassword.getText().toString().trim();
        url.replace(" ","%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this::actualizarContraResponse, this::actualizarContraError);
        request.add(jsonObjectRequest);


    }

    private void actualizarContraError(VolleyError volleyError) {
         Snackbar.make(getView(), "Error:"+volleyError.getMessage(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
    }

    private void actualizarContraResponse(JSONObject jsonObject) {
        String response = "";
        try {
            response = jsonObject.getJSONArray("user").getJSONObject(0).get("user_username").toString();
        } catch (JSONException e) {
            Snackbar.make(getView(), "Error:"+e.getMessage(), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
        }

        if( response.equals("No actualizado") ) {
            Snackbar.make(getView(), "Error datos no actualizados", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
            return;
        }else{
            Snackbar.make(getView(), "Contraseña actualizada correctamente:", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        newPassword.setText("");
        confirmPassword.setText("");

    }
}