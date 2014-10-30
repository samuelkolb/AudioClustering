package compress;

import audio.MixingAudioInputStream;
import audio.Song;
import knowledge.Files;
import util.FileUtil;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.util.Arrays;

/**
 * Created by samuelkolb on 30/10/14.
 */
public class MixingCombiner implements Combiner<Song> {

	@Override
	public Song combine(Song element1, Song element2) {
		String name = element1.getSongName() + "_MIX_" + element2.getSongName() + ".wav";
		File output = new File(Files.temp(), name);

		if(!output.exists())
			FileUtil.concatenate(output, element1.getFile(), element2.getFile());
		mix(element1, element2, output);

		return new Song(output);
	}

	private void mix(Song element1, Song element2, File output) {
		try {
			AudioInputStream clip1 = AudioSystem.getAudioInputStream(element1.getFile());
			AudioInputStream clip2 = AudioSystem.getAudioInputStream(element2.getFile());

			AudioInputStream mixed = new AudioInputStream(
							new MixingAudioInputStream(clip1.getFormat(), Arrays.asList(clip1, clip2)),
							clip1.getFormat(),
							Math.max(clip1.getFrameLength(), clip2.getFrameLength()));

			AudioSystem.write(mixed, AudioFileFormat.Type.WAVE, output);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
}
