package org.usip.osp.specialfeatures;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;


/**
 * This class represents one specific choice in a set of answers that a player may select. For example,
 * the question might be "Do you want steak, or fish?" There would be one 
 * 
 * @author Ronald "Skip" Cole<br />
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
@Table(name = "ALLOWABLE_RESPONSES")
@Proxy(lazy = false)
public class AllowableResponse{
	
	/** Database id of this Simulation. */
	@Id
	@GeneratedValue
	private Long id;
    
	/** Words that introduce this choice. */
	@Lob
    private String responseText = "";
	
	/** If this response has a value associated with it, it is store here. */
	private String specificValue = "";

	@Lob
    private String specificWordsForAAR = "";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getResponseText() {
		return responseText;
	}

	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}

	public String getSpecificValue() {
		return specificValue;
	}

	public void setSpecificValue(String specificValue) {
		this.specificValue = specificValue;
	}

	public String getSpecificWordsForAAR() {
		return specificWordsForAAR;
	}

	public void setSpecificWordsForAAR(String specificWordsForAAR) {
		this.specificWordsForAAR = specificWordsForAAR;
	}
 

}
