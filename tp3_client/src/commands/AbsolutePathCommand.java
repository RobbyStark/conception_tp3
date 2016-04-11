


package commands;

import java.io.File;

import treeManager.TreeSingleton;

/**
 * This commands returns the absolute path of a file or directory.
 */
public class AbsolutePathCommand implements ICommand {

	
	@Override
	public String run(String file) {
		return file;
	}

	@Override
	public boolean getSupportFolder() {
		return true;
	}

	@Override
	public boolean getSupportFile() {
		return true;
	}
}