package com.example.sweety8note;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GameScreen implements Screen {
    final Sweety8NoteGame game;

    private Texture birdOpenTexture;
    private Texture birdCloseTexture;
    private Texture backgroundTexture;
    private Texture groundTexture;
    private Texture obstacleTreeTexture;
    private Texture obstacleHouseTexture;

    private float birdX = 100;
    private float birdY;
    private float birdVelocity = 0;
    private float gravity = -0.5f;

    private BitmapFont font;
    private GlyphLayout layout;

    private float groundHeight;
    private float screenHeight;
    private float screenWidth;

    private boolean isGameOver = false;

    private class Obstacle {
        Rectangle rect;
        Texture texture;

        Obstacle(Rectangle rect, Texture texture) {
            this.rect = rect;
            this.texture = texture;
        }
    }

    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private float obstacleSpawnTimer = 0;
    private final float OBSTACLE_SPAWN_INTERVAL = 3.75f; // 障碍物生成间隔
    private final float OBSTACLE_WIDTH = 80;
    private final float OBSTACLE_SPEED = 200; // 障碍物移动速度

    private Random random;

    public GameScreen(Sweety8NoteGame game) {
        this.game = game;

        birdOpenTexture = new Texture("bird1open.png");
        birdCloseTexture = new Texture("bird1close.png");
        backgroundTexture = new Texture("background.png");
        groundTexture = new Texture("ground.png");
        obstacleTreeTexture = new Texture("obstacle_tree.png");
        obstacleHouseTexture = new Texture("obstacle_house.png");

        font = new BitmapFont();
        font.getData().setScale(2f);
        font.setColor(Color.BLACK);
        layout = new GlyphLayout();

        screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();
        groundHeight = screenHeight / 6f;
        birdY = groundHeight;

        random = new Random();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!isGameOver) {
            float volume = game.micInput != null ? game.micInput.getVolume() : 0;
            boolean isFlying = false;

            if (volume > 1000) {
                birdVelocity = 8f;
                isFlying = true;
            }

            birdVelocity += gravity;
            birdY += birdVelocity;

            if (birdY < groundHeight) {
                birdY = groundHeight;
                birdVelocity = 0;
            }
            if (birdY + 128 > screenHeight) {
                birdY = screenHeight - 128;
                birdVelocity = 0;
            }

            updateObstacles(delta);

            Rectangle birdRect = new Rectangle(birdX, birdY, 128, 128);
            for (Obstacle ob : obstacles) {
                if (birdRect.overlaps(ob.rect)) {
                    Gdx.app.log("GAME", "游戏结束：小鸟撞上障碍物！");
                    game.setScreen(new GameOverScreen(game));
                    return;
                }
            }

            Texture currentBirdTexture = isFlying ? birdOpenTexture : birdCloseTexture;

            game.batch.begin();
            game.batch.draw(backgroundTexture, 0, 0, screenWidth, screenHeight);
            game.batch.draw(groundTexture, 0, 0, screenWidth, groundHeight);

            for (Obstacle ob : obstacles) {
                game.batch.draw(ob.texture, ob.rect.x, ob.rect.y, ob.rect.width, ob.rect.height);
            }

            game.batch.draw(currentBirdTexture, birdX, birdY, 128, 128);

            String volumeText = String.format("Volume: %.1f", volume);
            layout.setText(font, volumeText);
            font.draw(game.batch, layout, 20, screenHeight - 20);
            game.batch.end();
        }
    }

    private void updateObstacles(float delta) {
        obstacleSpawnTimer += delta;

        if (obstacleSpawnTimer >= OBSTACLE_SPAWN_INTERVAL) {
            obstacleSpawnTimer = 0;

            Texture chosenTexture = random.nextBoolean() ? obstacleTreeTexture : obstacleHouseTexture;

            int scale = 1 + random.nextInt(4); // 1 ~ 4
            float obstacleHeight = 128 * scale;

            Rectangle rect = new Rectangle(screenWidth, groundHeight, OBSTACLE_WIDTH, obstacleHeight);
            obstacles.add(new Obstacle(rect, chosenTexture));
        }

        Iterator<Obstacle> iter = obstacles.iterator();
        while (iter.hasNext()) {
            Obstacle ob = iter.next();
            ob.rect.x -= OBSTACLE_SPEED * delta;
            if (ob.rect.x + OBSTACLE_WIDTH < 0) {
                iter.remove();
            }
        }
    }

    @Override
    public void dispose() {
        birdOpenTexture.dispose();
        birdCloseTexture.dispose();
        backgroundTexture.dispose();
        groundTexture.dispose();
        obstacleTreeTexture.dispose();
        obstacleHouseTexture.dispose();
        font.dispose();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void show() {}
    @Override public void hide() {}
}
