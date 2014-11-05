package clustering;


import util.Pair;
import util.TypePair;
import util.log.Log;

import java.util.LinkedList;

/**
 * Created by AnnaES on 29-Oct-14.
 */
public class TreeNode<T> extends Node<T> {

    private final TypePair<Node<T>> children;

	public TypePair<Node<T>> getChildren() {
		return children;
	}

	private final int label;

	public int getLabel() {
		return label;
	}

	public TreeNode(T value1, T value2){
		this(value1, value2, -1);
	}

	public TreeNode(T value1, Node<T> child2){
		this(value1, child2, -1);
	}

	public TreeNode(Node<T> child1, T value2){
		this(child1, value2, -1);
	}

	public TreeNode(Node<T> child1, Node<T> child2) {
		this(child1, child2, -1);
	}

	public TreeNode(T value1, T value2, int label){
		this(new LeafNode<T>(value1), new LeafNode<T>(value2), label);
	}

	public TreeNode(T value1, Node<T> child2, int label){
		this(new LeafNode<>(value1), child2, label);
	}

	public TreeNode(Node<T> child1, T value2, int label){
		this(child1, new LeafNode<T>(value2), label);
	}

	public TreeNode(Node<T> child1, Node<T> child2, int label){
		children = new TypePair.Implementation<>(child1, child2);
		child1.setParent(this);
		child2.setParent(this);
		this.label = label;
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

	@Override
	public boolean contains(T value) {
		return getChildren().getFirst().contains(value) || getChildren().getSecond().contains(value);
	}

	public double measureSeparation(T element1, T element2) {
		LeafNode<T> leaf1 = NodeFinder.find(this, element1);
		LeafNode<T> leaf2 = NodeFinder.find(this, element2);
		LinkedList<TreeNode<T>> path1 = leaf1.getPath();
		LinkedList<TreeNode<T>> path2 = leaf2.getPath();
		TreeNode<T> split;
		do {
			split = path1.pollFirst();
			path2.pollFirst();
		} while(!path1.isEmpty() && !path2.isEmpty() && path1.peekFirst() == path2.peekFirst());
		int firstNodeLabel = Math.min(leaf1.getParent().getLabel(), leaf2.getParent().getLabel());
		double distance = 2*split.getLabel() - leaf1.getParent().getLabel() - leaf2.getParent().getLabel();
		return distance/(getLabel()*2);
	}

	@Override
	public String toString() {
		return "TreeNode(" + getLabel() + ")";
	}
}
