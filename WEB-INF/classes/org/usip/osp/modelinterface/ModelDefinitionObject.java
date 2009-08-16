package org.usip.osp.modelinterface;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.USIP_OSP_Properties;
import org.usip.osp.networking.FileIO;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.log4j.*;

/**
 * This class holds the important definition information for a model that has been added to the system.
 *
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
	 * Reads the model definition objects from xml files, but does not save them to the database.
	 * 
	 * @param schema
	 * @return Returns a string indicating success, or not.
	 * 
	 */
	public static List screenModelsFromXMLFiles(String schema) {

		ArrayList returnList = new ArrayList();
		
		// The set of base simulation sections are read out of
		// XML files stored in the simulation_section_information directory.

		String fileLocation = USIP_OSP_Properties.getValue("model_dir"); //$NON-NLS-1$

		File locDir = new File(fileLocation);

		if (locDir == null) {
			Logger.getRootLogger().debug("Problem finding files at " + fileLocation); //$NON-NLS-1$
			return returnList;
		} else {

			File files[] = locDir.listFiles();

			if (files == null) {
				Logger.getRootLogger().debug("Problem finding files at " + fileLocation); //$NON-NLS-1$
				return returnList;
			} else {
				for (int ii = 0; ii < files.length; ii++) {

					String fName = files[ii].getName();

					if (fName.endsWith(".xml")) { //$NON-NLS-1$

						try {
							String fullFileLoc = fileLocation + fName;
							returnList.add(ModelDefinitionObject.readAheadXML(schema, files[ii], fullFileLoc));
							
						} catch (Exception e) {
							Logger.getRootLogger().debug("problem reading in file " + fName); //$NON-NLS-1$
							Logger.getRootLogger().debug(e.getMessage());
						}
					}

				}
			}

			return returnList;
		} // end of if found files.
	} // end of method 
	
	/**
	 * Returns an object from an xml file without saving it.
	 * @param schema
	 * @param thisFile
	 * @param customLibName
	 * @return
	 */
	public static Object readAheadXML(String schema, File thisFile, String fullFileLoc) {

		String fullModel = FileIO.getFileContents(thisFile);

		Object bRead = unpackageXML(fullModel);
		
		ModelDefinitionObject mdo = (ModelDefinitionObject) bRead;
		
		// Using the directory field temporarily just to pass back location on where the file read is.
		mdo.setFilelocation(fullFileLoc);
		
		return mdo;
	}
	
	/**
	 * 
	 * @param xmlString
	 * @return
	 */
	public static ModelDefinitionObject unpackageXML(String xmlString) {

		XStream xstream = new XStream(new DomDriver());
		xstream.alias("mdo", ModelDefinitionObject.class); //$NON-NLS-1$

		return (ModelDefinitionObject) xstream.fromXML(xmlString);
	}
	
	/**
	 * Creates the 
	 * @param request
	 * @return
	 */
	public static ModelDefinitionObject generateMDOforXML(HttpServletRequest request){
		
		ModelDefinitionObject mdo = new ModelDefinitionObject();
		
		mdo.setModelName(cleanNulls(request.getParameter("model_name"))); //$NON-NLS-1$

		return mdo;
		
	}
	
	/**
	 * Turns nulls into empty strings.
	 * @param input
	 * @return
	 */
	public static String cleanNulls(String input){
		if (input == null){
			return ""; //$NON-NLS-1$
		} else {
			return input;
		}
	}
	
	/**
	 * Returns all base sim sections.
	 * 
	 * @param schema
	 * @return
	 */
	public static List<ModelDefinitionObject> getAll(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<ModelDefinitionObject> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from ModelDefinitionObject").list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if (returnList == null) {
			returnList = new ArrayList<ModelDefinitionObject>();
		}

		Collections.sort(returnList);

		return returnList;
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

		String queryString = "from ModelDefinitionObject where creatingOrganization = '" + creatingOrganization + "' " //$NON-NLS-1$ //$NON-NLS-2$
				+ "AND modelName = '" + modelName + "' AND modelVersion = '" + modelVersion + "'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<ModelDefinitionObject> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(queryString).list();

		if ((returnList != null) && (returnList.size() > 0)){
			mdo = returnList.get(0);
			return mdo;
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		return mdo;
	}
	
}
