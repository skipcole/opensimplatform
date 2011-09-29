<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	afso.backBackPageCode = 1;
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}
	
	UserAssignment ua_temp = afso.handleAssignUserEmail(request);
	
	if (afso.forward_on){
		afso.forward_on = false;
		response.sendRedirect(afso.backPage);
		return;
	}
	
	////////////////////////////////////////////////////
	Simulation simulation = new Simulation();	
	
	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}
	/////////////////////////////////////////////////////
	RunningSimulation running_simulation = new RunningSimulation();
	if (afso.getRunningSimId() != null){
		running_simulation = afso.giveMeRunningSim();
	}
	//////////////////////////////////////////////////////
	
	String ua_id = request.getParameter("ua_id");
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>


<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<script type="text/javascript" src="../third_party_libraries/jquery/jquery.autocomplete.js"></script>
<link rel="stylesheet" href="../third_party_libraries/jquery/jquery.autocomplete.css" type="text/css" />
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
              <h1>Assign User to Actor <a href="helptext/assign_players_help.jsp" target="helpinright"></a></h1>
              <p align="left">The username you have entered, <span class="style1"><%= ua_temp.getUsername() %></span>, was not found. </p>
              <p align="left"><strong>Please verify</strong> that <%= ua_temp.getUsername() %> is the correct email address for your student. </p>
              <p align="left">You may then: </p>
			  <form id="form1" name="form1" method="post" action="assign_user_to_sim_user_not_found.jsp">
			  <input type="hidden" name="sending_page" value="assign_just_email" />
			  <input type="hidden" name="a_id" value="<%= ua_temp.getActor_id() %>" />
			  <input type="hidden" name="s_id" value="<%= ua_temp.getSim_id() %>" />
			  <input type="hidden" name="rs_id" value="<%= ua_temp.getRunning_sim_id() %>" />
			  <input type="hidden" name="ua_id" value="<%= ua_id %>" />
			  <input type="hidden" name="uname" value="<%= ua_temp.getUsername() %>" />
              <ul>
                <li>
                    <label>
                      <input type="submit" name="command" value="Cancel" />
                      This will return you to the previous page.
                    </label>   
                </li>
                <li>
                  <label>
                  <input type="submit" name="command" value="Create" /> 
                  This will take you to the create user page.
                  </label>
                 </li>
                <li>
                  <label>
                  <input type="submit" name="command" value="Add" /> 
                  This will take you to the previous page with the <%= ua_temp.getUsername() %> associated with the role selected. </label>
                </li>
                </ul>    
				</form>          
              <p align="left">&nbsp;    </p></td>
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