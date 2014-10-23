package clustering;

import java.util.List;

/**
 * Allows to do operations on instances and clusters
 * @author Samuel Kolb
 */
public interface ClusteringOracle<C, T> {

	/**
	 * Convert an instance to a cluster
	 * @param instance	The instance to be converted
	 * @return	The cluster that corresponds with the instance.
	 * 			The distance between clusters should correspond with the distance between instances
	 */
	public C toCluster(T instance);

	/**
	 * Converts instances to a cluster
	 * @param instances	The instances to be converted
	 * @return	A cluster that bundles the provided instances
	 */
	public C toCluster(List<T> instances);

	/**
	 * Calculates the distances between two instances
	 * @param instance1	The first instance
	 * @param instance2	The second instance
	 * @return	A double representing the distance between the two instances
	 */
	public double distance(T instance1, T instance2);
}
