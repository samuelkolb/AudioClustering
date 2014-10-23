package association;

import java.util.*;

/**
 * A list association allows multiple values to be stored for every key.
 * Contrary to the hash association there are no efficient operation to get keys and remove values.
 * @author Samuel Kolb
 */
public class ListAssociation<O, M> implements Association<O, M> {

	//region Variables
	private final Map<O, List<M>> oneToMany;

	private final boolean useKeyIdentity;

	@Override
	public boolean usesKeyIdentity() {
		return useKeyIdentity;
	}

	private final boolean useValueIdentity;

	@Override
	public boolean usesValueIdentity() {
		return useValueIdentity;
	}

	private int valueCount = 0;
	//endregion

	//region Construction

	/**
	 * Creates a new list association
	 * Wrapper for this(true, true)
	 */
	public ListAssociation() {
		this(true, true);
	}

	/**
	 * Creates a new list association
	 * @param useKeyIdentity	If this is true then keys will be compared and hashed according to their identity
	 * @param useValueIdentity	If this is true then values will be compared and hashed according to their identity
	 */
	public ListAssociation(boolean useKeyIdentity, boolean useValueIdentity) {
		oneToMany = useKeyIdentity ? new IdentityHashMap<O, List<M>>() : new HashMap<O, List<M>>();
		this.useKeyIdentity = useKeyIdentity;
		this.useValueIdentity = useValueIdentity;
	}
	//endregion

	//region Public methods

	@Override
	public void moveAssociation(O from, O to) {
		Collection<M> values = removeAllAssociations(from);
		for(M value : values)
			associate(to, value);
	}

	@Override
	public O getKey(M value) {
		for(O key : getKeys())
			if(containsValue(getValues(key), value))
				return key;
		return null;
	}

	@Override
	public Collection<M> getValues(O key) throws IllegalArgumentException {
		if(!containsKey(key))
			throw new IllegalArgumentException("No such key: "+key);
		return Collections.unmodifiableList(oneToMany.get(key));
	}

	@Override
	public Collection<Collection<M>> getValuesGrouped() {
		Collection<Collection<M>> outer = new ArrayList<Collection<M>>(getKeyCount());
		for(List<M> valueGroup : oneToMany.values())
			outer.add(valueGroup);
		return outer;
	}

	@Override
	public Collection<M> removeAllAssociations(O key) {
		valueCount -= oneToMany.get(key).size();
		return oneToMany.remove(key);
	}

	@Override
	public O removeAssociation(M value) {
		O key = getKey(value);
		if(key != null) {
			removeValue(oneToMany.get(key), value);
			valueCount--;
			if(oneToMany.get(key).isEmpty())
				removeAllAssociations(key);
		}
		return key;
	}

	@Override
	public void associate(O key, M value) {
		if(!containsKey(key))
			oneToMany.put(key, new ArrayList<M>());
		oneToMany.get(key).add(value);
		valueCount++;
	}

	@Override
	public boolean containsKey(O key) {
		return oneToMany.containsKey(key);
	}

	@Override
	public boolean containsValue(M value) {
		return containsValue(getValues(), value);
	}

	@Override
	public Set<O> getKeys() {
		return Collections.unmodifiableSet(oneToMany.keySet());
	}

	@Override
	public int getKeyCount() {
		return oneToMany.size();
	}

	@Override
	public Collection<M> getValues() {
		List<M> values = new ArrayList<M>(getValueCount());
		for(O key : getKeys())
			values.addAll(oneToMany.get(key));
		return values;
	}

	@Override
	public int getValueCount() {
		return valueCount;
	}
	//endregion

	/**
	 * Returns whether or not the given collection contains the given value according to this association.
	 * The implementation depends on whether value identity is used.
	 * @param values	The collection
	 * @param value		The value
	 * @return	True iff the collection contains the value according to whether value identity is used
	 */
	public boolean containsValue(Collection<M> values, M value) {
		for(M candidate : values)
			if(usesValueIdentity() ? candidate == value : candidate.equals(value))
				return true;
		return false;
	}

	@SuppressWarnings("UnusedReturnValue")
	private boolean removeValue(List<M> values, M value) {
		for(int i = 0; i < values.size(); i++)
			if(equal(values.get(i), value)) {
				values.remove(i);
				return true;
			}
		return false;
	}

	/**
	 * Returns whether or not the two values are equal according to this association.
	 * The implementation depends on whether value identity is used.
	 * @param value1	The first value
	 * @param value2		The second value
	 * @return	True iff the two values are equal according to whether value identity is used
	 */
	public boolean equal(M value1, M value2) {
		if(value1 == null)
			return value2 == null;
		return usesValueIdentity() ? value1 == value2 : value1.equals(value2);
	}
}
