package clustering;

import association.Association;

import java.util.Collection;

/**
 * A functional interface that allows Rectangles to be clustered in order to discover common dimensions.
 * @author Samuel Kolb
 */
public interface ClusteringAlgorithm<C, T> {

	/**
	 * Given a collection of instances, find clusters so that all instances can be assigned to a cluster.
	 * @param instances	A collection of Rectangles to be clustered
	 * @return An association connecting clusters to the instances assigned to it
	 */
	public Association<C, T> cluster(Collection<T> instances);
}
