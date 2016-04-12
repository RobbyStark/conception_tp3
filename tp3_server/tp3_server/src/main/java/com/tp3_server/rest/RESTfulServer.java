package com.tp3_server.rest;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
 
@Path("server")
public class RESTfulServer {
	
	@GET
	@Path("list")
	@Produces("text/html")
	/**
	 * Method that lists the files and folders contained in the path.
	 */
	public Response list(@QueryParam("path") String path)
	{
		ArrayList<Node> nodes = new ArrayList<Node>();
		
		try {
			// The root of the file system.
			File f = new File(path);
			
			// Iterates over all files and folders contained.
			File[] files = f.listFiles();
			for (File file : files) {
				nodes.add(new Node(file.getAbsolutePath(), file.isDirectory()));
			}
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
		File f = new File(path);
		return Response.status(200).entity(f.getName()).build();
	}
	
	@GET
	@Path("absolutePath")
	@Produces("text/html")
	/**
	 * Method that returns the absolutePath of the file or folder specified.
	 */
	public Response absolutePath(@QueryParam("path") String path)
	{
		File f = new File(path);
		return Response.status(200).entity(f.getAbsolutePath()).build();
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