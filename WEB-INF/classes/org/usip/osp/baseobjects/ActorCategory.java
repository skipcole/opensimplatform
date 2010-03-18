package org.usip.osp.baseobjects;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * Represents a class of characters that may all have the same simulation sections.
 * 
 * @author scole
 *
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
public class ActorCategory {

    /** Unique id of this actor. */
	@Id @GeneratedValue
    private Long id;
	
	private Long sim_id;
	
	private Long exemplar_id;
	
	public Long getExemplar_id() {
		return exemplar_id;
	}

	public void setExemplar_id(Long exemplar_id) {
		this.exemplar_id = exemplar_id;
	}

	private String categoryName = "";
	
	public ActorCategory(){
		
	}

	public ActorCategory(String ac_name, Long sim_id, Long exemplar_id, String schema) {
		this.categoryName = ac_name;
		this.sim_id = sim_id;
		this.exemplar_id = exemplar_id;
		
		this.saveMe(schema);
		
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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	/**
	 * Saves this generic variable to the database.
	 * @param schema
	 */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}
	
	/**
	 * Retrieves object from database.
	 * 
	 * @param schema
	 * @param ac_id
	 * @return
	 */
	public static ActorCategory getMe(String schema, Long ac_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		ActorCategory this_ac  = (ActorCategory) MultiSchemaHibernateUtil.getSession(schema).get(ActorCategory.class, ac_id);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return this_ac;

	}

	/**
	 * Returns all of the Actor Categories associated with a simulation. 
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static List <ActorCategory> getAllForSim(String schema, Long sim_id){
		
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from ActorCategory where sim_id = :sim_id")
				.setLong("sim_id", sim_id)
				.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}
	
	public List getMyActorIds(String schema){
		
		List returnList = new ArrayList();
		
		List rawList = ActorCategoryAssignments.getAllForActorCategory(schema, this.id);
		
		for (ListIterator<ActorCategoryAssignments> li = rawList.listIterator(); li.hasNext();) {
			ActorCategoryAssignments this_act = li.next();

			returnList.add(this_act.getActor_id());
		}
		
		return returnList;
		
	}
	
	public void applySectionsAcrossCategory(String schema, Long sim_id, Long exemplar_id){
		
		// Loop over all actors with this category
		for (ListIterator<Long> li = getMyActorIds(schema).listIterator(); li.hasNext();) {
			Long act_id = li.next();
			
			// Don't do the exemplar!
			if ((act_id != null) && (act_id.intValue() != exemplar_id.intValue()))  {
				
				// Loop over all phases in this sim
				
				
				
				
			}
		
		
		
		} // End of loop over Actors in this category
	}
	
}
