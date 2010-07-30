package org.usip.osp.baseobjects;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;

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
@Entity
@Proxy(lazy = false)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class SimulationBase {

	/** Database id of this Simulation. */
	@Id
	@GeneratedValue
	@Column(name = "SIM_ID")
	protected Long id;

	/** Id used when objects are exported and imported moving across databases. */
	protected Long transit_id;

	public Long getTransit_id() {
		return this.transit_id;
	}

	public void setTransit_id(Long transit_id) {
		this.transit_id = transit_id;
	}
	/** Name of this Simulation. */
	@Column(name = "SIM_NAME")
	protected String name = ""; //$NON-NLS-1$

	/** Version of this Simulation. */
	@Column(name = "SIM_VERSION")
	protected String version = ""; //$NON-NLS-1$

	/** Version of the software this simulation was made with. */
	protected String softwareVersion = ""; //$NON-NLS-1$
	
	public String getSimulationName() {
		return this.name;
	}

	public void setName(String name) {
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
