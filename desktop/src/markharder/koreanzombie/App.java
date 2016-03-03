package markharder.koreanzombie;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class App implements ApplicationListener {
    private float GAME_WIDTH;
    private float GAME_HEIGHT;

    private Camera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture fieldTexture;
    private Sprite field;

    @Override
    public void create() {
        GAME_WIDTH = 1440;
        GAME_HEIGHT = 1440 * Gdx.graphics.getHeight() / Gdx.graphics.getWidth();

        // set up the camera
        camera = new OrthographicCamera(GAME_WIDTH, GAME_HEIGHT);
        // set the position so that (0, 0) is in the bottom left corner
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        batch = new SpriteBatch();

        // set up the fonts
        font = new BitmapFont();
        font.setColor(Color.YELLOW);
        font.getData().scale(1.5f);

        // set up the images
        fieldTexture = new Texture(Gdx.files.internal("images/field.png"));
        field = new Sprite(fieldTexture);
        field.setCenter(GAME_WIDTH / 2, GAME_HEIGHT / 2);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        fieldTexture.dispose();
    }

    @Override
    public void render() {
        batch.setProjectionMatrix(camera.combined);

        // clear everything black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        // draw images
        field.draw(batch);

        // draw words
        font.draw(batch, "Word - 단어", 200, GAME_HEIGHT - 100);

        batch.end();
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
