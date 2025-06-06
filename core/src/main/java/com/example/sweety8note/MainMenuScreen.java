package com.example.sweety8note;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScreen implements Screen {
    final Sweety8NoteGame game;
    private Stage stage;
    private Texture background;

    public MainMenuScreen(final Sweety8NoteGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        try {
            background = new Texture("mainmenu.png");
            System.out.println("背景图片加载成功！");
        } catch (Exception e) {
            System.out.println("背景图片加载失败！");
            background = new Texture("white_pixel.png");  // 用白图代替
        }

        BitmapFont font = new BitmapFont();
        font.getData().setScale(2f);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;

        try {
            Texture up = new Texture("button_up.png");
            Texture down = new Texture("button_down.png");
            buttonStyle.up = new TextureRegionDrawable(new TextureRegion(up));
            buttonStyle.down = new TextureRegionDrawable(new TextureRegion(down));
        } catch (Exception e) {
            System.out.println("按钮图像加载失败，使用默认颜色！");
            Texture white = new Texture("white_pixel.png");
            buttonStyle.up = new TextureRegionDrawable(new TextureRegion(white)).tint(Color.DARK_GRAY);
            buttonStyle.down = new TextureRegionDrawable(new TextureRegion(white)).tint(Color.GRAY);
        }

        TextButton startButton = new TextButton("Start", buttonStyle);
        TextButton exitButton = new TextButton("Exit", buttonStyle);

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(startButton).padBottom(10).row();
        table.add(exitButton);
        stage.addActor(table);

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Start按钮点击，进入GameScreen！");
                game.setScreen(new GameScreen(game));
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("退出按钮点击！");
                Gdx.app.exit();
            }
        });
    }

    @Override public void show() {}

    @Override
    public void render(float delta) {
        game.batch.begin();
        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        stage.dispose();
        background.dispose();
    }
}
