package util;

/**
 * Contains methods to print commonly used objects to different output formats
 * @author Samuel Kolb
 */
public class StringUtil {

	private StringUtil() {

	}

	/*
	 * Prints the given tuple rounded to the required precision
	 * @param tuple		The tuple to be printed
	 * @param precision	The desired precision
	 * @param format	The PrintFormat to be used to print the tuple elements
	 * @return	A string representation of the given tuple
	 *
	public static String printPrecision(Tuple tuple, int precision, PrintFormat format) {
		String first = tuple.hasFirst() ? printPrecision(tuple.getFirst(), precision) : "?";
		String second = tuple.hasSecond() ? printPrecision(tuple.getSecond(), precision) : "?";
		return format.print(first, second);
	}*/

	/**
	 * Joins an array of objects using the given glue string
	 * @param glue		The string to be injected between every two elements
	 * @param objects	The objects to be joined
	 * @return	A string that consists of the toString() version of every object interlaced with the given glue string
	 * 			| ex. objects[0].toString()+glue+objects[1].toString()+glue+objects[2].toString()+...
	 * 			| "" iff objects.length == 0
	 */
	public static String join(String glue, Object... objects) {
		if(objects.length == 0)
			return "";
		else if(objects.length == 1)
			return toString(objects[0]);
		StringBuilder builder = new StringBuilder(toString(objects[0]));
		for(int i = 1; i < objects.length; i++)
			builder.append(glue).append(toString(objects[i]));
		return builder.toString();
	}

	/*
	 * Joins an array of objects
	 * @param objects	The objects to be joined
	 * @return	A string that consists of the toString() version of every object
	 * 			| ex. objects[0].toString()+objects[1].toString()+objects[2].toString()+...
	 * 			| "" iff objects.length == 0
	 *
	public static String join(Object... objects) {
		if(objects.length == 0)
			return "";
		else if(objects.length == 1)
			return toString(objects[0]);
		StringBuilder builder = new StringBuilder(toString(objects[0]));
		for(int i = 1; i < objects.length; i++)
			builder.append(toString(objects[i]));
		return builder.toString();
	}*/

	private static String toString(Object object) {
		return object == null ? "null" : object.toString();
	}

	/**
	 * Returns a generated object name
	 * @param object	The object for which a name is to be generated
	 * @return	A name based on the class name and a hash code based on the object
	 * 			| if object != null
	 * 			|	return == object.getClass().getSimpleName()+"@"+System.identityHashCode()
	 * 			| else
	 * 			|	return == "null"
	 */
	public static String getHashName(Object object) {
		if(object == null)
			return "null";
		return join("", object.getClass().getSimpleName(), "@", System.identityHashCode(object));
	}

	/**
	 * Returns a string that consists of the given character repeated for a given number of times
	 * @param character	The character to repeat
	 * @param number	The number of times the character should be repeated
	 * @return	A string of length number containing only the given character
	 */
	public static String getRepeated(char character, int number) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < number; i++)
			builder.append(character);
		return builder.toString();
	}

	public static String readStream() {
		return null; // TODO
	}
}
