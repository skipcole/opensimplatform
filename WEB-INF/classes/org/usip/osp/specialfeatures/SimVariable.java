package org.usip.osp.specialfeatures;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;

/**
 * @author Ronald "Skip" Cole
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
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class SimVariable{

	/** Database id of this Variable. */
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;
	
	/** The id of the running simulation. */
	@Column(name = "RS_ID")
    private Long rs_id;
	
	/** The name of the variable. */
	@Column(name = "NAME")
    private String name;
	
	/** The description of the variable. */
	@Column(name = "DESCRIPTION")
    private String description;
	
	/** The description of the variable. */
	@Column(name = "PROP_TYPE")
    private String propagation_type;

	public Long getRs_id() {
		return rs_id;
	}

	public void setRs_id(Long rs_id) {
		this.rs_id = rs_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPropagation_type() {
		return propagation_type;
	}

	public void setPropagation_type(String propagation_type) {
		this.propagation_type = propagation_type;
	}
	
}
