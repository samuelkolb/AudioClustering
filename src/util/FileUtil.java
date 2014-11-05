package util;

import java.io.*;

/**
 * Created by samuelkolb on 29/10/14.
 *
 * @author Samuel Kolb
 *
 * Copyright (c) Samuel Kolb. All rights reserved.
 */
public class FileUtil {

	public static final int BUFFER_SIZE = 1024;

	/**
	 * Returns the basename of a given name
	 * @param name  The name including extension
	 * @return  The name without the extension
	 */
	public static String getBasename(String name) {
		return name.replaceFirst("[.][^.]+$", "");
	}

	/**
	 * Concatenates the content of the input files and puts them into the output file, erasing previous content
	 * @param output        The file to write the result to
	 * @param inputFiles    An array of input files to concatenate
	 */
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
