package com.tp3_server.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

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
import com.tp3_server.file_system.Component;
import com.tp3_server.file_system.Composite;
import com.tp3_server.file_system.Leaf;
 
@Path("/drive")
public class RESTfulDrive {
	
	// Objects for handling HTTP transport and JSON formatting of API calls
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	private static final String CLIENT_ID =
			"PUT-YOUR-DRIVE-CLIENT-ID-HERE";
	private static final String CLIENT_SECRET = "PUT-YOUR-DRIVE-CLIENT-SECRET-HERE";
	private static final String REFRESH_TOKEN =
			"PUT-YOUR-DRIVE-REFRESH-TOKEN-HERE";
	
	@GET
	@Path("list")
	@Produces("text/html")
	/*
	 * Method that lists the files and folders contained in Drive.
	 * Returns the file system in JSON format.
	 */
	public Response list(
			@QueryParam("root") String root,
			@QueryParam("driveId") String driveId)
	{
		String json = "";
		
		// Request a new Access token using the refresh token.
		GoogleCredential credential = new GoogleCredential.Builder()
				.setTransport(HTTP_TRANSPORT)
				.setJsonFactory(JSON_FACTORY)
				.setClientSecrets(CLIENT_ID, CLIENT_SECRET).build()
				.setFromTokenResponse(new TokenResponse().setRefreshToken(REFRESH_TOKEN));
		
		try {
			credential.refreshToken();
			Drive drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
					.setApplicationName("tp3").build();
			
			Component fs = new Composite("root", driveId);
			fillFileSystem(drive, fs);
        	
        	// Convert object to JSON.
			Gson gson = new Gson();
			json = gson.toJson(fs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return Response.status(200).entity(json).build();
	}
	
	/*
	 * Method that recursively find files and directories.
	 */
	private void fillFileSystem(Drive drive, Component c) throws IOException {
		FileList files = drive.files().list().setQ("'" + c.getDriveId() +
				"' in parents and trashed = false").execute();
		List<File> result = new ArrayList<File>();
		result.addAll(files.getFiles());
		
		// Iterates over all files and folders contained.
		for (File file : result) {
			if (file.getMimeType().equals("application/vnd.google-apps.folder")) {
				// Create a new composite and call again on new composite.
				Component composite = new Composite(file.getName(), file.getId());
				c.addComponent(composite);
				fillFileSystem(drive, composite);
			} else {
				// Create a new leaf.
				Component leaf = new Leaf(file.getName(), file.getId());
				c.addComponent(leaf);
			}
		}
	}
}