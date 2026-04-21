package com.example.myapplication.datas.records;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;

public class RecordHelper {
    public MediaRecorder MediaRecord;
    public  Boolean isPause = false;
    public boolean isRecording = false;
    public void ResumeRecord(){
        if (MediaRecord != null){
            MediaRecord.resume();
            isPause = false;
        }
    }

    public void StopRecord() {
        if (MediaRecord != null) {
            try {
                MediaRecord.stop();
            } catch (IllegalStateException e) {
                // Игнорируем, если уже остановлен
            } finally {
                MediaRecord.release();
                MediaRecord = null;
                // Сбрасываем флаги
                isRecording = false;
                isPause = false;
            }
        }
    }

    public void PauseRecord(){
        if (MediaRecord != null){
            MediaRecord.pause();
            isPause = true;
        }
    }
    public void StartRecord(String pathFile){
        try {
            File OutFile = new File(pathFile);
            if (OutFile.exists()) OutFile.delete();
            MediaRecord = new MediaRecorder();
            MediaRecord.setAudioSource(MediaRecorder.AudioSource.MIC);
            MediaRecord.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            MediaRecord.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            MediaRecord.setOutputFile(pathFile);
            MediaRecord.prepare();
            MediaRecord.start();

            isPause = false;
            isRecording = true;
        }catch (Exception e){
            Log.e("RECORD", e.getMessage());
        }
    }
}
