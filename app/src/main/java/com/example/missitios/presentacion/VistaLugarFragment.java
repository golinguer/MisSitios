package com.example.missitios.presentacion;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.missitios.R;
import com.example.missitios.casosuso.CasosUsoLugar;
import com.example.missitios.modelo.Lugar;
import com.example.missitios.modelo.Lugares;

import java.text.DateFormat;
import java.util.Date;

public class VistaLugarFragment extends Fragment {


    private Lugares lugares;
    private CasosUsoLugar usoLugar;
    public int pos;
    private Lugar lugar;
    final static int RESULTADO_GALERIA = 2;
    final static int RESULTADO_FOTO = 3;
    final static int RESULTADO_EDITAR = 1;
    private ImageView imageView;
    private Uri uriUltimaFoto;
    public View v;

    @Override public View onCreateView(LayoutInflater inflador,
                                       ViewGroup contenedor,Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View vista = inflador.inflate(R.layout.vista_lugar,contenedor,false);
        return vista;
    }

    @Override public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            pos = extras.getInt("pos", 0);
            actualizaVistas();
        }
    }




   /* public boolean onCreateOptionsMenu(Menu menu) {
        this.getActivity().getMenuInflater().inflate(R.menu.vista_lugar, menu);
        return true;
        inflador.inflate(R.menu.vista_lugar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.vista_lugar, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
        View v = getView();
        TextView nombre = v.findViewById(R.id.nombre);
        nombre.setText(lugar.getNombre());
        ImageView logo_tipo = v.findViewById(R.id.logo_tipo);
        logo_tipo.setImageResource(lugar.getTipo().getRecurso());
        TextView tipo = v.findViewById(R.id.tipo);
        tipo.setText(lugar.getTipo().getTexto());
        TextView direccion = v.findViewById(R.id.direccion);
        direccion.setText(lugar.getDireccion());

        if (lugar.getTelefono() == 0) {
            v.findViewById(R.id.telefono).setVisibility(View.GONE);
        } else {
            v.findViewById(R.id.telefono).setVisibility(View.VISIBLE);
            TextView telefono = v.findViewById(R.id.telefono);
            telefono.setText(Integer.toString(lugar.getTelefono()));
        }
        TextView url = v.findViewById(R.id.mapa);
        url.setText(lugar.getUrl());
        TextView comentario = v.findViewById(R.id.detalles);
        comentario.setText(lugar.getComentario());
        TextView fecha = v.findViewById(R.id.calendario);
        fecha.setText(DateFormat.getDateInstance().format(
                new Date(lugar.getFecha())));
        TextView hora = v.findViewById(R.id.hora);
        hora.setText(DateFormat.getTimeInstance().format(
                new Date(lugar.getFecha())));
        RatingBar valoracion = v.findViewById(R.id.valoracion);
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

        final Intent i = new Intent(this.getActivity(), VistaLugarActivity.class);
        new AlertDialog.Builder(this.getActivity())
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

        final Intent i = new Intent(this.getActivity(), EdicionLugarActivity.class);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULTADO_EDITAR) {
            actualizaVistas();
            v.findViewById(R.id.scrollView1).invalidate();
        } else if (requestCode == RESULTADO_GALERIA) {
            if (resultCode == Activity.RESULT_OK) {
                //Cuidado que no es Imageview
                usoLugar.ponerFoto(pos, data.getDataString(), imageView);
            } else {
                Toast.makeText(this.getActivity(), "Foto no cargada",Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == RESULTADO_FOTO ) {
            if (resultCode == Activity.RESULT_OK && uriUltimaFoto!=null) {
                lugar.setFoto(uriUltimaFoto.toString());
                usoLugar.ponerFoto(pos, lugar.getFoto(), imageView);
            } else {
                Toast.makeText(this.getActivity(), "Error en captura", Toast.LENGTH_LONG).show();
            }
        }
    }


}
