package clustering;

/**
 * Created by AnnaES on 29-Oct-14.
 */
public class LeafNode<T> extends Node<T> {

    private T element;

    public LeafNode(T element){
        this.element = element;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    public T getElement() {
        return element;
    }

    @Override
    public void acceptVisitor(NodeVisitor<T> visitor) {
        visitor.visit(this);
    }

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		LeafNode leafNode = (LeafNode) o;

		return element == null ? leafNode.element == null : element.equals(leafNode.element);

	}

	@Override
	public boolean equalsIgnoreOrder(Node<T> node) {
		return equals(node);
	}

	@Override
	public int hashCode() {
		return element != null ? element.hashCode() : 0;
	}

	@Override
	public boolean contains(T value) {
		return getElement().equals(value);
	}

	@Override
	public int getLabel() {
		return 0;
	}
}
