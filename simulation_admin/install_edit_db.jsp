<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
		org.usip.osp.baseobjects.*,
		org.usip.osp.networking.*,
		org.usip.osp.persistence.*" 
	errorPage="/error.jsp" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (   !(afso.isLoggedin() )  || (   !(afso.isAdmin())   )   ) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	SchemaInformationObject sio = new SchemaInformationObject();
	
	String load = (String) request.getParameter("load");
	
	if ((load != null) && (load.equalsIgnoreCase("true") ) ) {
		String sio_id = (String) request.getParameter("sio_id");
		
		Long schema_id = new Long(sio_id);
		sio = SchemaInformationObject.getById(schema_id);
	}
	
	sio.cleanForPresentation();
	
	String error_msg = afso.handleCreateOrUpdateDB(request, afso.user_id);

        

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Open Simulation Platform Control Page</title>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
	background-image: url(../Templates/images/page_bg.png);
	background-repeat: repeat-x;
}
-->
</style>
</head>
<body onLoad="">

<table width="720" bgcolor="#DDDDFF" align="center" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="3">
        <h1>View / Edit / Install Database</h1>
        <p><strong>Table of Contents</strong></p>
        <ol>
          <li><a href="#view_conn_param">View Connection Parameters</a> (from USIP_OSP_Properties File)</li>
          <li><a href="#ViewDatabases">View Currently Installed Databases</a></li>
          <li><a href="#EditInstall">View Edit / Install a Database</a></li>
        </ol>
        <hr />
        <p>&nbsp;</p>
        <h2><a name="view_conn_param" id="view_conn_param"></a>View Connection Parameters (from USIP_OSP_Properties File)</h2>
        <blockquote>
          <table width="80%" border="1" cellspacing="2" cellpadding="1">
            <tr>
              <td width="40%">Database Location</td>
              <td width="60%"><%= USIP_OSP_Properties.getValue("loc") %></td>
            </tr>
            <tr>
              <td>Database Port</td>
              <td><%= USIP_OSP_Properties.getValue("port") %></td>
            </tr>
            <tr>
              <td>Database User</td>
              <td><%= USIP_OSP_Properties.getValue("username") %></td>
            </tr>
            <tr>
              <td>Database Password</td>
              <td>*******</td>
            </tr>
          </table>
          <p>&nbsp;</p>
      </blockquote>
        <blockquote>
          <h2>
            <%
		  	List ghostList = SchemaInformationObject.getAll();
		  %>
            <a name="ViewDatabases" id="ViewDatabases"></a>View Currently Installed Databases          </h2>
          <h3>(Click on Schema Name to See Details or Edit Below)</h3>
          <table width="80%" border="1" cellspacing="2" cellpadding="1">
            <tr> 
              <td><strong>id</strong></td>
              <td><strong>Schema Name</strong></td>
              <td><strong>Organization</strong></td>
              <td><strong>Last Login</strong></td>
            </tr>
            <%
			  	for (ListIterator<SchemaInformationObject> li = ghostList.listIterator(); li.hasNext();) {
            		SchemaInformationObject this_sg = (SchemaInformationObject) li.next();
				%>
            <tr> 
              <td><%= this_sg.getId() %></td>
              <td><a href="install_edit_db.jsp?load=true&sio_id=<%= this_sg.getId() %>"><%= this_sg.getSchema_name() %></a></td>
              <td><%= this_sg.getSchema_organization() %></td>
              <td><%= this_sg.getLastLogin() %></td>
            </tr>
            <%
		  		} // End of loop over schemas
		  %>
      </table>    </td>
  </tr>
  <tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24"  >&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="3"><a name="EditInstall" id="EditInstall"></a> 
    <form action="install_edit_db.jsp" method="post" name="form1" id="form1">
    <input type="hidden" name="sending_page" value="clean_db" />
        <h2> View Edit / Install Database     </h2>
        <blockquote>
          <blockquote> 
            <table  border="1" cellspacing="2" cellpadding="1">
              <tr> 
                <td colspan="2"><strong>Database Information</strong></td>
              </tr>
              <tr> 
                <td valign="top">DB Schema <a href="../osp_install/helptext/db_schema.jsp" target="helpinright">(?)</a></td>
                <td valign="top"><input type="text" name="db_schema" value="<%= sio.getSchema_name() %>" /></td>
              </tr>
              <tr> 
                <td valign="top">Organization</td>
                <td valign="top"><input type="text" name="db_org" value="<%= sio.getSchema_organization() %>" /></td>
              </tr>
              <tr>
                <td valign="top">DB Notes</td>
                <td valign="top"><textarea name="db_notes" cols="40" rows="2"><%= sio.getNotes() %></textarea></td>
              </tr>
              <tr> 
                <td valign="top">DB Creation Date</td>
                <td valign="top"><%= sio.getCreationDate() %></td>
              </tr>
                            <tr> 
                <td valign="top">DB Last Login</td>
                <td valign="top"><%= sio.getLastLogin() %></td>
              </tr>
              <tr> 
                <td valign="top">&nbsp;</td>
                <td valign="top">&nbsp;</td>
              </tr>
              <tr> 
                <td valign="top"><strong>Current Admins</strong></td>
                <td valign="top">&nbsp;</td>
              </tr>
              <tr> 
                <td valign="top">&nbsp;</td>
                <td valign="top">
                <%
					if (sio.getId() == null) {
				%>
                You () will be added as administrator to any schema you create.
                <%
				} else {
				%>
                Loop over Admins to show them.
                <% } %>
                </td>
              </tr>
               <tr>
                 <td valign="top">&nbsp;</td>
                 <td valign="top">&nbsp;</td>
               </tr>
              <tr> 
                <td valign="top"><strong>Email Settings</strong></td>
                <td valign="top">&nbsp;</td>
              </tr>
              <tr> 
                <td valign="top">email SMTP </td>
                <td valign="top"><label> 
                  <input type="text" name="email_smtp" value="<%= sio.getEmail_smtp() %>" />
                  </label></td>
              </tr>
             <tr> 
                <td valign="top">email user </td>
                <td valign="top"><input type="text" name="email_user" value="<%= sio.getSmtp_auth_user() %>" /></td>
              </tr>
              <tr> 
                <td valign="top">email password </td>
                <td valign="top"><input type="text" name="email_pass" value="<%= sio.getSmtp_auth_password() %>" /></td>
              </tr>
              <tr> 
                <td valign="top">email archive </td>
                <td valign="top"><input type="text" name="email_user_address"  value="<%= sio.getEmail_archive_address() %>" /></td>
              </tr>
              <tr>
                <td valign="top">email state</td>
                <td valign="top"><input type="text" name="email_user_address"  value="<%= sio.getEmailState() %>" /></td>
              </tr>
              <tr>
                <td valign="top">email server number (?)</td>
                <td valign="top"><input type="text" name="email_server_number"  value="<%= sio.getEmailServerNumber() %>" /></td>
              </tr>
              <tr>
                <td valign="top">&nbsp;</td>
                <td valign="top">&nbsp;</td>
              </tr>
              <tr>
                <td valign="top"><strong>Installed Components</strong></td>
                <td valign="top">&nbsp;</td>
              </tr>
              <tr>
                <td valign="top">&nbsp;</td>
                <td valign="top">
                <%
					if (sio.getId() == null) {
				%>
                	<input name="loadss" type="checkbox" value="true" checked="checked" />
					Load all section descriptor files found in sections directory
				<%
					} else {
				%>
                	Installed components for this schema may be seen by logging into the schema as an Admin and going to the 
                    administrative page 'Install Simulation Sections.'
                <% } %>

</td>
              </tr>
              <tr>
                <td valign="top">&nbsp;</td>
                <td valign="top">&nbsp;</td>
              </tr>
              <tr>
                <td valign="top"><strong>Create / Update</strong></td>
                <td valign="top">&nbsp;</td>
              </tr>
              <tr>
                <td valign="top">&nbsp;</td>
                <td valign="top">
				<%
					if (sio.getId() == null) {
				%>
                <input type="submit" name="command" value="Create" />
                <%
				} else {
				%>
                <input type="hidden" name="sio_id" value="<%= sio.getId() %>" />
                <input type="submit" name="command" value="Clear" tabindex="6" />
                <input type="submit" name="command" value="Update" />
                <%
					}
				%>              
                </td>
              </tr>
              <tr>
                <td valign="top">&nbsp;</td>
                <td valign="top"><%= error_msg %></td>
              </tr>
            </table>
          </blockquote>
          <p>&nbsp;</p>
        </blockquote>
        <p>&nbsp;</p>
      </form>
      <p>&nbsp;</p>
      <p>&nbsp;</p></td>
  </tr>
  <tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24"  >&nbsp;</td>
  </tr>
</table>
<p>&nbsp;</p>

<p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP 
  Open Source Software Project</a>. </p>
</body>
</html>
<%
	
%>
