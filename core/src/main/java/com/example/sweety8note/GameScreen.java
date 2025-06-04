package com.example.sweety8note;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.Color;

public class GameScreen implements Screen {
    final Sweety8NoteGame game;
    private Texture birdTexture;
    private Texture backgroundTexture;

    private float birdY = 200;          // 鸟的垂直位置
    private float birdVelocity = 0;     // 鸟的速度
    private float gravity = -0.5f;      // 重力加速度

    private BitmapFont font;
    private GlyphLayout layout;

    public GameScreen(Sweety8NoteGame game) {
        this.game = game;
        birdTexture = new Texture("bird.png");             // 替换为你的鸟图
        backgroundTexture = new Texture("background.png"); // 替换为你的背景图

        // 创建字体对象
        font = new BitmapFont();  // 默认字体，可自定义
        font.getData().setScale(2f); // 放大字体
        font.setColor(Color.BLACK);
        layout = new GlyphLayout();
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float volume = game.micInput != null ? game.micInput.getVolume() : 0;

        if (volume > 1000) {
            birdVelocity = 8f;
        }

        birdVelocity += gravity;
        birdY += birdVelocity;

        if (birdY < 0) {
            birdY = 0;
            birdVelocity = 0;
        }

        game.batch.begin();

        // 画背景
        game.batch.draw(backgroundTexture, 0, 0,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight());

        // 画小鸟
        game.batch.draw(birdTexture, 100, birdY, 64, 64);

        // 画音量值
        String volumeText = String.format("Volume: %.1f", volume);
        layout.setText(font, volumeText);
        font.draw(game.batch, layout, 20, Gdx.graphics.getHeight() - 20); // 左上角

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
        font.dispose();
    }
}
