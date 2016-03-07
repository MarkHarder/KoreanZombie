package markharder.koreanzombie.game;

import markharder.koreanzombie.App;
import markharder.koreanzombie.korean.KoreanString;
import markharder.koreanzombie.menu.SettingsMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Random;

public class Game {
    private SettingsMenu.Difficulty difficulty;
	private Random rand;
    private float width;
    private float height;

    private long lastZombieTime;
    private Array<Question> questions;
    private Array<Zombie> zombies;
    private Array<Texture> lives;
    private KoreanString input;

    // the currently displayed defintion and anwer
    private Question currentQuestion;

    /*
     * the powerbar
     *   correct answers give power
     *   press tab with 10+ power and clear all active zombies
     */
    private Texture powerbarOverflow;
    private TextureRegion powerbar;
    private int power;

    private Texture fieldTexture;
    private Sprite field;

    public Game(SettingsMenu.Difficulty difficulty) {
        this.difficulty = difficulty;

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

        questions = new Array<Question>();
        try {
            loadQuestions("lists/Vocab1.txt");
        } catch(IOException e) {
            e.printStackTrace();
        }
        currentQuestion = randomQuestion();

        powerbarOverflow = new Texture(Gdx.files.internal("images/powerbar_overflow.png"));
        powerbar = new TextureRegion(new Texture(Gdx.files.internal("images/powerbar.png")));
        power = 0;

        // set up the images
        fieldTexture = new Texture(Gdx.files.internal("images/field.png"));
        field = new Sprite(fieldTexture);
        field.setCenter(width / 2, height / 2);
    }

    public void keyTyped(char character) {

        if ((int) character == 27) {
            // escape key
            // go back to the main menu
            ((App) Gdx.app.getApplicationListener()).setMode(App.Mode.MENU);
        } else if ((int) character == 13) {
            // enter key
            // clear the input and give a new question
            // essentially skipping a question
            input.clear();
            currentQuestion = randomQuestion();
        } else if ((int) character == 9) {
            // tab key
            // clear zombies if there is enough power
            if (power >= 10) {
                power -= 10;
                zombies.clear();
            }
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
        if (input.toString().equals(currentQuestion.getAnswer())) {
            // clear the input, get a new question, increase power
            input.clear();
            currentQuestion = randomQuestion();
            power += 1;

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
                    ((App) Gdx.app.getApplicationListener()).setMode(App.Mode.MENU);
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
        
        // draw powerbar
        if (power >= 10) {
            batch.draw(powerbarOverflow, field.getX() + field.getWidth() + 25, field.getY());
            // TODO: figure out why rgb888 method is necessary
            // without it, the font color is white
            // Color(241f, 196f, 15f, 255f) doesn't work
            ((App) Gdx.app.getApplicationListener()).font.setColor(new Color(Color.rgb888(241f, 196f, 15f)));
            ((App) Gdx.app.getApplicationListener()).font.draw(batch, "x" + (power / 10), field.getX() + field.getWidth() + 50, field.getY() + field.getHeight());
        }
        batch.draw(powerbar, field.getX() + field.getWidth() + 20, field.getY(), 30, 80 * (power % 10));

        // draw words
        ((App) Gdx.app.getApplicationListener()).font.setColor(Color.YELLOW);
        ((App) Gdx.app.getApplicationListener()).font.draw(batch, currentQuestion.getDefinition(), 200, height - 100);
        ((App) Gdx.app.getApplicationListener()).font.draw(batch, input.toString(), 200, height - 150);

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
        // on east difficulty:
        //   70% Slow, 30% Normal, 0% Fast
        // on hard difficulty:
        //   50% Slow, 30% Normal, 20% Fast
		double category = rand.nextInt(10);

        if (difficulty == SettingsMenu.Difficulty.EASY) {
            category -= 1;
        } else if (difficulty == SettingsMenu.Difficulty.HARD) {
            category += 1;
        }

		if (category > 8) {
            return new FastZombie(degree, distance);
		}

		if (category > 5) {
            return new NormalZombie(degree, distance);
		}

		return new SlowZombie(degree, distance);
	}

    /*
     * load defintion/answers from a text file
     * the defintion is on one line and the answer is on the following line
     * the answer may be indented with whitespace
     */
    private void loadQuestions(String filePath) throws IOException {
        BufferedReader inbuffer = Gdx.files.internal(filePath).reader(2048);
        String definition = inbuffer.readLine();
        String answer = inbuffer.readLine();
        while (definition != null && answer != null) {
            questions.add(new Question(definition, answer.trim()));
            definition = inbuffer.readLine();
            answer = inbuffer.readLine();
        }
    }

    private Question randomQuestion() {
        return questions.random();
    }
}
