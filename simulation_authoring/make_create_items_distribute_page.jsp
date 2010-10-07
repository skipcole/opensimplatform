<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.specialfeatures.*" 
	errorPage="" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	String ii_id = (String) request.getParameter("ii_id");
	
	
	afso.handleDistributeItems(request);
	
	InventoryItem thisItem = new InventoryItem();
	
	if (ii_id != null) {
		thisItem = InventoryItem.getById(afso.schema, new Long(ii_id));
	}
	
	List actorL = new ArrayList();
		///////////////////////////////////////////////////////////////////
	Simulation simulation = new Simulation();
		if (afso.sim_id != null) {
			simulation = afso.giveMeSim();
			actorL = Actor.getAllForSimulation(afso.schema, afso.sim_id);
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
              <h1>Distribute Item <%= thisItem.getItemName() %></h1>
              <br />
    <p>Below are listed the quantities that the players will start this simulation with. If you  add the ability for them to give or receive items, the quanties that they have at any given time may change. 
    <form id="form1" name="form1" method="post" action="make_create_items_distribute_page.jsp">
	<input type="hidden" name="ii_id" value=<%= ii_id %> />
	<input type="hidden" name="sending_page" value="distribute_items"  />
      <table width="100%" border="1">
  <tr>
    <td valign="top"><strong>Actor</strong></td>
    <td valign="top"><strong>Quantity</strong></td>
    </tr>
        <%
			  		int ii = 0;
		for (ListIterator li = actorL.listIterator(); li.hasNext();) {
			Actor act = (Actor) li.next();
			
				%>
        
          <tr><td valign="top"><%= act.getActorName() %></td>
                <td valign="top"><label>
                  <input type="text" name="ii_assign_<%= act.getId() %>" />
                </label></td>
                </tr>
                <%
					}
				%>          <tr>
            <td valign="top">&nbsp;</td>
            <td valign="top">
              <label>
                <input type="submit" name="Submit" value="Submit" />
                </label>
            
            </td>
          </tr>
      </table>
	  </form>
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
