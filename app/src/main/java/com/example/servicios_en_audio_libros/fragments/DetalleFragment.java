package com.example.servicios_en_audio_libros.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.example.servicios_en_audio_libros.Aplicacion;
import com.example.servicios_en_audio_libros.Libro;
import com.example.servicios_en_audio_libros.MainActivity;
import com.example.servicios_en_audio_libros.MyService;
import com.example.servicios_en_audio_libros.R;

public class DetalleFragment extends Fragment implements View.OnTouchListener{
    public final String NotiChanelID = "audioLibros2";
    public static String ARG_ID_LIBRO = "id_libro";

    MyService servicio ;
    boolean mBound = false;
    int libroID;


    @Override public View onCreateView(LayoutInflater inflador, ViewGroup
            contenedor, Bundle savedInstanceState) {
        Intent intent = new Intent(getActivity().getApplicationContext(),MyService.class);
        getActivity().bindService(intent,connection, Context.BIND_AUTO_CREATE);
        Libro libro;
        View vista = inflador.inflate( R.layout.fragment_detalle,
                contenedor, false);
        Bundle args = getArguments();
        if (args != null) {
            int position = args.getInt(ARG_ID_LIBRO);
            libroID =args.getInt(ARG_ID_LIBRO);
            libro = ((Aplicacion) getActivity().getApplication())
                    .getVectorLibros().elementAt(position);
            ponInfoLibro(position, vista);
        } else {
            libroID=0;
            ponInfoLibro(0, vista);
            libro = ((Aplicacion) getActivity().getApplication())
                    .getVectorLibros().elementAt(0);

        }

        return vista;
    }

    private void ponInfoLibro(int id, View vista) {
        Libro libro = ((Aplicacion) getActivity().getApplication())
                .getVectorLibros().elementAt(id);
        ((TextView) vista.findViewById(R.id.titulo)).setText(libro.titulo);
        ((TextView) vista.findViewById(R.id.autor)).setText(libro.autor);
        ((ImageView) vista.findViewById(R.id.portada))
                .setImageResource(libro.recursoImagen);
        vista.setOnTouchListener(this);

    }

    public void ponInfoLibro(int id) {
        ponInfoLibro(id, getView());
    }


    @Override public boolean onTouch(View vista, MotionEvent evento) {

        try {
            if (servicio.isPlaying()){
                if (servicio.libroID != this.libroID){
                    if(libroID>=0){
                        NotificationCompat.Builder noti = new NotificationCompat.Builder(getActivity().getApplicationContext(),
                                NotiChanelID).setContentTitle("AudioLibros").setContentText("Reproducionedo Audio").setPriority(NotificationCompat.PRIORITY_LOW);
                        servicio.startForeground(0,noti.build());
                        Libro libro = ((Aplicacion) getActivity().getApplication())
                                .getVectorLibros().elementAt(libroID);
                        servicio.setMediaPlayer(getActivity(),libro,this.getView(),libroID);
                    }
                }else {
                    servicio.showMedia();
                }

            }

        }catch (Exception e){
            if(libroID>=0){
                NotificationCompat.Builder noti = new NotificationCompat.Builder(getActivity().getApplicationContext(),NotiChanelID).setContentTitle("AudioLibros").setContentText("Reproducionedo Audio").setPriority(NotificationCompat.PRIORITY_LOW);
                servicio.startForeground(0,noti.build());
                Libro libro = ((Aplicacion) getActivity().getApplication())
                        .getVectorLibros().elementAt(libroID);
                servicio.setMediaPlayer(getActivity(),libro,this.getView(),libroID);
            }
        }


        return false;
    }
    @Override public void onStop() {

        super.onStop();

    }

    @Override
    public void onDestroy() {
        servicio.stopSelf();
        super.onDestroy();
    }

    @Override public void onResume(){
        DetalleFragment detalleFragment = (DetalleFragment)
                getFragmentManager().findFragmentById(R.id.detalle_fragment);
        if (detalleFragment == null ) {
            ((MainActivity) getActivity()).mostrarElementos(false);
        }
        super.onResume();
    }


    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.miBinder binder = (MyService.miBinder) service;
            servicio = binder.getService();
            mBound=true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound=false;
        }
    };
}
