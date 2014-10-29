package compress;

import audio.Song;

/**
 * Created by samuelkolb on 29/10/14.
 *
 * @author Samuel Kolb
 */
public class NormalisedCompressionDistance<T> implements DistanceMeasure<T> {

	//region Variables

	private final Compressor<T> compressor;

	public Compressor<T> getCompressor() {
		return compressor;
	}

	private final Combiner<T> combiner;

	public Combiner<T> getCombiner() {
		return combiner;
	}

	//endregion

	//region Construction

	/**
	 * Creates a new distance calculator
	 * @param compressor	The compressor to use
	 * @param combiner		The combiner to use
	 */
	public NormalisedCompressionDistance(Compressor<T> compressor, Combiner<T> combiner) {
		this.compressor = compressor;
		this.combiner = combiner;
	}

	//endregion

	//region Public methods

	@Override
	public double distance(T element1, T element2) {
		double size1 = getCompressor().compress(element1);
		double size2 = getCompressor().compress(element2);
		double combinedSize = getCompressor().compress(getCombiner().combine(element1, element2));
		return (combinedSize - Math.min(size1, size2))/Math.max(size1, size2);
	}

	//endregion
}
