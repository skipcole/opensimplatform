package org.usip.osp.communications;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

import java.util.Date;
import java.util.StringTokenizer;

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
@Table(name = "ALERTS")
@Proxy(lazy=false)
public class Alert {

	/** This alert is of an undefined type.*/
	public static final int TYPE_UNDEFINED = 0;
	
	/** An announcement has been made and may be seen on the announcement page.*/
	public static final int TYPE_ANNOUNCEMENT = 1;
	
	/** The phase of the simulation has changed. */
	public static final int TYPE_PHASECHANGE = 2;
	
	/** New news is available from a news source accessible to the player. */
	public static final int TYPE_NEWS = 3;
	
	/** An event for the player. */
	public static final int TYPE_EVENT = 4;
	
    /** Database id of this Alert. */
	@Id
	@GeneratedValue
	@Column(name = "ALERT_ID")
    private Long id;

	@Column(name = "ALERT_TYPE")
	private int type = 0;
	
    @Lob
    private String alertMessage = "";
	
    @Lob
    private String alertPopupMessage = "";
    
    @Lob
    private String alertEmailMessage = "";
    
    
	@Column(name="ALERT_TIME", columnDefinition="datetime") 	
	@GeneratedValue
	private Date timeOfAlert;
	
	@Column(name="SPEC_TARGS")
	/** If this alert is only for specific actors (targets). */
	private boolean specific_targets = false;

	@Column(name="THE_SPEC_TARGS")
	/** a comma separated list of the actor ids for whom this alert is for.*/
	private String the_specific_targets = "";
	
	public boolean isSpecific_targets() {
		return specific_targets;
	}

	public void setSpecific_targets(boolean specific_targets) {
		this.specific_targets = specific_targets;
	}

	public String getThe_specific_targets() {
		return the_specific_targets;
	}

	public void setThe_specific_targets(String the_specific_targets) {
		this.the_specific_targets = the_specific_targets;
	}

	public Alert(){
		
		timeOfAlert = new java.util.Date();
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTimeOfAlert() {
		return timeOfAlert;
	}

	public void setTimeOfAlert(Date timeOfAlert) {
		this.timeOfAlert = timeOfAlert;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * Returns true if this actor is included in the distribution list for this 
	 * alert.
	 * 
	 * @param actor_id
	 * @return
	 */
	public boolean checkActor(Long actor_id){
		
		// If this is an alert for everyone, just return true.
		if (!(this.specific_targets)){
			return true;
		} else {
			
			String a_id = actor_id.toString();
			
			StringTokenizer str = new StringTokenizer(this.the_specific_targets, ",");
			
	        while (str.hasMoreTokens()) {
	            if(str.nextToken().trim().equalsIgnoreCase(a_id)){
	            	return true;
	            }
	        }
	        
	        return false;
		}
	}

	public String getAlertMessage() {
		return alertMessage;
	}

	public void setAlertMessage(String alertMessage) {
		this.alertMessage = alertMessage;
	}

	public String getAlertPopupMessage() {
		return alertPopupMessage;
	}

	public void setAlertPopupMessage(String alertPopupMessage) {
		this.alertPopupMessage = alertPopupMessage;
	}

	public String getAlertEmailMessage() {
		return alertEmailMessage;
	}

	public void setAlertEmailMessage(String alertEmailMessage) {
		this.alertEmailMessage = alertEmailMessage;
	}
	
}
