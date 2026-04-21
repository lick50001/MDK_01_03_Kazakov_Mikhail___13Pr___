package com.example.myapplication.datas.players;

import android.media.MediaPlayer;
import android.util.Log;

public class PlayerHelper {
    public MediaPlayer MediaPlayer;

    public void  StartPlay(String pathFile){
        try {
            MediaPlayer = new MediaPlayer();
            MediaPlayer.setDataSource(pathFile);
            MediaPlayer.start();
        }catch (Exception e){
            Log.e("PLAY", e.getMessage());
        }
    }

    public void StopPlay(){
        if (MediaPlayer != null){
            MediaPlayer.stop();
            MediaPlayer = null;
        }
    }
}
