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
}
