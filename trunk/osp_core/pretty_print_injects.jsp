<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.communications.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	String sending_page = (String) request.getParameter("sending_page");
		
	if ( (sending_page != null) && (sending_page.equalsIgnoreCase("push_injects"))){
	
		String announcement_text = (String) request.getParameter("announcement_text");
		String inject_action = (String) request.getParameter("inject_action");
		
		if ((inject_action != null) && (inject_action.equalsIgnoreCase("2"))) {
			announcement_text = announcement_text + "<BR /><strong>Communicate with Control your actions</strong><BR />";
		}
		
		String player_target = (String) request.getParameter("player_target");
		
		if ((player_target != null) && (player_target.equalsIgnoreCase("some"))){
			pso.alertInQueueText = announcement_text;
			pso.alertInQueueType = AlertLevels.TYPE_EVENT;
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
<title>Pretty Print Injects Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<STYLE TYPE="text/css">
     P.breakhere {page-break-after: always}
</STYLE> 
</head>

<body>

		  <%
			for (ListIterator li = InjectGroup.getAllForSim(pso.schema, pso.sim_id).listIterator(); li.hasNext();) {
			InjectGroup ig = (InjectGroup) li.next();
		%>
    <% 
	List injectList = Inject.getAllForSimAndGroup(pso.schema, pso.sim_id, ig.getId());

		int iii = 0;
		
		  for (ListIterator lii = injectList.listIterator(); lii.hasNext();) {
			Inject da_inject = (Inject) lii.next();
			iii += 1;
			
%>
<table width="100%" border="0" cellspacing="0" cellpadding="4">

  <tr>
    <td valign="top"><%= ig.getName() %></td>
    <td colspan="3" valign="top"><%= da_inject.getInject_name() %></td>
    </tr>
  <tr>
    <td valign="top">&nbsp;</td>
    <td width="4%" valign="top">&nbsp;</td>
    <td colspan="2" valign="top" bordercolor="#000000">
                  <%= da_inject.getInject_text() %>
        
      <p>      </p></td>
  </tr>
  <tr>
    <td valign="top">&nbsp;</td>
    <td valign="top">&nbsp;</td>
    <td colspan="2" valign="top" bordercolor="#000000"><%= da_inject.getInject_Notes() %></td>
  </tr>
  <tr>
    <td valign="top">&nbsp;</td>
    <td colspan="3" valign="top"><hr></td>
  </tr>
  <tr>
    <td colspan="4" valign="top"></td>
    </tr>
    </table>
    <P CLASS="breakhere"></P>
  <% } // End of loop over injects %>
 
 <% } // end of loop over inject groups %>

</body>
</html>
<%
	
%>
