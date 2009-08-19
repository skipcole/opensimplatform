package org.usip.osp.specialfeatures;

import javax.persistence.*;

/**
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
@Table(name = "OBJECT_HISTORY")
public class OSPObjectHistory {

	public static final int TYPE_INT = 1;
	
	/** Database id of this Variable. */
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "VALUE")
	private Integer value;
	
	/** If phase is needed, keep a record of it. */
	private Long phase;
	
	/** If sequence number is needed, keep a record of it. */
	private Long sequence;
	
	/** If an even finer level of detail than sequence is needed, keep a record of it. */
	private Long microsequence;
	
	public Long getPhase() {
		return phase;
	}

	public void setPhase(Long phase) {
		this.phase = phase;
	}

	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	public Long getMicrosequence() {
		return microsequence;
	}

	public void setMicrosequence(Long microsequence) {
		this.microsequence = microsequence;
	}

	@Column(name = "ROUND")
	private Integer round;

	public Integer getValue() {
		return this.value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getRound() {
		return this.round;
	}

	public void setRound(Integer round) {
		this.round = round;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
