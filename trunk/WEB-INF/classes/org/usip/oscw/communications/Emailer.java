package org.usip.oscw.communications;

import javax.mail.*;
import javax.mail.internet.*;

import org.usip.oscw.baseobjects.USIP_OSCW_Properties;
import org.usip.oscw.persistence.MultiSchemaHibernateUtil;
import org.usip.oscw.persistence.SchemaInformationObject;

import java.util.*;

/**
 * @author Ronald "Skip" Cole
 * 
 * This file is part of the USIP Online Simulation Platform.<br>
 * 
 * The USIP Online Simulation Platform is free software; you can
 * redistribute it and/or modify it under the terms of the new BSD Style license
 * associated with this distribution.<br>
 * 
 * The USIP Online Simulation Platform is distributed WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. <BR>
 * 
 */
public class Emailer {

    public static String simulation_url = "";

    static {
        try {

            simulation_url = USIP_OSCW_Properties.getValue("simulation_url");

        } catch (Exception e) {
            System.out
                    .println("Problem getting simulation URL to include in email to participants.");
        }
    }

    public static void main(String args[]) {

        Vector toV = new Vector();
        toV.add("scole@usip.org");

        String p = "a,b,c,";
        toV = listToVector(p);

        System.out.println(toV.size());
        // postMail(toV, "test", "words of message", "scole@usip.org", null,
        // null);
    }

    public static Vector listToVector(String inputList) {

        Vector returnV = new Vector();

        StringTokenizer st = new StringTokenizer(inputList, ",");

        while (st.hasMoreTokens()) {
            returnV.add(st.nextToken().trim());
        }

        return returnV;

    }
    
    /**
     * Returns the schema information object (which contains information on sending out emails
     * out) from the principal schema.
     * @param schema_id
     * @return
     */
    public static SchemaInformationObject getSIO(Long schema_id){
    	
        MultiSchemaHibernateUtil.beginTransaction(
                MultiSchemaHibernateUtil.principalschema, true);

        SchemaInformationObject sio = (SchemaInformationObject) MultiSchemaHibernateUtil
                .getSession(MultiSchemaHibernateUtil.principalschema, true)
                .get(SchemaInformationObject.class, schema_id);

        MultiSchemaHibernateUtil
                .commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);
        
        return sio;
    }

    /**
     * 
     * @param schema_id
     * @param recipients
     * @param subject
     * @param message
     * @param from
     * @param cced
     * @param bcced
     */
    public static void postMail(Long schema_id, Vector <String> recipients,
            String subject, String message, String from, Vector <String> cced,
            Vector <String> bcced) {

    	final SchemaInformationObject sio = getSIO(schema_id);
    	
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", sio.getEmail_smtp());
        props.put("mail.smtp.auth", "true");

        // Authenticator auth = em.new SMTPAuthenticator();
        Authenticator auth = new Authenticator() {

            public PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(sio.getSmtp_auth_user(), sio
                        .getSmtp_auth_password());
            }

        };
        Session session = Session.getDefaultInstance(props, auth);

        // Session session = Session.getDefaultInstance(props, null);
        session.setDebug(false);

        Message msg = new MimeMessage(session);

        try {
            // set the from and to address
            InternetAddress addressFrom = new InternetAddress(from);
            msg.setFrom(addressFrom);

            // //////////////////////////////////////////////////////// 
            int ii = 0;
            InternetAddress[] addressTo = new InternetAddress[recipients.size()];
            for (Enumeration<String> e = recipients.elements(); e
                    .hasMoreElements();) {
                String s = (String) e.nextElement();
                addressTo[ii] = new InternetAddress(s);
            }
            msg.setRecipients(Message.RecipientType.TO, addressTo);
            // /////////////////////////////////////////////////////////   
            ii = 0;
            InternetAddress[] addressCC = new InternetAddress[cced.size()];
            for (Enumeration<String> e = cced.elements(); e.hasMoreElements();) {
                String s = (String) e.nextElement();
                addressCC[ii] = new InternetAddress(s);
            }
            msg.setRecipients(Message.RecipientType.CC, addressCC);
            // /////////////////////////////////////////////////////////
            ii = 0;
            InternetAddress[] addressBCC = new InternetAddress[bcced.size()];
            for (Enumeration<String> e = bcced.elements(); e.hasMoreElements();) {
                String s = (String) e.nextElement();
                addressBCC[ii] = new InternetAddress(s);
            }
            msg.setRecipients(Message.RecipientType.BCC, addressBCC);

            // Setting the Subject and Content Type
            msg.setSubject(subject);
            msg.setContent(message, "text/plain");
            Transport.send(msg);

        } catch (Exception err) {
            err.printStackTrace();
        }
    }
    
    public static String postSimReadyMail(Long schema_id, String to,
            String from, String cc, String bcc, String subject, String message) {
    	
    	Vector recipients = new Vector<String>();
    	recipients.add(to);
    	
    	Vector cced = new Vector<String>();
    	cced.add(cc);
    	
    	Vector bcced = new Vector<String>();
    	recipients.add(bcc);
    	
    	postMail(schema_id, recipients,subject, message, from, cced, bcced);
    	
    	return "okay";
    }

    /**
     * 
     * @param schema_id
     * @param to
     * @param from
     * @param cc
     * @param subject
     * @param message
     * @return
     */
    public static String OLDpostSimReadyMail(Long schema_id, String to,
            String from, String cc, String bcc,  String message) {

    	final SchemaInformationObject sio = getSIO(schema_id);

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", sio.getEmail_smtp());
        props.put("mail.smtp.auth", "true");

        Authenticator auth = new Authenticator() {

            public PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(sio.getSmtp_auth_user(), sio
                        .getSmtp_auth_password());
            }

        };

        Session session = Session.getInstance(props, auth);

        session.setDebug(false);

        Message msg = new MimeMessage(session);

        try {
            // set the from and to address

            InternetAddress addressFrom = new InternetAddress(from);
            msg.setFrom(addressFrom);

            // /////////////////////////////////////////////////////////
            InternetAddress[] addressTo = new InternetAddress[1];
            addressTo[0] = new InternetAddress(to);
            msg.setRecipients(Message.RecipientType.TO, addressTo);
            // /////////////////////////////////////////////////////////
            if ((cc != null) && (cc.length() > 0)) {
                InternetAddress[] addressCC = new InternetAddress[1];
                addressCC[0] = new InternetAddress(cc);
                msg.setRecipients(Message.RecipientType.CC, addressCC);
            }
            // /////////////////////////////////////////////////////////
            if ((bcc != null) && (bcc.length() > 0)) {
                InternetAddress[] addressBCC = new InternetAddress[1];
                addressBCC[0] = new InternetAddress(bcc);
                msg.setRecipients(Message.RecipientType.BCC, addressBCC);
            }
            // /////////////////////////////////////////////////////////

            if (sio.getEmail_archive_address().trim().length() > 0) {
                InternetAddress[] addressBCCTo = new InternetAddress[1];
                addressBCCTo[0] = new InternetAddress(sio
                        .getEmail_archive_address().trim());
                msg.setRecipients(Message.RecipientType.BCC, addressBCCTo);
            }

            // Setting the Subject and Content Type
            msg.setSubject(subject);
            msg.setContent(message, "text/plain");
            
            //System.out.println(message);
            Transport.send(msg);

        } catch (Exception err) {
            err.printStackTrace();
            return err.getMessage();
        }

        return "okay";
    }
}