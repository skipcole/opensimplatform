package org.usip.osp.baseobjects;

/**
 * This class represents a non-persistent version of a Simulation Section. We ran into problems using the hibernate
 * versions of classes directly in the web application. If using this ghost is necessary, I don't know. But it
 * helped us quickly get around our issues.
 * 
 * @author Ronald "Skip" Cole<br />
 * 
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can
 * redistribute it and/or modify it under the terms of the new BSD Style license
 * associated with this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. <BR>
 * 
 */
public class SimulationSectionGhost {

	private String tabHeading = "";

	public String getTabHeading() {
		return tabHeading;
	}

	public void setTabHeading(String tabHeading) {
		this.tabHeading = tabHeading;
	}
	
}
