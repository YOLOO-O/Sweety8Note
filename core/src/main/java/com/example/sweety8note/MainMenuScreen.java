package com.example.sweety8note;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

public class MainMenuScreen implements Screen {

    private final Sweety8NoteGame game;
    private final SpriteBatch batch;
    private final Stage stage;
    private final Texture background;

    // ---- 纹理缓存，方便最后统一 dispose ----
    private Texture startUpTex, startDownTex;
    private Texture exitUpTex, exitDownTex;

    public MainMenuScreen(Sweety8NoteGame game) {
        this.game = game;
        this.batch = game.getBatch();
        this.stage = new Stage(new ScreenViewport(), batch);
        Gdx.input.setInputProcessor(stage);

        /* ---------- 背景 ---------- */
        background = new Texture("mainmenu.png");

        /* ---------- 按钮纹理 ---------- */
        startUpTex = new Texture("start.png"); // 使用你的按钮素材
        startDownTex = new Texture("start.png"); // 可以使用同一张图
        exitUpTex = new Texture("quit.png");
        exitDownTex = new Texture("quit.png");

        /* ---------- 使用 ImageButton ---------- */
        ImageButton startBtn = makeButton(startUpTex, startDownTex);
        ImageButton exitBtn = makeButton(exitUpTex, exitDownTex);

        /* ---------- 布局 ---------- */
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(startBtn).padBottom(20).row();
        table.add(exitBtn);
        stage.addActor(table);

        /* ---------- 点击逻辑 ---------- */
        startBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    /* 工具：把两张纹理封装成 ImageButton */
    private ImageButton makeButton(Texture up, Texture down) {
        ImageButton.ImageButtonStyle s = new ImageButton.ImageButtonStyle();
        s.up = new TextureRegionDrawable(up);
        s.down = new TextureRegionDrawable(down);
        return new ImageButton(s);
    }

    /* ---------- 渲染 ---------- */
    @Override
    public void render(float dt) {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act(dt);
        stage.draw();
    }

    /* ---------- 其他接口 ---------- */
    @Override
    public void resize(int w, int h) {
        stage.getViewport().update(w, h, true);
    }

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
        startUpTex.dispose();
        startDownTex.dispose();
        exitUpTex.dispose();
        exitDownTex.dispose();
    }
}
