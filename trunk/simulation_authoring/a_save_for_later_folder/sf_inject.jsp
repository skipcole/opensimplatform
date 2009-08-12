<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.specialfeatures.*" 
	errorPage="" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true), true);
	
	// Determine if setting sim to edit.
	String sending_page = (String) request.getParameter("sending_page");
	String create_new = (String) request.getParameter("create_new");
		
	String debug = "";
	
	IntVariable sv = new IntVariable();
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("add_sim_var"))){
		
		sv.setName(request.getParameter("var_name"));
		sv.setDescription(request.getParameter("description"));
		sv.setPropagation_type(request.getParameter("prop_type"));
		sv.setInitialValue(request.getParameter("start_value"));
		afso.hibernate_session.saveOrUpdate(sv);
		afso.simulation.getVar_int().add(sv);
		afso.hibernate_session.saveOrUpdate(afso.simulation);
		
	} // End of if 
	
		//////////////////////////////////
	// Put shared document on scratch pad
	String edit_sv = (String) request.getParameter("edit_sv");
	
	boolean inEditingMode = false;
	
	String varBoolean = "";
	String varDecimal = "";
	String varInteger = "";
	
	
	if ((edit_sv != null) && (edit_sv.equalsIgnoreCase("true"))){
		/*
		inEditingMode = true;
		
		sv = new IntegerVariable();
		
		sv.set_sf_id((String) request.getParameter("sf_id"));
		
		sv.load();
		*/
					
	}
	///////////////////////////////////////

	List simInts = afso.simulation.getVar_int();
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<title>Open Simulation Platform Control Page</title>



<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Add / Edit Integer Variable</h1>
              <br />
    <p><%= Debug.getDebug(debug) %></p>
    <blockquote>
      <p>Current integer variables for the Simulation <%= afso.simulation.getDisplayName() %>:</p>
          <blockquote>
            <p>
              <% if (simInts.size() == 0) { %>
              </p>
          </blockquote>
          <ul>
            <li>None
              <p>
                <% } %>
                <% for (ListIterator li = simInts.listIterator(); li.hasNext();) {
					IntVariable iv = (IntVariable) li.next();
	%>
                </p>
            </li>
            <li><a href="sf_sim_var_integer.jsp?edit_sv=true&amp;sf_id=<%= iv.getid() %>"><%= iv.getName() %></a>
              <a href="../delete_object.jsp?object_type=sf_var_int&amp;objid=<%= iv.getid() %>&amp;backpage=sf_sim_var_integer.jsp&amp;object_info=&quot;<%= iv.getname() %>&quot;"> 
                (Remove) <%= iv.getName() %> </a>
              <p>
                <% } %>
                </p>
            </li>
          </ul>
          <p>Add a possible inject </p>
      </blockquote>
    <form name="form2" id="form2" method="post" action="sf_sim_var_integer.jsp">
      <input type="hidden" name="sending_page" value="add_sim_var">
      <table width="80%" border="0" cellspacing="2" cellpadding="1">
        <tr valign="top"> 
          <td width="32%">&nbsp;</td>
              <td width="32%">Inject Name</td>
              <td width="68%" colspan="2"> <input name="var_name" type="text" size="80" value="<%= sv.getName() %>" />            </td>
            </tr>
        <tr valign="top">
          <td>&nbsp;</td>
              <td>Inject Description</td>
              <td colspan="2"><textarea name="description" cols="30" rows="2"><%= sv.getDescription() %></textarea></td>
            </tr>
        <tr valign="top"> 
          <td>&nbsp;</td>
              <td>Inject Text </td>
              <td colspan="2"> <input type="text" name="start_value" value="<%= sv.getInitial_value() %>" />            </td>
            </tr>
        <tr> 
          <td>&nbsp;</td>
              <td> <% if (inEditingMode) { %> <input type="submit" name="edit_sim_var" value="Update" /> 
                <% } else { %> <input type="submit" name="create_new" value="Submit" /> 
                <% } %></td>
              <td colspan="2">&nbsp;</td>
            </tr>
        </table>
      </form>
    <p>&nbsp;</p>
    <p align="center"><a href="../incorporate_underlying_model.jsp">Back to Add Special 
      Features</a></p>    <p>&nbsp;</p>			</td>
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
