package org.usip.osp.communications;

import java.util.*;
import java.util.Date;
import java.sql.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Proxy;
import org.usip.osp.networking.ParticipantSessionObject;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.persistence.MysqlDatabase;

/**
 * @author Ronald "Skip" Cole<br />
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
public class ChatLine {

    /** Unique id of this chat line. Also used for indexing (thus assuming ids only go up). */
	@Id 
	@GeneratedValue
	@Column(name = "CHATLINE_ID")
    private Long id;
	
    /** Identifier of the game this chat line is associated with*/
	@Column(name = "RUNNING_SIM_ID")
    private Long running_sim_id;
	
	@Column(name = "CONV_ID")
    private Long conversation_id;
    
    /** Id of the actor making this chat line. */
    protected Long fromActor;
    
    /** Id of the user making this chat line. */
    private Long fromUser;
    
    /** Body of the message text. */
    @Lob
    protected String msgtext = "";
    
	@Column(name="MSG_DATE", columnDefinition="datetime") 	
	private java.util.Date msgDate;
	
	@Transient
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yy HH:mm a");
	
    /**
     * Packages a line with information (index, from id, and message payload) with
     * expected delimiters.
     * 
     * @return
     */
    public String packageMe(){
        
        String me = id + "_" + fromActor + "_";
        
        me = me + "xyxyx_" + msgtext;
        
        return me;
    }
    
    /**
     * Packages a line with information (index, from id, and message payload) with
     * expected delimiters.
     * 
     * @return	Returns the chatline packaged in xml.
     */
    public String packageIntoXML(ParticipantSessionObject pso, HttpServletRequest request){
    	
    	String time_string = sdf.format(this.msgDate);
    	
    	String returnString = "<message>";
    	
    	String actorName = pso.getActorName(request, fromActor);
    	
    	returnString += "     <conversation>" + conversation_id + "</conversation>";
    	returnString += "     <time>" + time_string + "</time>";
    	returnString += "     <id>" + id + "</id>";
    	returnString += "     <author>" + actorName + "</author>";
    	returnString += "     <text>" + msgtext + "</text>";
    	returnString += "</message>\r\n";
        
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
    	System.out.println("Hi handsome");
    	ChatLine cl = new ChatLine("1", "1", "1", "1", "cargo cult");
    	cl.saveMe("test");
    	System.out.println("done");
    	
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
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRunning_sim_id() {
		return running_sim_id;
	}

	public void setRunning_sim_id(Long running_sim_id) {
		this.running_sim_id = running_sim_id;
	}

	public Long getConversation_id() {
		return conversation_id;
	}

	public void setConversation_id(Long conversation_id) {
		this.conversation_id = conversation_id;
	}

	public Long getFromActor() {
		return fromActor;
	}

	public void setFromActor(Long fromChatter) {
		this.fromActor = fromChatter;
	}

	public String getMsgtext() {
		return msgtext;
	}

	public void setMsgtext(String msgtext) {
		this.msgtext = msgtext;
	}

	public Long getFromUser() {
		return fromUser;
	}

	public void setFromUser(Long fromUser) {
		this.fromUser = fromUser;
	}

	public Date getMsgDate() {
		return msgDate;
	}

	public void setMsgDate(Date msgDate) {
		this.msgDate = msgDate;
	}
    
}
