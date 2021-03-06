<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
		org.usip.osp.baseobjects.*,
		org.usip.osp.networking.*,
		org.usip.osp.persistence.*" 
	errorPage="/error.jsp" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	String db_schema = (String) request.getParameter("db_schema");
	
	if ((db_schema != null) && (db_schema.length() > 0)) {
		afso.schema = db_schema;
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>USIP Open Simulation Platform</title>




<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<script language="JavaScript" type="text/javascript" src="../help-bubble.js">
</script>
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
              <h1>Catalog of Customized Sections </h1>
              <br />
    <blockquote> 
      <p>This page shows all of the sections that have been customized on this database.</p>
      <h2>Customized for this Simulation </h2>
      <table border="1" width="100%"><tr><td>
      <table border="0" width="100%" cellspacing="1">
        <tr align="left" valign="top">
          <td><strong>Tab Heading</strong></td>
          <td><strong>Description</strong></td>
          <td><strong>Usage/Remove</strong></td>
        </tr>

        <%

		for (ListIterator li = BaseSimSection.getAllBase(afso.schema).listIterator(); li.hasNext();) {
			BaseSimSection bss = (BaseSimSection) li.next(); 
			
			String nameToSend = java.net.URLEncoder.encode(bss.getRec_tab_heading());
			
			if ((bss.isAuthorGeneratedSimulationSection())){
			
				boolean checkSectionInUse = SimulationSectionAssignment.checkUsage(afso.schema, bss.getId());
			%>
        <%  
			} // End of if not customized.
		
		}  %>
        
        <%

		for (ListIterator li = new CustomizeableSection().getAllCustomizable(afso.schema).listIterator(); li.hasNext();) {
			CustomizeableSection bss = (CustomizeableSection) li.next(); 
			
			if ((bss.isAuthorGeneratedSimulationSection()) || ((bss.isThisIsACustomizedSection()))){ 
			
			if ((bss.getSimId() != null) && (bss.getSimId().intValue() == afso.sim_id)) {
			
			String nameToSend = java.net.URLEncoder.encode(bss.getRec_tab_heading());
			
			boolean checkSectionInUse = SimulationSectionAssignment.checkUsage(afso.schema, bss.getId());
			%>
        <tr align="left" valign="top"> 
          <td><strong><%= bss.getRec_tab_heading() %></strong><br /></td>
        <td><%= bss.getDescription() %></td>
        <td><% 
				if (checkSectionInUse) { %>
          <a href="view_customized_section_usage.jsp?bss_id=<%= bss.getId().toString() %>">view usage</a>
          <% } else { %>
          <a href="delete_object.jsp?object_type=bss&objid=<%= bss.getId().toString() %>&object_info=<%= nameToSend %>">delete</a>
          <% } %>
          <form id="section_form" name="section_form" method="post" action="<%= bss.getMakePage() %>" target="bodyinleft">
<input type="hidden" name="tab_heading" value="<%= bss.getRec_tab_heading() %>" />
<input name="edit_this_section" type="submit" value="Edit This Section" />
</form>
          
          </td>
      </tr>
        <%  
			} // End of if for this simulation
			} // End of if not customized or author generated.
		}  %>
        </table>
    </td></tr></table>
      <h2>&nbsp;</h2>
      <h2>Customized for other Simulations</h2>
      <table border="1" width="100%">
        <tr>
          <td><table border="1" width="100%" cellspacing="1">
              <tr align="left" valign="top">
                <td><strong>Simulation</strong></td>
                <td><strong>Tab Heading</strong></td>
                <td><strong>Description</strong></td>
                </tr>
              <%

		for (ListIterator li = BaseSimSection.getAllBase(afso.schema).listIterator(); li.hasNext();) {
			BaseSimSection bss = (BaseSimSection) li.next(); 
			
			String nameToSend = java.net.URLEncoder.encode(bss.getRec_tab_heading());
			
			if ((bss.isAuthorGeneratedSimulationSection())){
			
				boolean checkSectionInUse = SimulationSectionAssignment.checkUsage(afso.schema, bss.getId());
			%>
              <%  
			} // End of if not customized.
		
		}  %>
              <%

		for (ListIterator li = new CustomizeableSection().getAllCustomizable(afso.schema).listIterator(); li.hasNext();) {
			CustomizeableSection bss = (CustomizeableSection) li.next(); 
			
			if ((bss.isAuthorGeneratedSimulationSection()) || ((bss.isThisIsACustomizedSection()))){ 
			
			if ((bss.getSimId() != null) && (bss.getSimId().intValue() != afso.sim_id)) {
			
			String nameToSend = java.net.URLEncoder.encode(bss.getRec_tab_heading());
			
			boolean checkSectionInUse = SimulationSectionAssignment.checkUsage(afso.schema, bss.getId());
			%>
              <tr align="left" valign="top">
                <td><%= USIP_OSP_Cache.getSimulationNameById(request, afso.schema, bss.getSimId()) %></td>
                <td><strong><%= bss.getRec_tab_heading() %></strong><br /></td>
                <td><%= bss.getDescription() %></td>
                </tr>
              <%  
			  
			  } // End of if not for the sim being worked on.
			} // End of if not customized or author generated.
		}  %>
          </table></td>
        </tr>
      </table>
      <p>&nbsp; </p>
    </blockquote>
    <blockquote>
      <p>&nbsp;</p>
      </blockquote>			</td>
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
<%
	
%>
