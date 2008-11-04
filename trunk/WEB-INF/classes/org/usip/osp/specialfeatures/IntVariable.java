package org.usip.osp.specialfeatures;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.SimulationPhase;


/**
 * @author Ronald "Skip" Cole
 *
 * This file is part of the USIP Online Simulation Platform.<br>
 * 
 * The USIP Online Simulation Platform is free software; you can redistribute it and/or
 * modify it under the terms of the new BSD Style license associated with this
 * distribution.<br>
 * 
 * The USIP Online Simulation Platform is distributed WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. <BR>
 * 
 */
@Entity
@Proxy(lazy=false)
public class IntVariable extends SimVariable{

	public static void main(String args[]) {
		System.out.println("begin");
		IntVariable iv = new IntVariable();
		
	
		
		
		System.out.println("end");
	}

	@Column(name = "VALUE")
	private Integer value;
	
	@Column(name = "INITIAL_VALUE")
	private Integer initial_value;
	
	@OneToMany
	@JoinColumn(name = "IV_ID")
	private List<IntVariableHistory> history = new ArrayList<IntVariableHistory>();


	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}


	/**
	 * 
	 * @param rsid
	 * @return
	 */
	public IntVariable createCopy(Long rsid, org.hibernate.Session hibernate_session) {
		IntVariable iv = new IntVariable();
		
		iv.setName(this.getName());
		iv.setValue(this.getValue());
		iv.setRs_id(rsid);
		
		hibernate_session.saveOrUpdate(iv);
		
		return iv;
	}

	public Integer getInitial_value() {
		return initial_value;
	}

	public void setInitial_value(Integer initial_value) {
		this.initial_value = initial_value;
	}

	public void setInitialValue(String initialvalue) {
		Integer iv = new Integer(initialvalue);
		setInitial_value(iv);
	}
	
	
	
}