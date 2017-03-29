package markharder.koreanzombie.game;

import markharder.koreanzombie.App;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PowerBar {
    private final int FULL_CHARGE = 10;
    private final int CHARGE_INCREMENT = 1;
    private final int WIDTH = 30;

    private Texture powerbarOverflow;
    private TextureRegion powerbarBuilding;
    private int power;
    private float height;

    public PowerBar(float height) {
        this.powerbarOverflow = new Texture(Gdx.files.internal("images/powerbar_overflow.png"));
        this.powerbarBuilding = new TextureRegion(new Texture(Gdx.files.internal("images/powerbar.png")));
        this.power = 0;
        this.height = height;
    }

    public boolean isCharged() {
        return power >= FULL_CHARGE;
    }

    public void useCharge() {
        power -= FULL_CHARGE;
    }

    public int getFullCharges() {
        return power / FULL_CHARGE;
    }

    private int getBuildingCharges() {
        return power % FULL_CHARGE;
    }

    public void charge() {
        power += CHARGE_INCREMENT;
    }

    public void draw(Batch batch, float x, float y) {
        // draw powerbar
        if (isCharged()) {
            batch.draw(powerbarOverflow, x + 5, y);
            // TODO: figure out why rgb888 method is necessary
            // without it, the font color is white
            // Color(241f, 196f, 15f, 255f) doesn't work
            ((App) Gdx.app.getApplicationListener()).font.setColor(new Color(Color.rgb888(241f, 196f, 15f)));
            ((App) Gdx.app.getApplicationListener()).font.draw(batch, "x" + getFullCharges(), x + WIDTH, y + height);
        }
        batch.draw(powerbarBuilding, x , y, WIDTH, (height / FULL_CHARGE) * getBuildingCharges());
    }
}
