package org.usip.osp.baseobjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/**
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
@Proxy(lazy = false)
public class SimPolishingStone {
	
	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;
	
	/** Database id of the Simulation. */
	private Long sim_id;
	
    /** Unique id of this phase. */
    private Long phase_id;
    
    private Long actor_id;

	private boolean polished = false;
	
	private String polishersName = "";
	
	
}
