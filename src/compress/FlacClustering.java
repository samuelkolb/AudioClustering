package compress;

import audio.Song;
import clustering.*;
import knowledge.Files;
import util.log.Log;

import java.io.File;
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
		Combiner<Song> combiner = new MixingCombiner();
		cluster("Flac", new FlacCompressor(), combiner);
		cluster("Vorbis", new VorbisCompressor(), combiner);
	}

	private static void cluster(String type, Compressor<Song> compressor, Combiner<Song> combiner) {
		Log.LOG.printTitle(type + " clustering");
		DistanceMeasure<Song> distance = new NormalisedCompressionDistance<>(compressor, combiner);
		ClusteringAlgorithm<Song> algorithm = new HierarchicalClustering<>(distance);
		Node<Song> tree = algorithm.cluster(getSongs());
		Log.LOG.printLine("Visualize");
		NodeVisualizer.visualize(tree, new File(Files.temp(), type + "Clustering.gv"));
	}

	private static List<Song> getSongs() {
		List<Song> songs = new ArrayList<>();
		for(String string : new String[]{"A", "B", "C", "C2"})
			songs.addAll(getSongVariants("Song" +  string));
		return songs;
	}

	private static List<Song> getSongVariants(String song) {
		return Arrays.asList(
				new Song(new File(Files.res(), song + "_SGP_1.wav")),
				new Song(new File(Files.res(), song + "_CEP_1.wav")),
				new Song(new File(Files.res(), song + "_SGP_2.wav"))
		);
	}

	//public
	//endregion
}
