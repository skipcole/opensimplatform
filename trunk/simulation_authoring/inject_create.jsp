<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.communications.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	String select_recipients = (String) request.getParameter("select_recipients");
	System.out.println("select_recipients was: " + select_recipients);
	
	String sending_page = (String) request.getParameter("sending_page");
	
	Inject inj = new Inject();
	
	if (sending_page != null) {
	
		if ((sending_page.equalsIgnoreCase("create_ind_inject")) || (sending_page.equalsIgnoreCase("view")) ){
			inj = afso.handleCreateInject(afso.schema, request);
		} else if (sending_page.equalsIgnoreCase("injects")) {
			String ig_id = (String) request.getParameter("ig_id");
			inj.setGroup_id(new Long(ig_id));
		}
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Create Inject Group Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>

<style type="text/css">
<!--
.style1 {color: #FF0000}
-->
</style>

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
			<td width="100%"><br />
              <h1>Create Inject</h1>
              <br />
      <blockquote> 
        <% 
			if (afso.sim_id != null) {
		%>
        <p>          </p>
          <form id="form2" name="form2" method="post" action="inject_create.jsp">
            <input type="hidden" name="inj_id" value="<%= inj.getId() %>" />
            <input type="hidden" name="sending_page" value="create_ind_inject" />
            <table width="100%" border="1" cellspacing="0" cellpadding="4">
              <tr>
                <td valign="top">Inject Name:</td>
              <td valign="top">
                <label>
                  <input type="text" name="inject_name" id="inject_name" value="<%= inj.getInject_name() %>"/>
                  </label>            </td>
            </tr>
              <tr>
                <td valign="top">Inject Group:</td>
                <td valign="top">
                <select name="ig_id" id="select">
                       <%
					boolean foundIg = false;
					String createInjectButtonDisabled = "disabled=\"disabled\"";
					
					for (ListIterator li = InjectGroup.getAllForSim(afso.schema, afso.sim_id).listIterator(); li.hasNext();) {
						InjectGroup ig = (InjectGroup) li.next();
						
						String selected = "";
						
						if (inj.getGroup_id() != null) {
							if (inj.getGroup_id().intValue() == ig.getId().intValue()) {
								selected = " selected ";
							}
						}
					%>
                        <option value="<%= ig.getId() %>" <%= selected %>><%= ig.getName() %></option>
                       <% } %>
                      </select></td>
              </tr>
              <tr>
                <td valign="top">Inject Text:</td>
              <td valign="top">
                <textarea id="inject_text" name="inject_text" cols="45" rows="5"><%= inj.getInject_text() %></textarea>
                <script language="javascript1.2">
  			generate_wysiwyg('inject_text');
		</script>                </td>
            </tr>
              
              <tr>
                <td valign="top">Inject Notes <a href="helptext/inject_notes.jsp" target="helpinright">(?)</a>:<br /></td>
              <td valign="top"><label>
                <textarea name="inject_notes" id="inject_notes" cols="45" rows="5"><%= inj.getInject_Notes() %></textarea>
                </label></td>
            </tr>
              <tr>
                <td valign="top">Inject Target(s) (?):</td>
                <td valign="top">
                <%
					List actorL = Actor.getAll(afso.schema);
					///////////////////////////////////////////////////////////////////
					Simulation simulation = new Simulation();
					if (afso.sim_id != null) {
						simulation = afso.giveMeSim();
						actorL = Actor.getAllForSimulation(afso.schema, afso.sim_id);
					}
					
					String allSelected = "checked=\"checked\"";
					
					List targets = new ArrayList();
					List targetRaw = InjectActorAssignments.getAllForInject(afso.schema, inj.getId());
					
					for (ListIterator liInjId = targetRaw.listIterator(); liInjId.hasNext();) {
						InjectActorAssignments targ = (InjectActorAssignments) liInjId.next();
						targets.add(targ.getActor_id());	
					}
					
					System.out.println("number targets: " + targets.size());
					
					if ((inj.getId() != null) && ( targets.size() > 0 )) {
						allSelected = "";
					}
				%>
                <table width="100%"><tr><td valign="top">
                <input name="everyone_checkbox" type="checkbox" id="everyone_checkbox" <%= allSelected %> />
                <label for="everyone_checkbox"> Everyone </label>or 
                </td><td valign="top">
                  <% 
				  int ii = 0;
				  for (ListIterator li = actorL.listIterator(); li.hasNext();) {
					Actor act = (Actor) li.next();
					ii += 1;
					
					String thisActorIsTargetted = "";
					
					System.out.println("actor id: " + act.getId());
					if (targets.contains(act.getId())){
						thisActorIsTargetted= "checked=\"checked\"";
					}
				  %>
				  <label for="everyone_<%= act.getId().toString() %>">
                  <input type="checkbox" name="target_<%= act.getId().toString() %>" id="target_<%= act.getId().toString() %>" <%= thisActorIsTargetted %> /> 
				  <%= act.getActorName() %></label><br />
          		<% } %>
                </td></tr></table>
                  </td>
              </tr>
              <tr>
                <td valign="top">&nbsp;</td>
              <td valign="top">              
                <%
				if (inj.getId() == null) {
				%>
                <input type="submit" name="command" value="Create" />
                <%
				} else {
				%>
				<table width="100%"><tr>
                <td align="left"><input type="submit"  name="command" value="Update Inject" /></td>
				<td align="right"><input type="submit" name="clear_button" value="Clear to Create New Inject" /></td>
				</tr></table>
                <%
					}
				%></td>
            </tr>
              </table>
          </form>
          <p>&nbsp;</p>
      </blockquote>
      <p align="center">&nbsp;</p>
      <% } else { // End of if have set simulation id. %>
      <blockquote>
        <p>
          <%@ include file="select_message.jsp" %></p>
      </blockquote>
      <% } // End of if have not set simulation for edits. %>      <a href="injects.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
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
