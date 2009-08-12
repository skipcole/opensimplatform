package org.usip.osp.networking;

import javax.servlet.http.HttpServletRequest;

import org.usip.osp.persistence.BaseUser;

/**
 * This object contains methods to help the main session objects ( xSessionObject and PlayerSessionObject) do their 
 * work.
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

	/**
	 * 
	 * @param request
	 * @return
	 */
	public static BaseUser validate(HttpServletRequest request) {

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		BaseUser bu = BaseUser.validateUser(username, password);

		return bu;

	}
}
