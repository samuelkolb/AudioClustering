package compress;

import audio.Song;
import clustering.ClusteringAlgorithm;
import clustering.HierarchicalClustering;
import clustering.Node;
import clustering.NodePrinter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by samuelkolb on 29/10/14.
 *
 * @author Samuel Kolb
 */
public class FlacClustering {

	//region Variables
	//endregion

	//region Construction

	//endregion

	//region Public methods
	public static void main(String[] args) {
		Compressor<Song> compressor = new FlacCompressor();
		Combiner<Song> combiner = null;
		DistanceMeasure<Song> distance = new NormalisedCompressionDistance<>(compressor, combiner);
		ClusteringAlgorithm<Song> algorithm = new HierarchicalClustering<>(distance);
		Node<Song> tree = algorithm.cluster(getSongs());
		NodePrinter.printTree(tree);
	}

	private static List<Song> getSongs() {
		List<Song> songs = new ArrayList<>();
		for(String string : new String[]{"A", "B", "C", "C2"})
			songs.addAll(getSongVariants("Song" +  string));
		return songs;
	}

	private static List<Song> getSongVariants(String song) {
		return Arrays.asList(
				new Song(song + "_SGP_1.wav"),
				new Song(song + "_CEP_1.wav"),
				new Song(song + "_SGP_2.wav")
		);
	}

	//public
	//endregion
}
