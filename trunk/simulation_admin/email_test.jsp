<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.communications.*" 
	errorPage="" %>
<%

	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String e_to = "";
	String e_cc = "";
	String e_bcc = "";
	String e_from = "";
	String e_subject = "";
	String e_text = "";
	String e_schema = "";
	
	String debug = "okay";

	String sending_page = (String) request.getParameter("sending_page");
	String send_email = (String) request.getParameter("send_email");
	
	if ( (sending_page != null) && (send_email != null) && (sending_page.equalsIgnoreCase("email_test"))){
	
		e_to = (String) request.getParameter("e_to");
		e_cc = (String) request.getParameter("e_cc");
		e_bcc = (String) request.getParameter("e_bcc");
		e_from = (String) request.getParameter("e_from");
		e_subject = (String) request.getParameter("e_subject");
		e_text = (String) request.getParameter("e_text");
		e_schema = (String) request.getParameter("e_schema");
	
		debug = Emailer.postSimReadyMail(e_schema, e_to, e_from, e_cc, e_bcc, e_subject, e_text);
		
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
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
              <h1>Current Installlation Email Parameters</h1>
              <p>Below are the values for the principal schema. This information is pulled out of the properties file, and is used when users attempt to retrieve their password.</p>
              
              <form id="form1" name="form1" method="post" action="">
              <table border="1" cellspacing="2" cellpadding="0">
                <tr>
                  <td width="60%" valign="top">email_archive_address</td>
                  <td width="40%" valign="top"><input type="text" name="textfield2" id="textfield2" value="<%= USIP_OSP_Properties.getValue("email_archive_address") %>" /></td>
                </tr>
                <tr>
                  <td valign="top">email_smtp</td>
                  <td valign="top"><input type="text" name="textfield" id="textfield" value="<%= USIP_OSP_Properties.getValue("email_smtp") %>" /></td>
                </tr>
                <tr>
                  <td valign="top">schema_organization</td>
                  <td valign="top"><input type="text" name="textfield" id="textfield" value="<%= USIP_OSP_Properties.getValue("schema_organization") %>" /></td>
                </tr>
                <tr>
                  <td valign="top">smtp_auth_password</td>
                  <td valign="top"><input type="text" name="textfield" id="textfield" value="<%= USIP_OSP_Properties.getValue("smtp_auth_password") %>" /></td>
                </tr>
                <tr>
                  <td valign="top">smtp_auth_user</td>
                  <td valign="top"><input type="text" name="textfield" id="textfield" value="<%= USIP_OSP_Properties.getValue("smtp_auth_user") %>" /></td>
                </tr>
                <tr>
                  <td valign="top"><p>Make Changes:</p>
                    <p>(<strong><em>Coming Soon!</em></strong>)</p></td>
                  <td valign="top">
                    <input type="submit" name="button" id="button" value="Submit" disabled="disabled" />
                  
                  </td>
                </tr>
              </table>
              </form>
              <p>&nbsp;</p>
              <p>&nbsp;</p>
              <h1>Send Test Email </h1>
              <h1>(Functionality in Progress)</h1>
              <br />
      <form name="form2" id="form2" method="post" action="">
        <table width="80%" border="0" cellspacing="2" cellpadding="1">
          <tr> 
            <td>To:</td>
        <td> <input type="text" name="e_to" value="<%= e_to %>"/>          </td>
      </tr>
          <tr> 
            <td>CC:</td>
        <td> <input type="text" name="e_cc"  value="<%= e_cc %>"/>          </td>
      </tr>
          <tr> 
            <td>BCC:</td>
        <td> <input type="text" name="e_bcc"  value="<%= e_bcc %>"/>          </td>
      </tr>
          <tr> 
            <td>From:</td>
        <td><input type="text" name="e_from"  value="<%= e_from %>"/></td>
      </tr>
          <tr> 
            <td>Subject</td>
        <td><input type="text" name="e_subject"  value="<%= e_subject %>"/></td>
      </tr>
          <tr> 
            <td>Text:</td>
        <td><input type="text" name="e_text"  value="<%= e_text %>"/></td>
      </tr>
          <tr>
            <td>Schema:</td>
            <td><input type="text" name="e_schema"  value="<%= e_schema %>"/></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
        <td><input type="hidden" name="sending_page" value="email_test" />
          <input type="submit" name="send_email" value="Submit" /></td>
      </tr>
          </table>
        </form>      <p><%= debug %></p>			</td>
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
