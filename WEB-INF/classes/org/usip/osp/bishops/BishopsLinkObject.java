package org.usip.osp.bishops;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;

/* 
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it
 * and/or modify it under the terms of the new BSD Style license associated with
 * this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. <BR>
 * 
 */
@Entity
@Proxy(lazy = false)
public class BishopsLinkObject {
	
	/** Database id of this Alert. */
	@Id
	@GeneratedValue
	private Long id;
	
	private Long sim_id;
	
	private Long running_sim_id;
	
	private Long user_id;
	
	private String title = "";
	
	private String link = "";
	
	@Lob
	private String description = "";
	
	private String author = "";

	
}
