<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.specialfeatures.*" 
	errorPage="" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
		
	String sf_id = (String) request.getParameter("sf_id");
	String var_sim_id = (String) request.getParameter("var_sim_id");
	
	String game_values_table = pso.simulation.db_tablename_var_int_v;
	
	PlayerControl pc = new PlayerControl();
	
	pc.set_sf_id(sf_id);
	
	pc.load();
	
	String sending_page = (String) request.getParameter("sending_page");
	String change_value = (String) request.getParameter("change_value");
	
	///////////////////////////////////
	
	String debug_string = "";
	

	
	if ( (sending_page != null) && (change_value != null) && (sending_page.equalsIgnoreCase("player_control"))){
		debug_string = "do it";
		
		AllowableResponse ar = (AllowableResponse) pc.allowableResponses.get(0);
		
		String new_value = (String) request.getParameter("new_value");
		
		ar.execute(pso.simulation.db_tablename_var_int_v, var_sim_id, pso.simulation_round, new_value);
		
	}
	
	
		String previous_value = pc.intVar.getCurrentValue(pso.simulation.db_tablename_var_int_v, var_sim_id, pso.simulation_round);
	
%>
<html>
<head>
<title>Chart of Variable</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
</head>

<body>
<p>&nbsp;</p>
<p><%= pc.description %></p>
<p>&nbsp;</p>

<div align="center">
  <% 
        for (Enumeration e = pc.allowableResponses.elements(); e.hasMoreElements();) {
            AllowableResponse ar = (AllowableResponse) e.nextElement();
			
			debug_string += ar.name;
			
%>
</div>
<form name="form1" method="post" action="show_playercontrol.jsp">
  <div align="center">
    <input type="hidden" name="sending_page" value="player_control">
    <input type="hidden" name="change_value" value="change_value">
    <input type="hidden" name="sf_id" value="<%= sf_id %>">
    <input type="hidden" name="var_sim_id" value="<%= var_sim_id %>">
    <table "75%" border="0" cellspacing="2" cellpadding="1">
      <tr> 
        <td><%= ar.controlText %></td>
        <td><input type="text" name="new_value" value="<%= previous_value %>"></td>
      </tr>
      <tr> 
        <td>&nbsp;</td>
        <td><input type="submit" name="Submit" value="Submit"></td>
      </tr>
    </table>
  </div>
</form>
<div align="center">
  <% } %>
</div>
<p>&nbsp;</p>
<p>&nbsp;</p>

<p><%= debug_string %></p>
<p>&nbsp;</p>

<p>&nbsp;</p>

</body>
</html>
<%
	
%>