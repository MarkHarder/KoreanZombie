package markharder.koreanzombie;

import markharder.koreanzombie.game.Game;
import markharder.koreanzombie.menu.Menu;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class App implements ApplicationListener {
    private Camera camera;
    private SpriteBatch batch;
    private Menu menu;
    private Game game;
    private Mode currentMode;

    public enum Mode {
        MENU,
        GAME
    }

    @Override
    public void create() {
        // the main menu
        menu = new Menu();

        // the actual game
        game = new Game();

        currentMode = Mode.MENU;

        // set up the camera
        camera = new OrthographicCamera(game.getWidth(), game.getHeight());
        // set the position so that (0, 0) is in the bottom left corner
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        // batch to draw all our textures
        batch = new SpriteBatch();

        // keyboard observer
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            // when a character is typed, add it to the input string
            public boolean keyTyped(char character) {
                if (currentMode == Mode.MENU) {
                    menu.keyTyped(character);
                } else if (currentMode == Mode.GAME) {
                    game.keyTyped(character);
                }
                return true;
            }
        });
    }

    @Override
    public void dispose() {
        batch.dispose();
        menu.dispose();
        game.dispose();
    }

    @Override
    public void render() {
        if (currentMode == Mode.MENU) {
            menu.act();
        } else if (currentMode == Mode.GAME) {
            game.act();
        }

        batch.setProjectionMatrix(camera.combined);

        // clear everything black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        if (currentMode == Mode.MENU) {
            menu.draw(batch);
        } else if (currentMode == Mode.GAME) {
            game.draw(batch);
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = game.getWidth();
        camera.viewportHeight = game.getHeight();
        camera.update();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
