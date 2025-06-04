package com.example.sweety8note;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen implements Screen {
    final Sweety8NoteGame game;
    private Texture birdTexture;
    private Texture backgroundTexture;

    public GameScreen(Sweety8NoteGame game) {
        this.game = game;
        birdTexture = new Texture("bird.png");         // 替换为你的鸟贴图
        backgroundTexture = new Texture("background.png"); // 替换为你的背景贴图
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1); // 白色背景
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0);
        game.batch.draw(birdTexture, 100, 200); // 临时位置
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
