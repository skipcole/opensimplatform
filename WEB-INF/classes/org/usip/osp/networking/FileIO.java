package org.usip.osp.networking;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.usip.osp.baseobjects.BaseSimSection;
import org.usip.osp.baseobjects.SimulationPhase;
import org.usip.osp.baseobjects.USIP_OSP_Properties;
import org.usip.osp.baseobjects.UserAssignment;

/**
 * Utility class that provides static methods to handle file input and output.
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
public class FileIO {

	private static String actor_image_dir = "";
	private static String base_section_web_dir = "";
	private static String base_web_dir = "";
	private static String custom_section_web_dir = "";
	protected static String packaged_sim_dir = "";
	private static String sim_image_dir = "";

	static {
		base_web_dir = USIP_OSP_Properties.getValue("base_web_dir");
		base_section_web_dir = USIP_OSP_Properties.getValue("base_section_web_dir");
		custom_section_web_dir = USIP_OSP_Properties.getValue("custom_section_web_dir");
		actor_image_dir = USIP_OSP_Properties.getValue("actor_image_dir");
		packaged_sim_dir = USIP_OSP_Properties.getValue("packaged_sim_dir");
		sim_image_dir = USIP_OSP_Properties.getValue("sim_image_dir");

	}

	/**
	 * 
	 * @param saveType
	 * @param fileName
	 * @param fileData
	 */
	public static void saveImageFile(String saveType, String fileName, File fileData) {

		String saveDir = "";

		if (saveType.equalsIgnoreCase("actorImage")) {
			saveDir = actor_image_dir;
		} else if (saveType.equalsIgnoreCase("simImage")) {
			System.out.println("saving file " + fileName + " to " + sim_image_dir);
			saveDir = sim_image_dir;
		} else {
			System.out.println("Warning. Don't know where to save " + fileName);
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

	public static String getCustom_section_web_dir() {
		return custom_section_web_dir;
	}

	public static void setCustom_section_web_dir(String custom_section_web_dir) {
		FileIO.custom_section_web_dir = custom_section_web_dir;
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

	/**
	 * 
	 * @return
	 */
	public static List getListOfCustomLibraries() {
		File locDir = new File(custom_section_web_dir);

		ArrayList returnList = new ArrayList();

		if (locDir == null) {
			System.out.println("Problem finding files at " + custom_section_web_dir);
		} else {

			File files[] = locDir.listFiles();

			if (files == null) {
				System.out.println("Problem finding files at " + custom_section_web_dir);
			} else {
				for (int ii = 0; ii < files.length; ii++) {

					String fName = files[ii].getName();

					if (files[ii].isDirectory()) {

						if (!(fName.startsWith("."))) {
							returnList.add(fName);
						}
					}
				}
			}
		}

		return returnList;

	}

	/**
	 * 
	 * @return
	 */
	public static List getListOfSavedSims() {
		File locDir = new File(packaged_sim_dir);

		ArrayList returnList = new ArrayList();

		if (locDir == null) {
			System.out.println("Problem finding files at " + packaged_sim_dir);
		} else {

			File files[] = locDir.listFiles();

			if (files == null) {
				System.out.println("Problem finding files at " + packaged_sim_dir);
			} else {
				for (int ii = 0; ii < files.length; ii++) {

					String fName = files[ii].getName();

					if (fName.endsWith(".xml")) {

						returnList.add(fName);
					}
				}
			}
		}

		return returnList;

	}



	public static void main(String args[]) {


	}

	/**
	 * Gets the contents of a file (such as an xml file) as a string.
	 * 
	 * @param thisFile
	 * @return
	 */
	public static String getFileContents(File thisFile) {

		String fullString = "";
		try {
			FileReader fr = new FileReader(thisFile);
			BufferedReader br = new BufferedReader(fr);

			String daLine = br.readLine();

			while (daLine != null) {
				fullString += daLine;

				daLine = br.readLine();
				System.out.println(daLine);
			}

			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return fullString;
	}

}
