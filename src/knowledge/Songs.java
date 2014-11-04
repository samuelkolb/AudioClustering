package knowledge;

import audio.Song;
import util.Vector;
import util.WriteOnceVector;

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


    private static Songs samples = createSamples();

    public static Songs getSamples() {
        return samples;
    }

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

    private static Songs createSamples() {
        WriteOnceVector<SongClass> classes = new WriteOnceVector<>(new SongClass[3]);

        classes.add(new SongClass("Genre"));
	    classes.add(new SongClass("Artist"));
        /*classes.add(new SongClass("Songsamples"));
        classes.add(new SongClass("Song"));//*/
        classes.add(new SongClass("Live"));

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

    	/*songs.addAll(getSongSamples("Classical", "Brahms", 2));
		songs.addAll(getSongSamples("Classical", "Mozart", 2));
		songs.addAll(getSongSamples("Classical", "Tschaikovsky", 4));
		songs.addAll(getSongSamples("Live", "Manson", 7));
		songs.addAll(getSongSamples("Live", "Slipknot", 4));
		songs.addAll(getSongSamples("Metal", "Cradle", 4));
        songs.addAll(getSongSamples("Metal", "Manson", 5));
        songs.addAll(getSongSamples("Metal", "Slipknot", 4));
		songs.addAll(getSongSamples("Metal", "Trivium", 3));
		songs.addAll(getSongSamples("Pop", "BSB", 3));
		songs.addAll(getSongSamples("Pop", "Gaga", 3));
		songs.addAll(getSongSamples("Pop", "Katy", 3));*/
        return songs;
    }

    private static void addSamples(Songs songs, String genre, String artist, int song, int samples) {
        File dir = new File(Files.res(), genre);
        for(int i = 1; i <= samples; i++) {
            File file = new File(dir, genre + "_" + artist + "_song" + song + "_" + i + ".wav");
			String genreLabel = genre.equals("Live") ? null : genre;
			String liveLabel = genre.equals("Live") ? "Yes" : "No";
			songs.addSong(new Song(file), genreLabel, liveLabel, artist);
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
