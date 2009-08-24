package org.usip.osp.baseobjects;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;

/**
 * This class represents an entry into a particular section of a simulated world. It works with the 'UserTrail'
 * to help fulfill its intended purpose: keeping track (in some or all cases) how long a player spends in a particular
 * section of the simulation.
 *
 * 
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can
 * redistribute it and/or modify it under the terms of the new BSD Style license
 * associated with this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. <BR>
 * 
 */
@Entity
@Table(name = "USER_TRAIL_HIGHLIGHTS")
@Proxy(lazy = false)
public class UserTrailHighlights {


	/** Database id of this user trail highlights object. */
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
}
