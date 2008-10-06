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
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
	background-image: url(../Templates/images/page_bg.png);
	background-repeat: repeat-x;
}
-->
</style>
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">
<% String canEdit = (String) session.getAttribute("author"); %>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="666" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform </h1></td>
    <td align="right" background="../Templates/images/top_fade.png"> 

	  <div align="center">
	    <table border="0" cellspacing="4" cellpadding="4">
	<%  if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
        <tr>
          <td><a href="intro.jsp" target="_top">Home</a></td>
        </tr>
	<% } else { %>
		<tr>
          <td><a href="../simulation_facilitation/index.jsp" target="_top">Home </a></td>
        </tr>
	<% } %>	
        <tr>
          <td><a href="../simulation_user_admin/my_profile.jsp"> My Profile</a></td>
        </tr>
        <tr>
          <td><a href="logout.jsp" target="_top">Logout</a></td>
        </tr>
      </table>	  
	  </div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0"><% 
		
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
		
		<table border="0" cellpadding="0" cellspacing="0" >
		   <tr>
		<td><a href="../simulation_planning/index.jsp" target="_top" class="menu_item">THINK</a></td>
		<td>&nbsp;</td>
	    <td><a href="creationwebui.jsp" target="_top" class="menu_item">CREATE</a></td>
		<td>&nbsp;</td>
		<td><a href="../simulation_facilitation/facilitateweb.jsp" target="_top" class="menu_item">PLAY</a></td>
		<td>&nbsp;</td>
        <td><a href="../simulation_sharing/index.jsp" target="_top" class="menu_item">SHARE</a></td>
		   </tr>
		</table>
	<% } %></td>
  </tr>
  <tr>
    <td colspan="2" valign="top"><!-- InstanceBeginEditable name="pageTitle" --><H1>Creating customized '<%= cs.getRec_tab_heading() %>' </H1><!-- InstanceEndEditable --></td>
    <td width="194" align="right" valign="top">		</td>
  </tr>
</table>
<BR />
<table width="90%" bgcolor="#FFFFFF" align="center" border="1" cellspacing="0" cellpadding="0">
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
<!-- InstanceEndEditable -->
    <p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
<!-- InstanceEnd --></html>
