<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.baseobjects.*,org.usip.osp.persistence.*,org.usip.osp.specialfeatures.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
		
	String debug = "";
	
	String sf_id = (String) request.getParameter("sf_id");
	String sim_id = (String) request.getParameter("sim_id");
	
	PlayerControlToggleBoolean pctb = new PlayerControlToggleBoolean();
	
	pctb.set_sf_id(sf_id);
	
	pctb.load();
	
	BooleanVariable bv = new BooleanVariable();
	//bv.set_sf_id(pctb.booleanVarSFid);
	bv.sim_id = sim_id;
	bv.value = bv.getCurrentValue(pso.simulation.db_tablename_var_bool_v, sim_id, pso.simulation_round);
	
	String toggleSQL = "";
	
	String sending_page = (String) request.getParameter("sending_page");
	String change_value = (String) request.getParameter("change_value");
	
	if ( (sending_page != null) && (change_value != null) && (sending_page.equalsIgnoreCase("player_control_boolean_toggle"))){
		
		String var_value = (String) request.getParameter("var_value");
		
		if (!(var_value.equalsIgnoreCase(bv.value))){
			toggleSQL = "UPDATE " + pso.simulation.db_tablename_var_bool_v + "  SET value = '" + var_value + "' " +
				"WHERE sim_id = " + bv.sim_id + " AND sim_id = " + pso.simulation.id + 
				" AND running_sim_id = " + pso.running_sim.id + " AND game_round = " + pso.simulation_round;
						
			try { 
				Connection connection = MysqlDatabase.getConnection();
            	Statement stmt = connection.createStatement();
				stmt.execute(toggleSQL);
            	connection.close();

        	} catch (Exception e) {
            	e.printStackTrace();
        	}
			
			bv.value =  var_value;
		}
		
	}
	
	String selectedTrue = "";
	String selectedFalse = " checked ";
	
	if (bv.value.equalsIgnoreCase("true")){
		selectedTrue = " checked ";
		selectedFalse = "";
	}
%>
<html>
<head>
<title>Toggle Boolean Variable</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
</head>

<body>
<blockquote>
  <p><%= Debug.getDebug(debug) %></p>
  <p><%= toggleSQL %></p>
<p><%= pctb.description %></p>
<form name="form1" method="post" action="show_toggle_boolean.jsp"> 

  <input type="hidden" name="sf_id" value="<%= sf_id %>">
  <input type="hidden" name="sim_id" value="<%= bv.sim_id %>">
  
  <input type="hidden" name="sending_page" value="player_control_boolean_toggle">
  <input type="hidden" name="change_value" value="change_value">
  <table width="80%" border="0" cellpadding="1" cellspacing="2">
    <tr> 
      <td width="13%"><input type="radio" name="var_value" value="true" <%= selectedTrue %> >
        True</td>
      <td width="83%"><%= pctb.setToTrueLabelMessage %></td>
    </tr>
    <tr> 
      <td><input name="var_value" type="radio" value="false" <%= selectedFalse %> >
        False</td>
      <td><%= pctb.setToFalseLabelMessage %></td>
    </tr>
    <tr> 
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr> 
      <td colspan="2"><div align="center">
          <input type="submit" name="Submit" value="Submit">
        </div></td>
    </tr>
  </table>
<p>&nbsp;</p>
</form>
</blockquote>
</body>
</html>
<%
	
%>