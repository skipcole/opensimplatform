<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*" 
	errorPage="" %>
<% 
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	String tab_heading = (String) session.getAttribute("tab_heading");
    String tab_pos = (String) session.getAttribute("tab_pos");
	String universal = (String) session.getAttribute("universal");
	
	String new_tab_heading = request.getParameter("tab_heading");
	if ((new_tab_heading != null) && (new_tab_heading.length() > 0)) {
		tab_heading = new_tab_heading;
	}
	
	String custom_page = request.getParameter("custom_page");
	
	MultiSchemaHibernateUtil.beginTransaction(pso.schema);
	CustomizeableSection cs = (CustomizeableSection) MultiSchemaHibernateUtil.getSession(pso.schema).get(CustomizeableSection.class, new Long(custom_page));
	MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);
	
	// Determine if setting sim to edit.
	String sending_page = (String) request.getParameter("sending_page");
	
	String save_page = (String) request.getParameter("save_page");
	String save_and_add = (String) request.getParameter("save_and_add");
	
	if ( (sending_page != null) && 
		((save_page != null)  || (save_and_add != null) ) 
	
	&& (sending_page.equalsIgnoreCase("make_text_page"))){
		// If this is the original custom page, make a new page
		if (!(cs.isThisIsACustomizedSection())){
			System.out.println("making copy");
			cs = cs.makeCopy(pso.schema);
			custom_page = cs.getId() + "";
		}
		//Update page values 
		String text_page_text = (String) request.getParameter("text_page_text");
		cs.setBigString(text_page_text);
		cs.setRec_tab_heading(tab_heading);
		cs.save(pso.schema);
		
		if (save_and_add != null) {
			// add section
			pso.addSectionFromProcessCustomPage(cs.getId(), tab_pos, tab_heading, request, universal);
			// send them back
			response.sendRedirect(pso.backPage);
		
		}
		
		
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Online Simulation Platform Control Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>
<!-- InstanceEndEditable --><!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
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
    <td width="80%" valign="top"><!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Custom Text Page</h1>
    <!-- InstanceEndEditable --></td>
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
      <blockquote> 
        <p>Enter the text that will appear on this page. <br>
        </p>
      </blockquote>
      <form action="make_text_page.jsp" method="post" name="form2" id="form2">
        <blockquote>Tab Heading: 
          <input type="text" name="tab_heading" value="<%= tab_heading %>"/>
          <p>
            <textarea id="text_page_text" name="text_page_text" style="height: 710px; width: 710px;"><%= cs.getBigString() %></textarea>

		<script language="javascript1.2">
  			generate_wysiwyg('text_page_text');
		</script>
          </p>
          <p> 
            <input type="hidden" name="custom_page" value="<%= custom_page %>" />
            <input type="hidden" name="sending_page" value="make_text_page" />
            <input type="submit" name="save_page" value="Save" />
            <input type="submit" name="save_and_add" value="Save and Add Section" />
          </p>
          <p><input type="submit" name="create_duplicate" value="Create Duplicate" disabled /></p>
        </blockquote>
      </form>
	  <a href="<%= pso.backPage %>">&lt;-- Back</a><!-- InstanceEndEditable --></td>
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
<%
	
%>
