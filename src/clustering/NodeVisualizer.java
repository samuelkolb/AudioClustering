package clustering;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by samuelkolb on 30/10/14.
 */
public class NodeVisualizer<T> extends NodeVisitor<T> {

	private PrintStream writer;

	private int counter = 0;

	private List<Integer> leafs = new ArrayList<>();

	public static <T> void visualize(Node<T> tree, File output) {
		output.getParentFile().mkdirs();
		try {
			if(!output.exists())
				output.createNewFile();
			NodeVisualizer<T> visualizer = new NodeVisualizer<>();
			visualizer.writer = new PrintStream(new FileOutputStream(output));
			visualizer.writer.println("digraph clustering {");
			visualizer.writer.println("\tgraph [splines=polyline]");
			tree.acceptVisitor(visualizer);
			visualizer.writer.print("\t{rank=same;");
			for(Integer leaf : visualizer.leafs)
				visualizer.writer.print(leaf + ";");
			visualizer.writer.println("}");
			visualizer.writer.println("}");
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public void visit(LeafNode<T> node) {
		int id = counter++;
		leafs.add(id);
		writer.println("\t" + id + "[label=\"" + node.getElement().toString() + "\", shape=\"box\"]");
	}

	@Override
	public void visit(TreeNode<T> node) {
		int id = counter++;
		writer.println("\t" + id + "[label=\"\"]");
		writer.println("\t" + id + " -> " + counter);
		node.getChildren().getFirst().acceptVisitor(this);
		writer.println("\t" + id + " -> " + counter);
		node.getChildren().getSecond().acceptVisitor(this);
	}
}
