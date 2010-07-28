<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.communications.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.baseobjects.core.*" 
	errorPage="" %>
<% 

	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	CustomizeableSection cs = afso.handleCustomizeSection(request);
	
	if (afso.forward_on){
		afso.forward_on = false;
		response.sendRedirect(afso.backPage);
		return;
	}
	
	TipsCustomizer tc = new TipsCustomizer(request, afso, cs);
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>
<script language="JavaScript" type="text/javascript" src="../../../wysiwyg_files/wysiwyg.js">
</script>


<link href="../../../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../../../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Make Tips Page </h1>
              <br />
      <blockquote> 
        This page will display tips left by the simulation author, previous instructors, or both. <br>
      </blockquote>
      <form action="make_tips_page.jsp" method="post" name="form2" id="form2">
        <input type="hidden" name="cs_id" value="<%= cs.getId() %>" />
        <blockquote>
          Tab Heading: 
            <input type="text" name="tab_heading" value="<%= afso.getMyPSO_SectionMgmt().get_tab_heading() %>"/>
          
          <p>Enter the Tip Text Below </p>
          <blockquote>
            <p>
              <textarea id="tip_page_text" name="tip_page_text" style="height: 120px; width: 480px;"><%= tc.getTip().getTipText() %></textarea>
            </p>
            <p>
              <script language="javascript1.2">
				wysiwygWidth = 480;
				wysiwygHeight = 120;
  			generate_wysiwyg('text_page_text');
		    </script>
            </p>
            </blockquote>
            <p>Player will be able to leave tips for other's to see?            </p>
            <blockquote>
              <p>
                <label>
                  <input type="radio" name="<%= TipsCustomizer.KEY_FOR_CAN_LEAVE_TIPS %>" id="can_leave_tips_yes" value="true" 
			<%= Customizer.storedValueTrueToChecked(cs, TipsCustomizer.KEY_FOR_CAN_LEAVE_TIPS, true) %> /> 
                  Yes </label>
                /
                <label>
                  <input type="radio" name="<%= TipsCustomizer.KEY_FOR_CAN_LEAVE_TIPS %>" id="can_leave_tips_no" value="false" <%= Customizer.storedValueTrueToChecked(cs, TipsCustomizer.KEY_FOR_CAN_LEAVE_TIPS, false) %> />
                  No </label>
              </p>
              <p> 
                <input type="hidden" name="custom_page" value="<%= afso.getMyPSO_SectionMgmt().get_custom_section_id() %>" />
                <input type="hidden" name="universal" value="<%= afso.getMyPSO_SectionMgmt().get_universal() %>">
                <input type="hidden" name="sending_page" value="make_text_page" />
                <input type="hidden" name="save_results" value="true" />
                <input type="submit" name="save_page" value="Save" />
                <input type="submit" name="save_and_add" value="Save and Add Section" />
                  </p>
              <p>.</p>
            </blockquote>
        </blockquote>
      </form>      
      <blockquote>
        <p><a href="<%= afso.backPage %>"><img src="../../../Templates/images/back.gif" alt="Back" border="0"/></a>			</p>
      </blockquote></td>
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
