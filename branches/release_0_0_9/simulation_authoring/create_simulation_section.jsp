<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));

	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	afso.handleCreateSimulationSection(request);

	List baseList = BaseSimSection.getAll(afso.schema);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<title>Open Simulation Platform Control Page</title>



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
              <h1>Create Simulation Section</h1>
              <br />
			

      <form action="create_simulation_section.jsp" method="post" name="form1" id="form1">
  <input type="hidden" name="sending_page" value="create_section" />
        
        <table width="80%" border="0" cellspacing="2" cellpadding="2">
          <tr> 
            <td>URL:</td>
              <td><input type="text" name="url" tabindex="1" /></td>
            </tr>
          <tr> 
            <td>Directory:</td>
              <td><input type="text" name="directory" tabindex="2" /></td>
            </tr>
          <tr> 
            <td>Filename:</td>
              <td><input type="text" name="filename" tabindex="3" /></td>
            </tr>
          <tr> 
            <td>Recommended Tab Heading:</td>
              <td><input type="text" name="rec_tab_heading" tabindex="4" /></td>
            </tr>
          <tr> 
            <td>Description:</td>
              <td><textarea name="description"></textarea></td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td><label>
                <input name="send_rsid_info" type="checkbox" value="true" />
                Send Running Simulation Information</label></td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td><label>
                <input name="send_actor_info" type="checkbox" value="true" />
                Send Actor Information</label></td>
            </tr>
          <tr>
            <td>&nbsp;</td>
              <td><input name="send_user_info" type="checkbox" value="true" />
                Send User Information</td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td><input type="submit" name="createsection" value="Submit" tabindex="7" /> 
                <label> 
                  <input type="submit" name="clear_button" value="Clear" tabindex="8" />
                  </label></td>
            </tr>
          </table>
    <p>&nbsp;</p>
    <p></p>
        </form>
      <p>&nbsp;</p>
      <p>Below are listed alphabetically all of the current Base Simulation Sections.</p>
      <table width="80%" border="0" cellspacing="2" cellpadding="2">
        <tr> 
          <td><h2>Recommended Tab Heading</h2></td>
      <td><h2>File Name</h2></td>
    </tr>
        <%
		for (ListIterator li = baseList.listIterator(); li.hasNext();) {
			BaseSimSection bss = (BaseSimSection) li.next();		
		%>
        <tr> 
          <td><%= bss.getRec_tab_heading() %></td>
      <td><%= bss.getPage_file_name() %></td>
    </tr>
        <%
	}
%>
      </table>      <p><a href="<%= afso.backPage %>"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a></p>			</td>
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
