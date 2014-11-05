package util;

import java.util.Collection;

/**
 * Created by samuelkolb on 03/11/14.
 *
 * @author Samuel Kolb
 *
 * Copyright (c) Samuel Kolb. All rights reserved.
 */
public class WriteOnceVector<T> extends Vector<T> {

	//region Variables
	private int index = 0;

	public int getIndex() {
		return index;
	}

	//endregion

	//region Construction

	/**
	 * Creates a vector with an initially empty array that can be filled up once
	 * @param array	The initial array, values will be overwritten
	 */
	public WriteOnceVector(T[] array) {
		super(array);
	}
	//endregion

	//region Public methods

	@Override
	public boolean add(T t) {
		if(index >= size())
			throw new UnsupportedOperationException();
		changeElement(index++, t);
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		for(T element : c)
			add(element);
		return true;
	}

	//endregion
}
