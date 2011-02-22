<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.persistence.*" 
	errorPage="/error.jsp" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
		
	//////////////////////////////////
	List simList = Simulation.getAll(afso.schema);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
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
      <h1>Online Simulation Library </h1>
    <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" --> 
      <form action="instructor_home.jsp" method="post" name="form1" id="form1">
	  	<input type="hidden" name="sending_page" value="create_simulation" />
        <blockquote>
        <blockquote>
          <p>&nbsp;</p>
        </blockquote>
      </form>
      <blockquote>
        
        <p>Below are all of the currently published Simulations for your organization.</p>
        <p>Click on the name of the simulation template to begin preparing a play 
          session.</p>
        <table width="100%" border="1" cellspacing="0" cellpadding="2">
          <tr valign="top"> 
            <td width="15%"><strong>Name / Version</strong></td>
            <td width="16%"><strong>Author</strong></td>
            <td width="16%"><strong>Keywords</strong></td>
            <td width="16%"><strong>Publish Date</strong></td>
            <td width="16%"><strong>Review</strong></td>
            <td width="16%"><strong>User Comments</strong></td>
          </tr>
          <% 
		  for (ListIterator li = Simulation.getAllPublished(afso.schema).listIterator(); li.hasNext();) {
			Simulation sim = (Simulation) li.next();
			%>
          <tr valign="top"> 
            <td><a href="facilitateweb.jsp?loadSim=true&sim_id=<%= sim.getId() %>" target="_top"><%= sim.getSimulationName() %> : <%= sim.getVersion() %></a></td>
            <td>ETCD</td>
            <td><%= sim.getListingKeyWords() %></td>
            <td>1/2009</td>
            <td><a href="../simulation_authoring/review_sim.jsp?loadSim=true&sim_id=<%= sim.getId() %>">Review</a></td>
            <td><a href="sim_ratings.jsp?sim_id=<%= sim.getId() %>">
			<% if (true) { %>
            None
            <% } else { %>
            *****
            <% } %>
            </a>
            </td>
          </tr>
          <% } %>
        </table>
<br>
      </blockquote>
      <p align="center"></p>

      <!-- InstanceEndEditable -->
			</td>
		</tr>
		</table>
	</td>
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
<!-- InstanceEnd --></html>