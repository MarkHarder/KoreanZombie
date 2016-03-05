package markharder.koreanzombie.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class FastZombie extends Zombie {
	public static int SPEED = 3;

    public FastZombie(int degree, double distance) {
        super(new Texture(Gdx.files.internal("images/fast_zombie.png")), degree, distance, SPEED);
    }
}
