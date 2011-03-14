package org.usip.osp.sharing;

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
 */
public interface ImportedExperienceObject {

	public boolean isImportedRecord();

	public void setImportedRecord(boolean importedRecord);
	
	public Long getTransitId();

	public void setTransitId(Long transit_id);
	
}
