package compress;

import audio.Song;
import knowledge.Files;
import util.FileUtil;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.SequenceInputStream;

/**
 * Created by samuelkolb on 29/10/14.
 */
public class ConcatenationCombiner implements Combiner<Song> {

	@Override
	public Song combine(Song element1, Song element2) {
		String name = element1.getSongName() + "_AND_" + element2.getSongName() + ".wav";
		File output = new File(Files.temp(), name);

		if(!output.exists())
			FileUtil.concatenate(output, element1.getFile(), element2.getFile());
		concatenate(element1, element2, output);

		return new Song(output);
	}

	private void concatenate(Song element1, Song element2, File output) {
		try {
			AudioInputStream clip1 = AudioSystem.getAudioInputStream(element1.getFile());
			AudioInputStream clip2 = AudioSystem.getAudioInputStream(element2.getFile());

			AudioInputStream appendedFiles =
					new AudioInputStream(
							new SequenceInputStream(clip1, clip2),
							clip1.getFormat(),
							clip1.getFrameLength() + clip2.getFrameLength());

			AudioSystem.write(appendedFiles, AudioFileFormat.Type.WAVE, output);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
}
