package util;

import java.util.*;

/**
 * Created by samuelkolb on 23/10/14.
 *
 * @author Samuel Kolb
 */
public class Vector<T> implements Collection<T> {

	//region Variables
	public final int length;

	private final T[] array;

	void changeElement(int index, T value) {
		this.array[index] = value;
 	}

	/**
	 * Returns the ith element
	 * @param i	The index of the element to return (starting at element 0)
	 * @return	The element at the specified index
	 */
	public T get(int i){
		if(i < 0 || i >= length)
			throw new IllegalArgumentException("Illegal index: " + i);
		return array[i];
	}

	/**
	 * Shortcut for get(i)
	 * @param i	The index of the element to return (starting at element 0)
	 * @return	The element at the specified index
	 */
	public T e(int i) {
		return get(i);
	}

	public T[] getArray() {
		return ArrayUtil.copy(this.array);
	}
	//endregion

	//region Construction

	/**
	 * Creates an empty vector
	 */
	public Vector() {
		this.array = null;
		this.length = 0;
	}

	/**
	 * Creates a new wrapper that contains the given array
	 * @param array	The given array
	 */
	@SafeVarargs
	public Vector(T... array) {
		this.array = ArrayUtil.copy(array);
		this.length = array.length;
	}

	/**
	 * Creates a new vector that contains the same data as the given vector
	 * @param vector	The given vector
	 */
	public Vector(Vector<T> vector) {
		this.array = vector.array;
		this.length = vector.length;
	}
	//endregion

	//region Public methods

	/**
	 * Returns a vector that contains an additional element
	 * @param element	The additional element
	 * @return	| return.length == this.length + 1
	 * 			| return == [this[0], ..., this[end], element]
	 */
	public Vector<T> grow(T element) {
		return new Vector<>(ArrayUtil.addElement(this.array, element));
	}

	@Override
	public int size() {
		return length;
	}

	@Override
	public boolean isEmpty() {
		return length == 0;
	}

	@Override
	public boolean contains(Object o) {
		if(isEmpty())
			return false;
		for(T element : this.array)
			if(element.equals(o))
				return true;
		return false;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {

			private int index = 0;

			@Override
			public boolean hasNext() {
				return index < size();
			}

			@Override
			public T next() {
				return get(index++);
			}
		};
	}

	@Override
	public Object[] toArray() {
		Object[] array = new Object[this.length];
		for(int i = 0; i < size(); i++)
			array[i] = get(i);
		return array;
	}

	@SuppressWarnings("SuspiciousToArrayCall")
	@Override
	public <T1> T1[] toArray(T1[] a) {
		if(isEmpty())
			return new ArrayList<T1>().toArray(a);
		return Arrays.asList(this.array).toArray(a);
	}

	@Override
	public boolean add(T t) {
		throw new UnsupportedOperationException("The collection is not modifiable");
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("The collection is not modifiable");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		if(isEmpty())
			return c.isEmpty();
		return c.containsAll(Arrays.asList(this.array));
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException("The collection is not modifiable");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("The collection is not modifiable");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("The collection is not modifiable");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("The collection is not modifiable");
	}

	/**
	 * Returns a copy of the given vector
	 * @return	A new vector object containing the same data
	 */
	public Vector<T> copy() {
		return new Vector<>(this);
	}

	/**
	 * Corresponds with calling <code>subVector(start, this.length - start)</code>
	 * @param start The starting index
	 * @return  | return == subVector(start, this.length - start)
	 */
	public Vector<T> subVector(int start) {
		return subVector(start, length - start);
	}

	/**
	 * Returns a sub-vector
	 * @param start The starting index within the current vector
	 *              Start is corrected to start % length to a number between 0 and length - 1
	 * @param size  The size of the new vector
	 *              If the size is negative an empty vector is returned
	 *              The size can wrap around the end of the vector, even exceed the original vector size
	 * @return  A new vector that is as long as the given size
	 */
	public Vector<T> subVector(int start, int size) {
		if(size <= 0)
			return new Vector<>();
		start = start % length;
		while(start < 0)
			start += length;
		T[] array = ArrayUtil.createArray(this.array, size);
		for(int i = 0; i < size; i++)
			array[i] = this.array[(start + i) % length];
		return new Vector<>(array);
	}

	/**
	 * Returns a sorted vector containing the same elements as this vector
	 * @return	A new vector whose elements are sorted
	 */
	public Vector<T> sort() {
		Vector<T> vector = new Vector<>(this.array);
		Arrays.sort(vector.array);
		return vector;
	}

	/**
	 * Returns the index of the given element or -1 if the element is not in this vector
	 * @param element	The element to find
	 * @return	| if(this.contains(element))
	 * 			|	this.get(return).equals(element);
	 * 			| else
	 * 			|	return == -1
	 */
	public int indexOf(Object element) {
		for(int i = 0; i < length; i++)
			if(get(i).equals(element))
				return i;
		return -1;
	}

	/**
	 * Returns a sorted vector containing the same elements as this vector
	 * @param comparator	The comparator used for sorting
	 * @return	A new vector whose elements are sorted
	 */
	public Vector<T> sort(Comparator<T> comparator) {
		Vector<T> vector = new Vector<>(this.array);
		Arrays.sort(vector.array, comparator);
		return vector;
	}

	@Override
	public String toString() {
		String[] strings = new String[length];
		for(int i = 0; i < length; i++)
			strings[i] = get(i).toString();
		return "(" + StringUtil.join(", ", strings) + ")";
	}

	//endregion
}
