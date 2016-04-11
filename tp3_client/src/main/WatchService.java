

package main;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;

import mvc.FileManagerController;

/**
 * Notifies its controller in case of file changes.
 * Code largely taken from: /http://www.codejava.net/java-se/file-io/file-change-notification-example-with-watch-service-api
 */
public class WatchService implements Runnable {
	
	private FileManagerController fileManagerController_;
	
	/**
	 * Constructor method
	 * @param fileManagerController the Controller to be notified in case of file changes.
	 */
	public WatchService(FileManagerController fileManagerController) {
		fileManagerController_ = fileManagerController;
	}
	
	/**
	 * Registers the /src/commands/ directory to the watch service. 
	 */
	public void run() {
		try {
			java.nio.file.WatchService watcher = FileSystems.getDefault().newWatchService();
			
			String filePath = new File("").getAbsolutePath().concat("/src/commands/");		
			
			Path dir = Paths.get(filePath);
			dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

			while (true) {
			WatchKey key;
			try {
			key = watcher.take();
			} catch (InterruptedException ex) {
			return;
			}

			for (WatchEvent<?> event : key.pollEvents()) {
			WatchEvent.Kind<?> kind = event.kind();

			@SuppressWarnings("unchecked")
			WatchEvent<Path> ev = (WatchEvent<Path>) event;
			Path fileName = ev.context();

			fileManagerController_.updateCommands();
			
			}

			boolean valid = key.reset();
			if (!valid) {
			break;
			}
			}

			} catch (IOException ex) {
			System.err.println(ex);
			}
	}
}