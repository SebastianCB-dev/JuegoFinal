package com.example.juegofinal.ui.cerrarSesion.slideshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CerrarSesionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CerrarSesionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Cerrar Sesion fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}