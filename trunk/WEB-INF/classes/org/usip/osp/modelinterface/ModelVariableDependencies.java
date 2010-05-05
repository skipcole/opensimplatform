package org.usip.osp.modelinterface;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/**
 * This class represents dependencies that may exist between variables in a model. For example,
 * if the formula for a particular value 'x' is "x = y + z", then it is not possible to know x without
 * providing y and z. This information is important to provide to the simulation author(s) as they create 
 * their model based simulations.
 */
 /* 
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
@Table(name = "MODEL_VAR_DEPENDENCIES")
@Proxy(lazy = false)
public class ModelVariableDependencies {
	
	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;
	

}
