package org.usip.osp.baseobjects;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.sharing.ExportableObject;

/**
 * This class holds information regarding how a simulation author sees their simulation being played out.
 * 
 */
/*         This file is part of the USIP Open Simulation Platform.<br>
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
@Proxy(lazy = false)
public class PlannedPlaySessionParameters implements ExportableObject{

	/** Database id of this PPSP, And the Simulation this is associated with. */
	@Id
	private Long id;
	
	/** Id used when objects are exported and imported moving across databases. */
	private Long transitId;
	
	private String minNumPlayers = "";
	private String maxNumPlayers = "";
	private String minPlayTime = "";
	private String recommendedPlayTime = "";
	
	@Lob
	private String plannedPlayIdeas = ""; //$NON-NLS-1$
	
	/**
	 * Zero argument constructor required by hibernate.
	 */
	public PlannedPlaySessionParameters() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTransitId() {
		return this.transitId;
	}

	public void setTransitId(Long transitId) {
		this.transitId = transitId;
	}
	
	/**
	 * Saves this object back to the database.
	 * 
	 * @param schema
	 */
	public void saveMe(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}

	public String getMinNumPlayers() {
		return minNumPlayers;
	}

	public void setMinNumPlayers(String minNumPlayers) {
		this.minNumPlayers = minNumPlayers;
	}

	public String getMaxNumPlayers() {
		return maxNumPlayers;
	}

	public void setMaxNumPlayers(String maxNumPlayers) {
		this.maxNumPlayers = maxNumPlayers;
	}

	public String getMinPlayTime() {
		return minPlayTime;
	}

	public void setMinPlayTime(String minPlayTime) {
		this.minPlayTime = minPlayTime;
	}

	public String getRecommendedPlayTime() {
		return recommendedPlayTime;
	}

	public void setRecommendedPlayTime(String recommendedPlayTime) {
		this.recommendedPlayTime = recommendedPlayTime;
	}
	
	public String getPlannedPlayIdeas() {
		return plannedPlayIdeas;
	}

	public void setPlannedPlayIdeas(String planned_play_ideas) {
		this.plannedPlayIdeas = planned_play_ideas;
	}
	
	public static PlannedPlaySessionParameters getById(String schema, Long ppsp_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		PlannedPlaySessionParameters ppsp = 
			(PlannedPlaySessionParameters) MultiSchemaHibernateUtil.getSession(schema).get(PlannedPlaySessionParameters.class, ppsp_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		if (ppsp == null) {
			ppsp = new PlannedPlaySessionParameters();
			ppsp.setId(ppsp_id);
			ppsp.saveMe(schema);
		}

		return ppsp;
	}
	
}
