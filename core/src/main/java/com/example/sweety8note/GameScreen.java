package com.example.sweety8note;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen implements Screen {
    final Sweety8NoteGame game;
    private Texture birdTexture;
    private Texture backgroundTexture;

    private float birdY = 200;
    private float birdVelocity = 0;
    private float gravity = -0.5f;

    public GameScreen(Sweety8NoteGame game) {
        this.game = game;
        birdTexture = new Texture("bird.png");
        backgroundTexture = new Texture("background.png");
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float volume = game.micInput != null ? game.micInput.getVolume() : 0;
        System.out.println("🎧 当前音量：" + volume); // 调试信息

        if (volume > 500) { // 降低门限
            birdVelocity = 8f;
        }

        birdVelocity += gravity;
        birdY += birdVelocity;

        if (birdY < 0) {
            birdY = 0;
            birdVelocity = 0;
        }

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight());
        game.batch.draw(birdTexture, 100, birdY, 64, 64); // 控制贴图大小
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    @Override
    public void dispose() {
        birdTexture.dispose();
        backgroundTexture.dispose();
    }
}
