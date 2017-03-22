package markharder.koreanzombie.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class NormalZombie extends Zombie {
    public NormalZombie(int degree, double distance) {
        super(new Texture(Gdx.files.internal("images/normal_zombie.png")), degree, distance);
    }

    protected int getSpeed() {
        return 2;
    }
}
