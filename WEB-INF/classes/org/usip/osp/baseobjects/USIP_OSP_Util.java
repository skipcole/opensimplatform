package org.usip.osp.baseobjects;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import org.usip.osp.networking.*;
import org.usip.osp.persistence.*;

/*
 * 
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it
 * and/or modify it under the terms of the new BSD Style license associated with
 * this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. <BR>
 * 
 */
public class USIP_OSP_Util {

	/**
	 * Takes a comma separated list of actor ids and turns it into a list of
	 * names.
	 * 
	 * @param request
	 * @param id_list
	 * @return
	 */
	public static String stringListToNames(String schema, Long sim_id,
			Long running_sim_id, HttpServletRequest request, String id_list,
			String separator) {

		if (id_list == null) {
			return "";
		}

		StringTokenizer str = new StringTokenizer(id_list, ",");

		String returnList = "";

		while (str.hasMoreTokens()) {
			Long a_id = new Long(str.nextToken().trim());
			returnList += (USIP_OSP_Cache.getActorName(schema, sim_id,
					running_sim_id, request, a_id) + separator);

		}

		// chop the final comma off
		if (returnList.endsWith(separator)) {
			returnList = returnList.substring(0, returnList.length() - 2);
		}

		return returnList;
	}

	/**
	 * Strips HTML out of a string, and returns a smaller portion of it.
	 * 
	 * @param starterStuff
	 * @param length
	 * @return
	 */
	public static String cleanAndShorten(String starterStuff, int length) {

		starterStuff = starterStuff.replaceAll("\\<.*?>", " ");

		if (starterStuff.length() < length) {
			length = starterStuff.length();
		}

		String shortIntro = starterStuff.substring(0, length);

		return shortIntro;
	}

	/**
	 * Constructs a nicely formatted name from the standard 3 western parts:
	 * first, middle and last.
	 * 
	 * @param first
	 * @param middle
	 * @param last
	 * @return
	 */
	public static String constructName(String first, String middle, String last) {

		// Construct full name from the piece of name passed in.
		String returnString = first + " " + middle;
		returnString = returnString.trim();
		returnString += " " + last;
		returnString = returnString.trim();

		return returnString;

	}

	/**
	 * Returns the matchText if a and b match.
	 * 
	 * @param a
	 * @param b
	 * @param matchText
	 * @return
	 */
	public static String matchSelected(int a, int b, String matchText) {

		if (a == b) {
			return matchText;
		} else {
			return "";
		}
	}

	/**
	 * If a matches b, return the matchText.
	 * 
	 * @param a
	 * @param b
	 * @param matchText
	 * @return
	 */
	public static String matchSelected(String a, String b, String matchText) {
		if ((a == null) || (b == null)) {
			return "";
		}

		if (a.equalsIgnoreCase(b)) {
			return matchText;
		} else {
			return "";
		}
	}

	/**
	 * Returns the matchText if a and b match.
	 * 
	 * @param a
	 * @param b
	 * @param matchText
	 * @return
	 */
	public static String matchSelected(Long a, Long b, String matchText) {
		if ((a == null) || (b == null)) {
			return "";
		}

		if (a.equals(b)) {
			return matchText;
		} else {
			return "";
		}
	}

	public static String matchSelected(boolean a, String matchText) {

		if (a) {
			return matchText;
		} else {
			return "";
		}
	}

	/**
	 * Converts HTML to HTML presentation code.
	 * 
	 * @param htmlString
	 * @return
	 */
	public static String htmlToCode(String htmlString) {

		htmlString = htmlString.replaceAll("\\r\\n", "<br />");
		htmlString = htmlString.replaceAll("<", "&lt;");
		htmlString = htmlString.replaceAll(">", "&gt;");

		return htmlString;
	}

	/**
	 * Turns nulls into empty strings.
	 * 
	 * @param input
	 * @return
	 */
	public static String cleanNulls(String input) {
		if (input == null) {
			return ""; //$NON-NLS-1$
		} else {
			return input.trim();
		}
	}

	/**
	 * Turns a string into a Long.
	 * 
	 * @param inputString
	 * @return
	 */
	public static Long stringToLong(String inputString) {

		Long returnLong = null;

		if (inputString != null) {
			try {
				returnLong = new Long(inputString);
			} catch (Exception e) {
				Logger.getRootLogger().warn(
						"problem converting string to Long: " + inputString);
			}
		}

		return returnLong;
	}

	/**
	 * Checks to see if a string input field contains information.
	 * 
	 * @param inputField
	 * @return
	 */
	public static boolean stringFieldHasValue(String inputField) {
		if (inputField == null) {
			return false;
		}
		if (inputField.trim().length() == 0) {
			return false;
		}
		if (inputField.equalsIgnoreCase("null")) {
			return false;
		}
		return true;
	}
	
	
	public static boolean stringFieldMatches(String a, String b){
		
		if ((a == null) || (b == null)){
			return false;
		}
		
		return (a.equalsIgnoreCase(b));
		
	}

	public static boolean findMatchingLong(ArrayList list, Long longLookedFor) {

		if (longLookedFor == null) {
			return false;
		}

		for (ListIterator li = list.listIterator(); li.hasNext();) {
			Long listLong = (Long) li.next();
			if (longLookedFor.intValue() == listLong.intValue()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks to see if the user has a session object (AFSO or PSO) set. If so,
	 * returns the SessionObjectBase.
	 * 
	 * @param request
	 * @return
	 */
	public static SessionObjectBase getSessionObjectBaseIfFound(
			HttpServletRequest request) {

		PlayerSessionObject pso = (PlayerSessionObject) request.getSession()
				.getAttribute("pso");
		AuthorFacilitatorSessionObject afso = (AuthorFacilitatorSessionObject) request
				.getSession().getAttribute("afso");
		SessionObjectBase sob = (SessionObjectBase) request.getSession()
				.getAttribute("sob");

		if (pso != null) {
			sob = (SessionObjectBase) pso;
		}

		if (afso != null) {
			sob = (SessionObjectBase) afso;
		}

		return sob;
	}

	/**
	 * Returns a SessionObjectBase, no matter what.
	 * 
	 * @param request
	 * @return
	 */
	public static SessionObjectBase getSessionObjectBase(
			HttpServletRequest request) {

		SessionObjectBase sob = getSessionObjectBaseIfFound(request);

		if (sob == null) {
			sob = new SessionObjectBase();
			request.getSession().setAttribute("sob", sob);
		}

		return sob;
	}

	/**
	 * Everything below here just checks to make sure we have a good database
	 * connection. TODO Record when connections are being reset here
	 */
	public static void cleanConnections() {

		//if (completedStartup) {
			try {
				BaseUser bu = BaseUser.getByUserId(new Long(1));
			} catch (Exception e) {
				MultiSchemaHibernateUtil
						.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);
			}

			for (ListIterator<SchemaInformationObject> sio_l = SchemaInformationObject
					.getAll().listIterator(); sio_l.hasNext();) {
				SchemaInformationObject sio = sio_l.next();

				try {
					User u = User.getById(sio.getSchema_name(), new Long(1));
				} catch (Exception e) {
					MultiSchemaHibernateUtil.commitAndCloseTransaction(sio
							.getSchema_name());
					
					e.printStackTrace();
				}

			}
		//}

	}

	public static final String lineTerminator = "\r\n"; //$NON-NLS-1$

	/**
	 * returns a list of strings containing the value ( generally assumed to be
	 * an id) from the checkboxes of a form.
	 */
	public static List<String> getIdsOfCheckBoxes(String tagString,
			HttpServletRequest request) {

		ArrayList<String> returnList = new ArrayList<String>();

		for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
			String param_name = (String) e.nextElement();

			if (param_name.startsWith(tagString)) {
				if ((request.getParameter(param_name) != null)
						&& (request.getParameter(param_name)
								.equalsIgnoreCase("true"))) {
					String this_a_id = param_name.replaceFirst(tagString, "");

					returnList.add(this_a_id);
				}
			}
		}

		return returnList;
	}

	public static void main(String args[]) {

		cleanStringForFileName("!@#$Hello().alsdjfaosdas;ldfasf.text");

	}

	/**
	 * Takes a string which may contain special characters and returns an alpha
	 * numeric sequence suitable for using as a file name.
	 * 
	 * @param inputString
	 * @return
	 */
	public static String cleanStringForFileName(String inputString) {

		if (inputString == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();

		int lastDecimal = 0;

		int newStringCounter = 0;

		for (int ii = 0; ii < inputString.length(); ++ii) {

			if ((inputString.charAt(ii) >= 'A')
					&& (inputString.charAt(ii) <= 'Z')) {
				sb.append(inputString.charAt(ii));
				newStringCounter += 1;
			}

			if ((inputString.charAt(ii) >= 'a')
					&& (inputString.charAt(ii) <= 'z')) {
				sb.append(inputString.charAt(ii));
				newStringCounter += 1;
			}

			if ((inputString.charAt(ii) >= '0')
					&& (inputString.charAt(ii) <= '9')) {
				sb.append(inputString.charAt(ii));
				newStringCounter += 1;
			}

			if (inputString.charAt(ii) == '_') {
				sb.append(inputString.charAt(ii));
				newStringCounter += 1;
			}

			if (inputString.charAt(ii) == '.') {
				newStringCounter += 1;
				sb.append("_");
				lastDecimal = newStringCounter;
			}
		}

		String returnString = new String(sb);

		return returnString;
	}

	/**
	 * Copies in the values from the Simulation passed in.
	 * 
	 * @param objectBeingCopied
	 */
	public static void copyInBasicValues(Object receivingObject,
			Object objectBeingCopied, Class theClassAtHand) {

		Field[] fields = theClassAtHand.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {

			// Don't attempt to set final (constant) fields.
			int modifiers = fields[i].getModifiers();

			if (!(Modifier.isFinal(modifiers))) {
				fields[i].setAccessible(true);
				try {
					fields[i].set(receivingObject,
							fields[i].get(objectBeingCopied));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
