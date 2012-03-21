<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.coursemanagementinterface.*,
	org.usip.osp.baseobjects.*,
	com.oreilly.servlet.*" 
	errorPage="/error.jsp" %>
<%
		AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
		
		if (!(afso.isLoggedin())) {
			response.sendRedirect("../blank.jsp");
			return;
		}
		
		String importResults = CSVInterpreter.importCSV(request, afso.schema);


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>


<style type="text/css">
<!--
.style1 {
	color: #FF0000;
	font-weight: bold;
}
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
              <h1>Import Student Information</h1>
              <p>Below are the students to be imported for this class.</p>
              
              <form id="form1" name="form1" method="post" action="">
              <table width="100%" border="0">
                <tr>
                  <td>First Name</td>
                  <td>Last Name</td>
                  <td>User Name</td>
                  <td>Password</td>
                  <td>Include</td>
                </tr>
                <tr>
                  <td><label for="textfield"></label>
                    <input type="text" name="textfield" id="textfield" /></td>
                  <td>
                   
                    <input type="text" name="textfield2" id="textfield2" />
                  </td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                </tr>
              </table>
              </form>
              <p>&nbsp;</p>
              <p>&nbsp;</p>
              <p>The following statements about the file you import must be true:</p>
              <ol>
                <li>Any line not meant to be imported should be commented out with a '#' symbol in the first position of the line.</li>
                <li>The first line should list the fields that are contained in the file. (Email, First Name, Last Name, etc.) A template file you can enter data into is <a href="import_student_csv_template.csv">located here</a>.</li>
                <li>You can enter the user's intial password in the 'Password' field, or just enter the word 'Initials.' If you do that later, the user's initials will become their first password. For example, a user named Able Baker would have the intial password of 'AB'.</li>
                </ol>
              <p>Please also note that:</p>
              <ol>
                <li>The student's email address will become their user name.</li>
                <li>Importing students into the platform does not immediately give them access to any simulations. You will need to assign them a role in a simulation before they will actually be able to log in and do anything.</li>
              </ol>
              <p>Follow the steps below to perform the import.</p>

            
    <form enctype="multipart/form-data" action="../simulation_admin/import_student_csv_file.jsp" method="post">
      
      <input type="hidden" name="sending_page" value="import_csv" />
      <p>      </p>
  </form>
    <blockquote><%= importResults %></blockquote>
    <p>&nbsp;</p>
    <p align="center">&nbsp;</p></td>
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
