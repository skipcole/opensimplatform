<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.specialfeatures.*,org.usip.osp.graphs.*" 
	errorPage="../error.jsp" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
		

    String obj_id = request.getParameter("obj_id");
    String obj_class = request.getParameter("obj_class");
        

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>USIP Open Simulation Platform Delete Object Page</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<script language="JavaScript" type="text/javascript" src="../help-bubble.js">
</script>
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>This Object is used in These Sections</h1>
              <br />
      <blockquote>
          <p>
          <table border="1">
          <tr>
            <td><strong>Recommended Tab Heading</strong></td><td><strong>Section Description</strong></td></tr>
		  <% 
		  
		  Hashtable sectionsShown = new Hashtable();
		  
		  List theBss = BaseSimSectionDepObjectAssignment.getObjectUsages(afso.schema, new Long(obj_id), obj_class);
		  
		  for (ListIterator li = theBss.listIterator(); li.hasNext();) {
			BaseSimSectionDepObjectAssignment bssdoa = (BaseSimSectionDepObjectAssignment) li.next();
			
			CustomizeableSection custSec = CustomizeableSection.getById(afso.schema, bssdoa.getBss_id().toString());
			
			// Only show if not shown before
			if (sectionsShown.get(custSec.getId()) == null) {
		  %>
          <tr><td><%= custSec.getRec_tab_heading() %></td><td><%= custSec.getDescription() %></td></tr>
          <%
		  		// Record that it has been shown already.
		  	sectionsShown.put(custSec.getId(), "set");
		 	}  // end of if it has not been shown before
			
		  } // End of loop over bssdoa's
		  %>
          </table>
          </p>
      </blockquote>
      <blockquote>
        <p><a href="<%= afso.backPage %>">Back</a></p>
      </blockquote>
			</td>
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
