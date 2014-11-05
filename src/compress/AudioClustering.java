package compress;

import audio.Song;
import clustering.*;
import knowledge.Files;
import knowledge.SongClass;
import knowledge.Songs;
import util.Vector;
import util.log.Log;

import java.io.File;
import java.util.Arrays;

/**
 * Created by samuelkolb on 29/10/14.
 *
 * @author Samuel Kolb
 */
public class AudioClustering {

	//region Variables
	//endregion

	//region Construction

	//endregion

	//region Public methods
	public static void main(String[] args) {
		Songs songs;
		//if(args.length >= 1 && args[0].equalsIgnoreCase("samples"))
		 	songs = Songs.getSamples();
		//else
			/*/songs = Songs.getSimpleSongs();/**/

		for(Combiner<Song> combiner : Arrays.asList(new MixingCombiner(), new ConcatenationCombiner()))
			for(Compressor<Song> compressor : Arrays.asList(new FlacCompressor(), new VorbisCompressor()))
				for(Linkage linkage : Arrays.asList(Linkage.AVERAGE)/*/Linkage.values()/**/)
					cluster(compressor, combiner, linkage, songs);
	}

	private static void cluster(Compressor<Song> compressor, Combiner<Song> combiner, Linkage linkage, Songs songs) {
		String type = getTypeString(compressor, combiner, linkage);
		Log.LOG.printTitle(type).saveState().off();

		DistanceMeasure<Song> distance = new NormalisedCompressionDistance(compressor, combiner);
		ClusteringAlgorithm<Song> algorithm = new HierarchicalClustering<>(distance, linkage);
		Node<Song> tree = algorithm.cluster(songs.getSongs());
		NodeVisualizer.visualize(tree, new File(Files.temp(), type + ".gv"));

		Log.LOG.revert();
		for(SongClass songClass : songs.getClasses()) {
			double fAverageScore = songClass.getFAverageScore(tree);
			double correctedFAverageScore = (fAverageScore - songClass.getFAverageBaseLine(tree))/(1 - songClass.getFAverageBaseLine(tree));
			double fMaxScore = songClass.getFMaxScore(tree);
			double correctedFMaxScore = (fMaxScore - songClass.getFAverageBaseLine(tree))/(1 - songClass.getFAverageBaseLine(tree));
			Log.LOG.printLine(songClass.getPairwiseDistance(tree));
			//Log.LOG.formatLine("%f (%f)", correctedFAverageScore, fMaxScore);
			//Log.LOG.formatLine("%s: %f / %f - %f, %f", songClass, fAverageScore, fMaxScore, songClass.getFAverageBaseLine(tree), correctedFAverageScore);
			//Log.LOG.formatLine("Pairwise distance %s: %f", songClass, songClass.getPairwiseDistance(tree));
			//Log.LOG.formatObjects(PrintFormat.TAB_SEPARATED, songClass.getPairwiseDistance(tree), correctedFAverageScore, correctedFScore);
			//Log.LOG.printLine(String.format("FAScore %s: %f (%f)", songClass.getName(), fAverageScore, correctedFAverageScore));
			//Log.LOG.printLine(String.format("FScore %s: %f (%f)", songClass.getName(), fScore, correctedFScore));
		}

		Log.LOG.printLine("");
		Vector<SongClass> broadClasses = songs.getBroadClasses();
		double combinedFScore = SongClass.getCombinedFScore(broadClasses, tree);
		double combinedFBaseScore = SongClass.getCombinedFBaseLine(broadClasses, tree);
		double combinedFScoreCorrected = (combinedFScore - combinedFBaseScore) / (1 - combinedFBaseScore);
		Log.LOG.formatLine("Combined score (%f): %f", combinedFScore, combinedFScoreCorrected);
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
	//public
	//endregion
}
