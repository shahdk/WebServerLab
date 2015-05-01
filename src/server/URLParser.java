/*
 * URLParser.java
 * Apr 25, 2015
 *
 * Simple Web Server (SWS) for EE407/507 and CS455/555
 * 
 * Copyright (C) 2011 Chandan Raj Rupakheti, Clarkson University
 * 
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License 
 * as published by the Free Software Foundation, either 
 * version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/lgpl.html>.
 * 
 * Contact Us:
 * Chandan Raj Rupakheti (rupakhcr@clarkson.edu)
 * Department of Electrical and Computer Engineering
 * Clarkson University
 * Potsdam
 * NY 13699-5722
 * http://clarkson.edu/~rupakhcr
 */

package server;

import java.io.File;
import java.io.InputStream;

import logic.request.DeleteRequestHandler;
import logic.request.GetRequestHandler;
import logic.request.HttpRequest;
import logic.request.IHTTPRequest;
import logic.request.PostRequestHandler;
import logic.request.PutRequestHandler;
import logic.response.BadRequest400ResponseHandler;
import logic.response.HttpResponse;
import logic.response.NotSupported505ResponseHandler;
import protocol.Protocol;
import protocol.ProtocolException;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class URLParser {

	private InputStream inStream;
	private ConnectionHandler connHandler;
	private HttpResponse response;
	private HttpRequest request; 
	
	public URLParser(InputStream inStream, ConnectionHandler connHandler) {
		this.inStream = inStream;
		this.connHandler = connHandler;
		this.response = null;
		this.request = null;
	}

	public HttpResponse parseURL() {

		try {
			request = HttpRequest.read(inStream);
			System.out.println(request);
		} catch (ProtocolException pe) {
			int status = pe.getStatus();
			if (status == Protocol.BAD_REQUEST_CODE) {
				response = create400BadRequest();
			} else if(status == Protocol.NOT_SUPPORTED_CODE){
				response = create505NotSupported();
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = create400BadRequest();
		}

		if (response != null) {
			return response;
		}

		return handleRequest();
	}

	private HttpResponse handleRequest() {

		try {
			if (!request.getVersion().equalsIgnoreCase(Protocol.VERSION)) {
				response = create505NotSupported();
			} else if (request.getMethod().equalsIgnoreCase(Protocol.GET)) {
				response = createGetRequest(request);
			}  else if (request.getMethod().equalsIgnoreCase(Protocol.POST)) {
				response = createPostRequest(request);
			}  else if (request.getMethod().equalsIgnoreCase(Protocol.PUT)) {
				response = createPutRequest(request);
			}  else if (request.getMethod().equalsIgnoreCase(Protocol.DELETE)) {
				response = createDeleteRequest(request);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response; 
	}

	/**
	 * @param request
	 */
	private HttpResponse createDeleteRequest(HttpRequest request) {
		
		String uri = request.getUri();
		String rootDirectory = this.connHandler.getServer().getRootDirectory();
		
		IHTTPRequest deleteRequest = new DeleteRequestHandler();
		File file = deleteRequest.getFile(rootDirectory, uri);
		return deleteRequest.handleRequest(file, "");
	}

	/**
	 * @param request
	 */
	private HttpResponse createPutRequest(HttpRequest request) {
		String uri = request.getUri();
		String rootDirectory = this.connHandler.getServer().getRootDirectory();
		
		IHTTPRequest putRequest = new PutRequestHandler();
		File file = putRequest.getFile(rootDirectory, uri);
		return putRequest.handleRequest(file, new String(request.getBody()));
	}

	/**
	 * @param request
	 */
	private HttpResponse createPostRequest(HttpRequest request) {
		String uri = request.getUri();
		String rootDirectory = this.connHandler.getServer().getRootDirectory();
		
		IHTTPRequest postRequest = new PostRequestHandler();
		File file = postRequest.getFile(rootDirectory, uri);
		return postRequest.handleRequest(file, new String(request.getBody()));
	}

	private HttpResponse createGetRequest(HttpRequest request) {
		// Map<String, String> header = request.getHeader();
		// String date = header.get("if-modified-since");
		// String hostName = header.get("host");
		//

		String uri = request.getUri();
		String rootDirectory = this.connHandler.getServer().getRootDirectory();
		
		IHTTPRequest getRequest = new GetRequestHandler();
		File file = getRequest.getFile(rootDirectory, uri);
		return getRequest.handleRequest(file, "");
	}

	private HttpResponse create505NotSupported() {
		HttpResponse response;
		response = new NotSupported505ResponseHandler()
				.handleResponse(Protocol.CLOSE);
		return response;
	}

	private HttpResponse create400BadRequest() {
		HttpResponse response;
		response = new BadRequest400ResponseHandler()
				.handleResponse(Protocol.CLOSE);
		return response;
	}

	/**
	 * @return
	 */
	public HttpResponse getResponse() {
		return this.parseURL();
	}

}
