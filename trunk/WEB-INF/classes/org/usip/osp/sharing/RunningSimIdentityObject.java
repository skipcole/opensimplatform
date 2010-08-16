package org.usip.osp.sharing;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.*;
import org.usip.osp.networking.SessionObjectBase;

/**
 * This class represents a unique identifier for a running simulation
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
@Entity
@Proxy(lazy = false)
public class RunningSimIdentityObject {
	
	RunningSimIdentityObject(){
		
	}
	
	private String baseUrl = "";
	private String schema = "";
	private String dateEnabled = "";
	private String rsName = "";
	private String rndNumber = "";
	
	public setUniqueIdentifier(){
		SessionObjectBase.getBaseSimURL();
	}
	
	/** Name of this Simulation. */
	private String simulationName = ""; //$NON-NLS-1$

	/** Version of this Simulation. */
	private String version = ""; //$NON-NLS-1$

	/** Version of the software this simulation was made with. */
	private String softwareVersion = ""; //$NON-NLS-1$
	
	RunningSimIdentityObject(RunningSimulation rs, Simulation sim){
		
		
		this.setSimulationName(sim.getSimulationName());
		this.setSoftwareVersion(sim.getSoftwareVersion());
		this.setVersion(sim.getVersion());
		this.setSoftwareVersion(sim.getSoftwareVersion());
	}

	public String getSimulationName() {
		return simulationName;
	}

	public void setSimulationName(String simulationName) {
		this.simulationName = simulationName;
	}

	public String getVersion() {
		return version;
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
