package compress;


import audio.Song;
import javaFlacEncoder.FLAC_FileEncoder;
import knowledge.Files;
import util.FileUtil;
import util.log.Log;

import java.io.File;
import java.util.HashMap;

/**
 * Created by AnnaES on 29-Oct-14.
 */
public class FlacCompressor implements Compressor<Song> {

    //region Variables

    private FLAC_FileEncoder flacEncoder = new FLAC_FileEncoder();

	private HashMap<String, Double> cache = new HashMap<>();

    //endregion



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
	    Log.LOG.printLine(output.exists() ? "Cached file" : "Creatig file");
	    if(!output.exists())
            flacEncoder.encode(element.getFile(), output);
	    double result = (double) output.length();
	    cache.put(name, result);
	    return result;
    }
    //endregion

}


