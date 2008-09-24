<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,
		org.usip.oscw.networking.*,
		org.usip.oscw.unittests.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);

	String sending_page = (String) request.getParameter("sending_page");
	String runsuite = (String) request.getParameter("runsuite");
	
	String rundemo = (String) request.getParameter("rundemo");
	
	///////////////////////////////////
	
	String debug_string = "not set";
	
	String test_suite_results = "okay";
	boolean tests_ran = false;
	
	if ( (sending_page != null) && (runsuite != null) && (sending_page.equalsIgnoreCase("run_test_suite"))){

            tests_ran = true;
			
			test_suite_results = MasterTester.runTests();
	}
	
	///
	if ( (sending_page != null) && (rundemo != null) && (sending_page.equalsIgnoreCase("run_demo"))){

            tests_ran = true;
			
			test_suite_results = DemoTester.runTests();
	}



%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Online Simulation Platform Control Page</title>
<!-- InstanceEndEditable --><!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="../usip_oscw.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">

<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="80%" valign="top"><!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Run Test Suite</h1>
    <!-- InstanceEndEditable --></td>
    <td width="20%" align="right" valign="top"> 
		<% 
		String canEdit = (String) session.getAttribute("author");
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
		<a href="../simulation_planning/index.jsp" target="_top">Think</a><br>
	    <a href="creationwebui.jsp" target="_top">Create</a><br>
		<a href="../simulation_facilitation/facilitateweb.jsp" target="_top">Play</a><br>
        <a href="../simulation_sharing/index.jsp" target="_top">Share</a>
		<% } %>
		</td>
  </tr>
</table>
<BR />
<table width="720" bgcolor="#DDDDFF" align="center" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
</tr>
<tr> 
    <td colspan="3"><!-- InstanceBeginEditable name="pageBody" -->


        <table width="80%" border="0" cellspacing="2" cellpadding="2">
          <tr> 
            <td valign="top">Warning. Hitting submit will purge the database.</td>
            <td>Std. Tests</td>
            <td>
			<form action="run_test_suite.jsp" method="post" name="form1" id="form1">
			<input type="hidden" name="sending_page" value="run_test_suite" />
			<input type="submit" name="runsuite" value="Submit" />
			</form>
			</td>
          </tr>
          <tr> 
            <td>Warning. Hitting submit will purge the database.</td>
            <td>Demo Run</td>
            <td>
			<form action="run_test_suite.jsp" method="post" name="form1" id="form1">
			<input type="hidden" name="sending_page" value="run_demo" />
			<input type="submit" name="rundemo" value="Submit" />
			</form>
			</td>
          </tr>
        </table>
  <p>&nbsp;</p>

<% if (tests_ran) { %>
<%= test_suite_results %>
<% } %>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<!-- InstanceEndEditable --></td>
  </tr>
<tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24"  >&nbsp;</td>
  </tr>
</table>

<p>&nbsp;</p>
<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr> 
    <td align="left" valign="bottom"> 
	<% 
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
	<a href="intro.jsp" target="_top">Home 
      </a>
	  <% } else { %>
	  <a href="../simulation_facilitation/index.jsp" target="_top">Home 
      </a>
	  <% } %>
	  </td>
    <td align="right" valign="bottom"><a href="../simulation_user_admin/my_profile.jsp">My 
      Profile</a> </td>
  </tr>
  <tr>
    <td align="left" valign="bottom"><a href="logout.jsp" target="_top">Logout</a></td>
    <td align="right" valign="bottom">&nbsp;</td>
  </tr>
</table>
<p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/usiponlinesimulationcreationwizard/">USIP Open Source Software Project</a>. </p>
</body>
<!-- InstanceEnd --></html>
