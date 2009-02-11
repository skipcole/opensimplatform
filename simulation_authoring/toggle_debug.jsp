<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*,java.util.*,org.usip.osp.baseobjects.*" errorPage="" %>
<%

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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Open Simulation Platform Control Page</title>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
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
			<!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Toggle Debug</h1>
      <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" --> 
      <blockquote>
        <p> 
          <% if (Debug.debug_on) { 
		Debug.debug_on = false;
	%>
          Debugging is now off 
          <% } else { 
		Debug.debug_on = true;
	%>
          Debugging is now on 
          <% } %>
        </p>
      </blockquote>
      <p align="center"><a href="../simulation_admin/software_development_section.jsp">Back to Simulation 
        Software Administration</a></p>
      <!-- InstanceEndEditable -->
			</td>
		</tr>
		</table>
	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>.</p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
<!-- InstanceEnd --></html>
