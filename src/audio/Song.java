package audio;

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
	 * @param filename	The filename of the audio file
	 */
	public Song(String filename) {
		this.file = new File(filename);
		if(!file.exists())
			throw new IllegalArgumentException("File " + filename + " does not exist");
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

	//endregion
}
