package org.usip.osp.specialfeatures;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.SimSectionDependentObject;

/**
 * This class represents an inventory Item that a player may have.
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
public class InventoryItem implements SimSectionDependentObject{
	
	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;
	
	/** Indicates if this is a template object for a simulation. */
	private boolean templateObject = false;
	
	/** Id of the base from which this copy is made. */
    @Column(name = "BASE_ID")
    private Long base_id;
	
	/** Simulation id. */
    @Column(name = "SIM_ID")
    private Long sim_id;
    
    /** Running simulation id. */
    @Column(name = "RS_ID")
    private Long rs_id;
    
    /** Id of the actor who possesses this item */
    private Long owner_id;
    
    private String itemName = ""; //$NON-NLS-1$
    
    @Lob
    private String notes = ""; //$NON-NLS-1$

    @Lob
	private String description = ""; //$NON-NLS-1$
	
	private String imageFile = ""; //$NON-NLS-1$
	
	@Lob
	private String metaData = ""; //$NON-NLS-1$

	@Override
	public Long createRunningSimVersion(String schema, Long simId, Long rsId,
			Object templateObject) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getTransit_id() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveMe(String schema) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setId(Long theId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSimId(Long theId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTransit_id(Long transitId) {
		// TODO Auto-generated method stub
		
	}

	public boolean isTemplateObject() {
		return templateObject;
	}

	public void setTemplateObject(boolean templateObject) {
		this.templateObject = templateObject;
	}

	public Long getBase_id() {
		return base_id;
	}

	public void setBase_id(Long baseId) {
		base_id = baseId;
	}

	public Long getSim_id() {
		return sim_id;
	}

	public void setSim_id(Long simId) {
		sim_id = simId;
	}

	public Long getRs_id() {
		return rs_id;
	}

	public void setRs_id(Long rsId) {
		rs_id = rsId;
	}

	public Long getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(Long ownerId) {
		owner_id = ownerId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageFile() {
		return imageFile;
	}

	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}

	public String getMetaData() {
		return metaData;
	}

	public void setMetaData(String metaData) {
		this.metaData = metaData;
	}

	
}
