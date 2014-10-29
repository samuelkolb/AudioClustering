package util.log;

/**
 * Created by samuelkolb on 19/10/14.
 *
 * @author Samuel Kolb
 */
public class LogMessage {

	//region Variables
	public final String MESSAGE;

	public final StackTraceElement STACK_TRACE_ELEMENT;
	//endregion

	//region Construction

	/**
	 * Creates a new log message
	 * @param message			The message
	 * @param stackTraceElement	The callers stack trace element
	 */
	public LogMessage(String message, StackTraceElement stackTraceElement) {
		this.MESSAGE = message;
		this.STACK_TRACE_ELEMENT = stackTraceElement;
	}

	//endregion

	//region Public methods

	//endregion
}
