<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	com.seachangesimulations.osp.gametime.*"
	errorPage="/error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
		
	Simulation sim = new Simulation();	
	if (afso.sim_id != null){
		sim = afso.giveMeSim();
	}
	
	SimulationPhase sp = new SimulationPhase();
	
	String phase_id = (String) request.getParameter("phase_id");
	if (USIP_OSP_Util.stringFieldHasValue(phase_id)) {
		sp = SimulationPhase.getById(afso.schema, new Long(phase_id));
	}
	
	GameClockPhaseInstructions gcpi = GameClockPhaseInstructions.handleEdit(request, afso);
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Game Timer Phase Instructions</h1>
              <h2>Phase: <%= sp.getPhaseName() %></h2>
              <h2>Current Synopsis: <%= gcpi.getTextSynopsis() %>  </h2>
              <p>Instructions</p>
              <p>There are 7 kinds of counters. Counters can do any of the following items below.</p>
              <ol>
                <li>Display a constant value adjustable by Control.</li>
                <li>Count Up in Game Time (12:59, 1:00, etc.)</li>
                <li>Count up in Running Time (5 minutes, 6, etc.)</li>
                <li>Count Up in Intervals (months, days, etc.)</li>
                <li>Count Down in Game Time (1:00, 12:59, etc.)</li>
                <li>Count Down in Running Time (5 minutes, 6, etc.)</li>
                <li>Count Down in Intervals (months, days, etc.)              </li>
                </ol>
              <p>Select the type of timer below, and enter in the required information for it.</p>
              <form action="make_create_game_timer_phase_instructions.jsp" method="post" name="form2" id="form2">
                <input type="hidden" name="sending_page" value="edit_gcpi" />
	<input type="hidden" name="phase_id" value="<%= phase_id %>" />
      <table width="100%" border="1" cellspacing="0">
        <tr>
          <td valign="top">1.
            <input type="radio" name="timer_type" value="<%= GameClockPhaseInstructions.GCPI_CONST %>" <%= USIP_OSP_Util.matchSelected(GameClockPhaseInstructions.GCPI_CONST, gcpi.getTimerType(), " checked=\"checked\" ") %> />
            <label for="radio2"></label></td>
          <td valign="top">Constant Value</td>
          <td valign="top"><label for="textfield"></label>
            Starting Value:
            <input type="text" name="textfield" id="textfield" /></td>
        </tr>
        <tr>
          <td valign="top">2.
            <input name="timer_type" type="radio" value="<%= GameClockPhaseInstructions.GCPI_UP_TIME %>"  <%= USIP_OSP_Util.matchSelected(GameClockPhaseInstructions.GCPI_UP_TIME, gcpi.getTimerType(), " checked=\"checked\" ") %>  /></td>
          <td valign="top">&nbsp;</td>
          <td valign="top">&nbsp;</td>
        </tr>
        <tr>
          <td valign="top">3. 
            
            <label for="radio">
              <input type="radio" name="timer_type" value="<%= GameClockPhaseInstructions.GCPI_UP_RUNNING_TIME %>"  <%= USIP_OSP_Util.matchSelected(GameClockPhaseInstructions.GCPI_UP_RUNNING_TIME, gcpi.getTimerType(), " checked=\"checked\" ") %>  />
            </label></td>
          <td valign="top">&nbsp;</td>
          <td valign="top">&nbsp;</td>
        </tr>
        <tr>
          <td valign="top">4.            
            <input type="radio" name="timer_type" value="<%= GameClockPhaseInstructions.GCPI_UP_INTERVAL %>"  <%= USIP_OSP_Util.matchSelected(GameClockPhaseInstructions.GCPI_UP_INTERVAL, gcpi.getTimerType(), " checked=\"checked\" ") %> /></td>
          <td valign="top">Intervals</td>
          <td valign="top"><p>Seconds per Interval 
            <label for="textfield2"></label>
            <input type="text" name="seconds_per_interval" />
          </p>
            <p>Name of Interval 
              <label for="textfield3"></label>
              <input type="text" name="interval_name" id="textfield3" />
            </p></td>
        </tr>
        <tr>
          <td valign="top">5.
            <input type="radio" name="timer_type" value="<%= GameClockPhaseInstructions.GCPI_DOWN_TIME %>"  <%= USIP_OSP_Util.matchSelected(GameClockPhaseInstructions.GCPI_DOWN_TIME, gcpi.getTimerType(), " checked=\"checked\" ") %>  /></td>
          <td valign="top">&nbsp;</td>
          <td valign="top">&nbsp;</td>
        </tr>
        <tr>
          <td valign="top">6.
            <input type="radio" name="timer_type" value="<%= GameClockPhaseInstructions.GCPI_DOWN_RUNNING_TIME %>"  <%= USIP_OSP_Util.matchSelected(GameClockPhaseInstructions.GCPI_DOWN_RUNNING_TIME, gcpi.getTimerType(), " checked=\"checked\" ") %>  /></td>
          <td valign="top">&nbsp;</td>
          <td valign="top">&nbsp;</td>
        </tr>
        <tr>
          <td valign="top">7.
            <input type="radio" name="timer_type" value="<%= GameClockPhaseInstructions.GCPI_DOWN_INTERVAL %>"  <%= USIP_OSP_Util.matchSelected(GameClockPhaseInstructions.GCPI_DOWN_INTERVAL, gcpi.getTimerType(), " checked=\"checked\" ") %>  /></td>
          <td valign="top">&nbsp;</td>
          <td valign="top">&nbsp;</td>
        </tr>
        <tr>
          <td valign="top">&nbsp;</td>
          <td valign="top">&nbsp;</td>
          <td valign="top"><input type="submit" name="button" id="button" value="Submit" /></td>
        </tr>
    </table>
      <p>&nbsp;</p>
</form>
<p><a href="make_create_game_timer.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a></p>			</td>
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