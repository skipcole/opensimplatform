package org.usip.osp.addons.griddoc;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents data stored by the players.
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
public class GridData {

    /** Database id of this Inject. */
	@Id
	@GeneratedValue
    private Long id;
	
	private Long simId;
	
	private Long rsId;
	
	private Long csId;
	
	/** In case this is tied to a particular object. */
	private Long objectId;
	
	private Long versionNum;
	
	private int rowNum;
	
	private int colNum;
	
	/** In case this is ever used out to 3-d. */
	private int zNum;
	
	/** Just in case it is needed. */
	private String evenMoreMetaData;
	
	@Lob
	private String cellData;

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

	public Long getRsId() {
		return rsId;
	}

	public void setRsId(Long rsId) {
		this.rsId = rsId;
	}

	public Long getCsId() {
		return csId;
	}

	public void setCsId(Long csId) {
		this.csId = csId;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public Long getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(Long versionNum) {
		this.versionNum = versionNum;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public int getColNum() {
		return colNum;
	}

	public void setColNum(int colNum) {
		this.colNum = colNum;
	}

	public int getzNum() {
		return zNum;
	}

	public void setzNum(int zNum) {
		this.zNum = zNum;
	}

	public String getEvenMoreMetaData() {
		return evenMoreMetaData;
	}

	public void setEvenMoreMetaData(String evenMoreMetaData) {
		this.evenMoreMetaData = evenMoreMetaData;
	}

	public String getCellData() {
		return cellData;
	}

	public void setCellData(String cellData) {
		this.cellData = cellData;
	}

	public void saveMe(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}
	

}
