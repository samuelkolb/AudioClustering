package audio;

import knowledge.Files;
import util.FileUtil;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

/**
 * Created by samuelkolb on 29/10/14.
 *
 * @author Samuel Kolb
 */
public class Song {

	//region Variables
	private final File file;

    public File getFile() {
        return file;
    }

    public String getFilename() {
		return getFile().getName();
	}

	private final double length;

	public double getLength() {
		return length;
	}

	//endregion

	//region Construction

	/**
	 * Creates a new song
	 * @param file	The audio file
	 */
	public Song(File file) {
		this.file = file;
		if(!file.exists())
			throw new IllegalArgumentException("File " + getFile().getAbsolutePath() + " does not exist");
		this.length = getLength(file);
	}

	//endregion

	//region Public methods

	/**
	 * Returns the length of the given audio file
	 * @param file	The audio file
	 * @return	The length of the file
	 */
	public static double getLength(File file) {
		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(file);
			AudioFormat format = audioInputStream.getFormat();
			long frames = audioInputStream.getFrameLength();
			return (frames+0.0) / format.getFrameRate();
		} catch(Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public String toString() {
		return getSongName();
	}

	public String getSongName() {
		return FileUtil.getBasename(getFile().getName());
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		Song song = (Song) o;

		if(file != null ? !file.equals(song.file) : song.file != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return file != null ? file.hashCode() : 0;
	}

	//endregion
}
