package org.usip.osp.networking;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.usip.osp.baseobjects.USIP_OSP_Properties;
import org.apache.log4j.*;

/**
 * Utility class that provides static methods to handle file input and output.
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
public class FileIO {

	protected static String archives_dir = "";
	private static String actor_image_dir = ""; //$NON-NLS-1$
	private static String base_section_web_dir = ""; //$NON-NLS-1$
	private static String model_dir = ""; //$NON-NLS-1$
	private static String base_web_dir = ""; //$NON-NLS-1$
	protected static String packaged_sim_dir = ""; //$NON-NLS-1$
	private static String sim_image_dir = ""; //$NON-NLS-1$

	static {
		
		base_web_dir = USIP_OSP_Properties.getValue("base_web_dir"); //$NON-NLS-1$
		
		archives_dir = base_web_dir + "simulation_admin" + File.separator + "database_archives" + File.separator;
		base_section_web_dir = base_web_dir + "simulation_section_information" + File.separator;
		model_dir = base_web_dir + "simulation_model_information" + File.separator;
		actor_image_dir = base_web_dir + "osp_core" + File.separator + "images" + File.separator + "actors" + File.separator;
		packaged_sim_dir = base_web_dir + "simulation_sharing" + File.separator + "packaged_simulations" + File.separator;
		sim_image_dir = base_web_dir + "simulation" + File.separator + "images" + File.separator;

	}

	/**
	 * 
	 * @param saveType
	 * @param fileName
	 * @param fileData
	 */
	public static void saveImageFile(String saveType, String fileName, File fileData) {

		String saveDir = ""; //$NON-NLS-1$

		if (saveType.equalsIgnoreCase("actorImage")) { //$NON-NLS-1$
			saveDir = actor_image_dir;
		} else if (saveType.equalsIgnoreCase("simImage")) { //$NON-NLS-1$
			Logger.getRootLogger().debug("saving file " + fileName + " to " + sim_image_dir); //$NON-NLS-1$ //$NON-NLS-2$
			saveDir = sim_image_dir;
		} else {
			Logger.getRootLogger().debug("Warning. Don't know where to save " + fileName); //$NON-NLS-1$
		}

		try {
			File outFile = new File(saveDir + fileName);

			byte[] readData = new byte[1024];
			FileInputStream fis = new FileInputStream(fileData);

			FileOutputStream fos = new FileOutputStream(outFile);
			int i = fis.read(readData);

			while (i != -1) {
				fos.write(readData, 0, i);
				i = fis.read(readData);
			}
			fis.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getBase_web_dir() {
		return base_web_dir;
	}

	public static void setBase_web_dir(String base_web_dir) {
		FileIO.base_web_dir = base_web_dir;
	}

	public static String getModel_dir() {
		return model_dir;
	}

	public static void setModel_dir(String model_dir) {
		FileIO.model_dir = model_dir;
	}

	public static String getActor_image_dir() {
		return actor_image_dir;
	}

	public static void setActor_image_dir(String actor_image_dir) {
		FileIO.actor_image_dir = actor_image_dir;
	}

	public static String getSim_image_dir() {
		return sim_image_dir;
	}

	public static void setSim_image_dir(String sim_image_dir) {
		FileIO.sim_image_dir = sim_image_dir;
	}

	public static String getBase_section_web_dir() {
		return base_section_web_dir;
	}

	public static void setBase_section_web_dir(String base_section_web_dir) {
		FileIO.base_section_web_dir = base_section_web_dir;
	}

	/**
	 * Saves an archive of the users in this schema.
	 * 
	 * @param fileContents
	 * @param fileName
	 * @return
	 */
	public static boolean saveUserArchiveXMLFile(String fileContents, String fileName){
		try {
			File outFile = new File(archives_dir + fileName);

			FileWriter outFW = new FileWriter(outFile);

			outFW.write(fileContents);

			outFW.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	/**
	 * 
	 * @param fileContents
	 * @param fileName
	 * @return
	 */
	public static boolean saveSimulationXMLFile(String fileContents, String fileName) {

		try {
			File outFile = new File(packaged_sim_dir + fileName);

			FileWriter outFW = new FileWriter(outFile);

			outFW.write(fileContents);

			outFW.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static List getListOfSavedSims() {
		
		return getListOfFiles(packaged_sim_dir);
		
	}

	public static List getListOfUserArchives() {
		
		return getListOfFiles(archives_dir);
		
	}
	
	/**
	 * 
	 * @return
	 */
	public static List getListOfFiles(String fileLocation) {
		
		File locDir = new File(fileLocation);

		ArrayList returnList = new ArrayList();

		if (locDir == null) {
			Logger.getRootLogger().debug("Problem finding files at " + packaged_sim_dir); //$NON-NLS-1$
		} else {

			File files[] = locDir.listFiles();

			if (files == null) {
				Logger.getRootLogger().debug("Problem finding files at " + packaged_sim_dir); //$NON-NLS-1$
			} else {
				for (int ii = 0; ii < files.length; ii++) {

					String fName = files[ii].getName();

					if (fName.endsWith(".xml")) { //$NON-NLS-1$

						returnList.add(fName);
					}
				}
			}
		}

		return returnList;

	}


	/**
	 * Gets the contents of a file (such as an xml file) as a string.
	 * 
	 * @param thisFile
	 * @return
	 */
	public static String getFileContents(File thisFile) {

		StringBuilder tempBuffer = new StringBuilder();
		try {

			BufferedReader br = new BufferedReader(new FileReader(thisFile));

			String daLine = br.readLine();

			while (daLine != null) {
				tempBuffer.append(daLine);
				daLine = br.readLine();
			}

			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new String(tempBuffer);
	}
	
	public static String getPartialFileContents(File thisFile, String endString) {

		StringBuilder tempBuffer = new StringBuilder();
		try {

			BufferedReader br = new BufferedReader(new FileReader(thisFile));

			String daLine = br.readLine();

			boolean continueOn = true;
			
			while ((daLine != null) && (continueOn)) {
				tempBuffer.append(daLine);
				daLine = br.readLine();
				
				if (daLine.indexOf(endString) != -1){
					tempBuffer.append(daLine);
					continueOn = false;
				}
			}

			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new String(tempBuffer);
	}

}
