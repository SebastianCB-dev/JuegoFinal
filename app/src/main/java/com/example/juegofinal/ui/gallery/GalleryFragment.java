package com.example.juegofinal.ui.gallery;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import com.example.juegofinal.databinding.FragmentGalleryBinding;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;
    ListView lvResults;
    ArrayList<String> data = new ArrayList<>();
    ArrayAdapter<String> adapter;
    TextView tvOro, tvPlata, tvBronce;
    // Petición JSON
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    String url;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                
            }
        });
        lvResults = root.findViewById(R.id.listScore);
        adapter  = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, data);
        lvResults.setAdapter(adapter);
        tvOro = root.findViewById(R.id.tvOro);
        tvBronce = root.findViewById(R.id.tvBronce);
        tvPlata = root.findViewById(R.id.tvPlata);
        tvOro.setMovementMethod(new ScrollingMovementMethod());
        tvPlata.setMovementMethod(new ScrollingMovementMethod());
        tvBronce.setMovementMethod(new ScrollingMovementMethod());



        // Request
        request = Volley.newRequestQueue(getContext());

        // Hacer petición Web Service Score
        String url = "http://192.168.0.4/videojuego_moviles/consultarall.php?";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this::traerDatosResponse, this::traerDatosError);
        request.add(jsonObjectRequest);
        return root;
    }

    private void traerDatosError(VolleyError volleyError) {

    }

    private void limpiarSearch() {
        data.clear();
        adapter  = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, data);
        lvResults.setAdapter(adapter);
    }

    private void traerDatosResponse(JSONObject jsonObject) {

        limpiarSearch();
        int responseDataLength = 0;
        try {
            responseDataLength = jsonObject.getJSONArray("score").length();
        } catch (JSONException e) {
             Snackbar.make(getView(), "Ocurrio un error...", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
        }
        String userid, points, username;
        if(responseDataLength >= 10 ) {
            responseDataLength = 10;
        }
        for(int i = 0; i < responseDataLength; i++ ) {
            try {
                String[] data = jsonObject.getJSONArray("score").get(i).toString().replace("[", "").replace("]","").split(",");
                userid = data[2].replace("\"","");
                points = data[3].replace("\"","");
                username = data[1].replace("\"","");
            } catch (JSONException e) {
            Snackbar.make(getView(), "Error: "+e.getMessage(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                    return;
            }
            catch(NumberFormatException e) {
                Snackbar.make(getView(), "Error: "+e.getMessage(), Snackbar.LENGTH_LONG)
                   .setAction("Action", null).show();
                   return;
            }
            if( i < 3) {
                colocarMejores(username, points, i);

            }
            if(i > 2) {
                String record = "["+ (i + 1) +"]" + " ID: " + userid + " - Username: " + username + " - Points: " + points;
                data.add(record);
            }

        }

        adapter  = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, data);
        lvResults.setAdapter(adapter);


    }

    private void colocarMejores(String username, String points, int index) {
        switch(index){
            case 0:
               tvOro.setText(username + "("+ points + ")");
            break;

            case 1:
                tvPlata.setText(username + "("+ points + ")");
            break;

            case 2:
                tvBronce.setText(username + "("+ points + ")");
               break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}