

package composite;

import java.io.File;

/**
 * Class that represents a leaf object FileLeaf.
 */
public class FileLeaf extends Node {
	/**
	 * Class constructor.
	 * @param file The file to be represented by the FileLeaf object.
	 */
	public FileLeaf(File file) {
		file_ = file;
	}
}