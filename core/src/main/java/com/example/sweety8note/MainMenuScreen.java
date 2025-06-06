package com.example.sweety8note;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public class MainMenuScreen implements Screen {
    final Sweety8NoteGame game;

    private Texture background;
    private BitmapFont font;
    private GlyphLayout layout;

    private Rectangle startButtonBounds;
    private Rectangle exitButtonBounds;

    public MainMenuScreen(Sweety8NoteGame game) {
        this.game = game;

        background = new Texture("mainmenu.png");

        font = new BitmapFont();
        font.getData().setScale(5f);
        font.setColor(Color.BLACK);

        layout = new GlyphLayout();

        float buttonWidth = 300;
        float buttonHeight = 100;
        float centerX = Gdx.graphics.getWidth() / 2f - buttonWidth / 2;

        startButtonBounds = new Rectangle(centerX, 400, buttonWidth, buttonHeight);
        exitButtonBounds = new Rectangle(centerX, 250, buttonWidth, buttonHeight);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        layout.setText(font, "START");
        font.draw(game.batch, layout,
            startButtonBounds.x + (startButtonBounds.width - layout.width) / 2,
            startButtonBounds.y + layout.height + 20);

        layout.setText(font, "EXIT");
        font.draw(game.batch, layout,
            exitButtonBounds.x + (exitButtonBounds.width - layout.width) / 2,
            exitButtonBounds.y + layout.height + 20);

        game.batch.end();

        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (startButtonBounds.contains(touchX, touchY)) {
                game.setScreen(new GameScreen(game));
            } else if (exitButtonBounds.contains(touchX, touchY)) {
                Gdx.app.exit();
            }
        }
    }

    @Override public void dispose() {
        background.dispose();
        font.dispose();
    }

    @Override public void show() {}
    @Override public void hide() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
}
