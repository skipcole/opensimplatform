package org.usip.osp.communications;

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
public interface WebObject {

	
	/**
	 * Position in binary string of bit indicating if running simulation id
	 * should be sent.
	 */
	public static final int POS_RS_ID = 0;

	/**
	 * Position in binary string of bit indicating if running actor id should be
	 * sent.
	 */
	public static final int POS_A_ID = 1;

	/**
	 * Position in binary string of bit indicating if running user id should be
	 * sent.
	 */
	public static final int POS_U_ID = 2;
	

	public String getSendString();

	public void setSendString(String sendString);
}
