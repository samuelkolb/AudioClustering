package clustering;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samuelkolb on 04/11/14.
 *
 * @author Samuel Kolb
 */
public class NodeCutter<T> extends NodeVisitor<T> {

	//region Variables
	private List<Node<T>> nodes = new ArrayList<>();

	private final int limit;
	//endregion

	//region Construction

	private NodeCutter(int limit) {
		this.limit = limit;
	}

	//endregion

	//region Public methods

	public static <T> List<Node<T>> cut(Node<T> node, int limit) {
		NodeCutter<T> cutter = new NodeCutter<>(limit);
		node.acceptVisitor(cutter);
		return cutter.nodes;
	}

	@Override
	public void visit(LeafNode<T> node) {
		this.nodes.add(node);
	}

	@Override
	public void visit(TreeNode<T> node) {
		if(node.getLabel() <= limit)
			this.nodes.add(node);
		else {
			node.getChildren().getFirst().acceptVisitor(this);
			node.getChildren().getSecond().acceptVisitor(this);
		}
	}

	//endregion
}
