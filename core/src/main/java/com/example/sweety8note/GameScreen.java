package com.example.sweety8note;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen implements Screen {
    final Sweety8NoteGame game;
    private Texture birdTexture;
    private Texture backgroundTexture;

    private float birdY = 200;          // 鸟的垂直位置
    private float birdVelocity = 0;     // 鸟的速度
    private float gravity = -0.5f;      // 重力加速度

    public GameScreen(Sweety8NoteGame game) {
        this.game = game;
        birdTexture = new Texture("bird.png");             // 替换为你的鸟图
        backgroundTexture = new Texture("background.png"); // 替换为你的背景图
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        // 清屏
        Gdx.gl.glClearColor(1, 1, 1, 1); // 白背景
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 获取音量（防空指针）
        float volume = game.micInput != null ? game.micInput.getVolume() : 0;

        // 若音量超过门限值，则上升
        if (volume > 1000) { // 你可以根据实际声音调整这个值
            birdVelocity = 8f; // 给一个向上的速度
        }

        // 更新鸟的位置
        birdVelocity += gravity; // 重力作用，速度减少
        birdY += birdVelocity;   // 改变y坐标

        // 边界检查（防止掉下屏幕）
        if (birdY < 0) {
            birdY = 0;
            birdVelocity = 0;
        }

        // 绘制画面
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight());
        game.batch.draw(birdTexture, 100, birdY, 64, 64); // 根据你的图片比例适当调整
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
