<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
		org.usip.osp.networking.*,
		org.usip.osp.communications.*,
		org.usip.osp.persistence.*,
		org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
		
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}
	
	pso.handleMakeRatingAnnouncement(request);
	
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
// WYSIWYG Width and Height
wysiwygWidth = 1010;
wysiwygHeight = 210;
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
</head>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<h2>Make Rating Announcement </h2>
<form name="form1" method="post" action="make_rating_announcement.jsp">
<label><input name="player_target" type="radio" value="all" >
To All Players </label>
 / 
 <label>
 <input name="player_target" type="radio" value="some" checked>
 To Some Players (select who on next screen)</label>
 <p>
   <input type="hidden" name="sending_page" value="make_rating_announcement" />
   
   <input type="submit" name="add_news" value="Submit">
</p>
 <p>Number of 
   <label>
   <select name="image_icon">
     <option value="dove">Doves</option>
   </select>
   </label>
   <label>
   <select name="points_awarded">
     <option value="0">0</option>
     <option value="1">1</option>
     <option value="2">2</option>
     <option value="3">3</option>
     <option value="4">4</option>
     <option value="5">5</option>
   </select>
   </label>
 </p>
 <h2>Place any comments you have below: </h2>
 <p>
		  <textarea id="announcement_text" name="announcement_text" style="height: 120px; width: 480px;"></textarea>
		<script language="javascript1.2">
			wysiwygWidth = 480;
			wysiwygHeight = 120;
  			generate_wysiwyg('announcement_text');
		</script>
  </p>
  
  
  <p>
    <label></label>
  </p>
</form>
<h2 align="center">
  Previous Rating Announcements Displayed Here</h2>
<table width="80%" border="1" align="center">
<tr><td width="25%" valign="top">To</td>
  <td width="19%" valign="top">Date</td>
  <td width="56%" valign="top">Message</td>
<tr>
<%
	for (ListIterator li = pso.getAllAnnouncements().listIterator(); li.hasNext();) {
			Alert al = (Alert) li.next();
			
			if (al.getType() == Alert.TYPE_RATING_ANNOUNCEMENT) {
%>
  <tr>
  	<td valign="top"><% if ((!al.isSpecific_targets())) { %>All<% } else { %>
  	  <%= USIP_OSP_Util.stringListToNames(pso.schema, pso.sim_id, pso.getRunningSimId(), request, al.getThe_specific_targets(), ", ")%>
    <% } %>	</td>
    <td valign="top"><%= al.getTimeOfAlert() %></td>
    <td valign="top"><%= al.getAlertMessage() %></td>
  </tr>
<%
		}
	}
%>
</table>

<p>&nbsp; </p>
<p>&nbsp;</p>
</body>
</html>