
package commands;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import httpHelper.HttpRequest;
import httpHelper.HttpRequest.HttpRequestException;

/**
 * This commands returns the folder name of a node.
 */

public class FolderNameCommand implements ICommand {

	@Override
	public String run(String file) {
		String response = HttpRequest.GETnameFromRoot(file);
		
		return response;
	}

	@Override
	public boolean getSupportFolder() {
		return true;
	}

	@Override
	public boolean getSupportFile() {
		return false;
	}
}