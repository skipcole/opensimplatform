package org.usip.osp.baseobjects;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.*;

/**
 * @author Ronald "Skip" Cole
 * 
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can
 * redistribute it and/or modify it under the terms of the new BSD Style license
 * associated with this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. <BR>
 * 
 */
@Entity
@Table(name = "PHASES")
@Proxy(lazy=false)
public class SimulationPhase{

	/** Database id of this Phase. */
	@Id
	@GeneratedValue
	@Column(name = "PHASE_ID")
	private Long id;

	/** Name of this Phase. */
	@Column(name = "PHASE_NAME")
	private String name = "";
	
	/** Name of this Phase. */
	@Column(name = "PHASE_NOTES")
	@Lob
	private String notes = "";
	
	private boolean firstPhase = false;
	
	private boolean lastPhase = false;

	/** Possible order of this Phase in relation to the others.. */
	@Column(name = "PHASE_ORDER")
	private int order;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
	public boolean isFirstPhase() {
		return firstPhase;
	}

	public void setFirstPhase(boolean firstPhase) {
		this.firstPhase = firstPhase;
	}

	public boolean isLastPhase() {
		return lastPhase;
	}

	public void setLastPhase(boolean lastPhase) {
		this.lastPhase = lastPhase;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	
	public List getAllForSim(String schema, Long the_sim_id) {
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		String hql_string = "from SimulationPhase where SIM_ID = " + the_sim_id.toString();
		
		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(hql_string).list();
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;

	}
	
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}
	
	/**
	 * 
	 * @param schema
	 * @return
	 */
	public static SimulationPhase getNewFirstPhase(String schema){
		SimulationPhase sp_first = new SimulationPhase();
		sp_first.setName("Started");
		sp_first.setOrder(1);
		sp_first.setFirstPhase(true);
		sp_first.setNotes("The first phase of the simulation.");
		sp_first.saveMe(schema);
		
		return sp_first;
	}
	
	/**
	 * 
	 * @param schema
	 * @return
	 */
	public static SimulationPhase getNewLastPhase(String schema){
		
		SimulationPhase sp_last = new SimulationPhase();
		sp_last.setName("Completed");
		sp_last.setOrder(9999);
		sp_last.setLastPhase(true);
		sp_last.setNotes("The last phase of the simulation.");
		sp_last.saveMe(schema);
		
		return sp_last;
		
	}
	
	public static SimulationPhase getMe(String schema, String the_id){
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		SimulationPhase sp = (SimulationPhase) MultiSchemaHibernateUtil.
			getSession(schema).get(SimulationPhase.class, new Long(the_id));
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return sp;
		
	}
	
}
