package org.usip.osp.communications;

import java.lang.reflect.*;

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
 */
public class AlertLevels {

	/** This alert is of an undefined type. */
	public static final int TYPE_UNDEFINED = 0;
	
	/** The simulation has been enabled. */
	public static final int TYPE_RUN_ENABLED = 1;
	
	/** An announcement has been made and may be seen on the announcement page. */
	public static final int TYPE_ANNOUNCEMENT = 2;
	
	/** New news is available from a news source accessible to the player. */
	public static final int TYPE_NEWS = 3;
	
	/** An event for the player. */
	public static final int TYPE_EVENT = 4;
	
	/** The phase of the simulation has changed. */
	public static final int TYPE_PHASECHANGE = 5;
	
	/** An incoming memo for the player. */
	public static final int TYPE_MEMO = 6;
	
	/** An the teacher has given the player some from of grade page. */
	public static final int TYPE_RATING_ANNOUNCEMENT = 7;
	
	/** An email has been received by the player page. */
	public static final int TYPE_EMAIL = 8;
	
	/**
	 * Multiple events have occured for the user. (Don't pester with continuous
	 * pop-up windows.
	 */
	public static final int TYPE_MULTIPLE = 99;
	
	
	public static final int ALERT_LEVEL_NONE = 0;
	public static final int ALERT_LEVEL_MILDEST = 100;
	public static final int ALERT_LEVEL_MILD = 200;
	public static final int ALERT_LEVEL_BIG = 300;
	public static final int ALERT_LEVEL_CRITICAL = 400;
	
	public static void main(String args[]){
		System.out.println("hello");
		getAllAlertTypes();
	}
	
	public static void getAllAlertTypes(){
		
		Field field[] = AlertLevels.class.getFields();

		for (int ii = 0; ii < field.length; ++ii) {
			String fieldName = field[ii].getName();
			int mod = field[ii].getModifiers();
			String modifiers = Modifier.toString(mod);
			
			System.out.println(fieldName);

			if (modifiers.equalsIgnoreCase("public static final")) { //$NON-NLS-1$
				System.out.println("was here");
			}
		}
	}

}
