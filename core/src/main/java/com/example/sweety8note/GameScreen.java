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
    private Texture obstacleTexture;

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

    // 障碍物列表
    private ArrayList<Rectangle> obstacles;
    private float obstacleSpawnTimer = 0;
    private final float OBSTACLE_SPAWN_INTERVAL = 2.5f; // 每隔 2.5 秒出现一个
    private final float OBSTACLE_WIDTH = 80;
    private final float OBSTACLE_HEIGHT = 120;
    private final float OBSTACLE_SPEED = 200; // 每秒移动速度

    private Random random;

    public GameScreen(Sweety8NoteGame game) {
        this.game = game;

        birdOpenTexture = new Texture("bird1open.png");
        birdCloseTexture = new Texture("bird1close.png");
        backgroundTexture = new Texture("background.png");
        groundTexture = new Texture("ground.png");
        obstacleTexture = new Texture("obstacle.png"); // 你需要添加这张图

        font = new BitmapFont();
        font.getData().setScale(2f);
        font.setColor(Color.BLACK);
        layout = new GlyphLayout();

        screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();
        groundHeight = screenHeight / 6f;
        birdY = groundHeight;

        obstacles = new ArrayList<>();
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

            // 边界限制
            if (birdY < groundHeight) {
                birdY = groundHeight;
                birdVelocity = 0;
            }
            if (birdY + 128 > screenHeight) {
                birdY = screenHeight - 128;
                birdVelocity = 0;
            }

            // 更新障碍物
            updateObstacles(delta);

            // 检查碰撞
            Rectangle birdRect = new Rectangle(birdX, birdY, 128, 128);
            for (Rectangle obstacle : obstacles) {
                if (birdRect.overlaps(obstacle)) {
                    Gdx.app.log("GAME", "游戏结束：小鸟撞上障碍物！");
                    game.setScreen(new GameOverScreen(game)); // 切换到游戏结束界面
                    return; // 不再继续渲染
                }
            }

            // 渲染
            Texture currentBirdTexture = isFlying ? birdOpenTexture : birdCloseTexture;

            game.batch.begin();
            game.batch.draw(backgroundTexture, 0, 0, screenWidth, screenHeight);
            game.batch.draw(groundTexture, 0, 0, screenWidth, groundHeight);

            // 障碍物
            for (Rectangle obstacle : obstacles) {
                game.batch.draw(obstacleTexture, obstacle.x, obstacle.y, obstacle.width, obstacle.height);
            }

            game.batch.draw(currentBirdTexture, birdX, birdY, 128, 128);

            // 显示音量
            String volumeText = String.format("Volume: %.1f", volume);
            layout.setText(font, volumeText);
            font.draw(game.batch, layout, 20, screenHeight - 20);
            game.batch.end();
        }
    }

    private void updateObstacles(float delta) {
        obstacleSpawnTimer += delta;

        if (obstacleSpawnTimer >= OBSTACLE_SPAWN_INTERVAL) {
            float y = groundHeight;
            Rectangle obstacle = new Rectangle(screenWidth, y, OBSTACLE_WIDTH, OBSTACLE_HEIGHT);
            obstacles.add(obstacle);
            obstacleSpawnTimer = 0;
        }

        // 移动障碍物
        Iterator<Rectangle> iter = obstacles.iterator();
        while (iter.hasNext()) {
            Rectangle ob = iter.next();
            ob.x -= OBSTACLE_SPEED * delta;

            // 移除屏幕外的障碍物
            if (ob.x + OBSTACLE_WIDTH < 0) {
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
        obstacleTexture.dispose();
        font.dispose();
    }

    // 其他方法保持不变
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void show() {}
    @Override public void hide() {}
}
