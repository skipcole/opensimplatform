package org.usip.osp.baseobjects;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.*;

/**
 * This class represents a phase of a simulation.
 *
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
public class SimulationPhase implements Comparable{
	
	
	public static final int ROUND_BASED = 1;
	
	public static final int REALTIME_BASED = 2;
	
	public static final int PSEUDOTIME_BASED = 3;
	

	/** Database id of this Phase. */
	@Id
	@GeneratedValue
	@Column(name = "PHASE_ID")
	private Long id;
	
	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	public Long getTransit_id() {
		return transit_id;
	}

	public void setTransit_id(Long transit_id) {
		this.transit_id = transit_id;
	}

	/** Name of this Phase. */
	@Column(name = "PHASE_NAME")
	private String name = "";
	
	/** Name of this Phase. */
	@Column(name = "PHASE_NOTES")
	@Lob
	private String notes = "";
	
	private boolean firstPhase = false;
	
	private boolean lastPhase = false;
	
	/** Indicates if time (in the form of round or calendar) is tracked during this phase. */
	private boolean timePasses = false;
	
	/** Indicates the method by which time will pass during this simulation. */
	private int time_passage_mechanism = 0;
	
	/** Indicates if the computer will move time along independent of the players and control. */
	private boolean time_advances_automatically = false;

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

	
	public boolean isTimePasses() {
		return timePasses;
	}

	public void setTimePasses(boolean timePasses) {
		this.timePasses = timePasses;
	}

	public int getTime_passage_mechanism() {
		return time_passage_mechanism;
	}

	public void setTime_passage_mechanism(int time_passage_mechanism) {
		this.time_passage_mechanism = time_passage_mechanism;
	}

	public boolean isTime_advances_automatically() {
		return time_advances_automatically;
	}

	public void setTime_advances_automatically(boolean time_advances_automatically) {
		this.time_advances_automatically = time_advances_automatically;
	}

	public static List getAllForSim(String schema, Long sim_id) {
		
		return SimPhaseAssignment.getPhasesForSim(schema, sim_id);

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

	@Override
	public int compareTo(Object arg0) {
		
		SimulationPhase sp = (SimulationPhase) arg0;
		return  this.getOrder() - sp.getOrder();
	}
	
}
