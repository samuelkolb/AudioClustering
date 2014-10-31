package compress;


import audio.Song;
import javaFlacEncoder.FLAC_FileEncoder;
import knowledge.Files;
import util.FileUtil;
import util.log.Log;
import util.runtime.OperatingSystem;
import util.runtime.Terminal;

import java.io.File;
import java.util.HashMap;

/**
 * Created by AnnaES on 29-Oct-14.
 */
public class FlacCompressor implements Compressor<Song> {

    //region Variables

    private FLAC_FileEncoder flacEncoder = new FLAC_FileEncoder();

	private HashMap<String, Double> cache = new HashMap<>();

	private Terminal terminal = new Terminal();

    //endregion


	public FlacCompressor() {
		/*if(OperatingSystem.getOperatingSystem() == OperatingSystem.MAC) {
			String command = "ls -l " + Files.temp().getAbsolutePath() + " *.flac";
			String[] strings = new Terminal().runCommand(command).split("\n");
			for(String string : strings) {
				if(!string.isEmpty()) {
					String[] parts = string.split("\\s+");
					double size = Double.parseDouble(parts[4]);
					String fileName = FileUtil.getBasename(parts[7]);
					cache.put(fileName, size);
				}
			}
		}*/
	}

    //region Public methods

    /**
     * Compresses the given element and returns the compressed size
     * @param element	The element to compress
     * @return	The size of the compression
     */
    @Override
    public double compress(Song element) {
	    String name = element.getSongName();
	    if(cache.containsKey(name)) {
		    Log.LOG.printLine("Cache hit");
		    return cache.get(name);
	    }
        File output = new File(Files.temp(), name + "_Output.flac");
	    Log.LOG.printLine(output.exists() ? "Cached file" : "Creating file");
	    if(!output.exists())
            flacEncoder.encode(element.getFile(), output);
		double result = terminal.getFileSize(output);
	    cache.put(name, result);
	    return result;
    }
    //endregion

}


