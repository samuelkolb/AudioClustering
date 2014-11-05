package compress;

import audio.Song;

/**
 * Created by samuelkolb on 29/10/14.
 *
 * @author Samuel Kolb
 */
public class NormalisedCompressionDistance implements DistanceMeasure<Song> {

	//region Variables

	private final Compressor<Song> compressor;

	public Compressor<Song> getCompressor() {
		return compressor;
	}

	private final Combiner<Song> combiner;

	public Combiner<Song> getCombiner() {
		return combiner;
	}

	//endregion

	//region Construction

	/**
	 * Creates a new distance calculator
	 * @param compressor	The compressor to use
	 * @param combiner		The combiner to use
	 */
	public NormalisedCompressionDistance(Compressor<Song> compressor, Combiner<Song> combiner) {
		this.compressor = compressor;
		this.combiner = combiner;
	}

	//endregion

	//region Public methods

	@Override
	public double distance(Song element1, Song element2) {
		double size1 = getCompressor().compress(element1);
		double size2 = getCompressor().compress(element2);
		double combinedSize = getCompressor().compress(getCombiner().combine(element1, element2));
		return (combinedSize - Math.min(size1, size2))/Math.max(size1, size2);
	}

	//endregion
}
