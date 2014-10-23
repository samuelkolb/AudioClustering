package association;

import java.util.*;

/**
 * Associates keys with multiple values.
 * Keys can be obtained through values and values through keys.
 * @author Samuel Kolb
 */
public class HashAssociation<O, M> implements Association<O,M> {

	//region Variables
	private final Map<O, Set<M>> oneToMany;

	private final Map<M, O> oneToOne;

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

	//endregion

	//region Construction

	/**
	 * @effect	| this(true, tue)
	 */
	public HashAssociation() {
		this(true, true);
	}

	/**
	 * Constructs a new empty association.
	 * Object identity should be enabled if there might be distinct objects who are equal w.r.t. hash-code and equals
	 * @param useKeyIdentity	If true object identity will be used for hashing keys, else standard hash-code
	 * @param useValueIdentity	If true object identity will be used for hashing values, else standard hash-code
	 */
	public HashAssociation(boolean useKeyIdentity, boolean useValueIdentity) {
		this.useKeyIdentity = useKeyIdentity;
		oneToMany = useKeyIdentity ? new IdentityHashMap<O, Set<M>>() : new HashMap<O, Set<M>>();
		this.useValueIdentity = useValueIdentity;
		oneToOne = useValueIdentity ? new IdentityHashMap<M, O>() : new HashMap<M, O>();
	}

	/**
	 * Creates a copy of another association
	 * @param association	The association to copy
	 */
	public HashAssociation(Association<O, M> association) {
		this(association.usesKeyIdentity(), association.usesValueIdentity());
		for(O key : association.getKeys())
			for(M value : association.getValues(key))
				associate(key, value);
	}

	/**
	 * Creates a copy of another pairing
	 * @param pairing	The pairing to copy
	 */
	public HashAssociation(Pairing<O, M> pairing) {
		this(pairing.usesKeyIdentity(), pairing.usesValueIdentity());
		for(O key : pairing.getKeys())
				associate(key, pairing.getValue(key));
	}
	//endregion

	//region Public methods
	@Override
	public void associate(O key, M value) {
		if(oneToOne.get(value) == key)
			return;
		removeValueFromCurrentKey(value);
		if(!oneToMany.containsKey(key)) {
			Set<M> set = useValueIdentity
					? Collections.newSetFromMap(new IdentityHashMap<M, Boolean>())
					: new HashSet<M>();
			oneToMany.put(key, set);
		}
		oneToMany.get(key).add(value);
		oneToOne.put(value, key);
	}

	@Override
	public void moveAssociation(O from, O to) {
		Set<M> values = removeAllAssociations(from);
		for(M value : values)
			associate(to, value);
	}

	@Override
	public boolean containsKey(O key) {
		return oneToMany.containsKey(key);
	}

	@Override
	public boolean containsValue(M value) {
		return oneToOne.containsKey(value);
	}

	@Override
	public O getKey(M value) {
		if(!containsValue(value))
			throw new IllegalArgumentException("No such value: "+value);
		return oneToOne.get(value);
	}

	@Override
	public Set<O> getKeys() {
		return oneToMany.keySet();
	}

	@Override
	public int getKeyCount() {
		return oneToMany.size();
	}

	@Override
	public Set<M> getValues(O key) {
		if(!containsKey(key))
			throw new IllegalArgumentException("No such key: "+key);
		return Collections.unmodifiableSet(oneToMany.get(key));
	}

	@Override
	public Set<M> getValues() {
		return oneToOne.keySet();
	}

	@Override
	public Collection<Collection<M>> getValuesGrouped() {
		Collection<Collection<M>> outer = new ArrayList<Collection<M>>(getKeyCount());
		for(Set<M> valueGroup : oneToMany.values())
			outer.add(valueGroup);
		return outer;
	}

	@Override
	public int getValueCount() {
		return oneToOne.size();
	}

	@Override
	public Set<M> removeAllAssociations(O key) {
		Set<M> values = new HashSet<M>(getValues(key));
		for(M value : values)
			removeAssociation(value);
		return values;
	}

	@Override
	public O removeAssociation(M value) {
		O key = oneToOne.remove(value);
		removeValueFromKey(value, key);
		return key;
	}
	//endregion

	private void removeValueFromCurrentKey(M value) {
		if(oneToOne.containsKey(value))
			removeValueFromKey(value, oneToOne.get(value));
	}

	private void removeValueFromKey(M value, O key) {
		if(oneToMany.containsKey(key))
			oneToMany.get(key).remove(value);
		if(oneToMany.get(key).isEmpty())
			oneToMany.remove(key);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("{");
		int i = 0;
		for(O key : getKeys())
			builder.append(i++ > 0 ? "," : "").append(key).append(" -> ").append(getValues(key).toString());
		builder.append("}");
		return builder.toString();
	}
}
