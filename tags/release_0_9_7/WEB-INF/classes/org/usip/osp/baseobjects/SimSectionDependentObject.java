package org.usip.osp.baseobjects;

/**
 * This interface ensures that simulation objects that are associated with a particular simulation section 
 * can be created whne the running simulation is enabled.
 */
 /* 
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
	 * Returns the id of the object created.
	 * @return
	 */
	public Long getId();
	
	/**
	 * Allows the setting of the id of the dependent objected. (Needed when packaging into XML.)
	 */
	public void setId(Long theId);
	
	/** Sets the original simulation id to be the new simulation id. */
	public void setSimId(Long theId);
	
	/**
	 * Returns the transit id of the object when moving it across databases.
	 * @return
	 */
	public Long getTransitId();

	/**
	 * Allows the setting of transit ids when moving object across databases.
	 * @param transit_id
	 */
	public void setTransitId(Long transit_id);
	
	
	public void saveMe(String schema);
	
}
