<%@ page contentType="text/html; charset=iso-8859-1" language="java" 
import="java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*" 
errorPage="" %>
<%
	String custom_page = request.getParameter("custom_page");
	
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	String tab_heading = (String) session.getAttribute("tab_heading");
    String tab_pos = (String) session.getAttribute("tab_pos");
	String universal = (String) session.getAttribute("universal");
	
	System.out.println("cp universal was : " + universal);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	MultiSchemaHibernateUtil.beginTransaction(pso.schema);
	CustomizeableSection cs = (CustomizeableSection) MultiSchemaHibernateUtil.getSession(pso.schema).get(CustomizeableSection.class, new Long(custom_page));
	
	MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>USIP Online Simulation Platform</title>
<!-- InstanceEndEditable --><!-- InstanceBeginEditable name="head" --><!-- InstanceEndEditable -->
<link href="../usip_oscw.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">

<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="80%" valign="top"><!-- InstanceBeginEditable name="pageTitle" --><H1>Creating customized '<%= cs.getRec_tab_heading() %>' </H1><!-- InstanceEndEditable --></td>
    <td width="20%" align="right" valign="top"> 
		<% 
		String canEdit = (String) session.getAttribute("author");
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
		<a href="../simulation_planning/index.jsp" target="_top">Think</a><br>
	    <a href="creationwebui.jsp" target="_top">Create</a><br>
		<a href="../simulation_facilitation/facilitateweb.jsp" target="_top">Play</a><br>
        <a href="../simulation_sharing/index.jsp" target="_top">Share</a>
		<% } %>
		</td>
  </tr>
</table>
<BR />
<table width="720" bgcolor="#DDDDFF" align="center" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
</tr>
<tr> 
    <td colspan="3"><!-- InstanceBeginEditable name="pageBody" -->
<form id="form1" name="form1" method="post" action="process_custom_page.jsp">
<input type="hidden" name="tab_pos" value="<%= tab_pos %>">
<input type="hidden" name="universal" value="<%= universal %>">
  <blockquote>
    <p>
      <input type="hidden" name="custom_page" value=<%= custom_page %> />
    </p>
  </blockquote>
        <table>
          <tr valign="top"> 
            <td>New Tab Heading </td>
            <td> 
              <input type="text" name="tab_heading" value="<%= tab_heading %>"/></td>
    </tr>
    <%
		for (Enumeration e = cs.getMeta_content().keys(); e.hasMoreElements();){
			String key = (String) e.nextElement();
%>
          <tr valign="top"> 
            <td><%= cs.getMeta_content().get(key) %> </td>
            <td> 
              <%
	  	String currVal = "";
		try {
			currVal = (String) cs.getContents().get(key);
		} catch (Exception eal){
			System.out.println("current value null: " + eal.getMessage());
		}
		
		String dataType = "";
		try {
			dataType = (String) cs.getMeta_data_content().get(key);
		} catch (Exception eal){
			System.out.println("dataType value null: " + eal.getMessage());
		}
	  %>
	  <% if ((dataType != null) && (dataType.equalsIgnoreCase("textarea"))) { %>
	   <textarea name="<%= key %>" cols="60" rows="5"><%= currVal %></textarea>
	  <% } else { %>
	  <input type="text" name="<%= key %>" value="<%= currVal %>" />
	  <% } %>
	  </td>
    </tr>
    <%
		}
%>
  </table>
        <blockquote>
    <p>
      <input type="submit" name="Submit" value="Submit" />
    </p>
  </blockquote>
</form>
<blockquote>
  <p>&nbsp;</p>
  <p><a href="set_sim_sections.jsp"></a></p>
</blockquote>
<!-- InstanceEndEditable --></td>
  </tr>
<tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24"  >&nbsp;</td>
  </tr>
</table>

<p>&nbsp;</p>
<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr> 
    <td align="left" valign="bottom"> 
	<% 
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
	<a href="intro.jsp" target="_top">Home 
      </a>
	  <% } else { %>
	  <a href="../simulation_facilitation/index.jsp" target="_top">Home 
      </a>
	  <% } %>
	  </td>
    <td align="right" valign="bottom"><a href="../simulation_user_admin/my_profile.jsp">My 
      Profile</a> </td>
  </tr>
  <tr>
    <td align="left" valign="bottom"><a href="logout.jsp" target="_top">Logout</a></td>
    <td align="right" valign="bottom">&nbsp;</td>
  </tr>
</table>
<p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/usiponlinesimulationcreationwizard/">USIP Open Source Software Project</a>. </p>
</body>
<!-- InstanceEnd --></html>
