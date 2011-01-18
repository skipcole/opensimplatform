<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	
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
.style1 {
	color: #FF0000;
	font-weight: bold;
}
.style2 {color: #FF0000}
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
              <h1>Assign Useremail to a Running Simulation</h1>
              <p>
                The email address <span class="style1">x</span> has been assigned in the running simulation <span class="style2">y</span> to actor role <span class="style2">z</span>.</p>
              <p>You may now either</p>
              <ul>
                <li>Email the user at this email address an invition to join, or</li>
                <li>Return to the assign user's page. (You may then email mail several players at once.) </li>
              </ul>
              <p>&nbsp;</p>
      <p align="center"><a href="enable_simulation.jsp"></a></p>      
      <p align="left"><a href="create_user.jsp"></a></p>			</td>
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