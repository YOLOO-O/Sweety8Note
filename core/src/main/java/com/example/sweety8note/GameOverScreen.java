package com.example.sweety8note;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class GameOverScreen implements Screen {
    final Sweety8NoteGame game;
    private BitmapFont font;
    private GlyphLayout layout;

    public GameOverScreen(Sweety8NoteGame game) {
        this.game = game;
        font = new BitmapFont();
        font.getData().setScale(6f);
        font.setColor(Color.RED);
        layout = new GlyphLayout();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        layout.setText(font, "Game Over");
        font.draw(game.batch, layout,
            (Gdx.graphics.getWidth() - layout.width) / 2,
            (Gdx.graphics.getHeight() + layout.height) / 2 + 40);

        layout.setText(font, "Tap to Restart");
        font.draw(game.batch, layout,
            (Gdx.graphics.getWidth() - layout.width) / 2,
            (Gdx.graphics.getHeight() - layout.height) / 2 - 40);

        game.batch.end();

        if (Gdx.input.justTouched()) {
            game.setScreen(new GameScreen(game));
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        font.dispose();
    }
}
