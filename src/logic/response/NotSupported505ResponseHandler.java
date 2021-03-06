/*
 * NotSupported505ResponseHandler.java
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
 
package logic.response;

import java.util.HashMap;

import protocol.Protocol;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class NotSupported505ResponseHandler extends AbstractHTTPResponse {

	/**
	 * Creates a {@link HttpResponse} object for sending version not supported response.
	 * 
	 * @param connection Supported values are {@link Protocol#OPEN} and {@link Protocol#CLOSE}.
	 * @return A {@link HttpResponse} object represent 505 status.
	 */
	public HttpResponse handleResponse(String connection) {

		HttpResponse response = new HttpResponse(Protocol.VERSION, Protocol.NOT_SUPPORTED_CODE, 
				Protocol.NOT_SUPPORTED_TEXT, new HashMap<String, String>(), null);
		
		// Lets fill up the header fields with more information
		fillGeneralHeader(response, connection);
		
		return null;
	}
	
}
