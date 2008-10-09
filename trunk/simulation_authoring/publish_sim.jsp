<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,java.io.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*" 
	errorPage="" %>
<% 
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	pso.backPage = "publish_sim.jsp";
	
	String sending_page = (String) request.getParameter("sending_page");
	String command = (String) request.getParameter("command");
	String sim_key_words = (String) request.getParameter("sim_key_words");
	
	if ( (sending_page != null) && (sending_page.equalsIgnoreCase("publish_sim"))){
		pso.handlePublishing(command, sim_key_words);
	}
	
	Simulation simulation = new Simulation();	
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>USIP Online Simulation Platform</title>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" --><!-- InstanceEndEditable -->
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
    <td width="80%" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform </h1></td>
    <td align="right" background="../Templates/images/top_fade.png" width="20%"> 

	  <div align="center">
	    <table border="0" cellspacing="1" cellpadding="0">
	<%  if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
        <tr>
          <td><div align="center"><a href="intro.jsp" target="_top" class="menu_item"><img src="../Templates/images/home.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
	<% } else { %>
		<tr>
          <td><div align="center"><a href="../simulation_facilitation/index.jsp" target="_top" class="menu_item"><img src="../Templates/images/home.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
	<% } %>	
        <tr>
          <td><div align="center"><a href="../simulation_user_admin/my_profile.jsp" class="menu_item"><img src="../Templates/images/my_profile.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
        <tr>
          <td><div align="center"><a href="logout.jsp" target="_top" class="menu_item"><img src="../Templates/images/logout.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
      </table>	  
	  </div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0"><% 
		
		String bgColor_think = "#475DB0";
		String bgColor_create = "#475DB0";
		String bgColor_play = "#475DB0";
		String bgColor_share = "#475DB0";
		
		pso.findPageType(request);
		
		if (pso.page_type == 1){
			bgColor_think = "#9AABE1";
		} else if (pso.page_type == 2){
			bgColor_create = "#9AABE1";
		} else if (pso.page_type == 3){
			bgColor_play = "#9AABE1";
		} else if (pso.page_type == 4){
			bgColor_share = "#9AABE1";
		}
		
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
		
		<table border="0" cellpadding="0" cellspacing="0" >
		   <tr>
		<td bgcolor="<%= bgColor_think %>"><a href="../simulation_planning/index.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;THINK&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td>
	    <td bgcolor="<%= bgColor_create %>"><a href="creationwebui.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;CREATE&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td>
		<td bgcolor="<%= bgColor_play %>"><a href="../simulation_facilitation/facilitateweb.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;PLAY&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td>
        <td bgcolor="<%= bgColor_share %>"><a href="../simulation_sharing/index.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;SHARE&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		   </tr>
		</table>
	<% } %></td>
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
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
			<!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Publish Simulation</h1>
      <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" -->
	  <blockquote>
        <% 
			if (pso.simulationSelected) {
		%>
        <p>Publishing simulation <strong><%= simulation.getDisplayName() %></strong>.<br>
          (If you would like to look at a different simulation, <a href="select_simulation.jsp">click 
          here</a>.)</p>
      </blockquote>
	  
			<blockquote><hr />
			<form name="form1" id="form1" method="post" action="publish_sim.jsp">
            
          <table width="80%" border="0" cellspacing="2" cellpadding="1">
            <tr valign="top"> 
              <td>Simulation Name</td>
                
              <td><%= simulation.getDisplayName() %></td>
              </tr>
              
            <tr valign="top"> 
              <td>Simulation Key Words</td>
                
              <td> 
                <textarea name="sim_key_words"><%= simulation.getListingKeyWords() %></textarea></td>
              </tr>
              <tr> 
                <td>&nbsp;</td>
                <td>
				<input type="hidden" name="sending_page" value="publish_sim">
				<% if (simulation.isReadyForPublicListing()){ %>
				<input name="command" type="submit" id="submit_u" value="Un - Publish It!" /> 
				<% } else { %>
				<input name="command" type="submit" id="submit_p" value="Publish It!" />
                <% } %>
              </td>
              </tr>
            </table>
		  </form>
        </blockquote>
		
        
		  <% } else { // End of if have set simulation id. %>
      <blockquote>
		<%@ include file="select_message.jsp" %></p>
      </blockquote>
	  <p>
        <% } // End of if have not set simulation for edits. %>
      </p>
      <hr>
      <p>Below are listed all current simulations and their publication state. </p>
        
      <table width="100%" border="1" cellspacing="0" cellpadding="2">
        <tr valign="top"> 
          <td width="15%"><strong>Name / Version</strong></td>
          <td width="16%"><strong>Status</strong></td>
          <td width="16%"><strong>Author</strong></td>
          <td width="16%"><strong>Keywords</strong></td>
          <td width="16%"><strong>Date Marked Ready</strong></td>
        </tr>
        <% 
		  
		  for (ListIterator li = Simulation.getAll(pso.schema).listIterator(); li.hasNext();) {
			Simulation sim = (Simulation) li.next();
			
			String ready = "Under Development";
			
			if (sim.isReadyForPublicListing()){
				ready = "Available for Use";
			}
			%>
        <tr valign="top"> 
          <td><%= sim.getName() %> : <%= sim.getVersion() %></td>
          <td><%= ready %></td>
          <td><%= sim.getCreator() %></td>
          <td><%= sim.getListingKeyWords() %></td>
          <td>&nbsp;</td>
        </tr>
        <% } %>
      </table>
        <p>&nbsp;</p>
		    <blockquote>
		  <p align="center"><a href="../simulation_facilitation/facilitateweb.jsp" target="_top">On To The Play 
        Section</a></p>
		</blockquote>
	</blockquote>

      
      <a href="review_sim.jsp">&lt;-- Back</a><!-- InstanceEndEditable -->
			</td>
		</tr>
		</table>
	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
<!-- InstanceEnd --></html>
<%
	
%>