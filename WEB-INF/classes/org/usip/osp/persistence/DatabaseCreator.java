package org.usip.osp.persistence;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.BaseSimSection;
import org.usip.osp.baseobjects.User;
import org.usip.osp.networking.FileIO;

public class DatabaseCreator {

	static String db_schema = "";
	static String db_org  = "";
	static String db_logo  = "";
	static String db_banner  = "";
	static String db_website  = "";
	static String db_notes  = "";
	static String email_smtp  = "";
	static String email_user  = "";
	static String email_pass  = "";
	static String email_user_address  = "";

	static String email_tech_address  = "";
	static String email_noreply_address  = "";

	static String email_server_number  = "";
	static String email_status  = "";
	
	
	/** This method is the web facing version of the database creation routine. The other
	 * time a database may be created is for testing purposes.
	 * 
	 * @param request
	 * @param adminUserId
	 * @return
	 */
	public static String handleCreateOrUpdateDB(HttpServletRequest request,
			Long adminUserId) {

		
		String sending_page = (String) request.getParameter("sending_page");
		String command = (String) request.getParameter("command");

		if ((sending_page == null) || (command == null)) {
			return "";
		}

		if (command.equalsIgnoreCase("Clear")) {
			return "";
		}
		
		String sio_id = (String) request.getParameter("sio_id");
		String loadss = (String) request.getParameter("loadss");
		
		if ((command.equalsIgnoreCase("Update"))
				|| (command.equalsIgnoreCase("Create"))) {
			
			loadUpParameters(request);
		}
		
		return handleCreateOrUpdateDB(command, sio_id, loadss, adminUserId);
		
	}
	/**
	 * Creates or updates a database based on the parameters passed in.
	 * 
	 * 
	 * @param request
	 * @return
	 */
	public static String handleCreateOrUpdateDB( 
			String command, String sio_id, String loadss,
			Long adminUserId) {

		String error_msg = "";
		
		SchemaInformationObject sio = new SchemaInformationObject();
		
		if (command.equalsIgnoreCase("Update")) {
			sio = SchemaInformationObject.getById(new Long(sio_id));
		}

		if ((command.equalsIgnoreCase("Update"))
				|| (command.equalsIgnoreCase("Create"))) {
			
			// Parameters have already been loaded by loadUpParameters(request);
			
			sio = fillSIO();

			String ps = MultiSchemaHibernateUtil.principalschema;

			if (!(MultiSchemaHibernateUtil.testConn())) {
				error_msg += "<BR> Failed to create database connection";
				return error_msg;
			}

			// Store SIO. If a schema object with the same name already exist, return error
			try {
				sio.saveMe();
			} catch (Exception e) {

				error_msg = "Warning. Unable to create the database entry for this schema. <br />"
						+ "This may indicate that it already has been created.";

				e.printStackTrace();

				return error_msg;
			}
			
			// Created the directory for exported/impoted simulations to reside.
			FileIO.makeSchemaSpecificDirectories(db_schema);

			// Only if we are creating a new Schema Information Object will we
			// recreate the database.
			if (command.equalsIgnoreCase("Create")) {
				MultiSchemaHibernateUtil.recreateDatabase(sio);

				if ((loadss != null) && (loadss.equalsIgnoreCase("true"))) {
					BaseSimSection.readBaseSimSectionsFromXMLFiles(db_schema, FileIO.getBase_section_web_dir());
				}
			}

			BaseUser bu = BaseUser.getByUserId(adminUserId);

			// Create the admin in this schema
			@SuppressWarnings("unused")
			User user = new User(db_schema, bu.getUsername(), bu.getPassword(),
					bu.getFirst_name(), bu.getLast_name(), bu.getMiddle_name(),
					bu.getFull_name(), true, true, true);

			error_msg = "database_created";

		}
		return error_msg;

	}
	
	/**
	 * Takes the SIO parameters out of the request object.
	 * 
	 * @param request
	 */
	public static void loadUpParameters(HttpServletRequest request) {
		db_schema = (String) request.getParameter("db_schema");
		db_org = (String) request.getParameter("db_org");
		db_logo = (String) request.getParameter("db_logo");
		db_banner = (String) request.getParameter("db_banner");
		db_website = (String) request.getParameter("db_website");
		db_notes = (String) request.getParameter("db_notes");
		email_smtp = (String) request.getParameter("email_smtp");
		email_user = (String) request.getParameter("email_user");
		email_pass = (String) request.getParameter("email_pass");
		
		email_user_address = (String) request
				.getParameter("email_user_address");
		email_tech_address = (String) request
				.getParameter("email_tech_address");
		email_noreply_address = (String) request
				.getParameter("email_noreply_address");

		email_server_number = (String) request
				.getParameter("email_server_number");
		
		email_status = checkEmailStatus(email_smtp, email_user,
				email_pass, email_user_address);
		
	}
	
	
	public static void loadTestDBParameters(HttpServletRequest request, 
			String _db_schema, String _db_org, String _db_logo, String _db_banner,
			String _db_website, String _db_notes, String _email_smtp, String _email_user,
			String _email_pass, String _email_user_address, String _email_tech_address,
			String _email_noreply_address, String _email_server_number) {
		
		DatabaseCreator.db_schema = 			_db_schema;
		DatabaseCreator.db_org = 				_db_org;
		DatabaseCreator.db_logo = 				_db_logo;
		DatabaseCreator.db_banner = 			_db_banner;
		
		DatabaseCreator.db_website = 			_db_website;
		DatabaseCreator.db_notes = 				_db_notes;
		DatabaseCreator.email_smtp = 			_email_smtp;
		DatabaseCreator.email_user = 			_email_user;
		DatabaseCreator.email_pass = 			_email_pass;
		DatabaseCreator.email_user_address = 	_email_user_address;

		DatabaseCreator.email_tech_address = 	_email_tech_address;
		DatabaseCreator.email_noreply_address = _email_noreply_address;

		DatabaseCreator.email_server_number = 	_email_server_number;
		
		DatabaseCreator.email_status = checkEmailStatus(email_smtp, email_user,
				email_pass, email_user_address);
		
	}

	/**
	 * Verify that all required fields have been entered for the email smtp
	 * server.
	 */
	public static String checkEmailStatus(String email_smtp, String email_user,
			String email_pass, String email_user_address) {

		if ((email_smtp == null) || (email_user == null)
				|| (email_pass == null) || (email_user_address == null)) {
			return SchemaInformationObject.EMAIL_STATE_DOWN;
		} else if ((email_smtp.trim().equalsIgnoreCase(""))
				|| (email_user.trim().equalsIgnoreCase(""))
				|| (email_pass.trim().equalsIgnoreCase(""))
				|| (email_user_address.trim().equalsIgnoreCase(""))) {
			return SchemaInformationObject.EMAIL_STATE_DOWN;
		} else {
			return SchemaInformationObject.EMAIL_STATE_UNVERIFIED;
		}
	}
	
	public static SchemaInformationObject fillSIO(){
		
		SchemaInformationObject sio = new SchemaInformationObject();
		
		// Fill SIO
		sio.setSchema_name(db_schema);
		sio.setSchema_organization(db_org);
		sio.setSchemaOrganizationLogo(db_logo);
		sio.setSchemaOrganizationBanner(db_banner);
		sio.setSchemaOrganizationWebsite(db_website);
		sio.setNotes(db_notes);
		sio.setEmail_smtp(email_smtp);
		sio.setSmtp_auth_user(email_user);
		sio.setSmtp_auth_password(email_pass);
		sio.setEmailTechAddress(email_tech_address);
		sio.setEmail_archive_address(email_user_address);
		sio.setEmailNoreplyAddress(email_noreply_address);
		sio.setEmailState(email_status);
		sio.setEmailServerNumber(new Long(email_server_number));
		
		return sio;
	}

}
