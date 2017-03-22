package markharder.koreanzombie.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class SlowZombie extends Zombie {
    public SlowZombie(int degree, double distance) {
        super(new Texture(Gdx.files.internal("images/slow_zombie.png")), degree, distance);
    }

    protected int getSpeed() {
        return 1;
    }
}
