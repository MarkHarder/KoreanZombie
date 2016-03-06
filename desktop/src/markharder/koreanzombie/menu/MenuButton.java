package markharder.koreanzombie.menu;

import markharder.koreanzombie.App;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class MenuButton {
    private Texture background;
    private String value;
    private GlyphLayout layout;

    public MenuButton(String value) {
        this.value = value;

        background = new Texture(Gdx.files.internal("images/button_background.png"));
        layout = new GlyphLayout(((App) Gdx.app.getApplicationListener()).font, value);
    }

    public void dispose() {
        background.dispose();
    }

    public void draw(Batch batch, int x, int y) {
        batch.draw(background, x, y);
        // center the text
        float centerX = x + background.getWidth() / 2 - layout.width / 2;
        float centerY = y + background.getHeight() / 2 + layout.height / 2;
        ((App) Gdx.app.getApplicationListener()).font.setColor(Color.WHITE);
        ((App) Gdx.app.getApplicationListener()).font.draw(batch, value, centerX, centerY);
    }
}
