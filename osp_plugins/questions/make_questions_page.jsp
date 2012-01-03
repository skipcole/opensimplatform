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
                  <h1>Customize Questions Page</h1>
                  <p>This creates a table of questions that can be presented to the student. They may save their answers, but when they hit submit, the questions can no longer be edited, and they see any information that the instructor has left for them.</p>
                  <p><br />
                  </p>
                  <form action="../questions/make_questions_page.jsp" method="post" name="form2" id="form2">
                    <% if (cs.getId() != null) {
	   %>
                    <input type="hidden" name="cs_id" value="<%= cs.getId() %>" />
                    <% } %>
                    <blockquote>
                      <p><strong>Default Tab Heading</strong> <a href="../../simulation_authoring/helptext/default_tab_heading_for_section.jsp" target="helpinright">(?)</a>:
                        <input type="text" name="tab_heading" value="<%= afso.getMyPSO_SectionMgmt().get_tab_heading() %>"/>
                      </p>
                    </blockquote>
                    <blockquote>
                      <table width="100%" border="1" cellspacing="0">
                        <tr>
                          <td valign="top">Page Title </td>
                          <td valign="top"><label>
                              <input type="text" name="<%= QuestionCustomizer.KEY_FOR_PAGETITLE %>" value="<%=  QuestionCustomizer.getPageStringValue(cs, QuestionCustomizer.KEY_FOR_PAGETITLE) %>" />
                            </label></td>
                        </tr>
                        <tr>
                          <td valign="top">Page Introduction</td>
                          <td valign="top"><label>
                              <textarea name="cs_bigstring" id="textarea" cols="45" rows="5"><%= cs.getBigString() %></textarea>
                            </label></td>
                        </tr>
                      </table>
                      <p><strong>Questions to Include</strong> (<a href="create_questions_page.jsp">Click here</a> to create a new question.)<br />
                      </p>
                      <table width="100%" border="1" cellspacing="0">
                        <tr>
                          <td width="34%"><strong>Include</strong></td>
                          <td width="31%"><strong>Question Tag</strong></td>
                          <td width="35%"><strong>Position</strong></td>
                        </tr>
                        <%
	List <QuestionAndResponse> qAndRList = QuestionAndResponse.getAllForSim(afso.schema, afso.sim_id);
	
		Hashtable currentQuestions = QuestionCustomizer.getMyQuestions(afso.schema, cs.getId(), "com.seachangesimulations.osp.questions");
	
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
                      <p>&nbsp;</p>
                      <table width="100%" border="1" cellspacing="0">
                        <tr>
                          <td valign="top">Allow Phase Change</td>
                          <td valign="top">
 <%
 
 String phase_change_yes = "";
 String phase_change_no = "checked=\"checked\"";
 
	String allowingPhaseChange = QuestionCustomizer.getPageStringValue(cs, QuestionCustomizer.KEY_FOR_PHASE_CHANGE); 
	
	if (allowingPhaseChange.equalsIgnoreCase("yes")) {
		phase_change_yes = "checked=\"checked\"";
 		phase_change_no = "";
	}
	
	
 %>
                          <input type="radio" name="<%= QuestionCustomizer.KEY_FOR_PHASE_CHANGE %>" id="<%= QuestionCustomizer.KEY_FOR_PHASE_CHANGE %>_no" value="yes" <%= phase_change_yes %> />
                            Yes /
                              <input name="<%= QuestionCustomizer.KEY_FOR_PHASE_CHANGE %>_no" type="radio" id="radio2" value="no" <%= phase_change_no %> />
                              No</td>
                        </tr>
                        <tr>
                          <td valign="top">Phase to go to</td>
                          <td valign="top">
                          <%
						  	Long selectedPhase = QuestionCustomizer.getPhaseId(afso.schema, cs.getId());
							
							if (selectedPhase == null) {
								selectedPhase = new Long(0);
							}
						  %>
                          <select name="<%= QuestionCustomizer.KEY_FOR_PHASE_ID %>">
                              <% 
						
						for (ListIterator li = SimPhaseAssignment.getPhasesForSim(afso.schema, afso.sim_id).listIterator(); li.hasNext();) {
							SimulationPhase sp = (SimulationPhase) li.next();
							
							String selected_p = "";
							
							if (sp.getId().intValue() == selectedPhase.intValue()) {
								selected_p = "selected";
							}	
				%>
                              <option value="<%= sp.getId().toString() %>" <%= selected_p %>><%= sp.getPhaseName() %></option>
                              <% } %>
                            </select></td>
                        </tr>
                        <tr>
                          <td valign="top">Post Answer Text</td>
                          <td valign="top"><textarea name="<%= QuestionCustomizer.KEY_FOR_POSTANSWERTEXT %>" id="post_answer_text" cols="45" rows="5"><%=  QuestionCustomizer.getPageStringValue(cs, QuestionCustomizer.KEY_FOR_POSTANSWERTEXT) %></textarea></td>
                        </tr>
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
                        <input type="hidden" name="sending_page" value="make_cast_page" />
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
