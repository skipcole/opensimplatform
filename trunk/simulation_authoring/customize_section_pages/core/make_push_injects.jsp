<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,org.usip.osp.communications.*" 
	errorPage="/error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	CustomizeableSection cs = afso.handleMakePushInjectsPage(request);
	
	if (afso.forward_on){
		afso.forward_on = false;
		response.sendRedirect(afso.backPage);
		return;
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
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
                  <h1>Push Injects Page</h1>
                  <br />
                  <form action="make_push_injects.jsp" method="post" name="form2" id="form2">
                    <blockquote>
                      <p>To allow access to a player to push any of a set of injects.  To create a new set of injects associated with this simulation <a href="../../injects.jsp">click here</a>. </p>
                      <p>Tab Heading:
                        <input type="text" name="tab_heading" value="<%= afso.getMyPSO_SectionMgmt().get_tab_heading() %>"/>
                      </p>
                      <table width="100%" border="1">
                        <tr>
                          <td valign="top"><strong>Current Groups (?) </strong></td>
                          <td valign="top">
		<%
			List setOfInjectGroups = InjectGroup.getSetOfInjectGroupsForSection(afso.schema, cs.getId());
		
				for (ListIterator<InjectGroup> li = setOfInjectGroups.listIterator(); li.hasNext();) {

					InjectGroup ig = (InjectGroup) li.next();		
						  %>
						  <p><%= ig.getName() %></p>
						  <% } %>
                            <p> 
                              <input type="submit" name="remove_inject_group" id="remove_inject_group" value="Remove All"  onclick="return confirm('Are you sure you want to remove all inject groups?');" />
                            </p></td>
                        </tr>
                        <tr>
                          <td width="36%" valign="top"><strong>Add Inject Group (?) </strong></td>
                          <td width="64%" valign="top"><select name="new_ig_id" size="1">
                            <option value="0">None Selected</option>
                              <%
					for (ListIterator li = InjectGroup.getAllForSim(afso.schema, afso.sim_id).listIterator(); li.hasNext();) {
			InjectGroup ig = (InjectGroup) li.next();
				%>
                              <option value="<%= ig.getId() %>" ><%= ig.getName() %></option>
                              <%
					}
				%>
                            </select>

                          <input type="submit" name="add_inject_group" id="button" value="Add Inject Group" /></td>
                        </tr>
						<tr>
                          <td valign="top"><strong>Can Edit the Injects (?) </strong></td>
                          <td valign="top">
						  <% 
						  String player_editable = "";
						  
						  	if (cs.getContents() != null) {
								String can_edit = (String) cs.getContents().get(InjectGroup.PLAYER_CAN_EDIT);
								if (can_edit != null) {
									if (can_edit.equalsIgnoreCase("true")){
										player_editable = " checked=\"checked\" " ;
									}
								}
							}
						  
						  %>
						  <input name="injects_editable" type="checkbox" value="true" <%= player_editable %>  />
                          <label></label></td>
                        </tr>
                      </table>
                      <p>Enter the introductory text that will appear on this page.
                        <textarea id="make_push_injects_page_text" name="make_push_injects_page_text" style="height: 710px; width: 710px;"><%= cs.getBigString() %></textarea>
                        <script language="javascript1.2">
  			generate_wysiwyg('make_push_injects_page_text');
		</script>
                      </p>
                      <p>
                        <input type="hidden" name="custom_page" value="<%= afso.getMyPSO_SectionMgmt().get_custom_section_id() %>" />
                        <input type="hidden" name="sending_page" value="make_push_injects_page" />
                        <input type="submit" name="save_page" value="Save" />
                      </p>
                      <p>
                        <input type="submit" name="save_and_add" value="Save and Add Section" />
                      </p>
                      <p>&nbsp;</p>
                    </blockquote>
                  </form>
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