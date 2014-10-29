package clustering;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samuelkolb on 29/10/14.
 */
public class NodeFlattener<T> extends NodeVisitor<T> {

	public List<T> list = new ArrayList<>();

	public static <T> List<T> flatten(Node<T> tree) {
		NodeFlattener<T> flattener = new NodeFlattener<>();
		tree.acceptVisitor(flattener);
		return flattener.list;
	}

	@Override
	public void visit(LeafNode<T> node) {
		list.add(node.getElement());
	}

	@Override
	public void visit(TreeNode<T> node) {
		node.getChildren().getFirst().acceptVisitor(this);
		node.getChildren().getSecond().acceptVisitor(this);
	}
}
