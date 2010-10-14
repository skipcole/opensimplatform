<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
		org.usip.osp.networking.*,
		org.usip.osp.persistence.*,
		org.usip.osp.baseobjects.*,
		org.usip.osp.networking.*" 
	errorPage="" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	//////////////////////////////////
	// Get list of all simulations for pull down
	List simList = Simulation.getAll(afso.schema);
	
	// Determine if setting sim to edit.
	String sending_page = (String) request.getParameter("sending_page");
	
	String saveMsg = "";
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("package"))){
	
		String sim_id = (String) request.getParameter("sim_id");
		String filename = (String) request.getParameter("filename");
		
		saveMsg = "File saved to " + 
		afso.handlePackageSim(sim_id, filename);
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
              <h1>Package Simulation</h1>
              <br />
      <blockquote>
        <p><%= saveMsg %></p>
          <table width="100%" border="1">
            <tr><td>Simulation</td><td>File Name</td><td>Submit</td></tr>
            <%
		
		for (ListIterator li = simList.listIterator(); li.hasNext();) {
			Simulation sim = (Simulation) li.next();
			String nameToSend = java.net.URLEncoder.encode(sim.getSimulationName());
			
		%>
            <tr> 
              <form id="form2" name="form2" method="post" action="package_simulation.jsp">
                <td>
                  <input type="hidden" name="sending_page" value="package" />
                  <input type="hidden" name="sim_id" value="<%= sim.getId().toString() %>" />
                  <%= sim.getSimulationName() %> : <%= sim.getVersion() %>                  </td>
              <td>
                <input name="filename" type="text" id="textfield" value="<%= afso.getDefaultSimXMLFileName(sim) %>" size="60" />                </td>
              <td><label>
                <input type="submit" name="button" id="button" value="Package It" />
                </label>            </td>
			  </form>
            </tr>
            <%
	}
%>
            </table>
      </blockquote>
      <hr />
      <p>Your currently exported simulations:</p>
      <p> 
      <ul>
        <% 
		
		ArrayList loss = new ArrayList();
		
		for (ListIterator li = FileIO.getListOfSavedSims().listIterator(); li.hasNext();) {
			String sim = (String) li.next();
			loss.add(sim);	
		}
		
		Collections.sort(loss);
		
		for (ListIterator li = loss.listIterator(); li.hasNext();) {
			String sim = (String) li.next(); %>
        <li><a href="./packaged_simulations/<%= sim %>" target="_new"><%= sim %></a></li>
	  <% } %>
          </ul>
        </p>
        <p>&nbsp;</p>        <p>&nbsp;</p>			</td>
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
