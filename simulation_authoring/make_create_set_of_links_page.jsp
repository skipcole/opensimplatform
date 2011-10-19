<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.specialfeatures.*,
	org.usip.osp.communications.*" 
	errorPage="/error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	SetOfLinks sol  = afso.handleCreateSetOfLinks(request);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>

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
              <h1>Create/Edit Set of Links </h1>
              <br />
    <p>A 'Set of Links' is a set of Link objects that can be added into a running simulation or a set of running simulations.
    <form action="make_create_onelink_page.jsp" method="post" name="form2" id="form2">
      
      <h2>Create New 'Set of Links'</h2>
            <table width="100%">
              <tr>
                <td valign="top">Set of Links  Name  <a href="helptext/onelink_name.jsp" target="helpinright">(?)</a>:</td>
                <td valign="top"><input type="text" name="onelink_name" value="<%= sol.getName() %>" /></td></tr>
              <tr valign="top">
                <td>Set of Links  Notes <a href="helptext/onelink_notes.jsp" target="helpinright">(?)</a>:</td>
                <td><label>
                  <textarea name="onelink_notes" id="textarea" cols="45" rows="5"><%= sol.getNotes() %></textarea>
                </label></td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr><td>&nbsp;</td><td>
              
              <% if (sol.getId() == null) { %>
              
              <input type="submit" name="create_onelink" value="Create" />
              
              <%
				} else {
				%>
                <input type="hidden" name="sol_id" value="<%= sol.getId() %>" />
                <input type="submit" name="clear_button" value="Clear" />
                <input type="submit"  name="update_onelink" value="Update One Link" /> 
                <%
					}
				%>
              
              </td></tr>
              </table>
              <input type="hidden" name="sending_page" value="make_create_onelink_page" />
      </p>
    </form>
      <p>&nbsp;</p>
      <p>Below are listed all of the 'set of links' currently associated with this simulation. </p>
      <table border="1" width="100%">
  <tr>
    
    <td><strong>Set of Links   Identifier</strong></td>
    </tr>
        <%
					for (ListIterator li = SetOfLinks.getAllBaseSetOfLinksForSim(afso.schema, afso.sim_id).listIterator(); li.hasNext();) {
						SetOfLinks sol_l = (SetOfLinks) li.next();
		%>
          <tr>
            <td><a href="make_create_set_of_links_page.jsp?sol_id=<%= sol_l.getId() %>&queueup=true"><%= sol_l.getName() %></a></td>
            </tr>
        <% } %>
      </table>
      <p>&nbsp;</p>
      <p><a href="<%= afso.backPage %>"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a></p>			</td>
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