package compress;

import audio.Song;
import clustering.*;
import knowledge.Files;
import knowledge.SongClass;
import knowledge.Songs;
import util.Vector;
import util.log.Log;
import util.log.PrintFormat;

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

	/**
	 * Run the main program
	 * Uncomment code to select samples or allow command line selection
	 * @param args	Commandline arguments
	 */
	public static void main(String[] args) {

		Songs songs;
		if(args.length >= 1 && args[0].equalsIgnoreCase("samples"))
		 	songs = Songs.getSamples();
		else
			songs = Songs.getSimpleSongs();

		for(Combiner<Song> combiner : Arrays.asList(new MixingCombiner(), new ConcatenationCombiner()))
			for(Compressor<Song> compressor : Arrays.asList(new FlacCompressor(), new VorbisCompressor()))
				for(Linkage linkage : Arrays.asList(Linkage.AVERAGE)/*/Linkage.values()/**/) // Possibly compare linkage
					cluster(compressor, combiner, linkage, songs);
	}

	private static void cluster(Compressor<Song> compressor, Combiner<Song> combiner, Linkage linkage, Songs songs) {
		String type = getTypeString(compressor, combiner, linkage);

		Log.LOG.printTitle(type).saveState();
		Log.LOG.off(); // Remove to print progress during compression

		DistanceMeasure<Song> distance = new NormalisedCompressionDistance(compressor, combiner);
		HierarchicalClustering<Song> algorithm = new HierarchicalClustering<>(distance, linkage);
		Node<Song> tree = algorithm.cluster(songs.getSongs());

		// Creates a GraphViz visualisation in the temp folder
		NodeVisualizer.visualize(tree, new File(Files.temp(), type + ".gv"));

		Log.LOG.revert();
		for(SongClass songClass : songs.getClasses()) {

			double fAverageScore = songClass.getFAverageScore(tree);
			double fAverageBaseLine = songClass.getFAverageBaseLine(tree);
			double cFAverageScore = (fAverageScore - fAverageBaseLine)/(1 - fAverageBaseLine);

			double fMaxScore = songClass.getFMaxScore(tree);
			double separation = songClass.getPairwiseSeparation(tree);
			double distances = songClass.getPairwiseDistances(tree, algorithm.getDistances());

			// "Raw" printing
			//Log.LOG.formatObjects(PrintFormat.TAB_SEPARATED, fMaxScore, cFAverageScore, separation, distances);

			Log.LOG.formatLine("Class %s:", songClass);
			Log.LOG.formatLine("\tFMaxScore:\t%f", fMaxScore);
			Log.LOG.formatLine("\tCFAvgScore:\t%f", cFAverageScore);
			Log.LOG.formatLine("\tSeparation:\t%f", separation);
			Log.LOG.formatLine("\tDistance:\t%f", distances);/**/
		}

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
