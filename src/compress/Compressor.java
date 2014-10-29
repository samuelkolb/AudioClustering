package compress;

/**
 * Created by samuelkolb on 29/10/14.
 *
 * @author Samuel Kolb
 */
public interface Compressor<T> {

	/**
	 * Compresses the given element and returns the compressed size
	 * @param element	The element to compress
	 * @return	The size of the compression
	 */
	public double compress(T element);
}
