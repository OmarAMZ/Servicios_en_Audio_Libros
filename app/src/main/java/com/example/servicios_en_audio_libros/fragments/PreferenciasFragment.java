package com.example.servicios_en_audio_libros.fragments;

import android.os.Bundle;

import com.example.servicios_en_audio_libros.PreferenceFragmentCompat;
import com.example.servicios_en_audio_libros.R;

public class PreferenciasFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
    }
}
