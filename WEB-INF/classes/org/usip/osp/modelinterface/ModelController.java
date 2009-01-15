package org.usip.osp.modelinterface;

import java.util.List;

/**
 * This abstract class is sub-classed by objects that can implement a particular model in a simulation.
 * 
 * @author Ronald "Skip" Cole<br />
 * 
 *         This file is part of the USIP Open Simulation Platform.<br>
 * 
 *         The USIP Open Simulation Platform is free software; you can
 *         redistribute it and/or modify it under the terms of the new BSD Style
 *         license associated with this distribution.<br>
 * 
 *         The USIP Open Simulation Platform is distributed WITHOUT ANY
 *         WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *         FITNESS FOR A PARTICULAR PURPOSE. <BR>
 * 
 */
public abstract class ModelController {

	/**
	 * 
	 * @param equationName Name of this equation, for example 'Calculate GDP.'
	 * @param equationFlags
	 * @param inputMetaData Classes 
	 * @param inputData
	 * @param outputMetaData
	 * @param outputData
	 * @return
	 */
	public abstract List doEquation(String equationName, List equationFlags, List inputMetaData, List inputData, List outputMetaData, List outputData);
	
}
