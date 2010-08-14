package org.usip.osp.communications;

import java.util.Date;

public interface EventInterface {
	
	public static int EVENT_TYPE_EVENT = 1;
	
	public static int EVENT_TYPE_ALERT = 2;
	
	public static int EVENT_TYPE_DOCUMENT = 3;
	
	public static int EVENT_TYPE_EMAIL = 4;
	
	public static int EVENT_TYPE_PRIVATE_MSG = 5;

	public Date getEventStartTime();
	
	public Date getEventEndTime();
	
	public String getEventTitle();
	
	public int getEventType();
	
	public String getEventMsgBody();
	
	public Long getEventId();
	
	public String getEventClass();
	
	public Long getEventParentId();
	
	public Long getEventParentClass();
	
}
