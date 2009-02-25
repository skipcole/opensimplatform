<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page contentType="text/html; charset=ISO-8859-1" language="java" import="java.sql.*,java.util.*" errorPage="../error.jsp" %>
<%

        // Establish the cache of login tickets
        Hashtable sim_section_info = new Hashtable();
        getServletContext().setAttribute("sim_section_info", sim_section_info);

        // Establish the cache of login tickets
        Hashtable tickets = new Hashtable();
        getServletContext().setAttribute("activeTickets", tickets);
        
		Hashtable broadcast_conversations = new Hashtable();
        getServletContext().setAttribute("broadcast_conversations", broadcast_conversations);
		
        // Establish the cache of chat conversations
        Hashtable conversations = new Hashtable();
        getServletContext().setAttribute("conversations", conversations);
        
        // Establish the cache of conversation actors
        Hashtable conversation_actors = new Hashtable();
        getServletContext().setAttribute("conversation_actors", conversation_actors);
        
        // Establish the cache of charts.
        Hashtable charts = new Hashtable();
        getServletContext().setAttribute("charts", charts);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<title>Open Simulation Platform Control Page</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Reset of Simulation Caches</h1>
              <br />
 Values stored in cache have been reset.			</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>
