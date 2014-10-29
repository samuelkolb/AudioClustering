package compress;


import audio.Song;
import javaFlacEncoder.FLAC_FileEncoder;
import util.FileUtil;

import java.io.File;

/**
 * Created by AnnaES on 29-Oct-14.
 */
public class FlacCompressor implements Compressor<Song> {

    //region Variables

    private FLAC_FileEncoder flacEncoder = new FLAC_FileEncoder();

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
	    File temp = new File("temp");
        File output = new File(temp, name + "_Output.flac");
	    if(!output.exists())
            flacEncoder.encode(element.getFile(), output);
	    return (double) output.length();
    }
    //endregion

}


