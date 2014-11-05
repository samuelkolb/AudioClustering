package util.log;

import util.StringUtil;

/**
 * Created by samuelkolb on 20/10/14.
 *
 * @author Samuel Kolb
 *
 * Copyright (c) Samuel Kolb. All rights reserved.
 */
public class DashLinePrinter implements PrintOperation {

	public final static int DEFAULT_LENGTH = 53;

	//region Variables

	private final int length;

	public final String title;

	//endregion

	//region Construction

	/**
	 * Creates a new dash line printer with the given title and the default length
	 * @param title	The title
	 */
	public DashLinePrinter(String title) {
		this(title, DEFAULT_LENGTH);
	}

	/**
	 * Creates a new dash line printer with the given title and the given length
	 * @param title		The title
	 * @param length	The total number of dashes used (max 3 in front of the title, the rest after the title)
	 */
	public DashLinePrinter(String title, int length) {
		this.length = length;
		this.title = title;
	}
	//endregion

	//region Public methods

	@Override
	public String getString() {
		return StringUtil.getRepeated('-', Math.min(this.length, 3))
				+ " " + this.title + " "
				+ StringUtil.getRepeated('-', Math.max(this.length - 3 - 2 - this.title.length(), 0));
	}
	//endregion
}
