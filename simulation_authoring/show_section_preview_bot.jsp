<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	String sec_id = request.getParameter("sec_id");
	
	System.out.println("secid is " + sec_id);
	
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>USIP OSP Control Page</title>
</head>

<body>
<div align="center">
<table><tr><TD>
<a href="<%= afso.backPage %>" target="bodyinleft"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>
</TD>
<%
	if (sec_id != null) {
	
		BaseSimSection bss = BaseSimSection.getMe(afso.schema, sec_id);
		
		if (bss.getClass().getName().equalsIgnoreCase("org.usip.osp.baseobjects.CustomizeableSection")) {	// if it can be customized

			CustomizeableSection cbss = (CustomizeableSection) bss;
%>
<td>&nbsp;</td>
<TD>
<%	// Figure out where to send the form to.
	
%>
<form id="section_form" name="section_form" method="post" action="<%= cbss.getMakePage() %>" target="bodyinleft">
<input name="edit_this_section" type="submit" value="Edit This Section" />
</form>
</TD>
<%
		} // End of if this is a customizable section.
	} // End of if sec_id is not null.
%>
</tr>
</table>
</div>
</body>
</html>
