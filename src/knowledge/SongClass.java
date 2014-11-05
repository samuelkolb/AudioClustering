package knowledge;

import association.Association;
import association.HashAssociation;
import audio.Song;
import clustering.*;
import util.TypePair;

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
	public void addSong(String value, Song song) {
		this.association.associate(value, song);
	}

	public List<Song> getSongs(String value) {
		return new ArrayList<>(this.association.getValues(value));
	}

	public String getValue(Song song) {
		return this.association.getKey(song);
	}

	public boolean contains(Song song) {
		return this.association.containsValue(song);
	}

	public double getFBaseLine(Node<Song> node) {
		TreeNode<Song> tree = (TreeNode<Song>) node;
		double allClusters = getFAverageScore(NodeCutter.cut(tree, 0));
		double oneCluster = getFAverageScore(NodeCutter.cut(tree, tree.getLabel() + 1));
		return Math.max(allClusters, oneCluster);
	}

	public double getFAverageScore(Node<Song> node) {
		TreeNode<Song> tree = (TreeNode<Song>) node;
		double max = 0;
		for(int i = 0; i <= tree.getLabel(); i++)
			max = Math.max(max, getFAverageScore(NodeCutter.cut(tree, i)));
		return max;
	}

	private double getFAverageScore(List<Node<Song>> cut) {
		double score = 0;
		for(String value : this.association.getKeys()) {
			double maxScore = 0;
			for(Node<Song> node : cut)
				maxScore = Math.max(maxScore, getScore(value, node));
			score += maxScore / this.association.getKeys().size();
		}
		return score;
	}

	public double getFMaxScore(Node<Song> node) {
		double score = 0;
		for(String value : this.association.getKeys()) {
			double fScore = getFMaxScore(value, node);
			//Log.LOG.printLine(String.format("Score: %f for value: %s", fScore, value));
			score += fScore / this.association.getKeys().size();
		}
		return score;
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
		return recall + precision != 0 ? 2 * recall * precision / (recall + precision) : 0;
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
