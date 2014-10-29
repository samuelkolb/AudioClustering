package compress;

/**
 * Created by samuelkolb on 29/10/14.
 *
 * @author Samuel Kolb
 */
public interface Combiner<T> {

	/**
	 * Combine two elements into one
	 * @param element1	The first element
	 * @param element2	The second element
	 * @return	A combined element
	 */
	public T combine(T element1, T element2);
}
