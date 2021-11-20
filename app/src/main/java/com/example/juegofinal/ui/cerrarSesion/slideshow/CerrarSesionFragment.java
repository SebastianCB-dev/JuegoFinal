package com.example.juegofinal.ui.cerrarSesion.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.juegofinal.MainActivity;
import com.example.juegofinal.User;
import com.example.juegofinal.databinding.FragmentCerrarSesionBinding;
import com.example.juegofinal.databinding.FragmentSlideshowBinding;

public class CerrarSesionFragment extends Fragment {

    private CerrarSesionViewModel slideshowViewModel;
    private FragmentCerrarSesionBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(CerrarSesionViewModel.class);

        binding = FragmentCerrarSesionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                
            }
        });
        User.id = "";
        User.username = "";
        User.last_access = "";
        // Cambio de actividad a Login
       Intent intent = new Intent(getContext(), MainActivity.class);
       startActivity(intent);
       this.onDestroyView();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}