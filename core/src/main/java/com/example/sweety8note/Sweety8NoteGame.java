package com.example.sweety8note;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Sweety8NoteGame extends Game {

    public SpriteBatch batch;
    public MicrophoneInput micInput; // ⬅️ 加在类内部

    @Override
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new GameScreen(this));
    }

    public void setMicrophoneInput(MicrophoneInput input) {
        this.micInput = input;
    }

    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();
    }
}
