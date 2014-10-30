package knowledge;

import java.io.File;

/**
 * Created by samuelkolb on 30/10/14.
 */
public class Files {

	public static File temp() {
		return getDir("temp");
	}

	public static File res() {
		return getDir("res");
	}

	private static File getDir(String name) {
		File dir = new File(name);
		dir.mkdirs();
		return dir;
	}
}
