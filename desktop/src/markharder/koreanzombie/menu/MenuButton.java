package markharder.koreanzombie.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class MenuButton {
    private Texture background;
    private String value;
    private Array<TextureRegion> fontTextureRegions;
    private BitmapFont font;
    private GlyphLayout layout;

    public MenuButton(String value) {
        this.value = value;

        background = new Texture(Gdx.files.internal("images/button_background.png"));
        // set up the fonts
        fontTextureRegions = new Array<TextureRegion>();

        // load Batang1.png to Batang43.png as texture regions
        for (int i = 1; i < 44; i++) {
            fontTextureRegions.add(new TextureRegion(new Texture(Gdx.files.internal("fonts/Batang" + i + ".png"))));
        }

        // load the font file and the texture regions
        font = new BitmapFont(new BitmapFont.BitmapFontData(Gdx.files.internal("fonts/Batang.fnt"), false), fontTextureRegions, false);
        font.setColor(Color.WHITE);

        layout = new GlyphLayout(font, value);
    }

    public void dispose() {
        background.dispose();
        font.dispose();
    }

    public void draw(Batch batch, int x, int y) {
        batch.draw(background, x, y);
        // center the text
        float centerX = x + background.getWidth() / 2 - layout.width / 2;
        float centerY = y + background.getHeight() / 2 + layout.height / 2;
        font.draw(batch, value, centerX, centerY);
    }
}
