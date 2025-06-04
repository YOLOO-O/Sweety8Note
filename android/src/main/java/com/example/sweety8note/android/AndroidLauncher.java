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

        // 请求麦克风权限（Android 6.0+）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            } else {
                startRecording();
            }
        } else {
            startRecording();
        }

        Sweety8NoteGame game = new Sweety8NoteGame();
        game.setMicrophoneInput(this);
        initialize(game, new AndroidApplicationConfiguration());
    }

    private void startRecording() {
        int sampleRate = 8000;
        int bufferSize = AudioRecord.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT);

        if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
            Log.e("MIC", "无效的最小 bufferSize: " + bufferSize);
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            Log.e("MIC", "未授予麦克风权限");
            return;
        }

        try {
            audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize * 2  // 更保险的 buffer 空间
            );

            if (audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
                Log.e("MIC", "AudioRecord 初始化失败！");
                return;
            }

            audioRecord.startRecording();
            isRecording = true;
            Log.i("MIC", "AudioRecord 开始录音");

            recordingThread = new Thread(() -> {
                short[] buffer = new short[bufferSize];
                while (isRecording) {
                    int read = audioRecord.read(buffer, 0, buffer.length);
                    if (read > 0) {
                        long sum = 0;
                        for (int i = 0; i < read; i++) {
                            sum += buffer[i] * buffer[i];
                        }
                        double rms = Math.sqrt(sum / (double) read);
                        currentVolume = (float) rms;

                        Log.d("MIC_VOLUME", "Volume: " + currentVolume);
                    } else {
                        Log.w("MIC_VOLUME", "读取失败，返回值: " + read);
                    }

                    try {
                        Thread.sleep(100); // 降低 CPU 占用
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            recordingThread.start();

        } catch (Exception e) {
            Log.e("MIC", "创建 AudioRecord 失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public float getVolume() {
        return currentVolume;
    }

    @Override
    protected void onDestroy() {
        isRecording = false;
        if (audioRecord != null) {
            try {
                audioRecord.stop();
                audioRecord.release();
            } catch (Exception e) {
                Log.e("MIC", "停止或释放 audioRecord 出错: " + e.getMessage());
            }
        }
        super.onDestroy();
    }

    // 权限回调
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
