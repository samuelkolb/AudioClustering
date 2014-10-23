package clustering;

import association.Association;
import association.HashAssociation;
import util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Uses agglomerative clustering and stops building clusters as soon as the distance to a cluster becomes too large
 *
 * Possible improvements: (Indexed) Priority queue for minimum distance extraction
 * @author Samuel Kolb
 */
public class ThresholdClustering<C, T> implements ClusteringAlgorithm<C, T> {

	private class Cluster {
		private final C cluster;
		private final int delegate;

		public int getDelegate() {
			return delegate;
		}

		public Cluster(C cluster, int delegate) {
			this.cluster = cluster;
			this.delegate = delegate;
		}

		@Override
		public String toString() {
			return StringUtil.join("", cluster.toString(), " [", delegate, "]");
		}
	}

	//region Variables
	private List<T> instances;

	private Association<Cluster, Integer> assignments;

	private List<double[]> distances;

	private final double threshold;

	private final ClusteringOracle<C, T> oracle;
	//endregion

	//region Construction
	/**
	 * Creates a new instance of the ThresholdClustering algorithm with the given threshold
	 * @param oracle	The clustering oracle needed to perform operations on arbitrary data types
	 * @param threshold	The threshold limiting the maximal intra-cluster distance
	 */
	public ThresholdClustering(ClusteringOracle<C, T> oracle, double threshold) {
		this.oracle = oracle;
		this.threshold = threshold;
	}
	//endregion

	//region Public methods
	@Override
	public Association<C, T> cluster(Collection<T> instances) {
		if(instances.isEmpty())
			return new HashAssociation<C, T>();
		init(instances);
		fillDistances();
		cluster();
		return getResult();
	}
	//endregion

	//region Helper methods
	private void init(Collection<T> instances) {
		this.instances = new ArrayList<T>(instances);
		this.assignments = new HashAssociation<Cluster, Integer>(true, false);
		for(int i = 0; i < this.instances.size(); i++) {
			Cluster cluster = new Cluster(oracle.toCluster(this.instances.get(i)), i);
			assignments.associate(cluster, i);
		}
	}

	private HashAssociation<C, T> getResult() {
		HashAssociation<C, T> result = new HashAssociation<C, T>(true, true);
		List<T> instances = new ArrayList<T>();
		for(Collection<Integer> list : assignments.getValuesGrouped()) {
			instances.clear();
			for(Integer integer : list)
				instances.add(this.instances.get(integer));
			C cluster = oracle.toCluster(instances);
			for(Integer i : list)
				result.associate(cluster, this.instances.get(i));
		}
		return result;
	}

	private void cluster() {
		if(assignments.getKeyCount() <= 1)
			return;

		Cluster growing = null;
		Cluster disappearing = null;
		double value = Double.MAX_VALUE;
		List<Cluster> clusters = new ArrayList<Cluster>(assignments.getKeys());
		for(int i = 0; i < clusters.size()-1; i++) {
			for(int j = i+1; j < clusters.size(); j++) {
				if(getDistance(clusters.get(i), clusters.get(j)) < value) {
					growing = clusters.get(i);
					disappearing = clusters.get(j);
					value = getDistance(clusters.get(i), clusters.get(j));
				}
			}
		}
		if(value > threshold || growing == null)
			return;
		assignments.moveAssociation(disappearing, growing);
		updateDistances(growing, disappearing);
		cluster();
	}

	private void updateDistances(Cluster growing, Cluster disappearing) {
		for(Cluster cluster : assignments.getKeys()) {
			if(cluster != growing) {
				double distance1 = getDistance(cluster, growing);
				double distance2 = getDistance(cluster, disappearing);
				double max = Math.max(distance1, distance2);
				updateDistance(cluster.getDelegate(), growing.getDelegate(), max);
			}
		}
	}

	private void fillDistances() {
		distances = new ArrayList<double[]>(instances.size()-1);
		for(int i = 0; i < instances.size()-1; i++) {
			distances.add(new double[instances.size()-i-1]);
			for(int j = i+1; j < instances.size(); j++)
				distances.get(i)[j-i-1] = oracle.distance(instances.get(i), instances.get(j));
		}
	}

	private void updateDistance(int index1, int index2, double distance) {
		if(index1 == index2)
			return;
		if(index1 > index2)
			updateDistance(index2, index1, distance);
		else
			distances.get(index1)[index2-index1-1] = distance;
	}

	private double getDistance(Cluster cluster1, Cluster cluster2) {
		return getDistance(cluster1.getDelegate(), cluster2.getDelegate());
	}

	private double getDistance(int index1, int index2) {
		if(index1 == index2)
			return 0.0 ;
		if(index1 > index2)
			return getDistance(index2, index1);
		return distances.get(index1)[index2-index1-1];
	}
	//endregion
}
