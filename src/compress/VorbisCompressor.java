package compress;

import audio.Song;
import audio.VorbisEncoder;
import knowledge.Files;
import util.log.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by samuelkolb on 30/10/14.
 */
public class VorbisCompressor implements Compressor<Song> {

	private VorbisEncoder encoder = new VorbisEncoder();

	private Map<String, Double> cache = new HashMap<>();

	@Override
	public double compress(Song element) {
		String name = element.getSongName();
		if(cache.containsKey(name)) {
			Log.LOG.printLine("Cache hit");
			return cache.get(name);
		}
		File output = new File(Files.temp(), name + "_Output.ogg");
		Log.LOG.printLine(output.exists() ? "Cached file" : "Creating file");
		if(!output.exists())
			encoder.encode(element.getFile(), output);
		double result = (double) output.length();
		cache.put(name, result);
		return result;
	}
}
