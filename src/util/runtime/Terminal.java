package util.runtime;

import util.StringUtil;

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
	//endregion
}
