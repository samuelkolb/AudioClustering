package util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Contains methods to perform common array operations
 * @author Samuel Kolb
 *
 * Copyright (c) Samuel Kolb. All rights reserved.
 */
public class ArrayUtil {

	private ArrayUtil() {

	}

	/**
	 * Adds an element to an array
	 * @param array		The array to expand
	 * @param element	The element to add
	 * @param <T>		The type of the array values
	 * @return	\ return == [array[0], ...., array[end], element]
	 */
	public static <T> T[] addElement(T[] array, T element) {
		T[] newArray = createArray(array, array.length+1);
		System.arraycopy(array, 0, newArray, 0, array.length);
		newArray[array.length] = element;
		return newArray;
	}

	/**
	 * Adds to arrays and returns the resulting array
	 * @param array1	The first array
	 * @param array2	The second array
	 * @param <T>		The type of the array values
	 * @return	| return [array1[0], ...., array1[end], array2[0], ..., array2[end]]
	 */
	public static <T> T[] addArrays(T[] array1, T[] array2) {
		T[] newArray = createArray(array1, array1.length+array2.length);
		System.arraycopy(array1, 0, newArray, 0, array1.length);
		System.arraycopy(array2, 0, newArray, array1.length, array2.length);
		return newArray;
	}

	/**
	 * Combines a list of arrays
	 * @param list		The list of arrays to combine
	 * @param template	The template array (for empty lists)
	 * @return	An array that contains all the elements of the arrays in the list in order
	 */
	public static <T> T[] combine(List<T[]> list, T[] template) {
		if(list.isEmpty())
			return createArray(template, 0);
		return combine(list);
	}

	/**
	 * Combines a non-empty list of arrays
	 * @param list	The list of arrays to combine
	 * @return	An array that contains all the elements of the arrays in the list in order
	 */
	public static <T> T[] combine(List<T[]> list) {
		if(list.isEmpty())
			throw new IllegalArgumentException();

		int length = 0;
		for(T[] array : list)
			length += array.length;
		T[] combined = createArray(list.get(0), length);

		int offset = 0;
		for(T[] array : list) {
			System.arraycopy(array, 0, combined, offset, array.length);
			offset += array.length;
		}
		return combined;
	}

	/**
	 * Copies the given array
	 * @param array	The array to copy
	 * @param <T>		The type of the array values
	 * @return	A new array containing exactly all the values of the given array in order
	 */
	public static <T> T[] copy(T[] array) {
		T[] copy = ArrayUtil.createArray(array, array.length);
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}

	/**
	 * Creates a new array with the given length
	 * @param array		The array to use for copying
	 * @param length	The length of the new array
	 * @return	The new array of the given length
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] createArray(T[] array, int length) {
		return (T[]) Array.newInstance(array.getClass().getComponentType(), length);
	}

	/**
	 * Fills an array with the given element
	 * @param array		The array to fill
	 * @param element	The element to fill the array with
	 * @param <T>		The type of the elements in the array
	 * @return	The given array filled with element
	 * 			| foreach(value in return)
	 * 			|	value == element
	 */
	public static <T> T[] fill(T[] array, T element){
		Arrays.fill(array, element);
		return array;
	}

	/**
	 * Wraps the given array in an array wrapper
	 * @param array	The array to wrap
	 * @return	An array wrapper constructed with the given array
	 */
	public static <T> Vector<T> wrap(T[] array) {
		return new Vector<>(array);
	}

}
