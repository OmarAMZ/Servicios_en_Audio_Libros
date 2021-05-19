package com.example.servicios_en_audio_libros;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.servicios_en_audio_libros.fragments.PreferenciasFragment;

public class PreferenceFragmentCompat extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.
                content, new PreferenciasFragment()).commit();
    }
}
