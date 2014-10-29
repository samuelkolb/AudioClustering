package compress;

import audio.Song;
import org.junit.Test;
import compress.FlacCompressor;
import util.log.LinkTransformer;
import util.log.Log;

import java.io.File;

import static org.junit.Assert.*;



public class FlacCompressorTest {

    @Test
    public void test1(){

        FlacCompressor fCompressor = new FlacCompressor();

        Song song = new Song("res" + File.separator + "SongA_CEP_1.wav");
        double size = fCompressor.compress(song);

        Log.LOG.printLine("Size: " + size);
        Log.LOG.printLine("Input size: " + song.getFile().length());

        assertTrue(size < song.getFile().length());

    }


}