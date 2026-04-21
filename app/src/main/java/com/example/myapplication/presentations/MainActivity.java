package com.example.myapplication.presentations;

import android.Manifest;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.datas.permissions.PermissionHelper;
import com.example.myapplication.datas.players.PlayerHelper;
import com.example.myapplication.datas.records.RecordHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    PlayerHelper play;
    RecordHelper record;
    String PathRecords;

    View bthRecords;
    View bthRecordIcon;
    View bthPlay;
    ImageView bthPause;
    ImageView bthPlayIcon;
    private static final String TEMP_FILE_NAME = "record_temp.3gpp";

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
            if (record.MediaRecord != null) {
                if (record.isPause) {
                    record.ResumeRecord();
                }

                record.StopRecord();

                bthPause.setVisibility(View.INVISIBLE);
                ChangecolorBth(false);

                showSaveFileNameDialog();

            } else {
                if (PermissionHelper.CheckPermission(this) == false) {
                    requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 100);
                } else {
                    StartRecord();
                }
            }
        });

        bthPause.setOnClickListener(v -> {
            if (record.MediaRecord == null) return;

            if (record.isPause) {
                record.ResumeRecord();
                bthPause.setColorFilter(Color.parseColor("#000000"));
                ChangecolorBth(true);
            } else {
                record.PauseRecord();
                bthPause.setColorFilter(Color.parseColor("#868686"));
            }
        });

        bthPlay.setOnClickListener(v -> {
            if (play.MediaPlayer == null) {
                play.StartPlay(PathRecords + "/record.3gpp");
                if (play.MediaPlayer != null) {
                    play.MediaPlayer.setOnCompletionListener(EndPlay);
                    bthPlayIcon.setImageResource(R.drawable.pause);
                }
            } else {
                play.StopPlay();
                bthPlayIcon.setImageResource(R.drawable.play);
            }
        });
    }

    private void showSaveFileNameDialog() {
        final EditText input = new EditText(this);
        input.setHint("Введите имя файла");
        input.setText("record_" + System.currentTimeMillis());

        int paddingInDp = 16;
        float scale = getResources().getDisplayMetrics().density;
        int paddingInPx = (int) (paddingInDp * scale + 0.5f);
        input.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Сохранить запись как");
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String fileName = input.getText().toString().trim();

                if (!fileName.isEmpty()) {
                    // Добавляем расширение, если пользователь его не ввел
                    if (!fileName.endsWith(".3gpp")) {
                        fileName += ".3gpp";
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Имя файла не может быть пустым", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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