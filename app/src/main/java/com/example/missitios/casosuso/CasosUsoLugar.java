package com.example.missitios.casosuso;

        import android.app.Activity;
        import android.app.FragmentManager;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.net.Uri;
        import android.os.Build;
        import android.os.Environment;
        import android.provider.MediaStore;
        import android.support.v4.app.FragmentActivity;
        import android.support.v4.content.FileProvider;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.Toast;

        import com.example.missitios.R;
        import com.example.missitios.modelo.GeoPunto;
        import com.example.missitios.modelo.Lugar;
        import com.example.missitios.modelo.Lugares;
        import com.example.missitios.modelo.LugaresBD;
        import com.example.missitios.presentacion.VistaLugarActivity;
        import com.example.missitios.presentacion.VistaLugarFragment;

        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.io.InputStream;
        import java.net.URL;

public  class CasosUsoLugar {

    //private Activity actividad;
    private FragmentActivity actividad;
    private Lugares lugares;
    private LugaresBD lugaresbd;


    public CasosUsoLugar(FragmentActivity  actividad, Lugares lugares) {
        this.actividad = actividad;
        this.lugares =lugares;
    }
    public CasosUsoLugar(FragmentActivity  actividad, LugaresBD lugares) {
        this.actividad = actividad;
        this.lugaresbd =lugares;
    }
    // OPERACIONES BÁSICAS
    // OPERACIONES BÃSICAS
    public void mostrar(int pos) {
        Intent i = new Intent(actividad, VistaLugarActivity.class);
        i.putExtra("pos", pos);
        actividad.startActivity(i);
    }

    public void borrar(final int id) {

        lugares.borrar(id);
        actividad.finish();
    }
    public void editar(final int id) {

        //lugares.(id);
        actividad.finish();
    }

    // INTENCIONES
    public void compartir(Lugar lugar) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT,
                lugar.getNombre() + " - " + lugar.getUrl());
        actividad.startActivity(i);
    }

    public void llamarTelefono(Lugar lugar) {
        actividad.startActivity(new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:" + lugar.getTelefono())));
    }

    public void verPgWeb(Lugar lugar) {
        actividad.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(lugar.getUrl())));
    }

    public final void verMapa(Lugar lugar) {
        double lat = lugar.getPosicion().getLatitud();
        double lon = lugar.getPosicion().getLongitud();
        Uri uri = lugar.getPosicion() != GeoPunto.SIN_POSICION
                ? Uri.parse("geo:" + lat + ',' + lon)
                : Uri.parse("geo:0,0?q=" + lugar.getDireccion());
        actividad.startActivity(new Intent("android.intent.action.VIEW", uri));
    }
    public void galeria(View view) {
        String action;
        if (android.os.Build.VERSION.SDK_INT >= 19) { // API 19 - Kitkat
            action = Intent.ACTION_OPEN_DOCUMENT;
        } else {
            action = Intent.ACTION_PICK;
        }
        Intent intent = new Intent(action,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        actividad.startActivityForResult(intent, 2);
    }
    public void ponerFoto(int pos, String uri, ImageView imageView) {
        Lugar lugar = lugares.elemento(pos);


        lugar.setFoto(uri);
        visualizarFoto(lugar, imageView);
    }

    public void visualizarFoto(Lugar lugar, ImageView imageView) {
        if (lugar.getFoto() != null && !lugar.getFoto().isEmpty()) {
            //imageView.setImageBitmap(reduceBitmap(this, uri, 1024,   1024));
            //imageView.setImageURI(Uri.parse(uri));
            imageView.setImageURI(Uri.parse(lugar.getFoto()));
        } else {
            imageView.setImageBitmap(null);
        }
    }
    public Uri tomarFoto(int codidoSolicitud) {
        try {
            Uri uriUltimaFoto;
            File file = File.createTempFile(
                    "img_" + (System.currentTimeMillis()/ 1000), ".jpg" ,
                    actividad.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            if (Build.VERSION.SDK_INT >= 24) {
                uriUltimaFoto = FileProvider.getUriForFile(
                        actividad, "es.unai.missitios.fileProvider", file);
            } else {
                uriUltimaFoto = Uri.fromFile(file);
            }
            Intent intent   = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra (MediaStore.EXTRA_OUTPUT, uriUltimaFoto);
            actividad.startActivityForResult(intent, codidoSolicitud);
            return uriUltimaFoto;
        } catch (IOException ex) {
            Toast.makeText(actividad, "Error al crear fichero de imagen",
                    Toast.LENGTH_LONG).show();
            return null;
        }
    }
    private Bitmap reduceBitmap(Context contexto, String uri,
                                int maxAncho, int maxAlto) {
        try {
            InputStream input = null;
            Uri u = Uri.parse(uri);
            if (u.getScheme().equals("http") || u.getScheme().equals("https")) {
                input = new URL(uri).openStream();
            } else {
                input = contexto.getContentResolver().openInputStream(u);
            }
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inSampleSize = (int) Math.max(
                    Math.ceil(options.outWidth / maxAncho),
                    Math.ceil(options.outHeight / maxAlto));
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(input, null, options);
        } catch (FileNotFoundException e) {
            Toast.makeText(contexto, "Fichero/recurso de imagen no encontrado",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            Toast.makeText(contexto, "Error accediendo a imagen",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return null;
        }
    }
}
