package clustering;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Created by samuelkolb on 29/10/14.
 *
 * @author Samuel Kolb
 */
public abstract class Node<T> {

	//region Variables
	private Optional<TreeNode<T>> parent = Optional.empty();

	void setParent(TreeNode<T> parent) {
		this.parent = Optional.of(parent);
	}

	public TreeNode<T> getParent() {
		return parent.get();
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

	public abstract boolean contains(T value);

	public int getDepth() {
		if(!this.parent.isPresent())
			return 0;
		return 1 + this.parent.get().getDepth();
	}

	public LinkedList<TreeNode<T>> getPath() {
		LinkedList<TreeNode<T>> path = new LinkedList<>();
		extendPath(path);
		return path;
	}
	//endregion

	protected void extendPath(LinkedList<TreeNode<T>> path) {
		if(this.parent.isPresent()) {
			path.addFirst(getParent());
			getParent().extendPath(path);
		}
	}
}
