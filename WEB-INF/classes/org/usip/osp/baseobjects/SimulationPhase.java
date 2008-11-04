package org.usip.osp.baseobjects;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.*;

/**
 * @author Ronald "Skip" Cole
 * 
 * This file is part of the USIP Online Simulation Platform.<br>
 * 
 * The USIP Online Simulation Platform is free software; you can
 * redistribute it and/or modify it under the terms of the new BSD Style license
 * associated with this distribution.<br>
 * 
 * The USIP Online Simulation Platform is distributed WITHOUT ANY
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
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}


	/** Order of this Phase. This is currently not used. */
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
	
}
