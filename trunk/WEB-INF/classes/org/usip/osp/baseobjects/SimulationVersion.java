package org.usip.osp.baseobjects;

/**
 * This class contains only version information on the simulation and the software underwhich it
 * was created. The idea is that the import process will begin by reading this, and since this NEVER
 * changes, the import process will proceed long enough to alert the user of potential problems -
 * such as trying to import a simulation on a system that is newer than the one it was created on, 
 * or older than the one it was created on and not being able to find the file containing the information
 * necessary to update it.
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
public class SimulationVersion {
	
	public SimulationVersion(){
		
	}
	
	private Long transitId;
	
	public Long getTransitId() {
		return transitId;
	}

	public void setTransitId(Long transitId) {
		this.transitId = transitId;
	}

	/** Name of this Simulation. */
	private String name = ""; //$NON-NLS-1$

	/** Version of this Simulation. */
	private String version = ""; //$NON-NLS-1$

	/** Version of the software this simulation was made with. */
	private String softwareVersion = ""; //$NON-NLS-1$
	
	/** Not to put too fine a point on it, this is an unused field. But since the SimBase is the first thing read on import, and since
	 * changes to its structure may cause major compatibility issues between versions, we are including
	 * it here so future programmers can leverage it if necessary. */
	private String finePoints = "";

	public String getFinePoints() {
		return finePoints;
	}

	public void setFinePoints(String finePoints) {
		this.finePoints = finePoints;
	}

	public String getSimulationName() {
		return this.name;
	}

	public void setSimulationName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}
}
