package util.runtime;

import util.StringUtil;
import util.log.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by samuelkolb on 23/10/14.
 *
 * @author Samuel Kolb
 */
public class Terminal {

	//region Variables

	//endregion

	//region Construction

	//endregion

	//region Public methods

	/**
	 * Executes a (non-interactive) command
	 * @param command	The command to be run
	 * @param wait		Indicates whether or not to wait for the command to execute
	 */
	public void execute(String command, boolean wait) {
		Runtime runtime = Runtime.getRuntime();
		try {
			Process process = runtime.exec(command);
			if(wait)
				process.waitFor();
		} catch(Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	//endregion

	/**
	 * Runs a (non-interactive) command
	 * @param command	The command to be run
	 * @return	The output received while executing the command
	 */
	public String runCommand(String command) {
		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		try {
			process = runtime.exec(command);
			process.waitFor();
			return StringUtil.readStream(process.getInputStream());
		} catch(Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	public double getFileSize(File file) {
		String command = "ls -l " + file.getAbsolutePath();
		OperatingSystem os = OperatingSystem.getOperatingSystem();
		if(os == OperatingSystem.MAC || os == OperatingSystem.LINUX) {
			String string = runCommand(command);
			String[] parts = string.split("\\s+");
			return Double.parseDouble(parts[4]);
		} else {
			return file.length();
		}
	}
	//endregion
}
