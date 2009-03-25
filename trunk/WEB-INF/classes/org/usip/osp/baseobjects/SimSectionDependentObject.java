package org.usip.osp.baseobjects;

/**
 * This interface ensures that simulation objects that are associated with a particular simulation section 
 * can be created whne the running simulation is enabled.
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
public interface SimSectionDependentObject {

	/**
	 * 
	 * @param schema
	 * @param sim_id
	 * @param bss_id
	 * @param rs_id
	 * @param templateObject
	 * @return
	 */
	public Long createRunningSimVersion(String schema, Long sim_id, Long rs_id, Object templateObject);
	
	/**
	 * Returns the class of the object.
	 * @return
	 */
	public String getObjectClass();
	
	/**
	 * Returns the id of the object created.
	 * @return
	 */
	public Long getId();
	
	/**
	 * Returns the transit id of the object when moving it across databases.
	 * @return
	 */
	public Long getTransit_id();

	/**
	 * Allows the setting of transit ids when moving object across databases.
	 * @param transit_id
	 */
	public void setTransit_id(Long transit_id);
	
}
