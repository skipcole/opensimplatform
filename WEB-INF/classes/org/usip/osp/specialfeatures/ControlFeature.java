package org.usip.osp.specialfeatures;

import java.util.List;
import java.util.ListIterator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.BaseSimSectionDepObjectAssignment;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.sharing.ExportableObject;

/**
 * This class represents the controller over whether a particular control player's control
 * gets shown or not. The program has grown to now where the control player has a lot
 * of interesting and potential levers: most of which they may never use. So now we are going
 * to allow the author to reduce the set of which get shown to reduce the complexity to the 
 * control player.
 */
/*
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
@Proxy(lazy = false)
public class ControlFeature implements ExportableObject{

	
	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;
	
	/** Simulation id. */
    private Long simId;
    
    /** The phase for which this control is effective. Zero indicates all phases. */
    private Long phaseid;
    
    private String featureName;
    
    private boolean shown = true;
    
    private String notes = "";
    
    private Long transitId;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSimId() {
		return simId;
	}

	public void setSimId(Long simId) {
		this.simId = simId;
	}

	public Long getPhaseid() {
		return phaseid;
	}

	public void setPhaseid(Long phaseid) {
		this.phaseid = phaseid;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public boolean isShown() {
		return shown;
	}

	public void setShown(boolean shown) {
		this.shown = shown;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@Override
	public Long getTransitId() {
		return transitId;
	}

	@Override
	public void setTransitId(Long transit_id) {
		this.transitId = transit_id;
		
	}

	/**
	 * Saves this object to the database.
	 * @param schema
	 */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}
	
	/**
	 * Gets all of the control features for a simulation.
	 * 
	 * @param schema
	 * @param simId
	 * @return
	 */
	public static List getAllForSim(String schema, Long simId) {
		
		String hql_string = "from ControlFeature where SIM_ID = :simId";  //$NON-NLS-1$
	
		MultiSchemaHibernateUtil.beginTransaction(schema);
		List returnList = MultiSchemaHibernateUtil.getSession(schema)
			.createQuery(hql_string)
			.setLong("simId", simId)
			.list();
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
	}
	
	/**
	 * Returns the value found when one passes it in a particular list (probably for a particular simulation) 
	 * and a feature name.
	 * 
	 * @param daList
	 * @param phaseId
	 * @param featureName
	 * @return
	 */
	public static boolean getValue(List <ControlFeature> daList, Long phaseId, String featureName){
		
		if ((phaseId == null) || (featureName == null)){
			return false;
		}
		
		// Loop over dependent object assignments found for this simulation
		for (ListIterator<ControlFeature> lc = daList
				.listIterator(); lc.hasNext();) {
			ControlFeature cf = lc.next();
			
			if ((cf.phaseid.intValue() == 0) || (cf.phaseid.intValue() == phaseId.intValue())){
				
				if (cf.featureName.equalsIgnoreCase(featureName)){
					return cf.isShown();
				}
			}
			
		}
		
		return false;
	}


}
