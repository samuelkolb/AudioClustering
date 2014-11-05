package knowledge;

import audio.Song;
import util.Vector;
import util.WriteOnceVector;
import util.log.Log;

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

	// --- Static Variables

    private static Songs samples = createSamples();

    public static Songs getSamples() {
        return samples;
    }

	private static Songs simpleSongs = createSimpleSongs();

	public static Songs getSimpleSongs() {
		return simpleSongs;
	}

	// --- Instance Variables

    private final Vector<Song> songs;

    public Vector<Song> getSongs() {
        return songs;
    }

    private final Vector<SongClass> classes;

    public Vector<SongClass> getClasses() {
        return classes;
    }

    public Songs(Vector<SongClass> classes, int numberSongs) {
        this.classes = classes;
        this.songs = new WriteOnceVector<>(new Song[numberSongs]);
    }

    public void addSong(Song song, String... values) {
        this.songs.add(song);
        for(int i = 0; i < classes.length; i++)
            if(values[i] != null)
                classes.get(i).addSong(values[i], song);
    }

	@Override
	public String toString() {
		String classes = this.classes.toString();
		String songs = this.songs.toString();
		return String.format("Classes: %s, Songs: %s", classes, songs);
	}

	// --- Static creation

    private static Songs createSamples() {
        WriteOnceVector<SongClass> classes = new WriteOnceVector<>(new SongClass[4]);

        classes.add(new SongClass("Genre"));
	    classes.add(new SongClass("Artist"));
		classes.add(new SongClass("Live"));
        classes.add(new SongClass("SongVersion"));
        /*classes.add(new SongClass("Song"));//*/

        Songs songs = new Songs(classes, 46);

        addSamples(songs, "Classical", "Brahms", 1, 2);
        addSamples(songs, "Classical", "Mozart", 1, 2);

        for (int i = 1; i <= 2; i++) {
            addSamples(songs, "Classical", "Tschaikovsky", i, 2);
            addSamples(songs, "Live", "Slipknot", i, 2);
            addSamples(songs, "Metal", "Cradle", i, 2);
            addSamples(songs, "Metal", "Slipknot", i, 2);
        }

        addSamples(songs, "Live", "Manson", 1, 3);
        addSamples(songs, "Metal", "Manson", 1, 3);
        for (int i = 2; i <= 3; i++) {
            addSamples(songs, "Live", "Manson", i, 2);
            addSamples(songs, "Metal", "Manson", i, 2);
        }

        addSamples(songs, "Metal", "Trivium", 1, 2);
        addSamples(songs, "Metal", "Trivium", 2, 1);

        addSamples(songs, "Pop", "BSB", 1, 2);
        addSamples(songs, "Pop", "BSB", 2, 1);

        addSamples(songs, "Pop", "Gaga", 1, 2);
        addSamples(songs, "Pop", "Gaga", 2, 1);

        addSamples(songs, "Pop", "Katy", 1, 2);
        addSamples(songs, "Pop", "Katy", 2, 1);

        return songs;
    }

    private static void addSamples(Songs songs, String genre, String artist, int song, int samples) {
        File dir = new File(Files.res(), genre);
        for(int i = 1; i <= samples; i++) {
            File file = new File(dir, genre + "_" + artist + "_song" + song + "_" + i + ".wav");
			String genreLabel = genre.equals("Live") ? null : genre;
			String liveLabel = genre.equals("Live") ? "Yes" : "No";
			songs.addSong(new Song(file), genreLabel, liveLabel, artist, genre+artist+song);
        }
    }

	private static Songs createSimpleSongs() {
		WriteOnceVector<SongClass> classes = new WriteOnceVector<>(new SongClass[4]);
		classes.add(new SongClass("Instrument"));
		classes.add(new SongClass("Song"));
		classes.add(new SongClass("SongInstrument"));
		classes.add(new SongClass("Complexity"));

		String[] songNames = new String[]{"A", "B", "C", "C2"};
		Songs songs = new Songs(classes, songNames.length*3);

		for(String songName : songNames) {
			addSimpleSongs(songs, songName, "SGP", 2);
			addSimpleSongs(songs, songName, "CEP", 1);
		}

		return songs;
	}

	private static void addSimpleSongs(Songs songs, String songName, String instrument, int versions) {
		File dir = Files.res();
		for(int i = 1; i <= versions; i++) {
			File file = new File(dir, String.format("Song%s_%s_%d.wav", songName, instrument, i));
			String complexity = songName.equals("C2") ? "Complex" : "Simple";
			songs.addSong(new Song(file), instrument, songName, songName+instrument, complexity);
		}
	}

	/*public static List<Song> getCustomSongs() {
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
	}*/

}
