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
			response.sendRedirect("../login.jsp");
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
              <h1>Import Student CSV File</h1>
              <p>This page will allow you to import </p>
              <p>The following statements about the file you import must be true:</p>
              <ol>
                <li>Any line not meant to be imported should be commented out with a '#' symbol in the first position of the line.</li>
                <li>The first line should list the fields that are contained in the file. (Email, First Name, Last Name, etc.)</li>
                <li>The first field of the first non-commented line </li>
              </ol>

            
    <form enctype="multipart/form-data" action="import_student_csv_file.jsp" method="post">
   
        <input type="hidden" name="sending_page" value="import_csv" />
          <table width="50%" border="0" cellspacing="2" cellpadding="2">
            <tr> 
              <td valign="top"><input type="hidden" name="MAX_FILE_SIZE" value="200000" />
                Select file to upload: 
                   <input name="uploadedfile" type="file" tabindex="5" /></td>
            </tr>
            <tr> 
              <td valign="top">&nbsp;</td>
            </tr>
            <tr>
              <td valign="top"><input type="submit" name="import" value="Import" /></td>
              </tr>
            </table>
    <p>      </p>
    </form>
    <blockquote><%= importResults %></blockquote>
    <p>&nbsp;</p>
    <p align="center">&nbsp;</p>    <a href="simulation_admin_userrelated.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
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
