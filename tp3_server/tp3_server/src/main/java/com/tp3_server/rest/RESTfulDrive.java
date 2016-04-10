package com.tp3_server.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.dropbox.core.DbxException;
import com.dropbox.core.v1.DbxClientV1;
import com.dropbox.core.v1.DbxEntry;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.gson.Gson;
 
@Path("/drive")
public class RESTfulDrive {
	
	// Objects for handling HTTP transport and JSON formatting of API calls
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	private static final String CLIENT_ID =
			"...";
	private static final String CLIENT_SECRET = "...";
	private static final String REFRESH_TOKEN =
			"...";
	
	@GET
	@Path("list")
	@Produces("text/html")
	/*
	 * Method that lists the files and folders contained in Drive.
	 * Returns the file system in JSON format.
	 */
	public Response list(@QueryParam("id") String id)
	{
		ArrayList<Node> nodes = new ArrayList<Node>();
		
		try {
			Drive drive = authentication();
			
			FileList files = drive.files().list().setQ("'" + id +
					"' in parents and trashed = false").execute();
			List<File> result = new ArrayList<File>();
			result.addAll(files.getFiles());
			
			// Iterates over all files and folders contained.
			for (File file : result) {
				nodes.add(new Node(
						file.getName(),
						file.getMimeType().equals("application/vnd.google-apps.folder"),
						file.getId()));
			}
		} catch (IOException e) {
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
	/*
	 * Method that returns the name of the file or folder specified.
	 */
	public Response name(@QueryParam("id") String id)
	{
		String name = "";
		try {
			Drive drive = authentication();
			
			File file = drive.files().get(id).execute();
			name = file.getName();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(name).build();
	}
	
	@GET
	@Path("absolutePath")
	@Produces("text/html")
	/*
	 * Method that returns the absolutePath of the file or folder specified.
	 */
	public Response absolutePath(@QueryParam("id") String id)
	{
		String name = "";
		try {
			Drive drive = authentication();
			
			File file = drive.files().get(id).execute();
			//name = file.
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(name).build();
	}
	
	/*
	 * Method to authenticate the user on Drive.
	 */
	private Drive authentication() throws IOException {
		// Request a new access token using the refresh token.
		GoogleCredential credential = new GoogleCredential.Builder()
				.setTransport(HTTP_TRANSPORT)
				.setJsonFactory(JSON_FACTORY)
				.setClientSecrets(CLIENT_ID, CLIENT_SECRET).build()
				.setFromTokenResponse(new TokenResponse().setRefreshToken(REFRESH_TOKEN));
		credential.refreshToken();
		return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName("tp3").build();
	}
	
	/*
	 * Class that defines either a file or directory.
	 */
	private class Node {
		private String Name;
		private boolean IsDirectory;
		private String Id;
		
		public Node(String name, boolean isDirectory, String id) {
			Name = name;
			IsDirectory = isDirectory;
			Id = id;
		}
	}
}