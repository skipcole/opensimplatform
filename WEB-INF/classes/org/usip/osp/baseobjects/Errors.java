package org.usip.osp.baseobjects;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Errors {

	/** Database id of this Simulation. */
	@Id
	@GeneratedValue
	protected Long id;
	
	private java.util.Date errorDate = new java.util.Date();
	
	private String errorText = "";
	
	
	
}
