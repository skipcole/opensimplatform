package org.usip.oscw.communications;

import java.util.*;
import java.sql.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.usip.oscw.persistence.MysqlDatabase;

/**
 * @author Ronald "Skip" Cole
 *
 * This file is part of the USIP Online Simulation Platform.<br>
 * 
 * The USIP Online Simulation Platform is free software; you can redistribute it and/or
 * modify it under the terms of the new BSD Style license associated with this
 * distribution.<br>
 * 
 * The USIP Online Simulation Platform is distributed WITHOUT ANY WARRANTY; without
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
     * @return
     */
    public String packageIntoXML(){
    	
    	String returnString = "<message>";
    	
    	returnString += "     <conversation>" + conversation_id + "</conversation>";
    	returnString += "     <id>" + id + "</id>";
    	returnString += "     <author>" + fromActor + "</author>";
    	returnString += "     <text>" + msgtext + "</text>";
    	returnString += "</message>";
        
        return returnString;
    }
    
    /**
     * Zero argument constructor needed by hibernate.
     *
     */
    public ChatLine(){
        
    }
    /**
     * 
     * @param rid
     * @param a
     * @param t
     */
    public ChatLine(String uid, String rid, String a, String t){
        
    	this.fromUser = new Long(uid);
    	
        this.running_sim_id = new Long(rid);
        
        this.fromActor = new Long(a);
        
        this.msgtext = t;
        
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
    
}
