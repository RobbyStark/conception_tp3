package com.tp3_server.rest;

import com.dropbox.core.*;
import com.dropbox.core.v1.DbxClientV1;
import com.dropbox.core.v1.DbxEntry;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
 
@Path("/dropbox")
public class RESTfulDropbox {
	
	private static final String TOKEN =
			//"PUT-YOUR-DROPBOX-TOKEN-HERE";
			"DVf0QlNpMHYAAAAAAAAANKef7FKfeMsFMhAmnV9ZHYGQ8DIL4IN6h5itajk86VVY";
	
	@GET
	@Path("list")
	@Produces("text/html")
	/**
	 * Method that lists the files and folders contained in the path.
	 */
	public Response list(@QueryParam("path") String path)
	{
		ArrayList<Node> nodes = new ArrayList<Node>();
		
        DbxClientV1 client = authentication();
        
        try {
        	DbxEntry.WithChildren listing = client.getMetadataWithChildren(path);
    		
    		// Iterates over all files and folders contained.
    		for (DbxEntry child : listing.children) {
    			nodes.add(new Node(child.path, child.isFolder()));
    		}
		} catch (DbxException e) {
			e.printStackTrace();
		} catch (java.lang.NullPointerException e) {
			e.printStackTrace();
		}
        
        // Convert object to JSON.
		Gson gson = new Gson();
		String json = gson.toJson(nodes);
		return Response.status(200).entity(json).build();
	}
	
	@GET
	@Path("name")
	@Produces("text/html")
	/**
	 * Method that returns the name of the file or folder specified.
	 */
	public Response name(@QueryParam("path") String path)
	{
		String name = "";
        DbxClientV1 client = authentication();
        DbxEntry file;
		try {
			file = client.getMetadata(path);
			name = file.name;
		} catch (DbxException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(name).build();
	}
	
	@GET
	@Path("absolutePath")
	@Produces("text/html")
	/**
	 * Method that returns the absolutePath of the file or folder specified.
	 */
	public Response absolutePath(@QueryParam("path") String path)
	{
		String name = "";
        DbxClientV1 client = authentication();
        DbxEntry file;
		try {
			file = client.getMetadata(path);
			name = file.path;
		} catch (DbxException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(name).build();
	}
	
	/**
	 * Method to authenticate the user on Dropbox.
	 */
	private DbxClientV1 authentication() {
		 DbxRequestConfig config = new DbxRequestConfig(
				 "JavaTutorial/1.0", Locale.getDefault().toString());
        return new DbxClientV1(config, TOKEN);
	}
	
	/**
	 * Class that defines either a file or directory.
	 */
	private class Node {
		private String Path;
		private boolean IsDirectory;
		
		public Node(String path, boolean isDirectory) {
			Path = path;
			IsDirectory = isDirectory;
		}
	}
}