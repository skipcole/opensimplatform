<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,
		org.usip.osp.networking.*,
		org.usip.osp.unittests.*" 
	errorPage="" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));

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
<title>Open Simulation Platform Control Page</title>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
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
			<!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Run Test Suite</h1>
    <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" -->


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
<!-- InstanceEndEditable -->
			</td>
		</tr>
		</table>
	</td>
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
<!-- InstanceEnd --></html>
