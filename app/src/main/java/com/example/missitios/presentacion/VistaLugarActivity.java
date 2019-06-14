package com.example.missitios.presentacion;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.missitios.Aplicacion;
import com.example.missitios.R;
import com.example.missitios.casosuso.CasosUsoLugar;
import com.example.missitios.modelo.Lugar;
import com.example.missitios.modelo.Lugares;


import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.util.Date;

public class VistaLugarActivity extends AppCompatActivity {
    private Lugares lugares;
    private CasosUsoLugar usoLugar;
    private int pos;
    private Lugar lugar;
    final static int RESULTADO_GALERIA = 2;
    final static int RESULTADO_FOTO = 3;
    final static int RESULTADO_EDITAR = 1;
    private ImageView imageView;
    private Uri uriUltimaFoto;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_lugar);
        Bundle extras = getIntent().getExtras();
        pos = extras.getInt("pos", 0);
        //lugares = ((Aplicacion) getApplication()).lugares;
        usoLugar = new CasosUsoLugar(this, lugares);
        lugar = lugares.elemento(pos);
        imageView = (ImageView) findViewById(R.id.foto);
        actualizaVistas();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vista_lugar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_compartir:
                usoLugar.compartir(lugar);
                return true;
            case R.id.accion_llegar:
                usoLugar.verMapa(lugar);
                return true;
            case R.id.accion_editar:
                lanzarEditado(item.getActionView());
            case R.id.accion_borrar:
               lanzarBorrado(item.getActionView());
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void actualizaVistas() {
        TextView nombre = findViewById(R.id.nombre);
        nombre.setText(lugar.getNombre());
        ImageView logo_tipo = findViewById(R.id.logo_tipo);
        logo_tipo.setImageResource(lugar.getTipo().getRecurso());
        TextView tipo = findViewById(R.id.tipo);
        tipo.setText(lugar.getTipo().getTexto());
        TextView direccion = findViewById(R.id.direccion);
        direccion.setText(lugar.getDireccion());

        if (lugar.getTelefono() == 0) {
            findViewById(R.id.telefono).setVisibility(View.GONE);
        } else {
            findViewById(R.id.telefono).setVisibility(View.VISIBLE);
            TextView telefono = findViewById(R.id.telefono);
            telefono.setText(Integer.toString(lugar.getTelefono()));
        }
        TextView url = findViewById(R.id.mapa);
        url.setText(lugar.getUrl());
        TextView comentario = findViewById(R.id.detalles);
        comentario.setText(lugar.getComentario());
        TextView fecha = findViewById(R.id.calendario);
        fecha.setText(DateFormat.getDateInstance().format(
                new Date(lugar.getFecha())));
        TextView hora = findViewById(R.id.hora);
        hora.setText(DateFormat.getTimeInstance().format(
                new Date(lugar.getFecha())));
        RatingBar valoracion = findViewById(R.id.valoracion);
        valoracion.setRating(lugar.getValoracion());
        valoracion.setOnRatingBarChangeListener(
                new RatingBar.OnRatingBarChangeListener() {
                    @Override public void onRatingChanged(RatingBar ratingBar,
                                                          float valor, boolean fromUser) {
                        lugar.setValoracion(valor);
                    }
                });
        usoLugar.visualizarFoto(lugar, imageView);
    }

    public void lanzarVistaLugar(View view){
        usoLugar.mostrar(0);
    }

    public void lanzarBorrado(View view){

        final Intent i = new Intent(this, VistaLugarActivity.class);
        new AlertDialog.Builder(this)
                .setTitle("Seguro que quieres borrar?")
                .setMessage("indica su id:")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        usoLugar.borrar(pos);
                        startActivity(i);
                    }})
                .setNegativeButton("Cancelar", null)
                .show();



    }
    public void lanzarEditado(View view){

        final Intent i = new Intent(this, EdicionLugarActivity.class);
        i.putExtra("pos",0);
        startActivity(i);

    }


    public void llamarTelefono(View view) {
        usoLugar.llamarTelefono(lugar);
    }
    public void verPgWeb(View view) {
        usoLugar.verPgWeb(lugar);
    }

    public void verMapa(View view) {
        usoLugar.verMapa(lugar);
    }
    public void ponerDeGaleria(View view) {
        //No es view, salia una constante
        usoLugar.galeria(view);
    }

    public void tomarFoto(View view) {
        uriUltimaFoto = usoLugar.tomarFoto(RESULTADO_FOTO);
    }
    public void eliminarFoto(View view) {
        usoLugar.ponerFoto(pos, "", imageView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULTADO_EDITAR) {
            actualizaVistas();
            findViewById(R.id.scrollView1).invalidate();
        } else if (requestCode == RESULTADO_GALERIA) {
            if (resultCode == Activity.RESULT_OK) {
                //Cuidado que no es Imageview
                usoLugar.ponerFoto(pos, data.getDataString(), imageView);
            } else {
                Toast.makeText(this, "Foto no cargada",Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == RESULTADO_FOTO ) {
            if (resultCode == Activity.RESULT_OK && uriUltimaFoto!=null) {
                lugar.setFoto(uriUltimaFoto.toString());
                usoLugar.ponerFoto(pos, lugar.getFoto(), imageView);
            } else {
                Toast.makeText(this, "Error en captura", Toast.LENGTH_LONG).show();
            }
        }
    }



}
