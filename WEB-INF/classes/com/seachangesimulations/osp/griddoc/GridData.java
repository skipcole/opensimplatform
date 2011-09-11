package com.seachangesimulations.osp.griddoc;

import java.util.List;

import javax.persistence.*;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents data stored by the players in a grid at GridData(row, col).
 * 
 *   There are 3 special cases of this:
 * 		GridPageData (0,0) is used to store the number of rows and columns.
 * 		GridPageData (row, 0) is used to store the name of the row in its data field.
 * 		GridPageData (0, col) is used to store the name of the column in its data field.
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
	
	public static void main(String args[]){
		System.out.println("h");
		
		GridData x = new GridData(new Long(1), new Long(1),new Long(1), "1", "1");
		x.saveMe("test");
	}
	public GridData(){
		
	}
	
	public GridData(Long simId, Long rsId, Long csId, String row_num, String col_num){
		
		this.simId = simId;
		this.rsId = rsId;
		this.csId = csId;
		
		try {
			this.rowNum = new Long (row_num).intValue();
			this.colNum = new Long (col_num).intValue();
		} catch (Exception e){
			Logger.getRootLogger().warn("bady row or column number sent to grid doc");
		}
		
	}

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
		
		if (cellData == null){
			cellData = "";
		}
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
	
	/**
	 * Pulls the simulation out of the database base on its id and schema.
	 * 
	 * @param schema
	 * @param gd_id
	 * @return
	 */
	public static GridData getById(String schema, Long gd_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		GridData gridData = (GridData) MultiSchemaHibernateUtil
				.getSession(schema).get(GridData.class, gd_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return gridData;
	}
	
	/**
	 * Returns the data point found at the location specified for that custom
	 * section and running simulation.
	 * 
	 * @param schema
	 * @param simId
	 * @param csId
	 * @param rsId
	 * @param colNum
	 * @param rowNum
	 * @return
	 */
	public static GridData getGridData(String schema, Long simId, Long csId,
			Long rsId, int colNum, int rowNum) {

		if ((rsId == null) || (simId == null) || (csId == null)) {
			return new GridData();
		}

		String hqlQuery = "from GridData where simId = :simId and "
				+ "csId = :csId and rsId = :rsId and colNum = :colNum and rowNum = :rowNum"; //$NON-NLS-1$

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List tempList = MultiSchemaHibernateUtil.getSession(schema)
				.createQuery(hqlQuery).setLong("simId", simId)
				.setLong("csId", csId).setLong("rsId", rsId)
				.setInteger("colNum", colNum).setInteger("rowNum", rowNum)
				.list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if ((tempList == null) || (tempList.size() == 0)) {
			return new GridData();
		} else if (tempList.size() > 1) {

			System.out.println("multiple data at same point. We have problem.");
			return new GridData();
		} else {
			GridData gd = (GridData) tempList.get(0);
			return gd;
		}
	}

}
