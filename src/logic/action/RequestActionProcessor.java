/*
 * RequestActionProcessor.java
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

import java.io.File;
import java.util.ArrayList;

import logic.response.HttpResponse;

/**
 * The class creates a chain of classes for the Chain of Responsibility pattern
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class RequestActionProcessor {

	private ArrayList<IRequestAction> actionList;

	public RequestActionProcessor() {
		this.actionList = new ArrayList<>();
	}

	// Add the handler to the list, and set the previous handler's nextAction to
	// the new handler
	public void addHandler(IRequestAction handler) {
		if (this.actionList.size() > 0) {
			this.actionList.get(this.actionList.size() - 1).setNext(handler);
		}
		this.actionList.add(handler);
	}

	public HttpResponse getResponse(File file, String content) {
		return this.actionList.get(0).performAction(file, content);
	}

}
