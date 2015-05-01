/*
 * ConnectionHandler.java
 * Oct 7, 2012
 *
 * Simple Web Server (SWS) for CSSE 477
 * 
 * Copyright (C) 2012 Chandan Raj Rupakheti
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
 */
 
package server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import protocol.Protocol;
import logic.request.HttpRequest;
import logic.response.BadRequest400ResponseHandler;
import logic.response.HttpResponse;

/**
 * This class is responsible for handling a incoming request
 * by creating a {@link HttpRequest} object and sending the appropriate
 * response be creating a {@link HttpResponse} object. It implements
 * {@link Runnable} to be used in multi-threaded environment.
 * 
 * @author Chandan R. Rupakheti (rupakhet@rose-hulman.edu)
 */
public class ConnectionHandler implements Runnable {
	private Server server;
	private Socket socket;
	
	public ConnectionHandler(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
	}
	
	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}


	/**
	 * The entry point for connection handler. It first parses
	 * incoming request and creates a {@link HttpRequest} object,
	 * then it creates an appropriate {@link HttpResponse} object
	 * and sends the response back to the client (web browser).
	 */
	public void run() {
		// Get the start time
		long start = System.currentTimeMillis();
		
		InputStream inStream = null;
		OutputStream outStream = null;
		
		try {
			inStream = this.socket.getInputStream();
			outStream = this.socket.getOutputStream();
		}
		catch(Exception e) {
			// Cannot do anything if we have exception reading input or output stream
			// May be have text to log this for further analysis?
			e.printStackTrace();
			
			// Increment number of connections by 1
			incrementOnFailure(start);
			return;
		}
		
		
		

//		// TODO: So far response could be null for protocol version mismatch.
//		// So this is a temporary patch for that problem and should be removed
//		// after a response object is created for protocol version mismatch.
//		if(response == null) {
//			response = HttpResponseFactory.create400BadRequest(Protocol.CLOSE);
//		}
		URLParser parser = new URLParser(inStream, this);
		HttpResponse response = parser.getResponse();
		if (response == null){
			response = new BadRequest400ResponseHandler().handleResponse(Protocol.CLOSE);
		}
		try{
			// Write response and we are all done so close the socket
			response.write(outStream);
			System.out.println(response);
			socket.close();
		}
		catch(Exception e){
			// We will ignore this exception
			e.printStackTrace();
		} 
		
		incrementOnFailure(start);
	}

	public void incrementOnFailure(long start) {
		server.incrementConnections(1);
		// Get the end time
		long end = System.currentTimeMillis();
		this.server.incrementServiceTime(end-start);
	}
	
	public Server getServer(){
		return this.server;
	}
}
