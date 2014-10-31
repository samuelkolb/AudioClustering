package util.runtime;

/**
 * Created by samuelkolb on 31/10/14.
 *
 * @author Samuel Kolb
 */
public enum OperatingSystem {

	MAC, LINUX, WINDOWS, OTHER;

	public static OperatingSystem getOperatingSystem() {
		String os = System.getProperty("os.name").toLowerCase();
		if(os.startsWith("mac os x"))
			return MAC;
		else if(os.startsWith("windows"))
			return WINDOWS;
		else if(os.contains("nix") || os.contains("nux"))
			return LINUX;
		return OTHER;
	}
}
