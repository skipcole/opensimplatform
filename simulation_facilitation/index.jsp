<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" errorPage="../error.jsp" %>
<%
				
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getPSO(request.getSession(true), true);
	String prevErrorMsg = afso.errorMsg;
	
	String attempting_login = (String) request.getParameter("attempting_login");
	
	if ((attempting_login != null) && (attempting_login.equalsIgnoreCase("true"))){
		session.setAttribute("afso", null);
		afso = AuthorFacilitatorSessionObject.getPSO(request.getSession(true), true);
		response.sendRedirect(afso.validateLoginToOSP(request, afso.FACILITATOR_LOGIN));
		return;
	} // End of if login in.
	
	afso.errorMsg = prevErrorMsg;
	
%>
<html>
<head>
<title>USIP Open Simulation Platform Login</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../usip_osp.css" rel="stylesheet" type="text/css">
</head>

<body>
<p>&nbsp;</p>
<p>&nbsp;</p>
<table width="720" border="0" cellspacing="0" cellpadding="0" align="center" background="../Templates/images/page_bg.png">

  <tr> 
    <td colspan="3" background="../Templates/images/page_bg.png" ><P>&nbsp;</P>
    <table width="100%"> <tr><td>
      <h1 align="center">&nbsp;&nbsp;&nbsp;USIP Open Simulation Platform <br>
        &nbsp;&nbsp;&nbsp;(Release <%= USIP_OSP_Properties.getRawValue("release") %>)<br> 
        <br>
        &nbsp;&nbsp;&nbsp;Simulation Facilitator Login</h1>
      <p>&nbsp;</p>
      </td>
      <td align="right"><img src="instructors.png" alt="InstructorLogo" width="194" height="162"></td>
    </tr>
      </table>
      <form name="form1" method="post" action="index.jsp" target="_top">
         
        <input type="hidden" name="attempting_login" value="true">
        </font> 
        <table width="58%" border="0" cellspacing="0" cellpadding="0" align="center">
          <tr> 
            <td>user name</font></td>
            <td> <input type="text" name="username"></td>
          </tr>
          <tr> 
            <td>password</font></td>
            <td> <input type="password" name="password"> </td>
          </tr>
          
          <tr> 
            <td>&nbsp;</td>
            <td> <input type="submit" name="Submit" value="Submit"> </td>
          </tr>
          <tr> 
            <td colspan="2"><font color="#FF0000"><%= afso.errorMsg %></font></td>
          </tr>
        </table>
      </form>
	  <center>
        <table width="50%" border="0" cellspacing="2" cellpadding="1">
          <tr>
            <td valign="top">Upcoming Planned Outage: <%= USIP_OSP_Properties.getRawValue("next_planned_outage") %></td>
          </tr>
          <tr> 
          <tr> 
            <td valign="top"><a href="../acknowledgements/index.htm">Acknowledgements</a></td>
          </tr>
        </table>
	  </center>
      <p align="center">&nbsp;</p>
    </td>
  </tr>

</table>

<p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Creation Software Wizard is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p>
</body>
</html>
<%
	afso.errorMsg = "";
%>