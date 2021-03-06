package clustering;

import compress.DistanceMeasure;
import util.StringUtil;
import util.log.Log;

import java.util.*;

/**
 * Created by samuelkolb on 29/10/14.
 *
 * @author Samuel Kolb
 */
public class HierarchicalClustering<T> implements ClusteringAlgorithm<T> {

	private class Cluster {
		private final Node<T> node;
		private final int delegate;

		public int getDelegate() {
			return delegate;
		}

		public Cluster(Node<T> node, int delegate) {
			this.node = node;
			this.delegate = delegate;
		}

		@Override
		public String toString() {
			return StringUtil.join("", node.toString(), " [", delegate, "]");
		}
	}

	public static class Distances<T> {

		private Map<T, Integer> instanceIndices = new HashMap<>();

		private List<double[]> distances;

		public Distances(List<T> instances, DistanceMeasure<T> distanceMeasure) {
			for(int i = 0; i < instances.size(); i++)
				instanceIndices.put(instances.get(i), i);
			fillDistances(instances, distanceMeasure);
		}

		private void fillDistances(List<T> instances, DistanceMeasure<T> distanceMeasure) {
			distances = new ArrayList<>(instances.size()-1);
			for(int i = 0; i < instances.size()-1; i++) {
				distances.add(new double[instances.size()-i-1]);
				for(int j = i+1; j < instances.size(); j++)
					distances.get(i)[j-i-1] = distanceMeasure.distance(instances.get(i), instances.get(j));
			}
		}

		public double getDistance(T instance1, T instance2) {
			return getDistance(this.instanceIndices.get(instance1), this.instanceIndices.get(instance2));
		}

		private double getDistance(int index1, int index2) {
			if(index1 == index2)
				return 0.0 ;
			if(index1 > index2)
				return getDistance(index2, index1);
			return distances.get(index1)[index2-index1-1];
		}

		private void updateDistance(int index1, int index2, double distance) {
			if(index1 > index2)
				updateDistance(index2, index1, distance);
			else if(index1 < index2)
				distances.get(index1)[index2-index1-1] = distance;
			else if(distance != 0.0)
				throw new IllegalStateException("Distance between an instance and itself must be 0");
		}
	}

	//region Variables
	private final DistanceMeasure<T> distanceMeasure;

	private List<Cluster> clusters;

	public DistanceMeasure<T> getDistanceMeasure() {
		return distanceMeasure;
	}

	private Distances<T> distances;

	public Distances<T> getDistances() {
		return distances;
	}

	private Linkage linkage;

	private int label;
	//endregion

	//region Construction

	/**
	 * Creates a new hierarchical clustering instance
	 * @param distanceMeasure	The distance measure
	 * @param linkage			The linkage
	 */
	public HierarchicalClustering(DistanceMeasure<T> distanceMeasure, Linkage linkage) {
		this.distanceMeasure = distanceMeasure;
		this.linkage = linkage;
	}

	//endregion

	//region Public methods

	@Override
	public Node<T> cluster(Collection<T> instances) {
		init(instances);
		Log.LOG.printLine("Filling distances");
		Log.LOG.printLine("Clustering");
		cluster();
		return clusters.get(0).node;
	}

	//endregion

	private void init(Collection<T> instances) {
		List<T> instances1 = new ArrayList<>(instances);
		this.distances = new Distances<>(instances1, this.distanceMeasure);
		this.clusters = new LinkedList<>();
		for(int i = 0; i < instances1.size(); i++)
			this.clusters.add(new Cluster(new LeafNode<>(instances1.get(i)), i));
		this.label = 1;
	}

	private void cluster() {
		while(clusters.size() > 1) {

			Cluster cluster1 = null;
			Cluster cluster2 = null;
			double value = Double.MAX_VALUE;

			for (int i = 0; i < clusters.size() - 1; i++)
				for (int j = i + 1; j < clusters.size(); j++)
					if (getDistance(clusters.get(i), clusters.get(j)) < value) {
						cluster1 = clusters.get(i);
						cluster2 = clusters.get(j);
						value = getDistance(clusters.get(i), clusters.get(j));
					}

			merge(cluster1, cluster2);
		}
	}

	private void merge(Cluster cluster1, Cluster cluster2) {
		TreeNode<T> node = new TreeNode<>(cluster1.node, cluster2.node, this.label++);
		Cluster newCluster = new Cluster(node, cluster1.getDelegate());
		updateDistances(cluster1, cluster2);
		this.clusters.removeAll(Arrays.asList(cluster1, cluster2));
		this.clusters.add(newCluster);
	}

	private void updateDistances(Cluster growing, Cluster disappearing) {
		for(Cluster cluster : this.clusters)
			if (cluster != growing && cluster != disappearing) {
				double distance1 = getDistance(cluster, growing);
				double distance2 = getDistance(cluster, disappearing);
				double newDistance = linkage.combine(distance1, distance2);
				this.distances.updateDistance(cluster.getDelegate(), growing.getDelegate(), newDistance);
			}
	}

	private double getDistance(Cluster cluster1, Cluster cluster2) {
		return this.distances.getDistance(cluster1.getDelegate(), cluster2.getDelegate());
	}
}
