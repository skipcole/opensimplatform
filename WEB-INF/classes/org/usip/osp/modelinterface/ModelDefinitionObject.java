package org.usip.osp.modelinterface;

import java.util.List;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.USIP_OSP_Properties;
import org.usip.osp.baseobjects.USIP_OSP_Util;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

import org.apache.log4j.*;

/**
 * This class holds the important definition information for a model that has been 
 * added to the system.
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
@Table(name = "MODEL_DEFINITIONS")
@Proxy(lazy = false)
public class ModelDefinitionObject implements Comparable{
	
	public static final int RUN_LOCAL = 1;
	public static final int RUN_REMOTE = 2;
	
	public static final int REMOTE_COMM_XML_OVER_HTTP = 1;
	
	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;
	
	private String creatingOrganization = ""; //$NON-NLS-1$
	
	private String modelName = ""; //$NON-NLS-1$
	
	private String modelVersion = ""; //$NON-NLS-1$
	
	private String modelDescription = ""; //$NON-NLS-1$
	
	private String modelDirectory = ""; //$NON-NLS-1$
	
	private int runningLocation = 0;
	
	private int remoteCommunication = 0;
	
	private String controllerClassName;
	
	private String controllerPackageName;
	
	private String filelocation;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreatingOrganization() {
		return this.creatingOrganization;
	}

	public void setCreatingOrganization(String creatingOrganization) {
		this.creatingOrganization = creatingOrganization;
	}

	public String getModelName() {
		return this.modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getModelVersion() {
		return this.modelVersion;
	}

	public void setModelVersion(String modelVersion) {
		this.modelVersion = modelVersion;
	}

	public String getModelDescription() {
		return this.modelDescription;
	}

	public void setModelDescription(String modelDescription) {
		this.modelDescription = modelDescription;
	}

	public String getModelDirectory() {
		return this.modelDirectory;
	}

	public void setModelDirectory(String modelDirectory) {
		this.modelDirectory = modelDirectory;
	}

	public int getRunningLocation() {
		return this.runningLocation;
	}

	public void setRunningLocation(int runningLocation) {
		this.runningLocation = runningLocation;
	}

	public int getRemoteCommunication() {
		return this.remoteCommunication;
	}

	public void setRemoteCommunication(int remoteCommunication) {
		this.remoteCommunication = remoteCommunication;
	}

	public String getControllerClassName() {
		return this.controllerClassName;
	}

	public void setControllerClassName(String controllerClassName) {
		this.controllerClassName = controllerClassName;
	}

	public String getControllerPackageName() {
		return this.controllerPackageName;
	}

	public void setControllerPackageName(String controllerPackageName) {
		this.controllerPackageName = controllerPackageName;
	}
	
	public String getFilelocation() {
		return this.filelocation;
	}

	public void setFilelocation(String filelocation) {
		this.filelocation = filelocation;
	}

	
	/**
	 * Creates the 
	 * @param request
	 * @return
	 */
	public static ModelDefinitionObject generateMDOforXML(HttpServletRequest request){
		
		ModelDefinitionObject mdo = new ModelDefinitionObject();
		
		mdo.setModelName(USIP_OSP_Util.cleanNulls(request.getParameter("model_name"))); //$NON-NLS-1$

		return mdo;
		
	}
	
	@Override
	public int compareTo(Object obj) {
		ModelDefinitionObject bss = (ModelDefinitionObject) obj;
		
		String itString = bss.getModelName() + bss.getModelVersion();
		String thisString = this.getModelName() + this.getModelVersion();

		return -(itString.compareTo(thisString));
	}
	
	/**
	 * Checks to see if a model with the same creating organization, name and version 
	 * has been loaded. If so, it returns the id of the section, else it returns null.
	 * @param schema
	 * @param bss
	 * @return
	 */
	public static Long checkInstalled(String schema, ModelDefinitionObject mdo){
		
		ModelDefinitionObject correspondingBss = getByName(schema, mdo.creatingOrganization, 
				mdo.modelName, mdo.modelVersion);
		
		if (correspondingBss == null){
			return null;
		} else {
			return correspondingBss.getId();
		}
	}
	
	/**
	 * 
	 * @param schema
	 * @param creatingOrganization
	 * @param uniqueName
	 * @param version
	 * @return
	 */
	public static ModelDefinitionObject getByName(String schema, String creatingOrganization, 
			String modelName, String modelVersion) {
		ModelDefinitionObject mdo = null;

		String queryString = "from ModelDefinitionObject where creatingOrganization = :creatingOrganization " //$NON-NLS-1$ 
				+ "AND modelName = :modelName AND modelVersion = :modelVersion "; //$NON-NLS-1$

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<ModelDefinitionObject> returnList = 
			MultiSchemaHibernateUtil.getSession(schema).createQuery(queryString)
			.setString("creatingOrganization", creatingOrganization)
			.setString("modelName", modelName)
			.setString("modelVersion", modelVersion)
			.list();

		if ((returnList != null) && (returnList.size() > 0)){
			mdo = returnList.get(0);
			return mdo;
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		return mdo;
	}
	
}
