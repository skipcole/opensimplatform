package org.usip.osp.networking;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.usip.osp.persistence.BaseUser;

/**
 * This object contains methods to help the main session objects (
 * AuthorFacilitatorSessionObject and PlayerSessionObject) do their work.
 * 
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
public class OSPSessionObjectHelper {
	
	/** Id of the user that has logged in. */
	private Long userid;
	
	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	/**
	 * Returns the OSP_SOH stored in the session, or creates one.
	 */
	public static OSPSessionObjectHelper getOSP_SOH(HttpSession session) {

		OSPSessionObjectHelper osp_soh = (OSPSessionObjectHelper) session.getAttribute("osp_soh");

		if (osp_soh == null) {
			Logger.getRootLogger().debug("osp_soh is new");
			osp_soh = new OSPSessionObjectHelper();
		}

		session.setAttribute("osp_soh", osp_soh);

		return osp_soh;
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public static BaseUser validate(HttpServletRequest request) {

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		BaseUser bu = BaseUser.validateUser(username, password);
		
		if (bu != null){
			OSPSessionObjectHelper osp_soh = getOSP_SOH(request.getSession());
			osp_soh.setUserid(bu.getId());
		}

		return bu;

	}

	/**
	 * Called from the head of the login jsp, this attempts to log the user in. 
	 * 
	 * @param request
	 * @return
	 */
	public static String handleLoginAttempt(HttpServletRequest request) {

		String returnString = "";

		String attempting_login = (String) request.getParameter("attempting_login");

		if ((attempting_login != null) && (attempting_login.equalsIgnoreCase("true"))) {

			BaseUser bu = validate(request);

			if (bu != null) {
				returnString = "forward_on";
			} else {
				returnString = "error";
			}

		}

		return returnString;

	}
}
