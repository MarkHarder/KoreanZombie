package markharder.koreanzombie.menu;

import markharder.koreanzombie.App;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

public class Menu {
    private Array<MenuButton> buttons;

    public Menu() {
        buttons = new Array<MenuButton>();
        addButton("Play");
        addButton("Settings");
        addButton("Quit");
    }

    public void addButton(String value) {
        int i = buttons.size;
        int x = 200;
        int y = Gdx.graphics.getHeight() - 300 - (150 * i);
        buttons.add(new MenuButton(value, x, y));
    }

    public void keyTyped(char character) {
    }

    public String touchUp(int x, int y, int pointer, int button) {
        for (MenuButton b : buttons) {
            if (b.contains(x, y)) {
                return b.getValue();
            }
        }
        return null;
    }

    public void dispose() {
        for (MenuButton button : buttons) {
            button.dispose();
        }
    }

    public void act() {
    }

    public void draw(Batch batch) {
        int x = Gdx.graphics.getWidth() / 2 - 150;
        int y = Gdx.graphics.getHeight() - 100;
        ((App) Gdx.app.getApplicationListener()).font.setColor(Color.WHITE);
        ((App) Gdx.app.getApplicationListener()).font.draw(batch, "Korean Zombie", x, y);

        for (MenuButton b : buttons) {
            b.draw(batch);
        }
    }
}
