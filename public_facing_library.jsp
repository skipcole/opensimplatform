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
		  
		  java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd yyyy");
		  
		  for (ListIterator li = Simulation.getAllPublished(afso.schema).listIterator(); li.hasNext();) {
			Simulation sim = (Simulation) li.next();
			
			String pubDate = sdf.format(sim.getPublishDate());
			
			%>
            <tr valign="top"> 
              <td><a href="simulation_facilitation/facilitateweb.jsp?loadSim=true&amp;sim_id=<%= sim.getId() %>" target="_top"><%= sim.getSimulationName() %> : <%= sim.getVersion() %></a></td>
              <td><%= sim.getCreation_org() %></td>
              <td><%= sim.getListingKeyWords() %></td>
              <td><%= pubDate %></td>
              <td><a href="simulation_facilitation/facilitator_review_sim.jsp?loadSim=true&amp;sim_id=<%= sim.getId() %>">Review</a></td>
              <td><a href="simulation_facilitation/sim_ratings.jsp?sim_id=<%= sim.getId() %>">
                <% if (true) { %>
                None
                <% } else { %>
                *****
                <% } %>
                </a>                </td>
            </tr>
            <% } %>
            </table>
  <br>
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