package com.example.myapplication.presentations;

import android.Manifest;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.datas.permissions.PermissionHelper;
import com.example.myapplication.datas.players.PlayerHelper;
import com.example.myapplication.datas.records.RecordHelper;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    PlayerHelper play;
    RecordHelper record;
    String PathRecords;

    View bthRecords;
    View bthRecordIcon;
    View bthPlay;
    ImageView bthPause;
    ImageView bthPlayIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bthRecords = findViewById(R.id.bthRecord);
        bthRecordIcon = findViewById(R.id.bthRecordIcon);
        bthPause = findViewById(R.id.bthPause);
        bthPlay = findViewById(R.id.bthPlay);
        bthPlayIcon = findViewById(R.id.bthPlayIcon);

        play = new PlayerHelper();
        record = new RecordHelper();

        PathRecords = String.valueOf(getExternalFilesDir(Environment.DIRECTORY_MUSIC));
        File RecordingsDirectory = new File(PathRecords);
        if (!RecordingsDirectory.exists())
            RecordingsDirectory.mkdirs();

        bthRecords.setOnClickListener(v -> {
            if (record.MediaRecord != null){
                if (record.isPause == true){
                    record.ResumeRecord();
                    bthPause.setColorFilter(getColor(R.color.black));
                }
                else{
                    record.StartRecord(PathRecords + "/record.3gpp");
                    bthPause.setVisibility(View.INVISIBLE);
                }
                ChangecolorBth(record.isPause);
            }
            else{
                if (PermissionHelper.CheckPermission(this) == false){
                    requestPermissions(new String[]{
                            Manifest.permission.RECORD_AUDIO}, 100);
                    }else{
                    StartRecord();
                }
            }
        });
        bthPause.setOnClickListener(v -> {
            record.PauseRecord();
            bthPause.setColorFilter(Color.parseColor("#868686"));
        });
        bthPlay.setOnClickListener(v -> {
            if (play.MediaPlayer == null){
                play.StartPlay(PathRecords + "/record.3gpp");
                play.MediaPlayer.setOnCompletionListener(EndPlay);
                bthPlayIcon.setImageResource(R.drawable.pause);
            }
            else
                StartRecord();
        });
        bthPause.setOnClickListener(v -> {
            record.PauseRecord();
            bthPause.setColorFilter(Color.parseColor("#868686"));
            ChangecolorBth(false);
        });
        bthPlay.setOnClickListener(v -> {
            if (play.MediaPlayer == null){
                play.StartPlay(PathRecords + "/record.3gpp");
                play.MediaPlayer.setOnCompletionListener(EndPlay);
                bthPlayIcon.setImageResource(R.drawable.pause);
            }
            else{
                play.StopPlay();
                bthPlayIcon.setImageResource(R.drawable.play);
            }
        });
    }
    MediaPlayer.OnCompletionListener EndPlay = new MediaPlayer.OnCompletionListener(){
        @Override
        public void onCompletion(MediaPlayer mp){
            bthPlayIcon.setImageResource(R.drawable.play);
        }
    };

    void  StartRecord(){
        record.StartRecord(PathRecords + "/record.3gpp");
        bthPause.setVisibility(View.VISIBLE);
        ChangecolorBth(true);
    }
    void ChangecolorBth(boolean state){
        int color = Color.parseColor("#EA4E4F");
        if (state) color = Color.parseColor("#F6F6F6");

        bthRecordIcon.setBackgroundTintList(ColorStateList.valueOf(color));
    }
}