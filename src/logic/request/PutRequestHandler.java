/*
 * PutRequestHandler.java
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

package logic.request;

import java.io.File;

import logic.action.AppendActionHandler;
import logic.action.CreateActionHandler;
import logic.action.RequestActionProcessor;
import logic.response.HttpResponse;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class PutRequestHandler extends AbstractHTTPRequest {

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.request.IHTTPRequest#handleRequest(java.io.File,
	 * java.lang.String)
	 */
	@Override
	public HttpResponse handleRequest(File file, String content) {
		RequestActionProcessor requestProcessor = new RequestActionProcessor();
		if (file.exists()) {
			requestProcessor.addHandler(new AppendActionHandler());
			return requestProcessor.getResponse(file, content);
		} else {
			requestProcessor.addHandler(new CreateActionHandler());
			return requestProcessor.getResponse(file, content);

		}
	}

}
