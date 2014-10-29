package clustering;

/**
 * Created by samuelkolb on 29/10/14.
 *
 * @author Samuel Kolb
 */
public abstract class NodeVisitor<T> {

	//region Variables

	//endregion

	//region Construction

	//endregion

	//region Public methods

    public abstract void visit(LeafNode<T> node);

    public abstract void visit(TreeNode<T> node);

	//endregion
}
