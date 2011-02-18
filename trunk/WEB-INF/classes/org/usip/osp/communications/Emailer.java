package org.usip.osp.communications;

import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

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


}
