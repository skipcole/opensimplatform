package org.usip.osp.baseobjects;

import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.Proxy;
import org.usip.osp.communications.Conversation;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents the assignment of an conversation into a simulation.
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
@Table(name = "SIM_CONVERSATION_ASSIGNMENT")
@Proxy(lazy = false)
public class SimConversationAssignment {

	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;
	
	/** Database id of this Simulation. */
	@Column(name = "SIM_ID")
	private Long sim_id;
	
    /** Unique id of this conversation. */
    @Column(name = "CONVERSATION_ID")
    private Long conversation_id;
	
	public SimConversationAssignment(){
		
	}
	
	public SimConversationAssignment(String schema, Long sim_id, Long conversation_id){
		this.sim_id = sim_id;
		this.conversation_id = conversation_id;
		if (SimConversationAssignment.getMe(schema, sim_id, conversation_id) == null){
			this.saveMe(schema);
		}
	}

	public Long getSim_id() {
		return this.sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getConversation_id() {
		return this.conversation_id;
	}

	public void setConversation_id(Long conversation_id) {
		this.conversation_id = conversation_id;
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static SimConversationAssignment getMe(String schema, Long sim_id, Long conversation_id){
		
		String hqlQuery = "from SimConversationAssignment where sim_id = " + sim_id + " AND conversation_id = " + conversation_id; //$NON-NLS-1$ //$NON-NLS-2$
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List<SimConversationAssignment> returnList = 
			MultiSchemaHibernateUtil.getSession(schema).createQuery(hqlQuery).list();
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		if ((returnList != null) && (returnList.size() > 0)){
			return returnList.get(0);
		} else {
			return null;
		}
		
	}
	
	public static void removeMe(String schema, Long sim_id, Long conversation_id){
		
		SimConversationAssignment saa = SimConversationAssignment.getMe(schema, sim_id, conversation_id);
		
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
	 * Returns the conversations associated with a particular sim.
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static List<Conversation> getConversationsForSim(String schema, Long sim_id){
		
		List startList = getConversationsAssignmentsForSim(schema, sim_id);
		
		ArrayList returnList = new ArrayList();
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		for (ListIterator<SimConversationAssignment> li = startList.listIterator(); li.hasNext();) {
			SimConversationAssignment this_saa = li.next();
			Conversation act = (Conversation) MultiSchemaHibernateUtil.getSession(schema).get(Conversation.class, this_saa.getConversation_id());
			returnList.add(act);
		}
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
		
	}
	
	/**
	 * Gets the conversation simulation assignments for a particular simulation.
	 * 
	 * @param simid
	 * @param schema
	 * @return
	 */
	public static List<SimConversationAssignment> getConversationsAssignmentsForSim(String schema, Long sim_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		List<SimConversationAssignment> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from SimConversationAssignment where sim_id = " + sim_id).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
	}
	
	
}