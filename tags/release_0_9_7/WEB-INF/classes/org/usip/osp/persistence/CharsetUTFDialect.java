package org.usip.osp.persistence;

import org.hibernate.dialect.MySQL5InnoDBDialect;

/*         This file is part of the USIP Open Simulation Platform.<br>
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
public class CharsetUTFDialect  extends MySQL5InnoDBDialect{

	public String getTableTypeString() { 
		return " ENGINE=InnoDB DEFAULT CHARSET=utf8"; 
	}
	
}
