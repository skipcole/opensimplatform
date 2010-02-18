<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
		org.usip.osp.communications.*,
		org.usip.osp.networking.*,
		org.usip.osp.persistence.*,
		org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	Simulation sim = afso.handleMakeCaucusPage(request);
	
	if (afso.forward_on){
		afso.forward_on = false;
		response.sendRedirect(afso.backPage);
		return;
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Open Simulation Platform Control Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" --><!-- InstanceEndEditable -->
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
			<!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Make Caucus  Page</h1>
    <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" --> 
      <blockquote> 
	  <form action="customize_section_pages/core/make_conference_room.jsp" method="post" name="form2" id="form2">
        <blockquote><strong>Tab Heading</strong>: 
          <input type="text" name="tab_heading" value="<%= afso.tab_heading %>"/>
        <p>Select the Actors to be included in this Chat room. <br />
          If desired, assign them a designated role (such as participant, visitor, etc.) </p>
        <p><%
		
		MultiSchemaHibernateUtil.beginTransaction(afso.schema);
		
		Conversation conv = new Conversation();
		
		if (afso.sim_conv_id != null) {
			conv = (Conversation) MultiSchemaHibernateUtil.getSession(afso.schema).get(Conversation.class, afso.sim_conv_id);
		}	
		
		%>
		<table border="1"><tr><td><strong>Name</strong></td>
		<td><strong>Role</strong></td>
		<td><strong>Room Owner</strong></td>
		<td><strong>Initially Present</strong></td>
		<td><strong>Can be Added/Removed</strong></td>
		</tr>
		<%
			for (ListIterator la = sim.getActors().listIterator(); la.hasNext();) {
				Actor act = (Actor) la.next();
				String checked = "";
				if (conv.hasActor(act.getId())){
					checked = " checked ";
				}
			%>
          <tr><td><%= act.getActorName() %></td><td><label>
            <input type="text" name="textfield" />
          </label></td>
            <td><label>
              <input name="radiobutton" type="radio" value="radiobutton" />
            </label></td>
            <td><input type="checkbox" name="checkbox2" value="checkbox" /></td>
            <td><input type="checkbox" name="checkbox3" value="checkbox" /></td>
          </tr>
           
		<% } // End of loop over Actors 
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(afso.schema);
		%>
		</table>
	
        <p>
          Add Notice of Addition/Removal into the auto generated AAR?
            <label>
            <input name="radiobutton" type="radio" value="radiobutton" />
            Yes</label>
        / 
        <label>
        <input name="radiobutton" type="radio" value="radiobutton" />
        No</label>
        </p>
        <p>Enter any text that will appear on this page. <br>
        </p>
        <p>
            <textarea id="text_page_text" name="text_page_text" style="height: 110px; width: 710px;"><%= afso.customizableSectionOnScratchPad.getBigString() %></textarea>

		<script language="javascript1.2">
  			generate_wysiwyg('text_page_text');
		</script>
          </p>
          <p> 
            <input type="hidden" name="custom_page" value="<%= afso.getMyPSO_SectionMgmt().get_custom_section_id() %>" />
            <input type="hidden" name="sending_page" value="make_caucus_page" />
            <input type="submit" name="save_page" value="Save" />
            <input type="submit" name="save_and_add" value="Save and Add Section" />
          </p>
          <p><input type="submit" name="create_duplicate" value="Create Duplicate" disabled /></p>
        </blockquote>
      </form>
	  <a href="<%= afso.backPage %>"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a><!-- InstanceEndEditable -->
			</td>
		</tr>
		</table>
	</td>
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
<!-- InstanceEnd --></html>
<%
	
%>
