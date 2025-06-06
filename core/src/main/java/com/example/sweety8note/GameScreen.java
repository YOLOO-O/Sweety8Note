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

    private Texture birdOpenTexture;
    private Texture birdCloseTexture;
    private Texture backgroundTexture;
    private Texture groundTexture;

    private float birdX = 100;
    private float birdY;
    private float birdVelocity = 0;
    private float gravity = -0.5f;

    private BitmapFont font;
    private GlyphLayout layout;

    private float groundHeight;
    private float screenHeight;

    public GameScreen(Sweety8NoteGame game) {
        this.game = game;

        birdOpenTexture = new Texture("bird1open.png");
        birdCloseTexture = new Texture("bird1close.png");
        backgroundTexture = new Texture("background.png");
        groundTexture = new Texture("ground.png");

        font = new BitmapFont();
        font.getData().setScale(2f);
        font.setColor(Color.BLACK);
        layout = new GlyphLayout();

        screenHeight = Gdx.graphics.getHeight();
        groundHeight = screenHeight / 6f;
        birdY = groundHeight; // 鸟出生在地面上
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float volume = game.micInput != null ? game.micInput.getVolume() : 0;

        boolean isFlying = false;
        if (volume > 1000) {
            birdVelocity = 8f;
            isFlying = true;
        }

        birdVelocity += gravity;
        birdY += birdVelocity;

        // 限制鸟不能落到地面以下
        if (birdY < groundHeight) {
            birdY = groundHeight;
            birdVelocity = 0;
        }

        // 限制鸟不能飞到屏幕之外
        if (birdY + 64 > screenHeight) {
            birdY = screenHeight - 64;
            birdVelocity = 0;
        }

        // 选择贴图
        Texture currentBirdTexture = isFlying ? birdOpenTexture : birdCloseTexture;

        game.batch.begin();

        // 背景
        game.batch.draw(backgroundTexture, 0, 0,
            Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // 地面贴图，放在最下方
        game.batch.draw(groundTexture, 0, 0,
            Gdx.graphics.getWidth(), groundHeight);

        // 小鸟贴图
        game.batch.draw(currentBirdTexture, birdX, birdY, 64, 64);

        // 显示音量
        String volumeText = String.format("Volume: %.1f", volume);
        layout.setText(font, volumeText);
        font.draw(game.batch, layout, 20, screenHeight - 20);

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
        birdOpenTexture.dispose();
        birdCloseTexture.dispose();
        backgroundTexture.dispose();
        groundTexture.dispose();
        font.dispose();
    }
}
