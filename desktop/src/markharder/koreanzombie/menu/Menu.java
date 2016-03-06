package markharder.koreanzombie.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

public class Menu {
    Array<MenuButton> buttons;

    public Menu() {
        buttons = new Array<MenuButton>();
        buttons.add(new MenuButton("Play"));
        buttons.add(new MenuButton("Quit"));
    }

    public void keyTyped(char character) {
    }

    public void dispose() {
        for (MenuButton button : buttons) {
            button.dispose();
        }
    }

    public void act() {
    }

    public void draw(Batch batch) {
        for (int i = 0; i < buttons.size; i++) {
            int x = 200;
            int y = Gdx.graphics.getHeight() - 250 - (150 * i);
            buttons.get(i).draw(batch, x, y);
        }
    }
}
