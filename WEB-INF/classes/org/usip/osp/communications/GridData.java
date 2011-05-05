package org.usip.osp.communications;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;

/**
 * This class represents data stored by the players.
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

	

}
