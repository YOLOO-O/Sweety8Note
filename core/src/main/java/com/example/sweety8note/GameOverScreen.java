package com.example.sweety8note;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class GameOverScreen implements Screen {
    final Sweety8NoteGame game;
    private Texture background;
    private Stage stage;
    private ImageButton restartButton;

    public GameOverScreen(Sweety8NoteGame game) {
        this.game = game;

        // 加载背景纹理
        background = new Texture("gameover.png"); // 确保文件名正确

        // 创建舞台和重启按钮
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // 创建重启按钮
        Texture restartTexture = new Texture("restart.png"); // 确保文件名正确
        restartButton = new ImageButton(new TextureRegionDrawable(restartTexture));

        // 调整按钮位置
        restartButton.setPosition((Gdx.graphics.getWidth() - restartButton.getWidth()) / 2,
            (Gdx.graphics.getHeight() - restartButton.getHeight()) / 2 - 300); // 下移150单位

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game)); // 返回主菜单
            }
        });

        // 添加按钮到舞台
        stage.addActor(restartButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        // 绘制背景
        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        // 渲染舞台内容（包含重启按钮）
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) {}
    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        background.dispose(); // 释放背景纹理
    }
}
