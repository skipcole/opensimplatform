package org.usip.osp.communications;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Proxy;
import org.usip.osp.networking.AuthorFacilitatorSessionObject;
import org.usip.osp.networking.PlayerSessionObject;
import org.usip.osp.networking.USIP_OSP_Cache;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.apache.log4j.*;

/**
 * This class represents a single line in a chat conversation.
 *
 *
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
@Table(name = "CHAT_LINES")
@Proxy(lazy=false)
public class ChatLine implements Comparable{

    /** Unique id of this chat line. Also used for indexing (thus assuming ids only go up). */
	@Id 
	@GeneratedValue
	@Column(name = "CHATLINE_ID")
    private Long id;
	
    /** Identifier of the game this chat line is associated with*/
	@Column(name = "RUNNING_SIM_ID")
    private Long running_sim_id;
	
	/** Phase in which the chat line occured. */
	private Long phaseId;
	
	@Column(name = "CONV_ID")
    private Long conversation_id;
    
    /** Id of the actor making this chat line. */
    private Long fromActor;
    
    /** Id of the user making this chat line. */
    private Long fromUser;
    
    /** Body of the message text. */
    @Lob
    private String msgtext = ""; //$NON-NLS-1$
    
	@Column(name="MSG_DATE", columnDefinition="datetime") 	
	private java.util.Date msgDate;
	
    /** Indicates if a chat line has been read or not. */
    private boolean hasBeenRead;
	
	@Transient
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yy HH:mm a"); //$NON-NLS-1$
	
    /**
     * Packages a line with information (index, from id, and message payload) with
     * expected delimiters.
     * 
     * @return
     */
    public String packageMe(){
        
        String me = this.id + "_" + this.fromActor + "_"; //$NON-NLS-1$ //$NON-NLS-2$
        
        me = me + "xyxyx_" + this.msgtext; //$NON-NLS-1$
        
        return me;
    }
    
    /**
     * Packages a line with information (index, from id, and message payload) with
     * expected delimiters.
     * 
     * @return	Returns the chatline packaged in xml.
     */
    public String packageIntoXML(PlayerSessionObject afso, HttpServletRequest request){
    	
    	String time_string = this.sdf.format(this.msgDate);
    	
    	String returnString = "<message>"; //$NON-NLS-1$
    	
    	String actorName = USIP_OSP_Cache.getActorName(afso.schema, afso.sim_id, afso.getRunningSimId(), request, this.fromActor);
    	
    	returnString += "     <conversation>" + this.conversation_id + "</conversation>"; //$NON-NLS-1$ //$NON-NLS-2$
    	returnString += "     <time>" + time_string + "</time>"; //$NON-NLS-1$ //$NON-NLS-2$
    	returnString += "     <id>" + this.id + "</id>"; //$NON-NLS-1$ //$NON-NLS-2$
    	returnString += "     <author>" + actorName + "</author>"; //$NON-NLS-1$ //$NON-NLS-2$
    	returnString += "     <text>" + this.msgtext + "</text>"; //$NON-NLS-1$ //$NON-NLS-2$
    	returnString += "</message>\r\n"; //$NON-NLS-1$
        
        return returnString;
    }
    
    /**
     * Zero argument constructor needed by hibernate.
     *
     */
    public ChatLine(){
    	this.msgDate = new java.util.Date();
    }
    
    public static void main(String args[]){
    	Logger.getRootLogger().debug("Hi handsome"); //$NON-NLS-1$
    	ChatLine cl = new ChatLine("1", "1", "1", "1", "cargo cult"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
    	cl.saveMe("test"); //$NON-NLS-1$
    	Logger.getRootLogger().debug("done"); //$NON-NLS-1$
    	
    }
    
    public void saveMe(String schema){

		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
    }

    /**
     * 
     * @param uid	User id of this chatline
     * @param rid	Running Session id of this chatline
     * @param aid	Actor id of this chatline
     * @param cid	Conversation id of this chatline
     * @param text	Text of this chatline.
     */
    public ChatLine(String uid, String rid, String aid, String cid, String text){
        
    	this.msgDate = new java.util.Date();
    	
    	this.fromUser = new Long(uid);
    	
        this.running_sim_id = new Long(rid);
        
        this.fromActor = new Long(aid);
        
        this.conversation_id = new Long(cid);
        
        this.msgtext = text;
        
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRunning_sim_id() {
		return this.running_sim_id;
	}

	public void setRunning_sim_id(Long running_sim_id) {
		this.running_sim_id = running_sim_id;
	}

	public Long getConversation_id() {
		return this.conversation_id;
	}

	public void setConversation_id(Long conversation_id) {
		this.conversation_id = conversation_id;
	}

	public Long getFromActor() {
		return this.fromActor;
	}

	public void setFromActor(Long fromChatter) {
		this.fromActor = fromChatter;
	}

	public String getMsgtext() {
		return this.msgtext;
	}

	public void setMsgtext(String msgtext) {
		this.msgtext = msgtext;
	}

	public Long getFromUser() {
		return this.fromUser;
	}

	public void setFromUser(Long fromUser) {
		this.fromUser = fromUser;
	}

	public Date getMsgDate() {
		return this.msgDate;
	}

	public void setMsgDate(Date msgDate) {
		this.msgDate = msgDate;
	}

	public boolean isHasBeenRead() {
		return hasBeenRead;
	}

	public void setHasBeenRead(boolean hasBeenRead) {
		this.hasBeenRead = hasBeenRead;
	}

	@Override
	public int compareTo(Object arg0) {
		
		ChatLine cl = (ChatLine) arg0;
		
		return cl.id.compareTo(this.id);
		
	}
    
}
