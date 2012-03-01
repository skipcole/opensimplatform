<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.baseobjects.*,
	com.seachangesimulations.osp.questions.*" 
	errorPage="/error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	CustomizeableSection cs = afso.handleCustomizeSection(request);
	
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
<script language="JavaScript" type="text/javascript" src="../../wysiwyg_files/wysiwyg.js">
</script>
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td><table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
              <tr>
                <td width="120"><img src="../../Templates/images/white_block_120.png" /></td>
                <td width="100%"><br />
                  <h1>Customize Review Answers Page</h1>
                  <p>This creates a page to allow users to see the answers to questions they have previously answered.</p>
                  <p>Allows one to select which sets of questions and answers will be seen on this page.</p>
                  <p><br />
                  </p>
                  <form action="../questions/make_questions_view_page.jsp" method="post" name="form2" id="form2">
                  <input type="hidden" name="sending_page" value="make_review_answers_page" />
                    <% if (cs.getId() != null) {
	   %>
                    <input type="hidden" name="cs_id" value="<%= cs.getId() %>" />
                    <% } %>
                    <blockquote>
                      <p>Default Tab Heading <a href="../../simulation_authoring/helptext/default_tab_heading_for_section.jsp" target="helpinright">(?)</a>:
                        <input type="text" name="tab_heading" value="<%= afso.getMyPSO_SectionMgmt().get_tab_heading() %>"/>
                      </p>
                    </blockquote>
                    <blockquote>
                      <table width="100%" border="1" cellspacing="0">
                        <tr>
                          <td valign="top"><strong>Page Title </strong></td>
                          <td valign="top"><label>
                              <input type="text" name="<%= QuestionCustomizer.KEY_FOR_PAGETITLE %>" value="<%=  QuestionCustomizer.getPageStringValue(cs, QuestionCustomizer.KEY_FOR_PAGETITLE) %>" />
                            </label></td>
                        </tr>
                        <tr>
                          <td valign="top"><strong>Page Introduction</strong></td>
                          <td valign="top"><label>
                              <textarea name="cs_bigstring" id="textarea" cols="45" rows="5"><%= cs.getBigString() %></textarea>
                            </label></td>
                        </tr>
                      </table>
                      <p>&nbsp;</p>
                      <p><strong>Questions to Include</strong><br />
                      </p>
                      <table width="100%" border="1" cellspacing="0">
                        <tr>
                          <td width="34%"><strong>Include</strong></td>
                          <td width="31%"><strong>Question Tag</strong></td>
                          <td width="35%"><strong>Position</strong></td>
                        </tr>
                        <%
	List <QuestionAndResponse> qAndRList = QuestionAndResponse.getAllForSim(afso.schema, afso.sim_id);
	
		Hashtable currentQuestions = QuestionCustomizer.getMyQuestions(afso.schema, cs.getId(), QuestionAndResponse.class.toString());
	
		for (ListIterator li = qAndRList.listIterator(); li.hasNext();) {
			QuestionAndResponse this_qar = (QuestionAndResponse) li.next();
			
			String checked = "";
			String position = (String) currentQuestions.get(this_qar.getId());
			
			if (position != null) {
				checked = "checked=\"checked\"";	
			} else {
				position = "";
			}
			
			
%>
                        <tr>
                          <td valign="top"><input type="checkbox" name="question_<%= this_qar.getId() %>" id="checkbox" <%= checked %> />
                          <label for="checkbox"></label></td>
                          <td valign="top"><%= this_qar.getQuestionIdentifier() %></td>
                          <td valign="top">
                          <input name="position_<%= this_qar.getId() %>" type="text" id="position_<%= this_qar.getId() %>" size="4" maxlength="4" value="<%= position %>" /></td>
                        </tr>
                        <%

	}

%>
                      </table>
                      <p><strong>Modify Section Description</strong></p>
                      <blockquote>
                        <p>
                          <textarea name="cs_description" cols="80" rows="3" id="cs_description"><%= cs.getDescription() %></textarea>
                      </blockquote>
                      <%
				
			boolean hasItAlready = SimulationSectionAssignment.determineIfActorHasThisSectionAtThisPhase(afso.schema, 
				afso.sim_id, afso.actor_being_worked_on_id, afso.phase_id, cs.getId());
			
			String actors_name_string = "fill it in from cache";
			
			if (afso.actor_being_worked_on_id.equals(new Long(0))) {
				actors_name_string = " every actor ";
			} else {
				actors_name_string = USIP_OSP_Cache.getActorName(afso.schema, afso.sim_id, new Long(0), request, afso.actor_being_worked_on_id);
			}
			%>
                      <% if (!(hasItAlready)) { %>
                      <p>
                        <input type="submit" name="save_and_add" value="Save and Add Section" />
                        Add this to <%= actors_name_string %> in phase <%= USIP_OSP_Cache.getPhaseNameById(request, afso.schema, afso.phase_id) %> </p>
                      <% } else { %>
                      <p>
                        <input type="submit" name="save_page" value="Save" />
                      This section has already been added to <%= actors_name_string %> for phase <%= USIP_OSP_Cache.getPhaseNameById(request, afso.schema, afso.phase_id) %>.</p>
                      <% } %>
                      <p>
                        <input type="hidden" name="custom_page" value="<%= afso.getMyPSO_SectionMgmt().get_custom_section_id() %>" />
                        <input type="hidden" name="save_results" value="true" />
                        
                      </p>
                      <p>&nbsp;</p>
                    </blockquote>
                  </form>
                  <a href="<%= afso.backPage %>"><img src="../../Templates/images/back.gif" alt="Back" border="0"/></a></td>
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