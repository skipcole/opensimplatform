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

	/**
	 * Imports the CSV file full of user information..
	 * 
	 * @param request
	 * @param schema
	 * @return
	 */
	public static String importCSV(HttpServletRequest request, String schema) {

		String returnString = "";

		Hashtable importMappings = new Hashtable();

		try {
			MultipartRequest mpr = new MultipartRequest(request,
					USIP_OSP_Properties.getValue("uploads"));

			String sending_page = (String) mpr.getParameter("sending_page");

			String MAX_FILE_SIZE = (String) mpr.getParameter("MAX_FILE_SIZE");

			Long max_file_longvalue = new Long(MAX_FILE_SIZE).longValue();

			if ((sending_page != null)
					&& (sending_page.equalsIgnoreCase("import_csv"))) {

				returnString = "Importing CSV File ";

				String initFileName = mpr.getOriginalFileName("uploadedfile");

				if ((initFileName != null)
						&& (initFileName.trim().length() > 0)) {

					returnString += mpr.getOriginalFileName("uploadedfile")
							+ "<br />";

					File fileData = mpr.getFile("uploadedfile");

					Logger.getRootLogger()
							.debug("File is " + fileData.length());

					if (fileData.length() <= max_file_longvalue) {

						BufferedReader br = new BufferedReader(new FileReader(
								fileData));

						String daLine = br.readLine();

						boolean foundFirstLine = false;
						while (daLine != null) {
							if (daLine.startsWith("#")) {
								returnString += daLine + "<br />";
							} else {

								if (!foundFirstLine) {
									// If its anything other then '
									if (!(daLine.startsWith("Email"))) {
										returnString += " File does not seem to be in the correct format";
										return returnString;
									} else {
										readInFileColumnHeadings(daLine,
												importMappings);
										foundFirstLine = true;
										returnString += "Found First Line <br />";
									}
								} else {
									returnString += readInLineOfUserData(
											schema, daLine, importMappings);
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
			returnString += "Ready for Import";
			Logger.getRootLogger().warn(
					"Entered Import Page: " + ioe.getMessage());
		} catch (Exception e) {
			returnString += e.getMessage();
			Logger.getRootLogger().debug(e.getMessage());
			e.printStackTrace();
		}

		return returnString;
	}

	/**
	 * Reads in a line expecting them to be the titles of the columns.
	 * 
	 * @param daLine
	 * @param importMapping
	 */
	public static void readInFileColumnHeadings(String daLine, Hashtable importMapping) {

		StringTokenizer str = new StringTokenizer(daLine, ",");

		int ii = 1;
		while (str.hasMoreTokens()) {
			Long iI = new Long(ii);
			importMapping.put(iI, str.nextToken().trim());

			++ii;
		}
	}

	public static String readInLineOfUserData(String schema, String daLine,
			Hashtable importMapping) {

		String returnString = "";

		StringTokenizer str = new StringTokenizer(daLine, ",");

		User user = new User();
		BaseUser bu = new BaseUser();

		boolean pullPasswordFromName = false;
		int ii = 1;
		/* Looping over all of the fields that have been read in. */
		while (str.hasMoreTokens()) {
			Long mapKey = new Long(ii);

			String fieldName = (String) importMapping.get(mapKey);
			String fieldValue = str.nextToken().trim();

			if (fieldName != null) {
				if (fieldName.equalsIgnoreCase("Email")) {
					bu.setUsername(fieldValue);
					user.setBu_username(fieldValue);
					user.setUser_name(user.getBu_username());

					if (!(User.getByUsername(schema, fieldValue) == null)) {
						returnString += "<BR />User already existed: "
								+ fieldValue;
						return returnString;
					}

				} else if (fieldName.equalsIgnoreCase("First Name")) {
					user.setBu_first_name(fieldValue);
				} else if (fieldName.equalsIgnoreCase("Last Name")) {
					user.setBu_last_name(fieldValue);
				}  else if (fieldName.equalsIgnoreCase("Author")) {
					if (fieldValue.equalsIgnoreCase("true")){
						user.setSim_author(true);
					}
				}  else if (fieldName.equalsIgnoreCase("Instructor")) {
					if (fieldValue.equalsIgnoreCase("true")){
						user.setSim_instructor(true);
					}
				} else if (fieldName.equalsIgnoreCase("Password")) {

					if (fieldValue.equalsIgnoreCase("Initials")) {
						pullPasswordFromName = true;
					} else {
						bu.setPassword(fieldValue);
					}

				} else if (fieldName.equalsIgnoreCase("Password Hashed")) {
					bu.setPasswordAlreadyHashed(fieldValue);
				} else {
					System.out.println("unaccounted for field: " + fieldValue);
				}
			} else {
				System.out.println("field null for ii = " + ii + ", field:"
						+ str.nextToken().trim());
			}

			++ii;
		} // End of loop over tokens. All data should be loaded by now.

		bu.setFirst_name(user.getBu_first_name());
		bu.setMiddle_name(user.getBu_middle_name());
		bu.setLast_name(user.getBu_last_name());

		if (pullPasswordFromName) {
			bu.setPassword(bu.getInitials());
		}

		user.setBu_full_name(user.getBu_first_name() + " "
				+ user.getBu_last_name());
		bu.setFull_name(user.getBu_full_name());

		bu.saveMe();

		user.setId(bu.getId());

		user.saveJustUser(schema);

		returnString += "<br /> saved user " + user.getUser_name();

		return returnString;
	}
}
