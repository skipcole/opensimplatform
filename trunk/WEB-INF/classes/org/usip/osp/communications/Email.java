package org.usip.osp.communications;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.baseobjects.SimulationPhase;
import org.usip.osp.baseobjects.User;
import org.usip.osp.baseobjects.UserAssignment;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.persistence.SchemaInformationObject;

/**
 * This class represents an in simulation email.
 * It is in development and is currently not used anywhere.
 */
/*
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it and/or
 * modify it under the terms of the new BSD Style license associated with this
 * distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. <BR>
 * 
 */
@Entity
@Proxy(lazy=false)
public class Email {
	
	/**
	 * Zero argument constructor required by hibernate.
	 */
	public Email(){
		
	}
	
	public Email(Long fromUser, String fromName, String subject, String bodyText, Long s_id, 
			Long rs_id){
		this.fromUser = fromUser;
		this.fromUserName = fromName;
		this.subjectLine = subject;
		this.msgtext = bodyText;
		this.sim_id = s_id;
		this.running_sim_id = rs_id;
	}
	
	public static Email getRawBlankSimInvite(){
		Email email = new Email();
		
		email.setSubjectLine("Simulation Starting");
		
		String msgText = "	     You are invited to enter a simulation.";
		msgText += "	     Please go to the website [web_site_location] to enter.";
		msgText += "	     Your username is [username]. Your password is either the one you entered when registering on the system, or the one that your instructor has assigned you.";
		msgText += "Please confirm that you have received this email by going to this website [confirm_receipt]";
		msgText += "Enjoy!";
		
		email.setMsgtext(msgText);
 
		return email;
	}
	
	/**
	 * Saves this object back to the database.
	 * 
	 * @param schema
	 */
	public void saveMe(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}
	
	/**
	 * Pulls the Email out of the database base on its id and schema.
	 * 
	 * @param schema
	 * @param email_id
	 * @return
	 */
	public static Email getById(String schema, Long email_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		Email email = (Email) MultiSchemaHibernateUtil.getSession(schema).get(Email.class, email_id);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return email;

	}

    /** Unique id of this email line. Also used for indexing (thus assuming ids only go up). */
	@Id 
	@GeneratedValue
    private Long id;
	
	private Long sim_id;
	
    /** Identifier of the game this chat line is associated with*/
	@Column(name = "RUNNING_SIM_ID")
    private Long running_sim_id;
	
	/** If this is a simulation invitation mail.  */
	private boolean simInvitationEmail = false;
	
	/** If this is a prototypical invite. */
	private boolean invitePrototype = false;
	
	/** Id of the thread that this email may exist in. */
    private Long thread_id;
    
    /** Id of the actor making this chat line. */
	private Long fromActor;
	
	/** Name of the sending actor. */
	private String fromActorName = ""; //$NON_NSL-1$
	
	/** String list of actors to whom this email has been sent to. */
	private String toActors = "";
	
	/** String list of actors to whom this email has been cc'd to. */
	private String ccActors = "";
	
	/** String list of actors to whom this email has been bcc'd to. */
	private String bccActors = "";
	
    /** Id of the user making this email. */
    private Long fromUser;
    
    /** Email address of the sending user. */
    private String fromUserName = "";
    

    
    /** Subject line of this email. */
    private String subjectLine = ""; //$NON-NLS-1$
    
    /** Body of the message text. */
    @Lob
    private String msgtext = ""; //$NON-NLS-1$
    
	@Column(name="MSG_DATE", columnDefinition="datetime") 	
	private java.util.Date msgDate;

	/** Indicates if this email is a forward of another email. */
	private boolean forward_email = false;
	
	/** Indicates if this email is to be sent externally also. */
	private boolean sendInRealWorld = true;
	
	public boolean isSendInRealWorld() {
		return sendInRealWorld;
	}

	public void setSendInRealWorld(boolean sendInRealWorld) {
		this.sendInRealWorld = sendInRealWorld;
	}
	
	public boolean isSimInvitationEmail() {
		return simInvitationEmail;
	}

	public void setSimInvitationEmail(boolean simInvitationEmail) {
		this.simInvitationEmail = simInvitationEmail;
	}

	public boolean isInvitePrototype() {
		return invitePrototype;
	}

	public void setInvitePrototype(boolean invitePrototype) {
		this.invitePrototype = invitePrototype;
	}

	public static String getOrderByDesc() {
		return orderByDesc;
	}

	public static void setOrderByDesc(String orderByDesc) {
		Email.orderByDesc = orderByDesc;
	}

	public boolean isHasBeenSent() {
		return hasBeenSent;
	}

	/** Indicates if this email is a reply to another email. */
	private boolean reply_email = false;
	
	public boolean isReply_email() {
		return reply_email;
	}

	public void setReply_email(boolean reply_email) {
		this.reply_email = reply_email;
	}

	public boolean isForward_email() {
		return forward_email;
	}

	public void setForward_email(boolean forward_email) {
		this.forward_email = forward_email;
	}

	/** Indicates if message is a draft, or if it has been actually sent. */
	private boolean hasBeenSent = false;

	public boolean hasBeenSent() {
		return hasBeenSent;
	}

	public void setHasBeenSent(boolean hasBeenSent) {
		this.hasBeenSent = hasBeenSent;
	}
	
	/** Indicates if this email has been deleted. */
	private boolean email_deleted;

	public boolean isEmail_deleted() {
		return email_deleted;
	}

	public void setEmail_deleted(boolean email_deleted) {
		this.email_deleted = email_deleted;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSim_id() {
		return sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getRunning_sim_id() {
		return running_sim_id;
	}

	public void setRunning_sim_id(Long running_sim_id) {
		this.running_sim_id = running_sim_id;
	}

	public Long getThread_id() {
		return thread_id;
	}

	public void setThread_id(Long thread_id) {
		this.thread_id = thread_id;
	}

	public Long getFromActor() {
		return fromActor;
	}

	public void setFromActor(Long fromActor) {
		this.fromActor = fromActor;
	}

	public String getFromActorName() {
		return fromActorName;
	}

	public void setFromActorName(String fromActorName) {
		this.fromActorName = fromActorName;
	}

	public Long getFromUser() {
		return fromUser;
	}

	public void setFromUser(Long fromUser) {
		this.fromUser = fromUser;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getSubjectLine() {
		return subjectLine;
	}

	public void setSubjectLine(String subjectLine) {
		this.subjectLine = subjectLine;
	}

	public String getMsgtext() {
		return msgtext;
	}

	public void setMsgtext(String msgtext) {
		this.msgtext = msgtext;
	}

	public java.util.Date getMsgDate() {
		return msgDate;
	}

	public void setMsgDate(java.util.Date msgDate) {
		this.msgDate = msgDate;
	}
	
	
	private static String orderByDesc = " order by id desc " ;

	
	/**
	 * Returns all of the email directed to an actor during a simulation.
	 * 
	 * @param schema
	 * @param running_sim_id
	 * @param actorId
	 * @return
	 */
	public static List getAllForRunningSim(String schema, Long running_sim_id){
		
		ArrayList returnList = new ArrayList();
		
		if (running_sim_id == null){
			return returnList;
			
		}
		MultiSchemaHibernateUtil.beginTransaction(schema);

		String hqlString = "from Email where " +
				"running_sim_id = :rsid order by id";
		
		List tempList = MultiSchemaHibernateUtil.getSession(schema)
			.createQuery(hqlString)
			.setString("rsid", running_sim_id.toString())
			.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		for (ListIterator<Email> li = tempList.listIterator(); li.hasNext();) {
			Email this_er = li.next();
			
			if (this_er.hasBeenSent()){
				returnList.add(this_er);
			}
			
		}
		
		return returnList;
	
	}
	
	/**
	 * 
	 * @param schema
	 * @param sim_id
	 * @param running_sim_id
	 * @return
	 */
	public static List getPrototypeInvites(String schema, Long sim_id, Long running_sim_id){
		
		ArrayList returnList = new ArrayList();
		
		if (running_sim_id == null){
			return returnList;
			
		}
		MultiSchemaHibernateUtil.beginTransaction(schema);

		String hqlString = "from Email where sim_id = :sim_id and " +
				"running_sim_id = :running_sim_id and invitePrototype is true order by id";
		
		List tempList = MultiSchemaHibernateUtil.getSession(schema)
			.createQuery(hqlString)
			.setLong("sim_id", sim_id)
			.setLong("running_sim_id", running_sim_id)
			.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
	
	}

	/**
	 * Returns all of the email directed to an actor during a simulation.
	 * 
	 * @param schema
	 * @param running_sim_id
	 * @param actor_id
	 * @return
	 */
	public static List getAllTo(String schema, Long running_sim_id, Long actor_id){
		
		ArrayList returnList = new ArrayList();
		
		if ((running_sim_id == null) || (actor_id == null)){
			return returnList;
			
		}
		MultiSchemaHibernateUtil.beginTransaction(schema);

		String hqlString = "from EmailRecipients where " +
				"running_sim_id = :rsid and actor_id = :aid" + orderByDesc;
		
		List tempList = MultiSchemaHibernateUtil.getSession(schema)
			.createQuery(hqlString)
			.setString("rsid", running_sim_id.toString())
			.setString("aid", actor_id.toString())
			.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		for (ListIterator<EmailRecipients> li = tempList.listIterator(); li.hasNext();) {
			EmailRecipients this_er = li.next();
			
			Email email = Email.getById(schema, this_er.getEmail_id());
			
			if (email.hasBeenSent()){
				returnList.add(email);
			}
			
		}
		
		return returnList;
	
	}
	
	/**
	 * Returns all of the email directed to an actor during a simulation.
	 * 
	 * @param schema
	 * @param running_sim_id
	 * @param actor_id
	 * @return
	 */
	public static List getDraftsOrSent(String schema, Long running_sim_id, Long actor_id, boolean getSent){
		
		String getDrafts = "0";
		
		if (getSent){
			getDrafts = "1";
		}
		
		MultiSchemaHibernateUtil.beginTransaction(schema);

		String hqlString = "from Email where " +
				"running_sim_id = :rsid and fromActor = :aid and hasbeenSent = '" + getDrafts 
				+ "' and email_deleted = '0'" + orderByDesc;
		
		List returnList = MultiSchemaHibernateUtil.getSession(schema)
			.createQuery(hqlString)
			.setString("rsid", running_sim_id.toString())
			.setString("aid", actor_id.toString())
			.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
	
	}
	
	
	/**
	 * Returns all of the email directed to an actor during a simulation.
	 * 
	 * @param schema
	 * @param runningSimId
	 * @param actorId
	 * @return
	 */
	public static List getRecipientsOfAnEmail(String schema, Long email_id){
		
		MultiSchemaHibernateUtil.beginTransaction(schema);

		String hqlString = "from EmailRecipients where " + "email_id = :eid";
		
		List returnList = MultiSchemaHibernateUtil.getSession(schema)
			.createQuery(hqlString)
			.setString("eid", email_id.toString())
			.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		
		return returnList;
	
	}
	
	/**
	 * Returns all of the email directed to an actor during a simulation.
	 * 
	 * @param schema
	 * @param runningSimId
	 * @param actorId
	 * @return
	 */
	public static List getRecipientsOfSpecifiedType(String schema, Long email_id, int e_type){
		
		List starterList = getRecipientsOfAnEmail(schema, email_id);

		List returnList = new ArrayList();
		
		for (ListIterator<EmailRecipients> li = starterList.listIterator(); li.hasNext();) {
			EmailRecipients this_er = li.next();
			
			if (this_er.getRecipient_type() == e_type){
				returnList.add(this_er);
			}
		}
		
		return returnList;
	
	}
	
	public static String generateListOfRecipients(String schema, Long email_id, int e_type){
		
		String returnString = "  ";
		
		List starterList = getRecipientsOfSpecifiedType(schema, email_id, e_type);
		
		for (ListIterator<EmailRecipients> li = starterList.listIterator(); li.hasNext();) {
			EmailRecipients this_er = li.next();
		
			returnString += this_er.getActorName() + ", ";
			
		}
		
		// Remove final 2 characters.
		returnString = (String) returnString.subSequence(0, returnString.length() - 2);
		
		// Remove final 2 spaces.
		returnString = returnString.trim();
		
		return returnString;
		
	}
	public String getToActors() {
		return toActors;
	}

	public void setToActors(String toActors) {
		this.toActors = toActors;
	}

	public String getCcActors() {
		return ccActors;
	}

	public void setCcActors(String ccActors) {
		this.ccActors = ccActors;
	}

	public String getBccActors() {
		return bccActors;
	}

	public void setBccActors(String bccActors) {
		this.bccActors = bccActors;
	}

	/**
	 * TODO: this is defunct.
	 * 
	 * Puts the ">" symbol in front of each line of an email that is being replied to or forwarded.
	 * @param text
	 * @return
	 */
	public static String markTextAsReplyOrForwardText(String text){
		
		String returnString = "";
		
		String[] lines = text.split("\n");

        for (String this_line : lines) {
            System.out.println("Count is: " + this_line);
            returnString += ">" + this_line;
        }

		return returnString;
	}
	
	public void sendEmail(SchemaInformationObject sio){

		Vector to = new Vector();
		Vector cc = new Vector();
		Vector bcc = new Vector();
		
		Emailer.postMail(sio, to, this.getSubjectLine(), this.getMsgtext(), this.fromUserName, cc, bcc);
	}
	
	public boolean sendIt(String schema, Long running_sim_id){
		
		SchemaInformationObject sio = SchemaInformationObject.lookUpSIOByName(schema);
		
		List recipients = Email.getRecipientsOfAnEmail(schema, this.getId());
		
		for (ListIterator<EmailRecipients> li = recipients.listIterator(); li.hasNext();) {
			EmailRecipients this_er = li.next();
			
			// Get List of Users playing the actors.
			List uaList = UserAssignment.getAllForActorInARunningSim(schema, this_er.getActor_id(), running_sim_id);
			
			for (ListIterator<UserAssignment> li_ua = uaList.listIterator(); li_ua.hasNext();) {
				UserAssignment this_ua = li_ua.next();
				
				User user = User.getById(schema, this_ua.getUser_id());
				Emailer.postMail(sio, user.getUser_name(), this.getSubjectLine(), this.getMsgtext(), "noreply@opensimplatform.org", 
						null, null);
			}
			
		}
		
		return true;
		
	}
	
}
