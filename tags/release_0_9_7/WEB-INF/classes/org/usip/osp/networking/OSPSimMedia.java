package org.usip.osp.networking;

/**
 * This file represents a media object (binary file) to be placed in a simulation export to be transferred.
 *
 */
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
public class OSPSimMedia {
	
	public static final int ACTOR_IMAGE = 1;
	
	public static final int SIM_IMAGE = 2;

	private int mediaType = 0;
	
	private String mediaName = null;

	private String mediaString = null;
		
	public int getMediaType() {
		return mediaType;
	}

	public void setMediaType(int mediaType) {
		this.mediaType = mediaType;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public String getMediaString() {
		return mediaString;
	}

	public void setMediaString(String mediaString) {
		this.mediaString = mediaString;
	}
	
}
