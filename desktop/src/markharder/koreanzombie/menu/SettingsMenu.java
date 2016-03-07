package markharder.koreanzombie.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

public class SettingsMenu {
    Array<MenuButton> radioButtons;
    MenuButton backButton;
    Texture selectionTexture;
    Difficulty difficulty;

    public enum Difficulty {
        EASY,
        NORMAL,
        HARD
    }

    public SettingsMenu() {
        selectionTexture = new Texture(Gdx.files.internal("images/selected_button.png"));
        radioButtons = new Array<MenuButton>();
        addRadioButton("Easy");
        addRadioButton("Normal");
        addRadioButton("Hard");
        backButton = new MenuButton("Back", 200, 100);
        difficulty = Difficulty.NORMAL;
    }

    public void addRadioButton(String value) {
        int i = radioButtons.size;
        int x = 200 + (275 * i);
        int y = 250;
        radioButtons.add(new MenuButton(value, x, y));
    }

    public void keyTyped(char character) {
    }

    public String touchUp(int x, int y, int pointer, int button) {
        for (MenuButton b : radioButtons) {
            if (b.contains(x, y)) {
                if (b.getValue() == "Easy") {
                    difficulty = Difficulty.EASY;
                } else if (b.getValue() == "Normal") {
                    difficulty = Difficulty.NORMAL;
                } else if (b.getValue() == "Hard") {
                    difficulty = Difficulty.HARD;
                }
                return b.getValue();
            }
        }
        if (backButton.contains(x, y)) {
            return backButton.getValue();
        }
        return null;
    }

    public void dispose() {
        selectionTexture.dispose();
        for (MenuButton button : radioButtons) {
            button.dispose();
        }
        backButton.dispose();
    }

    public void act() {
    }

    public void draw(Batch batch) {
        for (MenuButton b : radioButtons) {
            if ((b.getValue() == "Easy" && difficulty == Difficulty.EASY) ||
                    (b.getValue() == "Normal" && difficulty == Difficulty.NORMAL) ||
                    (b.getValue() == "Hard" && difficulty == Difficulty.HARD)) {
                batch.draw(selectionTexture, b.getX() - 10, b.getY() - 10);
            }
            b.draw(batch);
        }
        backButton.draw(batch);
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
}
