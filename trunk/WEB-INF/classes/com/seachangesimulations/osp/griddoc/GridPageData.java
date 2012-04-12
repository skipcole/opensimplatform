package com.seachangesimulations.osp.griddoc;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.baseobjects.SimulationPhase;
import org.usip.osp.baseobjects.SimulationSectionAssignment;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * GridPageData is used to summarize data pulled from GridData points.
 */
/*
 * 
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it
 * and/or modify it under the terms of the new BSD Style license associated with
 * this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. <BR>
 */
public class GridPageData {

	private int numCols = 0;

	private int numRows = 0;

	private ArrayList<String> rowNames = new ArrayList<String>();

	private ArrayList<String> colNames = new ArrayList<String>();

	public int getNumCols() {
		return numCols;
	}

	public void setNumCols(int numCols) {
		this.numCols = numCols;
	}

	public int getNumRows() {
		return numRows;
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	public ArrayList<String> getRowNames() {
		return rowNames;
	}

	public void setRowNames(ArrayList<String> rowNames) {
		this.rowNames = rowNames;
	}

	public ArrayList<String> getColNames() {
		return colNames;
	}

	public void setColNames(ArrayList<String> colNames) {
		this.colNames = colNames;
	}

	/**
	 * 
	 * @param request
	 * @param cs
	 */
	public static void handleChanges(HttpServletRequest request,
			CustomizeableSection cs, String schema, Long simId, Long rsId) {

		String do_add_col = (String) request.getParameter("do_add_col");
		String do_add_row = (String) request.getParameter("do_add_row");

		GridPageData gpd = new GridPageData();
		GridData newGD = new GridData();

		if ((do_add_col != null) || (do_add_row != null)) {
			newGD.setCsId(cs.getId());
			newGD.setSimId(simId);
			newGD.setRsId(rsId);
		}

		if (do_add_col != null) {

			String col_name = (String) request.getParameter("col_name");

			if ((col_name != null) && (col_name.trim().length() > 0)){
				// Load in number of cols
				gpd.loadColumnNames(schema, cs, simId, rsId);

				newGD.setRowNum(0);
				newGD.setColNum(gpd.getNumCols() + 1);
				newGD.setCellData(col_name);
				newGD.saveMe(schema);
			}

		}

		if (do_add_row != null) {

			String row_name = (String) request.getParameter("row_name");

			if ((row_name != null) && (row_name.trim().length() > 0)){
				// Load in row names
				gpd.loadRowNames(schema, cs, simId, rsId);

				newGD.setRowNum(gpd.getNumRows() + 1);
				newGD.setColNum(0);
				newGD.setCellData(row_name);
				newGD.saveMe(schema);
			}

		}
		
		String del_col = (String) request.getParameter("del_col");
		if (del_col != null) {
			String col = (String) request.getParameter("col");
			
			List objectsToDelete = getCol(schema, simId, cs.getId(), rsId, new Long(col));
			
			for (ListIterator <GridData >li = objectsToDelete.listIterator(); li.hasNext();) {
				GridData dataToDelete = li.next();
				GridData.deleteGridData(schema, dataToDelete);
			}
			
		}

	}

	/**
	 * 
	 * @param schema
	 * @param cs
	 * @param simId
	 * @param rsId
	 * @return
	 */
	public static GridPageData loadPage(String schema, CustomizeableSection cs,
			Long simId, Long rsId) {

		GridPageData gpd = new GridPageData();

		// Load in number of cols
		gpd.loadColumnNames(schema, cs, simId, rsId);

		// Load in number of rows
		gpd.loadRowNames(schema, cs, simId, rsId);

		return gpd;
	}

	/**
	 * Loads the data into the col
	 * 
	 * @param schema
	 * @param cs
	 * @param simId
	 * @param rsId
	 */
	public void loadColumnNames(String schema, CustomizeableSection cs, Long simId,
			Long rsId) {

		// Load in number of cols
		List cols = getCol(schema, simId, cs.getId(), rsId, new Long(0));

		if (cols == null) {
			this.setNumCols(0);
			this.setColNames(new ArrayList<String>());
		} else {
			this.setNumCols(cols.size());

			ArrayList<String> newList = new ArrayList<String>();
			// loop over and pull out names
			for (ListIterator li = cols.listIterator(); li.hasNext();) {
				GridData gd = (GridData) li.next();

				newList.add(gd.getCellData());
			}
			this.setColNames(newList);
		}
	}

	/**
	 * 
	 * @param schema
	 * @param cs
	 * @param simId
	 * @param rsId
	 */
	public void loadRowNames(String schema, CustomizeableSection cs, Long simId,
			Long rsId) {

		List rows = getRow(schema, simId, cs.getId(), rsId, new Long(0));
		
		if (rows == null) {
			this.setNumRows(0);
			this.setRowNames(new ArrayList<String>());
		} else {
			this.setNumRows(rows.size());

			ArrayList<String> newList = new ArrayList<String>();
			// loop over and pull out names
			for (ListIterator li = rows.listIterator(); li.hasNext();) {
				GridData gd = (GridData) li.next();

				newList.add(gd.getCellData());
			}
			this.setRowNames(newList);
		}

	}



	/**
	 * Returns rows where col equals 0. (Which contains the row names.)
	 * 
	 * @param schema
	 * @param simId
	 * @param csId
	 * @param rsId
	 * @return
	 */
	public static List getRow(String schema, Long simId, Long csId,
			Long rsId, Long rowNumber) {

		if ((rsId == null) || (simId == null) || (csId == null)) {
			return new ArrayList();
		}

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery(
						"from GridData where simId = :simId and csId = :csId and rsId = :rsId and rowNum = :rowNumber") //$NON-NLS-1$
				.setLong("simId", simId)
				.setLong("csId", csId)
				.setLong("rowNumber", rowNumber)
				.setLong("rsId", rsId).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	/**
	 * Returns cols where row equals 0. (Which contains the column names.)
	 * 
	 * @param schema
	 * @param simId
	 * @param csId
	 * @param rsId
	 * @return
	 */
	public static List<GridData> getCol(String schema, Long simId, Long csId,
			Long rsId, Long colNumber) {

		if ((rsId == null) || (simId == null) || (csId == null)) {
			return new ArrayList();
		}

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery(
						"from GridData where simId = :simId and csId = :csId and rsId = :rsId and colNum = :colNumber") //$NON-NLS-1$
				.setLong("simId", simId)
				.setLong("csId", csId)
				.setLong("colNumber", colNumber)
				.setLong("rsId", rsId).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

}
