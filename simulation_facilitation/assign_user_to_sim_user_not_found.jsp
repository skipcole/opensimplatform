<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<% 
	SessionObjectBase sob = USIP_OSP_Util.getSessionObjectBaseIfFound(request);
	
	if (!(sob.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	UserAssignment ua_temp = sob.handleAssignUserEmail(request);
	
	if (sob.forward_on){
		response.sendRedirect(sob.backPage);
		return;
	}
	
	////////////////////////////////////////////////////
	Simulation simulation = new Simulation();	
	
	if (sob.sim_id != null){
		simulation = afso.giveMeSim();
	}
	/////////////////////////////////////////////////////
	RunningSimulation running_simulation = new RunningSimulation();
	if (sob.getRunningSimId() != null){
		running_simulation = sob.giveMeRunningSim();
	}
	//////////////////////////////////////////////////////
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>


<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.4.1.js"></script>
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
              <p align="left">The username you have entered, <span class="style1"><%= ua_temp.getUsername() %></span>, was not found. Please verify that <%= ua_temp.getUsername() %> is the correct email address for your student. </p>
              <p align="left">You may then: </p>
			  <form id="form1" name="form1" method="post" action="assign_user_to_sim_user_not_found.jsp">
			  <input type="hidden" name="sending_page" value="assign_just_email" />
              <ul>
                <li>
                  
                    <label>
                      <input type="submit" name="command" value="Cancel" />
                      </label>
                  This will return you to the previous page. 
                  
                  <a href="assign_user_to_simulation.jsp"></a></li>
                <li><a href="create_user.jsp">
                  <label>
                  <input type="submit" name="command" value="Create" />
                  </label>
                </a>This will take you to the create user page. </li>
                <li><a href="assign_useremail_to_role.jsp">
                  <label>
                  <input type="submit" name="command" value="Add" />
                  </label>
                </a>You will be presented with options on how to inform the player that they have been assigned a role. </li>
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
<script type="text/javascript">
function findValue(li) {
	if( li == null ) return alert("No match!");

	// if coming from an AJAX call, let's use the CityId as the value
	if( !!li.extra ) var sValue = li.extra[0];

	// otherwise, display the value in the text box
	else var sValue = li.selectValue;

}

function selectItem(li) {
	findValue(li);
}

function formatItem(row) {
	return row[1] + ", " + row[0];
}

<%
	for (ListIterator li = simulation.getActors(afso.schema).listIterator(); li.hasNext();) {
		Actor act = (Actor) li.next();
		
		/*
function lookupAjax(){
	var oSuggest = $("#userNameAjax< % = act.getId() % > ")[0].autocompleter;
	oSuggest.findValue();
	return false;
}

*/
%>



$("#userNameAjax<%= act.getId() %>").autocomplete(
	"autocomplete.jsp",
	{
delay:3,
minChars:3,
matchSubset:3,
matchContains:3,
cacheLength:10,
onItemSelect:selectItem,
onFindValue:findValue,
formatItem:formatItem,
autoFill:true
	}
);

<% } // End of loop over actor ids %>
</script>
</body>
</html>