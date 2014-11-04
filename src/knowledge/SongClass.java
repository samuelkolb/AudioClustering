package knowledge;

import association.Association;
import association.HashAssociation;
import audio.Song;
import util.Vector;

import java.util.ArrayList;
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
	//endregion
}
