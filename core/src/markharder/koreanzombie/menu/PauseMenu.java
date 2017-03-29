package markharder.koreanzombie.menu;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

public class PauseMenu {
    private Array<MenuButton> buttons;

    public PauseMenu() {
        buttons = new Array<MenuButton>();
        addButton("Resume");
        addButton("Restart");
        addButton("Back to menu");
    }

    public void addButton(String value) {
        int i = buttons.size;
        int x = 600;
        int y = 600 - (150 * i);
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

    public void mouseMoved(int x, int y) {
        for (MenuButton b : buttons) {
            if (b.contains(x, y)) {
                b.select();
            } else {
                b.unselect();
            }
        }
    }

    public void dispose() {
        for (MenuButton button : buttons) {
            button.dispose();
        }
    }

    public void act() {
    }

    public void draw(Batch batch) {
        for (MenuButton b : buttons) {
            b.draw(batch);
        }
    }
}
