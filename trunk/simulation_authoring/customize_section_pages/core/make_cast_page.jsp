<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.core.*,
	org.usip.osp.baseobjects.*" 
	errorPage="" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	CustomizeableSection cs = afso.handleCustomizeSection(request);
	
	if (afso.forward_on){
		afso.forward_on = false;
		response.sendRedirect(afso.backPage);
		return;
	}
	
	String selected_display_control_yes = "";
	String selected_display_control_no = "";
	
	String stored_value = (String) cs.getContents().get(CastCustomizer.KEY_FOR_DISPLAY_CONTROL);
	
	if ((stored_value != null) && (stored_value.equalsIgnoreCase("true"))){
		selected_display_control_yes = "checked";
	} else {
		selected_display_control_no = "checked";
	}
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<title>Open Simulation Platform Control Page</title>
<script language="JavaScript" type="text/javascript" src="../../../wysiwyg_files/wysiwyg.js">
</script>



<link href="../../../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../../../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Customize Cast Page</h1>
              <br />
      <form action="make_cast_page.jsp" method="post" name="form2" id="form2">
        <% if (cs.getId() != null) {
	  	System.out.println("cs id was :" + cs.getId());
	   %>
        <input type="hidden" name="cs_id" value="<%= cs.getId() %>" />
        <% } %>
        <blockquote> 
          <p>Cast page will display control characters? 
            <label>
              <input type="radio" name="display_control" id="display_control_yes" value="true" <%= selected_display_control_yes %> />
              Yes</label>
            / 
            <input type="radio" name="display_control" id="display_control_no" value="false" <%= selected_display_control_no %> />
            No          </p>
        </blockquote>
        
        <blockquote>
          <p>Tab Heading: 
            <input type="text" name="tab_heading" value="<%= afso.getMyPSO_SectionMgmt().get_tab_heading() %>"/>
            </p>
            <table width="100%" border="1" cellspacing="0">
              <tr>
                <td valign="top">&nbsp;</td>
                <td valign="top">&nbsp;</td>
              </tr>
              <tr>
                <td valign="top">Page Introduction</td>
                <td valign="top"><label>
                  <textarea name="textarea" id="textarea" cols="45" rows="5"><%= cs.getBigString() %></textarea>
                  </label></td>
              </tr>
              </table>
            <p>

                ------------Ignore this stuff. it is work in progress-------------</p>
            <p>csid will not be null. Maybe we will have to look at the assignments? </p>
            <p>
            We need an 'actor already has this section at this phase' function.<br />
		    <label>
		    <input type="checkbox" name="checkbox" id="checkbox" />
		    </label>
		    Add this to actor <%= afso.actor_being_worked_on_id %> in phase <%= afso.phase_id %>              </p>
            <p>If this section has already been added, then just have 'save' and nothing else - so the section can't be added multiple times.</p>
            <p>------------------------------------------</p>
            <p> 
              <input type="hidden" name="custom_page" value="<%= afso.getMyPSO_SectionMgmt().get_custom_section_id() %>" />
              <input type="hidden" name="save_results" value="true" />
              <input type="hidden" name="sending_page" value="make_cast_page" />
              <input type="submit" name="save_page" value="Save" />
              <input type="submit" name="save_and_add" value="Save and Add Section" />
              </p>
            <p>&nbsp;</p>
          </blockquote>
      </form>      <a href="<%= afso.backPage %>"><img src="../../../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
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
