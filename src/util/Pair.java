package util;

/**
 * Created by samuelkolb on 19/10/14.
 *
 * @author Samuel Kolb
 *
 * Copyright (c) Samuel Kolb. All rights reserved.
 */
public interface Pair<T1, T2> {

	public class Implementation<T1, T2> implements Pair<T1, T2> {

		private final T1 first;

		private final T2 second;

		/**
		 * Creates a new pair implementation
		 * @param first		The first element
		 * @param second	The second element
		 */
		public Implementation(T1 first, T2 second) {
			this.first = first;
			this.second = second;
		}

		@Override
		public T1 getFirst() {
			return this.first;
		}

		@Override
		public T1 one() {
			return getFirst();
		}

		@Override
		public T2 getSecond() {
			return this.second;
		}

		@Override
		public T2 two() {
			return getSecond();
		}

		@Override
		public boolean matchesInOrder(T1 object1, T2 object2) {
			return this.first.equals(object1) && this.second.equals(object2);
		}

		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(!(o instanceof Implementation)) return false;

			Implementation that = (Implementation) o;

			if(first != null ? !first.equals(that.first) : that.first != null) return false;
			//noinspection RedundantIfStatement
			if(second != null ? !second.equals(that.second) : that.second != null) return false;

			return true;
		}

		@Override
		public int hashCode() {
			int result = first != null ? first.hashCode() : 0;
			result = 31 * result + (second != null ? second.hashCode() : 0);
			return result;
		}
	}

	/**
	 * Get the first object
	 * @return	The first object
	 */
	public T1 getFirst();

	/**
	 * Get the first object
	 * @return	The first object
	 */
	public T1 one();

	/**
	 * Get the second object
	 * @return	The second object
	 */
	public T2 getSecond();

	/**
	 * Get the second object
	 * @return	The second object
	 */
	public T2 two();

	/**
	 * Returns whether this pair matches the two given objects in order
	 * @param object1	The first object
	 * @param object2	The second object
	 * @return	True iff this pair is equal to the pair object1-object2
	 */
	public boolean matchesInOrder(T1 object1, T2 object2);
}
