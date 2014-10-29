package util.log;

/**
 * String transformers can transform logging messages
 *
 * @author Samuel Kolb
 */
public interface StringTransformer {

	/**
	 * Transforms the given string to a new string
	 * @param stackTraceElement	The stack trace element of the caller
	 * @param string			The logging message
	 * @return	A new string
	 */
	public String transform(StackTraceElement stackTraceElement, String string);
}
