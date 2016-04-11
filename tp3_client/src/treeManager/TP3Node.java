package treeManager;

/*
 * Class that defines either a file or directory.
 */
public class TP3Node {	

	private String Path;
	private boolean IsDirectory;

	public TP3Node(String path, boolean isDirectory) {
		Path = path;
		IsDirectory = isDirectory;
	}	

	public String getPath(){
		return Path;
	}

	public boolean isDirectory(){
		return IsDirectory;
	}

}
