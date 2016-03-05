package markharder.koreanzombie.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Zombie {
    private Texture texture;
	private int degree;
	private double distance;
	private int speed;
	
	public Zombie(Texture texture, int degree, double distance, int speed) {
        this.texture = texture;
		this.degree = degree;
		this.distance = distance;
        this.speed = speed;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public void act(float deltaTime) {
		distance -= speed;
	}
	
	public void draw(Batch batch) {
        int r = texture.getHeight() / 2;
        double yOffset = 50;
        double xOffset = 320;
        double x = xOffset + Math.cos(degree) * distance - r + 800 / 2;
        double y = yOffset + Math.sin(degree) * distance - r + 800 / 2;
        batch.draw(texture, (float) x, (float) y);
	}
}
