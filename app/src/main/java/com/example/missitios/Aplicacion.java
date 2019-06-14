package com.example.missitios;

import android.app.Application;
import android.view.View;

import com.example.missitios.casosuso.AdaptadorLugares;
import com.example.missitios.casosuso.AdaptadorLugaresBD;
import com.example.missitios.modelo.GeoPunto;
import com.example.missitios.modelo.Lugares;
import com.example.missitios.modelo.LugaresBD;

public class Aplicacion extends Application {

    public LugaresBD lugares;
    public AdaptadorLugaresBD adaptador;

    @Override public void onCreate() {
        super.onCreate();
        lugares = new LugaresBD(this);
        adaptador= new AdaptadorLugaresBD(lugares, lugares.extraeCursor());
    }

    public GeoPunto getPosicionActual() {
        return posicionActual;
    }

    public void setPosicionActual(GeoPunto posicionActual) {
        this.posicionActual = posicionActual;
    }

    public GeoPunto posicionActual = new GeoPunto(0.0, 0.0);





    public LugaresBD getLugares() {
        return lugares;
    }


}
