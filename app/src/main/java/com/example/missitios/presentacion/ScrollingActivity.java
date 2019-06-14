package com.example.missitios.presentacion;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.missitios.Aplicacion;
import com.example.missitios.R;
import com.example.missitios.casosuso.AdaptadorLugares;
import com.example.missitios.casosuso.AdaptadorLugaresBD;
import com.example.missitios.casosuso.CasosUsoLocalizacion;
import com.example.missitios.casosuso.CasosUsoLugar;
import com.example.missitios.modelo.LugaresBD;

public class ScrollingActivity extends AppCompatActivity {

    private Button bAcercaDe;
    private Button bSalir;
    private CasosUsoLugar usoLugar;
    //public AdaptadorLugares adaptador;
    MediaPlayer mp;
    private static final int SOLICITUD_PERMISO_LOCALIZACION = 1;
    private CasosUsoLocalizacion usoLocalizacion;
    private LugaresBD lugares;
    private AdaptadorLugaresBD adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //bAcercaDe = findViewById(R.id.button03);
       /* bAcercaDe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                lanzarAcercaDe(null);
            }
        });*/

        adaptador = ((Aplicacion) getApplication()).adaptador;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        usoLugar=new CasosUsoLugar(this,((Aplicacion) getApplication()).getLugares());
        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
        mp = MediaPlayer.create(this, R.raw.audio);
        mp.start();
        usoLocalizacion = new CasosUsoLocalizacion(this,
                SOLICITUD_PERMISO_LOCALIZACION);

    }

    @Override protected void onSaveInstanceState(Bundle guardarEstado) {
        super.onSaveInstanceState(guardarEstado);
        if (mp != null) {
            guardarEstado.putInt("posicion", mp.getCurrentPosition());
        }
    }

    @Override protected void onRestoreInstanceState(Bundle recEstado) {
        super.onRestoreInstanceState(recEstado);
        if (recEstado != null && mp != null) {
            int pos = recEstado.getInt("posicion");
            mp.seekTo(pos);
        }
    }



    @Override protected void onStart() {
        super.onStart();
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
    }

    @Override protected void onResume() {
        super.onResume();
        mp.start();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
        usoLocalizacion.activar();
    }

    @Override protected void onPause() {
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        mp.pause();
        usoLocalizacion.desactivar();
        super.onPause();
    }

    @Override protected void onStop() {
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();

        super.onStop();
    }

    @Override protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
    }

    @Override protected void onDestroy() {
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            lanzarPreferencias(null);
            return true;
        }
        if (id == R.id.acercaDe) {
            lanzarAcercaDe(null);
            return true;
        }
        if (id == R.id.menu_buscar) {
            lanzarVistaLugar(null);
            return true;
        }
        if (id==R.id.menu_mapa) {
            Intent intent = new Intent(this, MapaActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    public void lanzarAcercaDe(View view){
        Intent i = new Intent(this, AcercaDeActivity.class);
        startActivity(i);
    }

    public void lanzarPreferencias(View view){

        Intent i = new Intent(this, PreferenciasActivity.class);
        startActivity(i);
    }

    public void mostrarPreferencias(View view){
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(this);
        String s = "notificaciones: "+ pref.getBoolean("notificaciones",true)
                +", máximo a listar: " + pref.getString("maximo","?");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    public void salir(View view){
        finish();
    }


    /*public void lanzarVistaLugar(View view){
        Intent i = new Intent(this, VistaLugarActivity.class);
        i.putExtra("pos",0);
        startActivity(i);
    }*/

    public void lanzarVistaLugar(View view){
        final EditText entrada = new EditText(this);
        entrada.setText("0");
        final Intent i = new Intent(this, VistaLugarActivity.class);
        new AlertDialog.Builder(this)
                .setTitle("Selección de lugar")
                .setMessage("indica su id:")
                .setView(entrada)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int id = Integer.parseInt (entrada.getText().toString());


                        i.putExtra("pos",0);
                        startActivity(i);
                    }})
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override public void onRequestPermissionsResult(int requestCode,
                                                     String[] permissions, int[] grantResults) {
        if (requestCode == SOLICITUD_PERMISO_LOCALIZACION
                && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            usoLocalizacion.permisoConcedido();

    }

}