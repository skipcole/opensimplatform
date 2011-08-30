package org.usip.osp.baseobjects;

import java.util.List;

/**
 * This interface is being considered to be used to help get all of the sub-objects of a simulation, and
 * make copies of them - enabling a simple 'deep copy' functionality.
 *
 */
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
public interface SimulationSubObject {

	public List getAllForSimulation(String schema, Long simId);
	
	public void copyInBasicValues(Object arg);
	
}
