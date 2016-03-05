package markharder.koreanzombie;

import markharder.koreanzombie.korean.KoreanString;
import markharder.koreanzombie.game.Zombie;
import markharder.koreanzombie.game.SlowZombie;
import markharder.koreanzombie.game.NormalZombie;
import markharder.koreanzombie.game.FastZombie;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;

public class App implements ApplicationListener {
	private Random rand;
    private float GAME_WIDTH;
    private float GAME_HEIGHT;
    private long lastZombieTime;

    private Array<TextureRegion> fontTextureRegions;
    private Array<Zombie> zombies;
    private Array<Texture> lives;
    private Camera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture fieldTexture;
    private Sprite field;
    private KoreanString input;

    @Override
    public void create() {
        rand = new Random();
        GAME_WIDTH = 1440;
        GAME_HEIGHT = 1440 * Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
        lastZombieTime = TimeUtils.nanoTime();

        zombies = new Array<Zombie>();
        lives = new Array<Texture>();
        lives.add(new Texture(Gdx.files.internal("images/heart.png")));
        lives.add(new Texture(Gdx.files.internal("images/heart.png")));
        lives.add(new Texture(Gdx.files.internal("images/heart.png")));

        // set up the camera
        camera = new OrthographicCamera(GAME_WIDTH, GAME_HEIGHT);
        // set the position so that (0, 0) is in the bottom left corner
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        batch = new SpriteBatch();

        // set up the fonts
        fontTextureRegions = new Array<TextureRegion>();

        // load Batang1.png to Batang43.png as texture regions
        for (int i = 1; i < 44; i++) {
            fontTextureRegions.add(new TextureRegion(new Texture(Gdx.files.internal("fonts/Batang" + i + ".png"))));
        }

        // load the font file and the texture regions
        font = new BitmapFont(new BitmapFont.BitmapFontData(Gdx.files.internal("fonts/Batang.fnt"), false), fontTextureRegions, false);
        font.setColor(Color.YELLOW);

        // set up the images
        fieldTexture = new Texture(Gdx.files.internal("images/field.png"));
        field = new Sprite(fieldTexture);
        field.setCenter(GAME_WIDTH / 2, GAME_HEIGHT / 2);

        input = new KoreanString();

        // keyboard observer
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            // when a character is typed, add it to the input string
            public boolean keyTyped(char character) {
                input.add(character);
                return true;
            }
        });
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        fieldTexture.dispose();
        for (Texture t : lives) {
            t.dispose();
        }
        for (Zombie z : zombies) {
            z.dispose();
        }
    }

    @Override
    public void render() {
        // check the answer
        if (input.toString().equals("가격")) {
            input.clear();

            // find the zombie closest to the center
            Zombie closestZombie = null;

            for (Zombie z : zombies) {
                if (closestZombie == null) {
                    closestZombie = z;
                } else if (closestZombie.getDistance() > z.getDistance()) {
                    closestZombie = z;
                }
            }

            // if there is no zombie in play, reset the spawn timer
            // otherwise remove the closest zombie
            if (closestZombie == null) {
                lastZombieTime = TimeUtils.nanoTime();
            } else {
                closestZombie.dispose();
                zombies.removeValue(closestZombie, true);
            }
        }

        // update the zombies
        Array<Zombie> delete = new Array<Zombie>();
        for (Zombie z : zombies) {
            z.act(Gdx.graphics.getDeltaTime());
            // if a zombie reaches the center, remove it
            if (z.getDistance() <= 17.5) {
                delete.add(z);

                // remove a life, if no lives remain end the game
                lives.pop();
                if (lives.size == 0) {
                    Gdx.app.exit();
                }
            }
        }

        for (Zombie z : delete) {
            z.dispose();
            zombies.removeValue(z, true);
        }

        // spawn a new zombie after a set time
        if(TimeUtils.nanoTime() - lastZombieTime > 2000000000) {
            lastZombieTime = TimeUtils.nanoTime();
            zombies.add(spawnZombie());
        }

        batch.setProjectionMatrix(camera.combined);

        // clear everything black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        // draw field
        field.draw(batch);

        // draw hearts
        for (int i = 0; i < lives.size; i++) {
            Texture t = lives.get(i);
            float x = field.getX() - t.getWidth() - 20;
            float y = field.getY() + 30 + i * (t.getHeight() + 20);
            batch.draw(t, x, y);
        }

        // draw words
        font.draw(batch, "price, cost, value", 200, GAME_HEIGHT - 100);
        font.draw(batch, input.toString(), 200, GAME_HEIGHT - 150);

        // draw the zombies
        for (Zombie z : zombies) {
            z.draw(batch);
        }

        batch.end();
    }

	private Zombie spawnZombie() {
        // the angle from which the zombie approaches the center
        //   0 is straight right
		int degree = rand.nextInt(360);
        // the distance from the center
        //   (should start at the field edge so here it's just the field radius)
		int distance = ((int) field.getHeight()) / 2;
        // determine what kind of zombie (slow, normal, or fast)
        // 0-5 = Slow (60%)
        // 6-8 = Normal (30%)
        //   9 = Fast (10%)
		double category = rand.nextInt(10);
		double speed = 0.3;

		if (category > 8) {
			speed += 0.3;
            return new FastZombie(degree, distance);
		}

		if (category > 5) {
			speed += 0.3;
            return new NormalZombie(degree, distance);
		}

		return new SlowZombie(degree, distance);
	}

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = GAME_WIDTH;
        camera.viewportHeight = GAME_HEIGHT;
        camera.update();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
