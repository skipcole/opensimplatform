package org.usip.osp.communications;

import javax.mail.*;
import javax.mail.internet.*;

import org.usip.osp.baseobjects.RunningSimulation;
import org.usip.osp.baseobjects.USIP_OSP_Properties;
import org.usip.osp.baseobjects.UserAssignment;
import org.usip.osp.persistence.BaseUser;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.persistence.SchemaInformationObject;

import java.util.*;

import org.apache.log4j.*;

/**
 * This utility class provides the methods necessary to send information via email to the players.
 */
 /* 
 *         This file is part of the USIP Open Simulation Platform.<br>
 * 
 *         The USIP Open Simulation Platform is free software; you can
 *         redistribute it and/or modify it under the terms of the new BSD Style
 *         license associated with this distribution.<br>
 * 
 *         The USIP Open Simulation Platform is distributed WITHOUT ANY
 *         WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *         FITNESS FOR A PARTICULAR PURPOSE. <BR>
 * 
 */
public class Emailer {
	
	public static final String NOREPLY_EMAIL = "noreply@opensimplatform.org";

	/**
	 * Generic emailing method.
	 * 
	 * @param schema_id
	 * @param recipients
	 * @param subject
	 * @param message
	 * @param from
	 * @param cced
	 * @param bcced
	 */
	public static void postMail(final SchemaInformationObject sio, String to, String subject,
			String message, String from, Vector<String> cced,
			Vector<String> bcced) {

		Session session = getJavaxMailSessionForSchema(sio, true);

		Message msg = new MimeMessage(session);

		try {
			// set the from and to address
			InternetAddress addressFrom = new InternetAddress(from);
			msg.setFrom(addressFrom);

			InternetAddress[] addressTo = new InternetAddress[1];
			addressTo[0] = new InternetAddress(to);
			msg.setRecipients(Message.RecipientType.TO, addressTo);

			// /////////////////////////////////////////////////////////
			int ii = 0;
			if ((cced != null) && (cced.size() > 0)) {
				InternetAddress[] addressCC = new InternetAddress[cced.size()];
				for (Enumeration<String> e = cced.elements(); e
						.hasMoreElements();) {
					String s = e.nextElement();
					Logger.getRootLogger().debug("addressCC[ii] is " + addressCC[ii]); //$NON-NLS-1$
					addressCC[ii] = new InternetAddress(s);
				}
				msg.setRecipients(Message.RecipientType.CC, addressCC);
			}
			// /////////////////////////////////////////////////////////
			ii = 0;
			if ((bcced != null) && (bcced.size() > 0)) {
				InternetAddress[] addressBCC = new InternetAddress[bcced.size()];
				for (Enumeration<String> e = bcced.elements(); e
						.hasMoreElements();) {
					String s = e.nextElement();
					Logger.getRootLogger().debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!s is " + s); //$NON-NLS-1$
					addressBCC[ii] = new InternetAddress(s);

				}
				msg.setRecipients(Message.RecipientType.BCC, addressBCC);
			}
			// Setting the Subject and Content Type
			msg.setSubject(subject);
			msg.setContent(message, "text/plain"); //$NON-NLS-1$
			Transport.send(msg);

		} catch (Exception err) {
			err.printStackTrace();
		}
	}

	/**
	 * Sends the email to indicate that the simulation is ready to play.
	 * 
	 * @param schema
	 * @param to
	 * @param from
	 * @param cc
	 * @param bcc
	 * @param subject
	 * @param message
	 * @return
	 */
	public static String postSimReadyMail(String schema, String to,
			String from, String cc, String bcc, String subject, String message) {

		Vector cced = new Vector<String>();
		if (cc != null) {
			cced.add(cc);
		}

		Vector bcced = new Vector<String>();
		if (bcc != null) {
			bcced.add(bcc);
		}
		
		SchemaInformationObject sio = SchemaInformationObject.lookUpSIOByName(schema);

		postMail(sio, to, subject, message, from, cced, bcced);

		return "okay"; //$NON-NLS-1$
	}

	/**
	 * This method takes the SMTP information in the SchemaInformationObject and uses it to 
	 * create the javax.mail.Session object.
	 * 
	 * @param sio
	 * @param debug
	 * @return
	 */
	public static Session getJavaxMailSessionForSchema(final SchemaInformationObject sio,
			boolean debug) {
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp"); //$NON-NLS-1$ //$NON-NLS-2$
		props.put("mail.smtp.host", sio.getEmail_smtp()); //$NON-NLS-1$
		props.put("mail.smtp.auth", "true"); //$NON-NLS-1$ //$NON-NLS-2$

		Authenticator auth = new Authenticator() {

			public PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(sio.getSmtp_auth_user(), sio
						.getSmtp_auth_password());
			}

		};

		Session session = Session.getInstance(props, auth);

		session.setDebug(debug);

		return session;
	}

	/**
	 * Email sent from the simulation facilitator to the player. The simulation
	 * instructor may be copied on all emails as well. If the administrator has set a an
	 * email archive address it will also receive a copy.
	 * 
	 * @param from
	 * @param to
	 * @param emailText
	 */
	public static void sendWelcomeEmail(String schema, Long rs_id, String from, String emailText) {
	
		emailText = emailText
				.replace(
						"[web_site_location]", USIP_OSP_Properties.getValue("simulation_url") + "simulation/"); //$NON-NLS-1$
	
		Hashtable <Long, String> usersEmailed = new Hashtable<Long, String>();
	
		RunningSimulation rs = RunningSimulation.getById(schema, rs_id);
		
		for (ListIterator<UserAssignment> li = rs.getUser_assignments(schema,
				rs_id).listIterator(); li.hasNext();) {
			UserAssignment ua = li.next();
	
			// If we have not emailed this person yet.
			if (usersEmailed.get(ua.getUser_id()) == null) { 
	
				String this_guys_emailText = emailText;
	
				// /////////////////////////////////////
				MultiSchemaHibernateUtil.beginTransaction(
						MultiSchemaHibernateUtil.principalschema, true);
				BaseUser bu = (BaseUser) MultiSchemaHibernateUtil.getSession(
						MultiSchemaHibernateUtil.principalschema, true).get(
						BaseUser.class, ua.getUser_id());
				MultiSchemaHibernateUtil
						.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);
				// ///////////////////////////////////////
	
				this_guys_emailText = this_guys_emailText.replace(
						"[username]", bu.getUsername()); //$NON-NLS-1$
	
				String fullEmail;
	
				if ((bu.getFull_name() != null)
						&& (bu.getFull_name().trim().length() > 0)) {
					fullEmail = "Dear " + bu.getFull_name() + ",\r\n"; //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					fullEmail = "Dear Player, " + "\r\n"; //$NON-NLS-1$ //$NON-NLS-2$
				}
	
				fullEmail += this_guys_emailText;
	
				Logger.getRootLogger().debug("emailing : " + bu.getUsername()); //$NON-NLS-1$
	
				String cc = null;
				String bcc = from;
	
				postSimReadyMail(schema, bu.getUsername(), from, cc,
						bcc, "Simulation Starting", fullEmail); //$NON-NLS-1$
	
				usersEmailed.put(ua.getUser_id(), "set");
	
			}
	
		}
	
	}

}
