package association;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Creates a wrapper around an association view to allow the usage of super-types of the key-value types
 * @author Samuel Kolb
 *
 * Copyright (c) Samuel Kolb. All rights reserved.
 */
public class AssociationViewWrapper<O, M, OS extends O, MS extends M> implements Association<O, M> {

	//region Variables
	private final Association<OS, MS> base;

	private final Class<OS> keyClass;

	private final Class<MS> memberClass;
	//endregion

	//region Construction

	/**
	 * Creates a new AssociationViewWrapper with the given parameters
	 * @param base			The base association to be wrapped
	 * @param keyClass		The key super class
	 * @param memberClass	The value super class
	 */
	public AssociationViewWrapper(Association<OS, MS> base, Class<OS> keyClass, Class<MS> memberClass) {
		this.base = base;
		this.keyClass = keyClass;
		this.memberClass = memberClass;
	}
	//endregion

	//region Public methods
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
		return keyClass.isInstance(key) && base.containsKey(keyClass.cast(key));
	}

	@Override
	public boolean containsValue(M value) {
		return memberClass.isInstance(value) && base.containsValue(memberClass.cast(value));
	}

	@Override
	public O getKey(M value) {
		if(!memberClass.isInstance(value))
			throw new IllegalArgumentException();
		return base.getKey(memberClass.cast(value));
	}

	@Override
	public Set<O> getKeys() {
		return Collections.<O>unmodifiableSet(base.getKeys());
	}

	@Override
	public int getKeyCount() {
		return base.getKeyCount();
	}

	@Override
	public Collection<M> getValues(O key) {
		if(!keyClass.isInstance(key))
			throw new IllegalArgumentException();
		return Collections.<M>unmodifiableCollection(base.getValues(keyClass.cast(key)));
	}

	@Override
	public Collection<M> getValues() {
		return Collections.<M>unmodifiableCollection(base.getValues());
	}

	@Override
	public Collection<Collection<M>> getValuesGrouped() {
		throw new UnsupportedOperationException();
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

	@Override
	public boolean usesKeyIdentity() {
		return base.usesKeyIdentity();
	}

	@Override
	public boolean usesValueIdentity() {
		return base.usesValueIdentity();
	}

	//endregion
}
