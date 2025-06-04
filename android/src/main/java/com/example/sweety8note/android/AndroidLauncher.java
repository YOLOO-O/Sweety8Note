package com.example.sweety8note.android;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.example.sweety8note.MicrophoneInput;
import com.example.sweety8note.Sweety8NoteGame;

public class AndroidLauncher extends AndroidApplication implements MicrophoneInput {

    private AudioRecord audioRecord;
    private boolean isRecording = false;
    private Thread recordingThread;
    private float currentVolume = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Android 6.0+ 动态请求权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            } else {
                startRecording();
            }
        } else {
            startRecording(); // Android 6 以下不需要动态权限
        }

        Sweety8NoteGame game = new Sweety8NoteGame();
        game.setMicrophoneInput(this);
        initialize(game, new AndroidApplicationConfiguration());
    }

    private void startRecording() {
        int sampleRate = 8000;
        int bufferSize = AudioRecord.getMinBufferSize(sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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

                    // 输出音量日志（调试用）
                    Log.d("MIC_VOLUME", "Volume: " + currentVolume);
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

    // 动态权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecording();
            } else {
                Log.w("PERMISSION", "用户拒绝了麦克风权限");
            }
        }
    }
}
