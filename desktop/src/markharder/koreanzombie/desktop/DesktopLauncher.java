package markharder.koreanzombie.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import markharder.koreanzombie.App;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Korean Zombie";
        config.width = 1440;
        config.height = 900;
        config.fullscreen = true;
		new LwjglApplication(new App(), config);
	}
}
