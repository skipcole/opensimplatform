<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	pso.backPage = "add_underlying_model.jsp";
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Add Special Features Page</title>


<style type="text/css">
<!--
.style1 {color: #FF0000}
-->
</style>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Add  Underlying Model</h1>
              <br />
      <blockquote> 
        
        <p>The OSP allows people to plug-in different underlying models. These models need to minimal set of constraints. Essentially, they only need to be able to provide the simulation author a set of functions that can be used in their simulations.</p>
        <p>Models can be run remotely, or locally on the same machine that is running the simulation. <a href="add_underlying_model_programming_instructions.jsp">Click here</a> to see the programming instructions on how to create models to be run locally.</p>
        
        <table width="90%" border="1">
          <tr>
            <td width="13%" valign="top">Step 1.</td>
              <td width="87%" valign="top"><p>To add an underlying model, you will need to create or upload a model definitions file.</p>
                <ul>
                  <li>To create a model definitions file, <a href="../simulation_authoring/create_model_definitions.jsp">click here</a>.</li>
                  <li>To upload a model definitions file, click here.</li>
                </ul></td>
            </tr>
          <tr>
            <td valign="top">Step 2.</td>
              <td valign="top">If  the model is to be run locally, the jar file containing it will need to placed directory of libraries on your server. Please have your administrator do this and restart the web server.</td>
            </tr>
          <tr>
            <td valign="top">Step 3.</td>
              <td valign="top">Test that your model has been installed.</td>
            </tr>
          </table>
          <p>&nbsp;</p>
          <p>&nbsp;</p>
          <p>&nbsp;</p>
          <p>&nbsp;</p>
        </blockquote>      <a href="../simulation_authoring/incorporate_underlying_model.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
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
