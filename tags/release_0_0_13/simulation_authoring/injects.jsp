<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.communications.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	afso.backPage = "injects.jsp";
	
	Simulation simulation = new Simulation();	
	
	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Add Special Features Page</title>


<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" width="100%" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%">
            <blockquote> 
              <h1>Create Injects</h1>
              <p>Injects are events that can happen to the players. For example, they may receive news that the people in their town are having a protest parade, or they may receive an important memo from their headquarters. Injects are arranged into groups, and before creating an actual inject, you must first create a group to contain it.</p>
              <% 
			if (afso.sim_id != null) {
		%>
              <blockquote>
                <p>
                  Creating injects for the simulation <strong><%= simulation.getDisplayName() %></strong>.<br />
                  (If you would like to work on a different simulation, <a href="select_simulation.jsp">click 
                    here</a>.)           </p>
              </blockquote>
              <p>&nbsp;</p>
           <table width="100%" border="0" cellspacing="0" cellpadding="0">
             <tr>
               <td width="8%" valign="top">1.</td>
               <td width="92%" valign="top"><form id="form3" name="form3" method="post" action="inject_group.jsp">
                       <input type="submit" name="button3" id="button3" value="Create or Edit Groups" />

                     <p>&nbsp;    </p>
               </form>                 </td>
             </tr>
             <tr>
               <td valign="top">2.</td>
               <td valign="top"><form id="form1" name="form1" method="post" action="inject_create.jsp">
                    <input type="hidden" name="sending_page" value="injects" />
                     <select name="ig_id" id="select">
                       <%
					boolean foundIg = false;
					String createInjectButtonDisabled = "disabled=\"disabled\"";
					
					for (ListIterator li = InjectGroup.getAllForSim(afso.schema, afso.sim_id).listIterator(); li.hasNext();) {
						InjectGroup ig = (InjectGroup) li.next();
						foundIg = true;
					%>
                        <option value="<%= ig.getId() %>"><%= ig.getName() %></option>
                       <% } %>
                      </select>
                     <% if (foundIg == true) {
				 	createInjectButtonDisabled = "";
					}
				%>
                     <input type="submit" name="button" id="button" value="Create Inject in Group" <%= createInjectButtonDisabled %>  />
                  <p>&nbsp; </p>
               </form>                 </td>
             </tr>
             <tr>
               <td valign="top">3.</td>
               <td valign="top">
                 <form id="form2" name="form2" method="post" action="inject_view.jsp">
                       <input type="submit" name="button2" id="button2" value="View all Injects for Simulation" />
                     <p>&nbsp;    </p>
                 </form>                 </td>
             </tr>
           </table>
             <p>
             
               
           <%
			for (ListIterator li = InjectGroup.getAllForSim(afso.schema, afso.sim_id).listIterator(); li.hasNext();) {
			InjectGroup ig = (InjectGroup) li.next();
		%>
            <% 
		  List injectList = Inject.getAllForSimAndGroup(afso.schema, afso.sim_id, ig.getId());
		  
		   if (injectList.size() > 0) { 
		  	for (ListIterator lii = injectList.listIterator(); lii.hasNext();) {
			Inject da_inject = (Inject) lii.next();
		  
		  %>
            <% }  // End of loop.  
		  } else { %>
            <% } %>
            <p>&nbsp;</p>
          <% } // end of loop over inject groups %>
            </table>
          <p> 
            <!-- jsp:include page="snippet.jsp" flush="true" -->
            </p>
          <p>Current Injects in this Group</p>
          <p>&nbsp;</p>
          <p>&nbsp;</p>
          <p align="center"><a href="add_objects.jsp">Next Step: Add Objects</a><a href="set_universal_sim_sections.jsp?actor_index=0"></a></p>
      <% } else { // End of if have set simulation id. %>
      <blockquote>
        <p>
          <%@ include file="select_message.jsp" %></p>
      </blockquote>
      <% } // End of if have not set simulation for edits. %>
      
            <a href="assign_actor_to_simulation.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>.</p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>
<%
	
%>
