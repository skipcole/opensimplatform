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
          </label>
        
        </td>
      </tr>

      <tr>
            <td valign="top"><strong>Start Month</strong></td>
            <td valign="top"><label>
            <select name="start_month" id="start_month">
              <option value="1">January</option>
            </select>
              </label></td>
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
            <td valign="top"><strong>Start Year</strong></td>
            <td valign="top"><input name="start_year" type="text" id="textfield3" size="4"></td>
          </tr>
          <tr>
            <td valign="top"><strong>Hour</strong></td>
            <td valign="top"><label>
            <select name="select" id="select">
  <option value="0">12 am</option>
  <option value="1">1 am</option>
  <option value="2">2 am</option>
  <option value="3">3 am</option>
  <option value="4">4 am</option>
  <option value="5">5 am</option>
  <option value="6">6 am</option>
  <option value="7">7 am</option>
  <option value="8">8 am</option>
  <option value="9">9 am</option>
  <option value="10">10 am</option>
  <option value="11">11 am</option>
  <option value="12">12 pm</option>
  <option value="13">1 pm</option>
  <option value="14">2 pm</option>
  <option value="15">3 pm</option>
  <option value="16">4 pm</option>
  <option value="17">5 pm</option>
  <option value="18">6 pm</option>
  <option value="19">7 pm</option>
  <option value="20">8 pm</option>
  <option value="21">9 pm</option>
  <option value="22">10 pm</option>
  <option value="23">11 pm</option>
</select>
            </label></td>
          </tr>
          <tr>
            <td valign="top"><strong>Minute</strong></td>
            <td valign="top"><label>
              <input name="event_minute" type="text" id="event_minute" value="" size="2">
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
            <input type="submit" name="command" value="Clear" tabindex="6" />
            <input type="submit" name="update_timeline" value="Update" />
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