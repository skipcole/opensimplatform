package org.usip.osp.communications;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This class represents an alert, which can be of many different types, sent to
 * a player or players.
 */
/*
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it
 * and/or modify it under the terms of the new BSD Style license associated with
 * this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. <BR>
 */
@Entity
@Table(name = "ALERTS")
@Proxy(lazy = false)
public class Alert implements EventInterface {

	/** This alert is of an undefined type. */
	public static final int TYPE_UNDEFINED = 0;

	/** An announcement has been made and may be seen on the announcement page. */
	public static final int TYPE_RUN_ENABLED = 1;

	/** An announcement has been made and may be seen on the announcement page. */
	public static final int TYPE_ANNOUNCEMENT = 2;

	/** New news is available from a news source accessible to the player. */
	public static final int TYPE_NEWS = 3;

	/** An event for the player. */
	public static final int TYPE_EVENT = 4;

	/** The phase of the simulation has changed. */
	public static final int TYPE_PHASECHANGE = 5;

	/** An incoming for the player. */
	public static final int TYPE_MEMO = 6;

	/**
	 * Multiple events have occured for the user. (Don't pester with continuous
	 * pop-up windows.
	 */
	public static final int TYPE_MULTIPLE = 99;

	/** Database id of this Alert. */
	@Id
	@GeneratedValue
	@Column(name = "ALERT_ID")
	private Long id;

	@Column(name = "ALERT_TYPE")
	private int type = 0;

	@Lob
	private String alertMessage = ""; //$NON-NLS-1$

	@Lob
	private String alertPopupMessage = ""; //$NON-NLS-1$

	@Lob
	private String alertEmailMessage = ""; //$NON-NLS-1$

	@Column(name = "ALERT_TIME", columnDefinition = "datetime")
	@GeneratedValue
	private Date timeOfAlert;

	@Column(name = "SPEC_TARGS")
	/* If this alert is only for specific actors (targets). */
	private boolean specific_targets = false;

	@Column(name = "THE_SPEC_TARGS")
	/* a comma separated list of the actor ids for whom this alert is for. */
	private String the_specific_targets = ""; //$NON-NLS-1$

	private Long sim_id;

	private Long running_sim_id;

	public Long getRunning_sim_id() {
		return this.running_sim_id;
	}

	public void setRunning_sim_id(Long running_sim_id) {
		this.running_sim_id = running_sim_id;
	}

	public boolean isSpecific_targets() {
		return this.specific_targets;
	}

	public void setSpecific_targets(boolean specific_targets) {
		this.specific_targets = specific_targets;
	}

	public String getThe_specific_targets() {
		return this.the_specific_targets;
	}

	public void setThe_specific_targets(String the_specific_targets) {
		this.the_specific_targets = the_specific_targets;
	}

	public Alert() {

		this.timeOfAlert = new java.util.Date();

	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTimeOfAlert() {
		return this.timeOfAlert;
	}

	public void setTimeOfAlert(Date timeOfAlert) {
		this.timeOfAlert = timeOfAlert;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Returns a human readable translation of the alert type code.
	 * 
	 * @return
	 */
	public String getTypeText() {
		
		return getTypeText(this.type);

	}
	
	/**
	 * 
	 * @param a_type
	 * @return
	 */
	public static String getTypeText(int a_type){
		switch (a_type) {
		
		case TYPE_ANNOUNCEMENT:
			return "announcement";
		case TYPE_EVENT:
			return "event";
		case TYPE_MEMO:
			return "memo";
		case TYPE_NEWS:
			return "news";
		case TYPE_PHASECHANGE:
			return "phase_change";
		case Alert.TYPE_MULTIPLE:
			return "multiple";
		}

		return "unknown";
	}

	/**
	 * Returns true if this actor is included in the distribution list for this
	 * alert.
	 * 
	 * @param actor_id
	 * @return
	 */
	public boolean checkActor(Long actor_id) {

		// If this is an alert for everyone, just return true.
		if (!(this.specific_targets)) {
			return true;
		} else {

			String a_id = actor_id.toString();

			StringTokenizer str = new StringTokenizer(this.the_specific_targets, ","); //$NON-NLS-1$

			while (str.hasMoreTokens()) {
				if (str.nextToken().trim().equalsIgnoreCase(a_id)) {
					return true;
				}
			}

			return false;
		}
	}

	public String getAlertMessage() {
		return this.alertMessage;
	}

	public void setAlertMessage(String alertMessage) {
		this.alertMessage = alertMessage;
	}

	public String getAlertPopupMessage() {
		return this.alertPopupMessage;
	}

	public void setAlertPopupMessage(String alertPopupMessage) {
		this.alertPopupMessage = alertPopupMessage;
	}

	public String getAlertEmailMessage() {
		return this.alertEmailMessage;
	}

	public void setAlertEmailMessage(String alertEmailMessage) {
		this.alertEmailMessage = alertEmailMessage;
	}

	/** Saves an alert. */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}

	/**
	 * Returns all of the alerts for this running simulation.
	 * 
	 * @param schema
	 * @param running_sim_id
	 * @return
	 */
	public static List<Alert> getAllForRunningSim(String schema, Long running_sim_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<Alert> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from Alert where running_sim_id = " + running_sim_id).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	public static void main(String args[]) {
		System.out.println("Hello World");
		System.out.println(getHighestAlertNumber("test", new Long(1)));

	}

	/**
	 * Get the highest Alert number for a running simulation
	 */
	public static Long getHighestAlertNumber(String schema, Long running_sim_id) {

		Long returnLong = new Long(0);

		String query = "select max(id) from Alert where running_sim_id = " + running_sim_id;

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<Long> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(query).list();

		if (returnList != null) {
			returnLong = (Long) returnList.get(0);

		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnLong;
	}

	/**
	 * Gets all Alerts for a running simulation above the alert number passed in. 
	 * @param schema
	 * @param running_sim_id
	 * @param myHighestChangeNumber
	 * @return
	 */
	public static List<Alert> getAllForRunningSimAboveNumber(String schema, Long running_sim_id,
			Long myHighestAlertNumber) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<Alert> returnList = MultiSchemaHibernateUtil.getSession(schema)
				.createQuery(
						"from Alert where running_sim_id = " + running_sim_id
								+ " AND ALERT_ID > " + myHighestAlertNumber).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getSim_id() {
		return sim_id;
	}

	@Override
	public Date getEventEndTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEventMsgBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getEventStartTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEventTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getEventType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static String getMultipleAlertText() {
		
		return "Multiple Events have occured. Please check your environment carefully.";
	}

}
