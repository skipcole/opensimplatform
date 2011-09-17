<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,java.text.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,
	org.usip.osp.communications.*" 
	errorPage="/error.jsp" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
		
	// Date expected in format similar to: "Oct 15 2009 00:00:00 GMT"
	SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss z");
	sdf.setTimeZone(TimeZone.getDefault());
	
	SimpleDateFormat short_sdf = new SimpleDateFormat("HH:mm");
	short_sdf.setTimeZone(TimeZone.getDefault());
	
	afso.handleCreateTimeLine(request);

	
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
            <input type="text" name="timeline_name" id="textfield" value="<%= afso.timelineOnScratchPad.getName() %>">
          </label>        </td>
      </tr>

      <tr>
            <td valign="top"><strong>Start Date: </strong></td>
            <td valign="top">
			<%
				SimpleDateFormat sdf_startdate = new SimpleDateFormat("MM/dd/yyyy");
				String start_date_formatted = sdf_startdate.format(afso.timelineOnScratchPad.getTimeline_start_date());
			%>
			<input name="timeline_start_date" type="text" id="datepicker" value="<%= start_date_formatted %>"></td>
      </tr>
          <tr>
            <td valign="top"><strong>Start Hour: </strong></td>
            <td valign="top"><label>
			<%
				SimpleDateFormat hour_sdf = new SimpleDateFormat("HH");
				
				String start_hour = hour_sdf.format(afso.timelineOnScratchPad.getTimeline_start_date());
				
String selected_0	 = "";
String selected_1	 = "";
String selected_2	 = "";
String selected_3	 = "";
String selected_4	 = "";
String selected_5	 = "";
String selected_6	 = "";
String selected_7	 = "";
String selected_8	 = "";
String selected_9	 = "";
String selected_10	 = "";
String selected_11	 = "";
String selected_12	 = "";
String selected_13	 = "";
String selected_14	 = "";
String selected_15	 = "";
String selected_16	 = "";
String selected_17	 = "";
String selected_18	 = "";
String selected_19	 = "";
String selected_20	 = "";
String selected_21	 = "";
String selected_22	 = "";
String selected_23	 = "";

	if (start_hour != null) {
		
		if (start_hour.equalsIgnoreCase("00")){
			selected_0 = " selected ";
		} else
		if (start_hour.equalsIgnoreCase("01")){
			selected_1 = " selected ";
		} else
		if (start_hour.equalsIgnoreCase("02")){
			selected_2 = " selected ";
		} else
		if (start_hour.equalsIgnoreCase("03")){
			selected_3 = " selected ";
		} else
		if (start_hour.equalsIgnoreCase("04")){
			selected_4 = " selected ";
		} else
		if (start_hour.equalsIgnoreCase("05")){
			selected_5 = " selected ";
		} else
		if (start_hour.equalsIgnoreCase("06")){
			selected_6 = " selected ";
		} else
		if (start_hour.equalsIgnoreCase("07")){
			selected_7 = " selected ";
		} else
		if (start_hour.equalsIgnoreCase("08")){
			selected_8 = " selected ";
		} else
		if (start_hour.equalsIgnoreCase("09")){
			selected_9 = " selected ";
		} else
		if (start_hour.equalsIgnoreCase("10")){
			selected_10 = " selected ";
		} else
		if (start_hour.equalsIgnoreCase("11")){
			selected_11 = " selected ";
		} else
		if (start_hour.equalsIgnoreCase("12")){
			selected_12 = " selected ";
		} else
		if (start_hour.equalsIgnoreCase("13")){
			selected_13 = " selected ";
		} else
		if (start_hour.equalsIgnoreCase("14")){
			selected_14 = " selected ";
		} else		
		if (start_hour.equalsIgnoreCase("15")){
			selected_15 = " selected ";
		} else
		if (start_hour.equalsIgnoreCase("16")){
			selected_16 = " selected ";
		} else
		if (start_hour.equalsIgnoreCase("17")){
			selected_17 = " selected ";
		} else
		if (start_hour.equalsIgnoreCase("18")){
			selected_18 = " selected ";
		} else
		if (start_hour.equalsIgnoreCase("19")){
			selected_19 = " selected ";
		} else
		if (start_hour.equalsIgnoreCase("20")){
			selected_20 = " selected ";
		} else
		if (start_hour.equalsIgnoreCase("21")){
			selected_21 = " selected ";
		} else
		if (start_hour.equalsIgnoreCase("22")){
			selected_22 = " selected ";
		} else
		if (start_hour.equalsIgnoreCase("23")){
			selected_23 = " selected ";
		}
		
	}
			%>
            <select name="timeline_start_hour">
              <option value="0" <%= selected_0 %>>12 AM</option>
              <option value="1" <%= selected_1 %>>1 AM</option>
              <option value="2" <%= selected_2 %>>2 AM</option>
              <option value="3" <%= selected_3 %>>3 AM</option>
              <option value="4" <%= selected_4 %>>4 AM</option>
              <option value="5" <%= selected_5 %>>5 AM</option>
              <option value="6" <%= selected_6 %>>6 AM</option>
              <option value="7" <%= selected_7 %>>7 AM</option>
              <option value="8" <%= selected_8 %>>8 AM</option>
              <option value="9" <%= selected_9 %>>9 AM</option>
              <option value="10" <%= selected_10 %>>10 AM</option>
              <option value="11" <%= selected_11 %>>11 AM</option>
              <option value="12" <%= selected_12 %>>12 PM</option>
              <option value="13" <%= selected_13 %>>1 PM</option>
              <option value="14" <%= selected_14 %>>2 PM</option>
              <option value="15" <%= selected_15 %>>3 PM</option>
              <option value="16" <%= selected_16 %>>4 PM</option>
              <option value="17" <%= selected_17 %>>5 PM</option>
              <option value="18" <%= selected_18 %>>6 PM</option>
              <option value="19" <%= selected_19 %>>7 PM</option>
              <option value="20" <%= selected_20 %>>8 PM</option>
              <option value="21" <%= selected_21 %>>9 PM</option>
              <option value="22" <%= selected_22 %>>10 PM</option>
              <option value="23" <%= selected_23 %>>11 PM</option>
            </select>
            </label></td>
          </tr>
          <tr>
            <td valign="top">&nbsp;</td>
            <td valign="top"><label>
            <%
				if (afso.timelineOnScratchPad.getId() == null) {
				%>
            <input type="submit" name="create_timeline" value="Create" />
            <%
				} else {
				%>
            <input type="hidden" name="t_id" value="<%= afso.timelineOnScratchPad.getId() %>" />
			    <table width="100%"><tr>
                <td align="left"><input type="submit"  name="update_timeline" tabindex="5" value="Update" /></td>
				<td align="right"><input type="submit" name="clear_button" tabindex="6" value="Clear to Create new Timeline" /></td>
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
          <table width="100%">
		  <tr>
              <td>Timeline Name </td>
              <td>Add an Existing Inject </td>
              <td>&nbsp;</td>
              <td>Add a New Event </td>
            </tr>
            <%
		List timelineList = TimeLine.getAllBaseForSimulation(afso.schema, afso.sim_id);
		
		for (ListIterator li = timelineList.listIterator(); li.hasNext();) {
			TimeLine tim = (TimeLine) li.next();

		%>
            
            <tr> 
              <td><a href="timeline_creator.jsp?queueup=true&t_id=<%= tim.getId().toString() %>"><%= tim.getName() %></a></td>
              <td><a href="timeline_inject_adder.jsp?queueup=true&timeline_id=<%= tim.getId().toString() %>">Add Inject </a></td>
              <td>&nbsp;</td>
              <td><a href="timeline_editor.jsp?timeline_id=<%= tim.getId().toString() %>">Add Events </a></td>
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