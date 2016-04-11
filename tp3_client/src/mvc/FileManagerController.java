package mvc;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import composite.Directory;
import composite.FileLeaf;
import composite.Node;
import treeManager.NodeTreeHelper;
import treeManager.TreeSingleton;

/**
 * The main Controller class. following MVC design, this class
 * can send update commands to the model and view.
 */
public class FileManagerController {

	private static FileManagerView fileManagerView_;
	private static FileManagerModel fileManagerModel_;
	
	/**
	 * Class constructor.
	 * @param fileManagerView the associated view.
	 * @param fileManagerModel the associated model.
	 */
	public FileManagerController(FileManagerView fileManagerView, FileManagerModel fileManagerModel) {
		this.fileManagerView_ = fileManagerView;
		this.fileManagerModel_ = fileManagerModel;

		// Tells the view that we add the controller as an action listener for some components.
		this.fileManagerView_.registerUpdateRootListener(new UpdateRootListener());
		this.fileManagerView_.registerUpdateDropBoxListener(new UpdateDropBoxListener());
		this.fileManagerView_.registerClearListener(new ClearListener());
		this.fileManagerView_.registerAutoRunListener(new AutoRunListener());
		this.initializeRoot();
	}

	public static void initializeRoot(){

		NodeTreeHelper converter = new NodeTreeHelper();
		fileManagerView_.scrollPanelFileManager_.setViewportView(TreeSingleton.getInstance().getTree());
		TreeSingleton.getInstance().getTree().repaint();
		TreeSingleton.getInstance().getTree().revalidate();

		TreeSingleton.getInstance().getTree().getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		TreeSingleton.getInstance().getTree().addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) TreeSingleton.getInstance().getTree().getLastSelectedPathComponent();

				// If nothing is selected.
				if (node == null) {
					fileManagerModel_.setNodeSelected(null);
				} else {
					Object nodeInfo = node.getUserObject();
					TreeSingleton.getInstance().setCurrentPath(NodeTreeHelper.getAllPath(node));
					fileManagerModel_.updateSelectedNode(node);

				}
			}
		});


	}

	/**
	 * Scans the commands directory for classes and load the commands.
	 */
	public void updateCommands() {
		try {
			int gridy = 0;

			// Empty the commands panel and model data.
			fileManagerView_.panelCommands_.removeAll();
			fileManagerModel_.clearButtonMap();
			fileManagerModel_.clearTextFieldMap();

			String filePath = new File("").getAbsolutePath().concat("/src/commands/");						
			File commandsDir = new File(filePath);

			File[] commandsFiles = commandsDir.listFiles();
			for (File file : commandsFiles) {
				if ((file.isFile()) && (!file.getName().contains("ICommand.java")) && (file.getName().endsWith(".java"))) {
					// Add command button.
					GridBagConstraints gbc = new GridBagConstraints();
					gbc.anchor = GridBagConstraints.FIRST_LINE_START;
					gbc.gridy = gridy;
					gbc.gridx = 0;
					gbc.insets = new Insets(2,2,2,2);

					String name = file.getName().replace(".java", "");
					JButton button = new JButton(name);
					button.setName(name);
					button.setEnabled(false);
					button.addActionListener(new CommandButtonListener());
					fileManagerView_.panelCommands_.add(button, gbc);

					// Add the text field.
					JTextField textField = new JTextField();
					textField.setPreferredSize(new Dimension(150, 27));
					gbc.gridx = 1;
					fileManagerView_.panelCommands_.add(textField, gbc);

					//Add the button and text field to the maps.
					fileManagerModel_.addComponentToButtonMap(name, button);
					fileManagerModel_.addComponentToTextFieldMap(name, textField);

					gridy++;
				}
			}

			// Add filler to the GridBagLayout so that elements are pushed top left.
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridy = gridy;
			gbc.gridx = 0;
			gbc.weightx = 1;
			gbc.weighty = 1;
			JPanel filler = new JPanel();
			fileManagerView_.panelCommands_.add(filler, gbc);

			fileManagerView_.panelCommands_.repaint();
			fileManagerView_.panelCommands_.revalidate();
		}
		catch (NullPointerException e) {
			// The commands directory is invalid.
			System.out.println("The /commands directory is empty.");
		}
	}

	/**
	 * Action listener for the update root button.
	 */
	class UpdateRootListener implements ActionListener {
		/**
		 * This method performs an action event
		 * @param e the action event to be performed
		 */
		public void actionPerformed(ActionEvent e) {
			TreeSingleton.getInstance().setLocal(true);
			NodeTreeHelper.collapseAll(TreeSingleton.getInstance().getTree());
		}
	}
	
	/**
	 * Action listener for the update dropbox button.
	 */
	class UpdateDropBoxListener implements ActionListener {
		/**
		 * This method performs an action event
		 * @param e the action event to be performed
		 */
		public void actionPerformed(ActionEvent e) {
			NodeTreeHelper.collapseAll(TreeSingleton.getInstance().getTree());			
			
			TreeSingleton.getInstance().setLocal(false);
		}
	}

	/**
	 * Action listener for the clear button.
	 */
	class ClearListener implements ActionListener {

		/**
		 * This method performs an action event
		 * @param e the action event to be performed
		 */
		public void actionPerformed(ActionEvent e) {
			fileManagerModel_.clearCommandsResult();
		}
	}

	/**
	 * Action listener for the autorun checkbox.
	 */
	class AutoRunListener implements ActionListener {
		/**
		 * This method performs an action event
		 * @param e the action event to be performed
		 */
		public void actionPerformed(ActionEvent e) {
			// Sets the model attribute.
			fileManagerModel_.setAutoRun(((JCheckBox) e.getSource()).isSelected());
		}
	}

	/**
	 * Action listener for the command buttons.
	 */
	class CommandButtonListener implements ActionListener {
		/**
		 * This method performs an action event
		 * @param e the action event to be performed
		 */
		public void actionPerformed(ActionEvent e) {
			String commandName = ((JButton) e.getSource()).getName();
			String commandResult = fileManagerModel_.runCommand(commandName);
			fileManagerModel_.getTextFieldComponent(commandName).setText(commandResult);
		}
	}
}