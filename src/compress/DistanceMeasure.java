package compress;

import audio.Song;

/**
 * Created by samuelkolb on 29/10/14.
 *
 * @author Samuel Kolb
 */
public interface DistanceMeasure<T> {

	/**
	 * Calculates the distance between two elements
	 * @param element1	The first element
	 * @param element2	The second element
	 * @return	The distance according to the implemented algorithm
	 */
	public double distance(T element1, T element2);
}
