package util;

import java.io.*;

/**
 * Created by samuelkolb on 29/10/14.
 */
public class FileUtil {

	public static final int BUFFER_SIZE = 1024;

	public static String getBasename(String name) {
		return name.replaceFirst("[.][^.]+$", "");
	}

	public static void concatenate(File output, File... inputFiles) {
		output.getParentFile().mkdirs();
		try{
			if(!output.exists())
				output.createNewFile();
			else {
				PrintWriter printWriter = new PrintWriter(output);
				printWriter.write("");
				printWriter.close();
			}

			InputStream in = null;
			OutputStream out = new FileOutputStream(output, true);
			byte[] buffer = new byte[BUFFER_SIZE];
			int length;

			for(File input : inputFiles) {
				in = new FileInputStream(input);
				while ((length = in.read(buffer)) > 0)
					out.write(buffer, 0, length);
			}

			if(in != null)
				in.close();
			out.close();
		} catch(IOException e) {
			throw new IllegalArgumentException(e);
		}

	}
}
