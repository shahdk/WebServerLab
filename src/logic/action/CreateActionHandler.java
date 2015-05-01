/*
 * CreateActionHandler.java
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
 
package logic.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import logic.response.BadRequest400ResponseHandler;
import logic.response.HttpResponse;
import logic.response.IHTTPResponse;
import logic.response.Ok200ResponseHandler;
import protocol.Protocol;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class CreateActionHandler extends AbstractRequestAction {

	/* (non-Javadoc)
	 * @see logic.action.IRequestAction#performAction(java.io.File, java.lang.String)
	 */
	@Override
	public HttpResponse performAction(File file, String content) {
		IHTTPResponse httpResponse = new BadRequest400ResponseHandler();
		HttpResponse response = httpResponse.handleResponse(Protocol.CLOSE);

		if (file.isDirectory()) {
			return response;
		} else {
			
			try {
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file.getAbsolutePath(), false)));
				out.println(content);
				out.close();				
				
				httpResponse = new Ok200ResponseHandler(file);
				response = httpResponse.handleResponse(Protocol.CLOSE);
				
			} catch (Exception e){
				return response;
			}
		}

		if (this.nextAction == null) {
			return response;
		} else {
			return this.nextAction.performAction(file, content);
		}
	}

}
