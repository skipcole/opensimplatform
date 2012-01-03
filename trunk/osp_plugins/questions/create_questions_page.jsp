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
	
	if (afso.forward_on){
		afso.forward_on = false;
		response.sendRedirect(afso.backPage);
		return;
	}
	
		QuestionAndResponse this_qar = QuestionCustomizer.handleCreateQuestion(request, afso);
	
	
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
                  <h1>Create Questions Page</h1>
                  <p>Here you can create questions that can be presented to the student. <br />
                  </p>
                  <form action="../questions/create_questions_page.jsp" method="post" name="form2" id="form2">
                    <input type="hidden" name="sending_page" value="create_question" />
                    <blockquote>
                      <p><br />
                      </p>
                    </blockquote>
                    <blockquote>
                      <table width="100%" border="1" cellspacing="0">
                        <tr>
                          <td valign="top">Question #</td>
                          <td valign="top"><input type="text" name="qtag" id="qtag" value="<%= this_qar.getQuestionIdentifier() %>" /></td>
                        </tr>
                        <tr>
                          <td valign="top">Question</td>
                          <td valign="top"><label for="question"></label>
                            <textarea name="question" id="question" cols="45" rows="5"><%= this_qar.getQuestion() %></textarea></td>
                        </tr>
                        <tr>
                          <td valign="top">Answer</td>
                          <td valign="top"><label for="answer"></label>
                            <textarea name="answer" id="answer" cols="45" rows="5"><%= this_qar.getAnswer() %></textarea>
                            <script language="javascript1.2">
								newRootDir = "../../wysiwyg_files/";
  			generate_wysiwyg('answer');
		</script></td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td><% if (this_qar.getId() == null) { %>
                            <input type="submit" name="create_question" value="Create" tabindex="6" />
                            <%
				} else {
				%>
                            <input type="hidden" name="question_id" value="<%= this_qar.getId() %>" tabindex="7" />
                            <input type="submit" name="clear_button" value="Clear" tabindex="6" />
                            <input type="submit"  name="update_question" value="Update Question" tabindex="8" />
                            <%
					}
				%></td>
                        </tr>
                      </table>
                      <blockquote>
                        <p>
                      </blockquote>
                      <p>
                        <input type="hidden" name="custom_page" value="<%= afso.getMyPSO_SectionMgmt().get_custom_section_id() %>" />
                        <input type="hidden" name="save_results" value="true" />
                        <input type="hidden" name="sending_page" value="create_questions_page" />
                      </p>
                    </blockquote>
                  </form>
                  <p>Below are listed all of the questions currently associated with this simulation. </p>
                  <table border="1" width="100%">
                    <tr>
                      <td><strong>Question #</strong></td>
                      <td><strong>Question</strong></td>
                    </tr>
                    <%
			  		int ii = 0;
	List <QuestionAndResponse> qAndRList = new QuestionAndResponse().getAllForSimulation(afso.schema, afso.sim_id);
	
		for (ListIterator li = qAndRList.listIterator(); li.hasNext();) {
			QuestionAndResponse this_qar2 = (QuestionAndResponse) li.next();
				
						
				%>
                    <tr>
                      <td><a href="create_questions_page.jsp?question_id=<%= this_qar2.getId() %>"><%= this_qar2.getQuestionIdentifier() %></a></td>
                      <td><%= this_qar2.getQuestion() %></td>
                    </tr>
                    <%
					}
				%>
                  </table>
                  <p></p>
                  <a href="make_questions_page.jsp"><img src="../../Templates/images/back.gif" alt="Back" border="0"/></a></td>
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