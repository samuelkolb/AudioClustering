package clustering;

import association.Association;
import association.HashAssociation;
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

	//region Variables
	private final DistanceMeasure<T> distanceMeasure;

	private List<T> instances;

	private List<Cluster> clusters;

	public DistanceMeasure<T> getDistanceMeasure() {
		return distanceMeasure;
	}

	private List<double[]> distances;
	//endregion

	//region Construction

	public HierarchicalClustering(DistanceMeasure<T> distanceMeasure) {
		this.distanceMeasure = distanceMeasure;
	}

	//endregion

	//region Public methods

	@Override
	public Node<T> cluster(Collection<T> instances) {
		init(instances);
		fillDistances();
		cluster();
		return clusters.get(0).node;
	}

	//endregion

	private void init(Collection<T> instances) {
		this.instances = new ArrayList<>(instances);
		this.clusters = new LinkedList<>();
		for(int i = 0; i < this.instances.size(); i++)
			this.clusters.add(new Cluster(new LeafNode<>(this.instances.get(i)), i));
	}

	private void fillDistances() {
		distances = new ArrayList<>(instances.size()-1);
		for(int i = 0; i < instances.size()-1; i++) {
			distances.add(new double[instances.size()-i-1]);
			for(int j = i+1; j < instances.size(); j++)
				distances.get(i)[j-i-1] = getDistanceMeasure().distance(instances.get(i), instances.get(j));
		}
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
		Cluster newCluster = new Cluster(new TreeNode<>(cluster1.node, cluster2.node), cluster1.getDelegate());
		updateDistances(cluster1, cluster2);
		this.clusters.removeAll(Arrays.asList(cluster1, cluster2));
		this.clusters.add(newCluster);
	}

	private void updateDistances(Cluster growing, Cluster disappearing) {
		for(Cluster cluster : this.clusters)
			if (cluster != growing && cluster != disappearing) {
				double distance1 = getDistance(cluster, growing);
				double distance2 = getDistance(cluster, disappearing);
				double max = Math.max(distance1, distance2);
				updateDistance(cluster.getDelegate(), growing.getDelegate(), max);
			}
	}

	private void updateDistance(int index1, int index2, double distance) {
		if(index1 > index2)
			updateDistance(index2, index1, distance);
		else if(index1 < index2)
			distances.get(index1)[index2-index1-1] = distance;
		else if(distance != 0.0)
			throw new IllegalStateException("Distance between an instance and itself must be 0");
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

}
