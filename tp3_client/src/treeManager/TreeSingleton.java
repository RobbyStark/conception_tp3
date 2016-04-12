package treeManager;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import mvc.FileManagerController;
import mvc.FileManagerView;


/**
* This Singleton holds the JTreee and its properties
*/
public class TreeSingleton {

	private static TreeSingleton instance = null;

	private static DefaultTreeModel model; 
	private static JTree tree; 
	private static DefaultMutableTreeNode root;
	protected static String currentPath;
	protected static String defaultRoot;
	protected static String stringDelimiter;
	protected static String rootLabel;
	protected static String listURL;
	protected static String nameURL;
	protected static JTree tree_;

	protected TreeSingleton() {
		// Exists only to defeat instantiation.
	}
	
	/**
	 * Method to get the singletons unique instance
	 * @return the unique instance
	 */	
	public static TreeSingleton getInstance() {
		if(instance == null) {
			instance = new TreeSingleton();				
			setLocal(true);

		}
		return instance;
	}

	/**
	 * Method to get the selected path
	 * @return the current path
	 */	
	public String getCurrentPath( ) {
		return currentPath; 
	}
	
	public String getNameURL( ) {
		return nameURL; 
	}
	
	public String getListURL( ) {
		return listURL; 
	}
	
	public String getDefaultRoot( ) {
		return defaultRoot; 
	}

	public DefaultTreeModel getModel( ) {
		return model; 
	}
	public String getStringDelimiter( ) {
		return stringDelimiter; 
	}
	
	public String getRootName( ) {
		return rootLabel; 
	}

	public JTree getTree(){
		return tree;
	}

	public static void setCurrentPath(String path){
		currentPath = path;
		if (currentPath.equals(""))
			currentPath = defaultRoot;
	}

	/**
	 * Method to set whether or not the JTree is displaying local files
	 * @param islocal if the tree is local
	 */	
	public static void setLocal(boolean islocal){
		
		if (islocal){
	
			rootLabel = "ROOT";
			stringDelimiter = "\\\\";
			defaultRoot = "C:\\";
			listURL = "http://localhost:8080/tp3_server/server/list/?path=";
			nameURL = "http://localhost:8080/tp3_server/server/name/?path=";
			
		}
		else {
			 
			rootLabel = "DROPBOX";
			stringDelimiter = "/";
			defaultRoot = "/";
			listURL = "http://localhost:8080/tp3_server/dropbox/list/?path=";
			nameURL = "http://localhost:8080/tp3_server/dropbox/name/?path=";		
		}
			root = new DefaultMutableTreeNode(rootLabel);
			model = new DefaultTreeModel(root);
			tree = new JTree(model);
			currentPath = defaultRoot;
			NodeTreeHelper.buildTree(model);			
			FileManagerController.initializeRoot();
	}
	
	

}
