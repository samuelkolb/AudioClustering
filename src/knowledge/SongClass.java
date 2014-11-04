package knowledge;

import association.Association;
import association.HashAssociation;
import audio.Song;
import clustering.LeafNode;
import clustering.Node;
import clustering.NodeFlattener;
import clustering.TreeNode;
import util.TypePair;
import util.Vector;
import util.log.Log;

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

   /* public SongClass(Vector<Song> songs) {
        for(int i = 0; i < songs.size() - 1; i++) {
            String[] splitName = songs.get(i).getSongName().split("/([^_]+)/g");
            for(int j = 0; j < splitName.length - 1; j++){
                addSong(splitName[j], songs.get(i));
            }
        }
    }*/
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

	public double getFScore(Node<Song> node) {
		double score = 0;
		for(String value : this.association.getKeys()) {
			double fScore = getFScore(value, node);
			Log.LOG.printLine(String.format("Score: %f for value: %s", fScore, value));
			score += fScore /this.association.getKeys().size();
		}
		return score;
	}

	public double getFScore(String value, Node<Song> node) {
		double score = getScore(value, node);
		if(node instanceof LeafNode)
			return score;
		TreeNode<Song> tree = (TreeNode<Song>) node;
		TypePair<Node<Song>> children = tree.getChildren();
		return Math.max(Math.max(getFScore(value, children.getFirst()), getFScore(value, children.getSecond())), score);
	}

	public double getScore(String value, Node<Song> songs) {
		int classCount = this.association.getValues(value).size();
		List<Song> clusterElements = NodeFlattener.flatten(songs);
		int clusterCount = clusterElements.size();
		int correctCount = countCorrect(value, clusterElements);
		double recall = correctCount / (double) classCount;
		double precision = correctCount / (double) clusterCount;
		return 2 * recall * precision / (recall + precision);
	}

	private int countCorrect(String value, List<Song> clusterElements) {
		int result = 0;
		Collection<Song> values = this.association.getValues(value);
		for(Song song : clusterElements)
			if (values.contains(song))
				result++;
		return result;
	}

	//endregion
}
