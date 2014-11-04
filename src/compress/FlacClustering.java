package compress;

import audio.Song;
import clustering.*;
import knowledge.Files;
import knowledge.SongClass;
import knowledge.Songs;
import util.TypePair;
import util.log.Log;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import util.Vector;

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
		Vector<Song> songSamples = Songs.getSamples().getSongs();

		for(Compressor<Song> compressor : Arrays.asList(new FlacCompressor(), new VorbisCompressor()))
			for(Combiner<Song> combiner : Arrays.asList(new MixingCombiner(), new ConcatenationCombiner()))
				for(Linkage linkage : Arrays.asList(Linkage.COMPLETE)/*/Linkage.values()/**/)
					cluster(compressor, combiner, linkage, songSamples);
	}

	private static void cluster(Compressor<Song> compressor, Combiner<Song> combiner,
								Linkage linkage, Vector<Song> songs) {
		String type = getTypeString(compressor, combiner, linkage);
		Log.LOG.printTitle(type).saveState().off();

		DistanceMeasure<Song> distance = new NormalisedCompressionDistance<>(compressor, combiner);
		ClusteringAlgorithm<Song> algorithm = new HierarchicalClustering<>(distance, linkage);
		Node<Song> tree = algorithm.cluster(songs);
		NodeVisualizer.visualize(tree, new File(Files.temp(), type + ".gv"));

		Log.LOG.revert();
		for(SongClass songClass : Songs.getSamples().getClasses())
			Log.LOG.printLine(String.format("Score %s: %f", songClass.getName(), songClass.getFAverageScore(tree)));
	}

	private static String getTypeString(Compressor<Song> compressor, Combiner<Song> combiner, Linkage linkage) {
		String type = "";
		if(compressor instanceof FlacCompressor)
			type += "Flac";
		else if(compressor instanceof VorbisCompressor)
			type += "Vorbis";
		else
			type += "Unknown";
		if(combiner instanceof MixingCombiner)
			type += "Mix";
		else if(combiner instanceof ConcatenationCombiner)
			type += "Concat";
		else
			type += "Unknown";
		type += linkage.name().substring(0,1).toUpperCase() + linkage.name().substring(1).toLowerCase();
		type += "Clustering";
		return type;
	}

	public double[] pairwiseDistance(List<Song> group, TreeNode<Song> songs) {
		int numberOfPairs = (group.size() * (group.size() - 1)) / 2;
		double[] distances = new double[numberOfPairs];
		int index = 0;
		for(int i = 0; i < group.size()-1; i++)
			for(int j = i + 1; j < group.size(); j++)
				distances[index++] = songs.measureSeparation(group.get(i), group.get(j));
		return distances;
	}

	//public
	//endregion
}
