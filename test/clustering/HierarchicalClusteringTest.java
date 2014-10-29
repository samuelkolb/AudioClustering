package clustering;

import compress.DistanceMeasure;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class HierarchicalClusteringTest {

	private static final List<Integer> NUMBERS = Arrays.asList(1, 5, 8, 3, -10);

	private class IntegerDistance implements DistanceMeasure<Integer> {
		@Override
		public double distance(Integer element1, Integer element2) {
			return Math.abs(element1 - element2);
		}
	}

	@Test
	public void testNumberClustering() {
		ClusteringAlgorithm<Integer> algorithm = new HierarchicalClustering<>(new IntegerDistance());
		Node<Integer> solution = algorithm.cluster(NUMBERS);
		Node<Integer> root = new TreeNode<>(new TreeNode<>(new TreeNode<>(1, 3), new TreeNode<>(5, 8)), -10);
		assertTrue(solution.equalsIgnoreOrder(root));
	}
}