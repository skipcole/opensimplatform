<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java"  
	isErrorPage="true"  
	import="java.sql.*,
		java.io.*,
		java.util.*,
		org.usip.osp.baseobjects.*,
		org.usip.osp.persistence.*" %>
<%
	
	OSPErrors error = new OSPErrors();
	
	String sending_page = request.getParameter("sending_page");
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("error_jsp"))) {
		error = OSPErrors.processForm(request);
	} else {
		System.out.println("storing web errors");
		error = OSPErrors.storeWebErrors(exception, request);
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Error Page</title>

<link href="usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Sadly, An Error Has Occured</h1>
              <p>The error encountered has now been logged to the database. </p>
              <p>If you could be so kind to fill in the short form below, it will help us make our platform stronger. </p>
			  <%
			  		// The error.jsp page falls into place where ever the broken jsp was, so we must make a direct reference 
					// back to the form in our post.
					String error_url = USIP_OSP_Properties.getErrorJspUrl();
			  %>
			  <form id="form1" name="form1" method="post" action="<%= error_url %>">
			  <input type="hidden" name="sending_page" value="error_jsp" />
			  <input type="hidden" name="error_id" value="<%= error.getId() %>" />
              <table width="100%" border="1">
                <tr>
                  <td align="left" valign="top">What were you doing when this error occured? </td>
                  <td align="left" valign="top">
                    <label>
                      <textarea name="user_notes" cols="80" rows="5"><%= error.getUserNotes() %></textarea>
                      </label>                  </td>
                </tr>
                <tr>
                  <td align="left" valign="top">How would you rate this error? </td>
                  <td align="left" valign="top">
                  <label><input name="user_err_level" type="radio" value="1" />
                  Mild</label><br/>
                  <label><input name="user_err_level" type="radio" value="2" checked="checked" />
                  Annoying</label><br/>
                  <label><input name="user_err_level" type="radio" value="3" />
                  Severe</label><br/>
                  <label><input name="user_err_level" type="radio" value="4" />
                  Show Stopper</label> </td>
                </tr>
                <tr>
                  <td align="left" valign="top">Would you like an email when this issue is resolved? </td>
                  <td align="left" valign="top"><label>
                    <input name="radiobutton" type="radio" value="email_requested" checked="checked" />
                  </label>
                  No<br />
                  <label>
                  <input name="radiobutton" type="radio" value="email_requested" />
                  Yes</label></td>
                </tr>
                <tr>
                  <td align="left" valign="top">What email should we send the </td>
                  <td align="left" valign="top"><label>
                    <input type="text" name="user_email" value="<%= error.getUserEmail() %>" />
                  </label></td>
                </tr>
                <tr>
                  <td align="left" valign="top">&nbsp;</td>
                  <td align="left" valign="top"><label>
                    <input type="submit" name="Submit" value="Submit" />
                  </label></td>
                </tr>
              </table>
			  </form>
              <p>&nbsp;</p>
              <p>Thank you for helping us make the USIP OSP better and better, every day.</p>
              <p>&nbsp;</p></td>
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
