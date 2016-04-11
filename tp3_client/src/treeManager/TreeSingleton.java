package treeManager;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class TreeSingleton {

	private static TreeSingleton instance = null;

	private static DefaultTreeModel model; 
	private static JTree tree; 
	private static DefaultMutableTreeNode root;

	protected TreeSingleton() {
		// Exists only to defeat instantiation.
	}
	
	private static String defaultRoot = "C:\\";
	public static TreeSingleton getInstance() {
		if(instance == null) {
			instance = new TreeSingleton();	
			root = new DefaultMutableTreeNode("ROOT");
			model = new DefaultTreeModel(root);
			tree = new JTree(model);
			currentPath = defaultRoot;
			NodeTreeHelper.buildTree(model);

		}
		return instance;
	}

	protected static String currentPath;

	protected static JTree tree_;



	public String getCurrentPath( ) {
		return currentPath; 
	}

	public DefaultTreeModel getModel( ) {
		return model; 
	}

	public JTree getTree(){
		return tree;
	}

	public static void setCurrentPath(String path){
		currentPath = path;
		if (currentPath.equals(""))
			currentPath = defaultRoot;
	}

}
