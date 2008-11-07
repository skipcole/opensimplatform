<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	pso.setSectionRouterPassThroughParameters(request);
	///////////////////////////////////////////////////////////////////////////////////////
	String page_id = request.getParameter("page_id");
	String bss_id = request.getParameter("bss_id");
	String command = request.getParameter("command");
	pso.tab_heading = (String) request.getParameter("tab_heading");
    String tab_pos = (String) request.getParameter("tab_pos");
    String universal = (String) request.getParameter("universal");
	///////////////////////////////////////////////////////////////////////////////////////
	
	// bss_id is either an integeer, or the string 'create_simulation_section.jsp'
	if (bss_id.equalsIgnoreCase("new_section")){
		response.sendRedirect("create_simulation_section.jsp");
		return;
	}
	
	// Base sim section is retrieved by ID
	BaseSimSection bss = BaseSimSection.getMe(pso.schema, bss_id);
	
	if(command.equalsIgnoreCase("Add Section")){
		
		if (bss.getClass().getName().equalsIgnoreCase("org.usip.osp.baseobjects.BaseSimSection")){
			// Here we add the class straight away.
			pso.addSectionFromRouter(request, universal);
			response.sendRedirect(pso.backPage);
			return;
		} else if (bss.getClass().getName().equalsIgnoreCase("org.usip.osp.baseobjects.CustomizeableSection")){
			
			session.setAttribute("tab_pos", tab_pos);
			session.setAttribute("tab_heading", pso.tab_heading);
			session.setAttribute("universal", universal);
			
			pso.custom_page = bss_id;
			
			bss = null;
			
			CustomizeableSection cbss = CustomizeableSection.getMe(pso.schema, bss_id);
			
			if (!cbss.isHasASpecificMakePage()) {
				response.sendRedirect("customize_page.jsp?custom_page=" + new Long(bss_id));
			} else {
				response.sendRedirect(cbss.getSpecificMakePage() + "?custom_page=" + new Long(bss_id));
			}
			
			return;
		}
	}

	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Open Simulation Platform Control Page</title>
<!-- TemplateParam name="theBodyInfo" type="text" value="" --> 
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>
</head>
<body onLoad="loadFirstInfo();">
<BR />
<table width="720" bgcolor="#DDDDFF" align="center" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td colspan="3">
</td>
  </tr>
  <tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672"><p>This page should not be scene.</p>
      <p>bss_id was: <%= bss_id %></p>
      <p>bss rec tab was: <%= bss.getRec_tab_heading() %> </p>
	  
	  The class  is <%= bss.getClass().getName() %>
	  
	  </td>
    <td width="24" height="24"  >&nbsp;</td>
  </tr>
</table>
<p>&nbsp;</p>
<p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP 
  Open Source Software Project</a>. </p>
</body>
</html>
<%
	
%>