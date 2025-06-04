package com.example.sweety8note.android;

import android.Manifest;
import android.os.Bundle;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.example.sweety8note.Sweety8NoteGame;
import com.example.sweety8note.MicrophoneInput;

public class AndroidLauncher extends AndroidApplication implements MicrophoneInput {

    private AudioRecord audioRecord;
    private boolean isRecording = false;
    private Thread recordingThread;
    private float currentVolume = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 动态请求权限
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.RECORD_AUDIO}, 1);
        }

        startRecording();

        Sweety8NoteGame game = new Sweety8NoteGame();
        game.setMicrophoneInput(this);
        initialize(game, new AndroidApplicationConfiguration());
    }

    private void startRecording() {
        int sampleRate = 8000;
        int bufferSize = AudioRecord.getMinBufferSize(sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT);

        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize);

        audioRecord.startRecording();
        isRecording = true;

        recordingThread = new Thread(() -> {
            byte[] buffer = new byte[bufferSize];
            while (isRecording) {
                int read = audioRecord.read(buffer, 0, buffer.length);
                if (read > 0) {
                    long sum = 0;
                    for (int i = 0; i < read; i++) {
                        sum += buffer[i] * buffer[i];
                    }
                    double rms = Math.sqrt(sum / (double) read);
                    currentVolume = (float) rms;

                    // 打印调试音量值
                    Log.d("🎤MIC_VOLUME", "当前音量：" + currentVolume);
                }
            }
        });
        recordingThread.start();
    }

    @Override
    public float getVolume() {
        return currentVolume;
    }

    @Override
    protected void onDestroy() {
        isRecording = false;
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
        }
        super.onDestroy();
    }
}
