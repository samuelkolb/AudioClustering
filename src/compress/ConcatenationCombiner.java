package compress;

import audio.Song;
import util.FileUtil;

import java.io.File;

/**
 * Created by samuelkolb on 29/10/14.
 */
public class ConcatenationCombiner implements Combiner<Song> {

	@Override
	public Song combine(Song element1, Song element2) {
		String name = element1.getSongName() + "_AND_" + element2.getSongName() + ".wav";
		File temp = new File("temp");
		File output = new File(temp, name);
		if(!output.exists())
			FileUtil.concatenate(output, element1.getFile(), element2.getFile());
		return new Song(output.getAbsolutePath());
	}
}
