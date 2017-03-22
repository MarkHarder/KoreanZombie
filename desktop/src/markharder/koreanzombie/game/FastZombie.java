package markharder.koreanzombie.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class FastZombie extends Zombie {
    public FastZombie(int degree, double distance) {
        super(new Texture(Gdx.files.internal("images/fast_zombie.png")), degree, distance);
    }

    protected int getSpeed() {
        return 3;
    }
}
