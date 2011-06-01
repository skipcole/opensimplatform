<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.persistence.*" 
	errorPage="/error.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>



<link href="usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><a href="index.jsp"><img src="Templates/images/logo_top.png" width="120" height="100" border="0" /></a></td>
    <td width="80%" valign="middle"  background="Templates/images/top_fade.png"><h1 class="header">&nbsp;USIP Open Simulation Platform </h1></td>
    <td align="right" background="Templates/images/top_fade.png" width="20%"> 

	  <div align="center"></div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0"></td>
  </tr>
  <tr>
  	<td width="120" align="right" valign="top">&nbsp;</td>
    <td colspan="1" valign="top"><br /></td>
    <td width="194" align="right" valign="top">		</td>
  </tr>
</table>
<BR />
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Online Simulation Library </h1>
              <br />
      <blockquote>
        
        <p>Below are all of the  published Simulations for your organization.</p>
          <table width="100%" border="1" cellspacing="0" cellpadding="2">
            <tr valign="top"> 
              <td width="15%"><strong>Name / Version / Org </strong></td>
              <td width="16%"><strong>Keywords</strong></td>
              <td width="16%"><strong># Players (min/max) </strong></td>
              <td width="16%"><strong>Recommended Play Time (hours) </strong></td>
              <td width="16%"><strong>Review</strong></td>
              <td width="16%"><strong>User Comments</strong></td>
            </tr>
            <% 
		  
			// Loop over schema
			for (ListIterator si = SchemaInformationObject.getAll().listIterator(); si.hasNext();) {
				SchemaInformationObject sio = (SchemaInformationObject) si.next();
			
		  for (ListIterator li = Simulation.getAllExternallyPublished(sio.getSchema_name()).listIterator(); li.hasNext();) {
			Simulation sim = (Simulation) li.next();
			
			PlannedPlaySessionParameters ppsp = sim.getPPSP(sio.getSchema_name());
			
			if (ppsp == null) {
				ppsp = new PlannedPlaySessionParameters();
			}
			%>
            <tr valign="top"> 
              <td><%= sim.getSimulationName() %> / <%= sim.getVersion() %> / <%= sim.getCreation_org() %></a></td>
              <td><%= sim.getListingKeyWords() %></td>
              <td><%= ppsp.getMinNumPlayers() %> / <%= ppsp.getMaxNumPlayers() %></td>
              <td><%= ppsp.getRecommendedPlayTime() %></td>
              <td><a href="public_review_of_sim.jsp?loadSim=true&db_schema=<%= sio.getSchema_name() %>&sim_id=<%= sim.getId() %>">Review</a></td>
              <td><!-- a href="simulation_facilitation/sim_ratings.jsp?sim_id= < = sim.getId() %> >
                < %  if (true) { % >
                None
                < % } else { % >
                *****
                < % } % >
                </a --> (Coming Soon!)               </td>
            </tr>
            	<% } // End of loop over sims in a schema %>
			<% } // End of loop over schema %>
            </table>
          <p>&nbsp;</p>
          <h1>Access to Run </h1>
          <p>Instructor access is required to run any of the above simulations. If you are interested in becoming an instructor on this platform, <a href="instructor_application.jsp">please click here</a>. </p>
          <p><br>
              </p>
      </blockquote>      <p align="center"></p>			</td>
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