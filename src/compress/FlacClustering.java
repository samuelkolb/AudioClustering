package compress;

import audio.Song;
import clustering.*;
import knowledge.Files;
import knowledge.Songs;
import util.TypePair;
import util.log.Log;

import java.io.File;
import java.util.List;
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
		Combiner<Song> mixing = new MixingCombiner();
		Combiner<Song> concatenation = new ConcatenationCombiner();
		Compressor<Song> flac = new FlacCompressor();
		Compressor<Song> vorbis = new VorbisCompressor();
		Vector<Song> songSamples = Songs.getSongSamples();

		Linkage linkage = Linkage.COMPLETE;

		Node<Song> node = cluster("FlacMixSingle", flac, mixing, linkage, songSamples);

	}

	private static Node<Song> cluster(String type, Compressor<Song> compressor, Combiner<Song> combiner,
								Linkage linkage, Vector<Song> songs) {
		Log.LOG.printTitle(type + " clustering");
		Log.LOG.off();
		DistanceMeasure<Song> distance = new NormalisedCompressionDistance<>(compressor, combiner);
		ClusteringAlgorithm<Song> algorithm = new HierarchicalClustering<>(distance, linkage);
		Node<Song> tree = algorithm.cluster(songs);
		Log.LOG.on();
		Log.LOG.printLine("Visualize");
		NodeVisualizer.visualize(tree, new File(Files.temp(), type + "Clustering.gv"));
		return tree;
	}

	public double getFScore(List<Song> group, Node<Song> node) {
		double score = getScore(group, node);
		if(node instanceof LeafNode)
			return score;
		TreeNode<Song> tree = (TreeNode<Song>) node;
		TypePair<Node<Song>> children = tree.getChildren();
		return Math.max(Math.max(getFScore(group, children.getFirst()), getFScore(group, children.getSecond())), score);
	}

	public double getScore(List<Song> group, Node<Song> songs) {
		int classCount = group.size();
		List<Song> clusterElements = NodeFlattener.flatten(songs);
		int clusterCount = clusterElements.size();
		int correctCount = countCorrect(group, clusterElements);
		double recall = correctCount / (double) classCount;
		double precision = correctCount / (double) clusterCount;
		return 2 * recall * precision / (recall + precision);
	}

	private int countCorrect(List<Song> group, List<Song> clusterElements) {
		int result = 0;
		for(Song song : group)
			if(clusterElements.contains(song))
				result++;
		return result;
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
