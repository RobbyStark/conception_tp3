

package composite;

import java.io.File;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Class that represents a composite Directory object, containing Directory and
 * FileLead objects.
 */
public class Directory extends Node {
	// Array that contains directories and files contained by this directory.
	private ArrayList<Node> nodes_ = new ArrayList<Node>();
	
	/**
	 * Class constructor.
	 * @param file The file represented by the Directory object.
	 */
	public Directory(File file) {
		file_ = file;
	}
	
	/**
	 * Adds a new directory to the array of nodes.
	 * @param file used to instantiate the new Directory object
	 * @return the newly created node
	 */
	public Node addDirectory(File file) {
		Node node = new Directory(file);
		this.nodes_.add(node);
		// Create the new DefaultMutableTreeNode object.
		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(file.getName());
		this.getTreeNode().add(treeNode);
		node.setTreeNode(treeNode);
		return node;
	}
	
	/**
	 * Adds a new file (leaf) to the array of nodes.
	 * @param file used to instantiate the new FileLeaf object
	 * @return the newly created node
	 */
	public Node addFileLeaf(File file) {
		Node node = new FileLeaf(file);
		this.nodes_.add(node);
		// Create the new DefaultMutableTreeNode object.
		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(file.getName());
		this.getTreeNode().add(treeNode);
		node.setTreeNode(treeNode);
		return node;
	}
}