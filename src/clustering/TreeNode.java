package clustering;


import util.Pair;
import util.TypePair;

/**
 * Created by AnnaES on 29-Oct-14.
 */
public class TreeNode<T> extends Node<T> {


    private final TypePair<T> children;

    public TreeNode(T child1, T child2){
        children = new TypePair.Implementation<T>(child1, child2);
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    public TypePair<T> getChildren() {
        return children;
    }

    @Override
    public void acceptVisitor(NodeVisitor<T> visitor) {
        visitor.visit(this);
    }
}
