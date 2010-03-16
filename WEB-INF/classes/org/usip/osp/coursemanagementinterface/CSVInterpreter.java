package org.usip.osp.coursemanagementinterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.USIP_OSP_Properties;
import org.usip.osp.baseobjects.User;
import org.usip.osp.networking.FileIO;

import com.oreilly.servlet.MultipartRequest;

public class CSVInterpreter {

	private boolean getMiddleName = false;

	public User setField(User user, String fieldName, String fieldValue) {

		// switch case
		return null;
	}

	public static String importCSV(HttpServletRequest request) {

		String returnString = "";
		
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

						while (daLine != null) {
							System.out.println(daLine);
							
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
		}catch (Exception e) {
			Logger.getRootLogger().debug(e.getMessage());
			e.printStackTrace();
		}

		return returnString;

	}
}
