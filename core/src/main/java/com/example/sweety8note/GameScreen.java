package com.example.sweety8note;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
    private Texture obstacleStoneTexture1; // 新增障碍物

    private Texture obstacleStoneTexture2;
    private Texture obstacleChairTexture1; // 新增障碍物

    private Texture obstacleChairTexture2; // 新增障碍物

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

    // === 新增音效变量 ===
    private Sound hitSound;
    private Sound flapSound;
    private Music bgmMusic;

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
    private final float OBSTACLE_SPAWN_INTERVAL = 3.75f;
    private final float OBSTACLE_WIDTH = 120;
    private final float OBSTACLE_SPEED = 200;

    private Random random;

    // 新增角色和地图参数
    private String selectedCharacter;
    private String selectedMap;

    // 修改构造函数接收角色和地图
    public GameScreen(Sweety8NoteGame game, String selectedCharacter, String selectedMap, String[] obstacles) {
        this.game = game;
        this.selectedCharacter = selectedCharacter;
        this.selectedMap = selectedMap;

        // 根据角色选择不同的纹理
        switch (selectedCharacter) {
            case "Kobe Character":
                birdOpenTexture = new Texture("kobe.png");
                birdCloseTexture = new Texture("kobe.png");
                break;
            case "Cat Character":
                birdOpenTexture = new Texture("cat.png");
                birdCloseTexture = new Texture("cat.png");
                break;
            case "Bird Character":
                birdOpenTexture = new Texture("bird1open.png");
                birdCloseTexture = new Texture("bird1close.png");
                break;
            default:
                // 默认角色或异常处理
                birdOpenTexture = new Texture("bird1open.png");
                birdCloseTexture = new Texture("bird1close.png");
                break;
        }

        // 根据地图选择不同的背景和障碍物
        switch (selectedMap) {
            case "Background":
                backgroundTexture = new Texture("background.png");
                obstacleHouseTexture = new Texture("obstacle_house.png");
                obstacleTreeTexture = new Texture("obstacle_tree.png");
                break;
            case "Hell":
                backgroundTexture = new Texture("Hell.jpg");
                obstacleStoneTexture1 = new Texture("stone1.png"); // 使用stone1和stone2
                obstacleStoneTexture2 = new Texture("stone2.png");
                break;
            case "House":
                backgroundTexture = new Texture("house.jpg");
                obstacleChairTexture1 = new Texture("chair1.png"); // 使用chair1和chair2
                obstacleChairTexture2 = new Texture("chair2.png");
                break;
            default:
                // 默认背景或异常处理
                backgroundTexture = new Texture("background.png");
                break;
        }

        groundTexture = new Texture("ground.png");

        // 加载障碍物
        loadObstacles(selectedMap);

        font = new BitmapFont();
        font.getData().setScale(2f);
        font.setColor(Color.BLACK);
        layout = new GlyphLayout();

        screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();
        groundHeight = screenHeight / 6f;
        birdY = groundHeight;

        random = new Random();

        // 初始化音效
        hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
        flapSound = Gdx.audio.newSound(Gdx.files.internal("flap.wav"));
        bgmMusic = Gdx.audio.newMusic(Gdx.files.internal("bgm.wav"));
        bgmMusic.setLooping(true);
        bgmMusic.setVolume(0.5f);
        bgmMusic.play();
    }

    private void loadObstacles(String selectedMap) {
        // 根据选择的地图加载相应的障碍物
        switch (selectedMap) {
            case "Background":
                obstacles.add(new Obstacle(new Rectangle(screenWidth, groundHeight, OBSTACLE_WIDTH, 128), obstacleHouseTexture));
                obstacles.add(new Obstacle(new Rectangle(screenWidth + OBSTACLE_WIDTH * 2, groundHeight, OBSTACLE_WIDTH, 128), obstacleTreeTexture));
                break;
            case "Hell":
                obstacles.add(new Obstacle(new Rectangle(screenWidth, groundHeight, OBSTACLE_WIDTH, 128), obstacleStoneTexture1));
                obstacles.add(new Obstacle(new Rectangle(screenWidth + OBSTACLE_WIDTH * 2, groundHeight, OBSTACLE_WIDTH, 128), obstacleStoneTexture2));
                break;
            case "House":
                obstacles.add(new Obstacle(new Rectangle(screenWidth, groundHeight, OBSTACLE_WIDTH, 128), obstacleChairTexture1));
                obstacles.add(new Obstacle(new Rectangle(screenWidth + OBSTACLE_WIDTH * 2, groundHeight, OBSTACLE_WIDTH, 128), obstacleChairTexture2));
                break;
            default:
                break;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!isGameOver) {
            float volume = game.micInput != null ? game.micInput.getVolume() : 0;
            boolean isFlying = false;

            // 向上飞行
            if (volume > 1000) {
                birdVelocity = 8f;
                isFlying = true;

                // 播放小鸟飞行音效
                flapSound.play(0.3f);
            }

            birdVelocity += gravity;
            birdY += birdVelocity;

            // 控制鸟的垂直位置
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

                    // 播放碰撞音效
                    hitSound.play(1.0f);

                    bgmMusic.stop(); // 停止背景音乐
                    game.setScreen(new GameOverScreen(game));
                    return;
                }
            }

            Texture currentBirdTexture = isFlying ? birdOpenTexture : birdCloseTexture;

            // 绘制游戏内容
            game.batch.begin();
            game.batch.draw(backgroundTexture, 0, 0, screenWidth, screenHeight);

            // 仅在选择背景地图时绘制地面
            if (selectedMap.equals("Background")) {
                game.batch.draw(groundTexture, 0, 0, screenWidth, groundHeight);
            }

            // 绘制障碍物
            for (Obstacle ob : obstacles) {
                game.batch.draw(ob.texture, ob.rect.x, ob.rect.y, ob.rect.width, ob.rect.height);
            }

            // 绘制小鸟
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
            obstacleSpawnTimer = 0;

            Texture chosenTexture = random.nextBoolean() ? obstacleTreeTexture : obstacleHouseTexture;
            int scale = 1 + random.nextInt(4);
            float obstacleHeight = 128 * scale;

            Rectangle rect = new Rectangle(screenWidth, groundHeight, OBSTACLE_WIDTH, obstacleHeight);
            obstacles.add(new Obstacle(rect, chosenTexture));
        }

        // 更新障碍物位置
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
        obstacleStoneTexture1.dispose();// 释放stone资源
        obstacleStoneTexture2.dispose();
        obstacleChairTexture1.dispose();  // 释放chair资源
        obstacleChairTexture2.dispose();
        font.dispose();

        // 释放音效资源
        hitSound.dispose();
        flapSound.dispose();
        bgmMusic.dispose();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void show() {}
    @Override public void hide() {}
}

