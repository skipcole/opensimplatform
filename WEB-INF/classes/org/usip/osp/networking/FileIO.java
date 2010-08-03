package org.usip.osp.networking;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.usip.osp.baseobjects.USIP_OSP_Properties;
import org.apache.log4j.*;

/**
 * Utility class that provides static methods to handle file input and output.
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
 */
public class FileIO {

	protected static String archives_dir = "";
	private static String actor_image_dir = ""; //$NON-NLS-1$
	private static String base_section_web_dir = ""; //$NON-NLS-1$
	private static String model_dir = ""; //$NON-NLS-1$
	private static String base_web_dir = ""; //$NON-NLS-1$
	protected static String packaged_sim_dir = ""; //$NON-NLS-1$
	protected static String upgrade_files_dir = ""; //$NON-NLS-1$
	private static String sim_image_dir = ""; //$NON-NLS-1$

	static {

		base_web_dir = USIP_OSP_Properties.getValue("base_web_dir"); //$NON-NLS-1$

		archives_dir = base_web_dir + "simulation_admin" + File.separator
				+ "database_archives" + File.separator;
		base_section_web_dir = base_web_dir + "simulation_section_information"
				+ File.separator;
		model_dir = base_web_dir + "simulation_model_information"
				+ File.separator;
		actor_image_dir = base_web_dir + "osp_core" + File.separator + "images"
				+ File.separator + "actors" + File.separator;
		packaged_sim_dir = base_web_dir + "simulation_sharing" + File.separator
				+ "packaged_simulations" + File.separator;
		upgrade_files_dir = base_web_dir + "software_upgrade_files" + File.separator;
		sim_image_dir = base_web_dir + "simulation" + File.separator + "images"
				+ File.separator;

	}

	public static byte[] getImageFile(int saveType, String fileName) {

		byte[] returnByte = null;

		String fileDir = null;

		if (saveType == OSPSimMedia.ACTOR_IMAGE) {
			fileDir = actor_image_dir;
		} else {
			return null;
		}
		try {
			File imageFile = new File(fileDir + fileName);

			FileInputStream is = new FileInputStream(imageFile);

			long length = imageFile.length();

			if (length > Integer.MAX_VALUE) {
				throw new IOException("The file is too big");
			}

			returnByte = new byte[(int) length];

			// Read in the bytes
			int offset = 0;
			int numRead = 0;
			while (offset < returnByte.length
					&& (numRead = is.read(returnByte, offset, returnByte.length
							- offset)) >= 0) {
				offset += numRead;
			}

			if (offset < returnByte.length) {
				throw new IOException("The file was not completely read: "
						+ fileName);
			}

			is.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnByte;

	}

	public static String getSaveDirectory(int saveType) {

		String saveDir = null;

		if (saveType == OSPSimMedia.ACTOR_IMAGE) {
			saveDir = actor_image_dir;
		} else if (saveType == OSPSimMedia.SIM_IMAGE) {
			saveDir = sim_image_dir;
		} else {
			Logger.getRootLogger().debug(
					"Warning. Don't understand saveType: " + saveType);
		}

		return saveDir;
	}
	
	public static void saveImageFile(int saveType, String fileName,
			byte [] fileData) {

		String saveDir = getSaveDirectory(saveType);
		
		System.out.println("saveDir was " + saveDir);

		if (saveDir != null) {
			try {
				
				String filePathAndName = saveDir + fileName;
				
				System.out.println(filePathAndName);
				
				File outFile = new File(filePathAndName);
				
				if (outFile.exists()){
					System.out.println("getting new outfile name");
					
					outFile = getCleanFileName(filePathAndName);
				}
				FileOutputStream fos = new FileOutputStream(outFile);
				
				fos.write(fileData);

				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static File getCleanFileName(String filePathAndName){
		
		File outFile = new File(filePathAndName);
		
		int ii = 0;
		
		while(outFile.exists()){
			ii += 1;
			outFile = new File(filePathAndName + "_" + ii);
		}
		
		return outFile;
	}

	/**
	 * 
	 * @param saveType
	 * @param fileName
	 * @param fileData
	 */
	public static void saveImageFile(int saveType, String fileName,
			File fileData) {

		String saveDir = getSaveDirectory(saveType);

		if (saveDir != null) {
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
	public static boolean saveUserArchiveXMLFile(String fileContents,
			String fileName) {
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
	public static boolean saveSimulationXMLFile(String fileContents,
			String fileName) {

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

	public static boolean saveSimulationXMLFile(File uploadedFile,
			String fileName) {

		try {
			File outFile = new File(packaged_sim_dir + fileName);

			// FileWriter outFW = new FileWriter(outFile);

			byte[] readData = new byte[1024];
			FileInputStream fis = new FileInputStream(uploadedFile);

			FileOutputStream fos = new FileOutputStream(outFile);
			int i = fis.read(readData);

			while (i != -1) {
				fos.write(readData, 0, i);
				i = fis.read(readData);
			}
			fis.close();
			fos.close();

			return true;

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
			Logger.getRootLogger().debug(
					"Problem finding files at " + packaged_sim_dir); //$NON-NLS-1$
		} else {

			File files[] = locDir.listFiles();

			if (files == null) {
				Logger.getRootLogger().debug(
						"Problem finding files at " + packaged_sim_dir); //$NON-NLS-1$
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

	/**
	 * This method either reads everything that comes before a delimiter, or
	 * everything that comes after.
	 * 
	 * @param thisFile
	 * @param delimiter
	 * @return
	 */
	public static String getPartialFileContents(File thisFile,
			String delimiter, boolean readUptoDelimiter) {

		StringBuilder tempBuffer = new StringBuilder();
		try {

			BufferedReader br = new BufferedReader(new FileReader(thisFile));

			String daLine = br.readLine();

			boolean continueOn = true;
			boolean foundDelimiter = false;

			while ((daLine != null) && (continueOn)) {

				// If reading up to delimiter, and havent found it,
				// or if reading after the delimeter and have found it,
				// the append line.
				if (((readUptoDelimiter) && (!(foundDelimiter)))
						|| ((!(readUptoDelimiter)) && (foundDelimiter))) {
					tempBuffer.append(daLine);
				}

				daLine = br.readLine();

				// If we have found the delimiter, add the line and change the
				// state
				if ((daLine != null) && (daLine.indexOf(delimiter) != -1)) {
					if (readUptoDelimiter) {
						tempBuffer.append(daLine);
					}
					foundDelimiter = true;
				}

				// If we are only reading up to the delimiter, and we have found
				// it, then stop reading
				if ((readUptoDelimiter) && (foundDelimiter)) {
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
