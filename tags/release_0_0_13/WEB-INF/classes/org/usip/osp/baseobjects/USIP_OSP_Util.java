package org.usip.osp.baseobjects;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.usip.osp.networking.USIP_OSP_Cache;

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
	public static String stringListToNames(String schema, Long sim_id, Long running_sim_id, HttpServletRequest request, String id_list, String separator) {

		if (id_list == null) {
			return "";
		}

		StringTokenizer str = new StringTokenizer(id_list, ",");

		String returnList = "";

		while (str.hasMoreTokens()) {
			Long a_id = new Long(str.nextToken().trim());
			returnList += (USIP_OSP_Cache.getActorName(schema, sim_id, running_sim_id, request, a_id) + separator);

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
	public static String cleanAndShorten(String starterStuff, int length){
		
		starterStuff = starterStuff.replaceAll("\\<.*?>"," ");
		
		if (starterStuff.length() < length) {
			length = starterStuff.length();
		}
		
		String shortIntro = starterStuff.substring(0, length);

		return shortIntro;
	}
	
	/**
	 * Constructs a nicely formatted name from the standard 3 western parts: first, middle and last.
	 * 
	 * @param first
	 * @param middle
	 * @param last
	 * @return
	 */
	public static String constructName(String first, String middle, String last){
		
		// Construct full name from the piece of name passed in.
		String returnString = first + " " + middle;
		returnString = returnString.trim();
		returnString += " " + last;
		returnString = returnString.trim();
		
		return returnString;
		
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
	
	public static String htmlToCode(String htmlString){
		
		htmlString = htmlString.replaceAll("\\r\\n", "<br />");
		htmlString = htmlString.replaceAll("<", "&lt;");
		htmlString = htmlString.replaceAll(">", "&gt;");
		
		return htmlString;
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
	
	public static Long stringToLong(String inputString){
		
		Long returnLong = null;
		
		if (inputString != null){
			try {
				returnLong = new Long(inputString);
			} catch (Exception e) {
				Logger.getRootLogger().warn("problem converting string to Long: " + inputString);
			}	
		}
		
		return returnLong;
	}

	public static boolean findMatchingLong(ArrayList list, Long longLookedFor){
		
		if (longLookedFor == null){
			return false;
		}
		
		for (ListIterator li = list.listIterator(); li.hasNext();){
			Long listLong = (Long) li.next();
			if (longLookedFor.intValue() == listLong.intValue()){
				return true;
			}
		}
		
		return false;
	}
}