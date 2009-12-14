<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	Simulation simulation = new Simulation();	
	
	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}
	
	SimulationMetaPhase metaPhaseOnScratchPad = afso.handleCreateOrUpdateMetaPhase(request);


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
			  <h1>Create Simulation MetaPhase</h1>
			  <br />
            <blockquote> 
              <% 
			if (afso.sim_id != null) {

		%>
              <blockquote>
                <p>Create metaphase for the simulation <strong><%= simulation.getDisplayName() %></strong>. </p>
            <form action="create_simulation_metaphases.jsp" method="post" name="form1" id="form1">
              <table width="80%" border="0" cellspacing="2" cellpadding="2">
                <tr> 
                  <td valign="top">Meta Phase Name:</td>
                  <td valign="top"><input type="text" name="meta_phase_name" value="<%= metaPhaseOnScratchPad.getMetaPhaseName() %>" /></td>
                </tr>
                <tr>
                  <td valign="top">Meta Phase Notes:</td>
                  <td valign="top"><label>
                    <textarea name="meta_phase_notes" id="textarea" cols="45" rows="5"><%= metaPhaseOnScratchPad.getMetaPhaseNotes() %></textarea>
                    </label></td>
                </tr>
                <tr>
                  <td valign="top">Meta Phase Color:</td>
                  <td valign="top"><label>
                    <input type="text" name="meta_phase_color" id="meta_phase_color" value="<%= metaPhaseOnScratchPad.getMetaPhaseColor() %>" />
                  </label></td>
                </tr>
                <tr>
                  <td valign="top">Meta Phase Image:</td>
                  <td valign="top">&nbsp;</td>
                </tr>
                <tr> 
                  <td valign="top">&nbsp;</td>
                  <td valign="top">
                    <%
				if (metaPhaseOnScratchPad.getId() == null) {
				%>
                    <input type="submit" name="command" value="Create" />
                    <%
				} else {
				%>
                    <input type="hidden" name="mp_id" value="<%= metaPhaseOnScratchPad.getId() %>" />
                    <input type="hidden" name="sim_id" value="<%= simulation.getId() %>" />
                    <input type="submit" name="command" value="Clear" tabindex="6" />
                    <input type="submit" name="command" value="Update" />
                    <%
					}
				%>                </td>
                </tr>
                </table>
              <p>&nbsp;</p>
            </form>
            <p>Below are listed all of the current  meta phases for this simulation:</p>
            <table width="100%" border="1" cellspacing="2" cellpadding="2">
              <tr> 
                <td width="20%" valign="top"><h2>Meta Phase Name</h2></td>
                <td width="80%" valign="top"><h2>Meta Phase Notes</h2></td>
                <td width="40" valign="top"><h2>Remove</h2></td>
              </tr>
              <%
			  
		for (ListIterator li = SimulationMetaPhase.getAllForSim(afso.schema, afso.sim_id).listIterator(); li.hasNext();) {
			SimulationMetaPhase sp = (SimulationMetaPhase) li.next();			
		%>
              <tr>
                <td valign="top"><a href="create_simulation_metaphases.jsp?command=Edit&mp_id=<%= sp.getId().toString() %>"><%= sp.getMetaPhaseName() %></a></td>
                <td valign="top"><%= sp.getMetaPhaseNotes() %></td> 
                <td valign="top">
                  
                  <a href="delete_object.jsp?phase_sim_id=<%= afso.sim_id %>&object_type=phase&objid=<%= sp.getId().toString() %>&object_info=<%= sp.getMetaPhaseName() %>">remove</a>
                             </td>
              </tr>
              <%
	}
%>
              </table>
              </blockquote>
            </blockquote>
            <% } else { // End of if have set simulation id. %>
            <blockquote>
              <p>
                <%@ include file="select_message.jsp" %></p>
                  </blockquote>
            <% } // End of if have not set simulation for edits. %>            <a href="advanced_phase_features.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
		</tr>
		</table>	</td>
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
</html>
<%
	
%>