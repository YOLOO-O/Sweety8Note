package com.example.sweety8note.android;

import android.os.Bundle;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.example.sweety8note.Sweety8NoteGame;
import com.example.sweety8note.MicrophoneInput;

/**
 * 启动Android应用并实现麦克风输入功能
 */
public class AndroidLauncher extends AndroidApplication implements MicrophoneInput {

    private AudioRecord audioRecord;
    private boolean isRecording = false;
    private Thread recordingThread;
    private float currentVolume = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 启动录音
        startRecording();

        Sweety8NoteGame game = new Sweety8NoteGame();
        game.setMicrophoneInput(this); // 把自己传过去
        initialize(game, new AndroidApplicationConfiguration());
    }

    private void startRecording() {
        int sampleRate = 8000;
        int bufferSize = AudioRecord.getMinBufferSize(sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT);

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



