<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.hibernate.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	afso.backPage = "../simulation_authoring_play/create_running_sim.jsp";
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}
	
	String launchResults = afso.handleSetUpBetaTests(request);

	List simList = Simulation.getAll(afso.schema);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>USIP Open Simulation Platform</title>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td><table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
              <tr>
                <td width="120"><img src="../Templates/images/white_block_120.png" /></td>
                <td width="100%"><h1> <br />
                    Launch Beta Tests</h1>
                  <p>On this page you can automatically set up many running simulations instantly. One will be created and enabled for each  for each user (beta tester) that you enter. Each user will be added to the simulation created for them as every actor in that simulation. <br />
                  </p>
                  <blockquote><form id="form1" name="form1" method="post" action="">
                    <table width="100%" border="1" cellspacing="0" cellpadding="0">
                      <tr>
                        <td width="3%" valign="top">1</td>
                        <td width="41%" valign="top">Select Simulation</td>
                        <td width="56%" valign="top">
                          <select name="sim_id" id="select">
                          <% for (ListIterator li = simList.listIterator(); li.hasNext();) {
								Simulation sim = (Simulation) li.next(); 
								
								String selected = "";
								
								if ((sim.getId() != null) && (afso.sim_id != null) && (sim.getId().intValue() == afso.sim_id.intValue())){
								selected = "selected";
								
								}
								%>
                            <option value="<%= sim.getId() %>" <%= selected %>><%= sim.getDisplayName() %></option>
                          <% } %>
                          </select>
                        </td>
                      </tr>
                      <tr>
                        <td valign="top">2</td>
                        <td valign="top">Enter set of user's emails.</td>
                        <td valign="top">
                          <label>
                            <textarea name="users_emails" id="textarea" cols="60" rows="5"></textarea>
                            </label>
                        
                        </td>
                      </tr>
                      <tr>
                        <td valign="top">3</td>
                        <td valign="top">Send invitation emails?</td>
                        <td valign="top"><label>
                          <input name="send_emails" type="checkbox" id="checkbox" checked="checked" />
                        </label>
                        Checked for Yes</td>
                      </tr>
                      <tr>
                        <td valign="top">4</td>
                        <td valign="top">Enter email text to send.</td>
                        <td valign="top"><label>
                          <textarea name="email_text" id="email_text" cols="60" rows="8">
You are invited to enter a Beta Test of a simulation. 
Please go to the website [web_site_location] to enter. 
Your username is [username].
Enjoy!
                          </textarea>
                          <br />
                          <font color="#CC9900">Note:</font> You should not need to replace 
                    the text inside of brackets []. If your system is configured 
                    correctly, these will automatically be replaced with the correct 
                    information in the emails sent out.</label></td>
                      </tr>
                      <tr>
                        <td valign="top">5</td>
                        <td valign="top">Hit submit</td>
                        <td valign="top">
                        	<input type="hidden" name="sending_page" value="launch_beta" />
                          <input type="submit" name="button" id="button" value="Submit" />
                        </label></td>
                      </tr>
                    </table>
                    </form>
                    <p><%= launchResults %></p>
                    <p><a href="play_panel.jsp">&lt;-- Back</a></p>
                  </blockquote>
                  <p>&nbsp;</p>
                </td>
              </tr>
            </table></td>
        </tr>
        <tr>
          <td><p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>.</p></td>
        </tr>
      </table></td>
  </tr>
</table>
<p>&nbsp;</p>
<p align="center">&nbsp;</p>
</body>
</html>
<%
%>
