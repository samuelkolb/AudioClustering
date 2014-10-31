package compress;

import audio.Song;
import clustering.*;
import knowledge.Files;
import knowledge.Songs;
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
		Combiner<Song> mixing = new MixingCombiner();
		Combiner<Song> concatenation = new ConcatenationCombiner();
		Compressor<Song> flac = new FlacCompressor();
		Compressor<Song> vorbis = new VorbisCompressor();
		List<Song> songSamples = Songs.getSongSamples();

		Linkage linkage = Linkage.COMPLETE;

		cluster("FlacMixSingle", flac, mixing, linkage, songSamples);
		cluster("VorbisMixSingle", vorbis, mixing, linkage, songSamples);
		cluster("FlacConcatSingle", flac, concatenation, linkage, songSamples);
		cluster("VorbisConcatSingle", vorbis, concatenation, linkage, songSamples);
	}

	private static void cluster(String type, Compressor<Song> compressor, Combiner<Song> combiner,
								Linkage linkage, List<Song> songs) {
		Log.LOG.printTitle(type + " clustering");
		Log.LOG.off();
		DistanceMeasure<Song> distance = new NormalisedCompressionDistance<>(compressor, combiner);
		ClusteringAlgorithm<Song> algorithm = new HierarchicalClustering<>(distance, linkage);
		Node<Song> tree = algorithm.cluster(songs);
		Log.LOG.on();
		Log.LOG.printLine("Visualize");
		NodeVisualizer.visualize(tree, new File(Files.temp(), type + "Clustering.gv"));
	}

	//public
	//endregion
}
