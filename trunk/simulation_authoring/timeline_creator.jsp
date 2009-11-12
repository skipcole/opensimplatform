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
	
	Simulation simulation = new Simulation();
	if (afso.sim_id != null) {
		simulation = afso.giveMeSim();
		run_start = sdf.format(simulation.getPhaseStartTime(afso.schema, simulation.getFirstPhaseId(afso.schema)));
		
		if (afso.phase_id == null) {
			afso.phase_id = simulation.getFirstPhaseId(afso.schema);
		}
	}

	Event event = afso.handleTimeLineCreator(request);
			
	
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
        <script>Timeline_urlPrefix = "http://static.simile.mit.edu/timeline/api-2.3.0/";;</script>
        <script src="http://static.simile.mit.edu/timeline/api-2.3.0/timeline-api.js?bundle=true" type="text/javascript"></script>
        <script>
        var tl;
        function onLoad() {
        var eventSource = new Timeline.DefaultEventSource();
        var bandInfos = [
        Timeline.createBandInfo({
			timeZone:       -5,
             eventSource:    eventSource,
            date:           "<%= run_start %>",
            width:          "80%", 
            intervalUnit:   Timeline.DateTime.HOUR, 
            intervalPixels: 100
        }),
        Timeline.createBandInfo({
			timeZone:       -5,
            overview:       true,
            eventSource:    eventSource,
            date:           "<%= run_start %>",
            width:          "20%", 
            intervalUnit:   Timeline.DateTime.DAY, 
            intervalPixels: 200
        })
        ];
        bandInfos[1].syncWith = 0;
        bandInfos[1].highlight = true;
        tl = Timeline.create(document.getElementById("my-timeline"), bandInfos);
        Timeline.loadXML("timeline_server.jsp", function(xml, url) { eventSource.loadXML(xml, url); });
        }

        var resizeTimerID = null;
        function onResize() {
            if (resizeTimerID == null) {
                resizeTimerID = window.setTimeout(function() {
                    resizeTimerID = null;
                    tl.layout();
                }, 500);
            }
        }
        </script>     
        
    <link href="../usip_osp.css" rel="stylesheet" type="text/css" />  
    
    
    </head>

    <body onLoad="onLoad();" onResize="onResize();">
        <% 
			if (afso.sim_id != null) {
		%>
        <div id="my-timeline" style="height: 300px; border: 1px solid #aaa"></div>
        <hr>
        <h2>Add Event
        </h2>
        <blockquote>
<form name="form1" method="post" action="timeline_creator.jsp">
        <input type="hidden" name="sending_page" value="timeline_creator">
        
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td valign="top">Title</td>
            <td valign="top">
              <label>
                <input type="text" name="event_title" id="event_title" value="<%= event.getEventTitle() %>">
              </label>            </td>
          </tr>
          <tr>
            <td valign="top">Text</td>
            <td valign="top"><label>
              <textarea name="event_text" id="event_text" cols="45" rows="2"><%= event.getEventMsgBody() %></textarea>
            </label></td>
          </tr>
          <tr>
            <td valign="top">Date</td>
            <td valign="top">for now will be 1 Jan 2009</td>
          </tr>
          <tr>
            <td valign="top">Hour</td>
            <td valign="top"><label>
              <input type="text" name="event_hour" id="event_hour">
            </label></td>
          </tr>
          <tr>
            <td valign="top">Minute</td>
            <td valign="top"><label>
              <input type="text" name="event_minute" id="event_minute">
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
        <h2>Edit/Delete Events</h2>
        <blockquote>
        <table width="80%">
        <%
		for (ListIterator li = Event.getAllForSim(afso.sim_id, afso.phase_id, afso.schema).listIterator(); li.hasNext();) {
			Event event_l = (Event) li.next();
			%>
            <tr>
              <td width="60%" valign="top"><%= event_l.getEventTitle() %></td>
              <td width="20%" valign="top"><%= short_sdf.format(event_l.getEventStartTime()) %>
             </td>
            <td width="10%" valign="top"><a href="timeline_creator.jsp?remove_event=true&event_id=<%= event_l.getId() %>">Remove </a></td>
            <td width="10%" valign="top"><a href="timeline_creator.jsp?edit_event=true&event_id=<%= event_l.getId() %>">Edit </a></td>
            </td></tr> 
			<% } %>
        </table>
        <p>&nbsp; </p>
        </blockquote>
        
      <% } else { // End of if have set simulation id. %>
      <blockquote>
        <p>
          <%@ include file="select_message.jsp" %></p>
      </blockquote>
      <% } // End of if have not set simulation for edits. %>      
        
        
<noscript>
        This page uses Javascript to show you a Timeline. Please enable Javascript in your browser to see the full page. Thank you.
        </noscript>
    </body>

 </html>