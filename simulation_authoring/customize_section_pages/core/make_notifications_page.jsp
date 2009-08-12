<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,org.usip.osp.communications.*" 
	errorPage="" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true), true);

	String cs_id = (String)  request.getParameter("cs_id");
	
	SharedDocument sd = BaseSimSectionDepObjectAssignment.getSharedDocumentForSection(afso.schema, cs_id);
	
	if (afso.forward_on){
		afso.forward_on = false;
		response.sendRedirect(afso.backPage);
		return;
	}
	
	Simulation sim = new Simulation();
	
	if (afso.sim_id != null){
		sim = afso.giveMeSim();
	}
	
	///////////////////////////////////////////////////
	// This will move into psoHandleMakeNotifications
	String sending_page = (String) request.getParameter("sending_page");
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("make_notifications_page"))){
		
		String sdanao = (String) request.getParameter("sdanao");
		String actor_id = (String) request.getParameter("actor_id");
		String sdanao_text = (String) request.getParameter("sdanao_text");
		
		System.out.println(sdanao);
		if (sdanao.equalsIgnoreCase("create_null")){
			
			System.out.println("actor_id" + actor_id);
			
			Long from_actor_id = null;
			Long from_phase_id = null;
			
			SharedDocActorNotificAssignObj sdanao_new = new SharedDocActorNotificAssignObj(afso.schema, afso.sim_id, sd.getId(), new Long(actor_id), 
			from_actor_id, from_phase_id, sdanao_text);
		} else if (sdanao.startsWith("remove_")){
			
			sdanao = sdanao.replaceAll("remove_", "");
			
			System.out.println("removing " + sdanao);
			SharedDocActorNotificAssignObj.removeSdanao(afso.schema, sdanao);

		}
	}
	///////////////////////////////////////////////////
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Open Simulation Platform Control Page</title>
<script language="JavaScript" type="text/javascript" src="../../../wysiwyg_files/wysiwyg.js">
</script>
<link href="../../../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td><table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
              <tr>
                <td width="120"><img src="../../../Templates/images/white_block_120.png" /></td>
                <td width="100%"><br />
                  <h1>Set Notifications for this Document</h1>
                  <br />
                  
                  <blockquote>
                    <%
						if (sd == null) { 
					%>
                      <p>You must first set the document before you can set popup notifications to be sent to the players when this memo is submitted. </p>
                    <%
						} else {
					%>
                      <p>When the document x is submitted, the following actors will receive notification: </p>
                      <blockquote>
                      <table width="80%" border="0" cellspacing="0">
                <%
                      for (ListIterator la = sim.getActors(afso.schema).listIterator(); la.hasNext();) {
						Actor act = (Actor) la.next();
						
						// Get the SDANAO object for this actor and this document
						
						SharedDocActorNotificAssignObj sdanao = 
							SharedDocActorNotificAssignObj.getAssignmentForDocumentAndActor(afso.schema, sd.getId(), act.getId());
						
						String noneSelected = "";
						String hasSdanao = "";
						String sdanao_id = "null";
						String sdanao_text = "";
						
						if (sdanao == null){
							noneSelected = " checked ";
						} else {
							sdanao_id = sdanao.getId().toString();
							sdanao_text = sdanao.getNotificationText();
							hasSdanao = " checked ";
						}
						
						// Need to get id to set the 'none' button to indicate we want to delete the SDANAO object if it exists.
						
						
                %>
                <form action="make_notifications_page.jsp" method="post" name="form2" id="form2">
                <input type="hidden" name="cs_id" value="<%= cs_id %>" />
                <input type="hidden" name="actor_id" value="<%= act.getId() %>" />
                        <tr>
                          <td colspan="4"><%= act.getName() %> </td>
                          </tr>
                        <tr>
                          <td width="8%" valign="top">&nbsp;</td>
                          <td width="10%" valign="top"><input name="sdanao" type="radio" value="remove_<%= sdanao_id %>" <%= noneSelected %> />
None</td>
                          <td width="52%" valign="top"><input type="radio" name="sdanao" value="create_<%= sdanao_id %>" align="top" <%= hasSdanao %> />
Text:: 
  <textarea name="sdanao_text" id="textarea" cols="30" rows="2"><%= sdanao_text %></textarea></td>
                          <td width="30%" valign="top"><input type="submit" name="save_page" value="Save" />
                            <input type="hidden" name="sending_page" value="make_notifications_page" /></td>
                        </tr>
                        </form>
                        <% } %>
                      </table>
                      </blockquote>
                      <% } // End of if shared document not null %>
                    <p>&nbsp;</p>
                    </blockquote>
                  
                  <a href="<%= afso.backPage %>"><img src="../../../Templates/images/back.gif" alt="Back" border="0"/></a> </td>
              </tr>
            </table></td>
        </tr>
        <tr>
          <td><p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
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
