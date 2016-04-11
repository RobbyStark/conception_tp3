
package mvc;

import java.awt.Component;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;

import classLoader.JavaClassLoader;
import composite.Node;
import treeManager.NodeTreeHelper;
import treeManager.TP3Node;
import treeManager.TreeSingleton;
/**
 * The main Model class. following MVC design, this class
 * holds data to be accessed by the controller and displayed by the view
 */
public class FileManagerModel {

	private Map<String, Node> nodeMap_ = new HashMap<String, Node>();
	private Map<String, Component> buttonComponentMap_ = new HashMap<String, Component>();
	private Map<String, Component> textFieldComponentMap_ = new HashMap<String, Component>();

	private TP3Node nodeSelected_;
	private boolean autoRun_ = false;

	/**
	 * Sets the selected node
	 * @param node the selected node
	 */
	public void setNodeSelected(TP3Node node) {
		nodeSelected_ = node;
	}

	/**
	 * Sets the autorun function's state
	 * @param autoRun sets the autorun function's state
	 */
	public void setAutoRun(boolean autoRun) {
		autoRun_ = autoRun;
	}

	/**
	 * Method called when one tree node elements is clicked. Updates the buttons availability.
	 * @param node The name of the clicked node
	 */
	public void updateSelectedNode(DefaultMutableTreeNode node) {
		//TP3Node mnode = (TP3Node) node.getUserObject();4
		NodeTreeHelper.updateSelection(node);
		System.out.println("SelectedNode: "+ node.getUserObject());


		// Obtain the File handle for this node.
		TP3Node fileHandle = nodeSelected_;

		// For each class, use the class loader to determine if the class supports folders and/or files.
		JavaClassLoader javaClassLoader = new JavaClassLoader();
		Iterator it = buttonComponentMap_.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			String commandName = pair.getKey().toString();
			boolean supportFolder = javaClassLoader.invokeVerificationMethod("commands." + commandName, "getSupportFolder");
			boolean supportFile = javaClassLoader.invokeVerificationMethod("commands." + commandName, "getSupportFile");


			// Enable the button dependently if the command supports the file type.
			JButton button = (JButton) pair.getValue();

			String path = TreeSingleton.getInstance().getCurrentPath();

			//nodeSelected_ = NodeTreeHelper.getNode(path);

			nodeSelected_ = null;

			if (((NodeTreeHelper.isDirectory(path)) && (supportFolder)) ||((!NodeTreeHelper.isDirectory(path)) && (supportFile))) 
			{
				button.setEnabled(true);

				// If autorun is enabled, execute the command.
				if (autoRun_) {
					String commandResult = runCommand(commandName);
					getTextFieldComponent(commandName).setText(commandResult);
				}
			} 
			else{
				button.setEnabled(false);
			}
		}
	}

	/**
	 * runs the command
	 * @param commandName the name of the command
	 * @return the command's result
	 */	
	public String runCommand(String commandName) {
		// Use the class loader to run the command.
		JavaClassLoader javaClassLoader = new JavaClassLoader();
		String commandResult = javaClassLoader.invokeRunMethod("commands." + commandName, TreeSingleton.getInstance().getCurrentPath());

		// Update the result of the command.
		return commandResult;

	}

	/**
	 * Method to clear the text field content.
	 */
	public void clearCommandsResult() {
		// Iterate over all text fields and clear it.
		Iterator it = textFieldComponentMap_.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			JTextField textField = (JTextField) pair.getValue();
			textField.setText("");
		}
	}
	/**
	 * Method to add a component to the button map
	 * @param name the component's name
	 * @param component the component to add
	 */	
	public void addComponentToButtonMap(String name, Component component) {
		buttonComponentMap_.put(name, component);
	}
	/**
	 * Method to add a component to the text field's map
	 * @param name the component's name
	 * @param component the component to add
	 */	
	public void addComponentToTextFieldMap(String name, Component component) {
		textFieldComponentMap_.put(name, component);
	}
	/**
	 * Method to add a node the nodes map
	 * @param name the node's name
	 * @param node the node to add
	 */	
	public void addNodetoNodeMap(String name, Node node) {
		nodeMap_.put(name,  node);
	}
	/**
	 * Method to clear the button map.
	 */	
	public void clearButtonMap() {
		buttonComponentMap_.clear();
	}
	/**
	 * Method to clear the text field map.
	 */	
	public void clearTextFieldMap() {
		textFieldComponentMap_.clear();
	}
	/**
	 * Method to get the file handle.
	 * @param name the name of the file
	 * @return the file handle
	 */	
	public File getFileHandle(String name) {
		return nodeMap_.get(name).getFileHandle();
	}
	/**
	 * Method to get the textfield component
	 * @param componentName the name of the component on which to get the text field
	 * @return the textfield component
	 */	
	JTextField getTextFieldComponent(String componentName) {
		return (JTextField) textFieldComponentMap_.get(componentName);
	}
}