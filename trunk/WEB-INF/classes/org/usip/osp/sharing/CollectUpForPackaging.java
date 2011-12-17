package org.usip.osp.sharing;

import java.util.List;

/* 
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it
 * and/or modify it under the terms of the new BSD Style license associated with
 * this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. <BR>
 * 
 */
public interface CollectUpForPackaging {

	/** Used to get all of the objects associated with a simulation for packaging. */
	public List getAllForSimulation(String schema, Long simId);
	
}
