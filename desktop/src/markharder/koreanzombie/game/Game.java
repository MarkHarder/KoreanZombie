package markharder.koreanzombie.game;

import markharder.koreanzombie.App;
import markharder.koreanzombie.korean.KoreanString;
import markharder.koreanzombie.menu.PauseMenu;
import markharder.koreanzombie.menu.SettingsMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

    private PowerBar powerbar;

    private Texture fieldTexture;
    private Sprite field;

    private PauseMenu pauseMenu;
    private boolean paused;

    public Game(SettingsMenu.Difficulty difficulty) {
        this.difficulty = difficulty;

        rand = new Random();
        width = 1440;
        height = 1440 * Gdx.graphics.getHeight() / Gdx.graphics.getWidth();

        questions = new Array<Question>();
        try {
            loadQuestions("lists/Vocab1.txt");
        } catch(IOException e) {
            e.printStackTrace();
        }

        // set up the images
        fieldTexture = new Texture(Gdx.files.internal("images/field.png"));
        field = new Sprite(fieldTexture);
        field.setCenter(getWidth() / 2, getHeight() / 2);

        pauseMenu = new PauseMenu();

        setupGame();
    }

    public void touchUp(int x, int y, int pointer, int button) {
        String value = pauseMenu.touchUp(x, y, pointer, button);
        if (value == "Resume") {
            currentQuestion = randomQuestion();
            paused = !paused;
        } else if (value == "Restart") {
            setupGame();
        } else if (value == "Back to menu") {
            ((App) Gdx.app.getApplicationListener()).setMode(App.Mode.MENU);
        }
    }

    public void keyTyped(char character) {
        if (paused) {
            if ((int) character == 27) {
                // escape key
                // unpause the game
                paused = !paused;
            }
        } else {
            if ((int) character == 27) {
                // escape key
                // pause the game
                paused = !paused;
            } else if ((int) character == 13) {
                // enter key
                // clear the input and give a new question
                // essentially skipping a question
                input.clear();
                currentQuestion = randomQuestion();
            } else if ((int) character == 9) {
                // tab key
                // clear zombies if there is enough power
                if (powerbar.isCharged()) {
                    powerbar.useCharge();
                    zombies.clear();
                }
            } else {
                input.add(character);
            }
        }
    }

    public boolean mouseMoved(int screenX, int screenY) {
        if (paused) {
            pauseMenu.mouseMoved(screenX, screenY);
        }

        return true;
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
        if (paused) {
            return;
        }

        // check the answer
        if (input.toString().equals(currentQuestion.getAnswer())) {
            // clear the input, get a new question, increase power
            input.clear();
            currentQuestion = randomQuestion();
            powerbar.charge();

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
            if (z.reachedCenter()) {
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
        
        float powerbarX = field.getX() + field.getWidth() + 20;
        float powerbarY = field.getY();
        powerbar.draw(batch, powerbarX, powerbarY);

        // draw words
        ((App) Gdx.app.getApplicationListener()).font.setColor(Color.YELLOW);
        ((App) Gdx.app.getApplicationListener()).font.draw(batch, currentQuestion.getDefinition(), 200, getHeight() - 100);
        ((App) Gdx.app.getApplicationListener()).font.draw(batch, input.toString(), 200, getHeight() - 150);

        // draw the zombies
        for (Zombie z : zombies) {
            z.draw(batch, field.getX(), field.getY(), field.getWidth() / 2,
                field.getHeight() / 2);
        }

        if (paused) {
            batch.end();
            ShapeRenderer shape = new ShapeRenderer();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(new Color(0, 0, 0, 0.65f));
            shape.rect(0, 0, getWidth(), getHeight());
            shape.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            batch.begin();

            pauseMenu.draw(batch);
        }
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    private void setupGame() {
        input = new KoreanString();

        lastZombieTime = TimeUtils.nanoTime();
        zombies = new Array<Zombie>();

        lives = new Array<Texture>();
        lives.add(new Texture(Gdx.files.internal("images/heart.png")));
        lives.add(new Texture(Gdx.files.internal("images/heart.png")));
        lives.add(new Texture(Gdx.files.internal("images/heart.png")));

        currentQuestion = randomQuestion();

        powerbar = new PowerBar(field.getHeight());

        paused = false;
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
