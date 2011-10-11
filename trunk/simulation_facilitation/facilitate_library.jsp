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
      <blockquote>
        
        <p>Below are all of the currently published Simulations for your organization.</p>
        </blockquote>
      <ul>
        <li>Click on the name of the simulation to review it.</li>
        <li>Clicking on the 'Launch Game' button will lead you through the short path of initiating a game (entering in schedule information, assigning players, etc.) You do not need to complete all steps in that path at one sitting, but can come back to it later via the 'My Sims' section above.</li>
      </ul>
      <blockquote>
        <table width="100%" border="1" cellspacing="0" cellpadding="2">
          <tr valign="top"> 
            <td width="15%"><strong>Name / Version</strong></td>
            <td width="16%"><strong>Author</strong></td>
            <td width="16%"><strong>Keywords</strong></td>
            <td width="16%"><strong>Publish Date</strong></td>
            <td width="16%"><strong>User Comments</strong></td>
            <td width="16%"><strong>Launch Game</strong></td>
            </tr>
          <% 
		  
		  java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd yyyy");
		  
		  for (ListIterator li = Simulation.getAllExternallyPublished(afso.schema).listIterator(); li.hasNext();) {
			Simulation sim = (Simulation) li.next();
			
			String pubDate = "";
			
			if (sim.getPublishDate() != null) {
				pubDate = sdf.format(sim.getPublishDate());
			}
			
			%>
          <tr valign="top"> 
            <td>
              <a href="facilitator_review_sim.jsp?loadSim=true&sim_id=<%= sim.getId() %>">
                <%= sim.getSimulationName() %> : <%= sim.getVersion() %></a></td>
            <td><%= sim.getCreation_org() %></td>
            <td><%= sim.getListingKeyWords() %></td>
            <td><%= pubDate %></td>
            <td><a href="sim_ratings.jsp?sim_id=<%= sim.getId() %>">
              <% if (true) { %>
              None
              <% } else { %>
              *****
              <% } %>
              </a>
              </td>
            <td><form id="form1" name="form1" method="post" action="facilitateweb.jsp?ftab=launch" target="_top">
              <input type="hidden" name="sim_id" value="<%= sim.getId() %>" />
              <input type="hidden" name="loadSim" value="true" />
              <input type="hidden" name="newRunningSim" value="true" />
              <input type="submit" name="button" id="button" value="Launch" />
              </form></td>
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