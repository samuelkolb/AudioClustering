package util;

import util.log.PrintFormat;

import java.util.*;

/**
 * Holds two objects
 * @author Samuel Kolb
 */
public interface TypePair<T> extends Pair<T, T>, Collection<T> {

	public static class Implementation<T> extends Pair.Implementation<T, T> implements TypePair<T> {

		/**
		 * Creates a new implementation with the given objects
		 * @param first		The first object
		 * @param second	The second object
		 */
		public Implementation(T first, T second) {
			super(first, second);
		}

		@Override
		public T get(int i) throws NoSuchElementException {
			if(i == 0)
				return getFirst();
			if(i == 1)
				return getSecond();
			throw new NoSuchElementException();
		}

		@Override
		public List<T> getList() {
			return Arrays.asList(one(), two());
		}

		@Override
		public int indexOf(T object) {
			if(object == getFirst())
				return 0;
			if(object == getSecond())
				return 1;
			throw new IllegalArgumentException("No such object: "+object);
		}

		@Override
		public T getOther(T object) {
			return get((indexOf(object) + 1) % 2);
		}

		@Override
		public boolean matches(T object1, T object2) {
			return (getFirst().equals(object1) && getSecond().equals(object2))
					|| (getFirst().equals(object2) && getSecond().equals(object1));
		}

		@Override
		public boolean matchesInOrder(T object1, T object2) {
			return getFirst().equals(object1) && getSecond().equals(object2);
		}

		@Override
		public TypePair<T> replace(int index, T newObject) {
			if(index != 0 && index != 1)
				throw new IllegalArgumentException();
			T first = index == 0 ? newObject : getFirst();
			T second = index == 1 ? newObject : getSecond();
			return new TypePair.Implementation<>(first, second);
		}

		@Override
		public String toString() {
			return PrintFormat.NOT_SEPARATED.print("(1:", getFirst(), ", 2:", getSecond(), ")");
		}

		//region Collection implementation
		@Override
		public int size() {
			return 2;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public boolean contains(Object o) {
			return o == getFirst() || o == getSecond();
		}

		@Override
		public Iterator<T> iterator() {
			return getList().iterator();
		}

		@Override
		public Object[] toArray() {
			return new Object[]{one(), two()};
		}

		@SuppressWarnings("SuspiciousToArrayCall")
		@Override
		public <T1> T1[] toArray(T1[] a) {
			return getList().toArray(a);
		}

		@Override
		public boolean add(T t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean remove(Object o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return getList().containsAll(c);
		}

		@Override
		public boolean addAll(Collection<? extends T> c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clear() {
			throw new UnsupportedOperationException();
		}
		//endregion
	}

	/**
	 * Returns ith object
	 * @param i	The index
	 * @return	| if(i == 0)
	 * 			|	return == getFirst();
	 * 			| if(i == 1)
	 * 			|	return getSecond();
	 * @throws java.util.NoSuchElementException	Iff i != 0 && i != 1
	 */
	public T get(int i) throws NoSuchElementException;

	/**
	 * Returns the type pair as a list
	 * @return	A list containing the two objects in this type pair
	 * 			| return.size() == 2
	 * 			| return.contains(this.one()) && return.contains(this.two())
	 */
	public List<T> getList();

	/**
	 * Returns the index of the given object
	 * @param object	The object who's index has to be found
	 * @return	| if(object == getFirst())
	 * 			|	return == 0
	 * 			| if(object == getSecond())
	 * 			|	return == 1
	 */
	public int indexOf(T object);

	/**
	 * Returns the other object
	 * @param object	The object not to return
	 * @return	The element of this pair that is not the given object
	 */
	public T getOther(T object);

	/**
	 * Returns whether this pair matches the two given objects
	 * @param object1	An object
	 * @param object2	Another object
	 * @return	True iff this pair is equal to the pair object1-object2 or object2-object1
	 */
	public boolean matches(T object1, T object2);

	/**
	 * Returns a copy of this type pair where the object at the given index has been replaced by the new object
	 * @param index		The index of the object to replace
	 * @param newObject	The new object
	 * @return	| return.contains(newObject)
	 * 			| return.get(this.indexOf(this.getOther(this.get(index)))) == this.getOther(this.get(index))
	 */
	public TypePair<T> replace(int index, T newObject);
}
