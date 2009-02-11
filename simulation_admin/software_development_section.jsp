<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.osp.networking.*,org.usip.osp.baseobjects.*" errorPage="" %>
<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
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
      <h1>Simulation Software Admininstration</h1>
      <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" -->
      <table width="100%" border="0" cellspacing="2" cellpadding="1">
        <!--tr valign="top"> 
    <td>4.</td>
    <td>GC <a href="create_game_phases.jsp">creates Game Phases</a></td>
    <td>For example, &quot;In Progress&quot; or &quot;Done&quot;</td>
  </tr -->
        <tr valign="top"> 
          <td>&nbsp;</td>
          <td><h2>Step</h2></td>
          <td><h2>&nbsp;</h2></td>
          <td><h2>Description</h2></td>
        </tr>
        <tr valign="top"> 
          <td width="2%">&nbsp;</td>
          <td width="24%"><a href="../public_html/oscw_javadocs/index.html" target="_top">JavaDocs</a></td>
          <td width="2%">&nbsp;</td>
          <td width="72%">Hopefully these are clear. Don't hesitate to send an 
            email to <a href="mailto:scole@usip.org">scole@usip.org</a> if they 
            are not.</td>
        </tr>
        <tr valign="top"> 
          <td>&nbsp;</td>
          <td><a href="../simulation_authoring/reset.jsp">Reset Simulation Caches</a></td>
          <td>&nbsp;</td>
          <td>This should only be necessary if values have changed since the sim 
            began.</td>
        </tr>
        <tr valign="top"> 
          <td>&nbsp;</td>
          <td><a href="../simulation_diagnostics/run_test_suite.jsp">Run Test Suite</a></td>
          <td>&nbsp;</td>
          <td>Warning! Running this suite of tests will destroy all data in the 
            database.</td>
        </tr>
        <tr valign="top">
          <td>&nbsp;</td>
          <td><a href="../simulation_authoring/toggle_debug.jsp">Toggle Debug</a></td>
          <td>&nbsp;</td>
          <td>Turn debugging messages <% if (Debug.debug_on) { %> off <% } else { %> on <% } %>.</td>
        </tr>
      </table>
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
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>.</p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
<!-- InstanceEnd --></html>
