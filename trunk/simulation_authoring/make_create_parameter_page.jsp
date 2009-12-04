<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.communications.*" 
	errorPage="" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	GenericVariable gv = new GenericVariable();
	
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
              <h1>Create/Edit Parameters Page</h1>
              <br />
    <p>Parameters associated with a simulation can have their values modified during play.<form action="make_create_parameter_page.jsp" method="post" name="form2" id="form2">
      
      <h2>Create New Parameter</h2>
            <table>
              <tr>
                <td valign="top">Unique Parameter Name  <a href="helptext/uniq_doc_identifer_help.jsp" target="helpinright">(?)</a>:</td>
              <td valign="top"><input type="text" name="uniq_doc_title" value="<%= gv.getValue() %>" /></td></tr>
              <tr valign="top">
                <td>Starting Value</td>
                <td><input type="text" name="start_value" value="<%= gv.getStartingValue() %>" /></td>
              </tr>
              <tr valign="top">
                <td>Max Value</td>
                <td><input name="has_max_value" type="checkbox" value="checkbox" checked="checked" />
                  None or
                  <input type="text" name="max_value" value="<%= gv.getMaxValue() %>" /></td>
              </tr>
              <tr valign="top">
                <td>Min Value</td>
                <td><input name="has_min_value" type="checkbox" value="checkbox" checked="checked" />
                  None or
                  <input type="text" name="min_value" value="<%= gv.getMinValue() %>" /></td>
              </tr>
              <tr valign="top">
                <td>Propagation Means</td>
                <td><select name="prop_type">
                    <option value="player_set">Player Set</option>
                </select></td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr><td>&nbsp;</td><td>
              
              <% if (gv.getId() == null) { %>
              
              <input type="submit" name="create_doc" value="Create" />
              
              <%
				} else {
				%>
                <input type="hidden" name="gv_id" value="<%= gv.getId() %>" />
                <input type="submit" name="clear_button" value="Clear" />
                <input type="submit"  name="update_doc" value="Update Parameter" /> 
                <%
					}
				%>
              
              </td></tr>
              </table>
              <input type="hidden" name="sending_page" value="make_create_parameter_page" />
      </p>
    </form>
      <p>&nbsp;</p>
      <p>Below are listed all of the parameters currently associated with this simulation. </p>
      <table border="1">
  <tr><td><strong>Uniq Identifier</strong></td>
  </tr>
        <%
			  		int ii = 0;
					for (ListIterator li = GenericVariable.getAllBaseGenericVariablesForSim(afso.schema, afso.sim_id).listIterator(); li.hasNext();) {
						GenericVariable gv = (GenericVariable) li.next();
				%>
        
          <tr><td><a href="make_create_document_page.jsp?shared_doc_id=<%= gv.getId() %>"><%= gv.getUniqueDocTitle() %></a></td>
                </tr>
          
                <%
					}
				%>
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
<%
	
%>
