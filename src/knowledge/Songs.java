package knowledge;

import association.Association;
import association.ListAssociation;
import audio.Song;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by samuelkolb on 31/10/14.
 *
 * @author Samuel Kolb
 */
public class Songs {

	public static List<Song> getSongSamples() {
		List<Song> songs = new ArrayList<>();
		songs.addAll(getSongSamples("Classical", "Brahms", 2));
		songs.addAll(getSongSamples("Classical", "Mozart", 2));
		songs.addAll(getSongSamples("Classical", "Tschaikovsky", 4));
		songs.addAll(getSongSamples("Live", "Manson", 3));
		songs.addAll(getSongSamples("Live", "Slipknot", 3));
		songs.addAll(getSongSamples("Metal", "Cradle", 4));
		songs.addAll(getSongSamples("Metal", "Trivium", 3));
		songs.addAll(getSongSamples("Pop", "BSB", 3));
		songs.addAll(getSongSamples("Pop", "Gaga", 3));
		songs.addAll(getSongSamples("Pop", "Katy", 3));
		return songs;
	}

	private static List<Song> getSongSamples(String genre, String artist, int numberSamples) {
		List<Song> songs = new ArrayList<>();
		for(int i = 1; i <= numberSamples; i++)
			songs.add(getSongSample(genre, artist, (i-1)/2+1, (i-1)%2+1));
		return songs;
	}

	private static Song getSongSample(String genre, String artist, int number, int sample) {
		File dir = new File(Files.res(), genre);
		return new Song(new File(dir, genre + "_" + artist + "_song" + number + "_" + sample + ".wav"));
	}

	public static List<Song> getCustomSongs() {
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

}