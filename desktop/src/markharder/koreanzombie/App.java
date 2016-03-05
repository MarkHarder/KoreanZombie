package markharder.koreanzombie;

import markharder.koreanzombie.game.Game;

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
    private Game game;

    @Override
    public void create() {
        // the actual game
        game = new Game();

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
                game.keyTyped(character);
                return true;
            }
        });
    }

    @Override
    public void dispose() {
        batch.dispose();
        game.dispose();
    }

    @Override
    public void render() {
        game.act();

        batch.setProjectionMatrix(camera.combined);

        // clear everything black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        game.draw(batch);

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
