package org.usip.osp.baseobjects;

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
}
