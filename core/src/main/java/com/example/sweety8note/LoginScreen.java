package com.example.sweety8note;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class LoginScreen implements Screen {
    final Sweety8NoteGame game;
    private Stage stage;

    public LoginScreen(final Sweety8NoteGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // ========== 字体 ==========
        BitmapFont font = new BitmapFont();
        font.getData().setScale(3.5f);  // 稍微调小点，防止太大

        // ========== 单色贴图 ==========
        Texture whiteTexture = createColoredTexture(Color.WHITE);
        Texture grayTexture = createColoredTexture(Color.GRAY);
        Texture darkGrayTexture = createColoredTexture(Color.DARK_GRAY);

        // ========== 输入框样式 ==========
        TextFieldStyle textFieldStyle = new TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.BLACK;
        textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(grayTexture));
        textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(whiteTexture));
        textFieldStyle.selection = new TextureRegionDrawable(new TextureRegion(darkGrayTexture));

        // ========== 按钮样式 ==========
        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(darkGrayTexture));
        buttonStyle.down = new TextureRegionDrawable(new TextureRegion(grayTexture));

        // ========== 表单控件 ==========
        final TextField usernameField = new TextField("", textFieldStyle);
        final TextField passwordField = new TextField("", textFieldStyle);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        TextButton loginButton = new TextButton("Login", buttonStyle);
        TextButton registerButton = new TextButton("Register", buttonStyle);

        // ========== 布局 ==========
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(new Label("Username:", new Label.LabelStyle(font, Color.BLACK))).left();
        table.add(usernameField).width(200).padBottom(10).row();
        table.add(new Label("Password:", new Label.LabelStyle(font, Color.BLACK))).left();
        table.add(passwordField).width(200).padBottom(20).row();
        table.add(loginButton).colspan(2).padBottom(10).row();
        table.add(registerButton).colspan(2);

        stage.addActor(table);

        // ========== 点击事件 ==========
        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Login按钮被点击！");
                if (!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
                    System.out.println("跳转至 MainMenuScreen...");
                    game.setScreen(new MainMenuScreen(game));
                } else {
                    System.out.println("用户名或密码为空！");
                }
            }
        });

        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Register按钮被点击！");
                if (!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
                    System.out.println("跳转至 MainMenuScreen...");
                    game.setScreen(new MainMenuScreen(game));
                } else {
                    System.out.println("用户名或密码为空！");
                }
            }
        });
    }

    @Override public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
    }

    private Texture createColoredTexture(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }
}
