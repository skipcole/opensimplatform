<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,org.usip.osp.communications.*" 
	errorPage="/error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));

	SharedDocument sd = afso.handleMakeNotifications(request);

	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Open Simulation Platform Control Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<script language="JavaScript" type="text/javascript" src="../help-bubble.js">
</script>
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td><table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
              <tr>
                <td width="120"><img src="../Templates/images/white_block_120.png" /></td>
                <td width="100%"><br />
                  <h1>Set Notifications for Document</h1>
                  <br />
                  
                  <blockquote>
                  <p>You must first set the document before you can set popup notifications to be sent to the players when this memo is submitted. </p>
                      <form id="form1" name="form1" method="post" action="">
                        <label for="select">Document</label>
                        <select name="sd_id" id="sd_id">
                        <%
							String selectedNone = "";
							
							if (sd.getId() == null) {
								selectedNone =  "selected=\"selected\"";
							}
						%>
                        <option value="0" <%= selectedNone %>>None</option>
                        <%
						
						for (ListIterator li = SharedDocument.getAllBaseDocumentsForSim(afso.schema, afso.sim_id).listIterator(); li.hasNext();) {
						SharedDocument sd_l = (SharedDocument) li.next(); 
						
						String selected = "";
						if ((sd.getId() != null) && (sd_l.getId().intValue() == sd.getId().intValue())){
							selected = "selected=\"selected\"";
						}
						%>
                          <option value="<%= sd_l.getId() %>" <%= selected %>><%= sd_l.getUniqueDocTitle() %></option>
                        <% } %>
                        </select>
                        <input type="submit" name="button" id="button" value="Submit" />
                      </form>
                      
                  <%
						if (sd.getId() != null) { 
					%>
                      <p>When the document <%= sd.getUniqueDocTitle() %> is submitted, the following actors will receive notification: </p>
                      <blockquote>
                      <table width="80%" border="0" cellspacing="0">
                <%
                      for (ListIterator la = SimActorAssignment.getActorsForSim(afso.schema, afso.sim_id).listIterator(); la.hasNext();) {
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
                <form action="make_create_document_notifications_page.jsp" method="post" name="form2" id="form2">
                <input type="hidden" name="sd_id" value="<%= sd.getId() %>" />
                <input type="hidden" name="actor_being_worked_on_id" value="<%= act.getId() %>" />
                        <tr>
                          <td colspan="4"><%= act.getActorName() %> </td>
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
                  
                  <a href="add_objects.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a> </td>
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
