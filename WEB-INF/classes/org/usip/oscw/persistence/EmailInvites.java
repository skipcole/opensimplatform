package org.usip.oscw.persistence;

import java.sql.*;
import java.util.*;

import javax.persistence.*;

import org.hibernate.Session;
import org.hibernate.annotations.Proxy;
import org.usip.oscw.baseobjects.User;
import org.usip.oscw.baseobjects.UserAssignment;
import org.usip.oscw.networking.LoggedInTicket;

/**
 * @author Ronald "Skip" Cole
 * 
 * This file is part of the USIP Online Simulation Platform.<br>
 * 
 * The USIP Online Simulation Platform is free software; you can
 * redistribute it and/or modify it under the terms of the new BSD Style license
 * associated with this distribution.<br>
 * 
 * The USIP Online Simulation Platform is distributed WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. <BR>
 * 
 */
@Entity
@Table(name = "EMAIL_INVITES")
@Proxy(lazy = false)
public class EmailInvites {
	
	private String email_address;
	
	private String invitor_email_address;
	
	private Long user_id;

}
