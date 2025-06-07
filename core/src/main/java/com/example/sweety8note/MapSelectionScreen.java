package com.example.sweety8note;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MapSelectionScreen implements Screen {
    final Sweety8NoteGame game;
    private Stage stage;
    private String selectedCharacter;

    // 定义障碍物数组
    private String[] hellObstacles = {"stone1.png", "stone2.png"};
    private String[] houseObstacles = {"chair1.png", "chair2.png"};

    public MapSelectionScreen(Sweety8NoteGame game, String selectedCharacter) {
        this.game = game;
        this.selectedCharacter = selectedCharacter;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Texture hellTexture = new Texture("Hell.jpg");
        Texture houseTexture = new Texture("house.jpg");
        Texture backgroundTexture = new Texture("background.png");

        // 创建按钮并设置缩放
        ImageButton hellBtn = new ImageButton(new TextureRegionDrawable(hellTexture));
        ImageButton houseBtn = new ImageButton(new TextureRegionDrawable(houseTexture));
        ImageButton backgroundBtn = new ImageButton(new TextureRegionDrawable(backgroundTexture));

        hellBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, selectedCharacter, "Map 1", hellObstacles));
            }
        });

        houseBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, selectedCharacter, "House", houseObstacles));
            }
        });

        backgroundBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, selectedCharacter, "Background", new String[]{}));
            }
        });

        // 设置布局
        Table table = new Table();
        table.setFillParent(true);
        table.top(); // 顶部对齐
        table.center(); // 中心对齐

        // 添加按钮并设置大小
        table.add(hellBtn).size(1000, 500).pad(10);  // 设置按钮大小
        table.add(houseBtn).size(1000, 500).pad(10); // 设置按钮大小
        table.add(backgroundBtn).size(1000, 500).pad(10); // 设置按钮大小

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 绘制舞台内容
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
        stage.dispose();
    }
}
