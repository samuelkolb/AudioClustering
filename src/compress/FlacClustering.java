package compress;

import audio.Song;
import clustering.*;
import knowledge.Files;
import knowledge.SongClass;
import knowledge.Songs;
import util.log.Log;

import java.io.File;
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
		Songs songs;
		if(args.length >= 1 && args[0].equalsIgnoreCase("samples"))
		 	songs = Songs.getSamples();
		else
			songs = Songs.getSimpleSongs();

		for(Compressor<Song> compressor : Arrays.asList(new FlacCompressor(), new VorbisCompressor()))
			for(Combiner<Song> combiner : Arrays.asList(new MixingCombiner(), new ConcatenationCombiner()))
				for(Linkage linkage : Arrays.asList(Linkage.COMPLETE)/*/Linkage.values()/**/)
					cluster(compressor, combiner, linkage, songs);
	}

	private static void cluster(Compressor<Song> compressor, Combiner<Song> combiner, Linkage linkage, Songs songs) {
		String type = getTypeString(compressor, combiner, linkage);
		Log.LOG.printTitle(type).saveState().off();

		DistanceMeasure<Song> distance = new NormalisedCompressionDistance<>(compressor, combiner);
		ClusteringAlgorithm<Song> algorithm = new HierarchicalClustering<>(distance, linkage);
		Node<Song> tree = algorithm.cluster(songs.getSongs());
		NodeVisualizer.visualize(tree, new File(Files.temp(), type + ".gv"));

		Log.LOG.revert();
		for(SongClass songClass : songs.getClasses()) {
			double fAverageScore = songClass.getFAverageScore(tree);
			double correctedFAverageScore = (fAverageScore - songClass.getFBaseLine(tree))/(1 - songClass.getFBaseLine(tree));
			double fScore = songClass.getFMaxScore(tree);
			double correctedFScore = (fScore - songClass.getFBaseLine(tree))/(1 - songClass.getFBaseLine(tree));
			Log.LOG.formatLine("%f\t%f", correctedFAverageScore, correctedFScore);
			//Log.LOG.printLine(String.format("FAScore %s: %f (%f)", songClass.getName(), fAverageScore, correctedFAverageScore));
			//Log.LOG.printLine(String.format("FScore %s: %f (%f)", songClass.getName(), fScore, correctedFScore));
		}
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
