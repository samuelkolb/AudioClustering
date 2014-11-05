package knowledge;

import association.Association;
import association.HashAssociation;
import audio.Song;
import clustering.*;
import util.TypePair;
import util.Vector;
import util.log.Log;
import util.log.PrintFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by samuelkolb on 04/11/14.
 *
 * @author Samuel Kolb
 */
public class SongClass {

	//region Variables
	private Association<String, Song> association = new HashAssociation<>(false, false);

	private final String name;

	public String getName() {
		return name;
	}

	//endregion

	//region Construction

	public SongClass(String name) {
		this.name = name;
	}

	//endregion

	//region Public methods

	/**
	 * Adds a song to this class with the given class value
	 * @param value	The class value (label)
	 * @param song	The song to add
	 */
	public void addSong(String value, Song song) {
		this.association.associate(value, song);
	}

	/**
	 * Returns the list of songs in this class for the given class value
	 * @param value	The class value (label)
	 * @return	The list of songs carrying the given class value
	 */
	public List<Song> getSongs(String value) {
		return new ArrayList<>(this.association.getValues(value));
	}

	/**
	 * Returns the class value associated with the given song
	 * @param song	The song who's class label should be returned
	 * @return	The class value (label)
	 */
	public String getValue(Song song) {
		return this.association.getKey(song);
	}

	/**
	 * Returns whether this class contains the given song
	 * @param song	The song
	 * @return	True iff this class has a class value for the given song
	 */
	public boolean contains(Song song) {
		return this.association.containsValue(song);
	}

	/**
	 * Returns the baseline for the f-average evaluation
	 * @param node	The root node to determine the baseline for
	 * @return	| return == max(f-average([node]), f-average(all-leafs))
	 */
	public double getFAverageBaseLine(Node<Song> node) {
		TreeNode<Song> tree = (TreeNode<Song>) node;
		double allClusters = getFAverageScore(NodeCutter.cut(tree, 0));
		double oneCluster = getFAverageScore(NodeCutter.cut(tree, tree.getLabel() + 1));
		return Math.max(allClusters, oneCluster);
	}

	/**
	 * Returns the baseline for the f-combine evaluation
	 * @param classes	The vector of classes to evaluate
	 * @param node		The root node to determine the f-combine score for
	 * @return	| return == max(f-combine([node]), f-combine(leafsOf(node)))
	 */
	public static double getCombinedFBaseLine(Vector<SongClass> classes, Node<Song> node) {
		double allClusters = getCombinedFScore(classes, NodeCutter.cut(node, 0));
		double oneCluster = getCombinedFScore(classes, NodeCutter.cut(node, node.getLabel() + 1));
		return Math.max(allClusters, oneCluster);
	}

	/**
	 * Returns the f-combine score for the given classes and node
	 * @param classes	The vector of classes to evaluate
	 * @param node		The root node to determine the f-combine score for
	 * @return	| return == max([cut in node-tree \ f-combine(classes, clusters(node, cut))])
	 */
	public static double getCombinedFScore(Vector<SongClass> classes, Node<Song> node) {
		double max = 0;
		for(int i = 0; i <= node.getLabel(); i++)
			max = Math.max(max, getCombinedFScore(classes, NodeCutter.cut(node, i)));
		return max;

	}

	/**
	 * Returns the f-average score for the given node
	 * @param node	The root node to determine the f-average score for
	 * @return	\ return == max([cut in node-tree | f-average(clusters(node, cut))])
	 */
	public double getFAverageScore(Node<Song> node) {
		TreeNode<Song> tree = (TreeNode<Song>) node;
		double max = 0;
		for(int i = 0; i <= tree.getLabel(); i++)
			max = Math.max(max, getFAverageScore(NodeCutter.cut(tree, i)));
		return max;
	}

	/**
	 * Returns the f-max score for the given node
	 * @param node	The root node to determine the f-max score for
	 * @return	\ return
	 */
	public double getFMaxScore(Node<Song> node) {
		double score = 0;
		for(String value : this.association.getKeys()) {
			double classScale = this.association.getValues(value).size() / (double) this.association.getValueCount();
			score += getFMaxScore(value, node) * classScale;
		}
		return score;
	}

	/**
	 * Returns the average pairwise distance for the given node
	 * @param node	The root node to determine the pairwise distance for
	 * @return	The average pairwise distance per class value
	 */
	public double getPairwiseDistance(Node<Song> node) {
		double score = 0;
		for(String value : this.association.getKeys()) {
			double distance = pairwiseDistance(value, node);
			score += distance/this.association.getKeyCount();
		}
		return score;
	}

	@Override
	public String toString() {
		return getName();
	}

	private static double getCombinedFScore(Vector<SongClass> classes, List<Node<Song>> cut) {
		double score = 0;
		for(Node<Song> node : cut) {
			double max = 0;
			for(SongClass songClass : classes)
				max = Math.max(max, songClass.getFAverageScoreCluster(node));
			score += max / cut.size();
		}
		return score;
	}

	private double getFAverageScore(List<Node<Song>> cut) {
		double score = 0;
		for(Node<Song> node : cut) {
			double maxScore = getFAverageScoreCluster(node);
			score += maxScore / cut.size();
		}
		return score;
	}

	private double getFAverageScoreCluster(Node<Song> node) {
		double maxScore = 0;
		for(String value : this.association.getKeys()) {
			double classScale = this.association.getValues(value).size() / (double) this.association.getValueCount();
			double score = getScore(value, node) * classScale;
			maxScore = Math.max(maxScore, score);
		}
		return maxScore;
	}

	private int distanceBaseLine(int numberOfNodes) {
		int distance = 0;
		for(int i = 1; i < numberOfNodes-1; i++)
			distance += (numberOfNodes-i)*i;
		return distance;
	}

	private double pairwiseDistance(String value, Node<Song> node) {
		TreeNode<Song> tree = (TreeNode<Song>) node;
		List<Song> values = new ArrayList<>(this.association.getValues(value));
		int numberOfPairs = (values.size() * (values.size() - 1)) / 2;
		double[] distances = new double[numberOfPairs];
		int index = 0;
		for(int i = 0; i < values.size()-1; i++)
			for(int j = i + 1; j < values.size(); j++)
				distances[index++] = tree.measureSeparation(values.get(i), values.get(j));
		double average = 0;
		for(double distance : distances)
			average += distance / (double) numberOfPairs;
		//average -= (distanceBaseLine(values.size()) / (double) numberOfPairs);
		double classScale = this.association.getValues(value).size() / (double) this.association.getValueCount();
		return average * classScale;
	}

	private double getFMaxScore(String value, Node<Song> node) {
		double score = getScore(value, node);
		if(node instanceof LeafNode)
			return score;
		TreeNode<Song> tree = (TreeNode<Song>) node;
		TypePair<Node<Song>> children = tree.getChildren();
		double childrenScore = Math.max(getFMaxScore(value, children.get(0)), getFMaxScore(value, children.get(1)));
		return Math.max(childrenScore, score);
	}

	private double getScore(String value, Node<Song> songs) {
		int classCount = this.association.getValues(value).size();
		List<Song> clusterElements = new ArrayList<>();
		for(Song song : NodeFlattener.flatten(songs))
			if(this.association.containsValue(song))
				clusterElements.add(song);
		int clusterCount = clusterElements.size();
		int correctCount = countCorrect(value, clusterElements);
		if(correctCount == 0)
			return 0;
		double recall = correctCount / (double) classCount;
		double precision = correctCount / (double) clusterCount;
		return recall + precision != 0 ? (2 * recall * precision / (recall + precision)) : 0;
	}

	private int countCorrect(String value, Collection<Song> clusterElements) {
		int result = 0;
		Collection<Song> values = this.association.getValues(value);
		for(Song song : clusterElements)
			if (values.contains(song))
				result++;
		return result;
	}

	//endregion
}
