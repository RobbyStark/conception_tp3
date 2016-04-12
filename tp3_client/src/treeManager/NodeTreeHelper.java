package treeManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Scanner;

import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import httpHelper.HttpRequest;
import httpHelper.HttpRequest.HttpRequestException;
public class NodeTreeHelper {


	/**
	 * builds a tree given the singleton's model
	 * @param model The model to build in
	 */
	public static void buildTree(DefaultTreeModel model){

		String response = HttpRequest.GETfromRoot(TreeSingleton.getInstance().getDefaultRoot());

		Gson gson = new Gson();
		ClientNode[] navigationArray = gson.fromJson(response, ClientNode[].class);

		if(navigationArray!=null)
		for (int i = 0 ; i< navigationArray.length ; i++){
			buildTreeFromString(model,navigationArray[i] );			
			addFolders(model,navigationArray[i].getPath() );
		}

	}

	/**
	 * adds a layer of folders to a tree
	 * @param model The model to build in
	 */
	public static void addFolders(DefaultTreeModel model, String path){

		String response = HttpRequest.GETfromRoot(path);
		Gson gson = new Gson();
		ClientNode[] navigationArray = gson.fromJson(response, ClientNode[].class);

		if (navigationArray != null)
			for (int j = 0 ; j< navigationArray.length ; j++){
				buildTreeFromString(model,navigationArray[j] ); 			
			}
	}

	/**
	 * adds a layer of childs to a tree
	 * @param model The model to build in
	 */
	public static void addChilds(DefaultTreeModel model, String path){

		String response = HttpRequest.GETfromRoot(path);
		Gson gson = new Gson();
		ClientNode[] navigationArray = gson.fromJson(response, ClientNode[].class);

		if (navigationArray != null)
			for (int j = 0 ; j< navigationArray.length ; j++){
				buildTreeFromString(model,navigationArray[j] ); 	
				addFolders(model,navigationArray[j].getPath() );
			}
	}



	/**
	 * Builds a tree from a given forward slash delimited string.
	 * 
	 * @param model The tree model
	 * @param str The string to build the tree from
	 */
	private static void buildTreeFromString(final DefaultTreeModel model, final ClientNode newNode) {

		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

		String [] strings = newNode.getPath().split(TreeSingleton.getInstance().getStringDelimiter());
		
		DefaultMutableTreeNode node = root;

		for (String s: strings) {

			int index = childIndex(node, s);

			if (index < 0) {
				DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(s);
				node.insert(newChild, node.getChildCount());
				node = newChild;
			}
			else {
				node = (DefaultMutableTreeNode) node.getChildAt(index);
			}
		}
	}

	/**
	 * Returns the index of a child of a given node, provided its string value.
	 * 
	 * @param node The node to search its children
	 * @param childValue The value of the child to compare with
	 * @return The index
	 */
	private static int childIndex(final DefaultMutableTreeNode node, final String childValue) {
		Enumeration<DefaultMutableTreeNode> children = node.children();
		DefaultMutableTreeNode child = null;
		int index = -1;

		while (children.hasMoreElements() && index < 0) {
			child = children.nextElement();

			if (child.getUserObject() != null && childValue.equals(child.getUserObject())) {
				index = node.getIndex(child);
			}
		}

		return index;
	}

	/**
	 * Returns the path of a given node
	 * 
	 * @param node The node to search its children
	 * @return The path
	 */
	public static String getAllPath(DefaultMutableTreeNode node){
		String str = (String) node.getUserObject();
		DefaultMutableTreeNode parentNode = node;
		while (parentNode.getParent()!=null){
			parentNode = (DefaultMutableTreeNode) parentNode.getParent();

			str = parentNode.getUserObject()  + "/" + str  ;
		}
		str = str.replaceAll(TreeSingleton.getInstance().getRootName(),"");
		return str;		

	}

	public static void updateSelection(DefaultMutableTreeNode node){
		String str = getAllPath(node);
		addChilds(TreeSingleton.getInstance().getModel(),str);

	}

	/**
	 * finds wether or not a given node has children
	 * @param path The path of the node
	 */
	public static boolean hasChildren(String path){

		String response = HttpRequest.GETfromRoot(path);

		Gson gson = new Gson();
		ClientNode[] node = gson.fromJson(response, ClientNode[].class);

		if (node.length > 0){
			return true;
		}
		else 
			return false;

	}

	/**
	 * Collapses an entire JTree 
	 * @param tree The tree to search its children
	 */
	public static void collapseAll(JTree tree) {
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		collapseAll(tree, new TreePath(root));
	}

	private static void collapseAll(JTree tree, TreePath parent) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				collapseAll(tree, path);
			}
		}
		tree.collapsePath(parent);
	}

}
