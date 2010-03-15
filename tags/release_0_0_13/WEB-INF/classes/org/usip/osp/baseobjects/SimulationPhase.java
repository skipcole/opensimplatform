package org.usip.osp.baseobjects;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.networking.ObjectPackager;
import org.usip.osp.persistence.*;

/**
 * This class represents a phase of a simulation.
 *
 */
/*
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
	
	public SimulationPhase(){
		// For now arbitrarily set date to 1/1/2001.
		Calendar cal = new GregorianCalendar();
		
		// Year, month, day, hour, minute
		cal.set(2001, 0, 1, 9, 0);
		
		phaseStartDate = cal.getTime();
		
	}

	/** Database id of this Phase. */
	@Id
	@GeneratedValue
	@Column(name = "PHASE_ID")
	private Long id;
	
	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	public Long getTransit_id() {
		return this.transit_id;
	}

	public void setTransit_id(Long transit_id) {
		this.transit_id = transit_id;
	}

	/** Name of this Phase. */
	@Column(name = "PHASE_NAME")
	private String name = ""; //$NON-NLS-1$
	
	/** Name of this Phase. */
	@Column(name = "PHASE_NOTES")
	@Lob
	private String notes = ""; //$NON-NLS-1$
	
	/** Flag to indicate that this is the first phase. */
	private boolean firstPhase = false;
	
	/** Flag to indicate that this is the last phase. */
	private boolean lastPhase = false;
	
	/** If we are copying objects created in a previous phase, flag this here. */
	private boolean copyInObjects = false;
	
	private Date phaseStartDate = new Date();
	
	public Date getPhaseStartDate() {
		return phaseStartDate;
	}

	public void setPhaseStartDate(Date phaseStartDate) {
		this.phaseStartDate = phaseStartDate;
	}

	/** Indicates if time (in the form of round or calendar) is tracked during this phase. */
	private boolean timePasses = false;
	
	/** Indicates the method by which time will pass during this simulation. */
	private int time_passage_mechanism = 0;
	
	/** Indicates if the computer will move time along independent of the players and control. */
	private boolean time_advances_automatically = false;

	/** Possible order of this Phase in relation to the others.. */
	@Column(name = "PHASE_ORDER")
	private int order;
	
	private Long metaPhaseId = null;

	public Long getMetaPhaseId() {
		return metaPhaseId;
	}

	public void setMetaPhaseId(Long metaPhaseId) {
		this.metaPhaseId = metaPhaseId;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhaseName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
	public boolean isFirstPhase() {
		return this.firstPhase;
	}

	public void setFirstPhase(boolean firstPhase) {
		this.firstPhase = firstPhase;
	}

	public boolean isLastPhase() {
		return this.lastPhase;
	}

	public void setLastPhase(boolean lastPhase) {
		this.lastPhase = lastPhase;
	}

	public boolean isCopyInObjects() {
		return copyInObjects;
	}

	public void setCopyInObjects(boolean copyInObjects) {
		this.copyInObjects = copyInObjects;
	}

	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	
	public boolean isTimePasses() {
		return this.timePasses;
	}

	public void setTimePasses(boolean timePasses) {
		this.timePasses = timePasses;
	}

	public int getTime_passage_mechanism() {
		return this.time_passage_mechanism;
	}

	public void setTime_passage_mechanism(int time_passage_mechanism) {
		this.time_passage_mechanism = time_passage_mechanism;
	}

	public boolean isTime_advances_automatically() {
		return this.time_advances_automatically;
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
		sp_first.setName("Started"); //$NON-NLS-1$
		sp_first.setOrder(1);
		sp_first.setFirstPhase(true);
		sp_first.setNotes("The first phase of the simulation."); //$NON-NLS-1$
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
		sp_last.setName("Completed"); //$NON-NLS-1$
		sp_last.setOrder(9999);
		sp_last.setLastPhase(true);
		sp_last.setNotes("The last phase of the simulation."); //$NON-NLS-1$
		sp_last.saveMe(schema);
		
		return sp_last;
		
	}
	
	/**
	 * 
	 * @param schema
	 * @param the_id
	 * @return
	 */
	public static SimulationPhase getMe(String schema, String the_id){
		
		if (the_id == null){
			return null;
		}
		
		return getMe(schema, new Long(the_id));
	}
	
	/**
	 * Returns the simulation phase from the database.
	 * 
	 * @param schema
	 * @param the_id
	 * @return
	 */
	public static SimulationPhase getMe(String schema, Long the_id){
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		SimulationPhase sp = (SimulationPhase) MultiSchemaHibernateUtil.
			getSession(schema).get(SimulationPhase.class, the_id);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return sp;
		
	}

	@Override
	public int compareTo(Object arg0) {
		
		SimulationPhase sp = (SimulationPhase) arg0;
		return  this.getOrder() - sp.getOrder();
	}
	
	/**
	 * Compares two phases (generally matched by name) to make sure that they are identical.
	 * 
	 * @param phase_a
	 * @param phase_b
	 * @return
	 */
	public static String compare(SimulationPhase phase_a, SimulationPhase phase_b){
		
		boolean foundDifference = false;
		
		String differenceString = "<PHASE_COMPARE>\r\n";
		
		// Compare Objectives
		if (phase_a.getPhaseName().equals(phase_b.getPhaseName())){
			differenceString += ObjectPackager.addResultsToXML("     <PHASE_NAME>", "</PHASE_NAME>\r\n", true);
		} else {
			differenceString += ObjectPackager.addResultsToXML("     <PHASE_NAME>", "</PHASE_NAME>\r\n", false);
			foundDifference = true;
		}
		
		differenceString += ObjectPackager.addResultsToXML("     <RESULTS>", "</RESULTS>\r\n", !(foundDifference));
		
		differenceString += "</PHASE_COMPARE>\r\n";
		
		return differenceString;
	}
	
}
