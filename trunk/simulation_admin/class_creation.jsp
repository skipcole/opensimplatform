<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.coursemanagementinterface.*,	
	org.hibernate.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	int returnCode = ClassOfStudents.createClass(request);
		

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
.redstar {
	color: #F00;
}
-->
</style>
</head>
<body onLoad="">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
  	<td width="120" align="right" valign="top">&nbsp;</td>
    <td colspan="1" valign="top"><br /></td>
		</td>
  </tr>
</table>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
			  <h1>Class Creation</h1>
			  <br />
            <blockquote>
              <p>Please enter the information below:              </p>
              <form id="form1" name="form1" method="post" action="class_creation.jsp">
                
                <input type="hidden" name="add_class" value="true">
              <input type="hidden" name="schema" value="<%= afso.schema %>">
              
              <table width="100%" border="0">
                <tr>
                  <td>&nbsp;</td>
                  <td>Class Name <span class="redstar">*</span></td>
                  <td><label for="first_name"></label>
                    <input type="text" name="class_name" id="class_name" /></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td><input type="submit" name="button" id="button" value="Submit" /></td>
                </tr>



              </table>
            </form>
              <p><span class="redstar">*</span> Indicates a required field.</p>
              <p>&nbsp;</p>
<p>&nbsp;</p>
	          
			</blockquote>
            <p align="center">&nbsp;</p>
<p>&nbsp;</p>
</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">.</p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>

</body>
</html>