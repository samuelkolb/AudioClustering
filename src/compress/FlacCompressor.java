package compress;


import audio.Song;
import javaFlacEncoder.FLAC_FileEncoder;

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
    public double compress(Song element){
        File output = new File("output");
        FLAC_FileEncoder.Status status = flacEncoder.encode(element.getFile(), output);
        double size = output.length();
        if (! output.delete())
            throw new IllegalStateException("Could not delete file " + output.getAbsolutePath());
        return size;
    }
    //endregion

}


