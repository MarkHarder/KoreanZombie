package markharder.koreanzombie.game;

import markharder.koreanzombie.App;
import markharder.koreanzombie.korean.KoreanString;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.Random;

public class Game {
	private Random rand;
    private float width;
    private float height;

    private long lastZombieTime;
    private Array<Zombie> zombies;
    private Array<Texture> lives;
    private KoreanString input;

    private Texture fieldTexture;
    private Sprite field;

    public Game() {
        rand = new Random();
        width = 1440;
        height = 1440 * Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
        lastZombieTime = TimeUtils.nanoTime();
        input = new KoreanString();

        zombies = new Array<Zombie>();
        lives = new Array<Texture>();
        lives.add(new Texture(Gdx.files.internal("images/heart.png")));
        lives.add(new Texture(Gdx.files.internal("images/heart.png")));
        lives.add(new Texture(Gdx.files.internal("images/heart.png")));


        // set up the images
        fieldTexture = new Texture(Gdx.files.internal("images/field.png"));
        field = new Sprite(fieldTexture);
        field.setCenter(width / 2, height / 2);
    }

    public void keyTyped(char character) {
        if ((int) character == 13) {
            // enter key
            input.clear();
        } else if ((int) character == 9) {
            // tab key
            zombies.clear();
        } else {
            input.add(character);
        }
    }

    public void dispose() {
        fieldTexture.dispose();
        for (Texture t : lives) {
            t.dispose();
        }
        for (Zombie z : zombies) {
            z.dispose();
        }
    }

    public void act() {
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

    }

    public void draw(Batch batch) {
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
        App.font.setColor(Color.YELLOW);
        App.font.draw(batch, "price, cost, value", 200, height - 100);
        App.font.draw(batch, input.toString(), 200, height - 150);

        // draw the zombies
        for (Zombie z : zombies) {
            z.draw(batch);
        }
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    /*
     * Return a new Zombie
     *   Weighted random chance of slow, normal, or fast zombie
     *   On the edge of the field
     *   At a random angle
     */
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
}
