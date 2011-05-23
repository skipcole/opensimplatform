<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,java.io.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	afso.backPage = "publish_sim.jsp";
	
	afso.handlePublishing(request);
	
	Simulation simulation = new Simulation();	
	
	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>USIP Open Simulation Platform</title>


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
              <h1>Publish Simulation</h1>
	  <blockquote>
	    <% 
			if (afso.sim_id != null) {
		%>
	    <p>Publishing simulation <strong><%= simulation.getDisplayName() %></strong>.<br>
	      (If you would like to look at a different simulation, <a href="select_simulation.jsp">click 
	        here</a>.)</p>
      </blockquote>
	  <blockquote><hr />
	    <form name="form1" id="form1" method="post" action="publish_sim.jsp">
	      
	      <table width="80%" border="0" cellspacing="2" cellpadding="1">
	        <tr valign="top"> 
	          <td>Simulation Name</td>
                  
              <td><%= simulation.getDisplayName() %></td>
                </tr>
	        
	        <tr valign="top"> 
	          <td>Simulation Key Words (?) </td>
                  
              <td> 
                <textarea name="sim_key_words"><%= simulation.getListingKeyWords() %></textarea></td>
                </tr>
			<tr>
	          <td>Published Internally (?) </td>
	          <td>&nbsp;</td>
	          </tr>
			  
	        <tr>
	          <td>Published Publicly <a href="helptext/publish_publicly_help.jsp" target="helpinright">(?) </a></td>
	          <td>
			  
			  <input type="checkbox" name="publish_publicly" id="publish_publicly"  <%= USIP_OSP_Util.matchSelected( simulation.isExternallyPublished() , "value=\"true\"") %> />			  </td>
	          </tr>

	        <tr>
	          <td>Release Date (?) </td>
	          <td><label>
	            <input type="text" name="textfield" />
	          </label></td>
	          </tr>
	        <tr>
	          <td>Allow Player Auto-Registration <a href="helptext/auto_registration_help.jsp" target="helpinright">(?)</a></td>
                  <td><label>
                    <input type="checkbox" name="auto_registration" id="auto_registration"  value="true"/>
                    </label></td>
                </tr>
	        <tr> 
	          <td>&nbsp;</td>
                  <td>
                    <input type="hidden" name="sending_page" value="publish_sim">
                    <input name="command" type="submit" id="submit_p" value="Update" />                    </td>
                </tr>
	        </table>
		    </form>
        </blockquote>
	  <% } else { // End of if have set simulation id. %>
      <blockquote>
        <%@ include file="select_message.jsp" %></p>
      </blockquote>
      <p>
        <% } // End of if have not set simulation for edits. %>
      </p>
      <hr>
      <p>Below are listed all current simulations and their publication state. </p>
      <table width="100%" border="1" cellspacing="0" cellpadding="2">
        <tr valign="top"> 
          <td width="15%"><strong>Name / Version</strong></td>
            <td width="16%"><strong>Status</strong></td>
            <td width="16%"><strong>Author</strong></td>
            <td width="16%"><strong>Keywords</strong></td>
            <td width="16%"><strong>Date Published</strong></td>
          </tr>
        <% 
		  
		  for (ListIterator li = Simulation.getAll(afso.schema).listIterator(); li.hasNext();) {
			Simulation sim = (Simulation) li.next();
			
			String ready = "Under Development";
			
			if (sim.isInternallyPublished()){
				ready = "Available for Use";
				
				if (sim.isExternallyPublished()) {
					ready = "Externally Listed";
				}
			}
			%>
        <tr valign="top"> 
          <td><%= sim.getSimulationName() %> : <%= sim.getVersion() %></td>
            <td><%= ready %></td>
            <td><%= sim.getCreator() %></td>
            <td><%= sim.getListingKeyWords() %></td>
            <td><%= sim.getPublishDate() %></td>
          </tr>
        <% } %>
      </table>
      <p>&nbsp;</p>
      <blockquote>
        <p align="center">&nbsp;</p>
		  </blockquote>
      </blockquote>

      <a href="review_sim.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
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