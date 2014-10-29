package clustering;

import org.junit.Test;

import static org.junit.Assert.*;

public class NodePrinterTest {

	public static final Node<Integer> CHILD_1 = new LeafNode<>(1);
	public static final Node<Integer> CHILD_2 = new LeafNode<>(2);
	public static final Node<Integer> CHILD_3 = new LeafNode<>(3);
	public static final Node<Integer> CHILD_4 = new LeafNode<>(4);

	@Test
	public void testPrintRight() throws Exception {

		Node<Integer> root = new TreeNode<>(new TreeNode<>(new TreeNode<>(CHILD_1, CHILD_2), CHILD_3), CHILD_4);
		assertEquals("(((1 | 2) | 3) | 4)", NodePrinter.printTree(root));
	}

	@Test
	public void testPrintMiddle() throws Exception {

		Node<Integer> root = new TreeNode<>(CHILD_4, new TreeNode<>(new TreeNode<>(CHILD_1, CHILD_2), CHILD_3));
		assertEquals("(4 | ((1 | 2) | 3))", NodePrinter.printTree(root));
	}
}