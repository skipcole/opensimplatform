package org.usip.osp.graphs;


import java.util.*;

import javax.persistence.Entity;

import org.hibernate.annotations.Proxy;
import org.jfree.chart.*;

import org.usip.osp.persistence.MultiSchemaHibernateUtil;


/**
 *
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
public class Chart{

    private JFreeChart this_chart = null;

    private String title = ""; //$NON-NLS-1$

    private String type = ""; //$NON-NLS-1$
    
    private String variableSFID = ""; //$NON-NLS-1$

    private String xAxisTitle = ""; //$NON-NLS-1$

    private String yAxisTitle = ""; //$NON-NLS-1$

    private int height = 300;

    private int width = 400;

    private String selectDataStatement = ""; //$NON-NLS-1$

    public static final String SPECIALFIELDLABEL = "linechart_page"; //$NON-NLS-1$

    public JFreeChart getThis_chart() {
		return this.this_chart;
	}

	public void setThis_chart(JFreeChart this_chart) {
		this.this_chart = this_chart;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVariableSFID() {
		return this.variableSFID;
	}

	public void setVariableSFID(String variableSFID) {
		this.variableSFID = variableSFID;
	}

	public String getXAxisTitle() {
		return this.xAxisTitle;
	}

	public void setXAxisTitle(String axisTitle) {
		this.xAxisTitle = axisTitle;
	}

	public String getYAxisTitle() {
		return this.yAxisTitle;
	}

	public void setYAxisTitle(String axisTitle) {
		this.yAxisTitle = axisTitle;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getSelectDataStatement() {
		return this.selectDataStatement;
	}

	public void setSelectDataStatement(String selectDataStatement) {
		this.selectDataStatement = selectDataStatement;
	}
    
    public Chart(){
        

    }

	/** Saves a chart. */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}

	/**
	 * Pulls the chart out of the database base on its id and schema.
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static Chart getMe(String schema, Long _chart_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		Chart chart = (Chart) MultiSchemaHibernateUtil.getSession(schema).get(Chart.class, _chart_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return chart;

	}

    public void setHeight(String h) {

        try {
            this.height = new Integer(h).intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setWidth(String w) {

        try {
            this.width = new Integer(w).intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/**
	 * Returns a list of all running sims created for a simulation.
	 * 
	 * @param simid
	 * @param schema
	 * @return
	 */
	public static List<Chart> getAllForSim(String simid, String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<Chart> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from Chart where sim_id = " + simid).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

}