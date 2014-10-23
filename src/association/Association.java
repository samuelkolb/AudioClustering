package association;

import java.util.Collection;
import java.util.Set;

/**
 * Associates keys with multiple values.
 * Keys can be obtained through values and values through keys.
 * @author Samuel Kolb
 */
public interface Association<O, M> extends Relation<O, M> {

	public static class View<O, M> implements Association<O, M> {

		private final Association<O, M> base;

		/**
		 * Creates a view on another association that cannot influence this association
		 * @param base	The base association
		 */
		public View(Association<O, M> base) {
			this.base = base;
		}

		@Override
		public boolean usesKeyIdentity() {
			return base.usesKeyIdentity();
		}

		@Override
		public boolean usesValueIdentity() {
			return base.usesValueIdentity();
		}

		@Override
		public void associate(O key, M value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void moveAssociation(O from, O to) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean containsKey(O key) {
			return base.containsKey(key);
		}

		@Override
		public boolean containsValue(M value) {
			return base.containsValue(value);
		}

		@Override
		public O getKey(M value) {
			return base.getKey(value);
		}

		@Override
		public Set<O> getKeys() {
			return base.getKeys();
		}

		@Override
		public int getKeyCount() {
			return base.getKeyCount();
		}

		@Override
		public Collection<M> getValues(O key) {
			return base.getValues(key);
		}

		@Override
		public Collection<M> getValues() {
			return base.getValues();
		}

		@Override
		public Collection<Collection<M>> getValuesGrouped() {
			return base.getValuesGrouped();
		}

		@Override
		public int getValueCount() {
			return base.getValueCount();
		}

		@Override
		public Set<M> removeAllAssociations(O key) {
			throw new UnsupportedOperationException();
		}

		@Override
		public O removeAssociation(M value) {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Associate all values currently associated to the key from to the new key to
	 * @param from	The key remove associations for
	 * @param to	The key to move associations to
	 */
	public void moveAssociation(O from, O to);

	/**
	 * Returns the key associated with the given value
	 * @param value	The value
	 * @return	The key previously associated with the given value
	 * 			| this.getValues(return).contains(value)
	 */
	public O getKey(M value);

	/**
	 * Returns the values associated with the given key
	 * @param key	The key
	 * @return	An unmodifiable set of values previously associated with the given key
	 * 			| for(value : return)
	 * 			|	this.getKey(value) == key
	 */
	public Collection<M> getValues(O key);

	/**
	 * Returns all values, grouped by key
	 * @return	A collection of sets, with one set per key so that each set contains all values associated to the key
	 */
	public Collection<Collection<M>> getValuesGrouped();

	/**
	 * Removes all associations that involve the given key
	 * @param key    The key
	 * @return	All associations involving the given key or null if none exist
	 * @post	| !new.containsKey(key)
	 * 			| for(value : old.getValues(key))
	 * 			|	!new.containsValue(value)
	 */
	public Collection<M> removeAllAssociations(O key);

	/**
	 * Removes the association of the given value
	 * @param value	The value
	 * @return	The associated key or null if no associated key exists
	 * @post	| !new.containsValue(value)
	 * 			| if(old.getValues(old.getKey(value)).size() == 1)
	 * 			|	!new.containsKey(old.getKey(value))
	 */
	public O removeAssociation(M value);

	/**
	 * Import the given association and all its key-value bindings
	 * @param association   The association to be imported
	 * @post    | for(key->value : association)
	 *          |   new.getValues(key).contains(value)
	 *          |   new.getKey(value) == key
	 *
	public void importAssociation(Association<O, M> association);
	*/
}
