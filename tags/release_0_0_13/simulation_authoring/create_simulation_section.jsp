<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	//afso.backPage = afso.getBaseSimURL() + "/simulation_authoring/create_simulation_section.jsp";
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	
	BaseSimSection bss = afso.handleCreateSimulationSection(request);

	List baseList = BaseSimSection.getAllAuthorGenerated(afso.schema);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Create Simulation Section</h1>
              <br />
			

      <form action="create_simulation_section.jsp" method="post" name="form1" id="form1">
  <input type="hidden" name="sending_page" value="create_section" />   
        <table width="100%" border="0" cellspacing="2" cellpadding="2">
          <tr> 
            <td>Web Address (URL)* <a href="helptext/create_simsection_webaddress.jsp" target="helpinright">(?)</a>:</td>
              <td><input type="text" name="url" tabindex="1" value="<%= bss.getUrl() %>" /></td>
            </tr>
          <tr> 
            <td>Directory  <a href="helptext/create_simsection_directory.jsp" target="helpinright">(?)</a>:</td>
              <td><input type="text" name="directory" tabindex="2" value="<%= bss.getDirectory() %>" /></td>
            </tr>
          <tr> 
            <td>Filename  <a href="helptext/create_simsection_filename.jsp" target="helpinright">(?)</a>:</td>
              <td><input type="text" name="filename" tabindex="3" value="<%= bss.getPage_file_name() %>" /></td>
            </tr>
          <tr> 
            <td>Recommended Tab Heading*  <a href="helptext/create_simsection_rec_tab.jsp" target="helpinright">(?)</a>:</td>
              <td><input type="text" name="rec_tab_heading" tabindex="4" value="<%= bss.getRec_tab_heading() %>" /></td>
            </tr>
          <tr> 
            <td>Description <a href="helptext/create_simsection_description.jsp" target="helpinright">(?)</a>:</td>
              <td><textarea name="description" tabindex="5" ><%= bss.getDescription() %></textarea></td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td><label>
                <input name="send_rsid_info" type="checkbox" value="true" tabindex="6" />
                Send Running Simulation Information </label> <a href="helptext/create_simsection_sendrsinfo.jsp" target="helpinright">(?)</a></td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td><label>
                <input name="send_actor_info" type="checkbox" value="true" tabindex="7" />
                Send Actor Information</label> 
              <a href="helptext/create_simsection_sendactorinfo.jsp" target="helpinright">(?)</a></td>
            </tr>
          <tr>
            <td>&nbsp;</td>
              <td><input name="send_user_info" type="checkbox" value="true" tabindex="8" />
                Send User Information <a href="helptext/create_simsection_userinfo.jsp" target="helpinright">(?)</a></td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td><%
				if (bss.getId() == null) {
				%>
                <input type="submit" name="command" value="Create" tabindex="9" />
                <%
				} else {
				%>
                <input type="hidden" name="bss_id" value="<%= bss.getId() %>"  />
                <input type="submit" name="command" value="Clear" tabindex="10" />
                <input type="submit" name="command" value="Update" tabindex="11" />
                <%
					}
				%>
                  
                  </td>
            </tr>
          </table>
    <p>* Required Fields</p>
    <p></p>
        </form>
      <p>&nbsp;</p>
      <p>Below are listed alphabetically all of the Simulation Sections created by authors.</p>
      <table width="80%" border="0" cellspacing="2" cellpadding="2">
        <tr> 
          <td><h2>Recommended Tab Heading</h2></td>
      <td><h2>Full Path</h2></td>
      <td><h2>Delete</h2></td>
        </tr>
        <%
		for (ListIterator li = baseList.listIterator(); li.hasNext();) {
			BaseSimSection this_bss = (BaseSimSection) li.next();		
		%>
        <tr> 
          <td><a href="create_simulation_section.jsp?bss_id=<%= this_bss.getId() %>&command=Edit&sending_page=create_section"><%= this_bss.getRec_tab_heading() %></a></td>
      <td><%= this_bss.getFullPath() %></td>
      <td><a href="delete_object.jsp?object_type=section&objid=<%= this_bss.getId() %>">Remove</a></td>
        </tr>
        <%
	}
%>
      </table>      
      <p><a href="<%= afso.backPage %>"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a></p>			</td>
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
