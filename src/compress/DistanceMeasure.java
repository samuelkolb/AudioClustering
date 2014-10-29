package compress;

import audio.Song;

/**
 * Created by samuelkolb on 29/10/14.
 *
 * @author Samuel Kolb
 */
public interface DistanceMeasure {

	/**
	 * Calculates the distance between two songs
	 * @param song1	The first song
	 * @param song2	The second song
	 * @return	The distance according to the implemented algorithm
	 */
	public double distance(Song song1, Song song2);
}
