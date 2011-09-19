<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.specialfeatures.*" 
	errorPage="/error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	InventoryItem thisItem = afso.handleCreateItems(request);
	
	Simulation sim = new Simulation();	
	
	if (afso.sim_id != null){
		sim = afso.giveMeSim();
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
              <h1>CreateInventory Items </h1>
              <br />
    <p>Items are things that players can keep in their inventories and give to each other. 
    <form action="make_create_items_page.jsp" method="post" name="form2" id="form2">
      
      <h2>Create New Item </h2>
            <table width="100%">
              <tr>
                <td valign="top">Item Name <a href="#" target="helpinright">(?)</a>:</td>
                <td valign="top"><input type="text" name="item_name" value="<%= thisItem.getItemName() %>" /></td></tr>
              <tr>
                <td valign="top">Item Description </td>
                <td valign="top"><input type="text" name="item_description" value="<%= thisItem.getDescription() %>" /></td>
              </tr>
              <tr>
                <td valign="top">Items Notes:</td>
                <td valign="top"><label>
                  <textarea name="item_notes" id="item_notes" cols="45" rows="5"><%= thisItem.getNotes() %></textarea>
                </label></td>
              </tr>
              <tr><td>&nbsp;</td><td>
              
              <% if (thisItem.getId() == null) { %>
              
              <input type="submit" name="create_item" value="Create" />
              
              <%
				} else {
				%>
                <input type="hidden" name="ii_id" value="<%= thisItem.getId() %>" />
				<table width="100%"><tr>
                <td align="left"><input type="submit"  name="update_item" value="Update" /></td>
				<td align="right"><input type="submit" name="clear_button" value="Clear to Create New Item" /></td>
				</tr></table>
                <%
					}
				%>
              
              </td></tr>
              </table>
              <input type="hidden" name="sending_page" value="make_create_items_page" />
      </p>
    </form>
      <p>&nbsp;</p>
      <p>Below are listed all of the items currently associated with this simulation. </p>
      <table width="100%" border="1">
  <tr>
    <td valign="top"><strong>Item Names </strong></td>
    <td valign="top"><strong>Item Description </strong></td>
    <td valign="top"><strong>Item Notes</strong></td>
    <td valign="top"><strong>Distribute</strong></td>
  </tr>
        <%
			  		int ii = 0;
					for (ListIterator li = InventoryItem.getAllTemplateForSim(afso.schema, afso.sim_id).listIterator(); li.hasNext();) {
						InventoryItem inventoryItem = (InventoryItem) li.next();
				%>
        
          <tr><td valign="top"><a href="make_create_items_page.jsp?ii_id=<%= inventoryItem.getId() %>&queueup=true"><%= inventoryItem.getItemName() %></a></td>
                <td valign="top"><%= inventoryItem.getDescription() %></td>
                <td valign="top"><%= inventoryItem.getNotes() %></td>
                <td valign="top"><a href="make_create_items_distribute_page.jsp?ii_id=<%= inventoryItem.getId() %>">Give to Players</a> </td>
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
