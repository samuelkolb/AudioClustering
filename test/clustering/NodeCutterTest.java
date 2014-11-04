package clustering;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class NodeCutterTest {

	private static final Node<Integer> child = new TreeNode<Integer>(1, 2, 1);

	private static final Node<Integer> root = new TreeNode<Integer>(child, 3, 2);

	@Test
	public void testCut() throws Exception {
		List<Node<Integer>> onlyRoot = NodeCutter.cut(root, 2);
		List<Node<Integer>> childAndLeaf = NodeCutter.cut(root, 1);
		List<Node<Integer>> onlyLeafs = NodeCutter.cut(root, 0);

		test(onlyRoot, Arrays.asList(root));
		test(childAndLeaf, Arrays.asList(child, new LeafNode<>(3)));
		test(onlyLeafs, Arrays.asList(new LeafNode<>(1), new LeafNode<>(2), new LeafNode<>(3)));
	}

	private <N extends Node<Integer>> void test(List<Node<Integer>> nodes, List<N> control) {
		assertEquals(control.size(), nodes.size());
		for(int i = 0; i < control.size(); i++)
			assertTrue(nodes.get(i).equals(control.get(i)));
	}
}