
package main;

import java.awt.EventQueue;

import mvc.*;

/**
* This program displays a GUI that allows to run commands on files and folders.
* The FileManager class constitutes the application's entry point. Initializes the model, controller and view.
* @author  Francis De LaSalle et Matthieu Boglioni
* @version 1.0
* @since   2016-02-03
*/
public class FileManager {

	/**
	 * Application's entry point.
	 * @param args args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileManagerView fileManagerView = new FileManagerView();
					FileManagerModel fileManagerModel = new FileManagerModel();
					FileManagerController fileManagerController = new FileManagerController(fileManagerView, fileManagerModel);
					
					// First load the commands.
					fileManagerController.updateCommands();
					
					fileManagerView.frameFileManager_.setVisible(true);
					
					// Start a new thread to watch for a change in the commands directory.
					(new Thread(new WatchService(fileManagerController))).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
