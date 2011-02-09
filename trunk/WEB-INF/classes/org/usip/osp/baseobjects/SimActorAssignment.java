package org.usip.osp.baseobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents the assignment of an actor into a simulation.
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
@Table(name = "SIM_ACTOR_ASSIGNMENT")
@Proxy(lazy = false)
public class SimActorAssignment implements ExportableObject{

	public static final int TYPE_UNDEFINED = 0;
	public static final int TYPE_REQUIRED = 1;
	public static final int TYPE_OPTIONAL = 2;

	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;

	/** Database id of this Simulation. */
	@Column(name = "SIM_ID")
	private Long sim_id;

	/** Unique id of this actor. */
	@Column(name = "ACTOR_ID")
	private Long actorId;

	private String actors_role = ""; //$NON-NLS-1$

	private int assignmentType = 0;

	private String assignmentPriority = "";

	private String assignmentNotes = ""; //$NON-NLS-1$

	private String actors_chat_color = ""; //$NON-NLS-1$

	private boolean active = true;
	
	private Long transitId;

	public String getAssignmentTypeDescriptor() {

		String returnString = "";

		switch (assignmentType) {
		case TYPE_UNDEFINED:
			returnString = "";
			break;
		case TYPE_REQUIRED:
			returnString = "required";
			break;
		case TYPE_OPTIONAL:
			returnString = "optional";
			break;
		default:
			;
			break;
		}

		return returnString;

	}

	public String getActors_role() {
		return this.actors_role;
	}

	public void setActors_role(String actors_role) {
		this.actors_role = actors_role;
	}

	public int getAssignmentType() {
		return assignmentType;
	}

	public void setAssignmentType(int assignmentType) {
		this.assignmentType = assignmentType;
	}
	
	public void setAssignmentType(String saa_type){
		if (saa_type == null){
			return;
		}
		
		if (saa_type.equalsIgnoreCase("required")){
			this.assignmentType = TYPE_REQUIRED;
		} else {
			this.assignmentType = TYPE_OPTIONAL;
		}
	}

	public String getAssignmentPriority() {
		return assignmentPriority;
	}

	public void setAssignmentPriority(String assignmentPriority) {
		this.assignmentPriority = assignmentPriority;
	}

	public String getAssignmentNotes() {
		return assignmentNotes;
	}

	public void setAssignmentNotes(String assignmentNotes) {
		this.assignmentNotes = assignmentNotes;
	}

	public String getActors_chat_color() {
		return actors_chat_color;
	}

	public void setActors_chat_color(String actorsChatColor) {
		actors_chat_color = actorsChatColor;
	}

	public SimActorAssignment() {

	}

	public Long getSim_id() {
		return this.sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getActorId() {
		return this.actorId;
	}

	public void setActorId(Long actor_id) {
		this.actorId = actor_id;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public SimActorAssignment(String schema, Long sim_id, Long actor_id) {
		this.sim_id = sim_id;
		this.actorId = actor_id;
		if (SimActorAssignment.getBySimIdAndActorId(schema, sim_id, actor_id) == null) {
			this.saveMe(schema);
		}
	}

	public static SimActorAssignment getBySimIdAndActorId(String schema,
			Long sim_id, Long actor_id) {

		String hqlQuery = "from SimActorAssignment where sim_id = " + sim_id + " AND actor_id = " + actor_id; //$NON-NLS-1$ //$NON-NLS-2$

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<SimActorAssignment> returnList = MultiSchemaHibernateUtil
				.getSession(schema).createQuery(hqlQuery).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if ((returnList != null) && (returnList.size() > 0)) {
			return returnList.get(0);
		} else {
			return null;
		}

	}

	/**
	 * Removes the saa from the database.
	 * 
	 * @param schema
	 * @param sim_id
	 * @param actor_id
	 */
	public static void removeMe(String schema, Long sim_id, Long actor_id) {

		SimActorAssignment saa = SimActorAssignment.getBySimIdAndActorId(
				schema, sim_id, actor_id);

		if (saa != null) {
			MultiSchemaHibernateUtil.beginTransaction(schema);
			MultiSchemaHibernateUtil.getSession(schema).delete(saa);
			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		}
	}

	/**
	 * Saves the assignment back to the database.
	 * 
	 * @param schema
	 */
	public void saveMe(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}

	/**
	 * Returns the actors associated with a particular sim.
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static List<Actor> getActorsForSim(String schema, Long sim_id) {

		List startList = getActorsAssignmentsForSim(schema, sim_id);

		ArrayList returnList = new ArrayList();

		for (ListIterator<SimActorAssignment> li = startList.listIterator(); li
				.hasNext();) {
			SimActorAssignment this_saa = li.next();
			Actor act = Actor.getById(schema, this_saa.getActorId());
			returnList.add(act);
		}

		return returnList;

	}

	/**
	 * Removes all actor assignments for the given actor.
	 * 
	 * @param schema
	 * @param a_id
	 */
	@SuppressWarnings("unchecked")
	public static void removeActorAssignments(String schema, Long a_id) {

		if (a_id == null) {
			Logger
					.getRootLogger()
					.debug(
							"Null actor id sent to SimActorAssignment.removeActorAssignments");
			return;
		}

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<SimActorAssignment> removeList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery(
						"from SimActorAssignment where actor_id = :a_id order by id")
				.setString("a_id", a_id.toString()).list(); //$NON-NLS-1$ //$NON-NLS-2$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		for (ListIterator<SimActorAssignment> li = removeList.listIterator(); li
				.hasNext();) {
			SimActorAssignment this_saa = li.next();

			SimActorAssignment.removeMe(schema, this_saa.getSim_id(), this_saa
					.getActorId());

		}

	}

	/**
	 * Gets the actor simulation assignments for a particular simulation.
	 * 
	 * @param simid
	 * @param schema
	 * @return
	 */
	public static List<SimActorAssignment> getActorsAssignmentsForSim(
			String schema, Long sim_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<SimActorAssignment> returnList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery(
						"from SimActorAssignment where sim_id = " + sim_id + " order by id").list(); //$NON-NLS-1$ //$NON-NLS-2$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	
	public static List<SimActorAssignment> getActiveActorsAssignmentsForSim(
			String schema, Long sim_id, boolean active) {

		String activeString = "true";
		
		if (!(active)) {
			activeString = "false";
		}
		
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<SimActorAssignment> returnList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery(
						"from SimActorAssignment where sim_id = " + sim_id + 
						" and active is " + activeString + " order by id").list(); //$NON-NLS-1$ //$NON-NLS-2$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	/**
	 * 
	 * @param schema
	 * @param rs_id
	 * @return
	 */
	public static SimActorAssignment getById(String schema, Long saa_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		SimActorAssignment this_saa = (SimActorAssignment) MultiSchemaHibernateUtil
				.getSession(schema).get(SimActorAssignment.class, saa_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return this_saa;

	}

	public Long getTransitId() {
		return transitId;
	}

	public void setTransitId(Long transitId) {
		this.transitId = transitId;
	}	
	
	public void storeDetails(String saa_type, String saa_role, String saa_notes, String saa_priority, String schema){
		
		this.setAssignmentType(saa_type);
		this.setActors_role(saa_role);
		this.setAssignmentNotes(saa_notes);
		this.setAssignmentPriority(saa_priority);
		this.saveMe(schema);
		
		
	}

}
