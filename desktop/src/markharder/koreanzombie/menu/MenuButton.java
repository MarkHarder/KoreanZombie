package markharder.koreanzombie.menu;

import markharder.koreanzombie.App;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class MenuButton {
    private int x;
    private int y;
    private Texture background;
    private String value;
    private GlyphLayout layout;

    public MenuButton(String value, int x, int y) {
        this.value = value;
        this.x = x;
        this.y = y;

        background = new Texture(Gdx.files.internal("images/button_background.png"));
        layout = new GlyphLayout(((App) Gdx.app.getApplicationListener()).font, value);
    }

    public void dispose() {
        background.dispose();
    }

    public String getValue() {
        return value;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean contains(int checkX, int checkY) {
        return (checkX >= x && checkY <= y + background.getHeight() && checkX <= x + background.getWidth() && checkY >= y);
    }

    public void draw(Batch batch) {
        batch.draw(background, x, y);
        // center the text
        float centerX = x + background.getWidth() / 2 - layout.width / 2;
        float centerY = y + background.getHeight() / 2 + layout.height / 2;
        ((App) Gdx.app.getApplicationListener()).font.setColor(Color.WHITE);
        ((App) Gdx.app.getApplicationListener()).font.draw(batch, value, centerX, centerY);
    }
}
