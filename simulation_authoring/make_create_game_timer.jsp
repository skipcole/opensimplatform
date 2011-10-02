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
	
	String sending_page = (String) request.getParameter("sending_page");
	
	if ((USIP_OSP_Util.stringFieldHasValue(sending_page)) && 
		(sending_page.equalsIgnoreCase("game_timer"))) {
			
		String game_timer = (String) request.getParameter("game_timer");
		
		if ((game_timer != null) && (game_timer.equalsIgnoreCase("yes"))){
			sim.setUsesGameClock(true);
			sim.saveMe(afso.schema);
		} else {
			sim.setUsesGameClock(false);
			sim.saveMe(afso.schema);
		}
	}
	
	boolean usesTimer = sim.isUsesGameClock();
	
	String checkedYes = "";
	String checkedNo = " checked=\"checked\" ";
	
	if (usesTimer) {
		checkedYes = " checked=\"checked\" ";
		checkedNo = "";
	}
	
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
              <h1>Game Timer</h1>
              <br />
    <p>A game timer allows you to display the time in a simulation to the players. 
    <form action="make_create_game_timer.jsp" method="post" name="form2" id="form2">
      
      <input type="hidden" name="sending_page" value="game_timer" />
      
      <h2>Simulation Uses Game Timer</h2>
      <table width="100%" border="0">
        <tr>
          <td><input type="radio" name="game_timer" id="game_timer_yes" value="yes" <%= checkedYes %> />
            <label for="game_timer_yes">Yes</label></td>
          <td><input name="game_timer" type="radio" id="game_timer_no" value="no" <%= checkedNo %> />
            <label for="game_timer_no">No</label></td>
          <td><input type="submit" name="button" id="button" value="Submit" /></td>
        </tr>
      </table>
      <p>&nbsp;</p>
    </form>
<p>Below are listed all of the phases for this simulation. Each phase may have its own unique behaviour, or follow in          </p>
<table width="100%" border="1" cellspacing="2" cellpadding="2">
  <tr> 
              <td width="20%" valign="top"><h2>Phase Name</h2></td>
              <td width="70" valign="top"><h2>Game Time Controls</h2></td>
              <td width="10%" valign="top"><h2>Edit Controls</h2></td>
              </tr>
       <%
	   
	   List phaseList = SimulationPhase.getAllForSim(afso.schema, afso.sim_id);
	   
		for (ListIterator li = phaseList.listIterator(); li.hasNext();) {
			SimulationPhase sp = (SimulationPhase) li.next();
			
			GameClockPhaseInstructions gcpi = GameClockPhaseInstructions.getByPhaseAndSimId(afso, sp.getId(),  afso.sim_id);
			
			System.out.println("getting gcpi: " + afso.sim_id + ", " + sp.getId());
			
			String controlNotes = "";
			
			if (gcpi == null) {
				controlNotes = "No controller.";
			} else {
				controlNotes = gcpi.getTextSynopsis();
			}
			
			
		%>
        <form id="form2" name="form2" method="post" action="make_create_game_timer_phase_instructions.jsp">
        <input type="hidden" name="phase_id" value="<%= sp.getId() %>" />
            <tr>
              <td valign="top"><%= sp.getPhaseName() %> </td>
              <td valign="top"><%= controlNotes %></td>
              <td><input type="submit" name="command" value="Edit Controls"  /></td>
            </tr>
            </form>
<%
	}  // End of loop over phases.
%>
        </table>
<p>&nbsp;</p>
      <p><a href="<%= afso.backPage %>"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a></p>			</td>
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