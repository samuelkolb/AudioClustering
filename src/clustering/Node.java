package clustering;

import java.util.Optional;

/**
 * Created by samuelkolb on 29/10/14.
 *
 * @author Samuel Kolb
 */
public abstract class Node<T> {

	//region Variables
	private Optional<Node<T>> parent = Optional.empty();

	void setParent(Node<T> parent) {
		this.parent = Optional.of(parent);
	}
	//endregion

	//region Construction

	//endregion

	//region Public methods
	public abstract boolean isLeaf();

	public Node<T> getRoot(){
        if(!this.parent.isPresent())
	        return this;
		return this.parent.get().getRoot();
    }

	public abstract void acceptVisitor(NodeVisitor<T> visitor);

	public abstract boolean equalsIgnoreOrder(Node<T> node);
		//endregion
}
