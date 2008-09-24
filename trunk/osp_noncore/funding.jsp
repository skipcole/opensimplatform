<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
		
	String debug_string = "";
	// Get funding level for current round
	
	String sending_page = (String) request.getParameter("sending_page");
	String change_value = (String) request.getParameter("change_value");
	String value_given = (String) request.getParameter("value_given");
	
	String value = "";
	String lastValue = "0";
	
	
	String getValue = "SELECT value FROM `framework_game_values` WHERE running_sim_id = " + pso.running_sim.id + 
		" AND `value_label` = 'historical_values' AND game_round = " + pso.simulation_round;
	
	debug_string = getValue;
	
	boolean line_exists = false;

	try {
		Connection connection = MysqlDatabase.getConnection();		
		Statement stmt = connection.createStatement();
		ResultSet rst = stmt.executeQuery(getValue);
	
		if (rst.next()){
			line_exists = true;
			value = rst.getString(1);
		} else {		// Get the most recent value, and insert based on it.
			line_exists = false;
		} // End of if record not found
		
		connection.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		if ( (sending_page != null) && (change_value != null) && (sending_page.equalsIgnoreCase("funding.jsp"))){
	
			value = value_given;
		
			try {
				Connection connection = MysqlDatabase.getConnection();		
				Statement stmt = connection.createStatement();
			
				String sqlStatement = "";
			
			if (line_exists) {
				sqlStatement = "UPDATE `framework_game_values` SET `value` = '" + value + 
				"' WHERE running_sim_id = " + pso.running_sim.id + " AND game_round = " + pso.simulation_round + " AND value_label = 'historical_values'";
			} else {
				sqlStatement = "INSERT INTO `framework_game_values` ( `game_value_id` , `running_sim_id` , `value_label` , `game_round` , `value` ) " 
					+ "VALUES (NULL , '" + pso.running_sim.id + "', 'historical_values', '" + pso.simulation_round + "', '" + value + "' )";
			}
			
				debug_string = sqlStatement;
				stmt.execute(sqlStatement);
			
				connection.close();
		
			} catch (Exception e) {
				debug_string = e.getMessage();
				e.printStackTrace();
			}
	}

	String selected_0 = "";
	String selected_1000 = "";
	String selected_5000 = "";
	String selected_10000 = "";
	
	if (value.equalsIgnoreCase("0")){
		selected_0 = "checked";
	} else if (value.equalsIgnoreCase("1000")){
		selected_1000 = "checked";
	} else if (value.equalsIgnoreCase("5000")){
		selected_5000 = "checked";
	} else if (value.equalsIgnoreCase("10000")){
		selected_10000 = "checked";
	}

%>
<html>
<head>
<title>SRSG Actions</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
</head>

<body>
<p>&nbsp;</p>
<p>Set Monthly funding</p>
<form name="form1" method="post" action="funding.jsp">
<input type="hidden" name="sending_page" value="funding.jsp">
  <blockquote>
    <p> 
      <input name="value_given" type="radio" value="0" <%= selected_0 %> >
      $0.00<br>
      <input type="radio" name="value_given" value="1000" <%= selected_1000 %> >
      $1000.00 <br>
      <input type="radio" name="value_given" value="5000" <%= selected_5000 %> >
      $5,000.00<br>
	  <input type="radio" name="value_given" value="10000" <%= selected_10000 %> >
      $10,000.00</p>
  </blockquote>
  <p>
    <input type="submit" name="change_value" value="Submit">
  </p>
</form>
<p><!-- %= debug_string % --></p>
<p>&nbsp;</p>

<img src="../ver1/oscw_noncore/<%= pso.base_servlet_location %>GraphServer?chart_name=framework1&game_round=<%= pso.simulation_round %>&running_sim_id=<%= pso.running_sim.id %>"  border=2 />

<p>Any type of chart from <a href="http://www.jfree.org/jfreechart/samples.html" target="_blank">JFreeChart</a></p>
</body>
</html>
