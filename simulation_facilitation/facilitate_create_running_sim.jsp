<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.coursemanagementinterface.*,	
	org.hibernate.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	Simulation simulation = new Simulation();	
	
	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}
	
	afso.handleAddRunningSimulation(request, simulation);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>USIP Open Simulation Platform</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.style1 {color: #FF0000}
-->
</style>
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
			  <h1>Create Running Simulation<a href="helptext/create_running_sim_help.jsp" target="helpinright">(?)</a></h1>
			  <br />
            <blockquote> 
			  <span class="style1"><%= afso.errorMsg %></span>
			<% afso.errorMsg = ""; %>
            <% if (afso.sim_id != null) {  %>
			
              <p><% if (afso.getRunningSimId() != null) { %>
              You are currently working on running simulation <%= afso.run_sim_name %>.<% } %>
              </p>
              <p>Below are listed the running simulations of the simulation <b>
			  <%= simulation.getDisplayName() %> </b> where you are a designated instructor. </p>
			  	<%
			  		List rsilList = RunningSimulationInformationLine.getRunningSimLines(afso, afso.sim_id);
		
					if ((rsilList != null) && (rsilList.size() > 0)) {		
				
				%>
              <table width="80%" border = "1">
                <tr> 
                  <td><h2>Running Simulation*</h2></td>
                  <td><h2>TZ(?)</h2></td>
                  <td><h2>Enabled</h2></td>
                  <td><h2>Phase</h2></td>
            </tr>
                <%
			
				for (ListIterator li = rsilList.listIterator(); li.hasNext();) {
					RunningSimulationInformationLine rsil = (RunningSimulationInformationLine) li.next();
				
		%>
                <tr> 
                  <td><a href="facilitate_change_running_sim_name.jsp?rs_id=<%= rsil.getRsId() %>"><%= rsil.getRsName() %></a></td>
                  <td><%= rsil.getTimeZone() %></td>
                  <td>
				  <%= rsil.isEnabled() %>				  </td>
                  <td><%= rsil.getPhaseName() %></td>
            </tr>
                <%
			}
		%>
                </table>
				* You can edit the name of a Running Simulation by clicking on it above.
				    <% } else { %>
				    <ul><li>None</li></ul>
				    <% } %>
			<p>&nbsp;</p>
	          
            <form action="facilitate_create_running_sim.jsp" method="post" name="form1" id="form1">
              <input type="hidden" name="sending_page" value="create_running_sim" />
              <table width="80%" border="1" cellspacing="2" cellpadding="2">
                <tr> 
                  <td valign="top">Enter new Running Simulation Name (for example 'Summer 2007 - 
                    1')</td>
              <td valign="top"><input type="text" name="running_sim_name" /></td>
            </tr>
                <tr>
                  <td>Time Zone of Simulation </td>
                  <td><select name="timezone" id="timezone">
      <option value="-12.0">(GMT -12:00) Eniwetok, Kwajalein</option>
      <option value="-11.0">(GMT -11:00) Midway Island, Samoa</option>
      <option value="-10.0">(GMT -10:00) Hawaii</option>
      <option value="-9.0">(GMT -9:00) Alaska</option>
      <option value="PST">Pacific Time (US &amp; Canada)</option>
      <option value="-7.0">(GMT -7:00) Mountain Time (US &amp; Canada)</option>
      <option value="-6.0">(GMT -6:00) Central Time (US &amp; Canada), Mexico City</option>
      <option value="EST">Eastern Time (US &amp; Canada), Bogota, Lima</option>
      <option value="-4.0">(GMT -4:00) Atlantic Time (Canada), Caracas, La Paz</option>
      <option value="-3.5">(GMT -3:30) Newfoundland</option>
      <option value="-3.0">(GMT -3:00) Brazil, Buenos Aires, Georgetown</option>
      <option value="-2.0">(GMT -2:00) Mid-Atlantic</option>
      <option value="-1.0">(GMT -1:00 hour) Azores, Cape Verde Islands</option>
      <option value="0.0">(GMT) Western Europe Time, London, Lisbon, Casablanca</option>
      <option value="1.0">(GMT +1:00 hour) Brussels, Copenhagen, Madrid, Paris</option>
      <option value="2.0">(GMT +2:00) Kaliningrad, South Africa</option>
      <option value="3.0">(GMT +3:00) Baghdad, Riyadh, Moscow, St. Petersburg</option>
      <option value="3.5">(GMT +3:30) Tehran</option>
      <option value="4.0">(GMT +4:00) Abu Dhabi, Muscat, Baku, Tbilisi</option>
      <option value="4.5">(GMT +4:30) Kabul</option>
      <option value="5.0">(GMT +5:00) Ekaterinburg, Islamabad, Karachi, Tashkent</option>
      <option value="5.5">(GMT +5:30) Bombay, Calcutta, Madras, New Delhi</option>
      <option value="5.75">(GMT +5:45) Kathmandu</option>
      <option value="6.0">(GMT +6:00) Almaty, Dhaka, Colombo</option>
      <option value="7.0">(GMT +7:00) Bangkok, Hanoi, Jakarta</option>
      <option value="8.0">(GMT +8:00) Beijing, Perth, Singapore, Hong Kong</option>
      <option value="9.0">(GMT +9:00) Tokyo, Seoul, Osaka, Sapporo, Yakutsk</option>
      <option value="9.5">(GMT +9:30) Adelaide, Darwin</option>
      <option value="10.0">(GMT +10:00) Eastern Australia, Guam, Vladivostok</option>
      <option value="11.0">(GMT +11:00) Magadan, Solomon Islands, New Caledonia</option>
      <option value="12.0">(GMT +12:00) Auckland, Wellington, Fiji, Kamchatka</option>
</select></td>
                </tr>
                <tr> 
                  <td>&nbsp;</td>
              <td><input type="submit" name="addRunningSimulation" value="Submit" /></td>
            </tr>
                </table>
            </form>
			</blockquote>
            <p align="center"><a href="facilitate_create_schedule_page.jsp">Next step: Create Schedule Page</a></p>
            <p align="left"><a href="facilitate_panel.jsp">&lt;-- Back</a></p>
            <% } else { // End of if have set simulation id. %>
            <blockquote> 
              <p>You must first select the simulation you want to work on from the library.                 </p>
                  </blockquote>
            <p>
              <% } // End of if have not set simulation for edits. %>
                  </p>
            <p>&nbsp;</p>
</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>.</p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>
<%
%>
