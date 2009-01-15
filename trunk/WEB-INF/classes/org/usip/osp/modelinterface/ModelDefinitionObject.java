package org.usip.osp.modelinterface;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;

/**
 * This class holds the important definition information for a model that has been added to the system.
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
@Entity
@Table(name = "MODEL_DEFINITIONS")
@Proxy(lazy = false)
public class ModelDefinitionObject {
	
	public static final int RUN_LOCAL = 1;
	public static final int RUN_REMOTE = 2;
	
	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;
	
	private String modelName = "";
	
	private String modelVersion = "";
	
	private int runningLocation = 0;
	
	private String controllerClassName;
	
	private String controllerPackageName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getModelVersion() {
		return modelVersion;
	}

	public void setModelVersion(String modelVersion) {
		this.modelVersion = modelVersion;
	}

	public int getRunningLocation() {
		return runningLocation;
	}

	public void setRunningLocation(int runningLocation) {
		this.runningLocation = runningLocation;
	}

	public String getControllerClassName() {
		return controllerClassName;
	}

	public void setControllerClassName(String controllerClassName) {
		this.controllerClassName = controllerClassName;
	}

	public String getControllerPackageName() {
		return controllerPackageName;
	}

	public void setControllerPackageName(String controllerPackageName) {
		this.controllerPackageName = controllerPackageName;
	}
	
	
}
