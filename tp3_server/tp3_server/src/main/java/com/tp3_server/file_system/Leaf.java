package com.tp3_server.file_system;

/*
 * Class that represents a leaf (file).
 */
public class Leaf extends Component {
	/*
	 * Class constructor.
	 */
	public Leaf(String path, String driveId) {
		path_ = path;
		driveId_ = driveId;
	}
}