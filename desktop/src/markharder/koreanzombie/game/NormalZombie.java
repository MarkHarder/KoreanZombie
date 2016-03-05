package markharder.koreanzombie.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class NormalZombie extends Zombie {
	public static int SPEED = 2;

    public NormalZombie(int degree, double distance) {
        super(new Texture(Gdx.files.internal("images/normal_zombie.png")), degree, distance, SPEED);
    }
}
