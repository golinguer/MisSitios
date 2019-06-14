package com.example.missitios.presentacion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.missitios.Aplicacion;
import com.example.missitios.R;
import com.example.missitios.casosuso.AdaptadorLugaresBD;
import com.example.missitios.casosuso.CasosUsoLugar;
import com.example.missitios.modelo.LugaresBD;


public class SelectorFragment extends Fragment {
    private LugaresBD lugares;
    private AdaptadorLugaresBD adaptador;
    private CasosUsoLugar usoLugar;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflador, ViewGroup contenedor,
                             Bundle savedInstanceState) {
        View vista = inflador.inflate(R.layout.fragment_selector,
                contenedor, false);
        recyclerView = vista.findViewById(R.id.recycler_view);
        return vista;
    }



    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        lugares = ((Aplicacion) getActivity().getApplication()).lugares;
        adaptador = ((Aplicacion) getActivity().getApplication()).adaptador;
        usoLugar = new CasosUsoLugar(this.getActivity(), lugares);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adaptador);
        adaptador.setOnItemClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int pos = (Integer)(v.getTag());
                usoLugar.mostrar(pos);
            }
        });
    }
}


