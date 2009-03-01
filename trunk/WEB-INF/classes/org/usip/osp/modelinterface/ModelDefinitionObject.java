package org.usip.osp.modelinterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.baseobjects.USIP_OSP_Properties;
import org.usip.osp.networking.FileIO;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

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
	
	public static final int REMOTE_COMM_XML_OVER_HTTP = 1;
	
	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;
	
	private String modelName = "";
	
	private String modelVersion = "";
	
	private int runningLocation = 0;
	
	private int remoteCommunication = 0;
	
	private String controllerClassName;
	
	private String controllerPackageName;
	
	private String filelocation;

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

	public int getRemoteCommunication() {
		return remoteCommunication;
	}

	public void setRemoteCommunication(int remoteCommunication) {
		this.remoteCommunication = remoteCommunication;
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
	
	public String getFilelocation() {
		return filelocation;
	}

	public void setFilelocation(String filelocation) {
		this.filelocation = filelocation;
	}

	/**
	 * Reads the simulation sections from xml files, but does not save them to the database.
	 * 
	 * @param schema
	 * @return Returns a string indicating success, or not.
	 * 
	 */
	public static List screenBaseSimSectionsFromXMLFiles(String schema) {

		ArrayList returnList = new ArrayList();
		
		// The set of base simulation sections are read out of
		// XML files stored in the simulation_section_information directory.

		String fileLocation = USIP_OSP_Properties.getValue("model_dir");

		File locDir = new File(fileLocation);

		if (locDir == null) {
			System.out.println("Problem finding files at " + fileLocation);
			return returnList;
		} else {

			File files[] = locDir.listFiles();

			if (files == null) {
				System.out.println("Problem finding files at " + fileLocation);
				return returnList;
			} else {
				for (int ii = 0; ii < files.length; ii++) {

					String fName = files[ii].getName();

					if (fName.endsWith(".xml")) {

						try {
							String fullFileLoc = fileLocation + fName;
							returnList.add(ModelDefinitionObject.readAheadXML(schema, files[ii], fullFileLoc));
							
						} catch (Exception e) {
							System.out.println("problem reading in file " + fName);
							System.out.println(e.getMessage());
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

		String fullBSS = FileIO.getFileContents(thisFile);

		Object bRead = unpackageXML(fullBSS);
		
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
		xstream.alias("mdo", ModelDefinitionObject.class);

		return (ModelDefinitionObject) xstream.fromXML(xmlString);
	}
	
	/**
	 * Creates the 
	 * @param request
	 * @return
	 */
	public static ModelDefinitionObject generateMDOforXML(HttpServletRequest request){
		
		ModelDefinitionObject mdo = new ModelDefinitionObject();
		
		mdo.setModelName(cleanNulls(request.getParameter("model_name")));

		return mdo;
		
	}
	
	/**
	 * Turns nulls into empty strings.
	 * @param input
	 * @return
	 */
	public static String cleanNulls(String input){
		if (input == null){
			return "";
		} else {
			return input;
		}
	}
	
	
}
