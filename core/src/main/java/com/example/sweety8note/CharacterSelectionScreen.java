package com.example.sweety8note;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class CharacterSelectionScreen implements Screen {
    private final Sweety8NoteGame game;
    private Stage stage;
    private BitmapFont font;

    public CharacterSelectionScreen(Sweety8NoteGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // 初始化字体
        font = new BitmapFont();
        font.getData().setScale(2f);
        font.setColor(Color.BLACK);

        // 背景颜色设置为白色
        Gdx.gl.glClearColor(1, 1, 1, 1);

        // 初始化按钮和布局
        Texture character1Texture = loadTexture("kobe.png");
        Texture character2Texture = loadTexture("cat.png");
        Texture character3Texture = loadTexture("bird1close.png");

        // 创建按钮
        ImageButton character1Btn = createImageButton(character1Texture, "Kobe Character");
        ImageButton character2Btn = createImageButton(character2Texture, "Cat Character");
        ImageButton character3Btn = createImageButton(character3Texture, "Bird Character");

        // 设置布局
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        // 添加标题
        table.add(createTitleLabel("Select Role")).padBottom(20).row();

        // 不设置按钮大小和位置，直接添加按钮，LibGDX 将使用默认值
        table.add(character1Btn).pad(10);
        table.add(character2Btn).pad(10);
        table.add(character3Btn).pad(10);
        stage.addActor(table);
    }

    private Texture loadTexture(String filePath) {
        try {
            return new Texture(filePath);
        } catch (Exception e) {
            System.err.println("Failed to load texture: " + filePath);
            e.printStackTrace();
            return null; // 返回 null
        }
    }

    private ImageButton createImageButton(Texture texture, String characterName) {
        if (texture == null) {
            System.err.println("Texture is null for character: " + characterName);
            return null; // 不创建按钮
        }

        ImageButton button = new ImageButton(new TextureRegionDrawable(texture));
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MapSelectionScreen(game, characterName));
            }
        });

        return button;
    }

    private Label createTitleLabel(String text) {
        Label.LabelStyle style = new Label.LabelStyle(font, Color.BLACK);
        return new Label(text, style);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // 绘制舞台内容
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        stage.dispose(); // 释放舞台资源
        font.dispose(); // 释放字体资源
    }
}
