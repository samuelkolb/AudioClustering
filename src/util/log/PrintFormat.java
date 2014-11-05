package util.log;

import util.StringUtil;

import java.util.Collection;

/**
 * Supports different print formats for lists
 * @author Samuel Kolb
 *
 * Copyright (c) Samuel Kolb. All rights reserved.
 */
public enum PrintFormat {
	COMMA_SEPARATED {
		@Override
		public String print(Object... objects) {
			return StringUtil.join(",", objects);
		}
	}, TIMES_SEPARATED {
		@Override
		public String print(Object... objects) {
			return StringUtil.join("x", objects);
		}
	}, NOT_SEPARATED {
		@Override
		public String print(Object... objects) {
			return StringUtil.join("", objects);
		}
	};

	/**
	 * Returns a string representing the listed objects
	 * @param objects	A list of objects (printed using their toString() method)
	 * @return A textual representation of the given list
	 */
	public abstract String print(Object... objects);

	/**
	 * Wrapper for the array based print method
	 * @param collection	The collection of objects to print
	 * @return	A textual representation of the given collection
	 */
	public String print(Collection<?> collection) {
		return print(collection.toArray());
	}
}
