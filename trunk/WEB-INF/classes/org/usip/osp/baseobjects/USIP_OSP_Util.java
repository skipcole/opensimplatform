package org.usip.osp.baseobjects;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

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
}
