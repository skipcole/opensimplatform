package org.usip.osp.networking;

import javax.servlet.http.HttpSession;

/**
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
public class PlayerSessionObject {

	/** The Session object. */
	public HttpSession session = null;
	
	/**
	 * Returns the PSO stored in the session, or creates one. The coder can
	 * indicated if he or she wants to start a transaction.
	 */
	public static PlayerSessionObject getPSO(HttpSession session, boolean getConn) {

		PlayerSessionObject pso = (PlayerSessionObject) session.getAttribute("pso");

		if (pso == null) {
			System.out.println("pso is new");
			pso = new PlayerSessionObject();
			pso.session = session;
		}

		session.setAttribute("pso", pso);

		return pso;
	}
}
