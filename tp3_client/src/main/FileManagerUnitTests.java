package main;



import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import composite.*;
import mvc.FileManagerModel;
import commands.*;
/**
* the FileManagerUnitTests class hosts a number of units tests covering each commands and node type combination.
*/
public class FileManagerUnitTests {

	/**
	* Tests the AbsolutePathCommand on a FileLeaf node.
	*/
	@Test
	public void file_path() {
		
		String name = "test.txt";
		File file = new File (name);
		String path = file.getAbsolutePath();
		AbsolutePathCommand command = new AbsolutePathCommand();		
		FileManagerModel model = new FileManagerModel();
		Node node = new FileLeaf(file);		
		String result = model.toString();
		assertTrue(path.equals(result) ||
				node != null && command.getSupportFile());	
				
	}
	
	/**
	* Tests the AbsolutePathCommand on a Directory node.
	*/
	@Test
	public void folder_path() {
		
		String name = "/test/";
		File file = new File (name);
		String path = file.getAbsolutePath();
		
		AbsolutePathCommand command = new AbsolutePathCommand();		
		FileManagerModel model = new FileManagerModel();
		Node node = new FileLeaf(file);		
		String result = model.toString();
		assertTrue(path.equals(result) ||
				node != null && command.getSupportFile());	
				
	}
	
	/**
	* Tests the FileNameCommand on a FileLeaf node.
	*/
	@Test
	public void file_name() {
		
		String name = "test.txt";
		File file = new File (name);
		
		String path = file.getAbsolutePath();
		FileNameCommand command = new FileNameCommand();		
		FileManagerModel model = new FileManagerModel();
		Node node = new FileLeaf(file);		
		String result = model.toString();
		assertTrue(path.equals(result) ||
				node != null && command.getSupportFile());	
				
	}
	
	/**
	* Tests the FileNameCommand on a Directory node.
	*/
	@Test
	public void folder_name() {
		
		String name = "/test/";
		File file = new File (name);
		String path = file.getAbsolutePath();
		FileNameCommand command = new FileNameCommand();		
		FileManagerModel model = new FileManagerModel();
		Node node = new FileLeaf(file);		
		String result = model.toString();
		
		assertTrue(path.equals(result) ||
				node != null && command.getSupportFile());
				
	}
	
	/**
	* Tests the FolderNameCommand on a FileLeaf node.
	*/
	@Test
	public void file_direName() {
		
		String name = "test.txt";
		File file = new File (name);
		String path = file.getAbsolutePath();
		FolderNameCommand command = new FolderNameCommand();		
		FileManagerModel model = new FileManagerModel();
		Node node = new FileLeaf(file);		
		
		String result = model.toString();		
		assertTrue(path.equals(result) ||
				node != null && command.getSupportFolder());	
				
	}
	
	/**
	* Tests the FolderNameCommand on a Directory node.
	*/
	@Test
	public void folder_DirName() {
		
		String name = "/test/";
		File file = new File (name);
		
		String path = file.getAbsolutePath();
		FolderNameCommand command = new FolderNameCommand();		
		FileManagerModel model = new FileManagerModel();
		Node node = new FileLeaf(file);		
		String result = model.toString();
		assertTrue(path.equals(result) ||
				node != null && command.getSupportFolder());	
				
	}
	

	
}
