package com.tp3_server.file_system;

/*
 * Class that acts as an interface for the Composite and Leaf, represents a
 * tree structure.
 */
public abstract class Component {
	// Name of the component.
	String path_;
	
	// Google Drive id.
	String driveId_;
		
	/*
	 * Returns the name of the component.
	 */
	public String getPath() {
		return path_;
	}
	
	/*
	 * Returns the name of the component.
	 */
	public String getDriveId() {
		return driveId_;
	}
	
	/*
	 * Adds a new component to the array of components.
	 */
	public void addComponent(Component c) {
		throw new UnsupportedOperationException();
	}
}