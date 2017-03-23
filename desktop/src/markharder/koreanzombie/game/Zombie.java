package markharder.koreanzombie.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public abstract class Zombie {
    private Texture texture;
	private int degree;
	private double distance;
	
	public Zombie(Texture texture, int degree, double distance) {
        this.texture = texture;
		this.degree = degree;
		this.distance = distance;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public void act(float deltaTime) {
		distance -= getSpeed();
	}
	
	public void draw(Batch batch, float xOffset, float yOffset, float centerX,
            float centerY) {
        int r = texture.getHeight() / 2;
        double x = xOffset + Math.cos(degree) * distance - r + centerX;
        double y = yOffset + Math.sin(degree) * distance - r + centerY;
        batch.draw(texture, (float) x, (float) y);
	}

    public void dispose() {
        texture.dispose();
    }

    protected abstract int getSpeed();
}
