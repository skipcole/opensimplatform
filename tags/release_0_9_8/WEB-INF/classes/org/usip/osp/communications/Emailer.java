package org.usip.osp.communications;

import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.http.HttpServletRequest;
import javax.activation.*;

import org.usip.osp.baseobjects.RunningSimulation;
import org.usip.osp.baseobjects.USIP_OSP_Properties;
import org.usip.osp.baseobjects.USIP_OSP_Util;
import org.usip.osp.baseobjects.UserAssignment;
import org.usip.osp.networking.PlayerSessionObject;
import org.usip.osp.networking.USIP_OSP_Cache;
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
	
	/**
	 * Main Email Sending Method of the program.
	 * 
	 * @param sio
	 * @param to
	 * @param subject
	 * @param message
	 * @param htmlMessage
	 * @param from
	 * @param replyTo
	 * @param cced
	 * @param bcced
	 */
	public static void postMail(final SchemaInformationObject sio, Vector<String> to, String subject,
			String message, String htmlMessage, String from, String replyTo, Vector<String> cced,
			Vector<String> bcced) {
		
		Session session = getJavaxMailSessionForSchema(sio, true);

		Message msg = new MimeMessage(session);

		try {
			
			// set the from and to address
			InternetAddress addressFrom = new InternetAddress(from);
			msg.setFrom(addressFrom);
			
			if ((replyTo != null) && (replyTo.length() > 0)){
				InternetAddress[] addressReplyTo = new InternetAddress[1];
				addressReplyTo[0] = new InternetAddress(replyTo);
				msg.setReplyTo(addressReplyTo);
			}
			
			// /////////////////////////////////////////////////////////
			// Abandoning, for now, the idea of setting multiple 'replyTo' addresses.
			// TODO we might want to revisit this later. SC
			
			// /////////////////////////////////////////////////////////
			int ii = 0;
			if ((to != null) && (to.size() > 0)) {
				InternetAddress[] addressTo = new InternetAddress[to.size()];
				for (Enumeration<String> e = to.elements(); e.hasMoreElements();) {
					String s = e.nextElement();
					Logger.getRootLogger().debug("addressTo[ii] is " + addressTo[ii]); //$NON-NLS-1$
					addressTo[ii] = new InternetAddress(s);
				}
				msg.setRecipients(Message.RecipientType.TO, addressTo);
			}

			// /////////////////////////////////////////////////////////
			ii = 0;
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
			if (htmlMessage != null){
				msg.setDataHandler(new DataHandler(new HTMLDataSource(htmlMessage)));
			}
			Transport.send(msg);

		} catch (Exception err) {
			err.printStackTrace();
		}
		
	}
	
	/**
	 *  Wrapper around the actual postMail that takes a single name instead of a vector of addresses
	 *  to send this email to.
	 *  
	 * @param sio
	 * @param to
	 * @param subject
	 * @param message
	 * @param htmlMessage
	 * @param from
	 * @param replyTo
	 * @param cced
	 * @param bcced
	 */
	public static void postMail(final SchemaInformationObject sio, String to, String subject,
			String message, String htmlMessage, String from, String replyTo, Vector<String> cced,
			Vector<String> bcced) {
		
		Vector <String> toVector = new Vector();
		toVector.add(to);
		
		postMail(sio,  toVector,  subject, message,  htmlMessage,  from,  replyTo,  cced, bcced);
		
	}
	
	/** Used for quick direct emails to users. */
	public static void quickPostMail(String schema, String to, String subject,
			String message, String from, String replyTo) {
		
		SchemaInformationObject sio = SchemaInformationObject.lookUpSIOByName(schema);
		
		String htmlMessage = message;
		
		Vector <String> toVector = new Vector();
		toVector.add(to);
		
		postMail(sio,  toVector,  subject, message,  htmlMessage,  from,  replyTo, 
				new Vector<String> (), new Vector<String> ());
		
	}
	
	/**
	 * 
	 * @param schema
	 * @param to
	 * @param subject
	 * @param message
	 * @param from
	 * @param cc
	 */
	public static void quickDirectPostMailToAdmin(SchemaInformationObject sio, String to, String subject,
			String message, String from, String cc) {
		
		String htmlMessage = message;
		
		Vector <String> toVector = new Vector<String>();
		toVector.add(to);
		
		Vector <String> ccVector = new Vector<String>();
		ccVector.add(cc);
		
		postMail(sio,  toVector,  subject, message,  htmlMessage,  from,  null, 
				ccVector, new Vector<String> ());
	}
	
    /*
     * I copied this code from http://www.vipan.com/htdocs/javamail.html.
     * Not sure on the rights. Can remove it later if needed -- Skip
     * 
     * Inner class to act as a JAF datasource to send HTML e-mail content
     */
    static class HTMLDataSource implements DataSource {
        private String html;

        public HTMLDataSource(String htmlString) {
            html = htmlString;
        }

        // Return html string in an InputStream.
        // A new stream must be returned each time.
        public InputStream getInputStream() throws IOException {
            if (html == null) throw new IOException("Null HTML");
            return new ByteArrayInputStream(html.getBytes());
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("This DataHandler cannot write HTML");
        }

        public String getContentType() {
            return "text/html";
        }

        public String getName() {
            return "JAF text/html dataSource to send e-mail only";
        }
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
	 * 
	 * @param request
	 * @return
	 */
	public static Email handleEmailWrite(HttpServletRequest request, PlayerSessionObject pso) {

		Email email = new Email();

		pso.forward_on = false;

		String reply_to = request.getParameter("reply_to");
		String forward_to = request.getParameter("forward_to");
		String queue_up = request.getParameter("queue_up");
		String email_clear = request.getParameter("email_clear");
		String email_delete_draft = request.getParameter("email_delete_draft");
		String sending_page = request.getParameter("sending_page");

		if ((reply_to != null) && (reply_to.equalsIgnoreCase("true"))) {
			String reply_id = request.getParameter("reply_id");
			String reply_to_actor_id = request
					.getParameter("reply_to_actor_id");

			Email emailIAmReplyingTo = Email
					.getById(pso.schema, new Long(reply_id));

			email.setId(null);
			email.setFromActor(pso.getActorId());

			email.setFromActorName(pso.getActorName());
			email.setHasBeenSent(false);
			email.setSubjectLine("Re: " + emailIAmReplyingTo.getSubjectLine());
			email.setMsgtext(" \n"
					+ markTextAsReplyOrForwardText(emailIAmReplyingTo
							.getMsgtext()));

			email.setSim_id(emailIAmReplyingTo.getSim_id());
			email.setRunning_sim_id(emailIAmReplyingTo.getRunning_sim_id());

			email.setReply_email(true);
			email.setThread_id(emailIAmReplyingTo.getId());
			email.saveMe(pso.schema);

			String reply_to_name = USIP_OSP_Cache.getActorName(pso.schema, pso.sim_id,
					pso.runningSimId, request, new Long(reply_to_actor_id));

			EmailRecipients er = new EmailRecipients(pso.schema, email.getId(),
					pso.runningSimId, pso.sim_id, new Long(reply_to_actor_id),
					reply_to_name, EmailRecipients.RECIPIENT_TO);

			pso.draft_email_id = email.getId();

		} else if ((forward_to != null)
				&& (forward_to.equalsIgnoreCase("true"))) {
			String forward_id = request.getParameter("forward_id");
			Email emailIAmReplyingTo = Email.getById(pso.schema, new Long(
					forward_id));

			email.setId(null);
			email.setFromActor(pso.getActorId());
			email.setFromActorName(pso.getActorName());
			email.setHasBeenSent(false);
			email.setSubjectLine("Fwd: " + emailIAmReplyingTo.getSubjectLine());
			email.setMsgtext(Email
					.markTextAsReplyOrForwardText(emailIAmReplyingTo
							.getMsgtext()));
			email.setReply_email(true);
			email.setThread_id(emailIAmReplyingTo.getId());
			email.saveMe(pso.schema);

			pso.draft_email_id = email.getId();

		} else if ((queue_up != null) && (queue_up.equalsIgnoreCase("true"))) {
			String email_id = request.getParameter("email_id");
			pso.draft_email_id = new Long(email_id);
			email = Email.getById(pso.schema, pso.draft_email_id);

		} else if (email_clear != null) {
			email = new Email();
			pso.draft_email_id = null;

		} else if ((email_delete_draft != null) && (pso.draft_email_id != null)) {
			email = Email.getById(pso.schema, pso.draft_email_id);
			email.setEmail_deleted(true);
			email.saveMe(pso.schema);

			email = new Email();
			pso.draft_email_id = null;

		} else if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("writing_email"))) {

			String add_recipient = request.getParameter("add_recipient");
			String email_save = request.getParameter("email_save");
			String email_send = request.getParameter("email_send");
			String remove_recipient = request.getParameter("remove_recipient");

			boolean doSave = false;

			if ((remove_recipient != null) || (add_recipient != null)
					|| (email_save != null) || (email_send != null)) {

				doSave = true;

			}

			if (doSave) {

				String form_email_id = request.getParameter("draft_email_id");
				if ((form_email_id != null)
						&& (!(form_email_id.equalsIgnoreCase("null")))) {
					pso.draft_email_id = new Long(form_email_id);
					email.setId(pso.draft_email_id);
				}

				if (email_send != null) {

					// must have gotten a draft id when adding a recipient for
					// the email.
					if (pso.draft_email_id != null) {

						pso.emailRecipients = Email.getRecipientsOfAnEmail(pso.schema,
								pso.draft_email_id, EmailRecipients.RECIPIENT_TO);

						if ((pso.emailRecipients != null)
								&& (pso.emailRecipients.size() > 0)) {

							email.setHasBeenSent(true);
							email.setSendDate(new java.util.Date());
							email.setToActors(Email.generateListOfRecipients(
									pso.schema, email.getId(),
									EmailRecipients.RECIPIENT_TO));

							String send_real_world = request
									.getParameter("send_real_world");

							if ((send_real_world != null)
									&& (send_real_world
											.equalsIgnoreCase("true"))) {
								email.setSendInRealWorld(true);
							} else {
								email.setSendInRealWorld(false);
							}

							pso.forward_on = true;
						} else {
							pso.errorMsg = "no recipients";
						}
					}
				}

				email.setFromActor(pso.getActorId());
				email.setFromActorName(pso.getActorName());
				email.setFromUser(pso.user_id);
				email.setMsgDate(new java.util.Date());
				email.setMsgtext(USIP_OSP_Util.cleanNulls(request
						.getParameter("email_text")));
				email.setHtmlMsgText(USIP_OSP_Util.cleanNulls(request
						.getParameter("email_text")));
				email.setRunning_sim_id(pso.runningSimId);
				email.setSim_id(pso.sim_id);
				email.setSubjectLine(USIP_OSP_Util.cleanNulls(request
						.getParameter("email_subject")));

				email.saveMe(pso.schema);
				pso.draft_email_id = email.getId();
				
				// Forward on here indicates that the email was sent
				if (pso.forward_on){
					email.alertPlayersOfNewEmail(pso, pso.schema, request);
				}

				// Send real world email if called for.
				if ((pso.forward_on) && (email.isSendInRealWorld())) {
					SchemaInformationObject sio = SchemaInformationObject
							.lookUpSIOByName(pso.schema);
					email.sendInGameEmailOutside(pso, sio);
				}

				if (add_recipient != null) {
					String email_rep = request.getParameter("email_recipient");

					if ((email_rep != null)
							&& (email_rep.toString().length() > 0)) {

						String aname = USIP_OSP_Cache.getActorName(pso.schema,
								pso.sim_id, pso.runningSimId, request, new Long(
										email_rep));

						@SuppressWarnings("unused")
						EmailRecipients er = new EmailRecipients(pso.schema,
								pso.draft_email_id, pso.runningSimId, pso.sim_id, new Long(
										email_rep), aname,
								EmailRecipients.RECIPIENT_TO);
					}
				} else if (remove_recipient != null) {
					String removed_email = request
							.getParameter("removed_email");
					if (removed_email != null) {
						EmailRecipients.removeMe(pso.schema,
								new Long(removed_email));
					}
				}
			} // end of if saving.
		} // end of if writing email.

		pso.setUpEligibleActors();

		return email;
	}

	/**
	 * Puts the ">" symbol in front of each line of an email that is being
	 * replied to or forwarded.
	 * 
	 * @param text
	 * @return
	 */
	public static String markTextAsReplyOrForwardText(String text) {

		String returnString = "";

		String[] lines = text.split("<br>");

		for (String this_line : lines) {
			returnString += "> " + this_line + "<br>";
		}

		return returnString;
	}

}