<%@ page contentType="text/html; charset=iso-8859-1" language="java" 
import="java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
errorPage="" %>
<%
	String custom_page = request.getParameter("custom_page");
	
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String tab_heading = (String) session.getAttribute("tab_heading");
    String tab_pos = (String) session.getAttribute("tab_pos");
	String universal = (String) session.getAttribute("universal");
	
	System.out.println("cp universal was : " + universal);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	MultiSchemaHibernateUtil.beginTransaction(pso.schema);
	CustomizeableSection cs = (CustomizeableSection) MultiSchemaHibernateUtil.getSession(pso.schema).get(CustomizeableSection.class, new Long(custom_page));
	
	MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>USIP Open Simulation Platform</title>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" --><!-- InstanceEndEditable -->
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
			<!-- InstanceBeginEditable name="pageTitle" --><H1>Creating customized '<%= cs.getRec_tab_heading() %>' </H1><!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" -->
<form id="form1" name="form1" method="post" action="process_custom_page.jsp">
<input type="hidden" name="tab_pos" value="<%= tab_pos %>">
<input type="hidden" name="universal" value="<%= universal %>">
  <blockquote>
    <p>
      <input type="hidden" name="custom_page" value=<%= custom_page %> />
    </p>
  </blockquote>
        <table>
          <tr valign="top"> 
            <td>New Tab Heading </td>
            <td> 
              <input type="text" name="tab_heading" value="<%= tab_heading %>"/></td>
    </tr>
    <%
		for (Enumeration e = cs.getMeta_content().keys(); e.hasMoreElements();){
			String key = (String) e.nextElement();
%>
          <tr valign="top"> 
            <td><%= cs.getMeta_content().get(key) %> </td>
            <td> 
              <%
	  	String currVal = "";
		try {
			currVal = (String) cs.getContents().get(key);
		} catch (Exception eal){
			System.out.println("current value null: " + eal.getMessage());
		}
		
		String dataType = "";
		try {
			dataType = (String) cs.getMeta_data_content().get(key);
		} catch (Exception eal){
			System.out.println("dataType value null: " + eal.getMessage());
		}
	  %>
	  <% if ((dataType != null) && (dataType.equalsIgnoreCase("textarea"))) { %>
	   <textarea name="<%= key %>" cols="60" rows="5"><%= currVal %></textarea>
	  <% } else { %>
	  <input type="text" name="<%= key %>" value="<%= currVal %>" />
	  <% } %>
	  </td>
    </tr>
    <%
		}
%>
  </table>
        <blockquote>
    <p>
      <input type="submit" name="Submit" value="Submit" />
    </p>
  </blockquote>
</form>
<blockquote>
  <p>&nbsp;</p>
  <p><a href="set_specific_sim_sections.jsp"></a></p>
</blockquote>
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
