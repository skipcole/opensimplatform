package org.usip.osp.modelinterface;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.BaseSimSection;
import org.usip.osp.networking.FileIO;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

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
public class ModelSystemController {

	/**
	 * Reads the model definition objects from xml files, but does not save them to the 
	 * database.
	 * 
	 * @param schema
	 * @return Returns a string indicating success, or not.
	 * 
	 */
	public static List screenModelsFromXMLFiles(String schema) {

		ArrayList returnList = new ArrayList();

		String fileLocation = FileIO.getModel_dir(); //$NON-NLS-1$

		File locDir = new File(fileLocation);

		if (locDir == null) {
			Logger.getRootLogger().warn("Problem finding files at " + fileLocation); //$NON-NLS-1$
			return returnList;
		} else {

			File files[] = locDir.listFiles();

			if (files == null) {
				Logger.getRootLogger().warn("Problem finding files at " + fileLocation); //$NON-NLS-1$
				return returnList;
			} else {
				for (int ii = 0; ii < files.length; ii++) {

					String fName = files[ii].getName();

					if (fName.endsWith(".xml")) { //$NON-NLS-1$

						try {
							String fullFileLoc = fileLocation + fName;
							returnList.add(ModelSystemController.readAheadXML(schema, files[ii], fullFileLoc));
							
						} catch (Exception e) {
							Logger.getRootLogger().warn("problem reading in file " + fName); //$NON-NLS-1$
							Logger.getRootLogger().warn(e.getMessage());
						}
					}

				}
			}

			return returnList;
		} // end of if found files.
	} // end of method 

	/**
	 * Handles commands submitted on the install simulation sections page.
	 * 
	 * @param request
	 */
	public static void handleInstallModels(HttpServletRequest request, String schema) {
	
		String command = request.getParameter("command");
		String fullfileloc = request.getParameter("fullfileloc");
		String loaded_id = request.getParameter("loaded_id");
	
		if (command != null) {
			if (command.equalsIgnoreCase("Load")) {
				Logger.getRootLogger().debug(
						"Will be loading file from: " + fullfileloc);
				BaseSimSection
						.readInXMLFile(schema, new File(fullfileloc));
	
			} else if (command.equalsIgnoreCase("Reload")) {
				Logger.getRootLogger().debug(
						"Will be reloading file from: " + fullfileloc);
				BaseSimSection.reloadXMLFile(schema,
						new File(fullfileloc), new Long(loaded_id));
				// save
			} else if (command.equalsIgnoreCase("Unload")) {
				Logger.getRootLogger().debug(
						"Will be unloading bss id: " + loaded_id);
				BaseSimSection.removeBSS(schema, loaded_id);
			}
		}
	}

	/**
	 * Returns all models installed.
	 * 
	 * @param schema
	 * @return
	 */
	public static List<ModelDefinitionObject> getAllInstalledModels(String schema) {
	
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

	/**
	 * Returns an object from an xml file without saving it.
	 * @param schema
	 * @param thisFile
	 * @param customLibName
	 * @return
	 */
	public static Object readAheadXML(String schema, File thisFile, String fullFileLoc) {
	
		String fullModel = FileIO.getFileContents(thisFile);
	
		Object bRead = ModelSystemController.unpackageXML(fullModel);
		
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

}
