package clustering;


import util.Pair;
import util.TypePair;

/**
 * Created by AnnaES on 29-Oct-14.
 */
public class TreeNode<T> extends Node<T> {


    private final TypePair<Node<T>> children;

	public TypePair<Node<T>> getChildren() {
		return children;
	}

	public TreeNode(T value1, T value2){
		this(new LeafNode<T>(value1), new LeafNode<T>(value2));
	}

	public TreeNode(T value1, Node<T> child2){
		this(new LeafNode<>(value1), child2);
	}

	public TreeNode(Node<T> child1, T value2){
		this(child1, new LeafNode<T>(value2));
	}

	public TreeNode(Node<T> child1, Node<T> child2){
		children = new TypePair.Implementation<>(child1, child2);
		child1.setParent(this);
		child2.setParent(this);
	}

	@Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public void acceptVisitor(NodeVisitor<T> visitor) {
        visitor.visit(this);
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TreeNode treeNode = (TreeNode) o;

		if (!children.equals(treeNode.children)) return false;

		return true;
	}

	@Override
	public boolean equalsIgnoreOrder(Node<T> node) {
		if(!(node instanceof TreeNode))
			return false;
		TreeNode<T> other = (TreeNode<T>) node;

		if(getChildren().getFirst().equalsIgnoreOrder(other.getChildren().getFirst()))
			return getChildren().getSecond().equalsIgnoreOrder(other.getChildren().getSecond());

		if(getChildren().getSecond().equalsIgnoreOrder(other.getChildren().getFirst()))
			return getChildren().getFirst().equalsIgnoreOrder(other.getChildren().getSecond());

		return false;
	}

	@Override
	public int hashCode() {
		return children.hashCode();
	}
}
