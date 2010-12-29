package org.usip.osp.baseobjects;

/**
 * Objects implement this class to be able to be exported and imported to XML. The key feature of this
 * is that all of the objects are assigned transit ids which are their unique ids when exported. When they 
 * are imported on the new system, they will all have a different set of ids, but the transit ids
 * will be used to reconstruct their relationships. 
 * For example, Conversation 1 may exist in Sim 1. On export their transit ids will be set to their 
 * unique ids, and their ids will be set to null. On import, Sim 1, may become 'Sim 2' (if their already
 * exists 1 simulation on the system it is being imported onto) but Conversation 1 will still get pointed
 * correctly to it since the relationship information will be read out of the transit id.
 *
 */
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
public interface ExportableObject {

	public Long getTransitId();

	public void setTransitId(Long transit_id);
	
}
