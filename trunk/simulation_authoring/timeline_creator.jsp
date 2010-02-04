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

	
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />   
        
    <link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>

    <body onLoad="onLoad();" onResize="onResize();">
        <% 
			if (afso.sim_id != null) {
		%>
    
        <h2>Create/Edit Timeline</h2>
        <blockquote>
        <form name="form1" method="post" action="">
    <table width="80%" border="1" cellspacing="0" cellpadding="0">
      <tr>
        <td><strong>Name:</strong></td>
        <td>
          <label>
            <input type="text" name="textfield" id="textfield">
          </label>
        
        </td>
      </tr>
          <tr>
            <td valign="top"><strong>Start Day</strong></td>
            <td valign="top"><label>
            <select name="select2" id="start_day">
              <% for(int ii = 1 ; ii <= 31 ; ++ii) { %>
              <option value="<%= ii %>"><%= ii %></option>
              <% } %>
            </select>
            </label></td>
      </tr>
                    <tr>
            <td valign="top"><strong>Start Month</strong></td>
            <td valign="top"><label>
            <select name="start_month" id="start_month">
              <option value="1">January</option>
            </select>
              </label></td>
      </tr>          <tr>
            <td valign="top"><strong>Start Year</strong></td>
            <td valign="top"><input type="text" name="textfield3" id="textfield3"></td>
          </tr>
          <tr>
            <td valign="top"><strong>Hour</strong></td>
            <td valign="top"><label>
<input type="text" name="event_hour" id="event_hour" value="<%= event.getEventStartHour() %>">            
(24 Hour Clock)</label></td>
          </tr>
          <tr>
            <td valign="top"><strong>Minute</strong></td>
            <td valign="top"><label>
              <input type="text" name="event_minute" id="event_minute" value="<%= event.getEventStartMinute() %>">
            </label></td>
          </tr>
          <tr>
            <td valign="top">&nbsp;</td>
            <td valign="top"><label>
            <%
				if (event.getId() == null) {
				%>
            <input type="submit" name="command" value="Create" />
            <%
				} else {
				%>
            <input type="hidden" name="event_id" value="<%= event.getId() %>" />
            <input type="submit" name="command" value="Clear" tabindex="6" />
            <input type="submit" name="command" value="Update" />
            <%
					}
				%>
</label></td>
          </tr>
    </table>
    </form>
    </blockquote>
      <blockquote>
        <p>&nbsp;</p>
          <p>Below are listed all the TimeLines for this simulation.</p>
          <table>
            <%
		List timelineList = TimeLine.getAllForSimulation(afso.schema, afso.sim_id);
		
		for (ListIterator li = timelineList.listIterator(); li.hasNext();) {
			TimeLine sim = (TimeLine) li.next();

		%>
            <tr> 
              <td><a href="timeline_creator.jsp?command=Edit&sim_id=<%= TimeLine.getId().toString() %>"><%= TimeLine.getName() %></a></td>
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