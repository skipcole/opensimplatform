<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %>
<% 
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	pso.backPage = "add_underlying_model.jsp";
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<title>Open Simulation Platform Add Special Features Page</title>


<style type="text/css">
<!--
.style1 {color: #FF0000}
-->
</style>

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
              <h1>Create Model Definitions</h1>
              <br />
      <blockquote> 
        
        <p>On this page one can define a model and generated the required XML file to allow it to be transported to other OSP installations.</p>
        <form id="form1" name="form1" method="post" action="create_model_definitions.jsp">
          <table width="90%" border="1" cellspacing="0">
            <tr>
              <td width="33%">Model Name</td>
            <td width="2%">&nbsp;</td>
            <td width="65%">
              <label>
                <input type="text" name="textfield" id="textfield" />
                </label>          </td>
          </tr>
            <tr>
              <td>Model Version:</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
            <tr>
              <td>Run Location:</td>
            <td>&nbsp;</td>
            <td><label>
              <select name="select" id="select">
                <option value="local" selected="selected">local</option>
                <option value="remote">remote</option>
                </select>
              </label></td>
          </tr>
            <tr>
              <td>Controller Package Name</td>
            <td>&nbsp;</td>
            <td><label>
              <input type="text" name="textfield2" id="textfield2" />
              </label></td>
          </tr>
            <tr>
              <td>Controller Class Name</td>
            <td>&nbsp;</td>
            <td><label>
              <input type="text" name="textfield3" id="textfield3" />
              </label></td>
          </tr>
            <tr>
              <td>Variables</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
            <tr>
              <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td><label>
              <input type="submit" name="button" id="button" value="Add Variable" />
              </label></td>
          </tr>
            <tr>
              <td>Variable Dependency</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
            <tr>
              <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td><label>
              <input type="submit" name="button2" id="button2" value="Add Variable Dependency" />
              </label></td>
          </tr>
            </table>
        </form>
        <p>&nbsp;</p>
        <p>&nbsp;</p>
          <p>&nbsp;</p>
        </blockquote>      <a href="create_injects.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
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
<%
	
%>
