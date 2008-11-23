<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,
		org.usip.osp.networking.*,
		org.usip.osp.communications.*,
		org.usip.osp.persistence.*,
		org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
		
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	pso.handleMakeAnnouncement(request);
	
	// Forward on in this case is used to forward to the select players
	if (pso.forward_on){
		pso.forward_on = false;
		response.sendRedirect("select_actors.jsp");
		return;
	}

%>
<html>
<head>
<title>Make Announcement Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
</head>

<body>
<h2>Make Announcement </h2>
<form name="form1" method="post" action="make_announcement.jsp">
<label><input name="player_target" type="radio" value="all" checked>
To All Players </label>
 / 
 <label>
 <input name="player_target" type="radio" value="some">
 To Some Players </label>
<input type="hidden" name="sending_page" value="add_news" />
  
  		  <p>
		  <textarea id="announcement_text" name="announcement_text" style="height: 310px; width: 710px;"></textarea>
		<script language="javascript1.2">
  			generate_wysiwyg('announcement_text');
		</script>
		  </p>
  
  
  <p>
    <label>
    <input type="submit" name="add_news" value="Submit">
    </label>
  </p>
</form>
<h2 align="center">
  Current Announcements Displayed Here</h2>
<table width="80%" border="1" align="center">
<tr><td width="25%" valign="top">To</td>
  <td width="19%" valign="top">Date</td>
  <td width="56%" valign="top">Message</td>
<tr>
<%
	for (ListIterator li = pso.getAllAnnouncements().listIterator(); li.hasNext();) {
			Alert al = (Alert) li.next();
%>
  <tr>
  	<td valign="top"><% if ((!al.isSpecific_targets())) { %>All<% } else { %>
  	  <%= pso.stringListToNames(request, al.getThe_specific_targets(), ", ")%>
    <% } %>	</td>
    <td valign="top"><%= al.getTimeOfAlert() %></td>
    <td valign="top"><%= al.getAlertMessage() %></td>
  </tr>
<%
	}
%>
</table>
<div align="center"></div>
<p align="center">&nbsp;</p>

<p>&nbsp; </p>
<p>&nbsp;</p>
</body>
</html>
<%
	
%>
