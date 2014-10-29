package clustering;

import org.junit.Test;

import static org.junit.Assert.*;

public class NodeTest {

	public final TreeNode<Integer> TREE_1 = new TreeNode<>(1, new TreeNode<>(2, new TreeNode<>(3, 4)));
	public final TreeNode<Integer> TREE_2 = new TreeNode<>(new TreeNode<>(new TreeNode<>(3, 4), 2), 1);
	public final TreeNode<Integer> TREE_3 = new TreeNode<>(1, new TreeNode<>(2, 3));
	public final TreeNode<Integer> TREE_4 = new TreeNode<>(1, new TreeNode<>(2, 3));

	@Test
	public void testGetRoot() throws Exception {
		assertEquals(TREE_1, TREE_1.getChildren().getFirst().getRoot());
	}

	@Test
	public void testEqualsIgnoreOrder() throws Exception {
		assertTrue(TREE_1.equalsIgnoreOrder(TREE_2));
		assertFalse(TREE_2.equalsIgnoreOrder(TREE_3));
		assertTrue(TREE_3.equalsIgnoreOrder(TREE_4));
	}

	@Test
	public void testEquals() throws Exception {
		assertFalse(TREE_1.equals(TREE_2));
		assertFalse(TREE_2.equals(TREE_3));
		assertTrue(TREE_3.equals(TREE_4));
	}
}