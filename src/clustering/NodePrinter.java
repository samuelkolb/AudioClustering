package clustering;

/**
 * Created by samuelkolb on 29/10/14.
 */
public class NodePrinter<T> extends NodeVisitor<T> {

	private StringBuilder builder = new StringBuilder();

	public static <T> String printTree(Node<T> tree) {
		NodePrinter<T> visitor = new NodePrinter<>();
		tree.acceptVisitor(visitor);
		return visitor.builder.toString();
	}

	@Override
	public void visit(LeafNode<T> node) {
		builder.append(node.getElement().toString());
	}

	@Override
	public void visit(TreeNode<T> node) {
		builder.append("(");
		node.getChildren().getFirst().acceptVisitor(this);
		builder.append(" | ");
		node.getChildren().getSecond().acceptVisitor(this);
		builder.append(")");
	}
}
