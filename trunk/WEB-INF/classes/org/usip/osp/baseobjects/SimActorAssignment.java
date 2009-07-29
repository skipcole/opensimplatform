package org.usip.osp.baseobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents the assignment of an actor into a simulation.
 *
 * 
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
@Entity
@Table(name = "SIM_ACTOR_ASSIGNMENT")
@Proxy(lazy = false)
public class SimActorAssignment {

	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;
	
	/** Database id of this Simulation. */
	@Column(name = "SIM_ID")
	private Long sim_id;
	
    /** Unique id of this actor. */
    @Column(name = "ACTOR_ID")
    private Long actor_id;
    
    private String actors_role = "";
    
	public String getActors_role() {
		return actors_role;
	}

	public void setActors_role(String actors_role) {
		this.actors_role = actors_role;
	}
	
	private String actors_chat_color = "";
	

	public String getActors_chat_color() {
		return actors_chat_color;
	}

	public void setActors_chat_color(String actors_chat_color) {
		this.actors_chat_color = actors_chat_color;
	}

	public SimActorAssignment(){
		
	}
	
	public SimActorAssignment(String schema, Long sim_id, Long actor_id){
		this.sim_id = sim_id;
		this.actor_id = actor_id;
		if (SimActorAssignment.getMe(schema, sim_id, actor_id) == null){
			this.saveMe(schema);
		}
	}

	public Long getSim_id() {
		return sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getActor_id() {
		return actor_id;
	}

	public void setActor_id(Long actor_id) {
		this.actor_id = actor_id;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static SimActorAssignment getMe(String schema, Long sim_id, Long actor_id){
		
		String hqlQuery = "from SimActorAssignment where sim_id = " + sim_id + " AND actor_id = " + actor_id;
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List<SimActorAssignment> returnList = 
			MultiSchemaHibernateUtil.getSession(schema).createQuery(hqlQuery).list();
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		if ((returnList != null) && (returnList.size() > 0)){
			return (SimActorAssignment) returnList.get(0);
		} else {
			return null;
		}
		
	}
	
	public static void removeMe(String schema, Long sim_id, Long actor_id){
		
		SimActorAssignment saa = SimActorAssignment.getMe(schema, sim_id, actor_id);
		
		if (saa != null){
			MultiSchemaHibernateUtil.beginTransaction(schema);
			MultiSchemaHibernateUtil.getSession(schema).delete(saa);
			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		}
	}
	
	public void saveMe(String schema){
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
	public static List<Actor> getActorsForSim(String schema, Long sim_id){
		
		List startList = getActorsAssignmentsForSim(schema, sim_id);
		
		ArrayList returnList = new ArrayList();
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		for (ListIterator<SimActorAssignment> li = startList.listIterator(); li.hasNext();) {
			SimActorAssignment this_saa = (SimActorAssignment) li.next();
			Actor act = (Actor) MultiSchemaHibernateUtil.getSession(schema).get(Actor.class, this_saa.getActor_id());
			returnList.add(act);
		}
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
		
	}
	
	/**
	 * Gets the actor simulation assignments for a particular simulation.
	 * 
	 * @param simid
	 * @param schema
	 * @return
	 */
	public static List<SimActorAssignment> getActorsAssignmentsForSim(String schema, Long sim_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List<SimActorAssignment> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from SimActorAssignment where sim_id = " + sim_id + " order by id").list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
	}
	
	
}
