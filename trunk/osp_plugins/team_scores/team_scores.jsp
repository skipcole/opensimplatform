<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	com.seachangesimulations.osp.teamscores.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}

	String cs_id = (String) request.getParameter("cs_id");
	CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
	

	TeamScoresTimePeriod.checkForPeriodCreation(request, pso.schema, pso.sim_id, cs.getId(), pso.getRunningSimId());
	
%>
<html>
<head>
<title>Team Scores</title>
<script language="JavaScript" type="text/javascript" src="../../wysiwyg_files/wysiwyg.js">
</script>
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
<body>

<p>Team Scores</p>
<table width="100%" border="1" cellspacing="0" cellpadding="2">
  <tr>
    <td>Date</td>
      <% 
	  
	  Enumeration simSets = RunningSimSet.getAllRunningSimsInSameSet(pso.schema, pso.getRunningSimId());
	  
	  int ii = 0;
		for (; simSets.hasMoreElements();) {
			Long key = (Long) simSets.nextElement();
			
			RunningSimulation rsTemp = RunningSimulation.getById(pso.schema, key);
			ii += 1;
			%>
    <td><%= rsTemp.getName() %></td>
	<% } // end of loop over running sims in set. %>
  </tr>
<%	// Loop over Scoring Periods

List tstps = TeamScoresTimePeriod.getAllForSectionAndRunningSim(pso.schema, cs.getId(), pso.getRunningSimId());

		for (ListIterator li = tstps.listIterator(); li.hasNext();) {
			TeamScoresTimePeriod tstp = (TeamScoresTimePeriod) li.next();

%>
  <tr>
    <td><%= tstp.getPeriodDescription() %></td>
    <% 
	simSets = RunningSimSet.getAllRunningSimsInSameSet(pso.schema, pso.getRunningSimId());
	for (; simSets.hasMoreElements();) {
			Long rsId = (Long) simSets.nextElement();
	
	%>
    <td>
    <% TeamScores thisScore = 
		TeamScores.getBySimSectionRunningSimTimePeriod(pso.schema, pso.sim_id, cs.getId(), rsId, tstp.getId()); %>
     
     <% for (int iiD = 0; iiD < thisScore.getScoreValue() ; ++iiD) { %>
     <img src="../../simulation/images/dove_30by30.png" width="30" height="30" alt="dove">
     <% } //end of loop over doves. %>
     
     <% if (pso.isControlCharacter()) { %>
     <br />
     <a href="team_scores_enter.jsp?ts_id=<%= thisScore.getId() %>&cs_id=<%= cs_id %>&rs_id=<%= rsId %>&tstp_id=<%= tstp.getId() %>">Enter Score</a>
     <% } %>
    </td>
    <% } %>
  </tr>
<% // End of loop over Scoring Periods
} 
%>
</table>
<p>&nbsp;</p>
<p>

 <% if (pso.isControlCharacter()) { %> 		
<form name="form1" method="post" action="team_scores.jsp">
<input type="hidden" name="cs_id" value="<%= cs_id %>">
<input type="hidden" name="sending_page" value="create_tstp" />
<table width="100%" border="1" cellspacing="0" cellpadding="2">
  <tr>
    <td>Create Scoring Period: </td>
    <td><input type="text" name="tstp_name" id="textfield"></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><input type="submit" name="button" id="button" value="Submit"></td>
  </tr>
</table>
</form>
<% } // end of if actor is control %>
<p>&nbsp;</p>

<p><br />
  
  
  
</p>
<p>&nbsp;</p>
</body>
</html>