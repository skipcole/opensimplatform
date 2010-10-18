<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,java.text.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,
	org.usip.osp.communications.*" 
	errorPage="" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
		
	// Date expected in format similar to: "Oct 15 2009 00:00:00 GMT"
	SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss z");
	sdf.setTimeZone(TimeZone.getDefault());
	
	SimpleDateFormat short_sdf = new SimpleDateFormat("HH:mm");
	short_sdf.setTimeZone(TimeZone.getDefault());
	
	String run_start = sdf.format(new java.util.Date());
	
	TimeLine timeline = afso.handleCreateTimeLine(request);

	
%>
<html>
    <head>
		<link type="text/css" href="../third_party_libraries/jquery/jquery-ui-1.8.4/development-bundle/themes/cupertino/jquery.ui.all.css" rel="stylesheet" />
	<script type="text/javascript" src="../third_party_libraries/jquery/jquery-ui-1.8.4/js/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="../third_party_libraries/jquery/jquery-ui-1.8.4/development-bundle/ui/jquery.ui.core.js"></script>
	<script type="text/javascript" src="../third_party_libraries/jquery/jquery-ui-1.8.4/development-bundle/ui/jquery.ui.widget.js"></script>
	<script type="text/javascript" src="../third_party_libraries/jquery/jquery-ui-1.8.4/development-bundle/ui/jquery.ui.datepicker.js"></script>
	<script type="text/javascript">
	$(function() {
		$("#datepicker").datepicker();
		$('#dateselector').datepicker("setDate", new Date(2000,1,01) );

	});
	</script>

        <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />   
        
    <link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>

    <body onLoad="onLoad();" onResize="onResize();">
        <% 
			if (afso.sim_id != null) {
		%>
    
        <h2>Create/Edit Timeline</h2>
        <blockquote>
        <form name="form1" method="post" action="timeline_creator.jsp">
    <table width="80%" border="1" cellspacing="0" cellpadding="0">
      <tr>
        <td><strong>Name:</strong></td>
        <td>
          <label>
            <input type="text" name="timeline_name" id="textfield" value="<%= timeline.getName() %>">
          </label>        </td>
      </tr>

      <tr>
            <td valign="top"><strong>Start Date: </strong></td>
            <td valign="top"><input name="timeline_start_date" type="text" id="datepicker" value="10/24/2010"></td>
      </tr>
          <tr>
            <td valign="top"><strong>Start Hour: </strong></td>
            <td valign="top"><label>
            <select name="timeline_start_hour">
              <option value="8">8 AM</option>
              <option value="9" selected>9 AM</option>
              <option value="10">10 AM</option>
              <option value="11">11 AM</option>
              <option value="12">12 PM</option>
              <option value="13">1 PM</option>
              <option value="14">2 PM</option>
              <option value="15">3 PM</option>
              <option value="16">4 PM</option>
              <option value="17">5 PM</option>
              <option value="18">6 PM</option>
              <option value="19">7 PM</option>
              <option value="20">8 PM</option>
              <option value="21">9 PM</option>
              <option value="22">10 PM</option>
              <option value="23">11 PM</option>
              <option value="0">12 AM</option>
              <option value="1">1 AM</option>
              <option value="2">2 AM</option>
              <option value="3">3 AM</option>
              <option value="4">4 AM</option>
              <option value="5">5 AM</option>
              <option value="6">6 AM</option>
              <option value="7">7 AM</option>
              <option value="8">8 AM</option>
            </select>
            </label></td>
          </tr>
          <tr>
            <td valign="top">&nbsp;</td>
            <td valign="top"><label>
            <%
				if (timeline.getId() == null) {
				%>
            <input type="submit" name="create_timeline" value="Create" />
            <%
				} else {
				%>
            <input type="hidden" name="t_id" value="<%= timeline.getId() %>" />
			    <table width="100%"><tr>
                <td align="left"><input type="submit"  name="update_timeline" tabindex="5" value="Update" /></td>
				<td align="right"><input type="submit" name="command" tabindex="6" value="Clear to Create new Timeline" /></td>
				</tr></table>
            <%
					}
				%>
</label></td>
          </tr>
    </table>
    <input type="hidden" name="sending_page" value="make_create_timeline_page" />
    </form>
    </blockquote>
      <blockquote>
        <p>&nbsp;</p>
          <p>Below are listed all the TimeLines for this simulation.</p>
          <table>
            <%
		List timelineList = TimeLine.getAllBaseForSimulation(afso.schema, afso.sim_id);
		
		for (ListIterator li = timelineList.listIterator(); li.hasNext();) {
			TimeLine tim = (TimeLine) li.next();

		%>
            <tr> 
              <td><a href="timeline_creator.jsp?queueup=true&t_id=<%= tim.getId().toString() %>"><%= tim.getName() %></a></td>
              <td>&nbsp;</td>
              <td></td>
            </tr>
            <%
	}
%>
        </table>
      </blockquote>      
    <blockquote>
    </blockquote>
        
      <% } else { // End of if have set simulation id. %>
      <blockquote>
        <p>
          <%@ include file="select_message.jsp" %></p>
      </blockquote>
      <% } // End of if have not set simulation for edits. %>      
        
    </body>

 </html>