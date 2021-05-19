package com.example.servicios_en_audio_libros;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

public class MyService extends Service implements MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {
    public MyService() {

    }

    Activity activity;
    View view;
    public int libroID;

    private final IBinder binder = new miBinder();
    private final Random random = new Random();

    MediaPlayer mediaPlayer;
    MediaController mediaController;

    public void setMediaPlayer(Activity activity, Libro libro, View view, int idLibro) {
        this.activity=activity;
        this.view= view;
        this.libroID=idLibro;



        if (mediaPlayer != null){
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaController = new MediaController(activity);
        Uri audio = Uri.parse(libro.urlAudio);
        try {
            mediaPlayer.setDataSource(activity, audio);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e("Audiolibros", "ERROR: No se puede reproducir "+audio,e);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        SharedPreferences preferencias = PreferenceManager
                .getDefaultSharedPreferences(activity);
        if (preferencias.getBoolean("pref_autoreproducir", true)) {
            mediaPlayer.start();
        }
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(view);
        mediaController.setPadding(0, 0, 0,110);
        mediaController.setEnabled(true);
        mediaController.show();

        Intent intent = new Intent(activity.getApplicationContext(),MyService.class);


    }

    public void stopping(){
        mediaController.hide();
        try {
            mediaPlayer.stop();
            mediaPlayer.release();
        } catch (Exception e) {
            Log.d("Audiolibros", "Error en mediaPlayer.stop()");
        }
    }

    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        try {
            return mediaPlayer.getCurrentPosition();
        } catch (Exception e) {
            return 0;
        }
    }

    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }

    public MediaController getMediaController(){
        return this.mediaController;
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    //    Clase interfaz para la conexion del servicio con el activity
    public class miBinder extends Binder {
        public MyService getService(){
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "Enlazado", Toast.LENGTH_SHORT).show();
        return binder;
    }

    public void showMedia(){
        mediaController.show();

    }
    public int getRandomNumber(){
        return random.nextInt(100);
    }
}
