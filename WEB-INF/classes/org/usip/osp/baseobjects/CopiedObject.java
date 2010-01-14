package org.usip.osp.baseobjects;

import java.util.Hashtable;

/* 
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
public interface CopiedObject {

	public static Hashtable allObjects = new Hashtable ();
	
	public void setVersion(int version);
	
	public int getVersion();
	
	public void copyToNewVersion(String schema);
	
	
	
}
