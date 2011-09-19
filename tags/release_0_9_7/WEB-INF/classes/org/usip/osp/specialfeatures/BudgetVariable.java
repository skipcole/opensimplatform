package org.usip.osp.specialfeatures;

import java.sql.*;
import java.util.*;

import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/*
 * 
 *         This file is part of the USIP Open Simulation Platform.<br>
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
public class BudgetVariable {

	public static final String TRANSTYPE_INITIAL = "initial"; //$NON-NLS-1$
	public static final String TRANSTYPE_MOVE = "move"; //$NON-NLS-1$
	public static final String TRANSTYPE_FINAL = "final"; //$NON-NLS-1$

	private Long sim_id;
	private Long running_sim_id;
	private String var_name;
	private String description;
	private boolean accumulates = true;
	private float maxValue = Float.MAX_VALUE;
	private float minValue = Float.MIN_VALUE;
	private float initialValue = 0;
	private float value = 0;
	private boolean tracked = false;
	
	public BudgetVariable() {

	}
	
	
	
	public Long getSim_id() {
		return sim_id;
	}



	public void setSim_id(Long simId) {
		sim_id = simId;
	}



	public Long getRunning_sim_id() {
		return running_sim_id;
	}



	public void setRunning_sim_id(Long runningSimId) {
		running_sim_id = runningSimId;
	}



	public String getVar_name() {
		return var_name;
	}



	public void setVar_name(String varName) {
		var_name = varName;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public boolean isAccumulates() {
		return accumulates;
	}



	public void setAccumulates(boolean accumulates) {
		this.accumulates = accumulates;
	}



	public float getMaxValue() {
		return maxValue;
	}



	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}



	public float getMinValue() {
		return minValue;
	}



	public void setMinValue(float minValue) {
		this.minValue = minValue;
	}



	public float getInitialValue() {
		return initialValue;
	}



	public void setInitialValue(float initialValue) {
		this.initialValue = initialValue;
	}



	public float getValue() {
		return value;
	}



	public void setValue(float value) {
		this.value = value;
	}



	public boolean isTracked() {
		return tracked;
	}



	public void setTracked(boolean tracked) {
		this.tracked = tracked;
	}



	/**
	 * Based on the type of propagation, advances data to the next round.
	 * 
	 */
	public String propagate(Simulation sim, int gameround, Long rgid) {

		String returnString = "SimulationVariable.propogate(): "; //$NON-NLS-1$

		return returnString;
	}

}
