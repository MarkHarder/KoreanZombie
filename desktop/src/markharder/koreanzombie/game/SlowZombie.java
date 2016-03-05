package markharder.koreanzombie.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class SlowZombie extends Zombie {
	public static int SPEED = 1;

    public SlowZombie(int degree, double distance) {
        super(new Texture(Gdx.files.internal("images/slow_zombie.png")), degree, distance, SPEED);
    }
}
