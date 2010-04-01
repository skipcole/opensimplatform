package org.usip.osp.coursemanagementinterface;

import java.io.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.USIP_OSP_Properties;
import org.usip.osp.baseobjects.User;
import org.usip.osp.networking.FileIO;
import org.usip.osp.persistence.BaseUser;

import com.oreilly.servlet.MultipartRequest;

public class CSVInterpreter {

	private boolean getMiddleName = false;

	public User setField(User user, String fieldName, String fieldValue) {

		// switch case
		return null;
	}

	public static String importCSV(HttpServletRequest request, String schema) {

		String returnString = "";
		
		Hashtable importMappings = new Hashtable();

		try {
			MultipartRequest mpr = new MultipartRequest(request, USIP_OSP_Properties.getValue("uploads"));

			String sending_page = (String) mpr.getParameter("sending_page");

			String MAX_FILE_SIZE = (String) mpr.getParameter("MAX_FILE_SIZE");

			Long max_file_longvalue = new Long(MAX_FILE_SIZE).longValue();

			if ((sending_page != null) && (sending_page.equalsIgnoreCase("import_csv"))) {

				returnString = "Importing CSV File ";

				String initFileName = mpr.getOriginalFileName("uploadedfile");

				if ((initFileName != null) && (initFileName.trim().length() > 0)) {

					returnString += mpr.getOriginalFileName("uploadedfile") + "<br />";

					File fileData = mpr.getFile("uploadedfile");

					Logger.getRootLogger().debug("File is " + fileData.length());

					if (fileData.length() <= max_file_longvalue) {

						BufferedReader br = new BufferedReader(new FileReader(fileData));

						String daLine = br.readLine();

						boolean foundFirstLine = false;
						while (daLine != null) {
							if (daLine.startsWith("#")) {
								// Don't do anything, its a comment
							} else {

								if (!foundFirstLine) {
									// If its anything other then '
									if (!(daLine.startsWith("Email"))) {
										returnString += " File does not seem to be in the correct format";
										return returnString;
									} else {
										readInFileHeadings(daLine, importMappings);
										foundFirstLine = true;
									}
								} else {
								
									User u = readInLine(daLine, importMappings);
									if (User.getByUsername(schema, u.getUser_name()) == null) {
										returnString += saveUser(u, schema);
									} else {
										returnString += "<BR />User already existed: " + u.getUser_name();
									}
									

								}
							}

							daLine = br.readLine();
						}

						br.close();

					} else {
						returnString += "But selected csv file too large.";
					}

				}
			}
		} catch (java.io.IOException ioe) {
			Logger.getRootLogger().warn("Entered Import Page: " + ioe.getMessage());
		} catch (Exception e) {
			Logger.getRootLogger().debug(e.getMessage());
			e.printStackTrace();
		}

		return returnString;
	}

	/**
	 * Saves a copy of the user back to the database, and notes any problems.
	 * 
	 * @param u
	 * @param schema
	 * @return
	 */
	private static String saveUser(User u, String schema) {

		String returnString = "";
		
		try {
			BaseUser bu = new BaseUser();
			
			bu.setFirst_name(u.getBu_first_name());
			bu.setLast_name(u.getBu_last_name());
			bu.setMiddle_name(u.getBu_middle_name());
			bu.setUsername(u.getUser_name());
			bu.setPassword(u.getBu_password());
			bu.setFull_name(u.getBu_full_name());
			
			bu.saveMe();
			
			u.setId(bu.getId());
			
			u.saveJustUser(schema);
			
		} catch (Exception e){
			returnString += "problem saving user " + u.getBu_username() + ", " + e.getMessage();
		}
		
		return returnString;
	}
	
	public static void readInFileHeadings(String daLine, Hashtable importMapping) {

		StringTokenizer str = new StringTokenizer(daLine, ",");

		int ii = 1;
		while (str.hasMoreTokens()) {
			Long iI = new Long(ii);
			importMapping.put(iI, str.nextToken().trim());

			++ii;
		}
	}

	public static User readInLine(String daLine, Hashtable importMapping) {

		StringTokenizer str = new StringTokenizer(daLine, ",");

		User user = new User();

		boolean pullPasswordFromName = false;
		int ii = 1;
		while (str.hasMoreTokens()) {
			Long mapKey = new Long(ii);

			String fieldName = (String) importMapping.get(mapKey);		
			String fieldValue = str.nextToken().trim();
			
			if (fieldName != null) {
				if (fieldName.equalsIgnoreCase("Email")) {
					user.setBu_username(fieldValue);
					user.setUser_name(user.getBu_username());
					System.out.println(user.getBu_username());
				} else if (fieldName.equalsIgnoreCase("First Name")) {
					user.setBu_first_name(fieldValue);
				} else if (fieldName.equalsIgnoreCase("Last Name")) {
					user.setBu_last_name(fieldValue);
				} else if (fieldName.equalsIgnoreCase("Password")) {
					
					if (fieldValue.equalsIgnoreCase("Initials")){
						pullPasswordFromName = true;
					} else {
						user.setBu_password(fieldValue);
					}					
					
				} else {
					System.out.println("unaccounted for field: " + fieldValue);
				}
			} else {
				System.out.println("field null for ii = " + ii + ", field:" + str.nextToken().trim());
			}
			
			++ii;
		}
		
		if (pullPasswordFromName){
			BaseUser bu = new BaseUser();
			bu.setFirst_name(user.getBu_first_name());
			bu.setMiddle_name(user.getBu_middle_name());
			bu.setLast_name(user.getBu_last_name());
			user.setBu_password(bu.getInitials());
		}

		user.setBu_full_name(user.getBu_first_name() + " " + user.getBu_last_name());
		
		return user;
	}
}
