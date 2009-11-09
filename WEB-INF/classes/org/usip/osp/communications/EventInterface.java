package org.usip.osp.communications;

import java.util.Date;

public interface EventInterface {

	public Date getEventStartTime();
	
	public Date getEventEndTime();
	
	public String getEventTitle();
	
	public String getEventMsgBody();
	
}
