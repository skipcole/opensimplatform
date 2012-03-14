<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.baseobjects.*,
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
	
	ClassOfStudents cos = new ClassOfStudents();
	
	String classId = request.getParameter("class_id");
	
	if (classId != null) {
		cos = ClassOfStudents.getById(afso.schema, new Long(classId));
	}
	
	String add_instructor = request.getParameter("add_instructor");
	if ((add_instructor != null) && (add_instructor.equalsIgnoreCase("true"))) {
		String instructor_username = request.getParameter("instructor_username");
		System.out.println("adding instructor: " + instructor_username );
	}
		

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
			  <h1>Class Details</h1>
			  <p>&nbsp;</p>
			  <p>Class: <%= cos.getClassName() %></p>
			  <p>Creation Date</p>
			  <p>Instructors</p>
			  <blockquote>
              <% 
			  	List instructors = ClassOfStudents.getAllInstructorsForClass(afso.schema, cos.getId());
				%>
                <ol>
                               <%
			
				for (ListIterator li = instructors.listIterator(); li.hasNext();) {
					User user = (User) li.next();
					
					%>
                    <li><%= user.getId() %></li>
                    <% } %>
                    </ol>
			    <p></p>
			    <form id="form1" name="form1" method="post" action="class_details.jsp">
                <input type="hidden" name="class_id" value="<%= classId %>" />
                <input type="hidden" name="add_instructor" value="true">
			      <p>
			        <label for="textfield"></label>
			        <input type="text" name="instructor_username" id="instructor_username" />
			        </p>
			      <p>
			        <input type="submit" name="button" id="button" value="Add Instructor" />
			        </p>
			      </form>
			    </blockquote>
			  <p>&nbsp;</p>
			  <p>Students<br />
			    </p>
			  <blockquote>
			    
			    
			    
			    </blockquote>

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