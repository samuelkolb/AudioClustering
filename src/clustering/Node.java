package clustering;

/**
 * Created by samuelkolb on 29/10/14.
 *
 * @author Samuel Kolb
 */
public abstract class Node<T> {

	//region Variables

	//endregion

	//region Construction

	//endregion

	//region Public methods
	public abstract boolean isLeaf();

	public Node<T> getRoot(){
        throw new UnsupportedOperationException("The getRoot() method is not supported!");

    }

	public abstract void acceptVisitor(NodeVisitor<T> visitor);
	//endregion
}
