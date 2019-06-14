package com.example.missitios.presentacion;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.missitios.R;

public class PreferenciasFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
