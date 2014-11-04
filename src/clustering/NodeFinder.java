package clustering;

import java.util.NoSuchElementException;

/**
 * Created by samuelkolb on 31/10/14.
 *
 * @author Samuel Kolb
 */
public class NodeFinder<T> extends NodeVisitor<T> {

	//region Variables
	private T value;

	private LeafNode<T> node;
	//endregion

	//region Construction

	//endregion

	//region Public methods

	/**
	 * Finds the node holding the given value
	 * @param tree	The tree to look in
	 * @param value	The value to find
	 * @return	The node containing the given value
	 */
	public static <T> LeafNode<T> find(Node<T> tree, T value) {
		NodeFinder<T> nodeFinder = new NodeFinder<>();
		nodeFinder.value = value;
		tree.acceptVisitor(nodeFinder);
		if(nodeFinder.node != null)
			return nodeFinder.node;
		throw new NoSuchElementException();
	}

	@Override
	public void visit(LeafNode<T> node) {
		if(node.getElement().equals(this.value))
			this.node = node;
	}

	@Override
	public void visit(TreeNode<T> node) {
		node.getChildren().getFirst().acceptVisitor(this);
		if(this.node == null)
			node.getChildren().getSecond().acceptVisitor(this);
	}

	//endregion
}
