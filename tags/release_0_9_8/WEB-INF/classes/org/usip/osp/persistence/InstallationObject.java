package org.usip.osp.persistence;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.USIP_OSP_Properties;

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
 */
public class InstallationObject {

	public HttpSession session = null;
	
	public static final String IO_KEY = "usip_osp_install_object";
	
	/**
	 * Returns the Installation Object stored in the session, or creates one. 
	 */
	public static InstallationObject getInstallationObject(HttpSession session) {

		InstallationObject usip_osp_io = (InstallationObject) session
				.getAttribute(IO_KEY);

		if (usip_osp_io == null) {
			Logger.getRootLogger().debug("install object is new");
			usip_osp_io = new InstallationObject();
			usip_osp_io.session = session;
		}

		session.setAttribute(IO_KEY, usip_osp_io);

		return usip_osp_io;
	}
	
	public static final int INSTALL_ERROR_NO_PROP = 1;
	public static final int INSTALL_ERROR_NO_CONN = 2;

	/**
	 * Checks to see if properties file is found and connection to database works.
	 * 
	 * @param request
	 * @return
	 */
	public static int checkInstall(HttpServletRequest request) {

		if (!(USIP_OSP_Properties.isFoundPropertiesFile())) {
			return INSTALL_ERROR_NO_PROP;
		}

		if (!MultiSchemaHibernateUtil.testConn()) {
			return INSTALL_ERROR_NO_CONN;
		}

		return 0;
	}
	
	private boolean installationLogin = false;
	
	/** Page to forward the user on to. */
	public boolean forward_on = false;
	
	/** The page to take them back to if needed. */
	public String backPage = "index.jsp"; //$NON-NLS-1$
	
	/**
	 * Recreates the root database that will hold information on the other
	 * schemas and user information.
	 * 
	 * @param request
	 */
	public String handleCreateRootDB(HttpServletRequest request) {

		String returnMsg = "";

		String sending_page = request.getParameter("sending_page");
		String wipe_database_key = request.getParameter("wipe_database_key");

		boolean clearedToWipeDB = false;

		if ((wipe_database_key != null)
				&& (wipe_database_key.equals(USIP_OSP_Properties
						.getValue("wipe_database_key")))) {
			clearedToWipeDB = true;
		}

		if (clearedToWipeDB && (sending_page != null)
				&& (sending_page.equalsIgnoreCase("install_root_db"))) {

			MultiSchemaHibernateUtil.recreateRootDatabase();
			returnMsg = "Root schema should now contain empty tables.";

			// Entering the correct key is equivalent to having logged in.
			this.installationLogin = true;

			this.forward_on = true;
			this.backPage = "install_db.jsp";

			return returnMsg;

		} else if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("install_root_db"))) {
			returnMsg = "Wrong key entered.";
		}

		return returnMsg;

	}

	public boolean isInstallationLogin() {
		return installationLogin;
	}

	public void setInstallationLogin(boolean installationLogin) {
		this.installationLogin = installationLogin;
	}
	
	
}
