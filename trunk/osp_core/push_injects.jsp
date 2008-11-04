<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.communications.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String sending_page = (String) request.getParameter("sending_page");
		
	if ( (sending_page != null) && (sending_page.equalsIgnoreCase("push_injects"))){
	
		String announcement_text = (String) request.getParameter("announcement_text");
		
		String player_target = (String) request.getParameter("player_target");
		
		if ((player_target != null) && (player_target.equalsIgnoreCase("some"))){
			pso.alertInQueueText = announcement_text;
			pso.alertInQueueType = Alert.TYPE_EVENT;
			pso.backPage = "push_injects.jsp";
			response.sendRedirect("select_actors.jsp");
			return;
		} else {
			pso.makeGeneralAnnouncement(announcement_text, request);
		}
		
	}
	
%>
<html>
<head>
<title>News Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>
<h2>Push Injects </h2>
<table width="100%" border="0" cellspacing="0" cellpadding="4">
		  <%
			for (ListIterator li = InjectGroup.getAllForSim(pso.schema, pso.sim_id).listIterator(); li.hasNext();) {
			InjectGroup ig = (InjectGroup) li.next();
		%>
  <tr>
    <td colspan="5" valign="top"><%= ig.getName() %></td>
  </tr>
  <tr>
    <td width="15%" valign="top">&nbsp;</td>
    <td colspan="4" valign="top"><%= ig.getDescription() %></td>
  </tr>
    <% 
	List injectList = Inject.getAllForSimAndGroup(pso.schema, pso.sim_id, ig.getId());

		int iii = 0;
		
		  for (ListIterator lii = injectList.listIterator(); lii.hasNext();) {
			Inject da_inject = (Inject) lii.next();
			iii += 1;
			
%>
<form name="formforinject<%= iii %>" action="push_injects.jsp" method="post">
<input type="hidden" name="sending_page" value="push_injects">
  <tr>
    <td valign="top">&nbsp;</td>
    <td colspan="3" valign="top"><%= da_inject.getInject_name() %></td>
    <td width="20%" rowspan="3" valign="top"><label>
      <input name="player_target" type="radio" value="all" checked>
      To All Players </label>
  <br>
  <label>
  <input name="player_target" type="radio" value="some">
    To Some Players</label><br>
  <label>
  <input type="submit" name="button" id="button" value="Push Inject">
  </label></td>
  </tr>
  <tr>
    <td valign="top">&nbsp;</td>
    <td width="4%" valign="top">&nbsp;</td>
    <td colspan="2" valign="top">
        <label>
          <textarea name="announcement_text" id="textarea" cols="45" rows="5"><%= da_inject.getInject_text() %></textarea>
        </label>
    </td>
  </tr>
  <tr>
    <td valign="top">&nbsp;</td>
    <td valign="top">&nbsp;</td>
    <td colspan="2" valign="top"><%= da_inject.getInject_Notes() %></td>
  </tr>
  <tr>
    <td valign="top">&nbsp;</td>
    <td colspan="4" valign="top"><hr></td>
  </tr>
  </form>
  <% } // End of loop over injects %>
 
 <% } // end of loop over inject groups %>
</table>
<p>&nbsp;</p>
</body>
</html>
<%
	
%>
